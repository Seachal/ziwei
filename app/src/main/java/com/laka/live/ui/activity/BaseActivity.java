package com.laka.live.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.income.AliPayHelper;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.account.my.MyCoinsActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.application.LiveActivityManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.ChatEntity;
import com.laka.live.bean.OrderOrderPayInfo;
import com.laka.live.bean.OrderPayInfoMsg;
import com.laka.live.bean.UserInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.dialog.NewLoadingDialog;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.NetStateManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.order.widget.OrderDialog;
import com.laka.live.ui.widget.BottomMenuDialog;
import com.laka.live.ui.widget.LoadingDialog;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.ui.widget.swipebacklayout.SwipeBackActivity;
import com.laka.live.update.UpdateCheck;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ShareUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.laka.taste.wxapi.WXHelper;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import framework.ioc.InjectUtil;
import framework.ioc.Ioc;
import laka.live.bean.ChatMsg;

/**
 * Created by luwies on 16/3/4.
 */
public class BaseActivity extends SwipeBackActivity implements EventBusManager.OnEventBusListener {//SwipeBackActivity
    private static final String TAG = "BaseActivity";

    private static final long SESSION_CONTINUE_MILLIS = 30L * 1000L;
    private boolean isResume;

    private boolean mDestroyed = false;

    protected TextView mErrorText;

    protected View mErrorView;

    protected View mLoadingView;

    protected View mDataView;

    protected PageListLayout.ErrorState mErrorState;

    private long mLastBackTime = 0L;

    private static boolean isReOpen = false;

    protected BaseActivity mContext;
    protected boolean isNeedRegistEventBus = true;

    protected LiveActivityManager activityManager;
    // 带遮罩黑背景的进度圈
    protected NewLoadingDialog loadingNewDialog;

//    protected boolean isInBackGround = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Intent intent = getIntent();
            if (intent == null) {
                intent = new Intent();
                setIntent(intent);
            }
            intent.putExtras(savedInstanceState);
        }

        share = ShareUtil.getInstance();
        activityManager = LiveActivityManager.getInstance();
        activityManager.addActivity(this);
        if (isNeedRegistEventBus)
            EventBusManager.register(this);//注册EventBus
