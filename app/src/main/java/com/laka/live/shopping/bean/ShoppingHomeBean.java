package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by zhxu on 2016/4/28.
 * Email:357599859@qq.com
 */
public class ShoppingHomeBean {

    private List<ShoppingHomeCommonListBean> homeBanner;
    private ShoppingHomeTemplateBean homeCateEntry;
    private ShoppingHomeTemplateBean homeRecommend;
    private ShoppingHomeTemplateBean homeFreetry;
    private List<ShoppingHomeTemplateBean> homeCate;
    private ShoppingHomeTemplateBean homeGuess;
    private ShoppingHomeTemplateBean homeGuide;

    public List<ShoppingHomeCommonListBean> getHomeBanner() {
        return homeBanner;
    }

    public ShoppingHomeTemplateBean getHomeCateEntry() {
        return homeCateEntry;
    }

    public ShoppingHomeTemplateBean getHomeRecommend() {
        return homeRecommend;
    }

    public ShoppingHomeTemplateBean getHomeFreetry() {
        return homeFreetry;
    }

    public List<ShoppingHomeTemplateBean> getHomeCate() {
        return homeCate;
    }

    public ShoppingHomeTemplateBean getHomeGuess() {
        return homeGuess;
    }

    public ShoppingHomeTemplateBean getHomeGuide() {
        return homeGuide;
    }
}
