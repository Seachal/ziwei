package com.laka.live.account.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.MsgRemindConfig;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.Common;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

public class MsgRemindActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private SwitchButton mNoDisturbingBtn;
    private SwitchButton mFollowLiveStartRemindBtn;
    private SwitchButton mSbFollowMe;
    private SwitchButton mSystemMsgRemindBtn;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MsgRemindActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_remind);
        init();
    }


    private void init() {
        mNoDisturbingBtn = initItem(R.id.no_disturbing, getString(R.string.no_disturbing_in_night));
        mFollowLiveStartRemindBtn = initItem(R.id.follow_live_start_remind, getString(R.string.my_follow_live_start_remind));
        mSbFollowMe = initItem(R.id.sb_follow_me, getString(R.string.sb_follow_me));
        mSystemMsgRemindBtn = initItem(R.id.msg_remind, getString(R.string.msg_remind));

        mNoDisturbingBtn.setChecked(AccountInfoManager.getInstance().getCurrentAccountRemindInNightState());
        mFollowLiveStartRemindBtn.setChecked(AccountInfoManager.getInstance().getCurrentAccountRemindFollowLiveState());
        mSbFollowMe.setChecked(AccountInfoManager.getInstance().getCurrentAccountRemindFollowMeState());
        mSystemMsgRemindBtn.setChecked(AccountInfoManager.getInstance().getCurrentAccountRemindMessageInComeState());

        mNoDisturbingBtn.setOnCheckedChangeListener(this);
        mFollowLiveStartRemindBtn.setOnCheckedChangeListener(this);
        mSbFollowMe.setOnCheckedChangeListener(this);
        mSystemMsgRemindBtn.setOnCheckedChangeListener(this);
    }

    private SwitchButton initItem(int id, CharSequence text) {
        View view = findViewById(id);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);

        SwitchButton switchButton = (SwitchButton) view.findViewById(R.id.slide_switch);
        return switchButton;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        showLoadingDialog(true);
        if (buttonView == mNoDisturbingBtn) {
            if (isChecked) {
                JPushInterface.stopPush(LiveApplication.getInstance());
            } else {
                JPushInterface.resumePush(LiveApplication.getInstance());
            }
            setUserRemind(this, Common.DONOT, isChecked ? MsgRemindConfig.OPEN : MsgRemindConfig.CLOSE, new GsonHttpConnection.OnResultListener<Msg>() {
                @Override
                public void onSuccess(Msg msg) {
                    dismissLoadingsDialog();
                    AccountInfoManager.getInstance().updateCurrentAccountRemindInNightState(isChecked);
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    dismissLoadingsDialog();
                }
            });
        } else if (buttonView == mFollowLiveStartRemindBtn) {
            setUserRemind(this, Common.FOLLOW_ROOM, isChecked ? MsgRemindConfig.OPEN : MsgRemindConfig.CLOSE, new GsonHttpConnection.OnResultListener<Msg>() {
                @Override
                public void onSuccess(Msg msg) {
                    dismissLoadingsDialog();
                    AccountInfoManager.getInstance().updateCurrentAccountRemindFollowLiveState(isChecked);
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    dismissLoadingsDialog();
                }
            });
        } else if (buttonView == mSbFollowMe) {
            setUserRemind(this, Common.FOLLOW_ME, isChecked ? MsgRemindConfig.OPEN : MsgRemindConfig.CLOSE, new GsonHttpConnection.OnResultListener<Msg>() {
                @Override
                public void onSuccess(Msg msg) {
                    dismissLoadingsDialog();
                    AccountInfoManager.getInstance().updateCurrentAccountRemindFollowMeState(isChecked);
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    dismissLoadingsDialog();
                }
            });
        } else if (buttonView == mSystemMsgRemindBtn) {
            setUserRemind(this, Common.MESSAGE, isChecked ? MsgRemindConfig.OPEN : MsgRemindConfig.CLOSE, new GsonHttpConnection.OnResultListener<Msg>() {
                @Override
                public void onSuccess(Msg msg) {
                    dismissLoadingsDialog();
                    AccountInfoManager.getInstance().updateCurrentAccountRemindMessageInComeState(isChecked);
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    dismissLoadingsDialog();
                }
            });
        }
    }

    public static void setUserRemind(Object tag, String key, int value, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        DataProvider.addToken(params);
        params.put(key, String.valueOf(value));
        GsonHttpConnection.getInstance().post(tag, Common.SET_USER_REMIND_URL, params, Msg.class, listener);
    }
}
