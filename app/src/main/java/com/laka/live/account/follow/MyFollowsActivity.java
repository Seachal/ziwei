package com.laka.live.account.follow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.fragment.FollowFragment;
import com.laka.live.ui.widget.emoji.StringUtil;
import com.laka.live.util.Common;

/**
 * 关注
 */
public class MyFollowsActivity extends BaseActivity {

    public static final String EXTRA_ID_NAME = "userId";
    public static final String EXTRA_FROM_NAME = "from";

    public static void startActivity(Activity activity) {
        startActivity(activity, AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), FollowFragment.FROM_MY);
    }

    public static void startActivity(Activity activity, String userId, String from) {
        if (activity == null || StringUtil.isEmpty(userId) || StringUtil.isEmpty(from)) {
            return;
        }
        Intent intent = new Intent(activity, MyFollowsActivity.class);
        intent.putExtra(EXTRA_ID_NAME, userId);
        intent.putExtra(EXTRA_FROM_NAME, from);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void startActivity(Context context, String userId, String from) {
        if (context == null || StringUtil.isEmpty(userId) || StringUtil.isEmpty(from)) {
            return;
        }
        Intent intent = new Intent(context, MyFollowsActivity.class);
        intent.putExtra(EXTRA_ID_NAME, userId);
        intent.putExtra(EXTRA_FROM_NAME, from);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follows);
        init();
    }

    private void init() {
        String userId = getIntent().getStringExtra(EXTRA_ID_NAME);
        String from = getIntent().getStringExtra(EXTRA_FROM_NAME);
        FollowFragment followFragment = new FollowFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Common.ARGUMENT_USER_ID, userId);
        bundle.putString(FollowFragment.ARGUMENT_FROM, from);
        followFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, followFragment).commitAllowingStateLoss();//.commit()
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
