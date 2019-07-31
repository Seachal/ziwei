package com.laka.live.shopping.bean;


import java.io.Serializable;

/**
 * Created by zhxu on 2016/5/3.
 * Email:357599859@qq.com
 */
public class ShoppingTopBean implements Serializable {

    private int type;
    private int typeId;
    private int toplistId;
    private int cateId;
    private String imageUrl;
    private String title;

    public int getType() {
        return type;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getToplistId() {
        return toplistId;
    }

    public int getCateId() {
        return cateId;
    }

    public String getTitle() {
        return title;
    }
}
