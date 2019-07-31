package com.laka.live.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.account.my.ContributionListActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;

import framework.ioc.InjectUtil;


/**
 * Created by luwies on 16/5/19.
 */
public class BaseFragment extends Fragment {

    private boolean isDestroyed = false;

    private boolean isResume;

    protected Activity mContext;
    protected SimpleTextDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setArguments(savedInstanceState);
        }
    }

    public void setArguments(Bundle bundle) {
        Class classes = Fragment.class;
        try {
            Field field = classes.getDeclaredField("mArguments");
            field.setAccessible(true);
            field.set(this, bundle);
        } catch (NoSuchFieldException e) {
            Log.error("test", "NoSuchFieldException : ", e);
        } catch (IllegalAccessException e) {
            Log.error("test", "IllegalAccessException : ", e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            Bundle args = getArguments();
            if (args != null) {
                outState.putAll(args);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        InjectUtil.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isResume = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveApplication.mQueue.cancelAll(this);
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean isResume() {
        return isResume;
    }

    public void dismissButtonDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

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
            dialog = new SimpleTextDialog(getContext());
            curDialog = dialog;
        } else {//新建窗口
            curDialog = new SimpleTextDialog(getContext());
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

    /**
     * onShow，tab间切换时触发，onResume则不会.所以在这里调用onResume，确保onResume可以如期调用
     */
    public void onShow() {
        onResume();
    }

    public void onHide() {
    }

    public void showButtonDialog(int titleRes, int contentRes, int yesRes, int noRes, boolean cancelAble, boolean cancelOutside, boolean isReplace, boolean recomendButtonYes, IDialogOnClickListener onClickListener
    ) {
        showButtonDialog(ResourceHelper.getString(titleRes), ResourceHelper.getString(contentRes), yesRes, noRes, cancelAble, cancelOutside, isReplace, recomendButtonYes, onClickListener);
    }

    public void showButtonDialog(int titleRes, int contentRes, int yesRes, int noRes, boolean cancelAble, boolean cancelOutside, IDialogOnClickListener onClickListener
    ) {
        showButtonDialog(titleRes, contentRes, yesRes, noRes, cancelAble, cancelOutside, false, true, onClickListener);
    }


    protected void startActivity(Class cls) {
        Intent intent = new Intent(getActivity(), cls);
        ActivityCompat.startActivity(getActivity(), intent, null);
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


    public void goLogin() {
        LoginActivity.startActivity(getActivity(), LoginActivity.TYPE_FROM_NEED_LOGIN);
    }
}
