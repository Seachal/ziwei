package com.laka.live.ui.room.roommanagerlist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.UserInfo;
import com.laka.live.manager.RequestType;
import com.laka.live.ui.room.ResponseCodeHelper;
import com.laka.live.ui.widget.AlphaImageView;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/6/24.
 * Email-1501448275@qq.com
 */
public class LiveRoomManagerListPanel extends BasePanel {
    private static final int MAX_MANAGER_COUNT = 5;
    private static final String TAG ="LiveRoomManagerListPanel";

    private LinearLayout mParentView;
    private RecyclerView mRecyclerView;
    private LoadingLayout mLoadingLayout;
    private TextView mManagerCountView;
    private AlphaImageView mCloseButton;
    private LiveManagerListAdapter mAdapter;
    private List<UserInfo> mManagersList = new ArrayList<>();
    private LiveManagerRequest mRequest;
    private LiveManagerListRequest mManagerListRequest;

    public LiveRoomManagerListPanel(Context context) {
        super(context);
        setAlpha(0);
        initView();
        initRequest();
        tryGetManagerListData();
    }

    @Override
    protected View onCreateContentView() {
        mParentView = new LinearLayout(mContext);
        mParentView.setOrientation(LinearLayout.VERTICAL);
        return mParentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        return super.getLayoutParams();
    }

    private void initRequest() {
        mRequest = new LiveManagerRequest(mContext, new LiveManagerRequest.OnRequestCallback() {
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
                ResponseCodeHelper.showTips(mContext, errorCode);
            }
        });
        mManagerListRequest = new LiveManagerListRequest();
    }


    private void initView() {
        View view = View.inflate(mContext, R.layout.live_room_manager_list_activity_layout, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, HardwareUtil.getDeviceHeight() / 2);
        layoutParams.gravity = Gravity.BOTTOM;
        mParentView.addView(view, layoutParams);
        mCloseButton = (AlphaImageView) view.findViewById(R.id.back);
        mManagerCountView = (TextView) view.findViewById(R.id.live_manager_count);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.live_manager_list_recycler_view);
        mLoadingLayout = (LoadingLayout) view.findViewById(R.id.loading_layout);
        mLoadingLayout.setDefaultLoading();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new LiveManagerListAdapter(mContext, mManagersList);
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

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
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
        Log.d(TAG," handleOnRequestManagerListSuccess userInfoList="+userInfoList.size());
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
        String managerCountStr = ResourceHelper.getString(R.string.live_manager_current_user_count);
        if (mManagersList.size() > MAX_MANAGER_COUNT) {
            managerCountStr += MAX_MANAGER_COUNT + "/" + MAX_MANAGER_COUNT + ")";
        } else {
            managerCountStr += LiveRoomAdminManager.getInstance().getAdministrators().size()
                    + "/" + MAX_MANAGER_COUNT + ")";
        }
        mManagerCountView.setText(managerCountStr);
    }
}
