package com.laka.live.shopping.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhxu on 2015/12/22.
 * Email:357599859@qq.com
 */
public class ShoppingBrandBean implements Serializable {
    private int type;
    private int typeId;
    private String imageUrl;
    private String description;
    private List<ShoppingBrandGoodsBean> list;
    private int brandId;
    private String name;

    public int getId() {
        return brandId;
    }

    public void setId(int id) {
        this.brandId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public List<ShoppingBrandGoodsBean> getList() {
        return list;
    }
}
