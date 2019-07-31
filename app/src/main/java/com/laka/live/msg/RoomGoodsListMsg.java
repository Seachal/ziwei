package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/23.
 */
public class RoomGoodsListMsg extends ListMag<ShoppingGoodsDetailBean> {

    @Expose
    @SerializedName(Common.DATA)
    private List<ShoppingGoodsDetailBean> list;

    @Override
    public List<ShoppingGoodsDetailBean> getList() {
        return list;
    }
}
