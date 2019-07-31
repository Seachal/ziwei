package com.laka.live.shopping.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by zhxu on 2015/12/21.
 * Email:357599859@qq.com
 */
public class ShoppingTopicBean implements Serializable {

    private int id;
    private int topicId;
    private String title;
    private String subTitle;
    private int template;
    private String surplusSeconds;
    private String thumbUrl;
    private List<ShoppingTopicColumnBean> column;
    private List<ShoppingCategoriesBean> topic;

    public List<ShoppingCategoriesBean> getTopic() {
        return topic;
    }
    public int getId() {
        return id;
    }

    public int getTopicId() {
        return topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getTemplate() {
        return template;
    }

    public String getSurplusSeconds() {
        return surplusSeconds;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public List<ShoppingTopicColumnBean> getColumn() {
        return column;
    }
}
