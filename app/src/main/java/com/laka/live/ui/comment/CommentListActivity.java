package com.laka.live.ui.comment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;

import com.laka.live.R;
import com.laka.live.bean.CommentInfo;
import com.laka.live.bean.Course;
import com.laka.live.msg.CourseCommentMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.bean.VideoCommentInfo;
import com.laka.live.video.model.http.bean.VideoCommentListResponseBean;

import java.util.List;

/**
 * @ClassName: CommentListActivity
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 20/09/2017
 */

public class CommentListActivity extends CommentBaseActivity<CommentInfo> {
    private final static String TAG = CommentListActivity.class.getSimpleName();

    private CommentListAdapter mAdapter;

    public static void startActivity(Context context, Course course) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra(Common.COURSE, course);
        ActivityCompat.startActivity(context, intent, null);
    }

    public static void startActivity(Context context, MiniVideoBean miniVideoBean) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra(Common.MINI_VIDEO, miniVideoBean);
        ActivityCompat.startActivity(context, intent, null);
    }

    @Override
    public void initIntent() {
        super.initIntent();

        if (getIntent() == null) {
            finish();
            return;
        }
        mCourse = (Course) getIntent().getSerializableExtra(Common.COURSE);
        mVideo = (MiniVideoBean) getIntent().getSerializableExtra(Common.MINI_VIDEO);

        if (mCourse == null && mVideo == null) {
            finish();
        }

        mOriginalId = 0;
    }

    @Override
    public void initView() {
        super.initView();
        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setTitle(ResourceHelper.getString(R.string.all_comments));
        headView.setBackTextShow(false);
    }

    @Override
    public void initPageList() {
        super.initPageList();
        mAdapter = new CommentListAdapter(this);
        mAdapter.setData(mCommentList);
        mAdapter.setCourse(mCourse);
        mAdapter.setVideo(mVideo);
        mPageLayout.setAdapter(mAdapter);
    }

    @Override
    protected void reloadData() {
        super.reloadData();

        mCommentList.clear();
        mAdapter.notifyDataSetChanged();
        loadData(0);
    }

    @Override
    protected void loadData(final int page) {
        super.loadData(page);

        if (mCourse == null && mVideo == null) {
            return;
        }

        //暂时留坑，现在分两个接口获取。然后逻辑都很像的
        if (mCourse != null) {
            DataProvider.queryCourseComment(this, mCourse.getId(), page,
                    new GsonHttpConnection.OnResultListener<CourseCommentMsg>() {
                        @Override
                        public void onSuccess(CourseCommentMsg courseCommentMsg) {
                            mPageLayout.refreshComplete();
                            mPageLayout.setOnLoadMoreComplete();

                            if (courseCommentMsg == null || courseCommentMsg.getData() == null) {
                                mPageLayout.onFinishLoading(false, false);
                                if (Utils.listIsNullOrEmpty(mCommentList)) {
                                    mPageLayout.showEmpty();
                                } else {
                                    mPageLayout.showData();
                                }
                                return;
                            }

                            Log.d(TAG, "getReplies success : " + courseCommentMsg.getData().toString());

                            List<CommentInfo> datas = courseCommentMsg.getData().getComments();

                            if (!Utils.listIsNullOrEmpty(datas)) {
                                mCommentList.addAll(datas);
                            }

                            mAdapter.notifyDataSetChanged();

                            if (mCommentList.size() >= courseCommentMsg.getData().getTotalCount()) {
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
            DataProvider.getVideoComments(this, mVideo.getVideoId() + "", page,
                    new GsonHttpConnection.OnResultListener<VideoCommentListResponseBean>() {
                        @Override
                        public void onSuccess(VideoCommentListResponseBean responseBean) {
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

                            List<VideoCommentInfo> datas = responseBean.getData().getComments();

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
