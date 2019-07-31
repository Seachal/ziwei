package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.util.Common;

import java.util.List;

public class SearchRecommendGoodsMsg extends ListMag<ShoppingGoodsBaseBean> {

    @Expose
    @SerializedName(Common.DATA)
    private SearchGoodsData searchGoods;

    @Override
    public List<ShoppingGoodsBaseBean> getList() {
        return searchGoods.getList();
    }


    public class SearchGoodsData {

        @Expose
        @SerializedName(Common.GOODS)
        private List<ShoppingGoodsBaseBean> list;

        public List<ShoppingGoodsBaseBean> getList() {
            return list;
        }

        public void setList(List<ShoppingGoodsBaseBean> list) {
            this.list = list;
        }
    }

}
