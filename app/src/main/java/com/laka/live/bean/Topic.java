package com.laka.live.bean;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.R;
import com.laka.live.ui.adapter.TopicsAdapter;
import com.laka.live.util.Common;

import java.io.Serializable;

/**
 * n
 * Created by luwies on 16/6/29.
 */
public class Topic implements Serializable {

    @Expose
    @SerializedName(Common.ID)
    private String id;

    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.LIVE_COUNT)
    private int live_count;

    public Topic() {}

    public Topic(String name) {
        this.name = name;
    }

    // item的类型
    public int item_type = TopicsAdapter.TYPE_HEAD;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLive_count() {
        return live_count;
    }

    public void setLive_count(int live_count) {
        this.live_count = live_count;
    }

    public static Topic makeMoreTopic(Context context) {
        Topic topic = new Topic();
        topic.id = "";
        topic.name = context.getString(R.string.more_topic);
        return topic;
    }

    public String getFormatName(Context context) {
        if (context != null) {
            return context.getString(R.string.topic_format, name);
        }
        return "";
    }

    public String getFormatName() {
        return "<font color='#FFC401'>#</font>" + getName().replaceAll("#","") + "<font color='#FFC401'>#</font> ";
    }

    public String getFormatName(String color) {
        return "<font color='"+color+"'>#</font>" + getName().replaceAll("#","") + "<font color='"+color+"'>#</font> ";
    }
}
