package com.laka.live.video.constant;

/**
 * @Author:Rayman
 * @Date:2018/8/10
 * @Description:
 */

public class VideoEventConstant {

    public static final String UPDATE_MATERIAL_STORE = "UPDATE_MATERIAL_STORE";

    /**
     * description:主页更新
     **/
    public static final String DOUBLE_TAB_UPDATE_VIDEO_LIST = "DOUBLE_TAB_UPDATE_VIDEO_LIST";

    /**
     * description:小视频播放界面部分UI事件
     **/
    public static final String VIDEO_BACK = "VIDEO_BACK";
    public static final String VIDEO_FOLLOW = "VIDEO_FOLLOW";
    public static final String VIDEO_USER_INFO = "VIDEO_USER_INFO";
    public static final String VIDEO_MORE = "VIDEO_MORE";
    public static final String VIDEO_LIKE = "VIDEO_LIKE";
    public static final String VIDEO_COMMENT = "VIDEO_COMMENT";
    public static final String VIDEO_OPEN_COMMENT = "VIDEO_OPEN_COMMENT";
    public static final String VIDEO_OPEN_RECOMMEND = "VIDEO_OPEN_RECOMMEND";
    public static final String BACK_TO_MINI_PLAY = "BACK_TO_MINI_PLAY";

    /**
     * description:小视频页面相关操作事件
     **/
    public static final String HIDE_VIDEO_FUNCTION = "HIDE_VIDEO_FUNCTION";
    public static final String VIDEO_PAUSE = "VIDEO_PAUSE";
    public static final String VIDEO_RESUME = "VIDEO_RESUME";
    public static final String VIDEO_DESTROY = "VIDEO_DESTROY";
    public static final String VIDEO_PAGER_CHANGE = "VIDEO_PAGER_CHANGE";

    /**
     * description:小视频页面更新相关数据事件
     **/
    public static final String UPDATE_VIDEO_LIST = "UPDATE_VIDEO_LIST";
    public static final String LOAD_VIDEO_LIST = "LOAD_VIDEO_LIST";
    public static final String UPDATE_RELATIVE_USER_INFO = "UPDATE_RELATIVE_USER_INFO";
    public static final String UPDATE_VIDEO_PLAY_LIST = "UPDATE_VIDEO_PLAY_LIST";
    public static final String UPDATE_COMMENT_LIST = "UPDATE_COMMENT_LIST";
    public static final String UPDATE_RECOMMEND_GOODS_LIST = "UPDATE_RECOMMEND_GOODS_LIST";
    public static final String UPDATE_VIDEO_POSITION = "UPDATE_VIDEO_POSITION";
    public static final String UPDATE_VIDEO_REPLY_LIST = "UPDATE_VIDEO_REPLY_LIST";

    /**
     * description:Container更新子DecorView事件，通知MiniVideoPlay和UserInfoActivity更新views列表
     **/
    public static final String UPDATE_CONTAINER_VIEW = "UPDATE_CONTAINER_VIEW";

    /**
     * description:JCMediaManager 释放Media事件
     **/
    public static final String MEDIA_MANAGER_PREPARE = "MEDIA_MANAGER_PREPARE";
    public static final String MEDIA_MANAGER_RELEASE = "MEDIA_MANAGER_RELEASE";
}
