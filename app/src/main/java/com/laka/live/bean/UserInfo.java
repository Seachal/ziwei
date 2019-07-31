package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.util.Common;
import com.laka.live.util.Log;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luwies on 16/3/7.
 */
public class UserInfo extends ListUserInfo implements Serializable {

    private static String TAG = "UserInfo";

    private static final int NO_BLOCK = 0;

    private static final int HAS_BLOCK = 1;

    @Expose
    @SerializedName(Common.FORBIDDEN_STATE)
    private int forbid_say;

    @Expose
    @SerializedName(Common.SADMIN)
    public int sadmin;

    @Expose
    @SerializedName("live_state")
    private int liveState; // 直播状态，0=未直播，1=直播中，2=直播暂时离开

    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.PHONE)
    private String phone;

    @Expose
    @SerializedName(Common.WECHAT_ID)
    private String wechatId;

    @Expose
    @SerializedName(Common.WEIBO_ID)
    private String weiboId;

    @Expose
    @SerializedName(Common.QQ_ID)
    private String qqId;


    @Expose
    @SerializedName(Common.CHANNEL)
    private String channel;


    /**
     * 地区
     */
    @Expose
    @SerializedName(Common.REGION)
    private String region;

    @Expose
    @SerializedName(Common.TOKEN)
    private String token;

    @Expose
    @SerializedName(Common.TAG)
    private String tag;

    /**
     * 经验值
     */
    @Expose
    @SerializedName(Common.EXPS)
    private long exps;


    @Expose
    @SerializedName(Common.COINS)
    private double coins;

    @Expose
    @SerializedName(Common.RECV_COINS)
    private double recvCoins;

    @Expose
    @SerializedName(Common.TOTAL_COINS)
    private double totalCoins;

    @Expose
    @SerializedName(Common.GIVE_COINS)
    private double giveCoins;

    @Expose
    @SerializedName(Common.NEW_COINS)
    private double new_coins;

    @Expose
    @SerializedName(Common.VIDEOS)
    private int videos;

    @Expose
    @SerializedName(Common.FOLLOWS)
    private int follows;

    @Expose
    @SerializedName(Common.FANS)
    private int fans;

    @Expose
    @SerializedName(Common.SEND_RANKING_TOP3)
    private List<ListUserInfo> sendRankList;

    private String userSig;

    private boolean isCreater;

    @Expose
    @SerializedName(Common.WITHDRAW_WECHAT_ID)
    private int withdrawWechatId;

    @Expose
    @SerializedName(Common.WITHDRAW_PHONE)
    private int withdrawPhone;

    @Expose
    @SerializedName(Common.FROZEN_COINS)
    private double frozenCoins; //冻结帐户

    /**
     * 夜间是否消息提醒
     */
    @Expose
    @SerializedName(Common.DO_NOT_DISTURB_SLEEP)
    private int remindInNight;

    /**
     * 关注用户直播提醒
     */
    @Expose
    @SerializedName(Common.REMIND_FOLLOW_OPEN_ROOM)
    private int remindFollowLive;

    /**
     * 关注我提醒
     */
    @Expose
    @SerializedName(Common.REMIND_FOLLOW_ME)
    private int remindFollowMe;

    /**
     * 消息提醒
     */
    @Expose
    @SerializedName(Common.REMIND_MESSAGE)
    private int remindMessageInCome;

    /**
     * 只在Wi-Fi环境下直播
     */
    @Expose
    @SerializedName(Common.LIVE_ONLY_WIFI)
    private int liveOnlyWifi;

