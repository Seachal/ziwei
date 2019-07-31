package com.laka.live.shopping.bean.newversion;

import java.util.List;

/**
 * @ClassName: ShoppingHomeCateBean
 * @Description: 这一步封装为了在adapter中显示
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */

public class ShoppingHomeCateBean {
    private List<GoodsCate> goodsCates;

    public ShoppingHomeCateBean(List<GoodsCate> goodsCates) {
        this.goodsCates = goodsCates;
    }

    public List<GoodsCate> getGoodsCates() {
        return goodsCates;
    }

    public void setGoodsCates(List<GoodsCate> goodsCates) {
        this.goodsCates = goodsCates;
    }
}
