package com.laka.live.account.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.fragment.FansFragment;
import com.laka.live.ui.fragment.FollowFragment;
import com.laka.live.ui.widget.emoji.StringUtil;
import com.laka.live.util.Common;

/**
 * 我的粉丝
 */
public class MyFansActivity extends BaseActivity {
    public static final String EXTRA_FAN_ID_NAME = "userId";
    public static final String EXTRA_FAN_FROM_NAME = "from";

    public static void startActivity(Activity activity) {
        startActivity(activity, AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), FollowFragment.FROM_MY);
    }

    public static void startActivity(Activity activity, String userId, String from) {
        if (activity == null || StringUtil.isEmpty(userId) || StringUtil.isEmpty(from)) {
            return;
        }
        Intent intent = new Intent(activity, MyFansActivity.class);
        intent.putExtra(EXTRA_FAN_ID_NAME, userId);
        intent.putExtra(EXTRA_FAN_FROM_NAME, from);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void startActivity(Context context, String userId, String from) {
        if (context == null || StringUtil.isEmpty(userId) || StringUtil.isEmpty(from)) {
            return;
        }
        Intent intent = new Intent(context, MyFansActivity.class);
        intent.putExtra(EXTRA_FAN_ID_NAME, userId);
        intent.putExtra(EXTRA_FAN_FROM_NAME, from);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);

        String userId = getIntent().getStringExtra(EXTRA_FAN_ID_NAME);
        String from = getIntent().getStringExtra(EXTRA_FAN_FROM_NAME);
        FansFragment fansFragment = new FansFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FollowFragment.ARGUMENT_FROM, from);
        bundle.putString(Common.ARGUMENT_USER_ID, userId);
        fansFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fansFragment).commitAllowingStateLoss();//.commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
