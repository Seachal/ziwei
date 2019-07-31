package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.bean.Content;
import com.laka.live.bean.ContentBean;
import com.laka.live.bean.ContentHomeTitleBean;
import com.laka.live.bean.Course;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.CertificateInfoMsg;
import com.laka.live.msg.ContentMsg;
import com.laka.live.msg.CourseCategoryOneMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.pop.PostCoursePop;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.adapter.ContentHomeAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import framework.ioc.Ioc;
import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/9/15.
 * 内容页
 */
public class ContentFragment extends BaseFragment implements PageListLayout.OnRequestCallBack, GsonHttpConnection.OnResultListener<ContentMsg>, EventBusManager.OnEventBusListener {

    @InjectView(id = R.id.postCourse)
    public View postCourse;
    @InjectView(id = R.id.page_list_layout)
    public PageListLayout mPageListLayout;

    private boolean isApproved;
    private List<Object> mData = new ArrayList<>();
    private ContentHomeAdapter mContentHomeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        initPageList();
        mPageListLayout.loadData(); // 进来就加载内容页
    }

    @SuppressWarnings("unchecked")
    private void initPageList() {

        mContentHomeAdapter = new ContentHomeAdapter(mContext);
        mContentHomeAdapter.setData(mData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        // 设置不同item不同列数
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mContentHomeAdapter.getItemViewType(position);
                switch (itemViewType) {
                    case ContentHomeAdapter.TYPE_CATE:
                    case ContentHomeAdapter.TYPE_TITLE:
                    case ContentHomeAdapter.TYPE_TOPIC:
                        return 2;
                }
                return 1;
            }
        });
        mPageListLayout.setLayoutManager(gridLayoutManager);
        mPageListLayout.setAdapter(mContentHomeAdapter);
        mContentHomeAdapter.bindRecyclerView(mPageListLayout.getRecyclerView());
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.setIsReloadWhenEmpty(true);
        mPageListLayout.setIsLoadMoreEnable(false);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.postCourse:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                if (isApproved) {
                    PostCoursePop.getPostCoursePop(mContext).showAsDropDown(v);
                } else {
                    // 用户未认证，跳转H5进行认证
                    WebActivity.startActivity(getActivity(), Common.APPROVE_URL + Ioc.get(UserInfo.class).getIdStr(), getString(R.string.auth_info));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DataProvider.getApprove(this, new GsonHttpConnection.OnResultListener<CertificateInfoMsg>() {
            @Override
            public void onSuccess(CertificateInfoMsg certificateInfoMsg) {
                if (certificateInfoMsg.isSuccessFul()) {
                    isApproved = certificateInfoMsg.data.isApproved();
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
            }
        });
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        getCategoryData();
        return null;
    }

    private void getCategoryData() {

        DataProvider.getCourseCategoryOne(this, new GsonHttpConnection.OnResultListener<CourseCategoryOneMsg>() {
            @Override
            public void onSuccess(CourseCategoryOneMsg courseCategoryOneMsg) {
                mData.clear();
                // 查询完分类，再加载分类内容
                DataProvider.getCourseContent(this, ContentFragment.this);
                mData.add(courseCategoryOneMsg);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.log("getContentHome failed . errorStr : " + errorMsg + " ; code : " + errorCode);
                ToastHelper.showToast(R.string.homepage_network_error_retry);

                mPageListLayout.showNetWorkError();
                mPageListLayout.onFinishLoading(false, false);
                mPageListLayout.setOnLoadMoreComplete();
                mPageListLayout.setOnRefreshComplete();
            }
        });
    }

    @Override
    public void onSuccess(ContentMsg contentMsg) {
        mPageListLayout.onFinishLoading(false, false);
        mPageListLayout.setOnLoadMoreComplete();
        mPageListLayout.setOnRefreshComplete();

        if (contentMsg == null) {
            mPageListLayout.showEmpty();
            return;
        }

        List<ContentBean> contentBeanList = contentMsg.getContentBeanList();

        if (contentBeanList == null) {
            mPageListLayout.showEmpty();
            return;
        }

        // 直播列表
        if (contentMsg.getLiveList() != null) {
            ContentBean liveList = contentMsg.getLiveList();
            mData.add(new ContentHomeTitleBean(liveList));
            mData.addAll(liveList.getItems());
        }

        // 视频列表
        if (contentMsg.getVideoList() != null) {
            ContentBean videoList = contentMsg.getVideoList();
            mData.add(new ContentHomeTitleBean(videoList));
            mData.addAll(videoList.getItems());
        }

        // 资讯列表
        if (contentMsg.getNewsList() != null) {
            ContentBean newsList = contentMsg.getNewsList();
            mData.add(new ContentHomeTitleBean(newsList));
            for (Content content : newsList.getItems()) {
                content.setType(Course.NEWS);
            }
            mData.addAll(newsList.getItems());
        }

        // 专题列表
        if (contentMsg.getTopicsList() != null) {
            ContentBean topicsList = contentMsg.getTopicsList();
            mData.add(new ContentHomeTitleBean(topicsList));
            mData.addAll(topicsList.getItems());
        }

        if (Utils.listIsNullOrEmpty(mData)) {
            mPageListLayout.showEmpty();
        } else {
            mPageListLayout.showData();
            mContentHomeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        Log.log("getContentHome failed . errorStr : " + errorMsg + " ; code : " + errorCode);
        ToastHelper.showToast(R.string.homepage_network_error_retry);

        mPageListLayout.showNetWorkError();
        mPageListLayout.onFinishLoading(false, false);
        mPageListLayout.setOnLoadMoreComplete();
        mPageListLayout.setOnRefreshComplete();
    }

    // 开始测试直播
    public void startTestLive() {
        PostCoursePop.getPostCoursePop(mContext).getTestPushUrl();
    }


    @Subscribe
    @Override
    public void onEvent(PostEvent event) {
        if (TextUtils.equals(event.tag, SubcriberTag.CONTENT_TAB_DOUBLE_CLICK_EVENT)) {
            mPageListLayout.autoRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
    }

}