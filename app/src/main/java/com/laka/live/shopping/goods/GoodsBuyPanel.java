package com.laka.live.shopping.goods;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.dialog.EditDialog;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.photopreview.PhotoPreviewInfo;
import com.laka.live.photopreview.PhotoPreviewPanel;
import com.laka.live.shopping.ShoppingConstant;
import com.laka.live.shopping.activity.OrderActivity;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.bean.ShoppingCarParamBean;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsCount;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsCountBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsSkuBean;
import com.laka.live.shopping.network.HttpCode;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.order.model.OrderBalanceInfo;
import com.laka.live.shopping.shoppingcar.ShoppingCarConstant;
import com.laka.live.shopping.shoppingcar.ShoppingCarRequest;
import com.laka.live.shopping.widget.BuyPanelSkuView;
import com.laka.live.ui.widget.FlowLayout;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.orhanobut.logger.Logger;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiawei on 2015/12/29.
 */
public class GoodsBuyPanel extends BasePanel implements View.OnClickListener {
    private Context mContext;
    private SimpleDraweeView mGoodsImage;
    private TextView mGoodsSpecTitleView, mGoodsSpecCountView;
    private FlowLayout mFlowLayout;
    private TextView mNumTextView;
    private TextView mAddButton, mBuyButton;
    private TextView mGoodsPv, mGoodsOriginalPv;
    private ShoppingGoodsDetailBean mGoodsDetailBean;
    private int mPanelType = 0;
    private int mGoodsNum = 1;
    private ShoppingGoodsSkuBean mSelectedSkuBean;
    private PhotoPreviewPanel mPreviewPanel;
    private ShoppingCarRequest mShoppingCarRequest;
    private PhotoPreviewInfo mPhotoPreviewInfo;
    private int mCourseId = -1;
    private int mVideoId = -1;

    private EditDialog mEditDialog;

    public GoodsBuyPanel(Context context, ShoppingGoodsDetailBean bean, int panelType) {
        super(context, false);
        this.mContext = context;
        this.mGoodsDetailBean = bean;
        this.mPanelType = panelType;
        initPanel();
    }

    public void setCourseId(int courseId) {
        mCourseId = courseId;
    }

    public void setVideoId(int videoId) {
        mVideoId = videoId;
    }

    @Override
    protected View onCreateContentView() {
        View view = View.inflate(mContext, R.layout.shopping_goods_buy_panel, null);
        initPanelViews(view);
        setupPanel();
        return view;
    }

