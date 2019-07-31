package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/4/6.
 */
public class TransactionRecord {

    public final static int CHANNEL_MY_WALLET = 1;  //我的钱包
    public final static int CHANNEL_FREEZE_INCOME = 2;  //冻结收益
    public final static int CHANNEL_AVAILABLE_INCOME = 3;  //可用收益

    private final static String MY_WALLET = "我的钱包";
    private final static String FREEZE_INCOME = "冻结收益";
    private final static String AVAILABLE_INCOME = "可用收益";
    private final static String WALLET_BALANCE = "钱包余额";


    public static final int TYPE_RECHARGE = 1;  //充值

    public static final int TYPE_BUY_SESSION = 2;  //购买课程

    public static final int TYPE_SESSION_REFUND = 3;  //取消课程退款

    public static final int TYPE_SEND_BARRAGE = 4;  //发送弹幕

    public static final int TYPE_SEND_GIFT = 5;  //赠送礼物

    public static final int TYPE_SALE_SESSION = 6;  //售出课程

    public static final int TYPE_SESSION_SETTLEMENT = 7;  //课程结算

    public static final int TYPE_RECHARGE_WALLET = 8;  //兑换到钱包

    public static final int TYPE_WITHDRAW_CHECKING = 9;  //提现审核中

    public static final int TYPE_WITHDRAW_FAILED = 10;  //提现失败

    public static final int TYPE_WIRHDRAW_SUCCESS = 11;  //提现成功

    @Expose
    @SerializedName(Common.CHANNEL)
    private int channel;

    @Expose
    @SerializedName(Common.TYPE)
    private int type;

    @Expose
    @SerializedName(Common.SUMMARY)
    private String summary;

    @Expose
    @SerializedName(Common.TIME)
    private String time;

    @Expose
    @SerializedName(Common.REST_COINS)
    private float restCoins;

    @Expose
    @SerializedName(Common.TRANS_COINS)
    private float transCoins;

    private int year;

    private int month;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getChannelStr() {
        switch (channel) {
            case CHANNEL_MY_WALLET:
                return MY_WALLET;
            case CHANNEL_FREEZE_INCOME:
                return FREEZE_INCOME;
            default:
                return AVAILABLE_INCOME;
        }
    }

    public String getChannelStr2() {
        switch (channel) {
            case CHANNEL_MY_WALLET:
                return WALLET_BALANCE;
            case CHANNEL_FREEZE_INCOME:
                return FREEZE_INCOME;
            default:
                return AVAILABLE_INCOME;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getRestCoins() {
        return restCoins;
    }

    public void setRestCoins(float restCoins) {
        this.restCoins = restCoins;
    }

    public float getTransCoins() {
        return transCoins;
    }

    public void setTransCoins(float transCoins) {
        this.transCoins = transCoins;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "channel=" + channel +
                ", type=" + type +
                ", summary='" + summary + '\'' +
                ", time='" + time + '\'' +
                ", restCoins=" + restCoins +
                ", transCoins=" + transCoins +
                '}';
    }
}
