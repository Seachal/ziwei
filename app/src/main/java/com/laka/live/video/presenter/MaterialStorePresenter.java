package com.laka.live.video.presenter;

import com.laka.live.msg.Msg;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.video.contract.MaterialStoreContract;
import com.laka.live.video.model.http.MaterialStoreModel;
import com.laka.live.video.model.http.bean.MaterialCountResponseBean;
import com.laka.live.video.model.http.bean.MaterialDeleteResponseBean;
import com.laka.live.video.model.http.bean.MaterialListResponseBean;
import com.orhanobut.logger.Logger;

import static com.laka.live.msg.Msg.NETWORK_ERROR_PARSE_ERROR;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:素材库P层
 */

public class MaterialStorePresenter implements MaterialStoreContract.IMaterialPresenter {

    private MaterialStoreContract.IMaterialView mView;
    private MaterialStoreContract.IMaterialModel mModel;

    public MaterialStorePresenter(MaterialStoreContract.IMaterialView mView) {
        this.mView = mView;
        mModel = new MaterialStoreModel();
    }

    @Override
    public void getMaterialStoreList(int page, int limit) {
        mModel.getMaterialStoreList(page + "", limit + "",
                new GsonHttpConnection.OnResultListener<MaterialListResponseBean>() {
                    @Override
                    public void onSuccess(MaterialListResponseBean materialListResponseBean) {
                        mView.showMaterialList(materialListResponseBean);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        mView.showMaterialList(null);
                    }
                });
    }

    @Override
    public void deleteMaterial(String materialId) {
        mModel.deleteMaterial(materialId, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg responseBean) {
                String hint = "删除成功~";
                if (responseBean.isSuccessFul()) {
                    mView.deleteMaterialCallback(true, hint);
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Logger.e("输出错误：" + errorMsg);
                mView.deleteMaterialCallback(false, errorMsg);
            }
        });
    }

    @Override
    public void getMaterialCount() {
        mModel.getMaterialCount(new GsonHttpConnection.OnResultListener<MaterialCountResponseBean>() {
            @Override
            public void onSuccess(MaterialCountResponseBean materialCountResponseBean) {
                mView.showMaterialCount(materialCountResponseBean.getMaterialCount());
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mView.showMaterialCount("");
            }
        });
    }
}
