package com.laka.live.ui.room;

import com.laka.live.util.StringUtils;

/**
 * Created by zwl on 2016/7/8.
 * Email-1501448275@qq.com
 */
public class LiveRoomManager {
    private static LiveRoomManager sInstance;
    private String mCurrentLiveId;

    private LiveRoomManager() {
    }

    public static LiveRoomManager getInstance() {
        if (sInstance == null) {
            sInstance = new LiveRoomManager();
        }
        return sInstance;
    }

    public boolean checkUserIsCurrentLiveRoomUser(String userId) {
        if (StringUtils.isEmpty(mCurrentLiveId)) {
            return false;
        }
        return mCurrentLiveId.equals(userId);
    }

    public void setCurrentLiveRoomUserId(String liveRoomUserId) {
        mCurrentLiveId = liveRoomUserId;
    }

    public void clearCurrentLiveRoomUser() {
        mCurrentLiveId = "";
    }
}
