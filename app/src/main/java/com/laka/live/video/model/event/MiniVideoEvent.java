package com.laka.live.video.model.event;

/**
 * @Author:Rayman
 * @Date:2018/8/27
 * @Description:小视频事件发送
 */

public class MiniVideoEvent {

    /**
     * 接收的对象
     */
    private Object targetObj;

    /**
     * KeyWord
     */
    private String tag;

    /**
     * 事件的具体内容
     */
    private Object event;

    public Object getTargetObj() {
        return targetObj;
    }

    public MiniVideoEvent setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
        return this;
    }

    public String getTag() {
        return tag == null ? "" : tag;
    }

    public MiniVideoEvent setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Object getEvent() {
        return event;
    }

    public MiniVideoEvent setEvent(Object event) {
        this.event = event;
        return this;
    }


}
