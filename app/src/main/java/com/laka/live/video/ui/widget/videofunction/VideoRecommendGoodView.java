package com.laka.live.video.ui.widget.videofunction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.help.PostEvent;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.adapter.AdvertGoodsAdapter;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.event.MiniVideoEvent;
import com.laka.live.video.model.http.bean.VideoDetailResponseBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:小视频播放页面-商品推荐View
 */

public class VideoRecommendGoodView extends BaseVideoFunctionView implements View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyView)
    LinearLayout mLlEmptyView;

    /**
     * description:数据配置
     **/
    private List<ShoppingGoodsBaseBean> goodList;
    private List<ShoppingGoodsBaseBean> mData;
    private AdvertGoodsAdapter advertGoodsAdapter;
    private int videoId = -1;
    private boolean isReload = false;
    private GsonHttpConnection.OnResultListener resultListener;

    public VideoRecommendGoodView(Context context) {
        super(context);
    }

    public VideoRecommendGoodView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoRecommendGoodView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.view_recommend_goods;
    }

    @Override
    protected void initProperties() {
    }

    @Override
    protected void initView() {
        mData = new ArrayList<>();
        goodList = new ArrayList<>();
        mData.addAll(goodList);
        advertGoodsAdapter = new AdvertGoodsAdapter(mContext, videoId, false);
        advertGoodsAdapter.setData(mData);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(advertGoodsAdapter);
        advertGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        resultListener = new GsonHttpConnection.OnResultListener<VideoDetailResponseBean>() {
            @Override
            public void onSuccess(VideoDetailResponseBean responseBean) {
                if (mHelper != null && mHelper.isActivityRunning()) {
                    MiniVideoBean video = responseBean.getData();
                    if (!Utils.isEmpty(video.getRecommendGoodsList())) {
                        mLlEmptyView.setVisibility(GONE);
                        mData.addAll(video.getRecommendGoodsList());
                        advertGoodsAdapter.notifyDataSetChanged();
                    } else {
                        mLlEmptyView.setVisibility(VISIBLE);
                        if (isReload) {
                            ToastHelper.showToast("暂无商品推荐~");
                        }
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                if (mHelper != null && mHelper.isActivityRunning()) {
                    mLlEmptyView.setVisibility(VISIBLE);
                }
            }
        };
    }

    @OnClick({
            R.id.emptyView
    })
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emptyView:
                isReload = true;
                getRecommendDataList(mVideo);
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEvent(PostEvent event) {
        if (event.getEvent() instanceof MiniVideoEvent) {
            MiniVideoEvent videoEvent = (MiniVideoEvent) event.getEvent();
            switch (videoEvent.getTag()) {
                case VideoEventConstant.UPDATE_RECOMMEND_GOODS_LIST:
                    if (videoEvent.getTargetObj() != null && videoEvent.getTargetObj() instanceof VideoFunctionHelper) {
                        //判断对象唯一性
                        VideoFunctionHelper helper = (VideoFunctionHelper) videoEvent.getTargetObj();
                        //Logger.e("更新RecommendView：" + helper + "\n输出mHelper：" + mHelper);
                        if (helper == mHelper) {
                            mVideo = (MiniVideoBean) videoEvent.getEvent();
                            videoId = mVideo.getVideoId();
                            advertGoodsAdapter.setRelativeId(videoId);
                            isReload = false;
                            mData.clear();
//                            Logger.e("确保只有一个对象啊：" + VideoRecommendGoodView.this);
                            getRecommendDataList(mVideo);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void getRecommendDataList(MiniVideoBean videoBean) {
//        Logger.e("获取视频详情！！！！");
        DataProvider.getVideoDetailInfo(this, videoBean.getVideoId() + "", resultListener);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        resultListener = null;
    }
}
