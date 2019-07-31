package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by gangqing on 2016/3/16.
 * Email:denggangqing@ta2she.com
 */
public class TACoinMainBean extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        public int coinCount;
        public List<CoinDetail> coinDetail;

        public class CoinDetail {
            public int everCoin;
            public int everPoint;
            public boolean isGeted;
            public String type;
        }
    }
}