//        registerUpdateBroadcast();

        this.mContext = this;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        InjectUtil.inject(this);
    }


    protected void initDefView() {
        if (mDataView == null) {
            mDataView = findViewById(R.id.data);
        }

        if (mErrorView == null) {
            mErrorView = findViewById(R.id.error_layout);
            if (mErrorView != null) {
                if (mErrorText == null) {
                    mErrorText = (TextView) mErrorView.findViewById(R.id.tip);
                }
            }
        }

        if (mLoadingView == null) {
            mLoadingView = findViewById(R.id.loading);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;

        AnalyticsReport.onResume(this);

        // 此处代码移到MainActivity并修改了部分逻辑
//        if (AccountInfoManager.getInstance().checkUserIsLogin()
//                &&!UiPreference.getBoolean(Common.HAS_SET_JPUSH_ID, false)) {
//
//            String regId = JPushInterface.getRegistrationID(this);
//
//            if (!TextUtils.isEmpty(regId)) {
//
//                DataProvider.setJPushId(regId, new GsonHttpConnection.OnResultListener<Msg>() {
//                    @Override
//                    public void onSuccess(Msg msg) {
//                        UiPreference.putBoolean(Common.HAS_SET_JPUSH_ID, true);
//                    }
//
//                    @Override
//                    public void onFail(int errorCode, String errorMsg, String command) {}
//                });
//            }
//        }

        if (isReOpen) {
            isReOpen = false;
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastBackTime >= SESSION_CONTINUE_MILLIS) {
                AnalyticsReport.reportOnLive();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
        AnalyticsReport.onPause(this);
    }

    public boolean isResume() {
        return isResume;
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (isFinishing() == false) {
//            String packageName = getPackageName();
//            String topAppName = Utils.getLauncherTopApp(this);
//            if (TextUtils.equals(packageName, topAppName) == false) {
//                mLastBackTime = System.currentTimeMillis();
//                isReOpen = true;
//                LiveApplication.getInstance().applicationDidEnterBackground();
//                EventBusManager.postEvent(0,SubcriberTag.APP_ENTER_BACKGROUND);
//                isInBackGround = true;
//            }
//            Log.d(TAG," topAppName="+topAppName );
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(share!=null){
//            share.setActivity(null);
//        }
        EventBusManager.unregister(this);//反注册EventBus
        LiveApplication.mQueue.cancelAll(this);
        mDestroyed = true;
        LiveActivityManager.getInstance().removeActivity(this);
        UpdateCheck.getInstance().clearUpdateCheck();

//        if (share != null) {
//            share.destory();
//            share = null;
//        }
    }

    public boolean isDestroyed() {
        return mDestroyed;
    }

    public static boolean isActivityDestroted(Context context) {

        if (context == null) {
            return true;
        }

        if (context instanceof BaseActivity) {
            return ((BaseActivity) context).isDestroyed();
        }

        if (context instanceof Activity) {
            return ((Activity) context).isFinishing();
        }

        return false;
    }

    public void showToast(int resId, int duration) {
        showToast(getString(resId), duration);
    }

    public void showToast(CharSequence text, int duration) {
        ToastHelper.showToast(text, duration);
    }


    public void showToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        // if (BaseFragmentActivity.this.getCurrentFocus() != null) {
        // manager.hideSoftInputFromWindow(BaseFragmentActivity.this.getCurrentFocus()
        // .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // }
        if (manager != null) {
            manager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    BottomMenuDialog shareDialog;
    public ShareUtil share;

    /**
     * 分享弹出框
     */
    public void showShareDialog(final String shareUrl, final String title,
                                final String content, final String imageUrl, final boolean isLive) {
        showShareDialog(shareUrl, title, content, imageUrl, isLive, "", false);
    }

    public void showShareDialog(final String shareUrl, final String title,
                                final String content, final String imageUrl, final boolean isLive, boolean isVideo) {
        showShareDialog(shareUrl, title, content, imageUrl, isLive, "", isVideo);
    }

    public void showShareDialog(final String shareUrl, final String title,
                                final String content, final String imageUrl, final boolean isLive,
                                String proxyRatio, boolean isVideo) {

//        String shareContent = "";
//        if (!Utils.isEmpty(content)) {
//            if (shareContent.length() > 20)
//                ;
//            shareContent = content.substring(0, 20);
//        }
        if (!AccountInfoManager.getInstance().isLogin()) {
            LoginActivity.startActivity(this);
            return;
        }

        View dialogView = LayoutInflater.from(this).inflate(
                R.layout.dialog_share, null);
        TextView sharePro = (TextView) dialogView.findViewById(R.id.sharePro);

        if (Utils.isNotEmpty(proxyRatio)) {
            ((View) sharePro.getParent()).setVisibility(View.VISIBLE);
            sharePro.setText(ResourceHelper.getString(R.string.share_proxy_ratio, proxyRatio));
        } else {
            ((View) sharePro.getParent()).setVisibility(View.GONE);
        }

        Log.d(TAG, " showShareDialog shareUrl=" + shareUrl);

        Button tvCancle = (Button) dialogView.findViewById(R.id.tv_cancle);
        LinearLayout llWechat = (LinearLayout) dialogView
                .findViewById(R.id.ll_wechat);
        LinearLayout llMoment = (LinearLayout) dialogView
                .findViewById(R.id.ll_monent);
        LinearLayout llQQ = (LinearLayout) dialogView.findViewById(R.id.ll_qq);
        LinearLayout llWeibo = (LinearLayout) dialogView
                .findViewById(R.id.ll_weibo);
        LinearLayout llQzone = (LinearLayout) dialogView
                .findViewById(R.id.ll_qzone);
        LinearLayout llVideoShare = dialogView
                .findViewById(R.id.ll_share_video);
        LinearLayout llNoInteresting = dialogView
                .findViewById(R.id.ll_no_interest);
        LinearLayout llReport = dialogView
                .findViewById(R.id.ll_report);
        llVideoShare.setVisibility(isVideo ? View.VISIBLE : View.GONE);
        if (isVideo) {
            dialogView.setBackgroundColor(getResources().getColor(R.color.colorF9F9F9));
        } else {
            dialogView.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (share == null) {
            share = ShareUtil.getInstance();
        }

        llWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                String modifyUrl = shareUrl;

                share.shareUrl(BaseActivity.this,
                        title, content, modifyUrl, imageUrl, ShareUtil.WEIXIN_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11232);
                }
            }
        });
        llMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.FRIENDSHIP_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11236);
                }
            }
        });
        llQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.QQ_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11234);
                }
            }
        });
        llQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.QZONE_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11235);
                }
            }
        });
        llWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.WEIBO_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11233);
                }
            }
        });

        if (isVideo) {
            llNoInteresting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareDialog.dismiss();
                    ToastHelper.showToast("感谢反馈，我们将持续为您优化推荐内容！");
                }
            });

            llReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareDialog.dismiss();
                    ToastHelper.showToast("感谢反馈，我们会尽快处理！");
                }
            });
        }
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
            }
        });
        shareDialog = BottomMenuDialog.createNewDialog(this, dialogView);
        shareDialog.show();

    }

    /**
     * 分享弹窗，没有使用集成分享SDK，而是每个单独实现的
     *
     * @param shareUrl
     * @param title
     * @param content
     * @param imageUrl
     * @param isLive
     * @param onClickListener
     */
    public void showShareDialog(final String shareUrl, final String title,
                                final String content, final String imageUrl, final boolean isLive,
                                final View.OnClickListener onClickListener) {
        Log.d(TAG, " showShareDialog shareUrl=" + shareUrl);

        if (!AccountInfoManager.getInstance().isLogin()) {
            LoginActivity.startActivity(this);
            return;
        }

        View dialogView = LayoutInflater.from(this).inflate(
                R.layout.dialog_share, null);
        Button tvCancle = (Button) dialogView.findViewById(R.id.tv_cancle);
        LinearLayout llWechat = (LinearLayout) dialogView
                .findViewById(R.id.ll_wechat);
        LinearLayout llMoment = (LinearLayout) dialogView
                .findViewById(R.id.ll_monent);
        LinearLayout llQQ = (LinearLayout)
                dialogView.findViewById(R.id.ll_qq);
        LinearLayout llWeibo = (LinearLayout) dialogView
                .findViewById(R.id.ll_weibo);
        LinearLayout llQzone = (LinearLayout) dialogView
                .findViewById(R.id.ll_qzone);
        if (share == null) {
            share = ShareUtil.getInstance();
        }

        llWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.WEIXIN_PLATFORM);
                if (onClickListener != null)
                    onClickListener.onClick(arg0);
            }
        });
        llMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.FRIENDSHIP_PLATFORM);
                if (onClickListener != null)
                    onClickListener.onClick(arg0);
            }
        });
        llQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.QQ_PLATFORM);
                if (onClickListener != null)
                    onClickListener.onClick(arg0);
            }
        });
        llQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.QZONE_PLATFORM);
                if (onClickListener != null)
                    onClickListener.onClick(arg0);
            }
        });
        llWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                share.shareUrl(BaseActivity.this,
                        title, content, shareUrl, imageUrl, ShareUtil.WEIBO_PLATFORM);
                if (onClickListener != null)
                    onClickListener.onClick(arg0);
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
            }
        });
        shareDialog = BottomMenuDialog.createNewDialog(this, dialogView);
        shareDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode=" + requestCode + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_QQ_SHARE && share != null && share.mTencentIUiListener != null) {
            Log.d(TAG, "onActivityResult 处理qq 分享回调");
            share.mTencent.handleResultData(data, share.mTencentIUiListener);
        } else if (requestCode == Constants.REQUEST_QZONE_SHARE && share != null && share.mTencentIUiListener != null) {
            Log.d(TAG, "onActivityResult 处理qzone分享回调");
            share.mTencent.handleResultData(data, share.mTencentIUiListener);
        }
