package com.laka.live.account.replay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.bean.Video;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.Msg;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.AlphaTextView;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.LoadMoreFoot;
import com.laka.live.ui.widget.LoadMoreView;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.ui.widget.PullToRefreshRecyclerView;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MyLiveReplayActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_TYPE_INIT = 1;
    private static final int REQUEST_TYPE_REFRESH = 2;
    private static final int REQUEST_TYPE_LOAD_MORE = 3;

    private LinearLayout mToolsLayout;
    private AlphaTextView mDeleteButton;
    private AlphaTextView mCloseEditButton;
    private AlphaTextView mAllCheckButton;
    private TextView mDeleteCount;
    private HeadView mHeadView;
    private ReplayAdapter mReplayAdapter;
    private LoadingLayout mLoadingLayout;
    private PullToRefreshRecyclerView mRefreshRecyclerView;

    private ReplayRequest mRequest;
    private List<VideoParams> mVideos = new ArrayList<>();
    private boolean mIsEditState = false;
    private boolean mIsAllCheck = false;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MyLiveReplayActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_replay);

        init();
        initRequest();
        tryGetReplayListData(REQUEST_TYPE_INIT);
    }

    private void initRequest() {
        mRequest = new ReplayRequest(this, AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
    }

    private void init() {
        mDeleteCount = (TextView) findViewById(R.id.replay_checked_tip);
        mToolsLayout = (LinearLayout) findViewById(R.id.replay_tools_layout);
        mAllCheckButton = (AlphaTextView) findViewById(R.id.replay_all_check_button);
        mAllCheckButton.setOnClickListener(this);
        mDeleteButton = (AlphaTextView) findViewById(R.id.replay_delete_button);
        mDeleteButton.setOnClickListener(this);
        mCloseEditButton = (AlphaTextView) findViewById(R.id.replay_close_edit_button);
        mCloseEditButton.setOnClickListener(this);
        mHeadView = (HeadView) findViewById(R.id.replay_title_head);
        mHeadView.setTipOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClickEditButton(v);
            }
        });

       /* ReplayFragment replayFragment = new ReplayFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Common.ARGUMENT_USER_ID, String.valueOf(LiveApplication.getInstance().getMyId()));
        bundle.putString(FollowFragment.ARGUMENT_FROM, FollowFragment.FROM_MY);
        replayFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, replayFragment).commit();*/
        mLoadingLayout = (LoadingLayout) findViewById(R.id.replay_loading_layout);
        mLoadingLayout.setDefaultLoading();
        mRefreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.replay_recycle_view);
        mRefreshRecyclerView.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                tryGetReplayListData(REQUEST_TYPE_REFRESH);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        /*mRefreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tryGetReplayListData(REQUEST_TYPE_REFRESH);
            }
        });*/

        mRefreshRecyclerView.setPagingableListener(new com.lhh.ptrrv.library.PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                tryGetReplayListData(REQUEST_TYPE_LOAD_MORE);
            }
        });
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshRecyclerView.setResistance(1.7f);
        mRefreshRecyclerView.setRatioOfHeaderHeightToRefresh(1.2f);
        mRefreshRecyclerView.setDurationToClose(200);
        mRefreshRecyclerView.setDurationToCloseHeader(1000);
        mRefreshRecyclerView.setPullToRefresh(false);
        mRefreshRecyclerView.setKeepHeaderWhenRefresh(true);
        mRefreshRecyclerView.disableWhenHorizontalMove(true);
        mRefreshRecyclerView.setInterceptEventWhileWorking(true);
        mRefreshRecyclerView.setLoadingMinTime(0);
        mRefreshRecyclerView.setEnabledNextPtrAtOnce(true);

        mRefreshRecyclerView.setLoadMoreCount(ReplayRequest.DEFAULT_PAGE_SIZE);
        mRefreshRecyclerView.setSwipeEnable(true);
        mRefreshRecyclerView.setLoadmoreString("loading");
