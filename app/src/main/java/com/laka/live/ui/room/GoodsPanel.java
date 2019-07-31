package com.laka.live.ui.room;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.RecommendGoodsMsg;
import com.laka.live.msg.RoomGoodsListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.ShoppingConstant;
import com.laka.live.shopping.activity.OrderDetailActivity;
import com.laka.live.shopping.activity.ShoppingCarActivity;
import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.goods.GoodsBuyPanel;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * @ClassName: 类（或接口）名
 * @Description: 描述
 * @Author 关健
 * @Version 客户端版本
 * @Date 2017/7/26
 */

public class GoodsPanel extends RelativeLayout implements View.OnClickListener, EventBusManager.OnEventBusListener, PageListLayout.OnRequestCallBack
        , BaseAdapter.OnItemClickListener, PageListLayout.OnResultListener<RoomGoodsListMsg> {
    private String TAG = "GoodsPanel";
    private View view;
    protected Context context;
    private BaseActivity activity;
    private String courseId;
    private TextView tvGoodAmount;
    private Button btnCarAmount;
    private RelativeLayout rlCar;
    private PageListLayout mListLayout;
    private GoodsPanelAdapter mAdapter;


    public GoodsPanel(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public GoodsPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    private void initUI() {
        EventBusManager.register(this);
        view = LayoutInflater.from(context).inflate(R.layout.view_goods_panel, null);
        int height;
        height = Utils.getScreenHeight(context) * 2 / 3;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        addView(view, params);
        Log.d(TAG, " initUI height=" + height);
//        addView(view);
        tvGoodAmount = (TextView) view.findViewById(R.id.tv_good_amount);
        btnCarAmount = (Button) view.findViewById(R.id.btn_car_amount);
        btnCarAmount.setVisibility(View.GONE);
        rlCar = (RelativeLayout) view.findViewById(R.id.rl_car);
        rlCar.setOnClickListener(this);
        mListLayout = (PageListLayout) view.findViewById(R.id.listview);
        mListLayout.setIsReloadWhenEmpty(true);
        mAdapter = new GoodsPanelAdapter(context);
        mAdapter.setOnItemClickListener(this);
        mListLayout.setAdapter(mAdapter);
        mListLayout.setLayoutManager(new LinearLayoutManager(context));
        mListLayout.setOnRequestCallBack(this);
        mListLayout.setOnResultListener(this);
//        mListLayout.loadData();

    }

    public void initData(final BaseActivity activity, final String courseId) {
        this.activity = activity;
        this.courseId = courseId;

//        Log.d(TAG, "加载测试商品");
//
//        List<ShoppingGoodsDetailBean> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            ShoppingGoodsDetailBean item = new ShoppingGoodsDetailBean();
//            list.add(item);
//        }
//        mAdapter.addAll(list);
//        mListLayout.showData();
//        getRecommendGoods();
        mListLayout.loadData();
    }

    public void destory() {
        EventBusManager.unregister(this);
    }

    private void getRecommendGoods() {
        Log.d(TAG," getRecommendGoods");
        DataProvider.getRecommendGoods(courseId, new GsonHttpConnection.OnResultListener<RecommendGoodsMsg>() {
            @Override
            public void onSuccess(RecommendGoodsMsg recommendGoodsMsg) {
                mListLayout.setOnLoadMoreComplete();
                mListLayout.setOnRefreshComplete();
                if (curPage == 0) {
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                }
                if (recommendGoodsMsg.isSuccessFul()) {
                    ArrayList<ShoppingGoodsDetailBean> goods = recommendGoodsMsg.getList();
                    Log.d(TAG, " getRecommendGoods onSuccess goods=" + goods.size());
//                    btnGoods.setText(String.valueOf(goods.size()));
                    EventBusManager.postEvent(goods.size(), SubcriberTag.REFRESH_RECOMMEND_GOODS_COUNT);
                    mAdapter.addAll(goods);
                    mListLayout.showData();
                    mListLayout.onFinishLoading(false, false);
                } else {
                    Log.d(TAG, " getRecommendGoods onSuccess errorCode=" + recommendGoodsMsg.getCode());
                    if (Utils.listIsNullOrEmpty(mAdapter.getmDatas())) {
                        mListLayout.showEmpty();
                    }
                    mListLayout.onFinishLoading(false, false);
                }


            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mListLayout.setOnLoadMoreComplete();
                mListLayout.setOnRefreshComplete();
                mListLayout.onFinishLoading(false, false);
                Log.d(TAG, " getRecommendGoods onFail errorCode=" + errorCode);
                mListLayout.showError();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_car:
                EventBusManager.postEvent(0,SubcriberTag.SHOW_FLOAT_LIVE);
                ShoppingCarActivity.startActivity(activity);
                break;


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
//        if (SubcriberTag.SHOW_GOOD_BUY_PANEL.equals(event.tag)) {
//             //弹出规格
//
//        }
        if (SubcriberTag.UPDATE_GOODS_CAR_COUNT.equals(event.tag)) {
            int goodsCount = (int) event.event;
            Log.d(TAG, " UPDATE_GOODS_CAR_COUNT goodsCount=" + goodsCount);
            if (goodsCount == 0) {
                btnCarAmount.setVisibility(View.GONE);
            } else {
                btnCarAmount.setText(String.valueOf(goodsCount));
                btnCarAmount.setVisibility(View.VISIBLE);
            }
        } else if (SubcriberTag.SHOW_GOOD_BUY_PANEL.equals(event.tag)) {
            Log.d(TAG, " SHOW_GOOD_BUY_PANEL");
            mDetailBean = (ShoppingGoodsDetailBean) event.event;
            tryOpenGoodsBuyPanel(ShoppingConstant.PANEL_TYPE_BUY_CAR);
        }
    }

    private void tryOpenGoodsBuyPanel(int panelType) {
        Log.d(TAG, " tryOpenGoodsBuyPanel");
        if (mDetailBean == null) {
            return;
        }
        boolean isInvalidate = mDetailBean.getStatus() == CommonConst.GOODS_STATE_INVALIDATE;
        if (isInvalidate) {
            ToastHelper.showToast(R.string.shopping_goods_invalidate_tips);
            return;
        }
        if (mGoodsBuyPanel != null && mGoodsBuyPanel.isShowing()) {
            return;
        }
        mGoodsBuyPanel = new GoodsBuyPanel(activity, mDetailBean, panelType);
        mGoodsBuyPanel.setCourseId(Integer.parseInt(courseId));
        mGoodsBuyPanel.showPanel();
    }

    GoodsBuyPanel mGoodsBuyPanel;
    ShoppingGoodsDetailBean mDetailBean;

    @Override
    public void onResult(RoomGoodsListMsg followsListMsg) {

    }

    @Override
    public void onItemClick(int position) {
        EventBusManager.postEvent(0,SubcriberTag.SHOW_FLOAT_LIVE);
        Log.d(TAG," onItemClick position="+position);
        ShoppingGoodsDetailBean item = mAdapter.getItem(position);
        ShoppingGoodsDetailActivity.startActivity(getContext(),item.getGoodsId(),Integer.parseInt(courseId));
    }

    int curPage;
    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        curPage = page;
        getRecommendGoods();
        return null;
    }
}
