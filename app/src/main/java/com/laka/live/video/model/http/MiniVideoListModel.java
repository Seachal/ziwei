package com.laka.live.video.model.http;

import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.Common;
import com.laka.live.video.constant.VideoApiConstant;
import com.laka.live.video.contract.MiniVideoListContract;
import com.laka.live.video.model.http.bean.VideoListResponseBean;

import java.util.HashMap;

/**
 * @Author:Rayman
 * @Date:2018/8/2
 * @Description:小视频VideoModel类
 */

public class MiniVideoListModel implements MiniVideoListContract.IVideoListModel {

    @Override
    public void getVideoList(String type, String page, String limit,
                             GsonHttpConnection.OnResultListener<VideoListResponseBean> callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TYPE, type);
        params.put(Common.PAGE, page);
        params.put(Common.LIMIT, limit);
        DataProvider.addToken(params);
        GsonHttpConnection.getInstance().get(this,
                VideoApiConstant.GET_VIDEO_LIST_API, params,
                VideoListResponseBean.class, callBack);
    }
}
