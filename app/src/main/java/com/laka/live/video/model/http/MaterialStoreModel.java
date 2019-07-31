package com.laka.live.video.model.http;

import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.Common;
import com.laka.live.video.constant.VideoApiConstant;
import com.laka.live.video.contract.MaterialStoreContract;
import com.laka.live.video.model.http.bean.MaterialCountResponseBean;
import com.laka.live.video.model.http.bean.MaterialDeleteResponseBean;
import com.laka.live.video.model.http.bean.MaterialListResponseBean;

import java.util.HashMap;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:素材库页面Model
 */

public class MaterialStoreModel implements MaterialStoreContract.IMaterialModel {

    @Override
    public void getMaterialStoreList(String page, String limit,
                                     GsonHttpConnection.OnResultListener<MaterialListResponseBean> callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, page);
        params.put(Common.LIMIT, limit);
        DataProvider.addToken(params);
        GsonHttpConnection.getInstance().get(this,
                VideoApiConstant.GET_MATERIAL_LIST_API, params,
                MaterialListResponseBean.class, callBack);
    }

    @Override
    public void deleteMaterial(String materialId, GsonHttpConnection.OnResultListener<Msg> callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.MATERIAL_VIDEO_ID, materialId);
        DataProvider.addToken(params);
        GsonHttpConnection.getInstance().post(this,
                VideoApiConstant.DELETE_MATERIAL_API, params,
                Msg.class, callBack);
    }

    @Override
    public void getMaterialCount(GsonHttpConnection.OnResultListener<MaterialCountResponseBean> callBack) {
        DataProvider.getMaterialCount(this, callBack);
    }
}
