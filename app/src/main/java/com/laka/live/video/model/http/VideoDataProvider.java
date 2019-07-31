package com.laka.live.video.model.http;

import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.video.constant.VideoApiConstant;

import java.util.HashMap;

/**
 * @Author:Rayman
 * @Date:2018/8/8
 * @Description:视频库部分API获取（部分页面不需要MVP模式） 因为可能这部分的页面可以复用，MVP模式则太限定使用场景了
 */

public class VideoDataProvider {

    /**
     * 扫一扫登陆网页
     *
     * @return
     */
    public static String loginWebSiteUpLoadVideo(Object tag, String uuid, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("sign", uuid);
        DataProvider.addToken(params, true);
        return GsonHttpConnection.getInstance().post(tag,
                VideoApiConstant.SCAN_LOGIN_WEB_API, params, Msg.class, listener);
    }

}
