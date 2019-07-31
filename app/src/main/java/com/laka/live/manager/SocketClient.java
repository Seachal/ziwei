package com.laka.live.manager;

import com.laka.live.BuildConfig;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by heller on 16/3/23.
 */
public class SocketClient implements Runnable {

    public interface SocketListener {
        /**
         * Socket连接成功
         */
        void didConnect();

        /**
         * 断开连接
         */
        void didDisconnect();

        /**
         * 读取到数据,数据需要拷贝一份再做处理
         *
         * @param inputStream
         */
        void didReadData(BufferedInputStream inputStream) throws IOException;

        /**
         * 发生错误
         *
         * @param code
         * @param message
         */
        void didErroOccur(int code, String message);
    }

    public final static int ERROR_RECONNECT = 1000;
    public final static int ERROR_SERVER_DISCONNECT = 1001;
    public final static int ERROR_READTIMEOUT = 1002;
    public final static int ERROR_WRITETIMEOUT = 1003;


    // 超时间隔
    private final static int CONNECT_TIMEOUT = 30000;
    private final static int RECONNECT_TIME = 1000 * 3;//10000
    private final static int READ_TIMEOUT = 90000;
    private final static int WRITE_TIMEOUT = 10000;


    private final static String TAG = "SocketClient";

//    public final byte STATE_CONNECTING = 1;
//    public final byte STATE_RUNNING = 1 << 1;
//    public final byte STATE_CLOSED = 1 << 2;

    public enum ConnectState {
        /**
         * 未连接
         */
        DISCONNECT,
        /**
         * 连接中
         */
        CONNECTING,
        /**
         * 已经连接了
         */
        CONNECTED,
    }

    //服务是否连接中
    private ConnectState mConnectState = ConnectState.DISCONNECT;

    private String mIPAddress = null;
    private int mPort = -1;
    private Socket mSocket = null;
    //    private Selector mSelector = null;
    private WriteThread mWriteThread = null;
    private final BlockingQueue<ByteBuffer> mSendQueuenew = new PriorityBlockingQueue<>();

    //    public byte STATE = STATE_CONNECTING;
    private boolean onWork = true;// 是否工作状态

    private final Object lock = new Object();

    private SocketListener mListener = null;

    private BufferedInputStream mBIn;

    public SocketClient(String IPAddress, int port) {
        this.mIPAddress = IPAddress;
        this.mPort = port;
    }

    @Override
    public void run() {
        while (onWork) {
            switch (mConnectState) {
                case DISCONNECT:
                    connect();
                    break;
                case CONNECTED:
                    read();
                    break;
                default:
                    break;
            }
        }

        closeSocket();


        if (null != mListener) {
            mListener.didDisconnect();
        }
    }

    private class WriteThread extends Thread {
        @Override
        public void run() {

            while (onWork) {
                try {
                    ByteBuffer buffer = mSendQueuenew.take();
                    writeBuffer(buffer);

                } catch (InterruptedException e) {
                    Log.error(TAG, "ex ", e);
                } catch (IOException e) {
                    Log.error(TAG, "ex ", e);
                } catch (NotYetConnectedException e) {
                    Log.error(TAG, "ex ", e);
                }
            }

            mSendQueuenew.clear();
        }
    }

    private void writeBuffer(ByteBuffer buffer) throws IOException, NotYetConnectedException {

        buffer.flip();
        synchronized (lock) {
            if (mSocket != null)//防止NullPointerException
                mSocket.getOutputStream().write(buffer.array());
        }
    }

    public void write(ByteBuffer buffer) {
        mSendQueuenew.add(buffer);
    }

    public void setListener(SocketListener mListener) {
        this.mListener = mListener;
    }

    public void startWork() {
        onWork = true;
    }

    public void stopWork() {
        onWork = false;

        if (null != mWriteThread && mWriteThread.isAlive()) {
            mWriteThread.interrupt();
            mWriteThread = null;
        }
    }


    // 异常情况断线重连
    private void waitReconnect(String message) {
        if (mConnectState == ConnectState.DISCONNECT) {
            Log.d(TAG, "waitReconnect 不需要 DISCONNECT");
            return;
        }

        StringBuffer buffer = new StringBuffer(message);
        buffer.append(RECONNECT_TIME / 1000);
        buffer.append("秒后再尝试连接");

        if (null != mListener)
            mListener.didErroOccur(ERROR_RECONNECT, buffer.toString());

        closeKey();// 关闭通道
        Wait(RECONNECT_TIME);
        mConnectState = ConnectState.DISCONNECT;
    }

