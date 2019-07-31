package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/4/8.
 */
public class VersionInfo {

    public static final int NORMAL = 1;

    public static final int FORCE = 2;

    @Expose
    @SerializedName(Common.STATE)
    private int state;

    @Expose
    @SerializedName(Common.VERSION)
    private int version;

    @Expose
    @SerializedName(Common.VERSION_SHOW)
    private String version_show;

    @Expose
    @SerializedName(Common.URL)
    private String url;

    @Expose
    @SerializedName(Common.DESCRIPTION)
    private String description;

    @Expose
    @SerializedName(Common.EDIT_TIME)
    private String edittime;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion_show() {
        return version_show;
    }

    public void setVersion_show(String version_show) {
        this.version_show = version_show;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }

    public boolean isForce() {
        return state == FORCE;
    }
}
