package com.laka.live.shopping.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.view.KeyEvent;
import android.view.View;

import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.AppEnv;
import com.laka.live.shopping.framework.DeviceManager;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.Log;

import java.util.ArrayList;

/**
 * Created by Lyf on 2017/7/20.
 */
public class BaseShopActivity extends BaseActivity implements IDefaultWindowCallBacks {

    private boolean isFirstTime = true;
    protected AbstractWindow abstractWindow;
    private static int BASE_SHOP_ACTIVITY_COUNT = 0; // 统计当前有多少个BaseShop页面，当前页面计数为0时，释放AppEnv。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ++BASE_SHOP_ACTIVITY_COUNT;
        AppEnv.init(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        abstractWindow = (AbstractWindow) view;
        abstractWindow.onPublicWindowStateChange(AbstractWindow.STATE_AFTER_PUSH_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEnv.init(this);

        if (isFirstTime) {
            isFirstTime = false;
            return;
        }
        // 注意：第一次打开页面，不执行onResume,与STATE_ON_SHOW事件一致.
        abstractWindow.onPublicWindowStateChange(AbstractWindow.STATE_ON_SHOW);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        abstractWindow.onPublicWindowStateChange(AbstractWindow.STATE_ON_DETACH);
        --BASE_SHOP_ACTIVITY_COUNT;
        if (BASE_SHOP_ACTIVITY_COUNT == 0) {
            AppEnv.getInstance().destroy();
        }

    }

    // 左上角的返回键的点击事件
    @Override
    public void onTitleBarBackClicked(AbstractWindow window) {
        finish();
    }

    @Override
    public void onWindowExitEvent(AbstractWindow window, boolean withAnimation) {

    }

    @Override
    public void onWindowStateChange(AbstractWindow window, int stateFlag) {

    }

    @Override
    public boolean onWindowKeyEvent(AbstractWindow window, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public View onGetViewBehind(View view) {
        return null;
    }

    @Override
    public ArrayList<Integer> messages() {
        return null;
    }

    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    public Object handleMessageSync(Message msg) {
        return null;
    }


    public void showDialog(String msg) {
        showNewDialog(msg);
    }

    public void dismiss() {
        dismissDialog();
    }

}
