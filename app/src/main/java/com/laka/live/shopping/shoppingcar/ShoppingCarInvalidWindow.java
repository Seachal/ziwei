package com.laka.live.shopping.shoppingcar;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.laka.live.R;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.bean.ShoppingCarParamBean;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.NotificationCenter;
import com.laka.live.shopping.framework.adapter.MenuItemIdDef;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.framework.ui.DefaultTitleBar;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.shopping.shoppingcar.adapter.ShoppingCarInvalidAdapter;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiawei on 2016/1/7.
 */
public class ShoppingCarInvalidWindow extends DefaultWindow {
    private ListView mInvalidListView;
    private ShoppingCarInvalidAdapter mShoppingCarInvalidAdapter;
    private List<ShoppingCarGoodsBean> mShoppingCarGoodsBeans = new ArrayList<>();

    public ShoppingCarInvalidWindow(Context context, IDefaultWindowCallBacks callBacks) {
        super(context, callBacks);
        setLaunchMode(LAUNCH_MODE_SINGLE_INSTANCE);
        setTitle(R.string.shopping_car_invalid_title);
    }

    private void initActionBar() {
        ArrayList<TitleBarActionItem> actionList = new ArrayList<TitleBarActionItem>(1);
        TitleBarActionItem item = new TitleBarActionItem(getContext());
        item.setText(ResourceHelper.getString(R.string.shopping_category_clear));
        item.setItemId(MenuItemIdDef.TITLEBAR_SHOPPING_CLEAR);
        actionList.add(item);

        DefaultTitleBar titleBar = (DefaultTitleBar) getTitleBar();
        titleBar.setActionItems(actionList);
    }

    private void initUI() {
        View view = View.inflate(getContext(), R.layout.shopping_car_invalid_main, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());
        mInvalidListView = (ListView) findViewById(R.id.lv_shopping_car_invalid);
        mShoppingCarInvalidAdapter = new ShoppingCarInvalidAdapter(getContext(), mShoppingCarGoodsBeans);
        mInvalidListView.setAdapter(mShoppingCarInvalidAdapter);
        mInvalidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleTextDialog dialog = new SimpleTextDialog(getContext());
                dialog.setText(getResources().getString(R.string.shopping_car_invalid_tip));
                dialog.addYesNoButton();
                dialog.setRecommendButton(GenericDialog.ID_BUTTON_NO);
                dialog.setOnClickListener(new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            closeWindow();
                        }
                        return false;
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onTitleBarActionItemClick(int itemId) {
        if (itemId == MenuItemIdDef.TITLEBAR_SHOPPING_CLEAR) {
            if (mShoppingCarGoodsBeans.size() > 0) {
                List<ShoppingCarParamBean> shoppingCarParamBeans = new ArrayList<>();
                for (ShoppingCarGoodsBean s : mShoppingCarGoodsBeans) {
                    ShoppingCarParamBean shoppingCarParamBean = new ShoppingCarParamBean();
                    shoppingCarParamBean.setId(StringUtils.parseInt(s.getItemId()));
                    shoppingCarParamBeans.add(shoppingCarParamBean);
                }
                new ShoppingCarRequest(shoppingCarParamBeans).startShoppingCarDeleteRequest(new ShoppingCarRequest.ShoppingCarRequestResultCallback() {
                    @Override
                    public void onResultFail(int errorType) {
                        handlerGetDataFailed(CommonConst.ERROR_TYPE_NETWORK);
                    }

                    @Override
                    public void onResultSuccess() {
                        mShoppingCarGoodsBeans.clear();
                        mShoppingCarInvalidAdapter.notifyDataSetChanged();
                        ToastHelper.showToast("已清空过期商品");
                        ((Activity) mContext).finish();
                    }
                });
            }
        }
    }

    /**
     * 数据异常处理
     *
     * @param errorCode
     */
    private void handlerGetDataFailed(int errorCode) {
        if (errorCode == CommonConst.ERROR_TYPE_NETWORK) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.homepage_network_error_retry),
                    Toast.LENGTH_SHORT);
            return;
        }
    }

    public void setShoppingCarGoodsBeans(List<ShoppingCarGoodsBean> shoppingCarGoodsBeans) {
        this.mShoppingCarGoodsBeans = shoppingCarGoodsBeans;
        initUI();
        initActionBar();
        mShoppingCarInvalidAdapter.notifyDataSetChanged();
    }

    private void closeWindow() {
        mCallBacks.onWindowExitEvent(this, true);
    }
}
