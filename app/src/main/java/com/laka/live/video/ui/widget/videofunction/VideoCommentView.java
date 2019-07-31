package com.laka.live.video.ui.widget.videofunction;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.bean.CommentInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.CommentMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.comment.CommentListAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.chatKeyboard.KJChatKeyboardComment;
import com.laka.live.ui.widget.chatKeyboard.OnCommentOperationListener;
import com.laka.live.util.Common;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.bean.VideoCommentInfo;
import com.laka.live.video.model.event.MiniVideoEvent;
import com.laka.live.video.model.http.bean.VideoCommentListResponseBean;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.laka.live.account.login.LoginActivity.TYPE_FROM_LOGIN_OUT;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:小视频播放页面-评论View 可能逻辑比较绕，因为还和一个KJChatKeyboardComment的操作有关联
 * 不过业务逻辑都在这里面完成，但是部分需要和Activity交互的都是通过EventBus传递
 */

public class VideoCommentView extends BaseVideoFunctionView implements OnCommentOperationListener,
        View.OnClickListener, PageListLayout.OnRequestCallBack {

    @BindView(R.id.comment)
    TextView mTvComment;
    @BindView(R.id.page_list_layout)
    PageListLayout mPagerList;
    @BindView(R.id.emptyView)
    View emptyView;

    private KJChatKeyboardComment mChatKeyboard;

    /**
     * description:页面数据配置
     **/
    private int commentId = -1;
    private boolean isNeedRepeated = false;
    private boolean isReload = false;
    private boolean isComment = false;
    private String mContent;
    private List<CommentInfo> mCommentList;
    private CommentListAdapter mAdapter;
    private GsonHttpConnection.OnResultListener postListener;
    private GsonHttpConnection.OnResultListener resultListener;

    public VideoCommentView(Context context) {
        super(context);
    }

    public VideoCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_video_comment;
    }

    @Override
    protected void initProperties() {

    }

    @Override
    protected void initView() {
        mPagerList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mPagerList.setEnableRefresh(false);
        mPagerList.setIsLoadMoreEnable(true);
        mPagerList.setIsReloadWhenEmpty(true);
        mPagerList.showFooter(true);
    }

    @Override
    protected void initData() {
        mCommentList = new ArrayList<>();
        mAdapter = new CommentListAdapter(getContext());
        mAdapter.setData(mCommentList);
        mPagerList.setAdapter(mAdapter);

        postListener = new GsonHttpConnection.OnResultListener<CommentMsg>() {
            @Override
            public void onSuccess(CommentMsg commentMsg) {
                if (mHelper != null && mHelper.isActivityRunning()) {
                    commentId = 0;
                    if (mChatKeyboard.isCommentable() && isNeedRepeated) {
                        isNeedRepeated = false;
                        send(mContent);
                        return;
                    }
                    mHelper.dismissDialog();
                    isComment = true;
                    reloadData(mVideo);
                    ToastHelper.showToast("评论发表成功");
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                if (mHelper != null && mHelper.isActivityRunning()) {
                    commentId = 0;
                    mHelper.dismissDialog();
                    ToastHelper.showToast(errorMsg);
                }
            }
        };

        resultListener = new GsonHttpConnection.OnResultListener<VideoCommentListResponseBean>() {
            @Override
            public void onSuccess(VideoCommentListResponseBean responseBean) {
                mPagerList.refreshComplete();
                mPagerList.setOnLoadMoreComplete();

                if (responseBean == null || responseBean.getData() == null) {
                    mPagerList.onFinishLoading(false, false);
                    if (Utils.listIsNullOrEmpty(mCommentList)) {
                        emptyView.setVisibility(VISIBLE);
                        mPagerList.setVisibility(GONE);
                    } else {
                        emptyView.setVisibility(GONE);
                        mPagerList.setVisibility(VISIBLE);
                        mPagerList.showData();
                    }
                    return;
                }

                //数据处理
                List<VideoCommentInfo> datas = responseBean.getData().getComments();
                if (!Utils.listIsNullOrEmpty(datas)) {
                    mCommentList.addAll(datas);
                }
                mAdapter.notifyDataSetChanged();
                if (mCommentList.size() >= responseBean.getData().getTotalCount()) {
                    mPagerList.onFinishLoading(false, false);
                } else {
                    mPagerList.onFinishLoading(true, false);
                }
                mPagerList.showData();
                mPagerList.addCurrentPage();

                if (mCommentList.size() == 0) {
                    mPagerList.setVisibility(GONE);
                    emptyView.setVisibility(VISIBLE);
                } else {
                    mPagerList.setVisibility(VISIBLE);
                    emptyView.setVisibility(GONE);
                }

                int count = responseBean.getData().getTotalCount();
                if (mVideo != null & isComment) {
                    Logger.e("设置Video评论数呢：" + mVideo);
                    mVideo.setCommentCount(count + "");
                    //通知刷新外部列表和详情的数据
                    MiniVideoEvent event = new MiniVideoEvent();
                    event.setTargetObj(mHelper);
                    event.setTag(VideoEventConstant.VIDEO_COMMENT);
                    EventBusManager.postEvent(event);
                }
            }


            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mPagerList.refreshComplete();
                mPagerList.setOnLoadMoreComplete();
                mPagerList.onFinishLoading(false, false);
                if (Utils.listIsNullOrEmpty(mCommentList)) {
                    mPagerList.showNetWorkError();
                } else {
                    mPagerList.setVisibility(GONE);
                    emptyView.setVisibility(VISIBLE);
                }

                ToastHelper.showToast(R.string.network_error_tips);
            }
        };
    }

    @OnClick({
            R.id.comment,
            R.id.emptyView
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment:
                if (!AccountInfoManager.getInstance().isLogin()) {
                    ToastHelper.showToast("请您先登录");
                    LoginActivity.startActivity(getContext(), TYPE_FROM_LOGIN_OUT);
                    return;
                }
                mHelper.showChatKeyboard(null, false);
                break;
            case R.id.emptyView:
                //重新加载数据
                isReload = true;
                isComment = false;
                reloadData(mVideo);
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEvent(PostEvent event) {
        if (SubcriberTag.REPLY_COMMENT.equals(event.getTag())) {
            Map<String, Object> map = (Map<String, Object>) event.getEvent();
            String nickName = (String) map.get(Common.NICK_NAME);
            int replyId = (int) map.get(Common.REPLY_ID);
            doRely(nickName, replyId);
            Logger.e("添加评论回复欸：" + nickName + replyId);
        } else if (event.getTag().equals(VideoEventConstant.UPDATE_VIDEO_REPLY_LIST)) {
            reloadData(mVideo);
        }

        if (event.getEvent() instanceof MiniVideoEvent) {
            MiniVideoEvent videoEvent = (MiniVideoEvent) event.getEvent();
            switch (videoEvent.getTag()) {
                case VideoEventConstant.UPDATE_RECOMMEND_GOODS_LIST:
                    if (videoEvent.getTargetObj() != null && videoEvent.getTargetObj() instanceof VideoFunctionHelper) {
                        VideoFunctionHelper helper = (VideoFunctionHelper) videoEvent.getTargetObj();
                        if (helper == mHelper) {
                            mVideo = (MiniVideoBean) videoEvent.getEvent();
                            mAdapter.setVideo(mVideo);
                            isReload = false;
                            isComment = false;
                            reloadData(mVideo);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void send(final String content) {
        mHelper.showDialog("正在发表评论...");
        mContent = content;
        DataProvider.commentVideo(this, mVideo.getVideoId() + "", commentId, content,
                postListener);

    }

    @Override
    public void onHide() {
        commentId = 0;
    }

    @Override
    public void setVideoUIHelper(VideoFunctionHelper mHelper) {
        super.setVideoUIHelper(mHelper);
        mChatKeyboard = mHelper.getChatKeyboard();
        mChatKeyboard.setOnOperationListener(this);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        if (mChatKeyboard != null) {
            mChatKeyboard.setOnOperationListener(null);
            mChatKeyboard = null;
        }
        postListener = null;
        resultListener = null;
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        if (page == 0) {
            reloadData(mVideo);
        } else {
            loadData(mVideo, page);
        }
        return null;
    }

    /**
     * 回复评论
     *
     * @param nickName
     * @param commentId
     */
    private void doRely(String nickName, int commentId) {
        isNeedRepeated = true;
        this.commentId = commentId;
        mHelper.showChatKeyboard(nickName, true);
    }

    private void reloadData(MiniVideoBean videoBean) {
        mCommentList.clear();
        mAdapter.notifyDataSetChanged();
        loadData(videoBean, 0);
    }

    private void loadData(MiniVideoBean videoBean, int page) {
        DataProvider.getVideoComments(this, videoBean.getVideoId() + "", 0,
                resultListener);
    }
}
