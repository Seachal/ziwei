package com.laka.live.ui.room.roommanagerlist;

import android.content.Context;

import com.laka.live.manager.RequestType;
import com.laka.live.manager.RoomManager;

/**
 * Created by zwl on 2016/6/14.
 * Email-1501448275@qq.com
 */
public class LiveManagerRequest {
    private boolean isRequesting = false;
    private RoomManager mRoomManager;
    private String mCurrentIdStr;

    public LiveManagerRequest(Context context, final OnRequestCallback callback) {
        mRoomManager = RoomManager.getInstance();
        mRoomManager.startRoom();
        mRoomManager.setOnRequestAdminResultCallback(new RoomManager.onRequestAdminResultCallback() {
            @Override
            public void onFailed(int errorCode) {
                handleOnRequestResultFail(errorCode, "", callback);
            }

            @Override
            public void onSuccess(int requestType, Object object) {
                handleOnRequestResultSuccess(requestType, object, callback);
            }
        });
    }

    public void tryAddAdminRequest(final String managerId) {
        startRequest(managerId, RequestType.REQUEST_TYPE_ADD_ADMIN);
    }

    public void tryRemoveAdminRequest(final String managerId) {
        startRequest(managerId, RequestType.REQUEST_TYPE_REMOVE_ADMIN);
    }

    private void startRequest(final String managerId, int type) {
        if (isRequesting) {
            return;
        }
        mCurrentIdStr = managerId;
        isRequesting = true;
        if (type == RequestType.REQUEST_TYPE_ADD_ADMIN) {
            mRoomManager.sendAddRoomAdmin(managerId);
        } else if (type == RequestType.REQUEST_TYPE_REMOVE_ADMIN) {
            mRoomManager.sendRemoveRoomAdmin(managerId);
        }
    }

    private void handleOnRequestResultFail(int errorCode, String errorTips, OnRequestCallback callback) {
        isRequesting = false;
        callback.onFailed(errorCode, errorTips);
    }

    private void handleOnRequestResultSuccess(int requestType, Object object, OnRequestCallback callback) {
        isRequesting = false;
        callback.onSuccess(requestType, mCurrentIdStr);
        clearCurrentIdStr();
    }

    public String getCurrentId() {
        return mCurrentIdStr;
    }

    public void clearCurrentIdStr() {
        mCurrentIdStr = null;
    }

    public interface OnRequestCallback {
        void onSuccess(int requestType, Object object);

        void onFailed(int errorCode, String errorTip);
    }
}
