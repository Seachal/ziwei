package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/3/28.
 */
public class RoomRecvMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private Data data;

    public class Data {
        @Expose
        @SerializedName(Common.RECV_COINS)
        private int recv_coins;

        @Expose
        @SerializedName(Common.VIEWS)
        private int views;

        @Expose
        @SerializedName(Common.RECV_LIKES)
        private int recv_likes;


        @Expose
        @SerializedName(Common.DURATION)
        private int duration;

        public int getRecv_coins() {
            return recv_coins;
        }

        public void setRecv_coins(int recv_coins) {
            this.recv_coins = recv_coins;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getRecv_likes() {
            return recv_likes;
        }

        public void setRecv_likes(int recv_likes) {
            this.recv_likes = recv_likes;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