//        mRefreshRecyclerView.setLoadingMinTime(800);
        mRefreshRecyclerView.onFinishLoading(true, false);
        mRefreshRecyclerView.setLoadMoreFooter(new LoadMoreView(this, mRefreshRecyclerView.getRecyclerView()));
        mRefreshRecyclerView.setFooter(new LoadMoreFoot(this));
        mReplayAdapter = new ReplayAdapter(this, mVideos);
        mRefreshRecyclerView.setAdapter(mReplayAdapter);

        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
            @Override
            public void onClick() {
                tryGetReplayListData(REQUEST_TYPE_INIT);
            }
        });
    }

    @Override
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (event.tag.equals(SubcriberTag.REFRESH_DELETE_COUNT)) {
            setDeleteCount();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.replay_all_check_button:
                handleOnClickAllCheckButton();
                break;
            case R.id.replay_delete_button:
                handleOnClickDeleteButton();
                break;
            case R.id.replay_close_edit_button:
                handleOnCloseEditButton();
                break;
        }
    }

    private void setDeleteCount() {
        int deleteCount = DeleteReplayHelper.getInstance().getDeleteVideoList().size();
        if (deleteCount < mVideos.size()) {
            mIsAllCheck = false;
            mAllCheckButton.setText(ResourceHelper.getString(R.string.replay_all_check));
        } else {
            mIsAllCheck = true;
            mAllCheckButton.setText(ResourceHelper.getString(R.string.replay_cancel_all_check));
        }
        if (deleteCount == 0) {
            mDeleteCount.setVisibility(View.INVISIBLE);
            return;
        }
        mDeleteCount.setVisibility(View.VISIBLE);
        String count = ResourceHelper.getString(R.string.replay_have_checked);
        if (count == null) {
            mDeleteCount.setVisibility(View.INVISIBLE);
        } else {
            mDeleteCount.setVisibility(View.VISIBLE);
        }
        count = count.replace("#count#", String.valueOf(deleteCount));
        mDeleteCount.setText(count);
    }

    private void handleOnClickAllCheckButton() {
        DeleteReplayHelper.getInstance().clearDeleteList();
        if (mIsAllCheck) {
            mIsAllCheck = false;
        } else {
            mIsAllCheck = true;
        }
        for (VideoParams videoParams : mVideos) {
            videoParams.isChecked = mIsAllCheck;
            if (videoParams.isChecked) {
                DeleteReplayHelper.getInstance().addVideoToDeleteList(videoParams);
            }
        }
        if (mReplayAdapter != null) {
            mReplayAdapter.notifyDataSetChanged();
        }
        setDeleteCount();
    }

    private void handleOnClickDeleteButton() {
        DeleteReplayHelper.getInstance().showDialogWhenBeforeDeleteRequest(this,
                new DeleteReplayHelper.DeleteRequestCallback() {
                    @Override
                    public void deleteSuccess() {
                        handleOnDeleteSuccess();
                    }

                    @Override
                    public void deleteFailed() {

                    }
                });

    }

    private void handleOnDeleteSuccess() {
        for (VideoParams videoParams : DeleteReplayHelper.getInstance().getDeleteVideoList()) {
            mVideos.remove(videoParams);
        }
        if (mReplayAdapter != null) {
            mReplayAdapter.notifyDataSetChanged();
        }
        tryRefreshDataWhenDeleteOver();
        DeleteReplayHelper.getInstance().clearDeleteList();
        setDeleteCount();
    }

    private void tryRefreshDataWhenDeleteOver() {
        if (mVideos.size() > 0) {
            return;
        }
        mLoadingLayout.setVisibility(View.VISIBLE);
        mIsEditState = false;
        notifyRefreshUI();
        tryGetReplayListData(REQUEST_TYPE_INIT);
    }

    private void notifyRefreshUI() {
        if (mIsEditState) {
            mToolsLayout.setVisibility(View.VISIBLE);
            mHeadView.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.VISIBLE);
            return;
        }
        mToolsLayout.setVisibility(View.GONE);
        mHeadView.setVisibility(View.VISIBLE);
        mDeleteButton.setVisibility(View.GONE);
    }

    private void handleOnClickEditButton(View v) {
        mIsEditState = true;
        mToolsLayout.setVisibility(View.VISIBLE);
        mDeleteButton.setVisibility(View.VISIBLE);
        mHeadView.setVisibility(View.INVISIBLE);

        for (VideoParams videoParams : mVideos) {
            videoParams.isEdit = mIsEditState;
        }
        if (mReplayAdapter != null) {
            mReplayAdapter.notifyDataSetChanged();
        }
        setDeleteCount();
    }

    private void handleOnCloseEditButton() {
        DeleteReplayHelper.getInstance().clearDeleteList();
        mIsEditState = false;
        notifyRefreshUI();

        for (VideoParams videoParams : mVideos) {
            videoParams.isEdit = mIsEditState;
            videoParams.isChecked = false;
        }
        if (mReplayAdapter != null) {
            mReplayAdapter.notifyDataSetChanged();
        }
    }

    private void tryGetReplayListData(final int requestDataType) {
        boolean isLoadMore = false;
        if (requestDataType == REQUEST_TYPE_INIT || requestDataType == REQUEST_TYPE_REFRESH) {
            isLoadMore = false;
        } else if (requestDataType == REQUEST_TYPE_LOAD_MORE) {
            isLoadMore = true;
        }

        mRequest.startRequestData(isLoadMore, new ReplayRequest.OnRequestResultCallback() {
            @Override
            public void onRequestSuccess(List<Video> videoList) {
                handleOnRequestResultSuccess(videoList, requestDataType);
            }

            @Override
            public void onRequestFailed(int errorCode, String errorMsg) {
                handleOnFailed(errorCode, errorMsg, requestDataType);
            }
        });
    }

    private void handleOnFailed(int errorCode, String errorMsg, int requestType) {
        mHeadView.setTipShow(false);
        if (requestType == REQUEST_TYPE_INIT) {
            if (errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR) {
                mLoadingLayout.setDefaultNetworkError(true);
            } else {
                mLoadingLayout.setDefaultDataError(true);
            }
            return;
        }
        ToastHelper.showToast(ResourceHelper.getString(R.string.error_code_prefix) + errorCode);
    }

    private void handleOnRequestResultSuccess(List<Video> videosList, int requestType) {
        if (requestType == REQUEST_TYPE_INIT) {
            if (videosList.size() > 0) {
                mLoadingLayout.setVisibility(View.GONE);
                mHeadView.setTipShow(true);
            } else {
                mLoadingLayout.setBgContent(R.drawable.public_pic_empty
                        , ResourceHelper.getString(R.string.empty_tips), false);
                mHeadView.setTipShow(false);
            }
            mVideos.clear();
//            mRefreshRecyclerView.refreshComplete();
            mRefreshRecyclerView.setOnRefreshComplete();
        } else if (requestType == REQUEST_TYPE_REFRESH) {
            mVideos.clear();
//            mRefreshRecyclerView.refreshComplete();
            mRefreshRecyclerView.setOnRefreshComplete();
        } else if (requestType == REQUEST_TYPE_LOAD_MORE) {
        }
        DeleteReplayHelper.getInstance().clearDeleteList();
        setDeleteCount();
        for (Video video : videosList) {
            VideoParams params = new VideoParams();
            params.isEdit = mIsEditState;
            params.isChecked = false;
            params.video = video;
            mVideos.add(params);
        }

        if (mReplayAdapter == null) {
            return;
        }
        mReplayAdapter.notifyDataSetChanged();
        mRefreshRecyclerView.onFinishLoading(mRequest.isHasMoreData(), false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeleteReplayHelper.getInstance().clearDeleteList();
    }
}
