package com.laka.live.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.income.MyIncomeActivity;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.account.my.MyCoinsActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.HomeFunction;
import com.laka.live.ui.activity.HotNewsActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.chat.ChatHomeActivity;
import com.laka.live.ui.course.MyCoursesActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.homepage.FollowNewsActivity;
import com.laka.live.ui.homepage.FunctionSessionActivity;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: HomeFunctionView
 * @Description: 首页功能的控件，可根据接口配置数量
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/5/17
 */

public class HomeFunctionView extends LinearLayout {
    private final static String TAG = HomeFunctionView.class.getSimpleName();

    private final static int TYPE_SESSION = 1;
    private final static int TYPE_URL = 2;
    private final static int TYPE_APP = 3;

    //跳转app页面
    private final static int GO_FREE_SESSION = 1;
    private final static int GO_HOT_NEWS = 2;
    private final static int GO_FOLLOW_NEWS = 3;
    private final static int GO_MY_PAY = 4;
    private final static int GO_MY_PUBLISH = 5;
    private final static int GO_MY_INCOME = 6;
    private final static int GO_MY_WALLET = 7;
    private final static int GO_LETTER = 8;
    private final static int GO_NEW_SESSION = 9;  //最新课程
    private final static int GO_HOT_SESSION = 10;  //热门课程
    private final static int GO_BEST_SESSION = 11;  //优质课程
    private final static int GO_CHEAP_SESSION = 12;  //1元课程
    private final static int GO_LIMIT_SESSION = 13;  //限时优惠

    private LinearLayout mFirstLl;
    private LinearLayout mSecondLl;

    private final static int ITEM_WIDTH = Utils.getScreenWidth(LiveApplication.getInstance()) / 4;
    private final static int MAGIN_16 = Utils.dip2px(LiveApplication.getInstance(), 16);
    private final static int MAGIN_18 = Utils.dip2px(LiveApplication.getInstance(), 18);
    private final static int MAGIN_28 = Utils.dip2px(LiveApplication.getInstance(), 28);
    private final static int MAGIN_6 = Utils.dip2px(LiveApplication.getInstance(), 6);

    public HomeFunctionView(Context context) {
        this(context, null);
    }

    public HomeFunctionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeFunctionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);

    }

    public void updateData(List<HomeFunction> dataList) {
        if (Utils.listIsNullOrEmpty(dataList)) {
            removeAllViews();
            return;
        }

        if (mFirstLl == null) {
            mFirstLl = createLinearLayout(1);
            addView(mFirstLl);
        } else {
            mFirstLl.removeAllViews();
        }

        if (dataList.size() > 4) {
            if (mSecondLl == null) {
                mSecondLl = createLinearLayout(2);
                addView(mSecondLl);
            } else {
                mSecondLl.removeAllViews();
            }

            if (mSecondLl.getParent() == null) {
                addView(mSecondLl);
            }
        } else {
            if (mSecondLl != null) {
                removeView(mSecondLl);
            }
        }

        for (int i = 0; i < dataList.size(); i++) {
            if (i < 4) {
                mFirstLl.addView(createItemView(dataList.get(i)));
            } else {
                mSecondLl.addView(createItemView(dataList.get(i)));
            }
        }
    }

    private LinearLayout createLinearLayout(int line) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(HORIZONTAL);
        LayoutParams params =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (line) {
            case 1:
                params.topMargin = MAGIN_16;
                params.bottomMargin = MAGIN_18;
                break;
            case 2:
                params.topMargin = MAGIN_6;
                params.bottomMargin = MAGIN_28;
                break;
            default:
                break;
        }

        layout.setLayoutParams(params);

        return layout;
    }

    private View createItemView(final HomeFunction homeFunction) {
        if (homeFunction == null) {
            return null;
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_function_item, null);
        LinearLayout.LayoutParams params = new LayoutParams(ITEM_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        view.setLayoutParams(params);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isEmpty(homeFunction.getValue())) {
                    return;
                }

                switch (homeFunction.getType()) {
                    case TYPE_SESSION:
                        CourseDetailActivity.startActivity(getContext(), homeFunction.getValue());
                        break;
                    case TYPE_URL:
                        WebActivity.startActivity(getContext(), homeFunction.getValue(), "");
                        break;
                    case TYPE_APP:
                        goAppActivity(Integer.parseInt(homeFunction.getValue()), homeFunction.getName());
                        break;
                    default:
                        Log.d(TAG, "unhandled click. type : " + homeFunction.getType());
                        break;
                }
            }
        });

        SimpleDraweeView iconSdv = (SimpleDraweeView) view.findViewById(R.id.icon_sdv);
        TextView name = (TextView) view.findViewById(R.id.name_tv);

        ImageUtil.loadImage(iconSdv, homeFunction.getIcon());
        name.setText(homeFunction.getName());

        return view;
    }

    private void goAppActivity(int type, String name) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(type));
        AnalyticsReport.onEvent(getContext(), AnalyticsReport.EVENT_10255, params);

        switch (type) {
            case GO_HOT_NEWS: // 热门资讯
//                AnalyticsReport.onEvent(getContext(), AnalyticsReport.EVENT_10236);
                Utils.startActivity(getContext(), HotNewsActivity.class);
                break;
            case GO_FOLLOW_NEWS: // 关注动态
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    LoginActivity.startActivity(getContext(), LoginActivity.TYPE_FROM_NEED_LOGIN);
                    return;
                }

//                AnalyticsReport.onEvent(getContext(), AnalyticsReport.EVENT_10234);
                FollowNewsActivity.startActivity(getContext());
                break;
            case GO_MY_PAY:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    LoginActivity.startActivity(getContext(), LoginActivity.TYPE_FROM_NEED_LOGIN);
                    return;
                }

//                AnalyticsReport.onEvent(getContext(), AnalyticsReport.EVENT_10233);
                Utils.startActivity(getContext(), MyCoursesActivity.class, MyCoursesActivity.SUBSCRIBELIVE);
                break;
            case GO_MY_PUBLISH:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    LoginActivity.startActivity(getContext(), LoginActivity.TYPE_FROM_NEED_LOGIN);
                    return;
                }

                Utils.startActivity(getContext(), MyCoursesActivity.class, MyCoursesActivity.MYLIVE);
                break;
            case GO_MY_INCOME:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    LoginActivity.startActivity(getContext(), LoginActivity.TYPE_FROM_NEED_LOGIN);
                    return;
                }

                MyIncomeActivity.startActivity(getContext());
                break;
            case GO_MY_WALLET:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    LoginActivity.startActivity(getContext(), LoginActivity.TYPE_FROM_NEED_LOGIN);
                    return;
                }

                MyCoinsActivity.startActivity(getContext());
                break;
            case GO_LETTER:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    LoginActivity.startActivity(getContext(), LoginActivity.TYPE_FROM_NEED_LOGIN);
                    return;
                }

                ChatHomeActivity.startActivity(getContext());

                break;
            case GO_NEW_SESSION:
            case GO_HOT_SESSION:
            case GO_BEST_SESSION:
            case GO_CHEAP_SESSION:
            case GO_LIMIT_SESSION:
            case GO_FREE_SESSION:
                FunctionSessionActivity.startActivity(getContext(), type, name);
                break;
            default:
                Log.d(TAG, "unhandled go app type : " + type);
                break;
        }
    }

}
