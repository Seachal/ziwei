package com.laka.live.shopping.bean;

/**
 * Created by zhxu on 2016/4/28.
 * Email:357599859@qq.com
 */
public class ShoppingHomeCommonListBean {

    private String bannerId;
    private String topicId;
    private String columnId;
    private String title;
    private String description;
    private String type;
    private String imageUrl;
    private String adAttr;
    private String adAttrValue;
    private String attrKey;
    private String attrValue;
    private ShoppingHomeCommentBean comment;

    public String getBannerId() {
        return bannerId;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getColumnId() {
        return columnId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAdAttr() {
        return adAttr;
    }

    public String getAdAttrValue() {
        return adAttrValue;
    }

    public String getAttrKey() {
        return attrKey;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public ShoppingHomeCommentBean getComment() {
        return comment;
    }
}
