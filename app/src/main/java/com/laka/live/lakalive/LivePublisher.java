package com.laka.live.lakalive;

import android.text.TextUtils;
import android.view.View;

import com.laka.live.util.Log;
import com.laka.live.zego.widgets.ViewLive;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.constants.ZegoConstants;

import java.util.LinkedList;

/**
 * Created by luwies on 16/10/8.
 */
public class LivePublisher {

    private static final String TAG = "LivePublisher";
    protected LiveCallback mLiveCallback;

    public void setLiveCallback(LiveCallback callback) {
        mLiveCallback = callback;
    }

    protected void onPublishSucc() {
        if (mLiveCallback != null) {
            mLiveCallback.onPublishSucc();
        }
    }

    protected void onPublishStop(int retCode) {
        if (mLiveCallback != null) {
            mLiveCallback.onPublishStop(retCode);
        }
    }

    protected void onPlaySucc() {
        if (mLiveCallback != null) {
            mLiveCallback.onPlaySucc();
        }
    }

    protected  void onPlayStopEvent(int retCode, String streamID, String liveChannel) {
        if (mLiveCallback != null) {
            mLiveCallback.onPlayStopEvent(retCode, streamID, liveChannel);
        }
    }

    protected void onVideoSizeChanged(int width, int height) {
        if (mLiveCallback != null) {
            mLiveCallback.onVideoSizeChanged(width, height);
        }
    }

    protected void onCaptureVideoSize(int width, int height) {
        if (mLiveCallback != null) {
            mLiveCallback.onCaptureVideoSize(width, height);
        }
    }

    protected void onPlayQualityUpdate(int quality) {
        if (mLiveCallback != null) {
            mLiveCallback.onPlayQualityUpdate(quality);
        }
    }

    protected void onPublishQulityUpdate(int quality) {
        if (mLiveCallback != null) {
            mLiveCallback.onPublishQulityUpdate(quality);
        }
    }

    protected ZegoLiveRoom mZegoLiveRoom = null;
    protected int mPublishFlag = ZegoConstants.PublishFlag.JoinPublish;
    protected boolean mIsPublishing = false;
    protected int mLiveCount = 0;
    protected LinkedList<ViewLive> mListViewLive = new LinkedList<>();
    /**
     * 获取空闲的View用于播放或者发布.
     *
     * @return
     */
    protected ViewLive getFreeViewLive() {
        Log.d(TAG," getFreeViewLive mListViewLive.size()="+mListViewLive.size());
        ViewLive vlFreeView = null;
        for (int i = 0, size = mListViewLive.size(); i < size; i++) {
            ViewLive viewLive = mListViewLive.get(i);
            if (viewLive.isFree()) {
                vlFreeView = viewLive;
                vlFreeView.setVisibility(View.VISIBLE);
                break;
            }
        }
        return vlFreeView;
    }


    /**
     * 释放View用于再次播放.
     *
     * @param streamID
     */
    protected void releaseLiveView(String streamID) {
        if (TextUtils.isEmpty(streamID)) {
            return;
        }

        for (int i = 0, size = mListViewLive.size(); i < size; i++) {
            ViewLive currentViewLive = mListViewLive.get(i);
            if (streamID.equals(currentViewLive.getStreamID())) {
                int j = i;
                for (; j < size - 1; j++) {
                    ViewLive nextViewLive = mListViewLive.get(j + 1);
                    if (nextViewLive.isFree()) {
                        break;
                    }

                    if (nextViewLive.isPublishView()) {
                        mZegoLiveRoom.setPreviewView(currentViewLive.getTextureView());
                    } else {
                        mZegoLiveRoom.updatePlayView(nextViewLive.getStreamID(), currentViewLive.getTextureView());
                    }

                    currentViewLive.toExchangeView(nextViewLive);
                    currentViewLive = nextViewLive;
                }
                // 标记最后一个View可用
                mListViewLive.get(j).setFree();
                break;
            }
        }
    }

    /**
     * 通过streamID查找正在publish或者play的ViewLive.
     *
     * @param streamID
     * @return
     */
    protected ViewLive getViewLiveByStreamID(String streamID) {
        if (TextUtils.isEmpty(streamID)) {
            return null;
        }

        ViewLive viewLive = null;
        for (ViewLive vl : mListViewLive) {
            if (streamID.equals(vl.getStreamID())) {
                viewLive = vl;
                break;
            }
        }

        return viewLive;
    }

    /**
     * 推流成功.
     */
    protected void handlePublishSucc(String streamID) {
        mIsPublishing = true;
        Log.d(TAG,"handlePlayStop" + ": onPublishSucc(" + streamID + ")");

//        initPublishControlText();
//        mRlytControlHeader.bringToFront();
    }

    /**
     * 停止推流.
     */
    protected void handlePublishStop(int stateCode, String streamID) {
        mIsPublishing = false;
        Log.d(TAG,"handlePlayStop" + ": onPublishStop(" + streamID + ") --stateCode:" + stateCode);

        // 释放View
        releaseLiveView(streamID);

//        initPublishControlText();
//        mRlytControlHeader.bringToFront();
        onPublishStop(stateCode);
    }

    /**
     * 拉流成功.
     */
    protected void handlePlaySucc(String streamID) {
        Log.d(TAG,"handlePlaySucc" + ": onPlaySucc(" + streamID + ")");

        mLiveCount++;
        setPublishEnabled();

        onPlaySucc();
//        mRlytControlHeader.bringToFront();
    }

    /**
     * 停止拉流.
     */
    protected void handlePlayStop(int stateCode, String streamID) {
        Log.d(TAG,"handlePlayStop" + ": onPlayStop(" + streamID + ") --stateCode:" + stateCode);

        // 释放View
        releaseLiveView(streamID);

        mLiveCount--;
        setPublishEnabled();

//        mRlytControlHeader.bringToFront();
        onPlayStopEvent(stateCode,streamID,streamID);
    }

    protected void setPublishEnabled() {
        if (!mIsPublishing) {
            if (mLiveCount < ZegoLiveRoom.getMaxPlayChannelCount()) {
//                mTvPublisnControl.setEnabled(true);
            } else {
//                mTvPublisnControl.setEnabled(false);
            }
        }
    }



}
