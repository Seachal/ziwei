package com.laka.live.shopping.bean;

import java.io.Serializable;

/**
 * Created by Lyf on 2017/4/24.
 */

public class StsBean extends BaseBean {

    public Sts data;

    public class Sts implements Serializable {
        public String id; // 访问oss的id AccessKeyId
        public String token; // 访问oss的Secret AccessKeySecret
        public String secret; //访问oss的Token SecurityToken
    }

}
