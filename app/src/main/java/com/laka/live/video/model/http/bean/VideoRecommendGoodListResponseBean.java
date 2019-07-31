package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.ListMag;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频推荐商品ResponseBean
 */

public class VideoRecommendGoodListResponseBean extends ListMag<ShoppingGoodsBaseBean> {

    @SerializedName(Common.DATA)
    private List<ShoppingGoodsBaseBean> mList;

    @Override
    public List<ShoppingGoodsBaseBean> getList() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        return mList;
    }
}