//        else {
//            Log.d(TAG, "onActivityResult 处理qq qzone分享回调 " + share == null ? "share==null" : "" );
//        }
    }

    /**
     * 收到未登录通知
     *
     * @param event
     */
    boolean isHandleUnlogin = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        if (SubcriberTag.UNLOGIN.equals(event.tag)
                && AccountInfoManager.getInstance().checkUserIsLogin()) {
            Log.error(TAG, "looper = " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
            if (isResume && !isHandleUnlogin) {
                showToast(R.string.login_outdate);
                Log.error(TAG, "登录过期, 重新登录");
                isHandleUnlogin = true;
                AccountInfoManager.getInstance().loginOut();
                LiveApplication.getInstance().exitApp(this);
                LoginActivity.startActivity(this);
                EventBusManager.postEvent(0, SubcriberTag.FINISH_HOME_ACATIVITY);
//                finish();
            }
        } else if (SubcriberTag.WIFI_CHANGE_MOBILE.equals(event.tag)) {
            if (isResume()) {
                if (NetStateManager.getInstance().getIsAlreadyNotify()) {
                    showToast(R.string.in_mobile_net_be_careful);
                } else {
                    if (LiveApplication.getInstance().isInLiveRoomActivity) {
                        return;
                    }
                    NetStateManager.getInstance().showMobileNetWorkDialog(this, R.string.in_mobile_net_suggest_wifi_see, R.string.change_net, R.string.goon_use, new IDialogOnClickListener() {
                        @Override
                        public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                            if (viewId == GenericDialog.ID_BUTTON_YES) {
                                //跳去设置网络
                                NetStateManager.getInstance().goToNetSetting(BaseActivity.this);
                            }
                            return false;
                        }
                    });
                }
            }
        } else if (SubcriberTag.DO_PAY_THIRD.equals(event.tag)) {
            int type = (int) event.event;
            Log.d(TAG, " DO_PAY_THIRD type=" + type);
//            if(Utils.isEmpty(CommonConst.ORDERID)){
//                Log.d(TAG,"ORDERID==null不用处理");
//                return;
//            }
            if (!isNeedHandlePay) {
                Log.d(TAG, "isNeedHandlePay = false 不用处理");
                return;
            } else {
                Log.d(TAG, "isNeedHandlePay = truue  处理");
            }
            if (type == OrderDialog.WECHAT) {
                tryGetWeChatPayInfo(CommonConst.ORDERID);
            } else if (type == OrderDialog.ALIPAY) {
                tryGetAliPayOrderInfo(CommonConst.ORDERID);
            }
            isNeedHandlePay = false;
//            CommonConst.ORDERID = "";
        }
    }


