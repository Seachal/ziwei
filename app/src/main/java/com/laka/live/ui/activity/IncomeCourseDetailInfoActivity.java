package com.laka.live.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.IncomeCourseInfoInfo;
import com.laka.live.msg.CourseIncomeDetailInfoMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.util.Date;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/10/2017
 */

public class IncomeCourseDetailInfoActivity extends BaseActivity {
    private final static String TAG = IncomeCourseDetailInfoActivity.class.getSimpleName();

    private LoadingLayout mLoadingLayout;

    private TextView mIncomeTypeTv;
    private TextView mIncomeTv;
    private TextView mBuyerUserTv;
    private TextView mBuyTimeTv;
    private TextView mUserPayTv;
    private TextView mCourseAnchorTv;
    private TextView mAnchorIncomeTv;

    private int mId, mIncomeType;

    public static void startActivity(Context context, int id, int incomeType) {
        Intent intent = new Intent(context, IncomeCourseDetailInfoActivity.class);
        intent.putExtra(Common.ID, id);
        intent.putExtra(Common.INCOME_TYPE, incomeType);

        ContextCompat.startActivity(context, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_course_detail_info);
        getWindow().setBackgroundDrawable(null);

        initIntent();
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }

    private void initUI() {
        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setBackTextShow(false);

        mLoadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);
        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
            @Override
            public void onClick() {
                getData();
            }
        });

        mIncomeTypeTv = (TextView) findViewById(R.id.income_type_tv);
        mIncomeTv = (TextView) findViewById(R.id.income_tv);
        mBuyerUserTv = (TextView) findViewById(R.id.buy_user_tv);
        mBuyTimeTv = (TextView) findViewById(R.id.buy_time_tv);
        mUserPayTv = (TextView) findViewById(R.id.user_pay_tv);
        mCourseAnchorTv = (TextView) findViewById(R.id.course_anchor_tv);
        mAnchorIncomeTv = (TextView) findViewById(R.id.anchor_income_tv);
    }

    private void initIntent() {
        if (getIntent() == null) {
            finish();
            return;
        }

        mId = getIntent().getIntExtra(Common.ID, -1);
        mIncomeType = getIntent().getIntExtra(Common.INCOME_TYPE, 1);

        if (mId <= 0) {
            finish();
        }
    }

    private void getData() {
        mLoadingLayout.setDefaultLoading();

        DataProvider.queryCourseIncomeDetailInfo(this, mId, mIncomeType,
                new GsonHttpConnection.OnResultListener<CourseIncomeDetailInfoMsg>() {

                    @Override
                    public void onSuccess(CourseIncomeDetailInfoMsg courseIncomeDetailInfoMsg) {

                        if (courseIncomeDetailInfoMsg == null) {
                            mLoadingLayout.setDefaultNoData();
                            return;
                        }

                        IncomeCourseInfoInfo incomeDetail = courseIncomeDetailInfoMsg.getData();
                        if (incomeDetail == null) {
                            mLoadingLayout.setDefaultNoData();
                            return;
                        }

                        mLoadingLayout.hide();

                        mIncomeTypeTv.setText(incomeDetail.getIncomeTypeStr());
                        mIncomeTv.setText(Utils.float2String(incomeDetail.getIncome()));
                        mBuyerUserTv.setText(incomeDetail.getBuyerName());
                        mBuyTimeTv.setText(Utils.LONG_DATE_FORMATER.format(new Date(incomeDetail.getBuyerTime() * 1000)));
                        mUserPayTv.setText(ResourceHelper.getString(R.string.income_money, incomeDetail.getBuyerPaid()));
                        mCourseAnchorTv.setText(incomeDetail.getAnchorName());
                        mAnchorIncomeTv.setText(ResourceHelper.getString(R.string.income_money, Utils.float2String(incomeDetail.getAnchorIncome())));
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        mLoadingLayout.setDefaultNetworkError(true);
                        ToastHelper.showToast(R.string.shopping_network_error_retry);
                    }

                });
    }
}
