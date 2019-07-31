package com.laka.live.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.UserListAdapter;
import com.laka.live.ui.adapter.UserListCourseAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/4/1.
 */
public class FansFragment extends BaseFragment implements PageListLayout.OnRequestCallBack,
        BaseAdapter.OnItemClickListener {

    private String mUserId;
    private UserListAdapter mAdapter;
    private String mFrom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mUserId = bundle.getString(Common.ARGUMENT_USER_ID);
        mFrom = bundle.getString(FollowFragment.ARGUMENT_FROM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PageListLayout listLayout = (PageListLayout) inflater.inflate(R.layout.page_layout, container, false);
        Context context = getContext();
        int tipId = R.string.other_fans_null_tip;
        String from = "";
        if (TextUtils.equals(mFrom, FollowFragment.FROM_MY)) {
            from = UserListCourseAdapter.FROM_MY_FANS;
            tipId = R.string.my_fans_null_tip;
        } else if (TextUtils.equals(mFrom, FollowFragment.FROM_USER_INFO)) {
            from = UserListCourseAdapter.FROM_USER_INFO_FANS;
            tipId = R.string.other_fans_null_tip;
        }
        mAdapter = new UserListAdapter((BaseActivity) context, from);
        mAdapter.setOnItemClickListener(this);
        listLayout.setAdapter(mAdapter);
        listLayout.setLayoutManager(new LinearLayoutManager(context));
        listLayout.setOnRequestCallBack(this);
        listLayout.loadData();
        return listLayout;
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.queryFan(getActivity(), mUserId, page, listener);
    }

    @Override
    public void onItemClick(int position) {
        ListUserInfo userInfo = mAdapter.getItem(position);
        UserInfoActivity.startActivity(getActivity(), userInfo);
    }

}
