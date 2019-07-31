/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laka.live.ui.widget.chatKeyboard;

import android.app.Activity;
import android.content.Context;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.ChatEntity;
import com.laka.live.bean.GiftInfo;
import com.laka.live.gift.GiftAudioManager;
import com.laka.live.gift.GiftResManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.RoomManager;
import com.laka.live.msg.ProductsListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.account.my.MyCoinsActivity;
import com.laka.live.ui.chat.ChatMessageView;
import com.laka.live.ui.widget.ButtonDialog;
import com.laka.live.ui.widget.emoji.EmoticonPickerView;
import com.laka.live.ui.widget.emoji.IEmoticonSelectedListener;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.ui.widget.emoji.StringUtil;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.util.Common;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

/**
 * 控件主界面
 *
 * @author kymjs (http://www.kymjs.com/)
 */
public class KJChatKeyboard extends RelativeLayout implements
        SoftKeyboardStateHelper.SoftKeyboardStateListener, IEmoticonSelectedListener, View.OnClickListener, GiftGridView.GiftGridListener, EventBusManager.OnEventBusListener {
    public static final String TAG = "KJChatKeyboard";



    public interface OnToolBoxListener {
        void onShowFace();
    }

    public static final int LAYOUT_TYPE_HIDE = 0;
    public static final int LAYOUT_TYPE_FACE = 1;
    public static final int LAYOUT_TYPE_MORE = 2;

    /**
     * 最上层输入框
     */
    private EditText mEtMsg;
    private CheckBox mBtnFace;
    private Button mBtnSend;

    /**
     * 表情
     */
    // 表情
    public EmoticonPickerView emoticonPickerView;  // 贴图表情控件
    //    private ViewPager mPagerFaceCagetory;
//    private RelativeLayout mRlFace;
//    private PagerSlidingTabStrip mFaceTabs;
//
    private int layoutType = LAYOUT_TYPE_HIDE;
    //    private FaceCategroyAdapter adapter;  //点击表情按钮时的适配器
//
//    private List<String> mFaceData;
    private BaseActivity activity;
    private Context context;
    private OnOperationListener listener;
    private OnToolBoxListener mFaceListener;
    private SoftKeyboardStateHelper mKeyboardHelper;

    /**
     * 礼物
     */
    private ImageView ivGift;
    public GiftGridView giftGridView;
    private GiftInfo chooseGift;

    /**
     * 图片
     */
    private ImageView mIvPicture;
    private LinearLayout mLlPhoto, mLlAlbum;
    private RelativeLayout mRlPictureView;

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public KJChatKeyboard(Context context) {
        super(context);
        init(context);
    }

    public KJChatKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KJChatKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View root = View.inflate(context, R.layout.chat_tool_box, null);
        this.addView(root);
        EventBusManager.register(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
        this.initWidget();
    }

    private void initData() {
        getMyUserInfo();
        mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext())
                .getWindow().getDecorView());
        mKeyboardHelper.addSoftKeyboardStateListener(this);
    }

    RelativeLayout rlBottomBar;

    private void initWidget() {
        uiHandler = new Handler();
        rlBottomBar = (RelativeLayout) findViewById(R.id.rl_bottombar);//防止点击事件传递到下层
        rlBottomBar.setOnClickListener(this);

        mEtMsg = (EditText) findViewById(R.id.toolbox_et_message);
        initTextEdit();
        mBtnSend = (Button) findViewById(R.id.toolbox_btn_send);

        mIvPicture = (ImageView) findViewById(R.id.iv_pic);
        mIvPicture.setOnClickListener(this);

        mBtnFace = (CheckBox) findViewById(R.id.toolbox_btn_face);
//        mBtnMore = (CheckBox) findViewById(R.id.toolbox_btn_more);
        // 表情
        emoticonPickerView = (EmoticonPickerView) findViewById(R.id.emoticon_picker_view);
//        mRlFace = (RelativeLayout) findViewById(R.id.toolbox_layout_face);
//        mPagerFaceCagetory = (ViewPager) findViewById(R.id.toolbox_pagers_face);
//        mFaceTabs = (PagerSlidingTabStrip) findViewById(R.id.toolbox_tabs);
//        adapter = new FaceCategroyAdapter(((FragmentActivity) getContext())
//                .getSupportFragmentManager(), LAYOUT_TYPE_FACE);
        mBtnSend.setOnClickListener(this);
        // 点击表情按钮
        mBtnFace.setOnClickListener(getFunctionBtnListener(LAYOUT_TYPE_FACE));
        // 点击表情按钮旁边的加号
//        mBtnMore.setOnClickListener(getFunctionBtnListener(LAYOUT_TYPE_MORE));
        // 点击消息输入框
        mEtMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLayout();
            }
        });

        mEtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                com.laka.live.util.Log.d(TAG, "mEtMsg afterTextChanged =");
                String content = s.toString();
                if (Utils.isEmpty(content)) {
                    mBtnSend.setVisibility(View.GONE);
                    mIvPicture.setVisibility(View.VISIBLE);
                } else {
                    mBtnSend.setVisibility(View.VISIBLE);
                    mIvPicture.setVisibility(View.GONE);
                }

            }
        });
        ivGift = (ImageView) findViewById(R.id.iv_gift);
        ivGift.setOnClickListener(this);
        mBtnSend.setVisibility(View.GONE);
        initGiftView();
        emoticonPickerView.setVisibility(View.GONE);
        mEtMsg.setText("测试");
        mEtMsg.setText("");

        //图片
        initPictureView();
    }

    /**
     * 初始化发图片选框
     */
    private void initPictureView() {
        mLlPhoto = (LinearLayout) findViewById(R.id.take_photo);
        mLlPhoto.setOnClickListener(this);
        mLlAlbum = (LinearLayout) findViewById(R.id.album);
        mLlAlbum.setOnClickListener(this);
        mRlPictureView = (RelativeLayout) findViewById(R.id.pic_panel);
        mRlPictureView.setVisibility(View.GONE);
    }

    /**
     * 初始化礼物选框
     */
    private void initGiftView() {
        giftGridView = (GiftGridView) findViewById(R.id.gift_grid_view);
        giftGridView.fromPage = GiftGridView.PAGE_CHAT_MESSAGE;
        giftGridView.setListeners(this, this, false);
        giftGridView.setVisibility(View.GONE);
//        giftGridView.setBalance(AccountInfoManager.getInstance().getCurrentAccountCoins());
    }

    private void initTextEdit() {
        mEtMsg.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        mEtMsg.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    switchToTextLayout(true);
//                }
//                return false;
//            }
//        });

//        mEtMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                messageEditText.setHint("");
//                checkSendButtonEnable(messageEditText);
//            }
//        });

        mEtMsg.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                checkSendButtonEnable(messageEditText);
                MoonUtil.replaceEmoticons(context, s, start, count);//防止图片超出输入框边界
                int editEnd = mEtMsg.getSelectionEnd();
                mEtMsg.removeTextChangedListener(this);
                while (StringUtil.counterChars(s.toString()) > 5000 && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                mEtMsg.setSelection(editEnd);
                mEtMsg.addTextChangedListener(this);

//                sendTypingCommand();
            }
        });
    }

    /*************************
     * 内部方法 start
     ************************/

    private OnClickListener getFunctionBtnListener(final int which) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow()) {//&& which == layoutType
                    Log.d(TAG, "hideLayout");
                    hideLayout();
                    showKeyboard(context);
                    mEtMsg.requestFocus();
                } else {
                    Log.d(TAG, "showLayout");
                    changeLayout(which);
                    showLayout();
//                    mBtnFace.setChecked(layoutType == LAYOUT_TYPE_FACE);
//                    mBtnMore.setChecked(layoutType == LAYOUT_TYPE_MORE);
                }
            }
        };
    }

    private void changeLayout(int mode) {
//        adapter = new FaceCategroyAdapter(((FragmentActivity) getContext())
//                .getSupportFragmentManager(), mode);
//        adapter.setOnOperationListener(listener);
//        layoutType = mode;
//        setFaceData(mFaceData);
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        hideLayout();
    }

    @Override
    public void onSoftKeyboardClosed() {
    }

    /***************************** 内部方法 end ******************************/

    /**************************
     * 可选调用的方法 start
     **************************/

