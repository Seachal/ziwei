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
public class JoinLiveMsg extends Msg {


    @Expose
    @SerializedName(Common.TOPICS)
    public List<Topic> topics; // 话题数组

    @Expose
    @SerializedName(Common.LIVE_ID)
    public String liveId;

    @Expose
    @SerializedName(Common.STREAM_ID)
    public String streamId;

    @Expose
    @SerializedName(Common.CHANNEL_ID)
    public String channelId;

    public String getChannelId() {
        return channelId;
    }


    public String getStreamId() {
        return streamId;
    }



}
