package com.laka.live.video.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.bean.User;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.widget.chatKeyboard.KJChatKeyboardComment;
import com.laka.live.util.Common;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoConstant;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.contract.MiniVideoInfoContract;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.bean.VideoCommentBean;
import com.laka.live.video.model.event.MiniVideoEvent;
import com.laka.live.video.model.event.VideoDecorViewEvent;
import com.laka.live.video.model.http.bean.VideoDetailResponseBean;
import com.laka.live.video.presenter.MiniVideoInfoPresenter;
import com.laka.live.video.ui.widget.videofunction.VideoFunctionHelper;
import com.laka.live.video.ui.widget.videopager.IScrollVideoPagerView;
import com.laka.live.video.ui.widget.videopager.ScrollVideoPagerView;
import com.laka.live.video.ui.widget.videopop.VideoFunctionView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:小视频播放页面，页面的数据的操作都是更新本地的Adapter实现的。
 */

public class MiniVideoPlayActivity extends BaseActivity implements MiniVideoInfoContract.IMiniVideoInfoView {


//    上下滑动的视频播放控件
    @BindView(R.id.mini_video_pager)
    ScrollVideoPagerView videoPagerView;

    /**
     * description:其他一些UI配置,评论的软键盘弹出，需要在外层Activity实现
     **/
    @BindView(R.id.kjChatKeyboard)
    KJChatKeyboardComment mChatKeyboard;
    @BindView(R.id.view_function_view)
    VideoFunctionView mFunctionView;
    private Unbinder unbinder;

    /**
     * description:
     **/
    private VideoFunctionHelper mHelper;

    /**
     * description:弹窗配置，弹窗不考虑封装到PlayerView里面
     * 不然涉及业务逻辑，以后难拆分。之后发现封装到PopupWindow的时候，软键盘弹不出来= =封装到View上面吧，
     * 阿西吧
     **/
//    private VideoFunctionPopupWindow mPopupWindow;

    /**
     * description:跟随弹窗的动画配置
     **/
    private ValueAnimator enterAnimator, exitAnimator;
    private View videoFrame;

    /**
     * description:数据对象设置
     **/
    private List<View> mContainerChildViews;
    /**
     * description:由于可能存在多个小视频播放页面，但是从B销毁切换回去A的时候
     * B的MediaPlayer的销毁时间是在A resume之后的。所以A resume的函数就执行错误了
     * 这里需要JCMediaManager发送事件，然后上层处理
     **/
    private boolean isNeedToResume = false;
    private boolean isOtherPlayerRelease = false;
    //可能会出现，A在播放，然后跳转到B上面。然后B releaseMediaPlayer之后，通过回调将A的progress也设置成了0
    //这里多加个标识，在pause的时候设置，然后resume的时候设置
    private int curProgress;

