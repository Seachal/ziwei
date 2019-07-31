/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.shopping.bean.json2bean.JTBShoppingAddress;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.DeviceManager;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.NotificationCenter;
import com.laka.live.shopping.framework.adapter.MenuItemIdDef;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.framework.ui.DefaultTitleBar;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.order.model.OrderAddressInfo;
import com.laka.live.shopping.order.model.OrderRequest;
import com.laka.live.shopping.order.widget.OrderAreaPanel;
import com.laka.live.ui.widget.AlphaImageView;
import com.laka.live.ui.widget.dialog.LoadingDialog;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderAddressWindow extends DefaultWindow implements View.OnClickListener, OrderAreaPanel.IOrderAreaPanelListener {

    private final static String TAG = "OrderAddressWindow";

    private Activity mContext;
    private AbstractWindow mWindow;

    private EditText mEditName, mEditPhone, mEditAddress;
    private TextView mTextViewArea;
    private AlphaImageView ivNameDel, ivPhoneDel;

    private OrderAreaPanel mAreaPanel;

    private OrderRequest mOrderRequest;

    private LoadingDialog mDialog;

    private OrderAddressInfo mAddressInfo;

    private boolean isInit = true;

    public OrderAddressWindow(Context context, IDefaultWindowCallBacks callBacks, OrderAddressInfo orderAddressInfo) {
        super(context, callBacks);
        mContext = (Activity) context;
        mWindow = this;
        mAddressInfo = orderAddressInfo;

        initActionBar();
        initUI();
    }

    private void initUI() {
        setTitle(ResourceHelper.getString(R.string.order_address_title));
        View view = View.inflate(mContext, R.layout.order_address_add, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

        mTextViewArea = (TextView) findViewById(R.id.tv_area);
        mTextViewArea.setOnClickListener(this);
        mEditName = (EditText) findViewById(R.id.et_name);
        mEditPhone = (EditText) findViewById(R.id.et_phone);
        mEditAddress = (EditText) findViewById(R.id.et_address);
        ivNameDel = (AlphaImageView) findViewById(R.id.iv_name_del);
        ivNameDel.setOnClickListener(this);
        ivPhoneDel = (AlphaImageView) findViewById(R.id.iv_phone_del);
        ivPhoneDel.setOnClickListener(this);
        mDialog = new LoadingDialog(mContext);
        mDialog.setMessage(R.string.order_pay_load);

        String receiver = mAddressInfo.receiver;
        String mobile = mAddressInfo.mobile;
        String detailAddress = mAddressInfo.detailAddress;
        String phoneNumber = AccountInfoManager.getInstance().getPhoneNumber();

        if (!StringUtils.isEmpty(receiver)) {
            mEditName.setText(receiver);
        }
        if (!StringUtils.isEmpty(phoneNumber)) {
            mEditPhone.setText(phoneNumber);
        }
        if (!StringUtils.isEmpty(mobile)) {
            mEditPhone.setText(mobile);
        }
        if (!StringUtils.isEmpty(detailAddress)) {
            mEditAddress.setText(detailAddress);
        }
    }

    private void initActionBar() {
        ArrayList<TitleBarActionItem> actionList = new ArrayList<>(1);
        TitleBarActionItem item = new TitleBarActionItem(getContext());
        item.setItemId(MenuItemIdDef.TITLEBAR_ORDER_FINISH);
        item.setText(ResourceHelper.getString(R.string.order_address_save));
        actionList.add(item);
        DefaultTitleBar titleBar = (DefaultTitleBar) getTitleBar();
        titleBar.setActionItems(actionList);
    }

    private void notifyAddressChanged() {
        Notification notification = Notification.obtain(NotificationDef.N_ORDER_ADDRESS_CHANGED);
        NotificationCenter.getInstance().notify(notification);
    }

    @Override
    public void onSelected(String desc) {
        mTextViewArea.setText(desc);
    }

    @Override
    public void onTitleBarActionItemClick(int itemId) {
        if (itemId == MenuItemIdDef.TITLEBAR_ORDER_FINISH) {
            String receiver = mEditName.getText().toString();
            String phone = mEditPhone.getText().toString();
            String address = mEditAddress.getText().toString();
            if (StringUtils.isEmpty(receiver)) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.order_address_error1));
                return;
            }
            if (StringUtils.isEmpty(phone)) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.order_address_error2));
                return;
            }
            if (phone.length() != 11) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.order_address_error3));
                return;
            }

            if(Utils.isEmpty(mTextViewArea.getText().toString())) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.order_address_error4));
                return;
            }

            if (StringUtils.isEmpty(address)) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.order_address_error5));
                return;
            }
            mAddressInfo.mobile = phone;
            mAddressInfo.receiver = receiver;
            mAddressInfo.detailAddress = address;
            mAddressInfo.cityId = String.valueOf(mAreaPanel.getCurrentCityId());
            mAddressInfo.districtId = String.valueOf(mAreaPanel.getCurrentDistrictId());
            mAddressInfo.provinceId = String.valueOf(mAreaPanel.getCurrentProvinceId());
            addAddress(mAddressInfo);
        }
    }

    @Override
    protected void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
            mAreaPanel = new OrderAreaPanel(mContext);
            mAreaPanel.setPanelListener(this);
            mAreaPanel.initArea(mAddressInfo);
        }
    }

    @Override
    public void notify(Notification notification) {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_name_del) {
            mEditName.setText("");
        } else if (v.getId() == R.id.iv_phone_del) {
            mEditPhone.setText("");
        } else if (v.getId() == R.id.tv_area) {
            mAreaPanel.showPanel();
            if (isInit) {
                isInit = false;
                mAreaPanel.initArea(mAddressInfo);
            }
        }
    }

    /**
     * 保存地址
     * @param addressInfo
     */
    private void addAddress(OrderAddressInfo addressInfo) {
        mDialog.show();
        mOrderRequest = OrderRequest.getInstance();
        mOrderRequest.addAddress(addressInfo, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, result);
                }
                mDialog.dismiss();
                ToastHelper.showToast("保存成功");
                mContext.finish();
            }
        });
    }
}
