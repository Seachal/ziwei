package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by chenjiawei on 2016/1/4.
 */
public class ShoppingCarGoodsState {
    private List<ShoppingCarGoodsBean> validGoods;
    private List<ShoppingCarGoodsBean> invalidGoods;
    private List<String> message;

    public List<ShoppingCarGoodsBean> getValidGoods() {
        return validGoods;
    }

    public void setValidGoods(List<ShoppingCarGoodsBean> validGoods) {
        this.validGoods = validGoods;
    }

    public List<ShoppingCarGoodsBean> getInvalidGoods() {
        return invalidGoods;
    }

    public void setInvalidGoods(List<ShoppingCarGoodsBean> invalidGoods) {
        this.invalidGoods = invalidGoods;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
