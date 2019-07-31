package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.Video;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/28.
 */
public class VideosListMsg extends ListMag<Video> {

    @Expose
    @SerializedName(Common.USER)
    private ListUserInfo userInfo;

    @Expose
    @SerializedName(Common.DATA)
    private List<Video> list;

    @Override
    public List<Video> getList() {
        return list;
    }

    @Override
    public void parase() {
        if (userInfo == null) {
            userInfo = AccountInfoManager.getInstance().getAccountInfo();
        }
        if (list != null && userInfo != null) {
            for (Video video : list) {
                video.setId(userInfo.getId());
                video.setAvatar(userInfo.getAvatar());
                video.setLevel(userInfo.getLevel());
            }
        }
    }
}
