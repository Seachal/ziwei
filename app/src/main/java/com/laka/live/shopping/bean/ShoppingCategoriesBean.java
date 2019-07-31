package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by zhxu on 2016/5/3.
 * Email:357599859@qq.com
 */
public class ShoppingCategoriesBean {

    private int styleType;
    private String title;
    private List<ShoppingCategoriesColumnBean> column;

    public int getStyleType() {
        return styleType;
    }

    public String getTitle() {
        return title;
    }

    public List<ShoppingCategoriesColumnBean> getColumn() {
        return column;
    }
}
