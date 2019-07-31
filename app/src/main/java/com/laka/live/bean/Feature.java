package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/6/28.
 */
public class Feature {

    @Expose
    @SerializedName(Common.CHARID)
    private int charid;

    @Expose
    @SerializedName(Common.CHAR_NAME)
    private String name;

    @Expose
    @SerializedName(Common.DATA)
    private List<Room> rooms;

    public int getCharid() {
        return charid;
    }

    public void setCharid(int charid) {
        this.charid = charid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
