package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CourseCategoryOneBean;

import java.util.List;

/**
 * Created by Lyf on 2017/9/18.
 */

public class CourseCategoryOneMsg extends Msg {

    @Expose
    @SerializedName("data")
    private List<CourseCategoryOneBean> categories;

    public List<CourseCategoryOneBean> getCategories() {
        return categories;
    }

    public void setCategories(List<CourseCategoryOneBean> categories) {
        this.categories = categories;
    }

}
