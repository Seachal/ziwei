package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/5/9.
 */
public class QueryRoomMsg extends Msg {

    @Expose
    @SerializedName(Common.ROOMS)
    public Rooms rooms;

    class Rooms{
        @Expose
        @SerializedName(Common.DOWN_URL)
        public String downUrl;
        @SerializedName(Common.STREAM_ID)
        @Expose
        public String streamId;

        @SerializedName(Common.CHANNEL_ID)
        @Expose
        public String channelId;

        @SerializedName(Common.BEGIN_TIME)
        @Expose
        public long beginTime;

        @Expose
        @SerializedName(Common.STATE)
        public int state; // 房间状态，0=未开播，1=直播中，2=暂时离线

        @SerializedName(Common.COURSE_ID)
        @Expose
        public String courseId;

    }

    public String getCourselId() {
        if(rooms!=null){
            return rooms.courseId;
        }else{
            return "";
        }
    }

    public String getChannelId() {
        if(rooms!=null){
            return rooms.channelId;
        }else{
            return "";
        }
    }

    // 当前是否在直播
    public boolean isLiving() {

        if(rooms!=null){
            return rooms.state != 0;
        }else{
            return false;
        }
    }

    public String getStreamId() {
        if(rooms!=null){
            return rooms.streamId;
        }else{
            return "";
        }
    }

    public String getDownUrl() {
        if(rooms!=null){
            return rooms.downUrl;
        }else{
            return "";
        }

    }

    public long getBeginTime() {
        if(rooms!=null){
            return rooms.beginTime;
        }else{
            return 0;
        }

    }
}
