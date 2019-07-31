package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

import java.util.ArrayList;

/**
 * Created by luwies on 16/3/26.
 */
public class RecommendGoodsMsg extends ListMag<ShoppingGoodsDetailBean> {

    @Expose
    @SerializedName(Common.DATA)
    private RecommendMsg recommendMsg;

    @Override
    public ArrayList<ShoppingGoodsDetailBean> getList() {
        return recommendMsg.getList();
    }

    public class RecommendMsg {

        @Expose
        @SerializedName("goods")
        private ArrayList<ShoppingGoodsDetailBean> goods;

        public ArrayList<ShoppingGoodsDetailBean> getList() {
            if(Utils.listIsNullOrEmpty(goods)){
                return new ArrayList<ShoppingGoodsDetailBean>();
            }
            return goods;
        }

        public void setList(ArrayList<ShoppingGoodsDetailBean> goods) {
            this.goods = goods;
        }

    }


}
