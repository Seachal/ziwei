package com.tencent.karaoke.common.media.audiofx;


public class Reverb extends a {
    public static final int MIN_REVERB_LEVEL = 11;
    public static final int MAX_REVERB_LEVEL = 17;

    public static final int NEW_REVERB_NONE = 10;
    public static final int NEW_REVERB_KTV = 11;
    public static final int NEW_REVERB_WENNUAN = 12;
    public static final int NEW_REVERB_CIXING = 13;
    public static final int NEW_REVERB_KONGLING = 14;
    public static final int NEW_REVERB_YOUYUAN = 15;
    public static final int NEW_REVERB_MIHUAN = 16;
    public static final int NEW_REVERB_LAOCHANGPIAN = 17;

    public static final String REVERB_NAME_NONE = "原声";
    public static final String REVERB_NAME_KTV = "KTV";
    public static final String REVERB_NAME_WENNUAN = "温暖";
    public static final String REVERB_NAME_CIXING = "磁性";
    public static final String REVERB_NAME_KONGLING = "空灵";
    public static final String REVERB_NAME_YOUYUAN = "悠远";
    public static final String REVERB_NAME_MIHUAN = "3D迷幻";
    public static final String REVERB_NAME_LAOCHANGPIAN = "老唱片";


    @Deprecated
    public static final int REVERB_NONE = 0;

    @Deprecated
    public static final int REVERB_THEATER = 6;
    private static final String TAG = "Reverb";
    private static boolean mIsLoaded = false;
    private boolean mIsValid;
    private long nativeHandle;

    public static int mutator_lock = 0;


    static {
        System.loadLibrary("webrtc_audio_preprocessing_v7a");
        System.loadLibrary("audiobase_v7a");
        System.loadLibrary("audiobase_jni_v7a");
    }


//  static
//  {
//    try
//    {
//      boolean bool1 = Native.a("webrtc_audio_preprocessing", new boolean[0]);
//      int i = 0;
//      if (bool1)
//      {
//        boolean bool2 = Native.a("audiobase", new boolean[0]);
//        i = 0;
//        if (bool2)
//        {
//          boolean bool3 = Native.a("audiobase_jni", new boolean[0]);
//          i = 0;
//          if (bool3)
//            i = 1;
//        }
//      }
//      mIsLoaded = i;
//      return;
//    }
//    catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
//    {
//      o.e("Reverb", "System.loadLibrary failed", localUnsatisfiedLinkError);
//      return;
//    }
//    catch (Exception localException)
//    {
//      o.e("Reverb", "System.loadLibrary failed", localException);
//    }
//  }

    public Reverb() {
//    if (NotDoVerifyClasses.DO_VERIFY_CLASSES)
//      System.out.print(AntiLazyLoad.class);
//    this.mIsValid = false;
    }

    public static String getDesc(int paramInt) {
        switch (paramInt) {
            default:
                return "Unknow";
            case 10:
                return "REVERB_NONE";
            case 11:
                return "NEW_REVERB_GENERIC";
            case 13:
                return "NEW_REVERB_SEWERPIPE";
            case 14:
                return "NEW_REVERB_FOREST";
            case 12:
                return "NEW_REVERB_CAVE";
            case 16:
                return "NEW_REVERB_DIZZY";
            case 15:
                return "NEW_REVERB_SPORT_FULLSTADIUM";
            case 17:
        }
        return "NEW_REVERB_PHONOGRAPH_GENERIC";
    }

    private native int init();

//  public static boolean isValid(int paramInt)
//  {
//    if (NotDoVerifyClasses.DO_VERIFY_CLASSES)
//      System.out.print(AntiLazyLoad.class);
//    return ((paramInt >= 10) && (paramInt <= 17));
//  }

    public static int mapping(int paramInt) {
        if ((paramInt >= 10) && (paramInt <= 17))
            return paramInt;
        switch (paramInt) {
            case 1:
            case 2:
            case 3:
            default:
                return 10;
            case 5:
                return 14;
            case 4:
                return 11;
            case 0:
                return 10;
            case 6:
        }
        return 15;
    }

    private native int native_process(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2);

    private native void native_release();

    private native void setReverbType(int paramInt);

    public void init(int paramInt1, int paramInt2) {
        int i;
        int j;
        super.init(paramInt1, paramInt2);
        init();
//    if (mIsLoaded)
//    {
//      i = init();
//      if (i != 0)
//        break label62;
//      j = 1;
//    }
//    while (true)
//    {
//      this.mIsValid = j;
//      if (!(this.mIsValid))
//        o.d("Reverb", "Reverb init failed: " + i);
//      return;
//      label62: j = 0;
//    }
    }

    public int process(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2) {
//    if ((paramInt1 > paramArrayOfByte1.length) || (paramInt2 > paramArrayOfByte2.length) || (paramInt1 > paramInt2))
//    {
//      return -1;
//    }
//    if (this.mIsValid)
        return native_process(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
//    o.d("Reverb", "reverb invalid");
//    return -1;
    }

    public void release() {
//    if (mIsLoaded)
//    {
        native_release();
        return;
//    }
//    o.d("Reverb", "library missed");
    }

    public void setReverbLevel(int paramInt) {
        setReverbType(paramInt);
//    if ((paramInt == 0) || (paramInt == 10))
//    {
//      this.able = false;
//      return;
//    }
//    if ((paramInt < 10) || (paramInt > 17))
//    {
//      o.e("Reverb", "invalided revType: " + paramInt);
//      this.able = false;
//      return;
//    }
//    this.able = true;
//    if (this.mIsValid)
//    {
//      setReverbType(paramInt);
//      return;
//    }
//    o.d("Reverb", "reverb invalid");
    }
}