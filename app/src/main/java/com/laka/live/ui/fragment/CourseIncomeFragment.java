package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.bean.CourseIncomeSum;
import com.laka.live.msg.CourseIncomeSumMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.widget.CourseIncomeHeader;
import com.laka.live.ui.adapter.CourseIncomeAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Log;

/**
 * @ClassName: CourseIncomeFragment
 * @Description: 课程收益
 * @Author: chuan
 * @Version: 1.0
 * @Date: 25/07/2017
 */

public class CourseIncomeFragment extends BaseFragment implements PageListLayout.OnRequestCallBack {
    private final static String TAG = CourseIncomeFragment.class.getSimpleName();

    private PageListLayout mPageListLayout;
    private CourseIncomeHeader mHeadView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transo_detail, null);

        mHeadView = new CourseIncomeHeader(getContext());

        mPageListLayout = (PageListLayout) view.findViewById(R.id.page_list_layout);
        mPageListLayout.addHeaderView(mHeadView);
        mPageListLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        CourseIncomeAdapter adapter = new CourseIncomeAdapter(getContext());
        mPageListLayout.setAdapter(adapter);
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.loadData();
        return view;
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        if (page == 0) {
            getIncomeSum();
        }
        return DataProvider.queryCourseIncomesListMsg(this, page, listener);
    }

    private void getIncomeSum() {
        DataProvider.queryCourseIncomeSum(this, new GsonHttpConnection.OnResultListener<CourseIncomeSumMsg>() {
            @Override
            public void onSuccess(CourseIncomeSumMsg courseIncomeSumMsg) {
                if (courseIncomeSumMsg == null || mHeadView == null || courseIncomeSumMsg.getData() == null) {
                    return;
                }

                CourseIncomeSum incomeSum = courseIncomeSumMsg.getData();
                mHeadView.updateData(incomeSum.getTotal(), incomeSum.getLive(), incomeSum.getVideo(), incomeSum.getAgent());
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "queryCourseIncomeSum fail . errorMsg : " + errorMsg);
            }
        });
    }
}
