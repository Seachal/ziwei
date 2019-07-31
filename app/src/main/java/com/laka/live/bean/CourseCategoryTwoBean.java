package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by Lyf on 2017/9/18.
 */
public class CourseCategoryTwoBean {

    @Expose
    @SerializedName("category")
    private List<Category> category;

    public class Category {

        @Expose
        @SerializedName(Common.ID)
        private int id;

        @Expose
        @SerializedName(Common.NAME)
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }
}
