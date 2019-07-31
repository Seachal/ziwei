package com.laka.live.ui.course;

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
import com.laka.live.ui.fragment.BaseFragment;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/4/1.
 */
public class SubscribeFragment extends BaseFragment implements PageListLayout.OnRequestCallBack
        , BaseAdapter.OnItemClickListener {

    public static final String FROM_MY = "my";

    public static final String FROM_USER_INFO = "user_info";

    public static final String ARGUMENT_FROM = "from";

    private String mUserId;

    private String mCourseId;

    private UserListAdapter mAdapter;

    private String mFrom;

    private PageListLayout mListLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        mUserId = bundle.getString(Common.ARGUMENT_USER_ID);
        mFrom = bundle.getString(ARGUMENT_FROM);
        mCourseId = bundle.getString("course_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListLayout = (PageListLayout) inflater.inflate(R.layout.page_layout, container, false);
        Context context = getContext();

        String from = "";
        int tipId = R.string.other_follow_null_tip;
        if (TextUtils.equals(mFrom, FROM_MY)) {
            from = UserListCourseAdapter.FROM_MY_FOLLOW;
            tipId = R.string.my_follow_null_tip;
        } else if (TextUtils.equals(mFrom, FROM_USER_INFO)) {
            from = UserListCourseAdapter.FROM_USER_INFO_FOLLOW;
            tipId = R.string.other_follow_null_tip;
        }

        mListLayout.setIsReloadWhenEmpty(true);
        mAdapter = new UserListAdapter((BaseActivity) context, from);
        mAdapter.setOnItemClickListener(this);
        mListLayout.setAdapter(mAdapter);
        mListLayout.setLayoutManager(new LinearLayoutManager(context));
        mListLayout.setOnRequestCallBack(this);
        mListLayout.loadData();
        return mListLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.querySubscribe(getActivity(), mUserId, page,mCourseId, listener);
    }

    @Override
    public void onItemClick(int position) {
        ListUserInfo userInfo = mAdapter.getItem(position);
        UserInfoActivity.startActivity(getActivity(), String.valueOf(userInfo.getId()));
    }


}
