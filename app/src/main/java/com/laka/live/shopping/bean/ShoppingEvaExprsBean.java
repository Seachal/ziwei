package com.laka.live.shopping.bean;

/**
 * Created by linhz on 2016/5/11.
 * Email: linhaizhong@ta2she.com
 */
public class ShoppingEvaExprsBean {

    private String itemValue;//选择项的值
    private String itemCount;//选择项的总数；用于计算圆点位置
    private String imageUrl;//图片URL
    private String text;//此选项文本
    private String minText;//此选项左边文本
    private String maxText;//此选项右边文本
    private String avgText;//此选项平均值文本

    public String getItemValue() {
        return itemValue;
    }

    public String getItemCount() {
        return itemCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }

    public String getMinText() {
        return minText;
    }

    public String getMaxText() {
        return maxText;
    }

    public String getAvgText() {
        return avgText;
    }
}
