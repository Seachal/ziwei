package com.laka.live.ui.widget.room;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.config.SystemConfig;
import com.laka.live.control.QavsdkControl;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.Common;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;


/**
 * Created by ios on 16/6/28.
 */
public class ZhuboMorePanel extends BasePanel implements View.OnClickListener {
    private static final String TAG = "ZhuboMorePanel";
    private View parentView;
    private BaseActivity mActivity;
    private ImageView btnCamera, btnFilter, btnBeauty, btnFlash, btnMic;

    private boolean isOpenFlashLight;
    private boolean isOpenMic = true;

    private boolean isPortrait = true;

    public ZhuboMorePanel(Context context, BaseActivity activity) {
        super(context);
        this.mActivity = activity;
        setAlpha(0);
//        setAnimation(R.style.LiveRoomPanelAnim);
        initView();
    }

    public ZhuboMorePanel(Context context, boolean isPortrait, BaseActivity activity) {
        super(context, false);
        this.mActivity = activity;
        this.isPortrait = isPortrait;

        initPanel();

        setAlpha(0);
//        setAnimation(R.style.LiveRoomPanelAnim);
        initView();


    }

    private void initView() {
        btnMic = (ImageView) parentView.findViewById(R.id.btn_mic);
        btnMic.setOnClickListener(this);
        btnCamera = (ImageView) parentView.findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(this);
        btnFilter = (ImageView) parentView.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(this);
        btnFilter.setVisibility(View.GONE);
        btnBeauty = (ImageView) parentView.findViewById(R.id.btn_beauty);
        btnBeauty.setOnClickListener(this);

        btnFlash = (ImageView) parentView.findViewById(R.id.btn_flashlight);
        btnFlash.setOnClickListener(this);
        parentView.findViewById(R.id.transparent_view).setOnClickListener(this);
    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.panel_zhubo_more, null);
        return parentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.WRAP_CONTENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        Log.d(TAG, " width=" + width + " height=" + height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        if (isPortrait) {
            lp.gravity = Gravity.BOTTOM | Gravity.END;
            lp.rightMargin = Utils.dip2px(mContext, 10);
        } else {
            QavsdkControl mQavsdkControl = LiveApplication.getInstance().getQavsdkControl();
            if (mQavsdkControl != null && mQavsdkControl.getIsFrontCamera()) {
                com.laka.live.util.Log.d(TAG," 前置");
                mContentView.setRotation(90F);
                lp.gravity = Gravity.BOTTOM | Gravity.START;
                lp.leftMargin = Utils.dip2px(mContext, 77);
                lp.bottomMargin = Utils.dip2px(mContext, -63);
            }else{
                com.laka.live.util.Log.d(TAG," 后置");
                mContentView.setRotation(90F);
                lp.gravity = Gravity.BOTTOM | Gravity.START;
                lp.leftMargin = Utils.dip2px(mContext, 65);
                lp.bottomMargin = Utils.dip2px(mContext, -52);
            }


            setAnimation(R.style.SlideLeft2RightAnim);
        }

        return lp;
    }

    @Override
    public void showPanel() {
        super.showPanel();
        QavsdkControl mQavsdkControl = LiveApplication.getInstance().getQavsdkControl();
        if (mQavsdkControl != null && mQavsdkControl.getIsFrontCamera()) {
            btnFlash.setVisibility(View.GONE);
        } else {
            btnFlash.setVisibility(View.VISIBLE);
//            btnBeauty.setVisibility(View.GONE);

            if (isOpenFlashLight) {
//                btnFlash.setText(R.string.close_flash_flight);
//                Drawable drawable = ResourceHelper.getDrawable(R.drawable.live_btn_noflashlight_selector);
//                int width = Utils.dip2px(mActivity, 28f);
//                drawable.setBounds(0, 0, width, width);
//                btnFlash.setCompoundDrawables(drawable, null, null, null);
                btnFlash.setImageResource(R.drawable.live_btn_noflashlight_selector);
            } else {
                btnFlash.setImageResource(R.drawable.live_more_btn_flashlight_selector);
//                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(btnFlash, R.drawable.live_more_btn_flashlight_selector, 0, 0, 0);
//                btnFlash.setText(R.string.open_flash_flight);
            }
        }

        checkShowBtnBeauty();


        isOpenMic = mQavsdkControl.isOpenMic();
        if (isOpenMic) {
            btnMic.setImageResource(R.drawable.live_btn_mic_selector);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(btnMic, R.drawable.live_btn_mic_selector, 0, 0, 0);
        } else {
            btnMic.setImageResource(R.drawable.live_btn_no_mic_selector);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(btnMic, R.drawable.live_btn_no_mic_selector, 0, 0, 0);
        }
    }

