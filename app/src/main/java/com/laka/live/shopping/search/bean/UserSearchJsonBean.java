package com.laka.live.shopping.search.bean;

import java.util.List;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 5/17/17
 */

public class UserSearchJsonBean {
    private int status;
    private String msg;
    private List<UserSearchBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<UserSearchBean> getData() {
        return data;
    }

    public void setData(List<UserSearchBean> data) {
        this.data = data;
    }
}
