package com.laka.live.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.laka.live.R;
import com.laka.live.help.recycleViewDecoration.RecyclerViewItemDecoration;
import com.laka.live.msg.CourseIncomeDetail;
import com.laka.live.msg.CourseIncomeDetailMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.ui.adapter.IncomeCourseDetailAdapter;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.IncomeCourseView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

/**
 * @ClassName: IncomeCourseDetailActivity
 * @Description: 课程收益明细
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class IncomeCourseDetailActivity extends BaseActivity implements PageListLayout.OnRequestCallBack {
    private final static String TAG = IncomeCourseDetailActivity.class.getSimpleName();

    private boolean isHeadInit = false;
    private String mCourseId;
    private int mIncomeType;

    private IncomeCourseView mIncomeCourseView;
    private PageListLayout mPageListLayout;
    private IncomeCourseDetailAdapter mAdapter;

    public static void startActivity(Context context, String courseId, int incomeType) {
        Intent intent = new Intent(context, IncomeCourseDetailActivity.class);
        intent.putExtra(Common.COURSE_ID, courseId);
        intent.putExtra(Common.INCOME_TYPE, incomeType);
        ActivityCompat.startActivity(context, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_income_course_detail);
        getWindow().setBackgroundDrawable(null);

        initIntent();
        initPageListLayout();
        mIncomeCourseView = (IncomeCourseView) findViewById(R.id.income_view);

        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setBackTextShow(false);
    }

    private void initIntent() {
        if (getIntent() == null) {
            finish();
            return;
        }

        mCourseId = getIntent().getStringExtra(Common.COURSE_ID);
        mIncomeType = getIntent().getIntExtra(Common.INCOME_TYPE, 1);

        if (Utils.isEmpty(mCourseId)) {
            finish();
        }
    }

    private void initPageListLayout() {
        mPageListLayout = (PageListLayout) findViewById(R.id.page_list_layout);
        mPageListLayout.setLayoutManager(new LinearLayoutManager(this));
        mPageListLayout.getRecyclerView().addItemDecoration(
                new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL,
                        ResourceHelper.getColor(R.color.colorE5E5E5),
                        Utils.dip2px(this, 1), 0, 0));
        mAdapter = new IncomeCourseDetailAdapter(this, mIncomeType);
        mPageListLayout.setAdapter(mAdapter);
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.loadData();
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        getCourseIncomeDetails(page);
        return null;
    }

    private void getCourseIncomeDetails(final int page) {
        DataProvider.queryCourseIncomeDetails(this, mCourseId, mIncomeType, page, new GsonHttpConnection.OnResultListener<CourseIncomeDetailMsg>() {

            @Override
            public void onSuccess(CourseIncomeDetailMsg courseIncomeDetailMsg) {
                mPageListLayout.refreshComplete();
                mPageListLayout.setOnLoadMoreComplete();

                if (courseIncomeDetailMsg == null || courseIncomeDetailMsg.getData() == null) {
                    if (mAdapter.isEmpty()) {
                        mPageListLayout.showEmpty();
                    } else {
                        mPageListLayout.showData();
                    }
                    mPageListLayout.onFinishLoading(false, false);

                    return;
                }

                CourseIncomeDetail courseIncomeDetail = courseIncomeDetailMsg.getData();
                if (!isHeadInit && courseIncomeDetail.getCourse() != null) {
                    mIncomeCourseView.updateData(courseIncomeDetail.getCourse());
                    isHeadInit = true;
                }

                if (Utils.listIsNullOrEmpty(courseIncomeDetail.getInfos())) {
                    if (mAdapter.isEmpty()) {
                        mPageListLayout.showEmpty();
                    } else {
                        mPageListLayout.showData();
                    }
                    mPageListLayout.onFinishLoading(false, false);
                    return;
                }

                if (page == 0) {
                    mAdapter.clear();
                }

                mAdapter.addAll(courseIncomeDetail.getInfos());
                mAdapter.notifyDataSetChanged();
                mPageListLayout.onFinishLoading(courseIncomeDetail.getInfos().size() >= ShoppingRequest.LIMIT, false);
                mPageListLayout.addCurrentPage();
                mPageListLayout.showData();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "getCourseIncomeDetails error : " + errorMsg);
                showToast(R.string.homepage_network_error_retry);

                mPageListLayout.refreshComplete();
                mPageListLayout.setOnLoadMoreComplete();

                if (mAdapter.isEmpty()) {
                    mPageListLayout.showNetWorkError();
                } else {
                    mPageListLayout.showData();
                }

                mPageListLayout.onFinishLoading(false, false);
            }

        });
    }
}
