package com.laka.live.shopping.bean;

/**
 * Created by zhangwulin on 2016/3/16.
 * email 1501448275@qq.com
 */
public class SelfTaCoinJsonBean extends BaseBean {
    private SelfTaCoinBean data;

    public SelfTaCoinBean getData() {
        return data;
    }

    public void setData(SelfTaCoinBean data) {
        this.data = data;
    }

    public class SelfTaCoinBean {
        String coinCount;

        public String getCoinCount() {
            return coinCount;
        }

        public void setCoinCount(String coinCount) {
            this.coinCount = coinCount;
        }
    }
}
