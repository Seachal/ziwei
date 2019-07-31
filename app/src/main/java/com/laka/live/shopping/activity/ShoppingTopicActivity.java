package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.adapter.ShoppingTopicAdapter;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingTopicGoods;
import com.laka.live.shopping.bean.newversion.ShoppingTopicGoodsBean;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ShoppingTopicActivity
 * @Description: 商城专题
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */
public class ShoppingTopicActivity extends BaseActivity implements PageListLayout.OnRequestCallBack {
    private final static String TAG = ShoppingTopicActivity.class.getSimpleName();

    private final static String TITLE = "title";
    private final static String TOPIC_ID = "topicId";

    private int mTopicId = -1;
    private String mTitle;
    private String shareUrl;

    private HeadView mHeadView;

    private PageListLayout mPageListLayout;
    private ShoppingTopicAdapter mAdapter;
    private List<Object> mTopicList = new ArrayList<>();

    public static void startActivity(Context context, String title, int topicId) {
        Intent intent = new Intent(context, ShoppingTopicActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(TOPIC_ID, topicId);

        ActivityCompat.startActivity(context, intent, null);
    }

    public static void startActivity(Context context, int topicId) {
        Intent intent = new Intent(context, ShoppingTopicActivity.class);
        intent.putExtra(TOPIC_ID, topicId);

        ActivityCompat.startActivity(context, intent, null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_topic);

        if (getIntent() == null) {
            finish();
        }


        mTitle = getIntent().getStringExtra(TITLE);
        mTopicId = getIntent().getIntExtra(TOPIC_ID, -1);

        initUI();
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        getShoppingTopicList(page);
        return null;
    }

    private void initUI() {
        mHeadView = (HeadView) findViewById(R.id.head_view);
        mHeadView.setTitle(mTitle);
        mHeadView.setBackTextShow(false);
        mHeadView.setBackShow(true);
        mHeadView.setRightIconShow(true);
        mHeadView.setRightIcon(R.drawable.nav_icon_share);
        mHeadView.setRightIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUrl = Common.TOPIC_SHARE_URL_PREFIX + Common.GOODS_TOPIC_ID + "=" + mTopicId;
                String shareCoverUrl = "";
                if (Utils.isNotEmpty(mTopicList)) {
                    shareCoverUrl = mTopicList.get(0).toString();
                }
                showShareDialog(shareUrl,
                        mTitle,
                        "发现有滋有味的生活",
                        shareCoverUrl,
                        false);
            }
        });

        mPageListLayout = (PageListLayout) findViewById(R.id.page_list_layout);
        mPageListLayout.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ShoppingTopicAdapter(this, mTopicList);
        mPageListLayout.setAdapter(mAdapter);
        mPageListLayout.setOnRequestCallBack(this);

        mPageListLayout.loadData();
    }

    /*******************
     * 接口访问
     ********************/
    private void getShoppingTopicList(final int page) {
        ShoppingRequest.getInstance().getShoppingTopicGoods(mTopicId, page, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                Log.d(TAG, "getShoppingTopicGoods success . " + result);

                mPageListLayout.refreshComplete();
                mPageListLayout.setOnLoadMoreComplete();

                if (obj == null || !(obj instanceof JTBShoppingTopicGoods)) {
                    mPageListLayout.onFinishLoading(false, false);

                    if (Utils.listIsNullOrEmpty(mTopicList)) {
                        mPageListLayout.showEmpty();
                    } else {
                        mPageListLayout.showData();
                    }

                    return;
                }

                ShoppingTopicGoodsBean topicGoodsBean = ((JTBShoppingTopicGoods) obj).getData();

                if (topicGoodsBean == null || Utils.listIsNullOrEmpty(topicGoodsBean.getGoods())) {
                    mPageListLayout.onFinishLoading(false, false);

                    if (Utils.listIsNullOrEmpty(mTopicList)) {
                        mPageListLayout.showEmpty();
                    } else {
                        mPageListLayout.showData();
                    }

                    return;
                }

                if (page == 0) {
                    mTopicList.clear();
                    mTopicList.add(0, topicGoodsBean.getImageUrl());
                }

                mTopicList.addAll(topicGoodsBean.getGoods());
                mAdapter.notifyDataSetChanged();

                if (mTitle == null) {
                    mTitle = topicGoodsBean.getTitle();
                    mHeadView.setTitle(mTitle);
                }

//                mTopicId = topicGoodsBean.getTopicId();

                mPageListLayout.showData();
                mPageListLayout.onFinishLoading(topicGoodsBean.getGoods().size() >= ShoppingRequest.LIMIT, false);
                mPageListLayout.addCurrentPage();
            }

            @Override
            public void onError(String errorStr, int code) {
                mPageListLayout.refreshComplete();
                mPageListLayout.setOnLoadMoreComplete();

                ToastHelper.showToast(R.string.homepage_network_error_retry);

                if (Utils.listIsNullOrEmpty(mTopicList)) {
                    mPageListLayout.showNetWorkError();
                    mPageListLayout.onFinishLoading(false, false);
                } else {
                    showData();
                    mPageListLayout.onFinishLoading(true, false);
                }
            }
        });
    }

}