    private void initPanelViews(View view) {
        mPhotoPreviewInfo = new PhotoPreviewInfo();
        mPreviewPanel = new PhotoPreviewPanel(mContext);
        mGoodsImage = (SimpleDraweeView) view.findViewById(R.id.shopping_goods_buy_panel_goods_img);
        view.findViewById(R.id.shopping_goods_buy_panel_close_button).setOnClickListener(this);
        view.findViewById(R.id.empty_view).setOnClickListener(this);

        mGoodsSpecTitleView = (TextView) view.findViewById(R.id.shopping_goods_buy_panel_goods_title);
        mGoodsSpecCountView = (TextView) view.findViewById(R.id.shopping_goods_buy_panel_goods_count);

        mFlowLayout = (FlowLayout) view.findViewById(R.id.shopping_goods_buy_panel_spec_flow_layout);

        view.findViewById(R.id.shopping_goods_buy_panel_num_reduce).setOnClickListener(this);
        view.findViewById(R.id.shopping_goods_buy_panel_num_add).setOnClickListener(this);
        mNumTextView = (TextView) view.findViewById(R.id.shopping_goods_buy_panel_num);


        mEditDialog = new EditDialog((FragmentActivity) mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.dialog_commit) {
                    mGoodsNum = (int) v.getTag();
                    mNumTextView.setText(String.valueOf(mGoodsNum));
                }

            }
        });

        mNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditDialog.setMinCountNumber(1);
                mEditDialog.setMaxCountNumber(Integer.parseInt(mSelectedSkuBean.getSurplusCount()));
                mEditDialog.setCurrentNumber(mGoodsNum);
                if (!mEditDialog.isShowing()) {
                    mEditDialog.show();
                }
            }
        });

        mAddButton = (TextView) view.findViewById(R.id.shopping_goods_buy_panel_btn_add_car);
        mAddButton.setOnClickListener(this);

        mBuyButton = (TextView) view.findViewById(R.id.shopping_goods_buy_panel_btn_confirm);
        mBuyButton.setOnClickListener(this);

        mGoodsPv = (TextView) view.findViewById(R.id.shopping_goods_buy_panel_price);
        mGoodsOriginalPv = (TextView) view.findViewById(R.id.shopping_goods_buy_panel_original_price);
        mGoodsOriginalPv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if (mPanelType == ShoppingConstant.PANEL_TYPE_BUY) {
            mBuyButton.setText(ResourceHelper.getString(R.string.shopping_sure_buy));
            mAddButton.setVisibility(View.GONE);
        } else if (mPanelType == ShoppingConstant.PANEL_TYPE_BUY_CAR) {
            mAddButton.setText(ResourceHelper.getString(R.string.shopping_sure_add_car));
            mBuyButton.setVisibility(View.GONE);
        }

        mGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoodsImage.getTag() != null) {
                    mPhotoPreviewInfo.photoList.clear();
                    mPhotoPreviewInfo.photoList.add((String) mGoodsImage.getTag());
                    mPreviewPanel.setupPreviewPanel(mPhotoPreviewInfo);
                    mPreviewPanel.notifyImageViews();
                    mPreviewPanel.showPanel();
                }
            }
        });


    }

    private void setupPanel() {
        mNumTextView.setText(String.valueOf(1));
        mGoodsImage.setTag(mGoodsDetailBean.getThumbUrl());
        ImageUtil.loadImage(mGoodsImage, mGoodsDetailBean.getThumbUrl());
        initFlowLayout();
    }

    private void initFlowLayout() {
        BuyPanelSkuView view;
        for (ShoppingGoodsSkuBean skuBean : mGoodsDetailBean.getSku()) {
            view = new BuyPanelSkuView(mContext);
            view.setContent(skuBean.getName());
            view.setTag(skuBean);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleGoodsSpecSelected(v);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = Utils.dip2px(mContext, 8);
            params.topMargin = Utils.dip2px(mContext, 2);
            params.gravity = Gravity.CENTER_VERTICAL;
            mFlowLayout.addView(view, params);
        }

        if (mGoodsDetailBean.getSku().size() > 0 && mSelectedSkuBean == null) {
            handleGoodsSpecSelected(mFlowLayout.getChildAt(0));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_goods_buy_panel_close_button:
                hidePanel();
                break;
            case R.id.empty_view:
                hidePanel();
                break;
            case R.id.shopping_goods_buy_panel_num_reduce:
                if (mSelectedSkuBean != null) {
                    if (mGoodsNum > 1) {
                        mGoodsNum--;
                    }
                    mNumTextView.setText(String.valueOf(mGoodsNum));
                }
                break;
            case R.id.shopping_goods_buy_panel_num_add:
                if (mSelectedSkuBean != null) {
                    if (mGoodsNum < Integer.parseInt(mSelectedSkuBean.getSurplusCount())) {
                        mGoodsNum++;
                        mNumTextView.setText(String.valueOf(mGoodsNum));
                    } else {
                        ToastHelper.showToast(R.string.shopping_goods_more_than_max_num_tip);
                    }
                }
                break;
            case R.id.shopping_goods_buy_panel_btn_add_car:
                if (mSelectedSkuBean != null) {
                    hidePanel();
                    ShoppingCarParamBean shoppingCarParamBean = new ShoppingCarParamBean();
                    shoppingCarParamBean.setGoodsId(String.valueOf(mGoodsDetailBean.getGoodsId()));
                    shoppingCarParamBean.setSpecId(String.valueOf(mSelectedSkuBean.getSkuId()));
                    shoppingCarParamBean.setGoodsCount(String.valueOf(mGoodsNum));
                    startAddShoppingCarRequest(shoppingCarParamBean);
                } else {
                    ToastHelper.showToast(R.string.shopping_product_add_shopping_car_remind_text);
                }

                break;
            case R.id.shopping_goods_buy_panel_btn_confirm:
                if (mSelectedSkuBean == null) {
                    ToastHelper.showToast(R.string.shopping_product_add_shopping_car_remind_text);
                } else {
                    hidePanel();
                    if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                        goOrderInfo();
                    } else {
                        LoginActivity.startActivity(mContext, LoginActivity.TYPE_FROM_NEED_LOGIN);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 进入订单详情页面
     */
    private void goOrderInfo() {
        OrderActivity.startActivity(mContext, createOrderInfo());
    }


    private OrderBalanceInfo createOrderInfo() {
        OrderBalanceInfo balanceInfo = new OrderBalanceInfo();

        balanceInfo.goodsCount = mGoodsNum;
        balanceInfo.courseId = mCourseId;
        balanceInfo.videoId = mVideoId;
        balanceInfo.goodsId = String.valueOf(mGoodsDetailBean.getGoodsId());
//        if (StringUtils.parseInt(mGoodsDetailBean.getIsPanicShopping()) == ShoppingConstant.PANIC_TYPE_FALSE) {
//            balanceInfo.panicShoppingId = "0";
//        } else {
//            ShoppingPanicBean panicBean = mGoodsDetailBean.getPanicShopping();
//            if (panicBean != null) {
//                balanceInfo.panicShoppingId = panicBean.getPanicShoppingId();
//            }
//        }
        if (mSelectedSkuBean != null) {
            balanceInfo.skuId = String.valueOf(mSelectedSkuBean.getSkuId());
        }

        List<ShoppingCarGoodsBean> list = new ArrayList<>();
        ShoppingCarGoodsBean carGoodsBean = new ShoppingCarGoodsBean();
        carGoodsBean.setGoodsCount(String.valueOf(mGoodsNum));
        carGoodsBean.setGoodsId(String.valueOf(mGoodsDetailBean.getGoodsId()));
        carGoodsBean.setSalePrice(String.valueOf(mSelectedSkuBean.getPrice()));
        carGoodsBean.setSpecName(mSelectedSkuBean.getName());
        carGoodsBean.setThumbUrl(mGoodsDetailBean.getThumbUrl());
//        carGoodsBean.setIsPanicShopping(mGoodsDetailBean.getIsPanicShopping());
//        carGoodsBean.setDescription(mGoodsDetailBean.getDescription());
        carGoodsBean.setTitle(mGoodsDetailBean.getTitle());
        list.add(carGoodsBean);

        balanceInfo.setList(list);

        return balanceInfo;
    }


    private void handleGoodsSpecSelected(View selectedView) {
        int count = mFlowLayout.getChildCount();
        BuyPanelSkuView child;
        for (int i = 0; i < count; i++) {
            child = (BuyPanelSkuView) mFlowLayout.getChildAt(i);
            if (child == selectedView) {
                child.setSelected(true);
            } else {
                child.setSelected(false);
            }
        }

        ShoppingGoodsSkuBean skuBean = (ShoppingGoodsSkuBean) selectedView.getTag();
        mGoodsSpecTitleView.setText(skuBean.getName());
        mGoodsSpecCountView.setText(String.valueOf(skuBean.getSurplusCount()));
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        mGoodsPv.setText(ResourceHelper.getString(R.string.search_goods_price, numberFormat.format(skuBean.getPrice())));
        mGoodsOriginalPv.setText(ResourceHelper.getString(R.string.search_goods_price, numberFormat.format(mGoodsDetailBean.getMarketPrice())));

        if (Integer.parseInt(skuBean.getSurplusCount()) <= 0) {
            ToastHelper.showToast("该商品已经没有库存");
            mGoodsNum = 0;

            mAddButton.setText(ResourceHelper.getString(R.string.shopping_car_error_type_less));
            mAddButton.setEnabled(false);

            mBuyButton.setText(ResourceHelper.getString(R.string.shopping_car_error_type_less));
            mBuyButton.setEnabled(false);
        } else {
            mGoodsNum = 1;

            mBuyButton.setText(ResourceHelper.getString(R.string.shopping_sure_buy));
            mBuyButton.setEnabled(true);
            mAddButton.setText(ResourceHelper.getString(R.string.shopping_sure_add_car));
            mAddButton.setEnabled(true);
        }

        mNumTextView.setText(String.valueOf(mGoodsNum));

        if (Utils.isNotEmpty(skuBean.getImageUrl())) {
            mGoodsImage.setTag(skuBean.getImageUrl());
            ImageUtil.loadImage(mGoodsImage, skuBean.getImageUrl());
        }

        mSelectedSkuBean = skuBean;
    }

    private void startAddShoppingCarRequest(ShoppingCarParamBean shoppingCarParamBean) {
        mShoppingCarRequest = new ShoppingCarRequest(shoppingCarParamBean);
        mShoppingCarRequest.startShoppingCarAddRequest(mCourseId, mVideoId, new IHttpCallBack() {

            @Override
            public <T> void onSuccess(T obj, String result) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.shopping_car_add_success));

                if (obj == null || !(obj instanceof JTBShoppingGoodsCount)) {
                    return;
                }

                ShoppingGoodsCountBean goodsCountBean = ((JTBShoppingGoodsCount) obj).getData();
                EventBusManager.postEvent(goodsCountBean.getGoodsCount(), SubcriberTag.UPDATE_GOODS_CAR_COUNT);
            }

            @Override
            public void onError(String errorStr, int code) {
                handleAddShoppingCarFailed(code);
            }
        });
    }

    /**
     * 数据异常处理
     *
     * @param errorCode
     */
    private void handleAddShoppingCarFailed(int errorCode) {
        if (errorCode == ShoppingCarConstant.ERROR_TYPE_FAIL) {
            ToastHelper.showToast(R.string.shopping_car_error_type_fail);
        } else if (errorCode == ShoppingCarConstant.ERROR_TYPE_LESS) {
            ToastHelper.showToast(R.string.shopping_car_error_type_less);
        } else if (errorCode == ShoppingCarConstant.ERROR_TYPE_NO_HAVE) {
            ToastHelper.showToast(R.string.shopping_car_error_type_no_have);
        } else if (errorCode == ShoppingCarConstant.ERROR_TYPE_TOO_MUCH) {
            ToastHelper.showToast(R.string.shopping_car_error_type_too_much);
        } else if (errorCode == HttpCode.ERROR_NETWORK) {
            ToastHelper.showToast(R.string.homepage_network_error_retry);
        }

    }
}
