package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Topic;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by zwl on 2016/6/29.
 * Email-1501448275@qq.com
 */
public class TopicListMsg extends ListMag<Topic> {

    @Expose
    @SerializedName(Common.DATA)
    private List<Topic> topicList;

    @Override
    public List<Topic> getList() {
        return topicList;
    }
}
