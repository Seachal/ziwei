package com.laka.live.ui.course.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.msg.CommentMsg;
import com.laka.live.msg.CourseCommentMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.comment.CommentListActivity;
import com.laka.live.ui.widget.chatKeyboard.OnCommentOperationListener;
import com.laka.live.ui.widget.comment.CommentItemView;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/9/19.
 */
public class CommentAreaView extends BaseDetailView implements View.OnClickListener, OnCommentOperationListener {

    @InjectView(id = R.id.comment)
    private TextView comment;
    @InjectView(id = R.id.see_all_tv, click = "")
    private TextView see_all_tv;

    private int commentId = 0; // 回复的id


    public CommentAreaView(Context context) {
        this(context, null);
    }

    public CommentAreaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentAreaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(this, R.layout.view_comment_area);
    }

    @Override
    protected void initData(CourseDetailHelper mHelper) {
        super.initData(mHelper);
        initView();
    }

    private void initView() {

        mHelper.getChatKeyboard().setActivity(mHelper.getActivity());
        mHelper.getChatKeyboard().setOnOperationListener(this);
        final View emptyView = mRootView.findViewById(R.id.emptyView);
        final LinearLayout commentContainer = (LinearLayout) mRootView.findViewById(R.id.commentContainer);

        DataProvider.queryCourseComment(this, mHelper.getCourseId(), 0, new GsonHttpConnection.OnResultListener<CourseCommentMsg>() {
            @Override
            public void onSuccess(CourseCommentMsg courseCommentMsg) {

                if (courseCommentMsg == null || courseCommentMsg.getData() == null || courseCommentMsg.getData().getTotalCount() == 0) {
                    emptyView.setVisibility(VISIBLE);
                    commentContainer.setVisibility(GONE);
                } else {
                    emptyView.setVisibility(GONE);
                    commentContainer.setVisibility(VISIBLE);
                    commentContainer.removeAllViews(); // 每次重新请求，都要清空之前的Views
                    for (int count = 0; count < 10 && count < courseCommentMsg.getData().getTotalCount(); ++count) {
                        CommentItemView commentItemView = new CommentItemView(mContext, CommentItemView.TYPE_COMMENT_REPLY);
                        commentItemView.update(courseCommentMsg.getData().getComments().get(count), mHelper.getCourse());
                        final int id = courseCommentMsg.getData().getComments().get(count).getId();
                        final String nickName =courseCommentMsg.getData().getComments().get(count).getNickname();
                        commentItemView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doRely(nickName,id);
                            }
                        });
                        commentContainer.addView(commentItemView);
                    }
                    setCommentCount(courseCommentMsg.getData().getTotalCount());
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                emptyView.setVisibility(VISIBLE);
                commentContainer.setVisibility(GONE);
            }
        });
    }

    private boolean isNeedRepeated = false;

    // 回复
    private void doRely(String nickName,int commentId) {
        isNeedRepeated = true;
        this.commentId = commentId;
        setFitsSystemWindows(true);
        mHelper.showKJKeyboard(nickName,true);

    }

    @Override
    public void send(final String content) {

        mHelper.getActivity().showNewDialog("正在发表评论...");
        DataProvider.commentCourse(this, mHelper.getCourseId(), commentId, content,
                new GsonHttpConnection.OnResultListener<CommentMsg>() {
                    @Override
                    public void onSuccess(CommentMsg commentMsg) {
                        commentId = 0;
                        if (mHelper.getChatKeyboard().isCommentable() && isNeedRepeated) {
                            isNeedRepeated = false;
                            send(content);
                            return;
                        }
                        mHelper.getActivity().dismissDialog();
                        initView();
                        ToastHelper.showToast("评论发表成功");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        commentId = 0;
                        mHelper.getActivity().dismissDialog();
                        ToastHelper.showToast(errorMsg);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.comment) {
            setFitsSystemWindows(true);
            mHelper.showKJKeyboard(null,false);
        }
    }


    @Override
    public void onHide() {
        commentId = 0;
        setFitsSystemWindows(false);
    }

    public void setFitsSystemWindows(boolean fitSystemWindows) {

        RelativeLayout relativeLayout = (RelativeLayout) mHelper.getRootView().findViewById(R.id.rootView);

        if (fitSystemWindows) {
            relativeLayout.setFitsSystemWindows(true);
            relativeLayout.requestFitSystemWindows();
            mHelper.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            relativeLayout.setFitsSystemWindows(false);
            relativeLayout.requestFitSystemWindows();
            mHelper.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    public void setCommentCount(int count) {

        if (count > 10) {
            see_all_tv.setVisibility(VISIBLE);
            see_all_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentListActivity.startActivity(mContext, mHelper.getCourse());
                }
            });
        } else {
            see_all_tv.setVisibility(GONE);
        }

    }
}