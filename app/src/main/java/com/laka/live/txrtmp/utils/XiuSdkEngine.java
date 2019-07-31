package com.laka.live.txrtmp.utils;

/**
 * Created by luwies on 16/7/18.
 */
public class XiuSdkEngine {

    static {
        System.loadLibrary("xiusdk");
    }

    /**
     * 无效果
     */
    public static final int RT_FILTER_ID_BEAUTY = 0;

    /**
     * 清晰
     */
    public static final int RT_FILTER_BEAUTY_CLEAR = RT_FILTER_ID_BEAUTY + 1;

    /**
     * 白皙
     */
    public static final int RT_FILTER_BEAUTY_WHITESKINNED = RT_FILTER_ID_BEAUTY + 2;

    /**
     * 冷艳
     */
    public static final int RT_FILTER_BEAUTY_COOL = RT_FILTER_ID_BEAUTY + 3;

    /**
     * 冰灵
     */
    public static final int RT_FILTER_BEAUTY_ICESPIRIT = RT_FILTER_ID_BEAUTY + 4;

    /**
     * 典雅
     */
    public static final int RT_FILTER_BEAUTY_REFINED = RT_FILTER_ID_BEAUTY + 5;

    /**
     * 蓝调
     */
    public static final int RT_FILTER_BEAUTY_BLUESTYLE = RT_FILTER_ID_BEAUTY + 6;

    /**
     * 萝莉
     */
    public static final int RT_FILTER_BEAUTY_LOLITA = RT_FILTER_ID_BEAUTY + 7;

    /**
     * 洛可可
     */
    public static final int RT_FILTER_BEAUTY_LKK = RT_FILTER_ID_BEAUTY + 8;

    /**
     * 暖黄
     */
    public static final int RT_FILTER_BEAUTY_NUANHUANG = RT_FILTER_ID_BEAUTY + 9;

    /**
     * 清纯
     */
    public static final int RT_FILTER_BEAUTY_RCOOL = RT_FILTER_ID_BEAUTY + 10;

    /**
     * 日系
     */
    public static final int RT_FILTER_BEAUTY_JSTYLE = RT_FILTER_ID_BEAUTY + 11;

    /**
     * 柔光
     */
    public static final int RT_FILTER_BEAUTY_SOFTLIGHT = RT_FILTER_ID_BEAUTY + 12;

    /**
     * 甜美
     */
    public static final int RT_FILTER_BEAUTY_TIANMEI = RT_FILTER_ID_BEAUTY + 13;

    /**
     * 唯美
     */
    public static final int RT_FILTER_BEAUTY_WEIMEI = RT_FILTER_ID_BEAUTY + 14;

    /**
     * 丽人
     */
    public static final int RT_FILTER_BEAUTY_FRESH = RT_FILTER_ID_BEAUTY + 15;

    /**
     * 果冻
     */
    public static final int RT_FILTER_BEAUTY_JPJELLY = RT_FILTER_ID_BEAUTY + 16;

    /**
     * 花颜
     */
    public static final int RT_FILTER_BEAUTY_HUAYAN = RT_FILTER_ID_BEAUTY + 17;

    /**
     * 裸妆
     */
    public static final int RT_FILTER_BEAUTY_LUOZHUANG = RT_FILTER_ID_BEAUTY + 18;

    /**
     * 嫩红
     */
    public static final int RT_FILTER_BEAUTY_NENHONG = RT_FILTER_ID_BEAUTY + 19;

    /**
     * 艺术
     */
    public static final int RT_FILTER_BEAUTY_BLACKWHITE = RT_FILTER_ID_BEAUTY + 20;

    /**
     * 美肤
     */
    public static final int RT_FILTER_BEAUTY_WHITENING = RT_FILTER_ID_BEAUTY + 21;

    /**
     * 红润
     */
    public static final int RT_FILTER_BEAUTY_RUDDY = RT_FILTER_ID_BEAUTY + 22;

