package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by zhxu on 2016/1/8.
 * Email:357599859@qq.com
 */
public class ShoppingNewTopicBean {

    private List<ShoppingTopicListBean> goods;
    private String headImageUrl;
    private String description;
    private String title;

    public String getTitle() {
        return title;
    }

    public List<ShoppingTopicListBean> getGoods() {
        return goods;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public String getDescription() {
        return description;
    }
}