//    public void setFaceData(List<String> faceData) {
//        mFaceData = faceData;
//        adapter.refresh(faceData);
//        mPagerFaceCagetory.setAdapter(adapter);
//        mFaceTabs.setViewPager(mPagerFaceCagetory);
//        if (layoutType == LAYOUT_TYPE_MORE) {
//            mFaceTabs.setVisibility(GONE);
//        } else {
//            //加1是表示第一个分类为默认的emoji表情分类，这个分类是固定不可更改的
//            if (faceData.size() + 1 < 2) {
//                mFaceTabs.setVisibility(GONE);
//            } else {
//                mFaceTabs.setVisibility(VISIBLE);
//            }
//        }
//    }
    public EditText getEditTextBox() {
        return mEtMsg;
    }

    public void showLayout() {
        hideKeyboard(this.context);
        // 延迟一会，让键盘先隐藏再显示表情键盘，否则会有一瞬间表情键盘和软键盘同时显示
        postDelayed(new Runnable() {
            @Override
            public void run() {
//                mRlFace.setVisibility(View.VISIBLE);
//                emoticonPickerView.setVisibility(View.VISIBLE);
                showEmojiLayout();
                if (mFaceListener != null) {
                    mFaceListener.onShowFace();
                }
            }
        }, 10);
    }


    public boolean isShow() {
//        return mRlFace.getVisibility() == VISIBLE;
        return emoticonPickerView.getVisibility() == VISIBLE;
    }

    public void hideLayout() {
//        mRlFace.setVisibility(View.GONE);
//        emoticonPickerView.setVisibility(View.GONE);
        hideEmojiLayout();
        giftGridView.hide();
        mRlPictureView.setVisibility(View.GONE);
        if (mBtnFace.isChecked()) {
            mBtnFace.setChecked(false);
        }
//        if (mBtnMore.isChecked()) {
//            mBtnMore.setChecked(false);
//        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(Context context) {
        if (rlBottomBar != null) {
            rlBottomBar.requestFocus();
        }
        Activity activity = (Activity) context;
        if (activity != null) {
            isKeyboradShow = false;
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    public static boolean isKeyboradShow;

    /**
     * 显示软键盘
     */
    public static void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            isKeyboradShow = true;
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            AnalyticsReport.onEvent(context, LiveReport.MY_LIVE_EVENT_15007);
        }
    }


    public OnOperationListener getOnOperationListener() {
        return listener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.listener = onOperationListener;
//        adapter.setOnOperationListener(onOperationListener);
    }

    public void setOnToolBoxListener(OnToolBoxListener mFaceListener) {
        this.mFaceListener = mFaceListener;
    }

    /***********************
     * 可选调用的方法 end
     ******************************/

    // 点击表情，切换到表情布局
    private void toggleEmojiLayout() {
        if (emoticonPickerView == null || emoticonPickerView.getVisibility() == View.GONE) {
            showEmojiLayout();
        } else {
            hideEmojiLayout();
        }
    }

    private Handler uiHandler;

    // 隐藏表情布局
    public void hideEmojiLayout() {
        mBtnFace.setBackgroundResource(R.drawable.selector_chat_face);
        uiHandler.removeCallbacks(showEmojiRunnable);
        if (emoticonPickerView != null) {
            emoticonPickerView.setVisibility(View.GONE);
        }
    }

    // 显示表情布局
    private void showEmojiLayout() {
        mBtnFace.setBackgroundResource(R.drawable.chat_keyboard_selector);
        hideKeyboard(context);
//        hideActionPanelLayout();
//        hideAudioLayout();
//        mEtMsg.requestFocus();不获取焦点
        uiHandler.postDelayed(showEmojiRunnable, 10);
        emoticonPickerView.setVisibility(View.VISIBLE);
        emoticonPickerView.show(this);

        giftGridView.hide();
        mRlPictureView.setVisibility(View.GONE);
    }

    private Runnable showEmojiRunnable = new Runnable() {
        @Override
        public void run() {
            emoticonPickerView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = mEtMsg.getText();
        if (key.equals("/DEL")) {
            mEtMsg.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = mEtMsg.getSelectionStart();
            int end = mEtMsg.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }
    }

    private Runnable hideAllInputLayoutRunnable;

    @Override
    public void onStickerSelected(String category, String item) {
//        Log.i("InputPanel", "onStickerSelected, category =" + category + ", sticker =" + item);
//
//        if (customization != null) {
//            MsgAttachment attachment = customization.createStickerAttachment(category, item);
//            IMMessage stickerMessage = MessageBuilder.createCustomMessage(container.account, container.sessionType, "贴图消息", attachment);
//            container.proxy.sendMessage(stickerMessage);
//        }
    }

    @Override
    public void setChooseGift(GiftInfo gift) {
        chooseGift = gift;
    }


    int mFrom;
    public void setFrom(int from) {
        mFrom = from;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_gift_recharge://礼物框跳充值
                if(mFrom== Common.FROM_PRIVATE_MSG){
                    AnalyticsReport.onEvent(context, LiveReport.MY_LIVE_EVENT_15010);
                }else{
                    AnalyticsReport.onEvent(context, LiveReport.MY_LIVE_EVENT_11247);
                }
                jumpToRecharge();
                break;
            case R.id.btn_send_gift_multi:
                //连送一个并重置时间
//                showAnimGiftAddCount();
//               btnSendGiftMulti.startTimer();
                if (activity != null) {
                    if (!NetworkUtil.isNetworkOk(activity)) {
                        activity.showToast(R.string.network_unok_wait_try);
                        return;
                    }
                }
                if (listener != null) {
                    listener.sendGift(chooseGift);
                }
                giftGridView.startSendGiftMultiTimer();
                break;
            case R.id.btn_send_gift://礼物框送礼确认
                if (chooseGift == null) {
                    activity.showToast(R.string.please_choose_gift);
                    return;
                }
                if (!GiftResManager.getInstance().checkIsResReady(chooseGift)) {
                    activity.showToast(R.string.gift_readying);
                    return;
                }

                if( curWeidou< chooseGift.getKazuan()){
                    //余额不足
                    EventBusManager.postEvent(0,SubcriberTag.SEND_CHAT_GIFT_NOT_ENOUGH);
                    return;
                }

                HashMap<String, String> parmas = new HashMap<>();
                parmas.put("ID", chooseGift.getIdStr());
                AnalyticsReport.onEvent(context, LiveReport.MY_LIVE_EVENT_15011, parmas);
//                if (chooseGift.isMulti()) {
                if (false) {//私信不连送
                    //开启连送模式
                    showMultiGiftMode();
                } else {
                    //普通送礼物
                    /*if(activity!=null) {
                        if(!NetworkUtil.isNetworkOk(activity)){
                            activity.showToast(R.string.network_unok_wait_try);
                            return;
                        }
                    }*/
                    if (listener != null) {
                        listener.sendGift(chooseGift);
                    }
                }
                break;

            case R.id.toolbox_btn_send:
                if (listener != null) {
                    String content = mEtMsg.getText().toString();
                    listener.send(content);
                    mEtMsg.setText(null);
                }
                break;
            case R.id.iv_gift:
                AnalyticsReport.onEvent(context, LiveReport.MY_LIVE_EVENT_15008);
                giftGridView.show();
                curWeidou = AccountInfoManager.getInstance().getCurrentAccountCoins();
                giftGridView.setBalance(AccountInfoManager.getInstance().getCurrentAccountCoins());
                if (emoticonPickerView.getVisibility() == View.VISIBLE) {
                    emoticonPickerView.setVisibility(View.GONE);
                }
                hideKeyboard(context);
                mRlPictureView.setVisibility(View.GONE);
                break;
            case R.id.iv_pic:
                mRlPictureView.setVisibility(View.VISIBLE);
                giftGridView.hide();
                emoticonPickerView.setVisibility(View.GONE);
                hideKeyboard(context);
                break;
            case R.id.album:
                if (listener != null) {
                    listener.selectedFunction(OnOperationListener.FUNCATION_CHOICE_PHOTO);
                }
                break;
            case R.id.take_photo:
                if (listener != null) {
                    listener.selectedFunction(OnOperationListener.FUNCATION_TAKE_PHOTO);
                }
                break;
            default:
                Log.d(TAG, "default");
                break;
        }

    }

    /**
     * 打开连送模式
     */
    private void showMultiGiftMode() {
//        showGiftAnimation();
        giftGridView.showMultiGiftMode();
    }

    ButtonDialog needRechargeDialog;