    /**
     * description:数据配置
     **/
    private int videoId = -1;
    private List<MiniVideoBean> mVideoList;
    private String shareUrl;
    private int currentPosition = -1;
    private MiniVideoBean mVideo;
    private User mUser;
    private MiniVideoInfoContract.IMiniVideoInfoPresenter mPresenter;
    private IScrollVideoPagerView.OnPagerChangeListener pagerChangeListener;
    private GsonHttpConnection.OnResultListener videoDetailCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_mini_video_play);
        unbinder = ButterKnife.bind(this);
        initIntent(getIntent());
        initView();
        initData();
        initEvent();
    }

    private void initIntent(Intent intent) {
        currentPosition = intent.getIntExtra(VideoConstant.VIDEO_INFO_EXTRA_POSITION, 0);
        videoId = intent.getIntExtra(VideoConstant.VIDEO_INFO_EXTRA_ID, -1);
        if (intent.getExtras() != null) {
            mVideoList = (List<MiniVideoBean>) intent.getExtras().getSerializable(VideoConstant.VIDEO_INFO_EXTRA_LIST);
            if (Utils.isEmpty(mVideoList)) {
                mVideoList = new ArrayList<>();
            } else {
                mVideo = mVideoList.get(currentPosition);
            }
        }
    }

    private void initView() {
        pagerChangeListener = new IScrollVideoPagerView.OnPagerChangeListener() {
            @Override
            public void onPageChange(int position, boolean isInit) {
                //需要判断当前对象问题
                handleVideoPagerChange(position, isInit);
            }
        };

        videoPagerView
                .setOrientation(IScrollVideoPagerView.ORIENTATION_VERTICAL)
                .setCurrentPosition(currentPosition)
                .setPagerChangeListener(pagerChangeListener);
        if (!Utils.isEmpty(mVideoList)) {
            videoPagerView.setUpPlayVideoList(mVideoList);
        }

        mHelper = new VideoFunctionHelper(this);
        mHelper.setChatKeyboard(mChatKeyboard);
        mFunctionView.setVideoUiHelper(mHelper);
        mFunctionView.setOnFunctionAnimationListener(new VideoFunctionView.OnFunctionAnimationListener() {
            @Override
            public void onAnimStart(boolean isEnter, Animator animation) {
                if (isEnter) {
                    videoPagerView.getPlayerView().enableDefaultRatio(false);
                    enterAnimator.start();
                } else {
                    videoPagerView.getPlayerView().enableDefaultRatio(true);
                    exitAnimator.start();
                }
            }
        });

        initVideoPlayerAnimator();
    }

    private void initVideoPlayerAnimator() {
        float screenHeight = Utils.getScreenHeight(this);
        float targetHeight = screenHeight * (1 - VideoFunctionView.FUNCTION_PANEL_PERCENT);
        enterAnimator = ValueAnimator.ofFloat(screenHeight, targetHeight);
        enterAnimator.setDuration(VideoFunctionView.FUNCTION_PANEL_ANIM_DURATION);
        enterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) videoFrame.getLayoutParams();
                params.height = (int) value;
                videoFrame.setLayoutParams(params);
            }
        });

        exitAnimator = ValueAnimator.ofFloat(targetHeight, screenHeight);
        exitAnimator.setDuration(VideoFunctionView.FUNCTION_PANEL_ANIM_DURATION);
        exitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) videoFrame.getLayoutParams();
                params.height = (int) value;
                videoFrame.setLayoutParams(params);
                if (animation.getAnimatedFraction() == 1) {
                    videoPagerView.enableScroll(true);
                }
            }
        });
    }

    private void initData() {
        mPresenter = new MiniVideoInfoPresenter(this);
        if (videoId != -1) { // H5 跳转进入，只展示当前页
            videoDetailCallBack = new GsonHttpConnection.OnResultListener<VideoDetailResponseBean>() {
                @Override
                public void onSuccess(VideoDetailResponseBean responseBean) {
                    mVideo = responseBean.getData();
                    mVideoList = new ArrayList<>();
                    mVideoList.add(mVideo);
                    videoPagerView.setUpPlayVideoList(mVideoList);
                    mUser = mVideo.getUser();
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    mVideo = new MiniVideoBean();
                    mVideo.setVideoId(videoId);
                    mVideoList = new ArrayList<>();
                    mVideoList.add(mVideo);
                    videoPagerView.setUpPlayVideoList(mVideoList);
                }
            };
            getVideoDetailInfo(videoId);
        }
    }

    private void initEvent() {

    }

    /**
     * 因为可能存在多个播放小视频的页面，但是EventBus仅仅会根据监听者去发送事件，不会判断对象
     * 所以相关的操作都需要带上操作的对象（这里指具体操作的PlayerView对象）
     * 因为可能会存在两个小视频播放页面，例如AB页面，但是相关的操作都是通过EventBus传递的，那么
     * 我可能B页面的事件传递会影响到A页面的事件，所以需要加对象区分，具体的区分对象都是可以在Activity和具体的发送View
     * 上面绑定
     *
     * @param event
     */
    @Override
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (!Utils.isEmpty(mVideoList)) {
            mVideo = mVideoList.get(videoPagerView.getCurrentPage());
            mUser = mVideo.getUser();
        }
        if (VideoEventConstant.UPDATE_CONTAINER_VIEW.equals(event.getTag())) {
            List<View> decorViews = (List<View>) event.getEvent();
            if (decorViews.contains(getWindow().getDecorView())) {
                //更新Container变量
                mContainerChildViews = decorViews;
            }
        } else if (VideoEventConstant.MEDIA_MANAGER_RELEASE.equals(event.getTag())) {
            //TODO 更改为 JZ 播放器后不会执行这里了 JCMediaPlayer释放
            if (isOtherPlayerRelease && isNeedToResume) {
                videoPagerView.onResume(curProgress);
            }
        } else if (VideoEventConstant.UPDATE_VIDEO_PLAY_LIST.equals(event.getTag())) {
            List<MiniVideoBean> videoList = (List<MiniVideoBean>) event.getEvent();
            this.mVideoList = videoList;
            videoPagerView.setData(videoList);
        }

        //判断对象，假若获取到事件的targetObj,不同事件对应不同的targetObject
        MiniVideoEvent miniVideoEvent = null;
        if (event.getEvent() instanceof MiniVideoEvent) {
            miniVideoEvent = (MiniVideoEvent) event.getEvent();
            if (miniVideoEvent.getTargetObj() != null) {
                if (videoPagerView.getPlayerView() == miniVideoEvent.getTargetObj()) {
                    //播放器发送的事件,targetObject就是一个playerView
                    handleVideoEvent(miniVideoEvent);
                } else if (miniVideoEvent.getTargetObj() instanceof VideoDecorViewEvent) {
                    //但是这个有局限性以为TargetObj是一个VideoDecorViewEvent对象
                    //VideoContainer发送的事件，需要根据当前的decorView列表判断是否同一个对象
                    VideoDecorViewEvent viewEvent = (VideoDecorViewEvent) miniVideoEvent.getTargetObj();
                    handleLifeCycleEvent(viewEvent.getDecorViews(), miniVideoEvent.getTag());
                } else if (miniVideoEvent.getTargetObj() == mHelper) {
                    //FunctionView的事件传递需要通过Helper对象唯一性去判断
                    handleFunctionEvent(miniVideoEvent);
                }
            }
        }
    }

    @Override
    public void showVideoInfo(MiniVideoBean infoBean) {

    }

    @Override
    public void updateFollowStatus(boolean isFollow, String msg) {
        //通知PlayerView更新状态
        if (!TextUtils.isEmpty(msg)) {
            ToastHelper.showToast(msg);
            return;
        }
        if (videoPagerView == null) {
            return;
        }
        videoPagerView.getPlayerView().updateFollowState(isFollow);
        updateVideoList();
    }

    @Override
    public void updateLikeStatus(boolean isLike, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastHelper.showToast(msg);
            return;
        }
        if (videoPagerView == null) {
            return;
        }
        videoPagerView.getPlayerView().updateLikeState(isLike);
        updateVideoList();
    }

    @Override
    public void updateRecommendGoods(List<ShoppingGoodsBaseBean> goodsList) {

    }

    @Override
    public void updateCommentList(List<VideoCommentBean> commentList) {
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Logger.e("MiniVideoPlayActivity-------onResume");
        if (videoPagerView != null) {
            videoPagerView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Logger.e("MiniVideoPlayActivity-------onPause");
        if (videoPagerView != null) {
            videoPagerView.onPause();
        }
    }

    @Override
    public void onBackPressed() {
        Logger.e("MiniVideoPlayActivity-----onBackPressed");
        super.onBackPressed();
        // TODO 清除视频占用的资源
        if (videoPagerView != null) {
            pagerChangeListener = null;
            videoPagerView.onDestroy();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatKeyboard = null;
        videoDetailCallBack = null;
        if (mHelper != null) {
            mHelper.onRelease();
        }
        if (mFunctionView != null) {
            mFunctionView.onDestroy();
        }
        if (exitAnimator != null) {
            exitAnimator.removeAllUpdateListeners();
            exitAnimator.cancel();
            exitAnimator = null;
        }
        if (enterAnimator != null) {
            enterAnimator.removeAllUpdateListeners();
            enterAnimator.cancel();
            enterAnimator = null;
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 通知外部VideoListFragment更新当前列表数据
     */
    private void updateVideoList() {
        EventBusManager.postEvent(mVideoList, VideoEventConstant.UPDATE_VIDEO_LIST);
        Logger.e("MiniVideoPlayActivity 更新列表状态：" + mVideoList);
    }

    /**
     * 通知外部VideoListFragment加载更多视频数据
     */
    private void loadMoreVideo() {
        EventBusManager.postEvent(VideoEventConstant.LOAD_VIDEO_LIST);
    }

    /**
     * 页面切换回调
     *
     * @param position
     * @param isInit
     */
    private void handleVideoPagerChange(int position, boolean isInit) {
        //因为列表初始化会也会回调该函数，currentPosition需要在！init状态才同步position
        if (!isInit) {
            currentPosition = position;
            //通知外层列表刷新位置
            EventBusManager.postEvent(position, VideoEventConstant.UPDATE_VIDEO_POSITION);
        }
        mVideo = mVideoList.get(currentPosition);
        //需要根据对象发送事件
        //分别更新FunctionView、用户信息和列表信息
        //Logger.e("MiniVideoPlayActivity--------切换列表：" + position + isInit + "\n" + mContainerChildViews
        //          + "\ncurrent DecorView：" + getWindow().getDecorView());
        mHelper.notifyFunctionUpdate(mVideo);

        MiniVideoEvent event = new MiniVideoEvent();
        event.setEvent(mUser.getId() + "");
        event.setTag(VideoEventConstant.UPDATE_RELATIVE_USER_INFO);
        VideoDecorViewEvent decorViewEvent = new VideoDecorViewEvent(mContainerChildViews);
        event.setTargetObj(decorViewEvent);
        EventBusManager.postEvent(event);

        if (position > mVideoList.size() / 2) {
            //通知获取列表数据
            //Logger.e("输出当前列表大小：" + mVideoList.size() + "\nposition：" + position);
            loadMoreVideo();
        }
    }

    /**
     * H5跳转到当前页面的情况下，只获取当前id的数据，不做列表加载
     *
     * @param videoId
     */
    private void getVideoDetailInfo(final int videoId) {
        DataProvider.getVideoDetailInfo(this, videoId + "", videoDetailCallBack);
    }

    /**
     * 处理播放器相关事件(从MiniVideoPlayerView发送的事件)
     *
     * @param event
     */
    private void handleVideoEvent(MiniVideoEvent event) {
        switch (event.getTag()) {
            case VideoEventConstant.VIDEO_BACK:  // 回退键
                this.finish();
                break;
            case VideoEventConstant.VIDEO_USER_INFO:  // 用户信息
                UserInfoActivity.startActivity(this, mUser.getId() + "");
                break;
            case VideoEventConstant.VIDEO_FOLLOW: // 关注
                if (!AccountInfoManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(MiniVideoPlayActivity.this);
                    return;
                }
                if (Utils.isNotEmpty(mUser.getId()) && mUser.getId() == AccountInfoManager.getInstance().getCurrentAccountUserId()) {
                    ToastHelper.showCenterToast("不能关注自己哦~");
                    return;
                }
                mPresenter.followVideoAuthor(mUser.getId(), (Boolean) event.getEvent());
                break;
            case VideoEventConstant.VIDEO_MORE:  // 分享
                if (!AccountInfoManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(MiniVideoPlayActivity.this);
                    return;
                }
                shareUrl = Common.MINI_VIDEO_SHARE_URL_PREFIX + Common.MINI_VIDEO_ID + "=" + mVideo.getVideoId();
                showShareDialog(shareUrl,
                        mVideo.getVideoTitle(),
                        "发现有滋有味的生活",
                        mVideo.getVideoCover(),
                        false,
                        "",
                        true);
                break;
            case VideoEventConstant.VIDEO_LIKE:  // 喜欢
                if (!AccountInfoManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(MiniVideoPlayActivity.this);
                    return;
                }
                mPresenter.likeVideo(mVideo.getVideoId(), (Boolean) event.getEvent());
                break;
            case VideoEventConstant.VIDEO_OPEN_COMMENT:
                //弹出评论框，这里的缩小动画需要同步播放，不然在回调的时候调用会出现动画延迟
                videoFrame = videoPagerView.getPlayerView().getVideoFrame();
                mFunctionView.showFunctionView(VideoFunctionView.FUNCTION_COMMENT);
                videoPagerView.enableScroll(false);
                break;
            case VideoEventConstant.VIDEO_OPEN_RECOMMEND:
                //弹出推荐商品，这里的缩小动画需要同步播放，不然在回调的时候调用会出现动画延迟
                videoFrame = videoPagerView.getPlayerView().getVideoFrame();
                mFunctionView.showFunctionView(VideoFunctionView.FUNCTION_RECOMMEND);
                videoPagerView.enableScroll(false);
                break;
            case VideoEventConstant.HIDE_VIDEO_FUNCTION:  // 隐藏评论区和商品
                mFunctionView.dismissFunctionView();
                break;
            default:
                break;
        }
    }

    /**
     * 处理FunctionView相关事件(FunctionView的直属子View发送的)
     *
     * @param event
     */
    private void handleFunctionEvent(MiniVideoEvent event) {
        switch (event.getTag()) {
            case VideoEventConstant.VIDEO_COMMENT:
                if (videoPagerView.getPlayerView() != null) {
                    videoPagerView.getPlayerView().updateCommentCount(mVideo.getCommentCount());
                    updateVideoList();
                    Logger.e("输出评论数量：" + mVideo);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理生命周期事件(从VideoContainerActivity发送的事件)
     *
     * @param decorViews 父容器持有的View对象
     * @param tag        事件TAG
     */
    private void handleLifeCycleEvent(List<View> decorViews, String tag) {
        if (!decorViews.contains(getWindow().getDecorView())) {
            if (tag.equals(VideoEventConstant.VIDEO_DESTROY)) {
                isOtherPlayerRelease = true;
            } else {
                isOtherPlayerRelease = false;
            }
            return;
        }
        Logger.e("MiniVideoPlayActivity-------处理生命周期事件：" + decorViews
                + "\ncurrent decorView:" + getWindow().getDecorView()
                + "\ntag:" + tag);
        mContainerChildViews = decorViews;
        switch (tag) {
            case VideoEventConstant.VIDEO_PAUSE:
                isNeedToResume = false;
                if (videoPagerView != null) {
                    curProgress = videoPagerView.getPlayerView().getCurrentProgress();
                    videoPagerView.onPause();
                }
                break;
            case VideoEventConstant.VIDEO_RESUME:
                if (videoPagerView != null) {
                    isNeedToResume = true;
                    if (!isOtherPlayerRelease) {
                        videoPagerView.onResume();
                    }
                }
                break;
            case VideoEventConstant.VIDEO_DESTROY:
                isNeedToResume = false;
                //由于finish调用后，onDestroy不会马上执行，在当前的代码环境中可能存在内存泄漏
                //所以手动调用onDestroy释放资源
                finish();
                onDestroy();
                break;
            default:
                break;
        }
    }
}
