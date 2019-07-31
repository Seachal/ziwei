package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Banner;
import com.laka.live.bean.Room;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by lyf on 17/4/11.
 */
public class RoomListMsg extends ListMag<Room> {

    @Expose
    @SerializedName(Common.SLIDES)
    private List<Banner> banners;

    @Expose
    @SerializedName(Common.ROOMS)
    private List<Room> list;

    @Override
    public List<Room> getList() {
        return list;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public void setList(List<Room> list) {
        this.list = list;
    }


    @Override
    public boolean isEmpty() {
        return (banners == null || banners.isEmpty()) && (list == null || list.isEmpty());
    }
}
