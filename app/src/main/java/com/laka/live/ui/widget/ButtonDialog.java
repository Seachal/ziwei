
package com.laka.live.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.Utils;

/**
 * 带按钮（确定、取消）的对话框
 * 
 * @author xiaoyulong
 * @version 创建时间：2015-4-29 下午3:16:04
 */
public class ButtonDialog extends Dialog {

    protected View.OnClickListener mNeutralButtonClickListener = null;

    protected View.OnClickListener mPositiveButtonClickListener = null;

    protected View.OnClickListener mCloseButtonClickListener = null;

    private Button mOk;

    private Button mCancel;

    private TextView mContent, mTitle;

    private ImageView mCloseImg;

    public ButtonDialog(Context context) {
        super(context, R.style.mydialog);
        this.setContentView(R.layout.dialog_button_content);
        mOk = (Button) findViewById(R.id.sure_but);
        mCancel = (Button) findViewById(R.id.cancle_but);
        mContent = (TextView) findViewById(R.id.dialog_content);
        mOk.setOnClickListener(mOKClickListener);
        mCancel.setOnClickListener(mCancelClickListener);
//        mTitle = (TextView) findViewById(R.id.dialog_common_title);
//        mCloseImg = (ImageView) findViewById(R.id.dialog_common_cancel_img);
//        mCloseImg.setOnClickListener(mCloseClickListener);
        WindowManager.LayoutParams p = this.getWindow().getAttributes();
//        int screenWidth = LiveApplication.screenWidth;
//        p.width = (int) (screenWidth * 0.8); // 宽度占屏幕80%，高度自适应
        p.width = Utils.dip2px(context,260);
        this.getWindow().setAttributes(p);
    }

    /**
     * 显示单个按钮（隐藏了取消按钮）
     */
    public void setShowSingleBtn() {
        mCancel.setVisibility(View.GONE);
    }

    /**
     * 设置确定按钮文字（默认为“确定”）
     * 
     * @param text
     */
    public void setPositiveButtonText(String text) {
        mOk.setText(text);
        if (TextUtils.isEmpty(text)) {
            mOk.setVisibility(View.GONE);
        } else {
            mOk.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置确定按钮文字（默认为“确定”）
     * 
     * @param textId
     */
    public void setPositiveButtonText(int textId) {
        setPositiveButtonText(getContext().getString(textId));

    }

    /**
     * 设置取消按钮文字（默认为“取消”）
     * 
     * @param text
     */
    public void setNeutralButtonText(String text) {
        mCancel.setText(text);
        if (TextUtils.isEmpty(text)) {
            mCancel.setVisibility(View.GONE);
        } else {
            mCancel.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置取消按钮文字（默认为“取消”）
     * 
     * @param textId
     */
    public void setNeutralButtonText(int textId) {
        mCancel.setText(textId);
    }

    /**
     * 设置确定按钮点击事件
     * 
     * @param listener
     */
    public void setPositiveButtonClickListener(View.OnClickListener listener) {
        mPositiveButtonClickListener = listener;
    }

    /**
     * 设置取消按钮点击事件
     * 
     * @param listener
     */
    public void setNeutralButtonClickListener(View.OnClickListener listener) {
        mNeutralButtonClickListener = listener;
    }

    /**
     * 设置右上角关闭按钮点击事件（默认出发为关闭）
     * 
     * @param listener
     */
    public void setCloseButtonClickListener(View.OnClickListener listener) {
        mCloseButtonClickListener = listener;
    }

    /**
     * 设置显示内容
     * 
     * @param content
     */
    public void setContent(String content) {
        mContent.setText(content);
    }

    /**
     * 设置显示内容
     * 
     * @param contentId
     */
    public void setContent(int contentId) {
        mContent.setText(contentId);
    }

    /**
     * 设置显示内容
     * 
     * @param style
     */
    public void setContent(SpannableStringBuilder style) {
        mContent.setText(style);
    }

    /**
     * 设置标题
     * 
     * @param title
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * 设置标题
     * 
     * @param titleId
     */
    public void setTitleId(int titleId) {
        mTitle.setText(titleId);
    }

    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mNeutralButtonClickListener != null) {
                mNeutralButtonClickListener.onClick(v);
            }
            dismiss();
        }
    };

    private View.OnClickListener mOKClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mPositiveButtonClickListener != null) {
                mPositiveButtonClickListener.onClick(v);
            }
            dismiss();
        }
    };

    private View.OnClickListener mCloseClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mCloseButtonClickListener != null) {
                mCloseButtonClickListener.onClick(v);
            }
            dismiss();
        }
    };

    // 物理返回键是否有效
    private boolean backBtnDisable = false;

    /**
     * 设置右上角叉不可见
     * 
     */
    public void setCloseBtnDisable(boolean b){
        if(b){
            mCloseImg.setVisibility(View.GONE);
        }else{
            mCloseImg.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 设置物理返回按钮是否有效：true屏蔽返回键，false有效关闭弹窗 （默认为false）
     * 
     * @param b
     */
    public void setBackBtnDisable(boolean b) {
        backBtnDisable = b;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (backBtnDisable) {
                // 屏蔽物理返回键
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setRightTextColor(int color) {
        mCancel.setTextColor(color);
    }

    public void setLeftTextColor(int color) {
        mOk.setTextColor(color);
    }
}
