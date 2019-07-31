package com.laka.live.help;

/**
 * Created by ios on 16/5/28.
 */
public class PostEvent {
    public String tag;
    public Object event;

    public PostEvent(String tag) {
        this.tag = tag;
    }

    public PostEvent(Object event) {
        this.event = event;
    }

    public PostEvent(Object event, String tag) {
        this.tag = tag;
        this.event = event;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "PostEvent{" +
                "tag='" + tag + '\'' +
                ", event=" + event +
                '}';
    }
}
