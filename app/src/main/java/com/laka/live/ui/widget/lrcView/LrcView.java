package com.laka.live.ui.widget.lrcView;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;


import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ss on 2015/8/7.
 */
public class LrcView extends View implements EventBusManager.OnEventBusListener {
    private static final int[] miarry = {64, 71, 97, 119, 94, 50, 116, 71, 81, 54, 49, 45, 206, 210, 110, 105};//解密的key
    private static final String TAG = "LrcView";

    private List<KrcLine> mKrcLineList = new ArrayList<KrcLine>();//存放歌词

    private int mViewWidth; //view的宽度
    private int mLrcHeight; //lrc界面的高度
    private int mRows;      //多少行
    private int mCurrentLine = 0; //当前行
    private int mLastLine = 0; //上行
    private float mTextSize; //字体
    private float mDividerHeight; //行间距

    private Paint mNormalPaint; //常规的字体
    private Paint mCurrentPaint; //当前歌词的大小
    private Paint mCurrentNormalPaint; //常规的字体
    private Bitmap mBackground = null;

    private float float1 = 0.0f;//渲染百分比
    private float float2 = 0.01f;
    private boolean isChanging = false;//视图是否正在更新
    private int normalTextColor;//歌词颜色
    private int currentTextColor;//当前行选中歌词颜色
    private int currentNormalTextColor;//当前未选中歌词颜色
    private int lrcTime = 0;
    private boolean isStop = false;
    private String maxLineStr = "";// 最长的行
    private float mCurTextSize;

    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    // 初始化操作
    private void initViews(AttributeSet attrs) {
        EventBusManager.register(this);
        // <begin>
        // 解析自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.Lrc);
        mTextSize = ta.getDimension(R.styleable.Lrc_textSize, 50.0f);
        mRows = ta.getInteger(R.styleable.Lrc_rows, 5);
        mDividerHeight = ta.getDimension(R.styleable.Lrc_dividerHeight, 0.0f);

        normalTextColor = ta.getColor(R.styleable.Lrc_normalTextColor,
                Color.WHITE);
        currentTextColor = ta.getColor(R.styleable.Lrc_currentTextColor,
                Color.YELLOW);
        currentNormalTextColor = ta.getColor(R.styleable.Lrc_currentNormalColor,
                Color.WHITE);
        ta.recycle();
        // </end>
        // 计算krc面板的高度
        mLrcHeight = (int) (mTextSize + mDividerHeight) * mRows + 5;//
        mNormalPaint = new Paint();
        mCurrentPaint = new Paint();
        // 初始化paint
        mNormalPaint.setTextSize(mTextSize);
        mNormalPaint.setColor(normalTextColor);
        mCurrentPaint.setTextSize(mTextSize);
        //mCurrentPaint.setColor(currentTextColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 获取view的高度
        mViewWidth = getMeasuredWidth();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 重新设置view的高度
        int measuredHeight = MeasureSpec.makeMeasureSpec(mLrcHeight, MeasureSpec.AT_MOST);
        setMeasuredDimension(widthMeasureSpec, measuredHeight);
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    //关键代码
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mKrcLineList.isEmpty()) {
            return;
        }

        canvas.save();
        //圈出可视区域
        canvas.clipRect(0, 5, mViewWidth, mLrcHeight);//5解决上面有一些字

        if (null != mBackground) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(mBackground, mViewWidth, mLrcHeight, true),
                    new android.graphics.Matrix(), null);
        }
//      Log.d(TAG," ondraw mCurrentLine="+mCurrentLine);

        //将画布上移
        canvas.translate(0, -((mCurrentLine - (mRows + 1) / 2) * (mTextSize + mDividerHeight)));

        //画当前行上面的
        for (int i = mCurrentLine - 1; i >= 0; i--) {
            String lrc = mKrcLineList.get(i).lineStr;
            float x = (mViewWidth - mNormalPaint.measureText(lrc)) / 2;

            canvas.drawText(lrc, x, (mTextSize + mDividerHeight) * i, mNormalPaint);
        }

        String currentLrc = mKrcLineList.get(mCurrentLine).lineStr;
