package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by zhxu on 2016/4/28.
 * Email:357599859@qq.com
 */
public class ShoppingHomeTemplateBean {

    private int topicId;
    private String attrKey;
    private String attrValue;
    private String title;
    private String brief;
    private String imageUrl;
    private int sort;
    private ShoppingHomeCommonListBean banner;
    private List<ShoppingHomeCommonListBean> goods;
    private List<ShoppingHomeCommonListBean> list;

    public int getTopicId() {
        return topicId;
    }

    public String getAttrKey() {
        return attrKey;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public String getTitle() {
        return title;
    }

    public String getBrief() {
        return brief;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getSort() {
        return sort;
    }

    public ShoppingHomeCommonListBean getBanner() {
        return banner;
    }

    public List<ShoppingHomeCommonListBean> getGoods() {
        return goods;
    }

    public List<ShoppingHomeCommonListBean> getList() {
        return list;
    }
}
