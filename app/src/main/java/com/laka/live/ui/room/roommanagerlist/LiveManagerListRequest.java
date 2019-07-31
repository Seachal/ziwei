package com.laka.live.ui.room.roommanagerlist;

import com.laka.live.R;
import com.laka.live.bean.UserInfo;
import com.laka.live.msg.ListMag;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zwl on 2016/6/15.
 * Email-1501448275@qq.com
 */
public class LiveManagerListRequest {
    private boolean mIsRequesting = false;

    public LiveManagerListRequest() {
    }

    public void startRequest(final OnRequestManagerListCallback callback) {
        if (callback == null) {
            return;
        }
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        HashMap<String, String> params = new HashMap<>();
        DataProvider.addToken(params);
        GsonHttpConnection.getInstance().post(null, Common.QUERY_ROOM_MANAGER_LIST_URL, params,
                QueryRoomManagerListMsg.class,
                new GsonHttpConnection.OnResultListener<ListMag<UserInfo>>() {
                    @Override
                    public void onSuccess(ListMag<UserInfo> userInfoListMag) {
                        handleOnSuccess(userInfoListMag, callback);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        handleOnFail(errorCode, errorMsg, callback);
                    }
                });
    }

    private void handleOnFail(int errorCode, String errorTips, OnRequestManagerListCallback callback) {
        mIsRequesting = false;
        callback.onFailed(errorCode, errorTips);
    }

    private void handleOnSuccess(ListMag<UserInfo> listMag, OnRequestManagerListCallback callback) {
        mIsRequesting = true;
        if (listMag.getList() == null) {
            handleOnFail(Msg.NETWORK_ERROR_UNKNOW_ERROR, ResourceHelper.getString(R.string.live_manager_data_error_code), callback);
            return;
        }
        callback.onSuccess(listMag.getList());
    }

    public interface OnRequestManagerListCallback {
        void onSuccess(List<UserInfo> userInfoList);

        void onFailed(int errorCode, String errorTips);
    }
}
