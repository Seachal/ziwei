package com.laka.live.bean;

import java.util.List;

/**
 * Created by luwies on 16/10/31.
 */

public class OnlineUserMessage {

    /**
     * 房间普通观众人数
     */
    private int count;

    private List<ConnectUserInfo> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ConnectUserInfo> getList() {
        return list;
    }

    public void setList(List<ConnectUserInfo> list) {
        this.list = list;
    }
}
