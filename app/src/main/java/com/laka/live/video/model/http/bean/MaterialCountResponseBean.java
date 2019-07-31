package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.Msg;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:素材库数据量ResponseBean
 */

public class MaterialCountResponseBean extends Msg {

    @SerializedName("count")
    private String materialCount;

    public String getMaterialCount() {
        return materialCount == null ? "" : materialCount;
    }

    public void setMaterialCount(String materialCount) {
        this.materialCount = materialCount;
    }

    @Override
    public String toString() {
        return "MaterialCountResponseBean{" +
                "materialCount='" + materialCount + '\'' +
                '}';
    }
}
