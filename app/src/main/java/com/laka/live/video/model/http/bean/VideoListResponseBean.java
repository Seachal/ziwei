package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.User;
import com.laka.live.msg.ListMag;
import com.laka.live.util.Common;
import com.laka.live.video.model.bean.MiniVideoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/2
 * @Description:视频列表ResponseBean
 */

public class VideoListResponseBean extends ListMag<MiniVideoBean> {

    @SerializedName(Common.DATA)
    private List<MiniVideoBean> mList = new ArrayList<>();

    @Override
    public List<MiniVideoBean> getList() {
//        if (Utils.isEmpty(mList)) {
//            mList = createTestData();
//        }
        return mList;
    }

    private List<MiniVideoBean> createTestData() {
        List<MiniVideoBean> testList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MiniVideoBean bean = new MiniVideoBean();
            bean.setVideoId(i);
            bean.setVideoCover("https://timgsa.baidu.com/timg?image&quality=8" +
                    "0&size=b9999_10000&sec=1533204846838&di=5a1443a0d2d15bb8" +
                    "d0c1cd9f4d81f852&imgtype=0&src=http%3A%2F%2F" +
                    "p1.qhimgs4.com%2Ft01631b20733ad425a0.jpg");
            if (i % 2 == 0) {
                bean.setVideoTitle("哈哈哈华盛顿哈师大哈哈哈华盛顿哈师大哈哈哈华盛顿哈师" +
                        "大哈哈哈华盛顿哈师大哈哈哈华盛顿哈师大哈哈哈华盛顿哈师大哈哈哈华盛顿哈师大：" + i);
            } else {
                bean.setVideoTitle("测试小视频测试小视频测试小视频：" + i);
            }
            bean.setVideoDuration(i + "0");
            User user = new User();
            user.setNickname("迪丽热巴：" + i);
            user.setAvatar("https://timgsa.baidu.com/timg?image&quality=" +
                    "80&size=b9999_10000&sec=1533204915120&di=82d3a800b4764c816973e" +
                    "4b25c27948b&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.c" +
                    "n%2Fupload%2F20170318%2Fb2688cb8f08b4740b39520fe98eace79_th.jpg");
            bean.setUser(user);
            bean.setLike(i % 2 == 0);
            bean.setLikeCount("2." + i + "万");
            testList.add(bean);
        }
        return testList;
    }

    @Override
    public String toString() {
        return "VideoListResponseBean{" +
                "mList=" + mList +
                '}';
    }
}
