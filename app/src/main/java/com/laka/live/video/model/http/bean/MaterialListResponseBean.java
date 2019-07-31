package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.ListMag;
import com.laka.live.util.Common;
import com.laka.live.video.model.bean.VideoMaterialBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/3
 * @Description:
 */

public class MaterialListResponseBean extends ListMag<VideoMaterialBean> {

    //    private List<VideoMaterialBean> mList = createTestData();

    @SerializedName(Common.DATA)
    private List<VideoMaterialBean> mList = new ArrayList<>();

    private List<VideoMaterialBean> createTestData() {
        List<VideoMaterialBean> testList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoMaterialBean bean = new VideoMaterialBean();
            bean.setVideoId(i + "");
            bean.setVideoCover("https://timgsa.baidu.com/timg?image&quality=8" +
                    "0&size=b9999_10000&sec=1533204846838&di=5a1443a0d2d15bb8" +
                    "d0c1cd9f4d81f852&imgtype=0&src=http%3A%2F%2F" +
                    "p1.qhimgs4.com%2Ft01631b20733ad425a0.jpg");
            bean.setVideoUrl("https://vd1.bdstatic.com/mda-hbpk9asekkkx3pn3/mda-hbpk9asekkkx3pn3.mp4?auth_key=1533644448-0-0-da65a62b9146f0496b09fca2e02d328a&bcevod_channel=searchbox_feed&pd=wisenatural");
            if (i % 2 == 0) {
                bean.setVideoTitle("哈哈哈华盛顿哈师大哈哈哈华盛顿哈师大哈哈哈华盛顿哈师" +
                        "大哈哈哈华盛顿哈师大哈哈哈华盛顿哈师大哈哈哈华盛顿哈师大哈哈哈华盛顿哈师大：" + i);
            } else {
                bean.setVideoTitle("测试小视频测试小视频测试小视频：" + i);
            }
            bean.setVideoDuration(i + "0:0" + i);
            testList.add(bean);
        }
        return testList;
    }

    @Override
    public List<VideoMaterialBean> getList() {
        return mList;
    }
}
