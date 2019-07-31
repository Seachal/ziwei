package com.laka.live.video.ui.widget.videofunction;

import android.view.View;

import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.dialog.NewLoadingDialog;
import com.laka.live.help.EventBusManager;
import com.laka.live.ui.widget.chatKeyboard.KJChatKeyboardComment;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.event.MiniVideoEvent;
import com.laka.live.video.ui.activity.MiniVideoPlayActivity;

/**
 * @Author:Rayman
 * @Date:2018/8/21
 * @Description:FunctionView面板和Activity关联的工具类，主要实现一些UI操作和统一数据更新
 */

public class VideoFunctionHelper {

    /**
     * description:当初也考虑过使用EventBus直接传递
     * 但是在VideoCommentView里面需要持有一个KJChatKeyboardComment，所以这里做简单的中转
     **/
    private View rootView;
    private MiniVideoPlayActivity activity;
    private KJChatKeyboardComment mChatKeyboard;
    private NewLoadingDialog loadingNewDialog;

    public VideoFunctionHelper(MiniVideoPlayActivity activity) {
        //Logger.e("VideoFunctionHelper初始化啦：" + activity);
        this.activity = activity;
        rootView = activity.getWindow().getDecorView();
    }

    public MiniVideoPlayActivity getActivity() {
        return activity;
    }

    public View getRootView() {
        return rootView;
    }

    public KJChatKeyboardComment getChatKeyboard() {
        return mChatKeyboard;
    }

    public void setChatKeyboard(KJChatKeyboardComment mChatKeyboard) {
        this.mChatKeyboard = mChatKeyboard;
    }

    public void showChatKeyboard(String nickName, boolean isCommentToContent) {
        mChatKeyboard.showKeyboard(nickName, isCommentToContent);
        AnalyticsReport.onEvent(activity, AnalyticsReport.EVENT_16016);
    }

    public void showDialog(String msg) {
        if (loadingNewDialog == null) {
            loadingNewDialog = new NewLoadingDialog(activity);
            loadingNewDialog.setCanceledOnTouchOutside(false);
        }

        // 文本要更新
        loadingNewDialog.setLoadingTip(msg);

        if (!activity.isDestroyed() && !activity.isFinishing()) {
            // 如果已经在显示
            if (!loadingNewDialog.isShowing()) {
                loadingNewDialog.show();
            }
        }
    }

    public void dismissDialog() {
        if (!activity.isDestroyed() && !activity.isFinishing()) {
            if (loadingNewDialog != null && loadingNewDialog.isShowing()) {
                loadingNewDialog.dismiss();
            }
        }
    }

    public boolean isActivityRunning() {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 当外层PagerVideoView变化的时候，通知FunctionView更新对应的评论和推荐商品数据
     */
    public void notifyFunctionUpdate(MiniVideoBean miniVideoBean) {
        MiniVideoEvent miniVideoEvent = new MiniVideoEvent();
        miniVideoEvent.setEvent(miniVideoBean);
        miniVideoEvent.setTargetObj(this);
        miniVideoEvent.setTag(VideoEventConstant.UPDATE_COMMENT_LIST);
        EventBusManager.postEvent(miniVideoEvent);
        miniVideoEvent.setTag(VideoEventConstant.UPDATE_RECOMMEND_GOODS_LIST);
        EventBusManager.postEvent(miniVideoEvent);
//        EventBusManager.postEvent(miniVideoBean, VideoEventConstant.UPDATE_COMMENT_LIST);
//        EventBusManager.postEvent(miniVideoBean, VideoEventConstant.UPDATE_RECOMMEND_GOODS_LIST);
    }

    public void onRelease() {
        activity = null;
        rootView = null;
        mChatKeyboard = null;
        loadingNewDialog = null;
    }
}
