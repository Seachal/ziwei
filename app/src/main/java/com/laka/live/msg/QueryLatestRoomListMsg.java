package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.BaseRoom;
import com.laka.live.bean.Room;
import com.laka.live.bean.Topic;
import com.laka.live.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwies on 16/3/31.
 */
public class QueryLatestRoomListMsg extends ListMag<Room> {

    @Expose
    @SerializedName(Common.TOPICS)
    private List<Topic> topics;

    @Expose
    @SerializedName(Common.ROOMS)
    private List<Room> list;

    @Override
    public List<Room> getList() {
        return list;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    @Override
    public boolean isEmpty() {
        return (topics == null || topics.isEmpty()) && super.isEmpty();
    }
}
