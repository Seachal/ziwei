package com.laka.live.bean;

/**
 * Created by Lyf on 2017/9/18.
 */

public class ContentHomeTitleBean {

    public final static int TYPE_LIVE = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_NEWS = 3;
    public final static int TYPE_TOPICS = 4;

    private int type;
    private String title;

    public ContentHomeTitleBean(ContentBean contentBean) {
        if (contentBean != null) {
            this.type = contentBean.getType();
            this.title = contentBean.getTitle();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
