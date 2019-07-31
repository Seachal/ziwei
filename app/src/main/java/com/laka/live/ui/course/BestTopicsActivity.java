package com.laka.live.ui.course;

import com.laka.live.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.account.AccountInfoManager;
import com.laka.live.msg.BestTopicMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.bean.newversion.ShoppingHomeTopics;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.BestTopicsAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;
import com.orhanobut.logger.Logger;

import framework.ioc.annotation.InjectExtra;
import framework.ioc.annotation.InjectView;

import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 优选专题列表(专题详情列表页)
 */
public class BestTopicsActivity extends BaseActivity implements PageListLayout.OnRequestCallBack, PageListLayout.OnResultListener,
        View.OnClickListener {

    @InjectView(id = R.id.title)
    public TextView titleTv;
    @InjectView(id = R.id.iv_topics_share)
    public ImageView mIvShare;
    @InjectView(id = R.id.page_list_layout)
    public PageListLayout mPageListLayout;

    @InjectExtra(name = "topicId")
    public Integer topicId;

    /**
     * description:业务改变，需要加一个微信分享链接
     **/
    private String shareUrl;

    private ShoppingHomeTopics bestTopicBean;
    private BestTopicsAdapter mBestTopicsAdapter;
    private List<Object> mData = new ArrayList<>();

    public static void startActivity(Context context, int topicId) {
        Intent intent = new Intent(context, BestTopicsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("topicId", topicId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_topics);
        initView();
    }

    private void initView() {
        mIvShare.setOnClickListener(this);
        initPageList();
        mPageListLayout.loadData(); // 进来就加载内容页
    }

    @SuppressWarnings("unchecked")
    private void initPageList() {
        mBestTopicsAdapter = new BestTopicsAdapter(mContext);
        mBestTopicsAdapter.setData(mData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position > 0 ? 1 : 2;
            }
        });
        mPageListLayout.setLayoutManager(gridLayoutManager);
        mPageListLayout.setAdapter(mBestTopicsAdapter);
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.setIsReloadWhenEmpty(true);
        mPageListLayout.setIsLoadMoreEnable(false);
        mPageListLayout.setOnResultListener(this);
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.getCourseTopicsDetail(this, topicId, page, listener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_topics_share:
                shareUrl = Common.TOPIC_SHARE_URL_PREFIX + Common.COURSE_TOPIC_ID + "=" + topicId;
                String shareCoverUrl = "";
                if (bestTopicBean != null && !TextUtils.isEmpty(bestTopicBean.getCoverUrl())) {
                    shareCoverUrl = bestTopicBean.getCoverUrl();
                }
                showShareDialog(shareUrl,
                        titleTv.getText().toString(),
                        "发现有滋有味的生活",
                        shareCoverUrl,
                        false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResult(Object o) {
        BestTopicMsg bestTopicMsg = (BestTopicMsg) o;
        if (bestTopicBean == null) {
            bestTopicBean = new ShoppingHomeTopics();
            bestTopicBean.setTopicId(topicId);
            bestTopicBean.setTitle(bestTopicMsg.getData().getTitle());
            bestTopicBean.setCoverUrl(bestTopicMsg.getData().getCoverUrl());
            titleTv.setText(bestTopicBean.getTitle());
            mBestTopicsAdapter.setBestTopicBean(bestTopicBean);
        }
    }

}
