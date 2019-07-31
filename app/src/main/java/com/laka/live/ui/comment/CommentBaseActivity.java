package com.laka.live.ui.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.CommentMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.chatKeyboard.KJChatKeyboardComment;
import com.laka.live.ui.widget.chatKeyboard.OnCommentOperationListener;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.model.bean.MiniVideoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommentBaseActivity
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 20/09/2017
 */

public class CommentBaseActivity<T> extends BaseActivity implements
        OnCommentOperationListener, View.OnClickListener, PageListLayout.OnRequestCallBack {
    private static final String TAG = CommentBaseActivity.class.getSimpleName();

    protected PageListLayout mPageLayout;
    protected List<T> mCommentList;
    protected Course mCourse;
    protected MiniVideoBean mVideo;
    protected int mOriginalId = 0;

    private KJChatKeyboardComment mChatKeyboard;

    private int mCurrentReplyId = 0;  //当前回复的id

    /**
     * description:回调设置
     **/
    private GsonHttpConnection.OnResultListener<CommentMsg> requestCallBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isNeedRegistEventBus = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        getWindow().setBackgroundDrawable(null);

        initIntent();
        initView();
        initPageList();

        mCurrentReplyId = mOriginalId;
    }

    protected void initIntent() {

    }

    protected void initView() {
        findViewById(R.id.face_iv).setOnClickListener(this);
        findViewById(R.id.comment_tv).setOnClickListener(this);

        mChatKeyboard = (KJChatKeyboardComment) findViewById(R.id.kj_chat_keyboard);
        mChatKeyboard.setActivity(this);
        mChatKeyboard.setOnOperationListener(this);
    }

    protected void reloadData() {
    }

    protected void loadData(int page) {
    }

    protected void initPageList() {
        mPageLayout = (PageListLayout) findViewById(R.id.page_list_layout);
        mCommentList = new ArrayList<>();

        mPageLayout.setLayoutManager(new LinearLayoutManager(this));
        mPageLayout.setIsLoadMoreEnable(true);
        mPageLayout.setIsReloadWhenEmpty(true);
        mPageLayout.setOnRequestCallBack(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBusManager.register(this);

        if (Utils.listIsNullOrEmpty(mCommentList)) {
            mPageLayout.loadData(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBusManager.unregister(this);
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        if (page == 0) {
            reloadData();
        } else {
            loadData(page);
        }
        return null;
    }

    @Override
    public void send(String content) {
        Log.d(TAG, " send实现");
        if (mCourse != null) {
            commentArchive(mCourse.getId(), mCurrentReplyId, content, true);
        }

        if (mVideo != null) {
            commentArchive(mVideo.getVideoId() + "", mCurrentReplyId, content, true);
        }

        if (mChatKeyboard.isCommentable()) {
            if (mCourse != null) {
                commentArchive(mCourse.getId(), 0, content, false);
            }
            if (mVideo != null) {
                commentArchive(mVideo.getVideoId() + "", 0, content, false);
            }
        } else {
            mChatKeyboard.release();
        }

//        Log.d(TAG, "send ." + " ; archiveId : " + mCourse.getId()
//                + " ; commentId : " + mCurrentReplyId + " ; content : " + content);
    }

    @Override
    public void onHide() {

    }

    /**
     * 评论视频或回复
     *
     * @param archiveId 被评论的视频id
     * @param commentId 被回复的评论id，非回复，则默认为：0
     * @param content   评论内容
     */
    private void commentArchive(String archiveId, final int commentId, String content, final boolean isReload) {
        showLoadingDialog();
        requestCallBack = new GsonHttpConnection.OnResultListener<CommentMsg>() {
            @Override
            public void onSuccess(CommentMsg commentMsg) {
                Log.d(TAG, "commentArchive success");
                dismissLoadingsDialog();
                if (isReload) {
                    reloadData();
                } else {
                    mChatKeyboard.release();
                }
                mCurrentReplyId = mOriginalId;
                //有两种评论-一种是课程的评论，一种是小视频的评论，小视频的评论需要发送事件更新
                if (mVideo != null) {
                    EventBusManager.postEvent(VideoEventConstant.UPDATE_VIDEO_REPLY_LIST);
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, " getMayLikeVideos failed . errorCode : " + errorCode + " ; errorMsg ; " + errorMsg
                        + " ; command : " + command);
                showToast(errorMsg);
                dismissLoadingsDialog();
            }
        };

        if (mCourse != null) {
            DataProvider.commentCourse(this, archiveId, commentId, content,
                    requestCallBack);
        }

        if (mVideo != null) {
            DataProvider.commentVideo(this, archiveId, commentId, content,
                    requestCallBack);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face_iv:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_16016);
                mChatKeyboard.showEmoji(false);
                break;
            case R.id.comment_tv:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_16016);
                mChatKeyboard.showKeyboard(null, false);
                break;
            default:
                break;
        }
    }

    /**
     * 处理eventBus的回复用户事件
     *
     * @param event 事件的数据
     */
    private void handleReplayUser(Object event) {
        if (event == null) {
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) event;
        mCurrentReplyId = (int) map.get(Common.REPLY_ID);
        mChatKeyboard.showKeyboard((String) map.get(Common.NICK_NAME), true);
    }

    @Override
    public void onEvent(PostEvent event) {
        super.onEvent(event);

        Log.d(TAG, "onEvent : " + event.toString());
        switch (event.tag) {
            case SubcriberTag.REPLY_COMMENT:
                handleReplayUser(event.event);
                break;
            default:
                break;
        }
    }
}