    /**
     * 靓美
     */
    public static final int RT_FILTER_BEAUTY_JPAESTHETICISM = RT_FILTER_ID_BEAUTY + 23;

    /**
     * 紫韵
     */
    public static final int RT_FILTER_BEAUTY_PURPLEDREAM = RT_FILTER_ID_BEAUTY + 24;

    /**
     * 淡雅
     */
    public static final int RT_FILTER_BEAUTY_JPELEGANT = RT_FILTER_ID_BEAUTY + 25;

    /**
     * 清新
     */
    public static final int RT_FILTER_BEAUTY_JPFRESH = RT_FILTER_ID_BEAUTY + 26;

    /**
     * 柔美
     */
    public static final int RT_FILTER_BEAUTY_JPSWEET = RT_FILTER_ID_BEAUTY + 27;

    /**
     * 温暖
     */
    public static final int RT_FILTER_BEAUTY_JPWARM = RT_FILTER_ID_BEAUTY + 28;

    /**
     * 暖暖阳光
     */
    public static final int RT_FILTER_BEAUTY_SUNSHINE = RT_FILTER_ID_BEAUTY + 29;

    /**
     * 甜美可人
     */
    public static final int RT_FILTER_BEAUTY_SWEET = RT_FILTER_ID_BEAUTY + 30;

    /**
     * 阿宝色
     */
    public static final int RT_FILTER_BEAUTY_ABAOSE = RT_FILTER_ID_BEAUTY + 31;

    /**
     * 浪漫
     */
    public static final int RT_FILTER_BEAUTY_LANGMAN = RT_FILTER_ID_BEAUTY + 32;

    /**
     * 清透
     */
    public static final int RT_FILTER_BEAUTY_QINGTOU = RT_FILTER_ID_BEAUTY + 33;

    /**
     * 臻白
     */
    public static final int RT_FILTER_BEAUTY_ZHENBAI = RT_FILTER_ID_BEAUTY + 34;

    /**
     * 自然
     */
    public static final int RT_FILTER_BEAUTY_ZIRAN = RT_FILTER_ID_BEAUTY + 35;

    /**
     * 暖暖
     */
    public static final int RT_FILTER_BEAUTY_WARMER = RT_FILTER_ID_BEAUTY + 36;


    /**
     * @param bgr          原始图像数据 Buffer,支持 NV12/NV21 格式; 注:图像 pBGR 数据存放为 BGRA
     * @param width        原始图像宽度;
     * @param height       原始图像高度;
     * @param stride       图像跨度;
     * @param softEnable   磨皮开关;
     * @param softRatio    磨皮程度变量[0-100],建议值 35;
     * @param beautyEnable 美白开关;
     * @param beautyRatio  美白程度变量[0-100],建议值 70;
     * @param filterID     滤镜 ID;
     * @param filterRatio  滤镜程度变量[0-100],建议值 100;
     * @return 0-OK,其他参考异常列表;
     */
    public static native int XIUSDK_SoftSkinBGR(byte[] bgr, int width, int height, int stride, int softEnable,
                                                int softRatio, int beautyEnable, int beautyRatio, int filterID,
                                                int filterRatio);


    /**
     * @param yuv          原始图像数据 Buffer,支持 NV12/NV21 格式;
     * @param width        原始图像宽度;
     * @param height       原始图像高度;
     * @param softEnable   磨皮开关;
     * @param softRatio    磨皮程度变量[0-100],建议值 35;
     * @param beautyEnable 美白开关;
     * @param beautyRatio  美白程度变量[0-100],建议值 70;
     * @param filterID     滤镜 ID;
     * @param filterRatio  滤镜程度变量[0-100],建议值 100;
     * @param imgFormat    0 - nv21,1 - nv12;
     * @return 0-OK,其他参考异常列表;
     */
    public static native int XIUSDK_SoftSkin420(byte[] yuv, int width, int height, int softEnable,
                                                int softRatio, int beautyEnable, int beautyRatio, int filterID,
                                                int filterRatio, int imgFormat);
}
