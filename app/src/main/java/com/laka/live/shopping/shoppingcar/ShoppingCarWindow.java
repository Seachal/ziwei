package com.laka.live.shopping.shoppingcar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveActivityManager;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.CommunityConstant;
import com.laka.live.shopping.activity.OrderActivity;
import com.laka.live.shopping.activity.ShoppingCarInvalidActivity;
import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.bean.ShoppingCarGoodsState;
import com.laka.live.shopping.bean.ShoppingCarParamBean;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.NotificationCenter;
import com.laka.live.shopping.framework.adapter.MenuItemIdDef;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.framework.ui.DefaultTitleBar;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.shopping.network.HttpCode;
import com.laka.live.shopping.order.model.OrderBalanceInfo;
import com.laka.live.shopping.shoppingcar.adapter.ShoppingCarItemAdapter;
import com.laka.live.shopping.shoppingcar.adapter.ShoppingCarTopViewAdapter;
import com.laka.live.shopping.shoppingcar.model.ShoppingCarTopViewInfo;
import com.laka.live.shopping.shoppingcar.widget.ShoppingCarTopTextView;
import com.laka.live.shopping.widget.PriceView;
import com.laka.live.shopping.widget.LoadingLayout;
import com.laka.live.shopping.widget.SwipeListView;
import com.laka.live.ui.activity.MainActivity;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenjiawei on 2016/1/4.
 */
public class ShoppingCarWindow extends DefaultWindow implements View.OnClickListener {

    private LoadingLayout mLoadingLayout;
    private ShoppingCarRequest mShoppingCarRequest;
    private SwipeListView mGoodsListView;
    private ShoppingCarItemAdapter mShoppingCarItemAdapter;
    private List<ShoppingCarGoodsBean> mCarGoodsList = new ArrayList<>();
    private CheckBox mCheckBoxAll;
    private PriceView mGoodsCostAll;
    private TextView mOperationBtn;
    private LinearLayout mShoppingCarBottomArea;
    private List<ShoppingCarGoodsBean> mShoppingCarGoodsChangedBeans;
    private boolean mIsEditMode;
    private boolean mIsEditModeIsClick = true;
    private ArrayList<ShoppingCarGoodsBean> mGoodsInvalidList = new ArrayList<>();
    private TextView mInvalidGoods;
    private LinearLayout mInvalidGoodsArea;
    private LinearLayout mTopViewArea;
    private ShoppingCarTopTextView shoppingCarTopTextView;
    private View mFootView;
    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00");


    private List<ShoppingCarTopViewInfo> mInfoList = new ArrayList<>();

    public ShoppingCarWindow(Context context, IDefaultWindowCallBacks callBacks) {
        super(context, callBacks);
        setLaunchMode(LAUNCH_MODE_SINGLE_INSTANCE);
        setTitle(R.string.shopping_car_title);
        mShoppingCarRequest = new ShoppingCarRequest();
        initActionBar();
        initUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shopping_car_invalid:
                // 跳到商品失效列表
                ShoppingCarInvalidActivity.startActivity(mContext, mGoodsInvalidList);
                break;
            case R.id.tv_shopping_car_operation:
                operation(); // 结算或删除
                break;
            default:
                break;
        }
    }

    private void operation() {

        if (mIsEditMode) {
            //删除购物车操作
            if (mCarGoodsList.size() > 0) {
                mIsEditModeIsClick = false;
                List<ShoppingCarParamBean> shoppingCarParamBeanList = new ArrayList<>();

                for (int i = 0; i < mCarGoodsList.size(); i++) {

                    boolean isChecked = ShoppingCarState.isSelected.get(i);
                    if (isChecked) {
                        ShoppingCarParamBean s = new ShoppingCarParamBean();
                        s.setId(StringUtils.parseInt(mCarGoodsList.get(i).getItemId()));
                        shoppingCarParamBeanList.add(s);
                    }
                }
                sendShoppingCarDeleteRequest(shoppingCarParamBeanList);
            }
            mShoppingCarItemAdapter.notifyDataSetChanged();
        } else {
            if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                if (encapsulation().getList().size() == 0) {
                    ToastHelper.showToast("请选择要结算的商品");
                } else {
                    OrderActivity.startActivity(mContext, encapsulation());
                }
            } else {
                AccountInfoManager.getInstance().tryOpenLoginWindow();
            }
        }

    }

    @Override
    public void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_ON_DETACH) {
            shoppingCarTopTextView.stop();
        } else if (stateFlag == STATE_BEFORE_POP_OUT) {
            ShoppingCarState.isSelected.clear();
        } else if (stateFlag == STATE_AFTER_PUSH_IN) {
            mGoodsInvalidList.clear();
            shoppingCarTopTextView.start();
            sendGetShoppingCarListRequest();
        } else if (stateFlag == STATE_ON_SHOW) {
            if (!isInEditMode()) {
                sendGetShoppingCarListRequest();
            }
        } else if (stateFlag == STATE_ON_HIDE) {
            shoppingCarTopTextView.stop();
        }
    }

    @Override
    public void onTitleBarActionItemClick(int itemId) {
        if (itemId == MenuItemIdDef.TITLEBAR_EDIT) {
            if (mIsEditModeIsClick) {
                handleEditClick();
            }
        }
    }

