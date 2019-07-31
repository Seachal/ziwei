package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by Lyf on 2017/4/12.
 */

public class PayCourse {


    @Expose
    @SerializedName(Common.DISCOUNT)
    private float discount; // 购买的课程原价总价

    @Expose
    @SerializedName(Common.TOTAL_FEE)
    private float totalFee; // 购买的课程原价总价

    @Expose
    @SerializedName(Common.ACTUAL_TOTAL_FEE)
    private float actualTotalFee; // 购买的课程优惠总价

    @Expose
    @SerializedName("actual_total_fee_usecoins")
    private float actualTotalFeeUseCoins; // 使用味豆时实际支付费用

    @Expose
    @SerializedName(Common.NICK_NAME)
    private String nickname; //  直播用户的nickname

    @Expose
    @SerializedName(Common.ENOUGH_COINS)
    private int enough_coins; //     购买者是否够钻石购买，1:足够，2：不够

    @Expose
    @SerializedName(Common.SAVED_COINS)
    private float saveCoins; // 购买的课程节省的微豆

    @Expose
    @SerializedName("reduce_coins")
    private float reduceCoins; // 扣减味豆

    @Expose
    @SerializedName(Common.PURCHASE_STATEMENT)
    private String purchase_statement; // 购买声明

    @Expose
    @SerializedName(Common.COURSE_COUNT)
    private String course_count; // 预告下课程总数量

    @Expose
    @SerializedName(Common.NOTOWNCOURSECOUNT)
    private String not_own_course_count; // 用户剩余未购买的课程数量

    @Expose
    @SerializedName(Common.BUY_COURSE_COUNT)
    private String buy_course_count; // 用户本次购买课程的数量

    @Expose
    @SerializedName(Common.COURSES_DETAIL)
    private List<Course> coursesDetail;  // 课程数组

    public float getActualTotalFee() {
        return actualTotalFee;
    }

    // 是否足够购买
    public boolean isEnoughPay(){
        return enough_coins == 1;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTotalFee() {
        return totalFee;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getEnough_coins() {
        return enough_coins;
    }

    public void setEnough_coins(int enough_coins) {
        this.enough_coins = enough_coins;
    }

    public float getSaveCoins() {
        return saveCoins;
    }

    public void setSave_coins(float saveCoins) {
        this.saveCoins = saveCoins;
    }

    public float getReduceCoins() {
        return reduceCoins;
    }

    public void setReduceCoins(float reduceCoins) {
        this.reduceCoins = reduceCoins;
    }

    public String getPurchase_statement() {
        return purchase_statement;
    }

    public void setPurchase_statement(String purchase_statement) {
        this.purchase_statement = purchase_statement;
    }

    public String getCourse_count() {
        return course_count;
    }

    public void setCourse_count(String course_count) {
        this.course_count = course_count;
    }

    public String getNot_own_course_count() {
        return not_own_course_count;
    }

    public void setNot_own_course_count(String not_own_course_count) {
        this.not_own_course_count = not_own_course_count;
    }

    public String getBuy_course_count() {
        return buy_course_count;
    }

    public void setBuy_course_count(String buy_course_count) {
        this.buy_course_count = buy_course_count;
    }

    public List<Course> getCoursesDetail() {
        return coursesDetail;
    }

    public void setCoursesDetail(List<Course> coursesDetail) {
        this.coursesDetail = coursesDetail;
    }

    public float getActualTotalFeeUseCoins() {
        return actualTotalFeeUseCoins;
    }
}