//    protected void showRechargeDialog() {
//        if (needRechargeDialog == null) {
//            needRechargeDialog = new ButtonDialog(activity);
//            needRechargeDialog.setContent(R.string.need_recharge);
//            needRechargeDialog.setNeutralButtonText(R.string.cancel);
//            needRechargeDialog.setPositiveButtonText(R.string.go_recharge_page);
//            needRechargeDialog.setNeutralButtonClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    needRechargeDialog.dismiss();
//                }
//            });
//            needRechargeDialog.setPositiveButtonClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    needRechargeDialog.dismiss();
//                    jumpToRecharge();
//                }
//            });
//        }
//        if (!needRechargeDialog.isShowing()) {
//            needRechargeDialog.show();
//        }
//
//
//    }

    protected void jumpToRecharge() {
        /*if(!NetworkUtil.isNetworkOk(activity)){
            activity.showToast(R.string.network_unok_wait_try);
            return;
        }*/
        MyCoinsActivity.startActivity(activity);
    }

    double curWeidou;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        com.laka.live.util.Log.d(TAG, " onEvent tag=" + event.tag);
        if (SubcriberTag.MULTI_SEND_GIFT_FINISH.equals(event.tag)) {
            giftGridView.hideMultiGiftMode();
        } else if (SubcriberTag.REFRESH_LAST_KAZUAN.equals(event.tag)) {
            double result = (double) event.event;
            curWeidou = result;
            com.laka.live.util.Log.d(TAG, "REFRESH_LAST_KAZUAN coins=" + result);
            if (giftGridView != null) {
                giftGridView.setBalance(result);
            }
        }
    }

//    @Subscriber(tag = SubcriberTag.MULTI_SEND_GIFT_FINISH)
//    private void eventMultiSendGiftFinish(int event) {
//        giftGridView.hideMultiGiftMode();
//    }


    private void getMyUserInfo() {
        DataProvider.queryProduct(this, new GsonHttpConnection.OnResultListener<ProductsListMsg>() {
            @Override
            public void onSuccess(ProductsListMsg msg) {
//                if (msg.isSuccessFul()) {
//                    giftGridView.setBalance(msg.getBalance());
//                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }

    public void stop() {
        EventBusManager.unregister(this);
    }

    public void start() {
//        EventBusManager.register(this);

        if (giftGridView != null) {
            curWeidou = AccountInfoManager.getInstance().getCurrentAccountCoins();
            giftGridView.setBalance(AccountInfoManager.getInstance().getCurrentAccountCoins());
        }
    }

}
