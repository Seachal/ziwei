package com.laka.live.video.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author:Rayman
 * @Date:2018/8/3
 * @Description:小视频页面UI相关常量
 */

public class VideoConstant {

    /**
     * description:Intent跳转
     **/
    public static final String VIDEO_INFO_EXTRA_ID = "VIDEO_INFO_EXTRA_ID";
    public static final String VIDEO_INFO_EXTRA_URL = "VIDEO_INFO_EXTRA_URL";
    public static final String VIDEO_INFO_EXTRA_LIST = "VIDEO_INFO_EXTRA_LIST";
    public static final String VIDEO_INFO_EXTRA_POSITION = "VIDEO_INFO_EXTRA_POSITION";
    public static final String MATERIAL_EXTRA_ITEM = "MATERIAL_EXTRA_ITEM";

    /**
     * description:接口参数
     **/
    public static final String VIDEO_TYPE_FOLLOW = "follow";
    public static final String VIDEO_TYPE_RECOMMEND = "recommend";
    public static final String VIDEO_TYPE_NEWEST = "newest";


    /**
     * description:素材库UI类型
     **/
    public static final String MATERIAL_UI_TYPE = "MATERIAL_UI_TYPE";
    public static final int MATERIAL_TYPE_CHOOSE = 1;
    public static final int MATERIAL_TYPE_CONTROL = 2;

    @IntDef({
            MATERIAL_TYPE_CHOOSE,
            MATERIAL_TYPE_CONTROL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MATERIAL_UI_TYPE {
    }
}
