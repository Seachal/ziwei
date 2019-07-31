package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ShoppingGoodsBaseBean
 * @Description: 商品
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */

public class ShoppingGoodsBaseBean implements Serializable {
    private final static int INCOME_RECOMMEND = 1, INCOME_AGENT = 2;

    @Expose
    @SerializedName("goodsId")
    protected int goodsId;

    @Expose
    @SerializedName("title")
    protected String title;

    @Expose
    @SerializedName("salePrice")
    protected float salePrice;

    @Expose
    @SerializedName("marketPrice")
    protected float marketPrice;

    @Expose
    @SerializedName("saleCount")
    protected int saleCount;

    @Expose
    @SerializedName("thumbUrl")
    protected String thumbUrl;

    @Expose
    @SerializedName("income")
    protected float income;

    @Expose
    @SerializedName("income_type")
    protected int incomeType;

    @Expose
    @SerializedName("tags")
    protected List<String> tags;

    @Expose
    @SerializedName(Common.PRIMARY_CATE_ID)
    protected int primaryCateId;

    @Expose
    @SerializedName(Common.PROMOTEINCOME)
    protected float promoteIncome; // 推销分成收益

    @Expose
    @SerializedName(Common.POSTAGE_TYPE)
    protected int postageType;

    @Expose
    @SerializedName(Common.STATUS)
    protected int status;

    @Expose
    @SerializedName(Common.CATE_ID)
    protected int cateId;


    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public float getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(float marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public float getIncome() {
        return income;
    }

    public String getIncomeStr() {
        if ((income - (int) income) == 0) {
            return String.valueOf((int) income);
        } else {
            return String.valueOf(income);
        }
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public List<String> getTags() {
        if (Utils.listIsNullOrEmpty(tags)) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public int getPostageType() {
        return postageType;
    }

    public void setPostageType(int postageType) {
        this.postageType = postageType;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getPrimaryCateId() {
        return primaryCateId;
    }

    public void setPrimaryCateId(int primaryCateId) {
        this.primaryCateId = primaryCateId;
    }

    public float getPromoteIncome() {
        return promoteIncome;
    }

    public void setPromoteIncome(float promoteIncome) {
        this.promoteIncome = promoteIncome;
    }

    public int getIncomeType() {
        return incomeType;
    }

    public String getIncomeTypeStr() {
        switch (incomeType) {
            case INCOME_RECOMMEND:
                return "推荐收入";
            case INCOME_AGENT:
                return "代理收入";
            default:
                return "";
        }
    }

    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }

    public String getPromoteIncomeFormat() {
        if (promoteIncome > 0) {
            return "推荐分成￥" + promoteIncome;
        } else {
            return "";
        }
    }

    // 这个方法比较好理解
    public String getGoodsName() {
        return getTitle();
    }

}
