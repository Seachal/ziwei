package com.laka.live.bean;

import java.util.Date;

/**
 * Created by luwies on 16/3/7.
 */
public class ChatEntity {
//    private TIMConversationType type;
    private String grpSendName;

    private long time;
    private boolean bSelf;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatEntity() {
        // TODO Auto-generated constructor stub
    }


//    public void setType(TIMConversationType type) {
//        this.type = type;
//    }
//
//    public TIMConversationType getType() {
//        return type;
//    }

    public String getSenderName() {
        return grpSendName;
    }

    public void setSenderName(String grpSendName) {
        this.grpSendName = grpSendName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getIsSelf() {
        return bSelf;
    }

    public void setIsSelf(boolean bSelf) {
        this.bSelf = bSelf;
    }




    public final static int MSG_TYPE_TEXT = 0;//评论
    public final static int MSG_TYPE_GIFT = 1;//礼物
    public final static int MSG_TYPE_LIKE = 2;//点亮
    public final static int MSG_TYPE_COME = 3;//来了
    public final static int MSG_TYPE_SYSTEM = 4;//系统消息
    public final static int MSG_TYPE_FORBID_SAY = 6;//禁言
    public final static int MSG_TYPE_ATTENTION = 7;//关注消息
    public final static int MSG_TYPE_SHARE= 8;//分享消息
    public final static int MSG_TYPE_ZHUBO_STATUS= 9;//主播状态
    public final static int MSG_TYPE_PICTURE = 10 ; //图片消息

    public final static int MSG_STATE_SENDING = 3;
    public final static int MSG_STATE_SUCCESS = 1;
    public final static int MSG_STATE_FAIL = 2;

    private Long id;
    private int type; // 0-text  1-gift
    private int state; // 0-sending | 1-success | 2-fail
    private String fromUserName;
    private String fromUserAvatar;
    private String toUserName;
    private String toUserAvatar;
    private String content;//内容
    private String userId;
    private Boolean isSend;
    private Boolean sendSucces;

    // 用户等级
    public int level = 0;
    //礼物图标 本地资源
    private int giftRes;

    //礼物图标 url
    private String giftUrl;

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGiftRes() {
        return giftRes;
    }

    public void setGiftRes(int giftRes) {
        this.giftRes = giftRes;
    }

    public ChatEntity(int type, int state, String fromUserName,
                      String fromUserAvatar, String toUserName, String toUserAvatar,
                      String content, Boolean isSend, Boolean sendSucces, Date time) {
        super();
        this.type = type;
        this.state = state;
        this.fromUserName = fromUserName;
        this.fromUserAvatar = fromUserAvatar;
        this.toUserName = toUserName;
        this.toUserAvatar = toUserAvatar;
        this.content = content;
        this.isSend = isSend;
        this.sendSucces = sendSucces;
    }

    public int getLevel() {
        if(level==0){
            level = 1;
        }
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserAvatar() {
        return toUserAvatar;
    }

    public void setToUserAvatar(String toUserAvatar) {
        this.toUserAvatar = toUserAvatar;
    }


    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public Boolean getSendSucces() {
        return sendSucces;
    }

    public void setSendSucces(Boolean sendSucces) {
        this.sendSucces = sendSucces;
    }


}