//    @Override
//    publicboolean onWindowBackKeyEvent() {
//        boolean editState = isInEditMode();
//        if (editState) {
//            handleEditClick();
//            return true;
//        }
//        return super.onWindowBackKeyEvent();
//    }

    public void handleEditClick() {

        int selectedCount = 0;

        for (int i = 0; i < mCarGoodsList.size(); i++) {
            boolean isChecked = ShoppingCarState.isSelected.get(i);
            if (isChecked) {
                ++selectedCount;
            }
        }

        mIsEditMode = isInEditMode();

        if (mIsEditMode) {
            exitEditState();
            mOperationBtn.setCompoundDrawables(null, null, null, null);
            mOperationBtn.setText(ResourceHelper.getString(R.string.shopping_car_goods_operation_account) + "(" + selectedCount + ")");
            mShoppingCarItemAdapter.setPageState(ShoppingCarConstant.GOODSPAGE);
            mShoppingCarItemAdapter.notifyDataSetChanged();
            if (mShoppingCarGoodsChangedBeans != null) {
                sendShoppingCarUpdateRequest(mShoppingCarGoodsChangedBeans);
            }
        } else {
            enterEditState();
            mOperationBtn.setText(ResourceHelper.getString(R.string.shopping_car_goods_operation_delete) + "(" + selectedCount + ")");
            mShoppingCarItemAdapter.setPageState(ShoppingCarConstant.OPERATIONPAGE);
            mShoppingCarItemAdapter.notifyDataSetChanged();
        }
        mIsEditMode = isInEditMode();
        String text = ResourceHelper.getString(R.string.collect_edit);
        if (mIsEditMode) {
            text = ResourceHelper.getString(R.string.save);
        }
        TitleBarActionItem item = getTitleBarInner().getActionBar().getItem(MenuItemIdDef.TITLEBAR_EDIT);
        item.setText(text);
    }

    private void initActionBar() {
        ArrayList<TitleBarActionItem> actionList = new ArrayList<>(1);
        TitleBarActionItem item = new TitleBarActionItem(getContext());
        item.setText(ResourceHelper.getString(R.string.collect_edit));
        item.setItemId(MenuItemIdDef.TITLEBAR_EDIT);
        actionList.add(item);
        DefaultTitleBar titleBar = (DefaultTitleBar) getTitleBar();
        titleBar.setActionItems(actionList);
    }

    public void initUI() {
        View view = View.inflate(getContext(), R.layout.shopping_car_main, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());
        mGoodsListView = (SwipeListView) findViewById(R.id.lv_shopping_car);
        mLoadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);
        mLoadingLayout.setDefaultLoading();
        mCheckBoxAll = (CheckBox) findViewById(R.id.cb_shopping_car_all);
        mCheckBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = mCheckBoxAll.isChecked();
                double sumCost = 0;
                for (int i = 0; i < mCarGoodsList.size(); i++) {
                    ShoppingCarState.isSelected.put(i, isChecked);
                    sumCost += Double.valueOf(mCarGoodsList.get(i).getSalePrice());
                }
                if (isChecked) {
                    changeBtnOperation(mCarGoodsList.size());
                    mGoodsCostAll.setText(mDecimalFormat.format(sumCost));
                } else {
                    mGoodsCostAll.setText("0.00");
                    changeBtnOperation(0);
                }

                mShoppingCarItemAdapter.notifyDataSetChanged();
            }
        });
        mGoodsCostAll = (PriceView) findViewById(R.id.tv_shopping_car_cost);
        mOperationBtn = (TextView) findViewById(R.id.tv_shopping_car_operation);
        mOperationBtn.setOnClickListener(this);
        mShoppingCarBottomArea = (LinearLayout) findViewById(R.id.ll_shopping_car_bottom);
        mShoppingCarBottomArea.setVisibility(GONE);
        mShoppingCarItemAdapter = new ShoppingCarItemAdapter(getContext(), mCarGoodsList);
        mShoppingCarItemAdapter.setOnGoodsCostPristListener(new ShoppingCarItemAdapter.OnGoodsCostPriseListener() {
            @Override
            public void onChange() {
                double sumCost = 0;
                boolean isCkAll = true;
                int checkCount = 0;
                for (int i = 0; i < mCarGoodsList.size(); i++) {
                    boolean ckState = ShoppingCarState.isSelected.get(i);
                    if (ckState) {
                        sumCost += (Double.valueOf(mCarGoodsList.get(i).getSalePrice()) * Integer.valueOf(mCarGoodsList.get(i).getGoodsCount()));
                        checkCount++;
                    } else {
                        isCkAll = false;
                    }
                }
                mCheckBoxAll.setChecked(isCkAll);
                mGoodsCostAll.setText(mDecimalFormat.format(sumCost));
                changeBtnOperation(checkCount);
                mShoppingCarItemAdapter.notifyDataSetChanged();
            }
        });
        mShoppingCarItemAdapter.setOnGoodsOperationListener(new ShoppingCarItemHolder.OnGoodsOperationListener() {
            @Override
            public void onGoodsOperationChange(List<ShoppingCarGoodsBean> mShoppingCarGoodsBeans) {
                setShoppingCarGoodsChangedBeans(mShoppingCarGoodsBeans);
            }
        });
        mFootView = View.inflate(getContext(), R.layout.shopping_car_invalid_goods_main, null);
        mInvalidGoods = (TextView) mFootView.findViewById(R.id.invalid_goods_title);
        mInvalidGoodsArea = (LinearLayout) mFootView.findViewById(R.id.ll_shopping_car_invalid);
        mInvalidGoodsArea.setOnClickListener(this);
        mGoodsListView.addFooterView(mFootView);
        mGoodsListView.setAdapter(mShoppingCarItemAdapter);

        shoppingCarTopTextView = (ShoppingCarTopTextView) findViewById(R.id.shopping_car_top_view);
        mTopViewArea = (LinearLayout) findViewById(R.id.shopping_car_top_view_area);
        mTopViewArea.setVisibility(GONE);

        initListView();
    }

    private void initListView() {

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(mContext);
                openItem.setBackground(new ColorDrawable(Color.parseColor("#ff4a57")));
                openItem.setWidth(ResourceHelper.getDimen(R.dimen.space_80));
                openItem.setTitle("删除");
                openItem.setTitleSize(14);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };

        mGoodsListView.setMenuCreator(creator);
        mGoodsListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                sendShoppingCarDeleteRequest(position);
                return false;
            }
        });

        mGoodsListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        mGoodsListView.setOnItemPositionClickListener(new SwipeListView.onItemPositionClickListener() {
            @Override
            public void onClickPosition(int position) {
                String goodId = mCarGoodsList.get(position).getGoodsId();
                ShoppingGoodsDetailActivity.startActivity(mContext, Integer.parseInt(goodId));
            }
        });
    }

    private void initTopViewData(List<String> infoList) {
        for (String s : infoList) {
            mInfoList.add(new ShoppingCarTopViewInfo(s));
        }

        // 为最少改动到业务逻辑，这里必须要添加两条，才能实现垂直滚动
        if (mInfoList.isEmpty()) {
            mInfoList.add(new ShoppingCarTopViewInfo("她他社优惠进行中欢迎选购"));
            mInfoList.add(new ShoppingCarTopViewInfo("她他社优惠进行中欢迎选购"));
        }

        ShoppingCarTopViewAdapter shoppingCarTopViewAdapter = new ShoppingCarTopViewAdapter(mInfoList);
        shoppingCarTopTextView.setAdapter(shoppingCarTopViewAdapter);
        shoppingCarTopTextView.start();

        shoppingCarTopTextView.setOnTextShowCallback(new ShoppingCarTopTextView.OnTextShowCallback() {
            @Override
            public void onShow() {
                mTopViewArea.setVisibility(VISIBLE);
            }
        });
        shoppingCarTopTextView.performSwitch();
    }

    private OrderBalanceInfo encapsulation() {
        OrderBalanceInfo balanceInfo = new OrderBalanceInfo();
        List<ShoppingCarGoodsBean> checkList = new ArrayList<>();
        for (int i = 0; i < mCarGoodsList.size(); i++) {
            if (ShoppingCarState.isSelected.get(i)) {
                checkList.add(mCarGoodsList.get(i));
            }
        }
        balanceInfo.setList(checkList);
        return balanceInfo;
    }

    private void handleGetDataSuccess(ShoppingCarGoodsState shoppingCarGoodsState) {

        if (shoppingCarGoodsState.getMessage() != null) {
            //初始化购物车顶部信息滚动栏
            initTopViewData(shoppingCarGoodsState.getMessage());
        }

        // 切换成货源模式(删除成功后)
        mShoppingCarItemAdapter.setPageState(ShoppingCarConstant.GOODSPAGE);

        if (Utils.isEmpty(shoppingCarGoodsState.getValidGoods()) && Utils.isEmpty(shoppingCarGoodsState.getInvalidGoods())) {
            setEmptyCar();
            return;
        }

        setActionBarVisible(VISIBLE);

        mLoadingLayout.setVisibility(GONE);

        if (shoppingCarGoodsState.getValidGoods() != null) {
            mShoppingCarBottomArea.setVisibility(VISIBLE);
            mCarGoodsList.clear();
            mCarGoodsList.addAll(shoppingCarGoodsState.getValidGoods());
            double goodsCostAll = 0;
            for (ShoppingCarGoodsBean s : mCarGoodsList) {
                goodsCostAll += (Double.valueOf(s.getSalePrice()) * Integer.valueOf(s.getGoodsCount()));
            }
            mGoodsCostAll.setText(mDecimalFormat.format(goodsCostAll));
            mCheckBoxAll.setChecked(true);
            changeBtnOperation(mCarGoodsList.size());
            //初始化购物车选中状态
            ShoppingCarState.initCheckBoxState(mCarGoodsList.size());
        }

        if (mGoodsInvalidList.size() == 0) {
            mGoodsListView.removeFooterView(mFootView);
        } else {
            mInvalidGoods.setText(mGoodsInvalidList.size() + "件商品失效");
        }
        mShoppingCarItemAdapter.notifyDataSetChanged();
    }

    public void setEmptyCar() {

        setActionBarVisible(GONE);
        mLoadingLayout.setVisibility(VISIBLE);
        mShoppingCarBottomArea.setVisibility(GONE);
        mLoadingLayout.setBgContent(R.drawable.public_pic_empty,
                ResourceHelper.getString(R.string.shopping_car_no_goods_tip), false);
//        mLoadingLayout.setText(ResourceHelper.getString(R.string.shopping_car_go_to_see));
//        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
//            @Override
//            public void onClick() {
//                // 返回商城事件
//                EventBusManager.postEvent(0, SubcriberTag.OPEN_MALL_TAB);
//                LiveActivityManager.getInstance().popUntilActivity(MainActivity.class);
//            }
//        });
        if (!mIsEditModeIsClick) {
            handleEditClick();
            mIsEditModeIsClick = true;
        }
        mCarGoodsList.clear();
        mShoppingCarItemAdapter.notifyDataSetChanged();
    }

    /**
     * 数据异常处理
     *
     * @param errorCode
     */
    private void handlerGetDataFailed(int errorCode) {

        if (errorCode == CommunityConstant.ERROR_TYPE_DATA) {
            mLoadingLayout.setDefaultDataError(true);
            ToastHelper.showToast(ResourceHelper.getString(R.string.homepage_data_error), Toast
                    .LENGTH_SHORT);
            mShoppingCarBottomArea.setVisibility(GONE);
            return;
        }

        if (errorCode == HttpCode.ERROR_NETWORK) {
            mLoadingLayout.setDefaultNetworkError(true);
            mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
                @Override
                public void onClick() {
                    sendGetShoppingCarListRequest();
                }
            });
            ToastHelper.showToast(ResourceHelper.getString(R.string.homepage_network_error_retry),
                    Toast.LENGTH_SHORT);
            mShoppingCarBottomArea.setVisibility(GONE);
            return;
        }

        if (errorCode == HttpCode.ERROR_CODE_FAILD_OPT) {
            ToastHelper.showToast("请求失败");
        } else if (errorCode == HttpCode.ERROR_CODE_ILLEGAL) {
            ToastHelper.showToast("非法参数");
        }

    }


    private void sendGetShoppingCarListRequest() {

        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            return;
        }

        mShoppingCarRequest.startRequest(new ShoppingCarRequest.RequestResultCallback() {
            @Override
            public void onResultFail(int errorType) {
                handlerGetDataFailed(errorType);
            }

            @Override
            public void onResultSuccess(ShoppingCarGoodsState shoppingCarGoodsData) {

                if (shoppingCarGoodsData.getInvalidGoods() != null) {
                    mGoodsInvalidList.clear();
                    mGoodsInvalidList.addAll(shoppingCarGoodsData.getInvalidGoods());
                }

                handleGetDataSuccess(shoppingCarGoodsData);
            }
        });
    }

    private void sendShoppingCarUpdateRequest(List<ShoppingCarGoodsBean> mShoppingCarGoodsBeans) {
        List<ShoppingCarParamBean> shoppingCarParamBeans = new ArrayList<>();
        for (ShoppingCarGoodsBean s : mShoppingCarGoodsBeans) {
            ShoppingCarParamBean shoppingCarParamBean = new ShoppingCarParamBean();
            shoppingCarParamBean.setId(StringUtils.parseInt(s.getItemId()));
            shoppingCarParamBean.setCount(StringUtils.parseInt(s.getGoodsCount()));
            shoppingCarParamBeans.add(shoppingCarParamBean);
        }
        ShoppingCarRequest shoppingCarRequest = new ShoppingCarRequest(shoppingCarParamBeans);
        shoppingCarRequest.startShoppingCarUpdateRequest(new ShoppingCarRequest.ShoppingCarRequestResultCallback() {
            @Override
            public void onResultFail(int errorType) {
                handlerGetDataFailed(errorType);
            }

            @Override
            public void onResultSuccess() {
                // 刷新购物车的数量
                EventBusManager.postEvent(0, SubcriberTag.UPDATE_GOODS_CAR_COUNT);
            }
        });
    }

    private void sendShoppingCarDeleteRequest(int position) {
        List<ShoppingCarParamBean> shoppingCarParamBeanList = new ArrayList<>();
        ShoppingCarParamBean s = new ShoppingCarParamBean();
        s.setId(StringUtils.parseInt(mCarGoodsList.get(position).getItemId()));
        shoppingCarParamBeanList.add(s);
        sendShoppingCarDeleteRequest(shoppingCarParamBeanList);
    }

    private void sendShoppingCarDeleteRequest(final List<ShoppingCarParamBean> shoppingCarParamBeanList) {

        mShoppingCarRequest = new ShoppingCarRequest(shoppingCarParamBeanList);
        mShoppingCarRequest.startShoppingCarDeleteRequest(new ShoppingCarRequest.ShoppingCarRequestResultCallback() {
            @Override
            public void onResultFail(int errorType) {
                handlerGetDataFailed(errorType);
            }

            @Override
            public void onResultSuccess() {


                Iterator<ShoppingCarGoodsBean> iterator = mCarGoodsList.iterator();

                int index = 0;

                while (iterator.hasNext()) {

                    ShoppingCarGoodsBean goodsBean = iterator.next();

                    for (ShoppingCarParamBean temp : shoppingCarParamBeanList) {
                        if (goodsBean.getItemId().equals(String.valueOf(temp.getId()))) {
                            iterator.remove();
                            mShoppingCarItemAdapter.onItemDelete(index, mCheckBoxAll.isChecked());
                            break;
                        }
                    }

                    ++index;
                }

                // 退出编辑模式
                if (isInEditMode()) {
                    mIsEditModeIsClick = true;
                    handleEditClick();
                }

                mShoppingCarItemAdapter.onChange();
                mShoppingCarItemAdapter.notifyDataSetChanged();

                if (mCarGoodsList.isEmpty() && mGoodsInvalidList.isEmpty()) {
                    setEmptyCar();
                }

                ToastHelper.showToast("删除成功");
                // 刷新购物车的数量
                EventBusManager.postEvent(0, SubcriberTag.UPDATE_GOODS_CAR_COUNT);
            }
        });
    }

    public void setShoppingCarGoodsChangedBeans(List<ShoppingCarGoodsBean> mShoppingCarGoodsChangedBeans) {
        this.mShoppingCarGoodsChangedBeans = mShoppingCarGoodsChangedBeans;
    }


    private void changeBtnOperation(int count) {
        if (!mIsEditMode) {
            mOperationBtn.setCompoundDrawables(null, null, null, null);
            mOperationBtn.setText(ResourceHelper.getString(R.string.shopping_car_goods_operation_account) + "(" + count + ")");
        } else {
            mOperationBtn.setText(ResourceHelper.getString(R.string.shopping_car_goods_operation_delete) + "(" + count + ")");
        }
    }

    // 右上角的按钮显示或隐藏
    private void setActionBarVisible(int visibility) {
        getTitleBarInner().getActionBar().getItem(MenuItemIdDef.TITLEBAR_EDIT).setVisibility(visibility);
    }

}
