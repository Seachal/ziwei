package com.laka.live.video.model.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.util.Common;

import java.io.Serializable;

/**
 * @Author:Rayman
 * @Date:2018/8/22
 * @Description:小视频商品推荐Bean
 */

public class VideoRecommendGoods extends ShoppingGoodsBaseBean implements Serializable{

    @SerializedName("rec_id")
    private int recommendId;

    @SerializedName(Common.MINI_VIDEO_ID)
    private int miniVideoId;

    public int getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(int recommendId) {
        this.recommendId = recommendId;
    }

    public int getMiniVideoId() {
        return miniVideoId;
    }

    public void setMiniVideoId(int miniVideoId) {
        this.miniVideoId = miniVideoId;
    }
}
