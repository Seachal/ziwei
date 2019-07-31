package com.laka.live.video.ui.widget.videoplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.util.FastClickUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.MiniVideoProgressManager;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.event.MiniVideoEvent;
import com.laka.live.video.ui.widget.follow.FollowView;
import com.laka.live.player.MyExoPlayerPlus;
import com.orhanobut.logger.Logger;

import org.xutils.common.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaManager;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdMgr;
import cn.jzvd.JzvdStd;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频播放控件
 */

public class MiniVideoPlayerView extends ConstraintLayout implements IMiniVideoPlayerView, View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    public static final int VIDEO_CONTROL_BAR_HIDE_TIME = 5000;
    public static final int TIME_INTERVAL = 1000;
    private Context mContext;

    /**
     * description:当前控件UI配置
     **/
    @BindView(R.id.iv_mini_video_back)
    ImageView mIvBack;
    @BindView(R.id.iv_mini_video_avatar)
    SimpleDraweeView mIvUserAvatar;
    @BindView(R.id.tv_mini_video_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_mini_video_create_time)
    TextView mTvCreateTime;
    @BindView(R.id.iv_mini_video_more)
    ImageView mIvMore;
    @BindView(R.id.mini_video_follow_view)
    FollowView mFollowView;

    @BindView(R.id.rl_mini_video_player)
    RelativeLayout mRlPlayerFrame;
    @BindView(R.id.exo_player)
    MyExoPlayerPlus exoPlayer;
    @BindView(R.id.iv_mini_video_cover)
    ImageView mIvCover;
    @BindView(R.id.iv_mini_video_play)
    ImageView mIvPlay;

    @BindView(R.id.rl_mini_video_touch)
    RelativeLayout mRlVideoTouch;
    @BindView(R.id.ll_mini_video_seek_bar)
    LinearLayout mLlControlBar;
    @BindView(R.id.seek_mini_video_progress)
    SeekBar mSeekProgressBar;
    @BindView(R.id.iv_mini_video_play_state)
    ImageView mIvPlayState;
    @BindView(R.id.tv_mini_video_current_time)
    TextView mTvCurrentTime;
    @BindView(R.id.tv_mini_video_total_time)
    TextView mTvTotalTime;

    @BindView(R.id.cl_mini_video_info)
    ConstraintLayout mClInfo;
    @BindView(R.id.tv_mini_video_title)
    TextView mTvTitle;
    @BindView(R.id.ll_mini_video_like)
    LinearLayout mLlLike;
    @BindView(R.id.iv_mini_video_like)
    ImageView mIvLike;
    @BindView(R.id.tv_mini_video_like)
    TextView mTvLike;
    @BindView(R.id.tv_mini_video_comment)
    TextView mTvComment;
    @BindView(R.id.tv_mini_video_recommend)
    TextView mTvRecommend;
    @BindView(R.id.ll_mini_video_comment)
    LinearLayout mLlComment;
    @BindView(R.id.ll_mini_video_recommend)
    LinearLayout mLlRecommend;
    @BindView(R.id.progress_mini_video)
    ProgressBar mProgressBar;

    /**
     * description:加载框
     **/
    @BindView(R.id.ll_mini_video_loading)
    View mLoadingView;

    private Unbinder unbinder;

    /**
     * description:界面部分UI配置
     **/
    private boolean isFollow;
    private boolean isLike;
    private MiniVideoBean mVideo;
    private int defaultVideoHeight;
    private boolean isControlVisible = false;

    /**
     * description:播放相关参数控制
     **/
    private int mScreenWidth, mScreenHeight;
    private int curVideoProgress = 0;
    private boolean isPlaying = false;
    private CountDownTimer countDownTimer;
    private boolean isCountingDown = false;
    private boolean isNetWorkError = false;

    public MiniVideoPlayerView(Context context) {
        this(context, null);
    }

    public MiniVideoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiniVideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initProperties(context, attrs);
        initView();
    }

    @Override
    public void initProperties(Context context, AttributeSet attributeSet) {
    }

    @Override
    public void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.item_mini_video_player, this, true);
        unbinder = ButterKnife.bind(this);

        mScreenWidth = Utils.getScreenWidth(mContext);
        mScreenHeight = Utils.getScreenHeight(mContext);
        defaultVideoHeight = mScreenHeight;

        exoPlayer.isReply = true;
        exoPlayer.widthRatio = mScreenWidth;
        exoPlayer.heightRatio = mScreenHeight;
        exoPlayer.progressBar = mSeekProgressBar;
        exoPlayer.progressBar.setOnSeekBarChangeListener(exoPlayer);
        exoPlayer.currentTimeTextView = mTvCurrentTime;
        exoPlayer.totalTimeTextView = mTvTotalTime;
        exoPlayer.isShowBottomProgress = false;

        countDownTimer = new CountDownTimer(VIDEO_CONTROL_BAR_HIDE_TIME, TIME_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                isCountingDown = true;
            }

            @Override
            public void onFinish() {
                isCountingDown = false;
                isControlVisible = false;
                if (mLlControlBar.getVisibility() == View.VISIBLE) {
                    mLlControlBar.setVisibility(GONE);
                }
            }
        };
    }

    @Override
    public void updateData(final MiniVideoBean videoInfoBean) {
        Logger.e("每次都会updateData吗：" + videoInfoBean);
        mVideo = videoInfoBean;

//        curVideoProgress = mVideo.getVideoProgress();
//        curVideoProgress = 0; // 添加
//        isNetWorkError = false;
        curVideoProgress = MiniVideoProgressManager.getVideoProgressByUrl(mVideo.getVideoUrl());

        if (TextUtils.isEmpty(videoInfoBean.getUser().getNickname())) {
            mTvUserName.setText("");
        } else {
            mTvUserName.setText(videoInfoBean.getUser().getNickname());
        }
        ImageUtil.loadImage(mIvUserAvatar, videoInfoBean.getUser().getAvatar());
        if (TextUtils.isEmpty(videoInfoBean.getCreateTime())) {
            mTvCreateTime.setText("");
        } else {
            mTvCreateTime.setText(TimeUtil.getTime("MM.dd HH:mm",
                    Long.valueOf(videoInfoBean.getCreateTime()) * 1000));
        }
        isFollow = videoInfoBean.isFollow();
        isLike = videoInfoBean.isLike();
        mFollowView.setFollowState(isFollow);
        if (isLike) {
            mIvLike.setImageResource(R.drawable.icon_mini_video_like_check);
        } else {
            mIvLike.setImageResource(R.drawable.selector_mini_video_like);
        }

        mTvTitle.setText(videoInfoBean.getVideoTitle());
        mTvTotalTime.setText(TimeUtil.covertSecondToMS(videoInfoBean.getVideoDuration()));
        mTvLike.setText(String.format(ResourceHelper.getString(R.string.video_like_hint),
                NumberUtils.getVideoSummeryCount(videoInfoBean.getLikeCount())));
        mTvComment.setText(String.format(ResourceHelper.getString(R.string.video_comment_hint),
                NumberUtils.getVideoSummeryCount(videoInfoBean.getCommentCount())));
        mTvRecommend.setText(String.format(ResourceHelper.getString(R.string.video_shop_hint), videoInfoBean.getRecommendGoodsCount()));

        ImageUtil.displayImage(mIvCover, videoInfoBean.getVideoCover());
        ImageUtil.displayImage(exoPlayer.mThumbImageView, videoInfoBean.getVideoCover());
    }

    /**
     * 数据相当于和播放列表绑定，直接更改数据
     *
     * @param isFollow
     */
    public void updateFollowState(boolean isFollow) {
        mFollowView.setFollowState(isFollow);
        mVideo.setFollow(isFollow);
        this.isFollow = isFollow;
        Logger.e("PlayerView更新关注状态：" + this);
    }

    /**
     * 更新当前数据源点赞信息
     *
     * @param isLike
     */
    public void updateLikeState(boolean isLike) {
        int likeCount = Integer.valueOf(mVideo.getLikeCount());
        if (isLike) {
            mIvLike.setImageResource(R.drawable.icon_mini_video_like_check);
            likeCount++;
            mTvLike.setText(String.format(ResourceHelper.getString(R.string.video_like_hint), likeCount + ""));
        } else {
            mIvLike.setImageResource(R.drawable.selector_mini_video_like);
            likeCount--;
            mTvLike.setText(String.format(ResourceHelper.getString(R.string.video_like_hint), likeCount + ""));
        }
        mVideo.setLike(isLike);
        mVideo.setLikeCount(likeCount + "");
        this.isLike = isLike;
        Logger.e("PlayerView更新点赞状态：" + this + "\nisLike：" + isLike);
    }

    /**
     * 更新评论数量
     */
    public void updateCommentCount(String commentCount) {
        mVideo.setCommentCount(commentCount);
        mTvComment.setText(String.format(ResourceHelper.getString(R.string.video_comment_hint), commentCount));
    }

    /**
     * 设置视频播放默认图片
     */
    @Override
    public void setVideoCover(Bitmap bitmap) {
        mIvCover.setImageBitmap(bitmap);
    }

    /**
     * 设置视频播放默认图片
     */
    @Override
    public void setVideoCover(String url) {
        ImageUtil.displayImage(mIvCover, url);
        ImageUtil.displayImage(exoPlayer.mThumbImageView, url);
    }

    /**
     * 开始播放
     */
    @Override
    public void startVideoPlay(String coverUrl, String videoUrl) {
        setVideoCover(coverUrl);
        startVideoPlay(videoUrl);
    }

    @Override
    public void startVideoPlay(String videoUrl) {
        startVideoPlayLogic(videoUrl);
    }

    /**
     * 暂停播放
     */
    @Override
    public void pauseVideoPlay() {

        // 方式二：通过触发  播放/暂停 来实现，
        exoPlayer.currentState = MyExoPlayerPlus.CURRENT_STATE_PLAYING;
        switchPlayState(false);
        saveCurrentProgress();

        // 方式一：
        // JzvdStd.goOnPlayOnPause();
    }

    /**
     * 重新播放
     */
    @Override
    public void resumeVideoPlay() {
        if (exoPlayer != null && exoPlayer.isCurrentJzvd()) { // 未释放
            //    方式一：
            //    JzvdStd.goOnPlayOnResume();
            //    方式二：通过触发  播放/暂停 来实现
            exoPlayer.currentState = MyExoPlayerPlus.CURRENT_STATE_PAUSE;
            switchPlayState(false);
        } else {
            startVideoPlay(mVideo.getVideoCover(), mVideo.getVideoUrl());
        }

    }

    /**
     * 重新播放
     */
    @Override
    public void resumeVideoPlay(int progress) {
        this.curVideoProgress = progress;
        mVideo.setVideoProgress(curVideoProgress);
        Logger.e("resumeVideoPlay------设置progress：" + progress);
        resumeVideoPlay();
    }

    /**
     * item 划出屏幕时释放资源
     */
    @Override
    public void release() {
        videoRelease();
        if (mIvCover != null) {
            mIvCover.animate().alpha(1).start();
        }
        exoPlayer.removePlayListener();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * item 划出屏幕后释放资源
     */
    private void videoRelease() {
        Jzvd jzvd = exoPlayer;
        if (jzvd != null && jzvd.jzDataSource.containsTheUrl(JZMediaManager.getCurrentUrl())) {
            Jzvd currentJzvd = JzvdMgr.getCurrentJzvd();
            if (currentJzvd != null && currentJzvd.currentScreen != Jzvd.SCREEN_WINDOW_FULLSCREEN) {
                Jzvd.releaseAllVideos();
            }
        }
    }

    /**
     * 视频播放页面退出，释放全部资源
     */
    @Override
    public void totallyRelease() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        if (exoPlayer != null) {
            exoPlayer.removePlayListener();
            Jzvd.releaseAllVideos();
            exoPlayer = null;
        }

        if (unbinder != null) {
            unbinder.unbind();
        }
        EventBusManager.unregister(this);
    }

    /**
     * 获取当前进度
     */
    @Override
    public int getCurrentProgress() {
        return curVideoProgress;
    }

    @OnClick({
            R.id.iv_mini_video_back,
            R.id.tv_mini_video_user_name,
            R.id.iv_mini_video_avatar,
            R.id.tv_mini_video_create_time,
            R.id.mini_video_follow_view,
            R.id.iv_mini_video_more,
            R.id.rl_mini_video_touch,
            R.id.iv_mini_video_play_state,
            R.id.ll_mini_video_like,
            R.id.ll_mini_video_comment,
            R.id.ll_mini_video_recommend,
            R.id.exo_player,
            R.id.iv_mini_video_play
    })


    @Override
    public void onClick(View view) {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }
        MiniVideoEvent videoEvent = new MiniVideoEvent();
        videoEvent.setTargetObj(this);
        switch (view.getId()) {
            case R.id.btn_play:  // 直播回放
                switchPlayState();
                break;
            case R.id.exo_player: // 视频播放控件
                break;
            case R.id.iv_mini_video_back: // 回退键
                videoEvent.setTag(VideoEventConstant.VIDEO_BACK);
                EventBusManager.postEvent(videoEvent);
                break;
            case R.id.tv_mini_video_user_name:
            case R.id.iv_mini_video_avatar:
            case R.id.tv_mini_video_create_time: // 用户详情
                videoEvent.setTag(VideoEventConstant.VIDEO_USER_INFO);
                EventBusManager.postEvent(videoEvent);
                break;
            case R.id.mini_video_follow_view:  // 点击关注
                //点击了FollowView，通知外层刷新
                videoEvent.setTag(VideoEventConstant.VIDEO_FOLLOW);
                videoEvent.setEvent(!isFollow);
                EventBusManager.postEvent(videoEvent);
                break;
            case R.id.iv_mini_video_more: // 更多
                videoEvent.setTag(VideoEventConstant.VIDEO_MORE);
                EventBusManager.postEvent(videoEvent);
                break;
            case R.id.rl_mini_video_touch:  // 额外点击功能块
                handlePlayerTouchEvent();
                break;
            case R.id.iv_mini_video_play: //  播放/暂停 状态按钮
            case R.id.iv_mini_video_play_state:
                // 播放第一个视频的时关闭网络，滑动到第二个视频，第二个视频因为网络问题无法播放，
                // 所以 exoPlayer.isCurrentJZVD() 为 false，此时重新打开网络，调用 startVideoPlay() 重新播放视频
                if (exoPlayer != null && exoPlayer.isCurrentJzvd()) { // 未释放
                    switchPlayState();
                } else {
                    startVideoPlay(mVideo.getVideoCover(), mVideo.getVideoUrl());
                }
                break;
            case R.id.ll_mini_video_like:  // 赞
                videoEvent.setTag(VideoEventConstant.VIDEO_LIKE);
                videoEvent.setEvent(!isLike);
                EventBusManager.postEvent(videoEvent);
                break;
            case R.id.ll_mini_video_comment:  // 评论
                videoEvent.setTag(VideoEventConstant.VIDEO_OPEN_COMMENT);
                EventBusManager.postEvent(videoEvent);
                break;
            case R.id.ll_mini_video_recommend:  // 商品
                videoEvent.setTag(VideoEventConstant.VIDEO_OPEN_RECOMMEND);
                EventBusManager.postEvent(videoEvent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Logger.e("MiniVideoPlayerView-----dispatchTouchEvent------ActionDown");
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                Logger.e("MiniVideoPlayerView-----dispatchTouchEvent------ActionMove");
                getParent().requestDisallowInterceptTouchEvent(isControlVisible);
                break;
            case MotionEvent.ACTION_UP:
                Logger.e("MiniVideoPlayerView-----dispatchTouchEvent------ActionUp");
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 播放的主要逻辑，配置ExoPlayer
     *
     * @param videoUrl
     */
    private void startVideoPlayLogic(String videoUrl) {
        mLoadingView.setVisibility(VISIBLE);
        mLlControlBar.setVisibility(GONE);
        mIvPlay.setVisibility(GONE);
        exoPlayer.setVisibility(View.VISIBLE);

        Logger.e("输出progress：" + curVideoProgress);
        if (exoPlayer != null && exoPlayer.isCurrentJzvd()) { // 未释放
            if (!isNetWorkError) { // 网络正常
                curVideoProgress = 0;
            }
            exoPlayer.seekToInAdvance = curVideoProgress;
            // JZMediaManager.seekTo(curVideoProgress);
        } else { // 已释放(首次进入和已释放时走)
            exoPlayer.seekToInAdvance = curVideoProgress;
        }
        LogUtil.e("" + exoPlayer.seekToInAdvance);
        mVideo.setVideoProgress((int) exoPlayer.seekToInAdvance);
        // 列表播放小视频，播放模式为 SCREEN_WINDOW_LIST，之前使用 SCREEN_WINDOW_FULLSCREEN，导致列表快速滑动的时，
        // 下一个视频还没有初始化完成前，一直播放上一个视频的音乐，正确的逻辑应该是上一个视频一旦划出屏幕，则视频播放停止，音乐消失
        JZDataSource jzDataSource = new JZDataSource(mVideo.getVideoUrl(), "");
        jzDataSource.looping = true; //循环播放
        exoPlayer.setUp(jzDataSource, Jzvd.SCREEN_WINDOW_LIST);
        exoPlayer.startPlayLogic();
        exoPlayer.setFullScreenRatio();
        exoPlayer.enableImageThumb(true);
        exoPlayer.setPlayListener(new MyExoPlayerPlus.PlayListener() {

            // 开始播放
            @Override
            public void onPlayerPlay() {
                Logger.e(TAG, "Exo Player----onPlayerPlay");
                isPlaying = true;
                changePlayUI(isPlaying);
                isControlVisible = false;
            }

            @Override
            public void onPlayerPause() {
                Logger.e(TAG, "Exo Player----onPlayerPause");
                isPlaying = false;
                changePlayUI(isPlaying);
                isControlVisible = true;
            }

            @Override
            public void onPlayerFreeze(boolean isFreeze) {

            }

            @Override
            public void onPlayerError(int time) {
                Logger.e(TAG, "Exo Player----onPlayerError");
                ToastHelper.showToast("播放小视频失败~");
                changePlayUIError();
                // 网络播放错误可能出现网络问题，弹窗提示。不对MyExoPlayer做修改，外层处理没网络逻辑
                if (!NetworkUtil.isNetworkOk(getContext())) {
                    isNetWorkError = true;
                    exoPlayer.showNoNetWork();
                }
            }

            @Override
            public void onPlayerProgress(int progress, int secProgress, int currentTime, int totalTime) {
                mProgressBar.setProgress(progress);
                curVideoProgress = currentTime;
                mVideo.setVideoProgress(curVideoProgress);
            }
        });

        exoPlayer.setPlayDialogClickListener(new MyExoPlayerPlus.PlayDialogClickListener() {
            @Override
            public void onNoNetDialogClick() {
                //尝试继续播放
                exoPlayer.currentState = MyExoPlayerPlus.CURRENT_STATE_PAUSE;
                changePlayUIError();
            }

            @Override
            public void onWifiDialogClick(boolean isContinue) {
                if (!isContinue) {
                    EventBusManager.postEvent(VideoEventConstant.VIDEO_DESTROY);
                }
            }
        });
        Logger.e(TAG, "播放链接：" + videoUrl);
    }

    /**
     * 保存当前视频的当前播放进度
     */
    public void saveCurrentProgress() {
        MiniVideoProgressManager.saveVideoProgress(curVideoProgress, mVideo.getVideoUrl());
    }

    /**
     * 播放状态的切换
     */
    private void switchPlayState() {
        switchPlayState(true);
    }

    /**
     * 播放状态切换，实际上播放状态的记录都在ExoPlayer内部实现。暂停和继续播放都是调用此方法
     * 外层主要是改变UI
     *
     * @param isShowUi
     */
    private void switchPlayState(boolean isShowUi) {
        changePlayStateOrUI(isShowUi);

        if (exoPlayer != null) {
            exoPlayer.onPlayClick();
        }
    }

    /**
     * 更改状态和UI
     *
     * @param isShowUi
     */
    private void changePlayStateOrUI(boolean isShowUi) {
        if (exoPlayer.currentState == JzvdStd.CURRENT_STATE_PLAYING) {
            isPlaying = false;
        } else {
            isPlaying = true;
        }
        if (isShowUi) {
            changePlayUI(isPlaying);
        }
    }

    /**
     * 处理空白点击的事件
     * 例如SeekBar的显示，评论框的控制
     */
    private void handlePlayerTouchEvent() {
        int currentFrameHeight = mRlPlayerFrame.getLayoutParams().height;
        if (currentFrameHeight == -1) {
            //MatchParent默认为全屏
            currentFrameHeight = mScreenHeight;
        }
        //情况一：当功能页面展示的时候需要隐藏
        if (currentFrameHeight != defaultVideoHeight) {
            MiniVideoEvent videoEvent = new MiniVideoEvent();
            videoEvent.setTargetObj(this);
            videoEvent.setTag(VideoEventConstant.HIDE_VIDEO_FUNCTION);
            EventBusManager.postEvent(videoEvent);
        } else {
            //情况二：展示进度条和暂停图标
            if (!isControlVisible && isPlaying == false) {
                mIvPlay.setVisibility(VISIBLE);
            } else {
                mIvPlay.setVisibility(GONE);
            }
            showControl(!isControlVisible);
            isControlVisible = !isControlVisible;
        }
    }

    /**
     * 根据播放状态改变UI(播放和暂停)
     *
     * @param isPlaying
     */
    private void changePlayUI(boolean isPlaying) {
        changePlayUI(isPlaying, true);
    }

    /**
     * 根据播放状态改变UI，是否展示控制栏
     *
     * @param isPlaying
     * @param isShowControl
     */
    private void changePlayUI(boolean isPlaying, boolean isShowControl) {
        if (mIvCover.getAlpha() == 1) {
            hideVideoCover();
        }
        if (isShowControl) {
            //控制器的展示需要取反
            showControl(!isPlaying);
        }
        if (!isPlaying) {
            mIvPlayState.setImageResource(R.drawable.selector_mini_video_play);
            mIvPlay.setVisibility(VISIBLE);
        } else {
            mIvPlayState.setImageResource(R.drawable.selector_mini_video_pause);
            mIvPlay.setVisibility(GONE);
        }
//        Logger.e("根据播放状态设置UI：" + isPlaying + "\nisControlVisible：" + isShowControl);
    }

    /**
     * 展示控制器
     *
     * @param isShow
     */
    private void showControl(boolean isShow) {
        if (isShow) {
            if (mLlControlBar.getVisibility() == GONE) {
                mLlControlBar.setVisibility(VISIBLE);
            }
            startHideTimer();
        } else {
            if (mLlControlBar.getVisibility() == VISIBLE) {
                mLlControlBar.setVisibility(GONE);
            }
            cancelHideTimer();
        }
    }

    /**
     * 隐藏的时候需要加上alpha动态变化，不然会很突兀
     */
    private void hideVideoCover() {
        mIvCover.animate().alpha(0).setDuration(200).start();
    }

    /**
     * 播放错误的UI配置
     */
    private void changePlayUIError() {
        changePlayUI(false);
        mLoadingView.setVisibility(GONE);
        mIvPlay.setVisibility(VISIBLE);
        mIvCover.animate().alpha(1).setDuration(200).start();
    }

    /**
     * 隐藏操作栏
     */
    private void startHideTimer() {
        mLoadingView.setVisibility(View.GONE);
        if (!isCountingDown) {
            countDownTimer.start();
            isCountingDown = true;
        }
    }

    private void cancelHideTimer() {
        if (isCountingDown) {
            countDownTimer.cancel();
            isCountingDown = false;
        }
    }

    /**
     * 返回视频播放的真正区域
     *
     * @return
     */
    public View getVideoFrame() {
        return mRlPlayerFrame;
    }

    /**
     * 主要暴露API供Activity缩放播放器时候调用，因为内部MyExoPlayer继承的JCVideoPlayer默认设置了高度变化
     *
     * @param isEnable
     */
    public void enableDefaultRatio(boolean isEnable) {
        exoPlayer.enableDefaultRatio(isEnable);
    }
}
