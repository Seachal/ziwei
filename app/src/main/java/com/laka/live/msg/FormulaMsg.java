package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.ImageBean;
import com.laka.live.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/4/24.
 */

public class FormulaMsg extends Msg {

    @Expose
    @SerializedName(Common.FORMULA)
    public String formula; // 配方做法

    @Expose
    @SerializedName(Common.VIEWS)
    public String views; // 观看人数

    @Expose
    @SerializedName(Common.FORMULA_IMGS)
    private List<ImageBean> formulaImages = new ArrayList<>(); // 课程简介图片

    public List<ImageBean> getFormulaImages() {
        return formulaImages;
    }

    public void setFormulaImages(List<ImageBean> formulaImages) {
        this.formulaImages = formulaImages;
    }

}
