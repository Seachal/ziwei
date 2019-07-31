package com.laka.live.ui.shopping;

import android.graphics.LinearGradient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.config.SystemConfig;
import com.laka.live.dao.DbManger;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.activity.ShoppingCarActivity;
import com.laka.live.shopping.adapter.ShoppingHomeAdapter;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingCartCount;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingHome;
import com.laka.live.shopping.bean.newversion.ShoppingHomeBean;
import com.laka.live.shopping.bean.newversion.ShoppingHomeCateBean;
import com.laka.live.shopping.bean.newversion.ShoppingHomeTitleBean;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.fragment.BaseFragment;
import com.laka.live.ui.search.SearchGoodsActivity;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.qiyukf.unicorn.api.Unicorn;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import laka.live.bean.ChatMsg;
import laka.live.bean.ChatSession;

/**
 * @ClassName: ShoppingHomeFragment
 * @Description: 商场首页
 * @Author: chuan
 * @Version: 1.0
 * @Date: 14/07/2017
 */

public class ShoppingHomeFragment extends BaseFragment implements PageListLayout.OnRequestCallBack,
        View.OnClickListener, IHttpCallBack, EventBusManager.OnEventBusListener {
    private final static String TAG = ShoppingHomeFragment.class.getSimpleName();

    private PageListLayout mPageListLayout;
    private ShoppingHomeAdapter mAdapter;

    private TitleBarActionItem mShoppingCar, ivService;

    private List<Object> mDatas = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTheme(R.style.NoTranslucentActivityTheme);
        EventBusManager.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_home, null);

        view.findViewById(R.id.search_iv).setOnClickListener(this);
