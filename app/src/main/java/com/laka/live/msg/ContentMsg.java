package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.ContentBean;

import java.util.List;

/**
 * Created by Lyf on 2017/9/18.
 */

public class ContentMsg extends Msg {

    @Expose
    @SerializedName("data")
    private List<ContentBean> contentBeanList;

    public List<ContentBean> getContentBeanList() {
        return contentBeanList;
    }

    public void setContentBeanList(List<ContentBean> contentBeanList) {
        this.contentBeanList = contentBeanList;
    }

    /**
     * @return 获取直播列表
     */
    public ContentBean getLiveList() {

        if (contentBeanList != null) {
            for (ContentBean contentBean : contentBeanList) {
                if (contentBean.isLive()) {
                    return contentBean;
                }
            }
        }
        return null;
    }

    /**
     * @return 获取视频列表
     */
    public ContentBean getVideoList() {

        if (contentBeanList != null) {
            for (ContentBean contentBean : contentBeanList) {
                if (contentBean.isVideo()) {
                    return contentBean;
                }
            }
        }
        return null;
    }

    /**
     * @return 获取资讯列表
     */
    public ContentBean getNewsList() {

        if (contentBeanList != null) {
            for (ContentBean contentBean : contentBeanList) {
                if (contentBean.isNews()) {
                    return contentBean;
                }
            }
        }
        return null;
    }

    /**
     * @return 获取专题列表
     */
    public ContentBean getTopicsList() {

        if (contentBeanList != null) {
            for (ContentBean contentBean : contentBeanList) {
                if (contentBean.isTopic()) {
                    return contentBean;
                }
            }
        }
        return null;
    }


}
