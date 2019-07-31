package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by gangqing on 2016/3/16.
 * Email:denggangqing@ta2she.com
 */
public class TACoinIncomeBean extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        public List<CoinDetail> coinDetail;
        public int endId;

        public class CoinDetail {
            public int coinCount;
            public String createtime;
            public String des;
            public int rid;
            public String title;
            public String type;
            public String expiredTime;
        }
    }
}
