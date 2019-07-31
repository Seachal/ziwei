package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.FoundItem;
import com.laka.live.bean.FoundTopicItem;
import com.laka.live.bean.Room;
import com.laka.live.bean.Topic;
import com.laka.live.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwies on 16/7/7.
 */
public class FoundMsg extends ListMag {

    @Expose
    @SerializedName(Common.TOPICS)
    private List<Topic> topics;

    @Expose
    @SerializedName(Common.TOPICS_LIVE)
    private List<FoundTopicItem> topicItems;

    @Expose
    @SerializedName(Common.ROOMS)
    private List<Room> rooms;

    private List<FoundItem> list;

    @Override
    public List getList() {
        return list;
    }

    @Override
    public void parase() {
        list = new ArrayList<>();

        boolean isTopicsNotNull = topics != null && topics.isEmpty() == false;
        if (isTopicsNotNull) {
            FoundItem item = new FoundItem();
            item.setType(FoundItem.TYPE_TOPIC_KEY_LIST);
            item.setObject(topics);
            list.add(item);
        }

        boolean isTopicItemsNotNull = topicItems != null && topicItems.isEmpty() == false;
        if (isTopicItemsNotNull) {
            for (FoundTopicItem topicItem : topicItems) {
                if (topicItem != null) {
                    FoundItem item = new FoundItem();
                    item.setType(FoundItem.TYPE_TOPIC_ROOM_LIST);
                    item.setObject(topicItem);
                    list.add(item);
                    List<Room> rooms = topicItem.getRoomList();
                    if (rooms != null && rooms.isEmpty() == false) {
                        for (Room room : rooms) {
                            if (room != null) {
                                room.setIsLive(true);
                            }
                        }
                    }
                }
            }
        }

        boolean isRoomsNotNull = rooms != null && rooms.isEmpty() == false;

        if (isRoomsNotNull) {
            for (Room room : rooms) {
                if (room != null) {
                    room.setIsLive(true);
                    FoundItem item = new FoundItem();
                    item.setType(FoundItem.TYPE_NEWEST_ROOM);
                    item.setObject(room);
                    list.add(item);
                }
            }
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
