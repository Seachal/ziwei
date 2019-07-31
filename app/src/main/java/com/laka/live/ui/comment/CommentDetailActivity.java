package com.laka.live.ui.comment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;

import com.laka.live.R;
import com.laka.live.bean.BaseComment;
import com.laka.live.bean.Course;
import com.laka.live.bean.ReplyInfo;
import com.laka.live.msg.CourseReplyMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.bean.VideoReplyInfo;
import com.laka.live.video.model.http.bean.VideoCommentReplyListResponseBean;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * @ClassName: CommentInfoFragment
 * @Description: 评论详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 1/14/17
 */

public class CommentDetailActivity extends CommentBaseActivity<BaseComment> {
    private static final String TAG = CommentDetailActivity.class.getSimpleName();

    private CommentDetailAdapter mAdapter;
    private BaseComment mComment;

    public static void startActivity(Context context, Course course, BaseComment comment) {
        Intent intent = new Intent(context, CommentDetailActivity.class);
        intent.putExtra(Common.COURSE, course);
        intent.putExtra(Common.COMMENT, comment);
        ActivityCompat.startActivity(context, intent, null);
    }

    /**
     * 根据需求迭代，有Video的情况都不用点赞功能，直接隐藏
     *
     * @param context
     * @param video
     * @param comment
     */
    public static void startActivity(Context context, MiniVideoBean video, BaseComment comment) {
        Intent intent = new Intent(context, CommentDetailActivity.class);
        intent.putExtra(Common.MINI_VIDEO, video);
        intent.putExtra(Common.COMMENT, comment);
        ActivityCompat.startActivity(context, intent, null);
    }

    @Override
    public void initIntent() {
        super.initIntent();

        if (getIntent() == null) {
            finish();
            return;
        }
        mComment = getIntent().getParcelableExtra(Common.COMMENT);
        mCourse = (Course) getIntent().getSerializableExtra(Common.COURSE);
        mVideo = (MiniVideoBean) getIntent().getSerializableExtra(Common.MINI_VIDEO);
        if (mComment == null || (mCourse == null && mVideo == null)) {
            finish();
            return;
        }
        mOriginalId = mComment.getId();
    }

    @Override
    public void initView() {
        super.initView();
        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setTitle(ResourceHelper.getString(R.string.detail));
        headView.setBackTextShow(false);
    }

    @Override
    public void initPageList() {
        super.initPageList();
        mAdapter = new CommentDetailAdapter(this);
        mAdapter.setData(mCommentList);
        mPageLayout.setAdapter(mAdapter);
    }

    @Override
    protected void reloadData() {
        super.reloadData();

        mCommentList.clear();
        mAdapter.setBaseComment(mComment);
        mAdapter.notifyDataSetChanged();
        loadData(0);
    }

    @Override
    protected void loadData(final int page) {
        super.loadData(page);

        if (mComment == null) {
            return;
        }

        //扩展太蛋疼////留坑之后再拆分
        if (mCourse != null) {
            DataProvider.queryCourseReplies(this, mComment.getId(), page,
                    new GsonHttpConnection.OnResultListener<CourseReplyMsg>() {
                        @Override
                        public void onSuccess(CourseReplyMsg courseReplyMsg) {
                            mPageLayout.refreshComplete();
                            mPageLayout.setOnLoadMoreComplete();

                            if (courseReplyMsg == null || courseReplyMsg.getData() == null) {
                                mPageLayout.onFinishLoading(false, false);
                                if (Utils.listIsNullOrEmpty(mCommentList)) {
                                    mPageLayout.showEmpty();
                                } else {
                                    mPageLayout.showData();
                                }
                                return;
                            }

                            Logger.d(TAG, "getReplies success : " + courseReplyMsg.getData().toString());
                            List<ReplyInfo> datas = courseReplyMsg.getData().getReplies();

                            if (!Utils.listIsNullOrEmpty(datas)) {
                                mCommentList.addAll(datas);
                            }

                            mAdapter.notifyDataSetChanged();

                            if (mCommentList.size() >= courseReplyMsg.getData().getTotalCount()) {
                                mPageLayout.onFinishLoading(false, false);
                            } else {
                                mPageLayout.onFinishLoading(true, false);
                            }

                            mPageLayout.showData();
                            mPageLayout.addCurrentPage();
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg, String command) {
                            Log.d(TAG, "getReplies fail : " + errorMsg);
                            mPageLayout.refreshComplete();
                            mPageLayout.setOnLoadMoreComplete();
                            mPageLayout.onFinishLoading(false, false);
                            if (Utils.listIsNullOrEmpty(mCommentList)) {
                                mPageLayout.showNetWorkError();
                            } else {
                                mPageLayout.showData();
                            }
                            showToast(R.string.network_error_tips);
                        }
                    });
        } else {
            DataProvider.queryVideoReplies(this, mComment.getId(), page,
                    new GsonHttpConnection.OnResultListener<VideoCommentReplyListResponseBean>() {
                        @Override
                        public void onSuccess(VideoCommentReplyListResponseBean responseBean) {
                            mPageLayout.refreshComplete();
                            mPageLayout.setOnLoadMoreComplete();

                            if (responseBean == null || responseBean.getData() == null) {
                                mPageLayout.onFinishLoading(false, false);
                                if (Utils.listIsNullOrEmpty(mCommentList)) {
                                    mPageLayout.showEmpty();
                                } else {
                                    mPageLayout.showData();
                                }
                                return;
                            }

                            Log.d(TAG, "getReplies success : " + responseBean.getData().toString());
                            List<VideoReplyInfo> datas = responseBean.getData().getReplies();

                            if (!Utils.listIsNullOrEmpty(datas)) {
                                mCommentList.addAll(datas);
                            }

                            mAdapter.notifyDataSetChanged();

                            if (mCommentList.size() >= responseBean.getData().getTotalCount()) {
                                mPageLayout.onFinishLoading(false, false);
                            } else {
                                mPageLayout.onFinishLoading(true, false);
                            }

                            mPageLayout.showData();
                            mPageLayout.addCurrentPage();
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg, String command) {
                            Log.d(TAG, "getReplies fail : " + errorMsg);
                            mPageLayout.refreshComplete();
                            mPageLayout.setOnLoadMoreComplete();
                            mPageLayout.onFinishLoading(false, false);
                            if (Utils.listIsNullOrEmpty(mCommentList)) {
                                mPageLayout.showNetWorkError();
                            } else {
                                mPageLayout.showData();
                            }
                            showToast(R.string.network_error_tips);
                        }
                    });
        }
    }
}
