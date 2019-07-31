package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by zhxu on 2016/3/23.
 * Email:357599859@qq.com
 */
public class ShoppingNewCategoryBean {

    private String title;
    private List<ShoppingNewCategoryColumnBean> column;
    private int styleType;

    public String getTitle() {
        return title;
    }

    public List<ShoppingNewCategoryColumnBean> getColumn() {
        return column;
    }

    public int getStyleType() {
        return styleType;
    }
}
