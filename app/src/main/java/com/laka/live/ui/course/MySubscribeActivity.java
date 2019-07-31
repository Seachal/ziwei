package com.laka.live.ui.course;

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

import framework.ioc.annotation.InjectExtra;

public class MySubscribeActivity extends BaseActivity {

    public static final String EXTRA_ID_NAME = "userId";
    public static final String EXTRA_FROM_NAME = "from";

    public static void startActivity(Activity activity,String course_id) {
        startActivity(activity, AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), FollowFragment.FROM_MY,course_id);
    }

    public static void startActivity(Activity activity, String userId, String from,String course_id) {
        if (activity == null || StringUtil.isEmpty(userId) || StringUtil.isEmpty(from)) {
            return;
        }
        Intent intent = new Intent(activity, MySubscribeActivity.class);
        intent.putExtra("course_id",course_id);
        intent.putExtra(EXTRA_ID_NAME, userId);
        intent.putExtra(EXTRA_FROM_NAME, from);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void startActivity(Context context, String userId, String from) {
        if (context == null || StringUtil.isEmpty(userId) || StringUtil.isEmpty(from)) {
            return;
        }
        Intent intent = new Intent(context, MySubscribeActivity.class);
        intent.putExtra(EXTRA_ID_NAME, userId);
        intent.putExtra(EXTRA_FROM_NAME, from);
        context.startActivity(intent);
    }

    // 课程id
    @InjectExtra(name = "course_id")
    public String course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follows);
        init();
    }

    private void init() {
        String userId = getIntent().getStringExtra(EXTRA_ID_NAME);
        String from = getIntent().getStringExtra(EXTRA_FROM_NAME);
        SubscribeFragment subscribeFragment = new SubscribeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("course_id", String.valueOf(course_id));
        bundle.putString(Common.ARGUMENT_USER_ID, userId);
        bundle.putString(FollowFragment.ARGUMENT_FROM, from);
        subscribeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, subscribeFragment).commitAllowingStateLoss();//.commit()
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
