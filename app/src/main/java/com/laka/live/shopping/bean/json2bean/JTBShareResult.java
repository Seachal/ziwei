package com.laka.live.shopping.bean.json2bean;


import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.LevelInfoBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/3/16.
 * Email:357599859@qq.com
 */
public class JTBShareResult extends BaseBean implements Serializable {

    private LevelInfoBean data;

    public LevelInfoBean getData() {
        return data;
    }
}
