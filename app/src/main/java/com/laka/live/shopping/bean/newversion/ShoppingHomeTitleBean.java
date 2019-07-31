package com.laka.live.shopping.bean.newversion;

/**
 * @ClassName: ShoppingHomeTitleBean
 * @Description: 首页标题
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */

public class ShoppingHomeTitleBean {
    public final static int TYPE_HOT_GOODS = 0;
    public final static int TYPE_TOPICS = 1;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
