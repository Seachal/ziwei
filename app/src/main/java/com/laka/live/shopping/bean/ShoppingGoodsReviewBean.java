package com.laka.live.shopping.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhxu on 2015/12/23.
 * Email:357599859@qq.com
 */
public class ShoppingGoodsReviewBean implements Serializable {

    private List<ShoppingCommentBean> reviews;
    private String endId;
    private ShoppingSummaryBean summary;

    public List<ShoppingCommentBean> getReviews() {
        return reviews;
    }

    public void setReviews(List<ShoppingCommentBean> reviews) {
        this.reviews = reviews;
    }

    public String getEndId() {
        return endId;
    }

    public void setEndId(String endId) {
        this.endId = endId;
    }

    public ShoppingSummaryBean getSummary() {
        return summary;
    }

    public void setSummary(ShoppingSummaryBean summary) {
        this.summary = summary;
    }
}
