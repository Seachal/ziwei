package com.laka.live.video.contract;

import com.laka.live.msg.Msg;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.video.model.bean.VideoMaterialBean;
import com.laka.live.video.model.http.bean.MaterialCountResponseBean;
import com.laka.live.video.model.http.bean.MaterialDeleteResponseBean;
import com.laka.live.video.model.http.bean.MaterialListResponseBean;

import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:小视频素材库Contract
 */

public interface MaterialStoreContract {

    interface IMaterialView {

        /**
         * 获取素材库列表回调
         *
         * @param videoMaterialList
         */
        void showMaterialList(MaterialListResponseBean videoMaterialList);

        /**
         * 删除素材回调
         *
         * @param isDelete
         */
        void deleteMaterialCallback(boolean isDelete, String msg);

        /**
         * 获取素材库数量回调
         *
         * @param materialCount
         */
        void showMaterialCount(String materialCount);

    }

    interface IMaterialModel {

        /**
         * 获取素材库列表
         *
         * @param page
         * @param limit
         * @param callBack 回调获取列表数据
         */
        void getMaterialStoreList(String page, String limit, GsonHttpConnection.OnResultListener<MaterialListResponseBean> callBack);

        /**
         * 删除素材
         *
         * @param materialId
         * @param callBack   回调true 代表删除成功，false删除失败
         */
        void deleteMaterial(String materialId, GsonHttpConnection.OnResultListener<Msg> callBack);

        /**
         * 获取素材列表数量
         *
         * @param callBack 回调获取列表大小
         */
        void getMaterialCount(GsonHttpConnection.OnResultListener<MaterialCountResponseBean> callBack);
    }

    interface IMaterialPresenter {

        void getMaterialStoreList(int page, int limit);

        void deleteMaterial(String materialId);

        void getMaterialCount();
    }
}
