package com.laka.live.bean;

import android.app.Activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.shopping.activity.ShoppingTopicActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.BestTopicsActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.widget.emoji.StringUtil;
import com.laka.live.util.Common;
import com.laka.live.util.ToastHelper;

import java.util.HashMap;

/**
 * Created by luwies on 16/4/16.
 */
public class Banner {

    public final static int TYPE_ROOM = 1;

    public final static int TYPE_URL = 2;

    public final static int TYPE_COURSE = 5;

    public final static int TYPE_GOODS = 6;

    @Expose
    @SerializedName(Common.ID)
    private int id;

    @Expose
    @SerializedName(Common.IMAGE)
    private String image;

    @Expose
    @SerializedName(Common.TYPE)
    private int type;

    @Expose
    @SerializedName(Common.VALUE)
    private String value;

//    @SerializedName(Common.DOWN_URL)
//    @Expose
//    private String downUrl;//播放地址
//
//    @SerializedName(Common.STREAM_ID)
//    @Expose
//    private String streamId;
//
//    @SerializedName(Common.CHANNEL_ID)
//    @Expose
//    private String channelId;

//    @Expose
//    @SerializedName(Common.COURSE)
//    private Course course;

//    public String getChannelId() {
//        return channelId;
//    }
//
//    public void setChannelId(String channelId) {
//        this.channelId = channelId;
//    }
//
//    public String getStreamId() {
//        return streamId;
//    }
//
//    public void setStreamId(String streamId) {
//        this.streamId = streamId;
//    }
//
//    public String getDownUrl() {
//        return downUrl;
//    }
//
//    public void setDownUrl(String downUrl) {
//        this.downUrl = downUrl;
//    }

    /**
     * 广告属性
     */
    private int adAttr;
    /**
     * 广告属性值
     */
    private String adAttrValue;

    /**
     * 广告标题
     */
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAdAttr() {
        return adAttr;
    }

    public void setAdAttr(int adAttr) {
        this.adAttr = adAttr;
    }

    public String getAdAttrValue() {
        return adAttrValue;
    }

    public void setAdAttrValue(String adAttrValue) {
        this.adAttrValue = adAttrValue;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

//    public Course getCourse() {
//        return course;
//    }
//
//    public void setCourse(Course course) {
//        this.course = course;
//    }

    public void onClick(Activity activity) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.BANNER_ID, String.valueOf(id));
//        AnalyticsReport.onEvent(activity, AnalyticsReport.HOME_BANNER_CLICK_EVENT_ID, params);
        AnalyticsReport.onEvent(activity, AnalyticsReport.EVENT_10232, params);

        switch (type) {
            case TYPE_ROOM:  // 课程详情
                if (value == null) {
                    return;
                }
                // 点击banner跳到直播页，功能下版本做，只需要把屏幕的代码去掉，再检查一下startPlay方法的参数即可
//                if (course.getStatus() == 40 && course.hasBuy()) {
//                    if (course == null ||
//                            Utils.isEmpty(course.getRoom().getChannelId()) ||
//                            Utils.isEmpty(course.getRoom().getStreamId())) {
//                        ToastHelper.showToast(R.string.no_live_url);
//                        return;
//                    }
//                    try {
//
//                        LiveRoomActivity.startPlay(activity,course.user_id, false,"",
//                                course.room.getStreamId(),course.room.getChannelId(),"",
//                                "" ,Common.FROM_BANNER);
//
//                    } catch (NumberFormatException e) {
//
//                    }
//                } else {

                if (StringUtil.isEmpty(value)) {
                    ToastHelper.showToast("课程ID为空");
                    return;
                }
                CourseDetailActivity.startActivity(activity, value);
                //     }
                break;
            case TYPE_URL:  // web 页面
                WebActivity.startActivity(activity, value, "");
                break;
            case TYPE_GOODS:  // 商城专题
                if (value == null) {
                    return;
                }
                ShoppingTopicActivity.startActivity(activity, null, Integer.parseInt(value));
                break;
            case TYPE_COURSE:  // 优选专题详情
                if (value == null) {
                    return;
                }
                BestTopicsActivity.startActivity(activity, Integer.parseInt(value));
                break;
        }
    }
}
