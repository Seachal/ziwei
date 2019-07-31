package com.laka.live.ui.room.roommanagerlist;

import android.text.TextUtils;

import com.laka.live.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/6/16.
 * Email-1501448275@qq.com
 */
public class LiveRoomAdminManager {
    private List<UserInfo> mAdmins;
    private static LiveRoomAdminManager sAdminManager;
    private UserInfo mTempUserInfo;//用于记录当前被操作的用户，设置成功后将被清除
    public String mTempUserId;

    public static LiveRoomAdminManager getInstance() {
        if (sAdminManager == null) {
            sAdminManager = new LiveRoomAdminManager();
        }
        return sAdminManager;
    }

    private LiveRoomAdminManager() {
        mAdmins = new ArrayList<>();
    }

    public List<UserInfo> getAdministrators() {
        if (mAdmins == null) {
            mAdmins = new ArrayList<>();
        }
        return mAdmins;
    }

    public void addAdministrator(UserInfo userInfo) {
        mAdmins.add(userInfo);
    }

    public void removeAdministrator(UserInfo userInfo) {
        removeAdministrator(userInfo.getIdStr());
    }

    public void removeAdministrator(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        UserInfo tempUserInfo = null;
        for (UserInfo userInfo1 : mAdmins) {
            if (userInfo1.getIdStr().equals(userId)) {
                tempUserInfo = userInfo1;
            }
        }
        if (tempUserInfo != null) {
            mAdmins.remove(tempUserInfo);
        }
    }

    public void clearAdministrators() {
        mAdmins.clear();
    }

    public void setTempUserInfo(UserInfo tempUser) {
        this.mTempUserInfo = tempUser;
    }

    public UserInfo getTempUserInfo() {
        return mTempUserInfo;
    }

    public void clearTempUserInfo() {
        this.mTempUserInfo = null;
    }
}
