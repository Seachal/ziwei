package com.laka.live.ui.room.roommanagerlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.PostEvent;
import com.laka.live.manager.RequestType;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.room.ResponseCodeHelper;
import com.laka.live.ui.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/6/7.
 * Email-1501448275@qq.com
 */
public class LiveManagerListActivity extends BaseActivity {
    private static final int MAX_MANAGER_COUNT = 5;
    private RecyclerView mRecyclerView;
    private LoadingLayout mLoadingLayout;
    private TextView mManagerCountView;
    private LiveManagerListAdapter mAdapter;
    private List<UserInfo> mManagersList = new ArrayList<>();
    private LiveManagerRequest mRequest;
    private LiveManagerListRequest mManagerListRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_room_manager_list_activity_layout);
        initView();
        initRequest();
        tryGetManagerListData();
    }

    public static void startActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, LiveManagerListActivity.class);
        activity.startActivity(intent);
    }

    private void initRequest() {
        mRequest = new LiveManagerRequest(this, new LiveManagerRequest.OnRequestCallback() {
            @Override
            public void onSuccess(int requestType, Object object) {
                if (requestType == RequestType.REQUEST_TYPE_REMOVE_ADMIN) {
                    handleOnCancelManagerSuccess(object);
                } else if (requestType == RequestType.REQUEST_TYPE_ADD_ADMIN) {
                    handleOnAddManagerSuccess(object);
                }
            }

            @Override
            public void onFailed(int errorCode, String errorTip) {
                ResponseCodeHelper.showTips(LiveManagerListActivity.this, errorCode);
            }
        });
        mManagerListRequest = new LiveManagerListRequest();
    }


    private void initView() {
        mManagerCountView = (TextView) findViewById(R.id.live_manager_count);
        mRecyclerView = (RecyclerView) findViewById(R.id.live_manager_list_recycler_view);
        mLoadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);
        mLoadingLayout.setDefaultLoading();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new LiveManagerListAdapter(this, mManagersList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.setOnCancelManagerListener(new LiveManagerListAdapter.OnCancelManagerListener() {
            @Override
            public void refresh(UserInfo userInfo) {
                if (userInfo.isAdministrator()) {
                    handleOnTryCancelManager(userInfo);
                } else {
                    handleOnTryAddManager(userInfo);
                }
            }
        });
    }

    private void handleOnTryCancelManager(UserInfo userInfo) {
        mRequest.tryRemoveAdminRequest(userInfo.getIdStr());
    }

    private void handleOnTryAddManager(UserInfo userInfo) {
        mRequest.tryAddAdminRequest(userInfo.getIdStr());
    }

    private void handleOnAddManagerSuccess(Object object) {
        if (object == null) {
            object = mRequest.getCurrentId();
        }
        if (object == null) {
            return;
        }
        UserInfo tempUser = null;
        for (UserInfo userInfo : mManagersList) {
            if (userInfo.getIdStr().equals((String) object)) {
                tempUser = userInfo;
                break;
            }
        }
        if (tempUser != null) {
            tempUser.setSadmin(true);
            LiveRoomAdminManager.getInstance().addAdministrator(tempUser);
            notifyListDataChange(tempUser);
        }
        updateManagerCount();
    }

    private void notifyListDataChange(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        for (UserInfo userInfo1 : mManagersList) {
            if (userInfo1.getIdStr().equals(userInfo.getIdStr())) {
                userInfo1.setSadmin(userInfo.isAdministrator());
                break;
            }
        }
        if (mAdapter == null) {
            return;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void handleOnCancelManagerSuccess(Object object) {
        if (object == null) {
            object = mRequest.getCurrentId();
        }
        if (object == null) {
            return;
        }
        UserInfo tempUser = null;
        for (UserInfo userInfo : mManagersList) {
            if (userInfo.getIdStr().equals((String) object)) {
                tempUser = userInfo;
                break;
            }
        }
        if (tempUser != null) {
//            mManagersList.remove(tempUser);
            tempUser.setSadmin(false);
            LiveRoomAdminManager.getInstance().removeAdministrator(tempUser);
            notifyListDataChange(tempUser);
        }
        updateManagerCount();
        if (mManagersList.size() > 0) {
            mLoadingLayout.setVisibility(View.GONE);
            return;
        }
        mLoadingLayout.setDefaultNoData();
    }


    private void tryGetManagerListData() {
        mManagerListRequest.startRequest(new LiveManagerListRequest.OnRequestManagerListCallback() {
            @Override
            public void onSuccess(List<UserInfo> userInfoList) {
                handleOnRequestManagerListSuccess(userInfoList);
            }

            @Override
            public void onFailed(int errorCode, String errorTips) {
                handleOnRequestManagerListFailed(errorCode, errorTips);
            }
        });
    }

    private void handleOnRequestManagerListFailed(int errorCode, String errorTips) {
        mLoadingLayout.setDefaultDataError(false);
    }

    private void handleOnRequestManagerListSuccess(List<UserInfo> userInfoList) {
        mManagersList.clear();
        for (UserInfo userInfo : userInfoList) {
            userInfo.setSadmin(true);
        }
        mManagersList.addAll(userInfoList);
        if (mManagersList.size() > 0) {
            mLoadingLayout.setVisibility(View.GONE);
        } else {
            mLoadingLayout.setDefaultNoData();
        }
        mAdapter.notifyDataSetChanged();
        updateManagerCount();
    }

    private void updateManagerCount() {
        String managerCountStr = getString(R.string.live_manager_current_user_count);
        if (mManagersList.size() > MAX_MANAGER_COUNT) {
            managerCountStr += MAX_MANAGER_COUNT + "/" + MAX_MANAGER_COUNT + ")";
        } else {
            managerCountStr += LiveRoomAdminManager.getInstance().getAdministrators().size()
                    + "/" + MAX_MANAGER_COUNT + ")";
        }
        mManagerCountView.setText(managerCountStr);
    }

    @Override
    public void onEvent(PostEvent event) {
        super.onEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
