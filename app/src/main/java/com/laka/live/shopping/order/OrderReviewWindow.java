/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.DeviceManager;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.NotificationCenter;
import com.laka.live.shopping.framework.adapter.MenuItemIdDef;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.framework.ui.DefaultTitleBar;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpCode;
import com.laka.live.shopping.order.model.OrderListRequest;
import com.laka.live.shopping.order.model.OrderReviewInfo;
import com.laka.live.ui.widget.AlphaImageView;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderReviewWindow extends DefaultWindow implements View.OnClickListener {

    private final static String TAG = "OrderCommentWindow";

    private Activity mContext;
    private AbstractWindow mWindow;

    private OrderListRequest mOrderListRequest;

    private OrderReviewInfo mReviewInfo;

    private SimpleDraweeView sdvImg;
    private RatingBar mRating;
    private EditText mEditContent;
    private AlphaImageView mAnony;

    private boolean mIsAnony = false;

    public OrderReviewWindow(Context context, IDefaultWindowCallBacks callBacks, OrderReviewInfo reviewInfo) {
        super(context, callBacks);
        mContext = (Activity) context;
        mWindow = this;
        mReviewInfo = reviewInfo;

        initUI();
        initActionBar();
    }

    private void initUI() {
        setTitle(ResourceHelper.getString(R.string.order_btn_review));
        View view = View.inflate(mContext, R.layout.order_review, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

        sdvImg = (SimpleDraweeView) findViewById(R.id.order_review_goods_img);
        sdvImg.setAspectRatio(1);
        sdvImg.setImageURI(Uri.parse(mReviewInfo.goodsImg));
        mRating = (RatingBar) findViewById(R.id.order_review_rating);
        mEditContent = (EditText) findViewById(R.id.order_review_content);
        mAnony = (AlphaImageView) findViewById(R.id.order_review_anony);
        mAnony.setOnClickListener(this);
    }

    private void initActionBar() {
        ArrayList<TitleBarActionItem> actionList = new ArrayList<>(1);
        TitleBarActionItem item = new TitleBarActionItem(getContext());
        item.setItemId(MenuItemIdDef.TITLEBAR_ORDER_REVIEW_PUSH);
        item.setText(ResourceHelper.getString(R.string.order_review_push));
        actionList.add(item);
        DefaultTitleBar titleBar = (DefaultTitleBar) getTitleBar();
        titleBar.setActionItems(actionList);
    }

    private void notifyOrderReviewPush(int goodId) {
        Notification notification = Notification.obtain(NotificationDef.N_ORDER_REVIEW_PUSH_SUCCESS);
        notification.extObj = goodId;
        NotificationCenter.getInstance().notify(notification);
    }

    @Override
    protected void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
        } else if (stateFlag == STATE_ON_DETACH) {
            DeviceManager.getInstance().hideInputMethod();
        }
    }

    @Override
    public void notify(Notification notification) {
    }

    @Override
    public void onTitleBarActionItemClick(int itemId) {
        if (itemId == MenuItemIdDef.TITLEBAR_ORDER_REVIEW_PUSH) {
            if (mRating.getRating() == 0) {
                ToastHelper.showToast( R.string.order_review_error4);
                return;
            }
            String content = mEditContent.getText() + "";
            if (StringUtils.isEmpty(content)) {
                ToastHelper.showToast(  R.string.order_review_hint);
                return;
            }
            mReviewInfo.isAnony = mIsAnony ? 1 : 0;
            mReviewInfo.content = content;
            mReviewInfo.rating = mRating.getRating();
            postGoodsReview();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.order_review_anony) {
            mAnony.setSelected(mIsAnony ? false : true);
            mIsAnony = mIsAnony ? false : true;
        }
    }

    /*******************
     * 接口访问
     ********************/
    private void postGoodsReview() {
        mOrderListRequest = OrderListRequest.getInstance();
        mOrderListRequest.postGoodsReview(mReviewInfo, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, result);
                }

                //统计PRODUCT_REVIEW_POST
//                StatsModel.stats(StatsKeyDef.PRODUCT_REVIEW_POST, "spec", "OrderGoodID:" + mReviewInfo.orderGoodsId);

                // TODO: 2017/7/14 分享
//                if (obj instanceof JTBShareResult) {
//                    JTBShareResult shareResult = (JTBShareResult) obj;
//                    if (shareResult != null && shareResult.getStatus() == HttpCode.SUCCESS) {
//                        ToastHelper.showToast(mContext, shareResult.getData(), ResourceHelper.getString(R.string.order_review_success), TaCoinToast.TYPE_COMMENT_PRODUCT);
//                    }
//                    close();
//                }
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                if (code == HttpCode.ERROR_CODE_200) {
                    ToastHelper.showToast(  R.string.order_review_error1);
                } else if (code == HttpCode.ERROR_CODE_602) {
                    ToastHelper.showToast(  R.string.order_review_error2);
                }
                close();
            }
        });
    }

    private void close() {
        notifyOrderReviewPush(mReviewInfo.orderGoodsId);
        mCallBacks.onWindowExitEvent(mWindow, false);
    }
}