//        view.findViewById(R.id.service_iv).setOnClickListener(this);

        ivService = (TitleBarActionItem) view.findViewById(R.id.service_iv);
        ivService.setDrawable(ResourceHelper.getDrawable(R.drawable.nav_icon_service));
        ivService.setOnClickListener(this);

        mShoppingCar = (TitleBarActionItem) view.findViewById(R.id.shopping_car_tbai);
        mShoppingCar.setDrawable(ResourceHelper.getDrawable(R.drawable.nav_icon_shop));
        mShoppingCar.setOnClickListener(this);

        mPageListLayout = (PageListLayout) view.findViewById(R.id.page_list_layout);

        mAdapter = new ShoppingHomeAdapter(getContext());
        mDatas.clear();
        mAdapter.setData(mDatas);

        mPageListLayout.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mPageListLayout.setAdapter(mAdapter);
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.setIsReloadWhenEmpty(true);
        mPageListLayout.setIsLoadMoreEnable(false);

        mPageListLayout.loadData();

        refreshUnread();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (AccountInfoManager.getInstance().checkUserIsLogin()) {
            queryShoppingCartCount();
            refreshUnread();
        } else {
            updateShoppingCartNum(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBusManager.unregister(this);
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        ShoppingRequest.getInstance().getShoppingHome(this);
        queryShoppingCartCount();
        return null;
    }

    private void queryShoppingCartCount() {
        ShoppingRequest.getInstance().getShoppingCart(new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (obj == null || !(obj instanceof JTBShoppingCartCount)) {
                    return;
                }

                updateShoppingCartNum(((JTBShoppingCartCount) obj).getData().getQuantity());
            }

            @Override
            public void onError(String errorStr, int code) {
                updateShoppingCartNum(0);
            }
        });
    }

    @Override
    public <T> void onSuccess(T obj, String result) {
        mPageListLayout.onFinishLoading(false, false);
        mPageListLayout.setOnLoadMoreComplete();
        mPageListLayout.setOnRefreshComplete();

        if (obj == null || !(obj instanceof JTBShoppingHome)) {
            mPageListLayout.showEmpty();
            return;
        }

        ShoppingHomeBean shoppingHomeBean = ((JTBShoppingHome) obj).getData();

        if (shoppingHomeBean == null) {
            mPageListLayout.showEmpty();
            return;
        }

        mDatas.clear();

        if (!Utils.listIsNullOrEmpty(shoppingHomeBean.getGoodsCates())) {
            mDatas.add(new ShoppingHomeCateBean(shoppingHomeBean.getGoodsCates()));
        }

        if (!Utils.listIsNullOrEmpty(shoppingHomeBean.getHotGoods())) {
            ShoppingHomeTitleBean titleBean = new ShoppingHomeTitleBean();
            titleBean.setType(ShoppingHomeTitleBean.TYPE_HOT_GOODS);
            mDatas.add(titleBean);
            mDatas.addAll(shoppingHomeBean.getHotGoods());
        }

        if (!Utils.listIsNullOrEmpty(shoppingHomeBean.getTopics())) {
            ShoppingHomeTitleBean titleBean = new ShoppingHomeTitleBean();
            titleBean.setType(ShoppingHomeTitleBean.TYPE_TOPICS);
            mDatas.add(titleBean);
            mDatas.addAll(shoppingHomeBean.getTopics());
        }

        if (Utils.listIsNullOrEmpty(mDatas)) {
            mPageListLayout.showEmpty();
        } else {
            mAdapter.notifyDataSetChanged();
            mPageListLayout.showData();
        }
    }

    @Override
    public void onError(String errorStr, int code) {
        Log.d(TAG, "getShoppingHome failed . errorStr : " + errorStr + " ; code : " + code);
        ToastHelper.showToast(R.string.homepage_network_error_retry);

        mPageListLayout.showNetWorkError();
        mPageListLayout.onFinishLoading(false, false);
        mPageListLayout.setOnLoadMoreComplete();
        mPageListLayout.setOnRefreshComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        switch (event.tag) {
            case SubcriberTag.UPDATE_GOODS_CAR_COUNT:
//                updateShoppingCartNum((int) event.event);
                queryShoppingCartCount();
                break;
            case SubcriberTag.RECEIVE_CHAT_MSG:
                ChatMsg msg = (ChatMsg) event.event;
                if(msg.getUserId().equals(SystemConfig.getInstance().getKefuID())){
                    Log.d(TAG," 需要refreshUnread");

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshUnread();
                        }
                    },1500);
                }else{
                    Log.d(TAG," 不需要refreshUnread");
                }
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service_iv:
                handleOnServiceClick();
                break;
            case R.id.search_iv:
                handleOnSearchClick();
                break;
            case R.id.shopping_car_tbai:
                handleOnShoppingCarClick();
                break;
            default:
                Log.d(TAG, "unhandled click : " + v.toString());
                break;
        }
    }

    /**
     * 处理客服按钮的点击事件
     */
    private void handleOnServiceClick() {
        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            goLogin();
            return;
        }
//        if(session!=null){
//            session.setUnreadCnt(0);
////            DbManger.getInstance().updateSession(session);
//        }
       // refreshUnread();
        updateServiceNum(0);
        // TODO: 2017/7/31 客服
        Log.d(TAG, "handle on service click");

        ChatMessageActivity.startActivity(getActivity());
    }

    /**
     * 处理搜索按钮的点击事件
     */
    private void handleOnSearchClick() {
        Log.d(TAG, "handle on search click");
        SearchGoodsActivity.startActivity(mContext);
    }

    /**
     * 处理购物车的点击事件
     */
    private void handleOnShoppingCarClick() {
        Log.d(TAG, "handle on shopping car click");
        ShoppingCarActivity.startActivity(mContext);
    }

    private void updateShoppingCartNum(int count) {
        if (mShoppingCar != null) {
            mShoppingCar.setRedTipVisibility(count > 0);
            mShoppingCar.setRedTipText(count > 99 ? "99+" : String.valueOf(count));
        }
    }

    private void updateServiceNum(int count) {
        Log.d(TAG," updateServiceNum count="+count);
        if (ivService != null) {
            ivService.setRedTipVisibility(count > 0);
            ivService.setRedTipText(count > 99 ? "99+" : String.valueOf(count));
        }
    }

   // ChatSession session;
    private void refreshUnread() {
        updateServiceNum(Unicorn.getUnreadCount());
//          session = DbManger.getInstance().getSessionBySessionId(AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + SystemConfig.getInstance().getKefuID());
//        if(session!=null){
//            updateServiceNum(session.getUnreadCnt());
//        }
    }




}
