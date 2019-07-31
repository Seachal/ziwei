package com.laka.live.shopping.model;

import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/22.
 * Email:357599859@qq.com
 */
public class TemplateHolder implements Serializable {

    private int titleType;
    private int tempType;
    private Object items;

    public int getTempType() {
        return tempType;
    }

    public void setTempType(int tempType) {
        this.tempType = tempType;
    }

    public Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }

    public int getTitleType() {
        return titleType;
    }

    public void setTitleType(int titleType) {
        this.titleType = titleType;
    }
}
