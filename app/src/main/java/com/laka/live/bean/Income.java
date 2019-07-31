package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/4/5.
 */
public class Income {

    @Expose
    @SerializedName(Common.TODAY)
    public float todayEarn;

    @Expose
    @SerializedName(Common.MONTH)
    public float monthEarn;

    @Expose
    @SerializedName(Common.CASH_WITHDRAWAL)
    public float cashWithdrawal;

    @Expose
    @SerializedName(Common.CASH_WITHDRAWAL_TODAY)
    public float todayCashWithdrawal;

    @Expose
    @SerializedName(Common.TOTAL_CASH_WITHDRAWAL)
    public float total_cash_withdrawal; // 可提现收益（元）

    @Expose
    @SerializedName(Common.TODAY_CASH_WITHDRAWAL)
    public float today_cash_withdrawal; // 本次可转出

    @Expose
    @SerializedName(Common.RECV_COINS)
    public float recv_coins; // 可用账户

    @Expose
    @SerializedName(Common.FROZEN_COINS)
    public float frozenCoins; //冻结帐户

    @Expose
    @SerializedName(Common.DAY_CASH_WITHDRAWAL_REMAIN)
    public float day_cash_withdrawal_remain; // 本日还可提现的次数

    @Expose
    @SerializedName(Common.WEEK_COINS)
    public float week_coins; //今周收益的贝币
    @Expose
    @SerializedName(Common.TOTAL_EXCHANGE_COINS)
    public float total_exchange_coins; //兑换钱包-可兑换数额
    @Expose
    @SerializedName(Common.IS_CASH)
    public float is_cash; //是否开启提现按钮，1：是，2：否
    @Expose
    @SerializedName(Common.DAY_MAX_WITHDRAW_RMB)
    public float day_max_withdraw_rmb; //后台设置的每日限额
    @Expose
    @SerializedName(Common.DAY_MAX_WITHDRAW_TIMES)
    public int day_max_withdraw_times;

    /**
     * 提现每天限额
     */
    @Expose
    @SerializedName(Common.DAY_CASH_WITHDRAWAL)
    public float toDayCashWithdrawalLimit;


    @Expose
    @SerializedName(Common.EXCHANGE_COINS_MAX)
    public float maxExchangeCoins;

    @Expose
    @SerializedName(Common.IS_USER_SIGNS)
    public float isUserSigns = 0;

    public float getRecv_coins() {
        return recv_coins;
    }

    public void setRecv_coins(long recv_coins) {
        this.recv_coins = recv_coins;
    }

    public float getTodayEarn() {
        return todayEarn;
    }

    public void setTodayEarn(float todayEarn) {
        this.todayEarn = todayEarn;
    }

    public float getMonthEarn() {
        return monthEarn;
    }

    public void setMonthEarn(float monthEarn) {
        this.monthEarn = monthEarn;
    }

    public float getCashWithdrawal() {
        return cashWithdrawal;
    }

    public void setCashWithdrawal(float cashWithdrawal) {
        this.cashWithdrawal = cashWithdrawal;
    }

    public float getTodayCashWithdrawal() {
        return today_cash_withdrawal;
    }

    public void setTodayCashWithdrawal(float todayCashWithdrawal) {
        this.todayCashWithdrawal = todayCashWithdrawal;
    }

    public float getToDayCashWithdrawalLimit() {
        return toDayCashWithdrawalLimit;
    }

    public void setToDayCashWithdrawalLimit(float toDayCashWithdrawalLimit) {
        this.toDayCashWithdrawalLimit = toDayCashWithdrawalLimit;
    }

    public float getDay_cash_withdrawal_remain() {
        return day_cash_withdrawal_remain;
    }

    public void setDay_cash_withdrawal_remain(float day_cash_withdrawal_remain) {
        this.day_cash_withdrawal_remain = day_cash_withdrawal_remain;
    }

    public int getDay_max_withdraw_times() {
        return day_max_withdraw_times;
    }

    public void setDay_max_withdraw_times(int day_max_withdraw_times) {
        this.day_max_withdraw_times = day_max_withdraw_times;
    }

    public float getMaxExchangeCoins() {
        return maxExchangeCoins;
    }

    public void setMaxExchangeCoins(float maxExchangeCoins) {
        this.maxExchangeCoins = maxExchangeCoins;
    }

    public float getIsUserSigns() {
        return isUserSigns;
    }

    public void setIsUserSigns(float isUserSigns) {
        this.isUserSigns = isUserSigns;
    }

    // 是否开启提现按钮，1：是，2：否
    public boolean isCashMoney() {
        return is_cash == 0;
    }

}
