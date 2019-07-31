package com.laka.live.bean;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.R;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.util.Common;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.io.Serializable;

/**
 * Created by luwies on 16/3/31.
 */
public class BaseRoom implements Serializable{

    @SerializedName(Common.STREAM_ID)
    @Expose
    public String streamId;

    @SerializedName(Common.CHANNEL_ID)
    @Expose
    public String channelId;

    @Expose
    @SerializedName(Common.ID)
    public int id;

    @SerializedName(Common.AVATAR)
    @Expose
    public String avatar;

    @SerializedName(Common.TITLE)
    @Expose
    public String title;

    @SerializedName(Common.DOWN_URL)
    @Expose
    public String downUrl;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar The avatar_url
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public static void enterRoom(Context context, Room room, String from) {
        if (room == null) {
            return;
        }
        if (Utils.isEmpty(room.getStreamId()) || Utils.isEmpty(room.getChannelId())) {
            ToastHelper.showToast(R.string.no_live_url);
            return;
        }

        int id = room.getId();
        LiveRoomActivity.startPlay(context, id, false,
                room.getTitle(), room.getStreamId(), room.getChannelId(), room.getAvatar(), "", from,""
        );

    }


}
