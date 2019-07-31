package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CourseTrailer;
import com.laka.live.bean.Topic;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/5/9.
 */
public class OpenLiveMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    public Data data;

    @Expose
    @SerializedName(Common.TOPICS)
    public List<Topic> topics; // 话题数组

    public class Data{
        @SerializedName(Common.LIVE_ID)
        @Expose
        public String liveId;

        @SerializedName(Common.STREAM_ID)
        @Expose
        public String streamId;

        @SerializedName(Common.CHANNEL_ID)
        @Expose
        public String channelId;

        @SerializedName(Common.COURSE_ID)
        @Expose
        public String courseId;
    }

//    @Expose
//    @SerializedName(Common.UPSTREAM_ADDRESS)
//    public String upstreamAddress;


    public String getChannelId() {
        if(data!=null){
            return data.channelId;
        }
        return "";
    }


    public String getStreamId() {
        if(data!=null){
            return data.streamId;
        }
        return "";
    }

    public String getLiveId() {
        if(data!=null){
            return data.liveId;
        }
        return "";
    }

    public String getCourseId() {
        if(data!=null){
            return data.courseId;
        }
        return "";
    }

}