//    @Expose
//    @SerializedName(Common.RECV_COINS)
//    public long recv_coins; // 可用账户

    @Expose
    @SerializedName(Common.BLOCK)
    private int block;

    @Expose
    @SerializedName(Common.CASH_WITHDRAWAL)
    private long cashWithdrawal;

    @Expose
    @SerializedName(Common.IS_FIRST)
    private int isFirst;

    @Expose
    @SerializedName(Common.BOOKING_COURSE_COUNT)
    private String bookingCourseCount;

    @Expose
    @SerializedName(Common.RELEASE_COURSE_COUNT)
    private String releaseCourseCount;

    /**
     * description:素材库
     **/
    @Expose
    @SerializedName("mini_video_count")
    private String materialCount;

    @Expose
    @SerializedName("profit_level")
    private int profitLevel; // 分销的代理等级

    public boolean isForbid_say() {
        return forbid_say == 1;
    }

    public void setForbid_say(boolean forbid_say) {
        this.forbid_say = forbid_say ? 1 : 0;
    }

    // 是否是超级管理员
    public boolean isAdministrator() {
        return sadmin == 1;
    }

    public void setSadmin(boolean sadmin) {
        this.sadmin = sadmin ? 1 : 0;
    }

    public boolean isCreater() {
        return isCreater;
    }

    public void setIsCreater(boolean isCreater) {
        this.isCreater = isCreater;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        if (phone == null) {
            return "";
        }
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getExps() {
        return exps;
    }

    public void setExps(long exps) {
        this.exps = exps;
    }

//    public int getRecvLikes() {
//        return recvLikes;
//    }
//
//    public void setRecvLikes(int recvLikes) {
//        this.recvLikes = recvLikes;
//    }

    public int getVideos() {
        return videos;
    }

    public void setVideos(int videos) {
        this.videos = videos;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getUserSig() {
        return userSig;
    }

    public List<ListUserInfo> getSendRankList() {
        return sendRankList;
    }

    public void setSendRankList(List<ListUserInfo> sendRankList) {
        this.sendRankList = sendRankList;
    }


    public void setBindPhone(boolean isBind) {
        withdrawPhone = isBind ? 1 : 0;
    }

    public boolean isBindPhone() {
        return withdrawPhone == 1;
    }

    public void setBindWeChat(boolean bind) {
        withdrawWechatId = bind ? 1 : 0;
    }

    public boolean isBindWeChat() {
        return withdrawWechatId == 1;
    }


    public boolean isRemindMessageInCome() {
        return remindMessageInCome == 1;
    }

    public void setRemindMessageInCome(boolean remindMessageInCome) {
        this.remindMessageInCome = remindMessageInCome ? 1 : 0;
    }

    public boolean isLiveOnlyWifi() {
        return liveOnlyWifi == 1;
    }

    public void setLiveOnlyWifi(boolean liveOnlyWifi) {
        this.liveOnlyWifi = liveOnlyWifi ? 1 : 0;
    }

    public boolean isRemindInNight() {
        return remindInNight == 1;
    }

    public void setRemindInNight(boolean remindInNight) {
        this.remindInNight = remindInNight ? 1 : 0;
    }

    public boolean isRemindFollowLive() {
        return remindFollowLive == 1;
    }

    public void setRemindFollowLive(boolean remindFollowLive) {
        this.remindFollowLive = remindFollowLive ? 1 : 0;
    }

    public boolean isRemindFollowMe() {
        return remindFollowMe == 1;
    }

    public void setRemindFollowMe(boolean remindFollowMe) {
        this.remindFollowMe = remindFollowMe ? 1 : 0;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public boolean isBlock() {
        return block == HAS_BLOCK;
    }

    public void setIsBlock(boolean isBlock) {
        block = isBlock ? HAS_BLOCK : NO_BLOCK;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public double getRecvCoins() {
        return recvCoins;
    }

    public void setRecvCoins(double recvCoins) {
        this.recvCoins = recvCoins;
    }

    public double getGiveCoins() {
        return giveCoins;
    }

    public void setGiveCoins(float giveCoins) {
        this.giveCoins = giveCoins;
    }

    public long getCashWithdrawal() {
        return cashWithdrawal;
    }

    public void setCashWithdrawal(long cashWithdrawal) {
        this.cashWithdrawal = cashWithdrawal;
    }

    public double getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(double totalCoins) {
        this.totalCoins = totalCoins;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public String getBookingCourseCount() {
        return bookingCourseCount;
    }

    public void setBookingCourseCount(String bookingCourseCount) {
        this.bookingCourseCount = bookingCourseCount;
    }

    public String getReleaseCourseCount() {
        return releaseCourseCount;
    }

    public void setReleaseCourseCount(String releaseCourseCount) {
        this.releaseCourseCount = releaseCourseCount;
    }

    public double getFrozenCoins() {
        return frozenCoins;
    }

    public void setFrozenCoins(int frozenCoins) {
        this.frozenCoins = frozenCoins;
    }

    // 保存用户信息
    public void save() {
        AccountInfoManager.getInstance().saveAccountInfo(this);
    }

    public double getNew_coins() {
        return new_coins;
    }

    public void setNew_coins(double new_coins) {
        this.new_coins = new_coins;
    }


    // 当前用户是否在直播
    public boolean isLiving() {
        return liveState == 1 || liveState == 2;
    }

    public int getLiveState() {
        return liveState;
    }

    public void setLiveState(int liveState) {
        this.liveState = liveState;
    }

    // 是否有分销等级
    public boolean hasProfitLevel() {
        return profitLevel > 0;
    }

    public String getMaterialCount() {
        return materialCount == null ? "0" : materialCount;
    }

    public void setMaterialCount(String materialCount) {
        this.materialCount = materialCount;
    }

}