//        float currentX = (mViewWidth - mCurrentPaint.measureText(currentLrc)) / 2;//改为左对齐
        float currentX = 0;
        // 获得字符串的"长度"
        float len = mCurrentPaint.getTextSize() * currentLrc.length();
        // 参数color数组表示参与渐变的集合
        // 参数float数组表示对应颜色渐变的位置
        int[] a = new int[]{currentTextColor, currentNormalTextColor};
        float[] f = new float[]{float1, float2};
        Shader shader = new LinearGradient(currentX,
                (mTextSize + mDividerHeight) * mCurrentLine,
                len + currentX,
                (mTextSize + mDividerHeight) * mCurrentLine,
                a, f, Shader.TileMode.CLAMP);
        mCurrentPaint.setShader(shader);
        // 画当前行
        canvas.drawText(currentLrc, currentX, (mTextSize + mDividerHeight) * mCurrentLine, mCurrentPaint);

        // 画当前行下面的
        for (int i = mCurrentLine + 1; i < mKrcLineList.size(); i++) {
            String lrc = mKrcLineList.get(i).lineStr;
//            float x = (mViewWidth - mNormalPaint.measureText(lrc)) / 2;//改为左对齐
            float x = 0;//改为左对齐
            canvas.drawText(lrc, x, (mTextSize + mDividerHeight) * i, mNormalPaint);
        }

        canvas.restore();
    }

    //    public String testPath = "test.krc";
    public String testPath = "geci.txt";

    //解密krc数据
    public String getKrcText(String filenm) throws IOException {
        AssetManager a = getResources().getAssets();
        AssetFileDescriptor af = a.openFd(filenm);
        byte[] zip_byte = new byte[(int) af.getLength()];
        FileInputStream fileinstrm = af.createInputStream();
//        File krcfile = new File(filenm);
//        byte[] zip_byte = new byte[(int) krcfile.length()];
//        FileInputStream fileinstrm = new FileInputStream(krcfile);
        byte[] top = new byte[4];
        fileinstrm.read(top);
        fileinstrm.read(zip_byte);
        int j = zip_byte.length;
        for (int k = 0; k < j; k++) {
            int l = k % 16;
            int tmp67_65 = k;
            byte[] tmp67_64 = zip_byte;
            tmp67_64[tmp67_65] = (byte) (tmp67_64[tmp67_65] ^ miarry[l]);
        }
        String krc_text = new String(ZLibUtils.decompress(zip_byte), "utf-8");
        return krc_text;
    }

    public String getKrcLines(String filenm) throws Exception {
//        AssetManager a = getResources().getAssets();
//        InputStream is =a.open(filenm);
        File f = new File(filenm);
        String text = "";
        if (!f.exists() || f.length() == 0) {
            text = ResourceHelper.getString(R.string.no_lrc);
        } else {
            text = readTextFromSDcard(new FileInputStream(f));
        }

        return text;
    }

    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    // 外部提供方法
    // 设置krc路径
    public void setKrcPath(String path) throws Exception {
        mKrcLineList.clear();
        maxLineStr = "";
        mNormalPaint.setTextSize(mTextSize);
        mCurrentPaint.setTextSize(mTextSize);
        //解密解压
//        String lines = getKrcText(path);
        String lines = getKrcLines(path);
//        Log.d(TAG, "line=" + lines);
        //
        String lineArray[] = lines.split("\n");
//        String lineArray[] = lines.split("\r\n");
//        if(lineArray.length<=10){
//            return;
//        }

        //逐行解析
        for (int i = 0; i < lineArray.length; i++) {
            KrcLine krcLine = ParseLine(lineArray[i]);
            if (null != krcLine) {
                mKrcLineList.add(krcLine);
            }
        }

        mCurTextSize = mTextSize;
        //计算字体大小,适配屏幕宽度
        float textLength = mNormalPaint.measureText(maxLineStr);
        Log.d(TAG, "maxLineStr=" + maxLineStr + " textLength=" + textLength + " viewWidth = " + getMeasuredWidth()
                + " mCurTextSize=" + mCurTextSize);
        while (textLength > getMeasuredWidth()) {
            mCurTextSize--;
            mNormalPaint.setTextSize(mCurTextSize);
            textLength = mNormalPaint.measureText(maxLineStr);
            Log.d(TAG, " mCurTextSize=" + mCurTextSize + " textLength=" + textLength + " viewWidth = " + getMeasuredWidth());
        }

        mNormalPaint.setTextSize(mCurTextSize);
        mCurrentPaint.setTextSize(mCurTextSize);
    }


    Timer timer;

    public void startPlay() {
        stopPlay();
        isStop = false;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isStop) {
                    if (currentTime != 0) {
                        if (autoAdd < autoAddTimes) {
                            autoAdd++;
                            currentTime += 20;
//                            Log.d(TAG,"自动刷新");
                            changeCurrent();
                        }
                    }
                }
            }
        }, 0, 20);
