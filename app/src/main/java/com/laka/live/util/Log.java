package com.laka.live.util;

import android.os.Environment;
import android.os.SystemClock;

import com.android.volley.VolleyLog;
import com.laka.live.BuildConfig;
import com.laka.live.application.LiveApplication;
import com.laka.live.constants.Constants;
import com.orhanobut.logger.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luwies on 16/3/4.
 */
public class Log {

    private static final String PACKAGE_PATH = "data/data/" + Constants.PACKAGE_NAME + "/";
    private static final String FILE_DIR = LiveApplication.getInstance().getExternalFilesDir(null)
            .getAbsolutePath() + "/ZiWeiLive/";
    private static final String LOG_DIR = "CommonLog/";
    private static final String VIDEO_LOG = "videoInfo/";
    private static final String FILE_NAME = "VideoInfo";
    private static final String FILE_NAME_SUFFIX = ".log";

    public final static String TAG = "DEBUG";

    public static void log(String msg) {
        debug(TAG, msg);
    }

    public static void log(String TAG, String msg) {
        debug(TAG, msg);
    }

    public static void verbose(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void info(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void error(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void error(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, msg, tr);
        }
    }

    public static void s(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            System.out.print(tag + "," + msg);
        }
    }

    public static void v(String tag, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(tag, buildMessage(format, args));
        }
    }

    public static void d(String tag, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, buildMessage(format, args));
        }
    }

    public static void e(String tag, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, buildMessage(format, args));
        }
    }

    public static void e(String tag, Throwable tr, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, buildMessage(format, args), tr);
        }
    }

    /**
     * 记录视频播放异常信息
     *
     * @param ex
     * @return
     * @throws IOException
     */
    public static void dumpVideoExceptionToSdCard(final Throwable ex) throws IOException {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }

        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                try {
                    File dir = new File(FILE_DIR + LOG_DIR + VIDEO_LOG);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    } else {
                        //存在则清除一个星期之前的VideoLog
                        //IOUtil.delAllFiles(dir.getAbsolutePath());
                    }
                    Logger.e("当前文件夹：" + dir.getAbsolutePath());

                    long current = System.currentTimeMillis();
                    String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(current));
                    File file = new File(dir, FILE_NAME + time + FILE_NAME_SUFFIX);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    Logger.e("创建当前文件：" + file.getAbsolutePath());

                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                    pw.println(time);
                    SystemUtil.dumpPhoneInfo(pw);
                    pw.println();
                    pw.println();
                    pw.print("Message：" + ex.getMessage());
                    pw.println();
                    pw.print("Cause：" + ex.getCause());
                    pw.println();
                    pw.print("LocalMessage：" + ex.getLocalizedMessage());
                    pw.println();
                    ex.printStackTrace(pw);
                    pw.close();
                } catch (Exception ex) {
                    Log.e(TAG, "dump crash info failed");
                }
            }
        });

        observable.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of VolleyLog.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(VolleyLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg);
    }

    /**
     * A simple event log with records containing a name, thread ID, and timestamp.
     */
    public static class MarkerLog {

        private String tag;

        public MarkerLog(String tag) {
            this.tag = tag;
        }

        /**
         * Minimum duration from first marker to last in an marker log to warrant logging.
         */
        private static final long MIN_DURATION_FOR_LOGGING_MS = 0;

        private static class Marker {
            public final String name;
            public final long thread;
            public final long time;

            public Marker(String name, long thread, long time) {
                this.name = name;
                this.thread = thread;
                this.time = time;
            }
        }

        private final List<Marker> mMarkers = new ArrayList<Marker>();
        private boolean mFinished = false;

        public void add(String name) {
            add(name, Thread.currentThread().getId());
        }

        /**
         * Adds a marker to this log with the specified name.
         */
        public synchronized void add(String name, long threadId) {
            if (BuildConfig.DEBUG) {
                if (mFinished) {
//                    throw new IllegalStateException("Marker added to finished log");
                    Log.error(tag, "Marker added to finished log");
                }

                mMarkers.add(new Marker(name, threadId, SystemClock.elapsedRealtime()));
            }
        }

        /**
         * Closes the log, dumping it to logcat if the time difference between
         * the first and last markers is greater than {@link #MIN_DURATION_FOR_LOGGING_MS}.
         *
         * @param header Header string to print above the marker log.
         */
        public synchronized void finish(String header) {

            mFinished = true;

            if (BuildConfig.DEBUG) {
                long duration = getTotalDuration();
                if (duration <= MIN_DURATION_FOR_LOGGING_MS) {
                    return;
                }

                long prevTime = mMarkers.get(0).time;
                d(tag, "(%-4d ms) %s", duration, header);
                for (Marker marker : mMarkers) {
                    long thisTime = marker.time;
                    d(tag, "(+%-4d) [%2d] %s", (thisTime - prevTime), marker.thread, marker.name);
                    prevTime = thisTime;
                }
            }
        }

        @Override
        protected void finalize() throws Throwable {
            // Catch requests that have been collected (and hence end-of-lifed)
            // but had no debugging output printed for them.
            if (!mFinished) {
                finish("Request on the loose");
                e(tag, "Marker log finalized without finish() - uncaught exit point for request");
            }
        }

        /**
         * Returns the time difference between the first and last events in this log.
         */
        private long getTotalDuration() {
            if (mMarkers.size() == 0) {
                return 0;
            }

            long first = mMarkers.get(0).time;
            long last = mMarkers.get(mMarkers.size() - 1).time;
            return last - first;
        }
    }

}
