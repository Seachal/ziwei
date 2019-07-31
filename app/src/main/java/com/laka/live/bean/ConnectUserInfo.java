package com.laka.live.bean;

/**
 * Created by luwies on 16/10/27.
 */

public class ConnectUserInfo extends UserInfo {

    /**
     * 未连接
     */
    public static final int STATE_NONE = 0;

    /**
     * 主播确认连麦等待观众确认
     */
    public static final int STATE_ANCHOR_WAITING_CONFIRM = 1;

    /**
     * 观众确认连麦等待主播确认
     */
    public static final int STATE_AUDIENCE_WAITING_CONFIRM = 2;

    /**
     * 连麦已确认等待连麦成功
     */
    public static final int STATE_WAITING_SUCCESS = 3;

    /**
     * 连麦成功
     */
    public static final int STATE_CONNECT_SUCCESS = 7;

    /**
     * 连麦失败
     */
    public static final int STATE_CONNECT_FAIL = 14;

    private int state = STATE_NONE;

    private boolean isInConnectMicList;

    private boolean isManager;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isInConnectMicList() {
        return isInConnectMicList;
    }

    public void setInConnectMicList(boolean inConnectMicList) {
        isInConnectMicList = inConnectMicList;
    }

    public void setInConnectMicList(byte inConnectMicList) {
        setInConnectMicList(inConnectMicList == 1);
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public void setManager(byte flag) {
        setManager(flag == 1);
    }
}
