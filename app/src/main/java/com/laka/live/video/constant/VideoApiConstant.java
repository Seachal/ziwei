package com.laka.live.video.constant;

import com.laka.live.util.Common;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:滋味--小视频相关API
 */

public class VideoApiConstant {

    /**
     * description:主页
     **/
    public static final String GET_VIDEO_LIST_API = Common.SERVER_URL + "miniVideo/home";

    /**
     * description:小视频详情
     **/
    public static final String GET_VIDEO_INFO_API = Common.SERVER_URL + "miniVideo/one";
    public static final String LIKE_VIDEO_API = Common.SERVER_URL + "miniVideo/like";
    public static final String POST_COMMENT_API = Common.SERVER_URL + "miniVideo/comment";
    public static final String GET_SUB_COMMENT_API = Common.SERVER_URL + "miniVideo/replies";
    public static final String GET_VIDEO_COMMENT_LIST_API = Common.SERVER_URL + "miniVideo/comments";

    /**
     * description:素材库
     **/
    public static final String GET_MATERIAL_LIST_API = Common.SERVER_URL + "material/home";
    public static final String DELETE_MATERIAL_API = Common.SERVER_URL + "material/delete";
    public static final String GET_MATERIAL_COUNT_API = Common.SERVER_URL + "material/count";

    /**
     * description:上传视频
     **/
    public static final String SCAN_WEB_LINK = "miniVideo/qrcode";
    public static final String SCAN_LOGIN_WEB_API = Common.SERVER_URL + "miniVideo/login";

}
