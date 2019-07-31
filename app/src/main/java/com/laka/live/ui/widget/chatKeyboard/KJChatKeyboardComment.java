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

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.emoji.EmoticonPickerView;
import com.laka.live.ui.widget.emoji.IEmoticonSelectedListener;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

/**
 * 控件主界面
 */
public class KJChatKeyboardComment extends RelativeLayout implements IEmoticonSelectedListener,
        SoftKeyboardStateHelper.SoftKeyboardStateListener, View.OnClickListener {
    public final static String TAG = KJChatKeyboardComment.class.getSimpleName();

    /**
     * 输入法事件监听
     */
    public interface OnToolBoxListener {
        void onShowFace();
    }

    /**
     * 最上层输入框
     */
    public EditText mEtMsg;
    private CheckBox mBtnFace;
    private TextView mBtnSend;

    private CheckBox mAlsoCb;

    private TextView mInputCountTv;

    public EmoticonPickerView mEmoticonPickerView;  // 贴图表情控件

    private Context mContext;
    private OnCommentOperationListener mListener;
    private OnToolBoxListener mFaceListener;

    private RelativeLayout mCommentRl;

    private boolean isComment = false;

    private BaseActivity mActivity;

    public KJChatKeyboardComment(Context context) {
        this(context, null);
    }

    public KJChatKeyboardComment(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KJChatKeyboardComment(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI(context);
    }

    /**
     * 初始化界面
     *
     * @param context context
     */
    private void initUI(Context context) {
        mContext = context;

        inflate(context, R.layout.view_comment_box, this);
        initWidget();
    }

    private void showAlsoComment(boolean show) {
        mAlsoCb.setVisibility(show ? VISIBLE : GONE);
        mAlsoCb.setChecked(show);
        isComment = show;
    }

    public boolean isCommentable() {
        return isComment;
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        mCommentRl = (RelativeLayout) findViewById(R.id.comment_rl);//防止点击事件传递到下层
        mCommentRl.setOnClickListener(this);

        mEtMsg = (EditText) findViewById(R.id.comment_et);
        mEtMsg.setText(null);
        mEtMsg.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
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
                MoonUtil.replaceEmoticons(mContext, s, start, count);//防止图片超出输入框边界
                String string = s.toString().trim();
                mEtMsg.setSelection(s.length());

                mInputCountTv.setText(String.valueOf(s.length()));

                if (string.length() > 0) {
                    mBtnSend.setEnabled(true);
                } else {
                    mBtnSend.setEnabled(false);
                }
            }
        });

        mBtnSend = (TextView) findViewById(R.id.send_tv);
        mBtnFace = (CheckBox) findViewById(R.id.face_cb);

        mAlsoCb = (CheckBox) findViewById(R.id.comment_cb);

        mInputCountTv = (TextView) findViewById(R.id.input_count_tv);

        mEmoticonPickerView = (EmoticonPickerView) findViewById(R.id.emoticon_picker_view);
        mEmoticonPickerView.setVisibility(GONE);

        findViewById(R.id.cancel_tv).setOnClickListener(this);
        findViewById(R.id.blank_view).setOnClickListener(this);

        mBtnSend.setOnClickListener(this);
        mBtnFace.setOnClickListener(this);  // 点击表情按钮
        mEtMsg.setOnClickListener(this);  // 点击消息输入框
        mAlsoCb.setOnClickListener(this);
    }

    /**
     * 隐藏表情布局
     */
    private void hideEmojiLayout() {
        if (mEmoticonPickerView != null) {
            mEmoticonPickerView.setVisibility(GONE);
        }

        if (mBtnFace.isChecked()) {
            mBtnFace.setChecked(false);
        }
    }

    /**
     * 判断表情是否显示
     *
     * @return true 显示 ， false 未显示
     */
    private boolean isFaceShow() {
        return mEmoticonPickerView != null && mEmoticonPickerView.getVisibility() == VISIBLE;
    }

    /**
     * 显示表情布局
     */
    private void showEmojiLayout() {
        // 延迟一会，让键盘先隐藏再显示表情键盘，否则会有一瞬间表情键盘和软键盘同时显示
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mEmoticonPickerView.setVisibility(VISIBLE);
                mEmoticonPickerView.show(KJChatKeyboardComment.this);

                if (mFaceListener != null) {
                    mFaceListener.onShowFace();
                }

                mBtnFace.setChecked(true);
            }
        }, 30);
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (mCommentRl != null) {
            mCommentRl.requestFocus();
        }

        if (mActivity != null) {
            InputMethodManager imm = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && mActivity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }

    }

