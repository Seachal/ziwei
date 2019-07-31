package com.laka.live.bean;

/**
 * Created by luwies on 16/7/7.
 */
public class FoundItem {

    public final static int TYPE_TOPIC_KEY_LIST = 0;

    public final static int TYPE_TOPIC_ROOM_LIST = 1;

    public final static int TYPE_NEWEST_ROOM = 2;

    private int type;

    private Object object;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
