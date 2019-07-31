package com.laka.live.shopping.bean.json2bean.newversion;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingHomeBean;

/**
 * @ClassName: JTBShoppingHome
 * @Description: 商城首页
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */

public class JTBShoppingHome extends BaseBean {
    private ShoppingHomeBean data;

    public ShoppingHomeBean getData() {
        return data;
    }

    public void setData(ShoppingHomeBean data) {
        this.data = data;
    }
}
