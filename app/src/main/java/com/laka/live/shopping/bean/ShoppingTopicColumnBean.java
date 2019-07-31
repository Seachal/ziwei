package com.laka.live.shopping.bean;


import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/21.
 * Email:357599859@qq.com
 */
public class ShoppingTopicColumnBean implements Serializable {

    private int id;
    private int columnId;
    private String title;
    private String name;
    private int type;
    private String thumbUrl;
    private String imageUrl;
    private String headImageUrl;
    private String description;
    private int typeValue;
    private String brief;
    private int attrKey;
    private int attrValue;

    public int getId() {
        return id;
    }

    public int getColumnId() {
        return columnId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBrief() {
        return brief;
    }

    public int getAttrKey() {
        return attrKey;
    }

    public int getAttrValue() {
        return attrValue;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getTypeValue() {
        return typeValue;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }
}