//    private InputMethodManager mIMM;
//
//    /**
//     * 初始化输入法管理器
//     */
//    private void initImm() {
//        if (mFragment == null) {
//            return;
//        }
//
//        if (mIMM == null) {
//            mIMM = (InputMethodManager) mFragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        }
//    }

    /**
     * 显示软键盘
     */
    private void showKeyboard() {
        mEtMsg.requestFocus();

        if (mActivity != null) {
            InputMethodManager imm = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(mActivity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 处理评论发送事件
     */
    private void onSendClick() {
        final String content = mEtMsg.getText().toString();

        if (Utils.isEmpty(content)) {
            ToastHelper.showToast(R.string.please_input_comment);
            return;
        }

        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            LoginActivity.startActivity(getContext(), LoginActivity.TYPE_FROM_NEED_LOGIN);
            return;
        }

        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_16017);

        if (mListener != null) {
            mListener.send(content);
        }
        mEtMsg.setText(null);
        mEtMsg.setHint(R.string.simple_say);

        release();
    }

    /**
     * IEmoticonSelectedListener接口
     */
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

    @Override
    public void onStickerSelected(String categoryName, String stickerName) {

    }

    /**
     * SoftKeyboardStateListener接口
     */
    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        hideEmojiLayout();
    }

    /**
     * SoftKeyboardStateListener接口
     */
    @Override
    public void onSoftKeyboardClosed() {

    }

    /**
     * OnClickListener接口
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_tv:
                if (mListener != null) {
                    onSendClick();
                }
                break;
            case R.id.comment_et:
                hideEmojiLayout();
                //showKeyboard();
                break;
            case R.id.face_cb:
                if (isFaceShow()) {
                    hideEmojiLayout();
                    showKeyboard();
                } else {
                    hideKeyboard();
                    showEmojiLayout();
                }
                break;
            case R.id.cancel_tv:
                release();
                break;
            case R.id.blank_view:
                release();
                break;
            case R.id.comment_cb:
                isComment = !isComment;
                mAlsoCb.setChecked(isComment);
                break;
            default:
                Log.d(TAG, "default");
                break;
        }
    }

    /**
     * 设置BaseFragment
     */
    public void setActivity(BaseActivity activity) {
        mActivity = activity;
    }

    /**
     * 释放键盘状态
     */
    public void release() {
        hideEmojiLayout();
        hideKeyboard();
        mAlsoCb.setChecked(false);
        setVisibility(GONE);
        if (mListener != null) {
            mListener.onHide();
        }
    }

    public OnCommentOperationListener getOnOperationListener() {
        return mListener;
    }

    public void setOnOperationListener(OnCommentOperationListener onOperationListener) {
        mListener = onOperationListener;
    }

    public void setOnToolBoxListener(OnToolBoxListener mFaceListener) {
        this.mFaceListener = mFaceListener;
    }

    boolean isReplyOther = false;

    /**
     * 显示键盘
     */
    public void showKeyboard(String nickName, boolean alsoComment) {
        showAlsoComment(alsoComment);

        mEtMsg.setText(null);
        if (Utils.isEmpty(nickName)) {
            mEtMsg.setHint(ResourceHelper.getString(R.string.simple_say));
            isReplyOther = false;
        } else {
            isReplyOther = true;
            mEtMsg.setHint(ResourceHelper.getString(R.string.reply) + " : " + nickName);
        }

        hideEmojiLayout();
        showKeyboard();
        setVisibility(VISIBLE);
    }

    /**
     * 显示表情
     */
    public void showEmoji(boolean alsoComment) {
        showAlsoComment(alsoComment);

        mEtMsg.setText(null);

        showEmojiLayout();
        hideKeyboard();
        setVisibility(VISIBLE);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

    }
}
