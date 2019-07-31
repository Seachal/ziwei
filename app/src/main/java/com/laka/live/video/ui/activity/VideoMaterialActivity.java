package com.laka.live.video.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.decoration.DividerItemDecoration;
import com.laka.live.util.Common;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.PhotoUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoConstant;
import com.laka.live.video.contract.MaterialStoreContract;
import com.laka.live.video.model.bean.VideoMaterialBean;
import com.laka.live.video.model.http.bean.MaterialListResponseBean;
import com.laka.live.video.presenter.MaterialStorePresenter;
import com.laka.live.video.ui.adapter.VideoMaterialAdapter;
import com.laka.live.player.MyExoPlayerPlus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import framework.utils.PermissionUtils;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:视频素材Activity
 */

public class VideoMaterialActivity extends BaseActivity implements MaterialStoreContract.IMaterialView {

    public static final int CLICK_INTERVAL = 1500;

    @BindView(R.id.iv_material_back)
    ImageView mIvBack;
    @BindView(R.id.tv_material_upload)
    TextView mTvUpLoad;
    @BindView(R.id.page_list_layout)
    PageListLayout mPageListLayout;

    /**
     * description:UI配置
     **/
    private int uiType = VideoConstant.MATERIAL_TYPE_CONTROL;
    private long lastClickTime = 0;

    /**
     * description:数据配置
     **/
    private VideoMaterialAdapter mAdapter;
    private int pageLimit = 10;
    private int deletePosition = -1;
    private GsonHttpConnection.OnResultListener mResultListener;
    private MaterialStoreContract.IMaterialPresenter mPresenter;

    public static void startActivity(Activity activity, @VideoConstant.MATERIAL_UI_TYPE int uiType) {
        Intent intent = new Intent();
        intent.setClass(activity, VideoMaterialActivity.class);
        intent.putExtra(VideoConstant.MATERIAL_UI_TYPE, uiType);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_material);
        ButterKnife.bind(this);

        initIntent(getIntent());
        initView();
        initData();
        initEvent();
    }

    private void initIntent(Intent intent) {
        uiType = intent.getIntExtra(VideoConstant.MATERIAL_UI_TYPE, VideoConstant.MATERIAL_TYPE_CONTROL);
    }

    private void initView() {
        initListView();
    }

    private void initData() {
        mPresenter = new MaterialStorePresenter(this);
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new VideoMaterialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, VideoMaterialBean item) {
                if (mAdapter.getUiType() == VideoConstant.MATERIAL_TYPE_CHOOSE) {
                    Intent intent = new Intent();
                    VideoMaterialBean materialBean = mAdapter.getData().get(position);
                    materialBean.setCheck(true);
                    mAdapter.notifyDataSetChanged();
                    intent.putExtra(VideoConstant.MATERIAL_EXTRA_ITEM, materialBean);
                    setResult(PhotoUtil.MATERIAL_STORE, intent);
                    VideoMaterialActivity.this.finish();
                } else {
                    //跳转到小视频播放页面
                    if (Utils.isNotEmpty(item.getVideoUrl())) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put(Common.VIDEO, item.getVideoId());
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.HOME_VIDEO_CLICK_EVENT_ID, params);

                        //TODO 播放小视频
                        // MyVideoPlayer.startFullscreen(mContext, MyVideoPlayer.class, item.getVideoUrl(), item.getVideoTitle());
                        MyExoPlayerPlus.startFullscreen(mContext, MyExoPlayerPlus.class, item.getVideoUrl(), item.getVideoTitle());

                        setSwipeBackEnable(false);
                    }
                }
            }

            @Override
            public void onDeleteClick(int position, VideoMaterialBean item) {
                deletePosition = position;
                mPresenter.deleteMaterial(item.getVideoId());
            }
        });

        mPageListLayout.setOnRequestCallBack(new PageListLayout.OnRequestCallBack() {
            @Override
            public String request(int page, GsonHttpConnection.OnResultListener listener) {
                mPresenter.getMaterialStoreList(page, pageLimit);
                mResultListener = listener;
                return null;
            }
        });

        mPageListLayout.loadData();
    }

    private void initListView() {
        mAdapter = new VideoMaterialAdapter(this, uiType);
        mPageListLayout.setAdapter(mAdapter);
        mPageListLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mPageListLayout.setItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, Utils.dp2px(this, 5)));
        mPageListLayout.setEnableRefresh(true);
        mPageListLayout.setIsLoadMoreEnable(true);
        mPageListLayout.setIsReloadWhenEmpty(true);
        mPageListLayout.showFooter(true);
        mPageListLayout.setEmptyTipText("暂无视频素材~");
    }

    @OnClick({
            R.id.iv_material_back,
            R.id.tv_material_upload})
    public void onClick(View view) {
        if (System.currentTimeMillis() - lastClickTime < CLICK_INTERVAL) {
            lastClickTime = System.currentTimeMillis();
            return;
        }
        lastClickTime = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.iv_material_back:
                this.finish();
                break;
            case R.id.tv_material_upload:
                if (PermissionUtils.checkPermissions(this, Manifest.permission.CAMERA)) {
                    startActivity(new Intent(this, ScanQrCodeActivity.class));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            setSwipeBackEnable(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_PERMISSION_CODE:
                if (grantResults != null && grantResults.length > 0) {
                    for (int i : grantResults) {
                        if (i == PackageManager.PERMISSION_DENIED) {
                            ToastHelper.showToast("请打开摄像头权限~");
                            return;
                        } else {
                            startActivity(new Intent(this, ScanQrCodeActivity.class));
                        }
                    }
                } else {
                    ToastHelper.showToast("请打开摄像头权限~");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showMaterialList(MaterialListResponseBean responseBean) {
        if (responseBean == null) {
            if (NetworkUtil.isNetworkOk(this)) {
                mPageListLayout.showEmpty();
            } else {
                mPageListLayout.showError();
                ToastHelper.showToast("断网啦，点击重试~");
            }
            return;
        }
        mResultListener.onSuccess(responseBean);
    }

    @Override
    public void deleteMaterialCallback(boolean isDelete, String msg) {
        if (isDelete) {
            mAdapter.remove(deletePosition);
            if (mAdapter.getData().size() == 0) {
                mPageListLayout.showEmpty();
            }
        }
        ToastHelper.showToast(msg);
    }

    @Override
    public void showMaterialCount(String materialCount) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