//    @Subscribe
//    public void onEventMainThread(PostEvent event) {
//
//    }

//    @Subscriber(tag = SubcriberTag.UNLOGIN)
//    private void eventUnlogin(int event) {
//        if (isResume && !isHandleUnlogin) {
//            showToast(R.string.login_outdate);
//            isHandleUnlogin = true;
//            LiveApplication.getInstance().logout();
//            UiPreference.saveObject(Common.KEY_MYSELF, null);
//            LoginActivity.startActivity(this);
//            EventBusManager.postEvent(0, SubcriberTag.FINISH_HOME_ACATIVITY);
//            finish();
//        }
//    }


    public void showButtonDialog(String title, String content, int yesRes, int noRes, boolean cancelAble, boolean cancelOutside, boolean isReplace, boolean recomendButtonYes, IDialogOnClickListener onClickListener
    ) {
        if (isDestroyed()) {
            return;
        }
        SimpleTextDialog curDialog = null;
        if (isReplace) {//替换当前弹窗（如果有）
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new SimpleTextDialog(this);
            curDialog = dialog;
        } else {//新建窗口
            curDialog = new SimpleTextDialog(this);
            dialog = curDialog;
        }
        curDialog.setTitle(title);
        curDialog.setText(content);
        if (noRes == 0) {
            dialog.addSingleButton(GenericDialog.ID_BUTTON_YES, ResourceHelper.getString(R.string.yes));
            curDialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
        } else {
            curDialog.addYesNoButton(ResourceHelper.getString(yesRes), ResourceHelper.getString(noRes));
            curDialog.setRecommendButton(recomendButtonYes ? GenericDialog.ID_BUTTON_YES : GenericDialog.ID_BUTTON_NO);
        }
        curDialog.setCancelable(cancelAble);
        curDialog.setCanceledOnTouchOutside(cancelOutside);
        curDialog.setOnClickListener(onClickListener);
        curDialog.show();
    }

    public void showButtonDialog(int titleRes, int contentRes, int yesRes, int noRes, boolean cancelAble, boolean cancelOutside, boolean isReplace, boolean recomendButtonYes, IDialogOnClickListener onClickListener
    ) {
        showButtonDialog(ResourceHelper.getString(titleRes), ResourceHelper.getString(contentRes), yesRes, noRes, cancelAble, cancelOutside, isReplace, recomendButtonYes, onClickListener);
    }

    public void showButtonDialog(int titleRes, int contentRes, int yesRes, int noRes, boolean cancelAble, boolean cancelOutside, IDialogOnClickListener onClickListener
    ) {
        showButtonDialog(titleRes, contentRes, yesRes, noRes, cancelAble, cancelOutside, false, true, onClickListener);
    }


    public void dismissButtonDialog() {
        if (mDestroyed == false && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    LoadingDialog loadingDialog;

    public void showLoadingDialog() {
        showLoadingDialog(true);
    }

    public void showLoadingDialog(String... msg) {
        showLoadingDialog(true, msg);
    }

    // 显示新版进度圈
    public void showNewDialog(String msg) {

        if (loadingNewDialog == null) {
            loadingNewDialog = new NewLoadingDialog(this);
            loadingNewDialog.setCanceledOnTouchOutside(false);
        }

        // 文本要更新
        loadingNewDialog.setLoadingTip(msg);

        // 如果已经在显示
        if (!loadingNewDialog.isShowing())
            loadingNewDialog.show();

    }


    public void showLoadingDialog(boolean canCancel, String... msg) {
        if (isDestroyed()) {
            return;
        }
        if (!mDestroyed && loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        String text = "";
        if (msg != null && msg.length > 0) {
            text = msg[0];
        }
        loadingDialog = new LoadingDialog(this, R.style.loading_dialog, text);
        loadingDialog.setCancelable(canCancel);
        loadingDialog.setCanceledOnTouchOutside(canCancel);
        loadingDialog.show();
    }

    public void dismissLoadingsDialog() {
        Log.d(TAG, "dismissLoadingsDialog");
        if (!mDestroyed && loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    // 关闭所有对话框
    public void dismissDialog() {

        if (!mDestroyed && loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

        if (!mDestroyed && loadingNewDialog != null && loadingNewDialog.isShowing()) {
            loadingNewDialog.dismiss();
        }

    }

    public static ChatMsg createPictureMessagee(String content, boolean isSend, long date, String userId, String nickName, String avatar) {
        ChatMsg message = new ChatMsg();
        message.setIsSend(isSend);
        message.setContent(content);
        message.setDate(date);//毫秒数
        message.setUserId(userId);
        message.setNickName(nickName);
        message.setAvatar(avatar);
        message.setType(ChatEntity.MSG_TYPE_PICTURE);
        return message;
    }

    public static ChatMsg createMessagee(String content, boolean isSend, long date, String userId, String nickName, String avatar) {
        ChatMsg message = new ChatMsg();
        message.setIsSend(isSend);
        message.setContent(content);
        message.setDate(date);//毫秒数
        message.setUserId(userId);
        message.setNickName(nickName);
        message.setAvatar(avatar);
        message.setType(ChatEntity.MSG_TYPE_TEXT);
        return message;
    }

    public static ChatMsg createGiftMessagee(String content, boolean isSend, long date, String userId, String nickName, String avatar,
                                             int giftId) {
        ChatMsg message = new ChatMsg();
        message.setIsSend(isSend);
        message.setContent(content);
        message.setDate(date);
        message.setUserId(userId);
        message.setNickName(nickName);
        message.setAvatar(avatar);
        message.setGiftId(giftId);
        message.setType(ChatEntity.MSG_TYPE_GIFT);//礼物
        return message;
    }

    public static void updateMessage(ChatMsg chatMsg) {
        DbManger.getInstance().updateMessage(chatMsg);
    }

    public static void deleteMessage(ChatMsg chatMsg) {
        DbManger.getInstance().deleteMsg(chatMsg);
    }

    public static void saveMessage(ChatMsg message, boolean isUnread) {
        message.setIsUnread(isUnread);
        DbManger.getInstance().addChatMsg(message);
    }

    public static void saveMessage(ChatMsg message, boolean isUnread, int follow, int level, int gender) {
        message.setIsUnread(isUnread);
        DbManger.getInstance().addChatMsg(message, follow, level, gender, true);
    }

    private SimpleTextDialog dialog;

    public void showRechargeDialog(final String errMsg) {
        showButtonDialog(R.string.go_recharge_page_title, R.string.need_recharge, R.string.go_recharge_page, R.string.cancel,
                true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            jumpToRecharge();
                            addAnalyticsReport(errMsg);
                        }
                        return false;
                    }
                });
    }

    private void addAnalyticsReport(String errMsg) {
        if ("1".equals(errMsg)) {//送礼余额不足
            AnalyticsReport.onEvent(LiveApplication.getInstance(), LiveReport.MY_LIVE_EVENT_11249);
        } else if ("2".equals(errMsg)) {
//                    AnalyticsReport.onEvent(LiveApplication.getInstance(), LiveReport.MY_LIVE_EVENT_11249);
        }
    }

    public void jumpToRecharge() {
        MyCoinsActivity.startActivity(this);
    }

    /**
     * 显示软键盘
     */
    public static void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }


    protected void showData() {
        mErrorState = null;
        if (mDataView != null) {
            mDataView.setVisibility(View.VISIBLE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    protected void showLoading() {
        mErrorState = null;
        if (mDataView != null) {
            mDataView.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            Log.d(TAG, " showLoading");
            mLoadingView.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, " mLoadingView==null");
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    private void showError() {
        if (mDataView != null) {
            mDataView.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    protected void showEmpty() {
        mErrorState = PageListLayout.ErrorState.ERROR_STATE_EMPTY;
        showError();
        if (mErrorText != null) {
            mErrorText.setText(R.string.empty_tips);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorText, null,
                    ContextCompat.getDrawable(this, R.drawable.public_pic_empty), null, null);
        }
    }

    protected void showDataException() {
        mErrorState = PageListLayout.ErrorState.ERROR_STATE_DATA_EXCEPTION;
        if (mErrorText != null) {
            mErrorText.setText(R.string.empty_tips);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorText, null,
                    ContextCompat.getDrawable(this, R.drawable.public_pic_empty), null, null);
        }
    }

    protected void showNetworkError() {
        mErrorState = PageListLayout.ErrorState.ERROR_STATE_NETWORK_ERROR;
        showError();
        if (mErrorText != null) {
            mErrorText.setText(R.string.network_error_tips);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorText, null,
                    ContextCompat.getDrawable(this, R.drawable.public_pic_nowifi), null, null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Intent intent = getIntent();
        if (outState != null && intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                outState.putAll(bundle);
            }
        }
    }


    /**
     * 便捷打印log
     */
    public void log(String msg) {
        Log.log(msg);
    }

    /**
     * @param editText 控件
     * @return 获取EditText的文本
     */
    public String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * @param textView 控件
     * @return 获取TextView的文本
     */
    public String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * @param checkBox 控件
     * @return CheckBox
     */
    public String getText(CheckBox checkBox) {
        return checkBox.getText().toString().trim();
    }

    /**
     * @param editText 控件
     * @return 获取EditText的文本的长度
     */
    public int getEditTextLength(EditText editText) {
        return editText.getText().toString().trim().length();
    }

    /**
     * @param editText 控件
     * @return 将光标移至最后
     */
    public void setSelection(EditText editText) {
        editText.setSelection(getEditTextLength(editText));
    }

    /**
     * @param editText 控件
     * @return 将光标移至最后
     */
    public void setSelection(EditText editText, String text) {
        editText.setText(text);
        editText.setSelection(text.length());
    }

    protected void startActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        ActivityCompat.startActivity(this, intent, null);
    }

    protected void startActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(Class cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode, bundle);
    }

    protected <T extends View> T findById(int resId) {
        return ViewUtils.findById(getWindow().getDecorView(), resId);
    }

    /**
     * 子类可以重写这个方法，实现界面进场动画
     */
    protected void activityAnim() {
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);//右边进场
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityAnim();
    }

    @Override
    public void finish() {
        super.finish();
        activityAnim();
    }

    public void goLogin() {
        LoginActivity.startActivity(this, LoginActivity.TYPE_FROM_NEED_LOGIN);
    }

    //支付
    private WXHelper mWXHelper;
    private AliPayHelper mAliPayHelper;
    private boolean isStartWeChatPay = false;
    public boolean isNeedHandlePay = false;

    // 获取微信支付参数
    private void tryGetWeChatPayInfo(String orderId) {

        if (mWXHelper == null) {
            mWXHelper = new WXHelper(mContext);
        }

        if (!mWXHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }

        isStartWeChatPay = true;
        showLoadingDialog("正在加载...");
        DataProvider.getOrderWechatPayInfo(orderId,
                new GsonHttpConnection.OnResultListener<OrderPayInfoMsg>() {
                    @Override
                    public void onSuccess(OrderPayInfoMsg msg) {
                        handleGetWeChatPayInfoSuccess(msg);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        ToastHelper.showToast(errorMsg);
                        dismissLoadingsDialog();
                    }
                });
    }

    // 调起微信支付
    private void handleGetWeChatPayInfoSuccess(OrderPayInfoMsg msg) {
        dismissLoadingsDialog();
        if (msg == null) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.my_get_order_info_fail_tips));
            return;
        }
        if (mWXHelper == null) {
            mWXHelper = new WXHelper(mContext);
        }
        if (!mWXHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }
        Log.d(TAG, " handleGetWeChatPayInfoSuccess sendPayReq");
        isStartWeChatPay = true;
        mWXHelper.sendPayReq(OrderOrderPayInfo.toPayReq(msg.getData().getPayParams()));
    }

    // 微信支付成功的回调
    public void handleOnWeChatPayResultCallback(int resultCode) {

        if (resultCode == BaseResp.ErrCode.ERR_OK) {
//            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
            paySuccess();
            return;
        }
        if (resultCode == WXHelper.WE_CHAT_PAY_TYPE_CANCEL) {
//            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
            payCancel();
            return;
        }

        payFailed();

    }

    // 调起支付宝支付
    private void tryGetAliPayOrderInfo(String orderId) {

        showLoadingDialog("正在加载...");
        DataProvider.getOrderAliPayInfo(orderId,
                new GsonHttpConnection.OnResultListener<OrderPayInfoMsg>() {
                    @Override
                    public void onSuccess(OrderPayInfoMsg msg) {
                        mAliPayHelper.startPayTask(mContext, msg.getData().getAliPayParams());
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        ToastHelper.showToast(errorMsg);
                        dismissLoadingsDialog();
                    }
                });

        mAliPayHelper = new AliPayHelper(new AliPayHelper.AliPayCallback() {
            @Override
            public void onSuccess(String payResultInfo) {
                dismissLoadingsDialog();
//                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
                // 支付宝支付成功后的回调
                paySuccess();
            }

            @Override
            public void onCancel() {
                dismissLoadingsDialog();
//                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
                payCancel();
            }

            @Override
            public void onWait() {
                dismissLoadingsDialog();
//                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_wait_tips));
            }

            @Override
            public void onFailed() {
                dismissLoadingsDialog();
//                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_fail_tips));
                payFailed();
            }
        });

    }

    private void payCancel() {
        ToastHelper.showToast(R.string.pay_cancel_tips);
        EventBusManager.postEvent(0, SubcriberTag.PAY_CANCEL);
    }

    // 支付失败
    private void payFailed() {
        ToastHelper.showToast(R.string.pay_fail_tips);
        EventBusManager.postEvent(0, SubcriberTag.PAY_FAIL);
    }

    // 支付成功
    private void paySuccess() {
        ToastHelper.showToast(R.string.pay_success_tips);
        EventBusManager.postEvent(0, SubcriberTag.PAY_SUCCESS);
        //通知支付成功处理
//        OrderSuccessActivity.startActivity(mContext, mBalanceInfo.getShoppingAddressBean());
//        LiveActivityManager.getInstance().popUntilActivity(MainActivity.class);
    }
}
