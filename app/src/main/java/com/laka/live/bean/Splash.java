package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * @ClassName: Splash
 * @Description: 闪屏
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/09/2017
 */

public class Splash {
    public final static int TYPE_COURSE = 1;
    public final static int TYPE_URL = 2;

    @Expose
    @SerializedName(Common.TYPE)
    private int type;

    @Expose
    @SerializedName(Common.VALUE)
    private String value;

    @Expose
    @SerializedName(Common.IMG)
    private String img;

    @Expose
    @SerializedName(Common.TITLE)
    private String title;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Splash{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