//        new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (!isStop) {
//                         changeCurrent(lrcTime);
//                        try {
//                            Thread.sleep(50);
//                            lrcTime += 50;
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
    }

    public void stopPlay() {
        float1 = 0;
        float2 = 0;
        currentTime = 0;
        mCurrentLine = 0;
        lrcTime = 0;
        isStop = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void destory() {
        EventBusManager.unregister(this);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    long currentTime = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
//        Log.d(TAG," onEvent="+event.tag);
        if (SubcriberTag.AUDIO_TRACK_PLAY_BGM.equals(event.tag)) {
            startPlay();
        } else if (SubcriberTag.AUDIO_TRACK_STOP_BGM.equals(event.tag)) {
            stopPlay();
        } else if (SubcriberTag.AUDIO_TRACK_UPDATE_BGM_POSITION.equals(event.tag)) {
//             Log.d(TAG,"歌词刷新时间 changeCurrent="+event.event);
            long curTime = (long) event.event;
            if (curTime >= currentTime) {
//                 Log.d(TAG,"播放器刷新");
                autoAdd = 0;
                currentTime = curTime;
                changeCurrent();
            }

        }

    }

    int autoAddTimes = 5;
    int autoAdd = 0;

    //用来存储歌词的类
    class KrcLineTime {
        long startTime;
        long spanTime;
        long reserve;

        public KrcLineTime() {
        }

        public KrcLineTime(long startTime, long spanTime, long reserve) {
            super();
            this.startTime = startTime;
            this.spanTime = spanTime;
            this.reserve = reserve;
        }

        @Override
        public String toString() {
            return "KrcLineTime{" +
                    "reserve=" + reserve +
                    ", startTime=" + startTime +
                    ", spanTime=" + spanTime +
                    '}';
        }
    }

    //用来存储一行歌词的类
    class KrcLine {
        KrcLineTime lineTime;
        List<KrcLineTime> wordTime;
        String lineStr;

        public KrcLine() {
            lineTime = new KrcLineTime();
            wordTime = new ArrayList<KrcLineTime>();
            lineStr = "";
        }

        @Override
        public String toString() {
            return "KrcLine{" +
                    "lineStr='" + lineStr + '\'' +
                    ", lineTime=" + lineTime +
                    ", wordTime start=" + "" +
                    '}';//wordTime.size()>0?wordTime.get(0):
        }
    }

    boolean isLrc = false;

    //解析一行
    private KrcLine ParseLine(String line) {
        Log.d(TAG, " ParseLine line=" + line);
        line = line.trim();
        KrcLine krcLine = null;
        try {
            krcLine = new KrcLine();
            if (line.matches("\\[.+\\].+")) {
                line = line.substring(1);
                String strArray[] = line.split("\\]", 2);
                String[] timeStr = strArray[0].split(",");

                Log.d(TAG, "line=" + line + "timeStr.length=" + timeStr.length);

//                if(timeStr.length!=2){//过滤没有时间的无效歌词
//                    return null;
//                }
                if (timeStr.length == 1) { //兼容lrc
//                    isLrc = true;
                    String time = timeStr[0];
                    try {
                        long startTime = strToLongToTime(time);
//                        Date date =  Utils.MINUTE_SESOND_FORMATER.parse(time);
                        Log.d(TAG, " startTime=" + startTime + " time=" + time);
                        krcLine.lineTime.startTime = startTime;
                        krcLine.lineTime.spanTime = 1000;
//                        krcLine.lineTime.spanTime  = ;
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw, true));
                        Log.d(TAG, "date转换异常报错:"
                                + sw.toString() + " time=" + time);
                    }
                    krcLine.lineStr = strArray[1];

                } else {
                    krcLine.lineTime.startTime = Long.parseLong(timeStr[0]);
                    krcLine.lineTime.spanTime = Long.parseLong(timeStr[1]);
                    String lyricsStr[] = strArray[1].split("[<>]");
                    for (int i = 1; i < lyricsStr.length; i += 2) {
                        String wordTime[] = lyricsStr[i].split(",");
//                    if (wordTime.length < 3) {
//                        continue;
//                    }
                        long startT = Long.parseLong(wordTime[0]);
                        long spanT = Long.parseLong(wordTime[1]);
                        long reverse = Long.parseLong(wordTime[2]);
                        KrcLineTime krcLineTime = new KrcLineTime(startT, spanT, reverse);

                        krcLine.wordTime.add(krcLineTime);
                        krcLine.lineStr += lyricsStr[1 + i];
                    }
                }

                if (krcLine.lineStr.length() > maxLineStr.length()) {
                    maxLineStr = krcLine.lineStr;
                }

                Log.d(TAG, " krcLine=" + krcLine);
            } else {
                Log.d(TAG, " lineStr= return  null");
                return null;
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            Log.d(TAG, "ParseLine报错:"
                    + sw.toString());
            return null;
        }

        return krcLine;
    }

    //
    // 背景图片
    public void setBackground(Bitmap bmp) {
        mBackground = bmp;
    }


    long lastTimeSpan;

    // 外部提供方法
    // 传入当前播放时间
    public void changeCurrent() {
        if (isChanging) {
            return;
        }
//        Log.d(TAG," changeCurrent curTime ="+currentTime);
        isChanging = true;
        // 每次进来都遍历存放的时间
        KrcLine kl = null;
        KrcLine klNext = null;
        for (int i = 0; i < mKrcLineList.size(); i++) {

            // 遍历个词列表来获取当前行和当前字
//            float1 = 0.0f;
//            float2 = 0.001f;
            kl = mKrcLineList.get(i);
            klNext = null;
            if (mKrcLineList.size() > i + 1) {
                klNext = mKrcLineList.get(i + 1);
            }
            /*Log.d(TAG, " mCurrentLine=" + mCurrentLine + " startTime=" + kl.lineTime.startTime + " currentTime=" + currentTime + " spanTime=" + kl.lineTime.spanTime);
            if (klNext != null) {
                Log.d(TAG, "klNext.lineTime.startTime=" + klNext.lineTime.startTime);
            }*/
//                if(klNext!=null&&isLrc){
//                    if(kl.lineTime.startTime <= currentTime &&(klNext!=null&&currentTime<klNext.lineTime.startTime))
//                    mCurrentLine = i;
//                    Log.d(TAG,"klNext.lineTime.startTime="+klNext.lineTime.startTime);
//                    float1 = 1f;
//                    float2 = 1f;
//                    break;
//                }
            if (kl.lineTime.startTime <= currentTime && ((currentTime < kl.lineTime.startTime + kl.lineTime.spanTime) || (klNext != null && currentTime < klNext.lineTime.startTime))) {
                mCurrentLine = i;
                if (mLastLine == mCurrentLine) {
                    //同一行
                } else {
                    Log.d(TAG, "换行从" + mLastLine + "-》" + mCurrentLine);
                    float1 = 1f;
                    float2 = 1f;
                    mLastLine = mCurrentLine;
                }

                //当前行
                int len = kl.lineStr.length();
                int currentWordIndex = 0;
                KrcLineTime klt = null;
                KrcLineTime kltNext = null;
                long timeSpan = currentTime - kl.lineTime.startTime;

                if (kl.wordTime.size() == 0) {//lrc处理全高亮显示
                    float1 = 1f;
                    float2 = 1f;
                    break;
                }

                for (int j = 0; j < kl.wordTime.size(); j++) {
                    currentWordIndex = j;
                    klt = kl.wordTime.get(j);

                    if (kl.wordTime.size() > j + 1) {//防止中间隔空导致先变白再变绿
                        kltNext = kl.wordTime.get(j + 1);
                    }
                    if (kl.wordTime.size() == 1) { //处理整句只有一个时间的全高亮显示
                        float1 = 1f;
                        float2 = 1f;
                    } else if ((klt.startTime <= timeSpan && timeSpan < klt.startTime + klt.spanTime + 1)) {//
                        //计算当前歌词所在的百分比
                        float1 = (j + (timeSpan - klt.startTime) * 1.0f / klt.spanTime) / len;
                        float2 = float1 > 0.99f ? float1 : (float1 + 0.01f);
                        lastTimeSpan = timeSpan;
                        break;
                    } else if (kltNext != null && timeSpan < kltNext.startTime) {//防止中间隔空导致先跳过再回退
                        //计算当前歌词所在的百分比
                        float1 = (j + (lastTimeSpan - klt.startTime) * 1.0f / klt.spanTime) / len;
                        float2 = float1 > 0.99f ? float1 : (float1 + 0.01f);
                        break;
                    }
                }
                break;
            } else if (klNext != null && currentTime > kl.lineTime.startTime + kl.lineTime.spanTime && currentTime < klNext.lineTime.startTime) {//当前句最后一字停止知道下一句出来
                float1 = 1f;
                float2 = 1f;
                break;
            }
//            else if(kl.lineTime.startTime>=currentTime){//解决闪动
//                break;
//            }
        }

        //更新视图
        postInvalidate();
        isChanging = false;
    }


    /**
     * 保证时间格式一致 为m:ss
     *
     * @param str 时间字符
     * @return 判断用的时间符
     */
    private long strToLongToTime(String str) {
        // System.out.println(str);
        int m = Integer.parseInt(str.substring(0, str.indexOf(":")));
        int s = 0;
        int ms = 0;

        // 判断歌词时间是否有毫秒
        if (str.indexOf(".") != -1) {
            s = Integer.parseInt(str.substring(str.indexOf(":") + 1, str
                    .indexOf(".")));
            ms = Integer.parseInt(str.substring(str.indexOf(".") + 1, str
                    .length()));
        } else {
            s = Integer.parseInt(str.substring(str.indexOf(":") + 1, str
                    .length()));
        }
        return m * 60000 + s * 1000 + ms * 10;
//        return timeMode(m * 60000 + s * 1000 + ms * 10);
    }

    /**
     * 返回时间
     *
     * @param time 毫秒时间
     */
    public static String timeMode(int time) {
        int tmp = (time / 1000) % 60;
        if (tmp < 10)
            return time / 60000 + ":" + "0" + tmp;
        else
            return time / 60000 + ":" + tmp;
    }
}
