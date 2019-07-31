package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.download.DownloadState;
import com.laka.live.bean.MusicInfo;
import com.laka.live.music.MusicManager;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/8/15.
 */
public class MusicMsg extends ListMag<MusicInfo> {

    @Expose
    @SerializedName(Common.DATA)
    private List<MusicInfo> list;

    @Override
    public List getList() {
        return list;
    }

    @Override
    public void parase() {
        super.parase();
        if (list != null && list.isEmpty() == false) {
            MusicInfo tempInfo;
            for (MusicInfo info : list) {
                if (info != null) {
                    tempInfo = MusicManager.getInstance().getMusicInfo(info.getId());
                    if (tempInfo == null) {
                        info.setState(DownloadState.NONE);
                        continue;
                    }
                    MusicInfo.copyLocalInfo(info, tempInfo);
                }
            }
        }
    }


}
