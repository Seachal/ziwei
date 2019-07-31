package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/7/7.
 */
public class FoundTopicItem {

    @Expose
    @SerializedName(Common.ID)
    private String id;

    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.DATA)
    private List<Room> roomList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
}
