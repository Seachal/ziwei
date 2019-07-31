package com.laka.live.account.replay;

import android.content.Context;

import com.laka.live.bean.Video;
import com.laka.live.msg.Msg;
import com.laka.live.msg.VideosListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by zwl on 2016/7/1.
 * Email-1501448275@qq.com
 */
public class ReplayRequest {
    public static final int DEFAULT_PAGE_SIZE = 10;
    private Context mContext;
    private boolean mIsHasMoreData;
    private int mPage = 0;
    private boolean mIsRequesting;
    private String mUserId;
    private int mPageSize = DEFAULT_PAGE_SIZE;

    public ReplayRequest(Context context, String userId) {
        mContext = context;
        mUserId = userId;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    public void startRequestData(boolean isLoadMore, OnRequestResultCallback callback) {
        if (isLoadMore) {
            startRequest(mUserId, mPage, callback);
            return;
        }
        startRequest(mUserId, 0, callback);
    }

    private void startRequest(String useId, int page, final OnRequestResultCallback callback) {
        if (callback == null) {
            return;
        }
        if (StringUtils.isEmpty(useId)) {
            return;
        }
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        mPage = page;
        DataProvider.queryVideo(mContext, useId, page, mPageSize, new GsonHttpConnection.OnResultListener<VideosListMsg>() {
            @Override
            public void onSuccess(VideosListMsg videosListMsg) {
                handleOnRequestSuccess(videosListMsg, callback);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnRequestFailed(errorCode, errorMsg, callback);
            }
        });
    }

    private void handleOnRequestSuccess(VideosListMsg videosListMsg, OnRequestResultCallback callback) {
        mIsRequesting = false;
        if (videosListMsg.getCode() != Msg.TLV_OK) {
            handleOnRequestFailed(Msg.NETWORK_ERROR_UNKNOW_ERROR, "", callback);
            return;
        }
        if (videosListMsg.getList() == null) {
            handleOnRequestFailed(Msg.NETWORK_ERROR_UNKNOW_ERROR, "", callback);
            return;
        }
        if (videosListMsg.getList().size() == 0) {
            mIsHasMoreData = false;
        } else {
            mPage++;
            mIsHasMoreData = true;
        }
        callback.onRequestSuccess(videosListMsg.getList());
    }

    private void handleOnRequestFailed(int errorCode, String errorMsg, OnRequestResultCallback callback) {
        mIsRequesting = false;
        callback.onRequestFailed(errorCode, errorMsg);
    }


    public boolean isHasMoreData() {
        return mIsHasMoreData;
    }

    public interface OnRequestResultCallback {
        void onRequestSuccess(List<Video> videoList);

        void onRequestFailed(int errorCode, String errorMsg);
    }
}