    // 连接服务器
    private void connect() {
        long start = 0;
        long end = 0;
        try {
//            mSelector = Selector.open();

            start = System.currentTimeMillis();
            mSocket = new Socket();
            mConnectState = ConnectState.CONNECTING;
            mSocket.connect(new InetSocketAddress(mIPAddress, mPort), CONNECT_TIMEOUT);
            end = System.currentTimeMillis();

            // More info:
            // http://groups.google.com/group/android-developers/browse_thread/thread/45a8b53e9bf60d82
            // http://stackoverflow.com/questions/2879455/android-2-2-and-bad-address-family-on-socket-connect
            System.setProperty("java.net.preferIPv4Stack", "true");
            System.setProperty("java.net.preferIPv6Addresses", "false");

            mSocket.setSoTimeout(READ_TIMEOUT);
//            mSocket.register(mSelector, SelectionKey.OP_READ);

            if (mSocket.isConnected()) {

                mConnectState = ConnectState.CONNECTED;
                InputStream inputStream = mSocket.getInputStream();
                mBIn = new BufferedInputStream(inputStream);

                // 连接成功开始监听服务端消息
                // 发送一个验证数据包到服务器进行验证
                if (null != mListener)
                    mListener.didConnect();

                if (mWriteThread == null || mWriteThread.isInterrupted()) {
                    mWriteThread = new WriteThread();
                    mWriteThread.start();
                }

            } else {
                // 重连
                // 关闭通道过10S重新开始连接
                Log.d(TAG, "服务器连接失败,3秒后重试");
                EventBusManager.postEvent("服务器连接失败,请检查您的网络", SubcriberTag.SOCKET_ERROR_TIPS);
                waitReconnect("服务器连接失败");
            }
        } catch (Exception e) {
            end = System.currentTimeMillis();
            // 重连
            // 有异常关闭通道过10S重新开始连接
            Log.e(TAG, "连接出错,3秒后重试", e);
            EventBusManager.postEvent("服务器连接失败,请检查您的网络", SubcriberTag.SOCKET_ERROR_TIPS);
            waitReconnect("连接出错");
        } finally {
            reportSocketConnect(end - start);
        }
    }

    private void closeSocket() {
        if (mSocket != null) {
            synchronized (mSocket) {
                try {
                    if (mSocket != null) {
                        mSocket.close();
                        mSocket = null;
                    }
                    Log.d(TAG, "mSocket重置");
                } catch (Exception e) {
                    Log.error(TAG, "ex ", e);
                } finally {
                    mConnectState = ConnectState.DISCONNECT;
                }

            }
        }
    }

    public void closeKey() {
        if (mBIn != null) {
            try {
                mBIn.close();
            } catch (IOException e1) {
                Log.error(TAG, "ex", e1);
            }
        }
        closeSocket();
    }

    private void read() {

        if (mSocket == null) {
            return;
        }
//        SocketChannel client = (SocketChannel) selectionKey.channel();
        if (null != mListener) {
            try {
                mListener.didReadData(mBIn);
            } catch (IOException e) {
                if (onWork) {
                    waitReconnect("IOException");
                }
                Log.error(TAG, "didReadData IOException : ", e);
            }
            /*if (result <= 0) {
                Log.d(TAG, "result = " + result + " <= 0 重连");
                waitReconnect("读取到的数据异常 ");
            }*/
        }
        // 如果缓冲区过小的话那么信息流会分成多次接收
        /*int actual = client.read(readBuffer);
        if (actual > 0) {

            readBuffer.flip();

            //TODO: process data
            if (null != mListener)
                mListener.didReadData(client);

            readBuffer.clear();// 清空
        } else {
            Log.d(TAG, "actual==0重连");
            stopWork();
            connect();
        }*/
    }

    // 阻塞Socket线程,N秒后再重试
    private synchronized void Wait(long millis) {
        try {
            wait(millis);
        } catch (InterruptedException e) {
            Log.error(TAG, "Wait : ", e);
        }
    }

    public ConnectState getConnectState() {
        return mConnectState;
    }

    public static void reportSocketConnect(long usedTime) {
        if (BuildConfig.DEBUG) {
            return;
        }
        Log.error(TAG, "reportSocketConnect usedTime : " + usedTime);
        if (usedTime <= 500) {
            return;
        }
        String eventId = new StringBuilder(AnalyticsReport.DEFAULT_VIEW_ID)
                .append(AnalyticsReport.SEPARATOR)
                .append("15451")
                .append(AnalyticsReport.SEPARATOR)
                .append(AnalyticsReport.NETWORK_ENVIRONMENT_MONITORING).toString();
        AnalyticsReport.onEventValue(LiveApplication.getInstance(), eventId, null, (int) usedTime);
    }
}