    private void checkShowBtnBeauty() {
        QavsdkControl mQavsdkControl = LiveApplication.getInstance().getQavsdkControl();
//        if(!mQavsdkControl.getIsFrontCamera()){
//            btnBeauty.setVisibility(View.GONE);//后置摄像头不设置美颜
//            return;
//        }

        if (!SystemConfig.getInstance().isShowFaceBeauty()) {
            btnBeauty.setVisibility(View.GONE);
        } else {
            btnBeauty.setVisibility(View.VISIBLE);
            refreshBtnBeauty();
        }
    }

    private void refreshBtnBeauty() {
        boolean isEnableBeauty = UiPreference.getBoolean(Common.KEY_IS_ENABLE_BEAUTY, true);
        if (isEnableBeauty) {
            btnBeauty.setImageResource(R.drawable.live_btn_beauty_selector);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(btnBeauty, R.drawable.live_begin_btn_beauty_selector, 0, 0, 0);
//            btnBeauty.setText(R.string.close_beauty_filter);
        } else {
            btnBeauty.setImageResource(R.drawable.live_btn_beauty_no_selector);
//            btnBeauty.setImageResource(R.drawable.live_btn_nobeauty_selector);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(btnBeauty, R.drawable.live_btn_nobeauty_selector, 0, 0, 0);
//            btnBeauty.setText(R.string.open_beauty_filter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mic:
                hidePanel();
                EventBusManager.postEvent(0, SubcriberTag.LIVE_OPEN_MIC);
                break;
            case R.id.btn_camera:
                hidePanel();
                toggleBtnMic();
                EventBusManager.postEvent(0, SubcriberTag.LIVE_SWITCH_CAMERA);
                break;
            case R.id.btn_filter:
                hidePanel();
                EventBusManager.postEvent(0, SubcriberTag.SHOW_FILTER_PANEL);
                break;
            case R.id.btn_beauty:
                hidePanel();
                EventBusManager.postEvent(0, SubcriberTag.SHOW_BEAUTY_PANEL);
                break;
            case R.id.btn_flashlight:
                hidePanel();
                toggleFlashLight();
                EventBusManager.postEvent(0, SubcriberTag.FLASH_LIGHT_ONOFF);
                break;
            case R.id.transparent_view:
                hidePanel();
                break;

        }

    }

    private void toggleBtnMic() {
        isOpenMic = !isOpenMic;
        if (isOpenMic) {
            btnMic.setImageResource(R.drawable.live_btn_mic_selector);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(btnMic, R.drawable.live_btn_mic_selector, 0, 0, 0);
        } else {
            btnMic.setImageResource(R.drawable.live_btn_no_mic_selector);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(btnMic, R.drawable.live_btn_no_mic_selector, 0, 0, 0);
        }

    }

    private void toggleFlashLight() {
        isOpenFlashLight = isOpenFlashLight == false;
    }

    @Override
    public void hidePanel() {
        super.hidePanel();
        EventBusManager.postEvent(0, SubcriberTag.CLOSE_ZHUBO_MORE_PANEL);
    }
}
