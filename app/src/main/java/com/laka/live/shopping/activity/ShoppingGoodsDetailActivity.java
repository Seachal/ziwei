package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.ShoppingConstant;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingCartCount;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsDetail;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingRecommend;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsImageUrlBean;
import com.laka.live.shopping.bean.newversion.ShoppingRecommendBean;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.shopping.goods.GoodsBuyPanel;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.network.HeaderManager;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.system.SystemHelper;
import com.laka.live.shopping.web.WebChromeClientBaseImpl;
import com.laka.live.shopping.web.WebViewClientDefaultImpl;
import com.laka.live.shopping.web.WebViewConst;
import com.laka.live.shopping.web.WebViewJsInterface;
import com.laka.live.shopping.widget.CustomScrollView;
import com.laka.live.shopping.widget.GoodsRecommendLayout;
import com.laka.live.shopping.widget.ImageCycleView;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.widget.FlowLayout;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.util.Common;
import com.laka.live.util.CommonUtils;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.SystemUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ShoppingGoodsDetailActivity
 * @Description: 商品详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class ShoppingGoodsDetailActivity extends BaseActivity implements View.OnClickListener, CustomScrollView.ScrollViewListener,
        WebViewJsInterface.JsInterfaceCallback, WebChromeClientBaseImpl.WebChromeClientCallback, WebViewClientDefaultImpl.WebViewClientAddCallback {
    private final static String TAG = ShoppingGoodsDetailActivity.class.getSimpleName();

    private final static String GOOD_ID = "goodId";
    private final static String RELATIVE_ID = "relativeId";
    private final static String IS_COURSE = "IS_COURSE";

    private TitleBarActionItem mShoppingCar;
    private RelativeLayout mHeadView;

    private CustomScrollView mScrollView;

    private ImageCycleView mImageCycleView;
    private LoadingLayout mLoadingLayout;
    private TextView mTitleView, mBuyerView;
    private TextView mOriginalPriceTv;
    private TextView mSalePriceTv;

    private int mGoodsId;
    private RelativeLayout mDetailRl;
    private TextView mDetailTv;
    private ImageView mDetailIv;
    private RelativeLayout mRecommendRl;
    private TextView mRecommendTv;
    private ImageView mRecommendIv;
    private FlowLayout mGoodsTagLayout;
    private GoodsRecommendLayout mRecommendedLayout;
    private WebView mWebView;
    private FrameLayout mWebViewLayout;
    private ImageView mGoodsInvalidateView;
    private LinearLayout mFloatView;
    private TextView mFloatViewDetail, mFloatViewRecommended;
    private TextView mBuyButton;

    private int mStatusHeight;

    private ShoppingGoodsDetailBean mGoodsDetailBean;

    private GoodsBuyPanel mGoodsBuyPanel;

    private int mRelativeId = -1;
    private boolean isCourse = true;

    public static void startActivity(Context context, int goodId) {
        startActivity(context, goodId, -1, true);
    }

    public static void startActivity(Context context, int goodId, int courseId) {
        startActivity(context, goodId, courseId, true);
    }

    /**
     * 新增扩展
     *
     * @param context
     * @param goodId
     * @param relativeId
     * @param isCourse
     */
    public static void startActivity(Context context, int goodId, int relativeId, boolean isCourse) {
        Intent intent = new Intent(context, ShoppingGoodsDetailActivity.class);
        intent.putExtra(GOOD_ID, goodId);
        intent.putExtra(RELATIVE_ID, relativeId);
        intent.putExtra(IS_COURSE, isCourse);
        ActivityCompat.startActivity(context, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_goods_detail);
        getWindow().setBackgroundDrawable(null);

        initTopBar();
        initUI();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
        mImageCycleView.stopImageCycle();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        switch (event.tag) {
            case SubcriberTag.UPDATE_GOODS_CAR_COUNT:
//                updateShoppingCartNum((int) event.event);
                queryShoppingCartCount();
                break;
            case SubcriberTag.CLICK_FLOAT_LIVE:
                finish();
                break;
            default:
                break;
        }
    }

    private void initTopBar() {
        mHeadView = (RelativeLayout) findViewById(R.id.head_view);
        mShoppingCar = (TitleBarActionItem) findViewById(R.id.shopping_car_tbai);
        mShoppingCar.setDrawable(ResourceHelper.getDrawable(R.drawable.nav_icon_shop));
        mShoppingCar.setOnClickListener(this);
        findViewById(R.id.share_iv).setOnClickListener(this);
        findViewById(R.id.back_icon).setOnClickListener(this);
    }

    private void initUI() {
        mStatusHeight = SystemUtil.getStatusBarHeight(this);

        mScrollView = (CustomScrollView) findViewById(R.id.shopping_goods_detail_scrollview);
        mScrollView.setScrollViewListener(this);

        mImageCycleView = (ImageCycleView) findViewById(R.id.goods_detail_image_cycle_view);
        mImageCycleView.setAspectRatio(1.0f);
        ViewGroup.LayoutParams params = mImageCycleView.getLayoutParams();
        params.width = Utils.getScreenWidth(this);
        params.height = Utils.getScreenWidth(this);

        mGoodsInvalidateView = (ImageView) findViewById(R.id.goods_detail_invalidate_icon);

        mLoadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);
        mLoadingLayout.setDefaultLoading();
        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
            @Override
            public void onClick() {
                queryShoppingCartCount();
                getGoodsDetail();
            }
        });

        mFloatView = (LinearLayout) findViewById(R.id.float_view);
        mFloatViewDetail = (TextView) findViewById(R.id.float_view_detail_title_button);
        mFloatViewRecommended = (TextView) findViewById(R.id.float_view_recommended_title_button);
        mFloatViewDetail.setOnClickListener(this);
        mFloatViewDetail.setSelected(true);
        mFloatViewRecommended.setOnClickListener(this);

        mTitleView = (TextView) findViewById(R.id.shopping_goods_detail_title);
        mSalePriceTv = (TextView) findViewById(R.id.shopping_goods_detail_sale_price);
        mOriginalPriceTv = (TextView) findViewById(R.id.shopping_goods_detail_original_price);
        mOriginalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        mBuyerView = (TextView) findViewById(R.id.shopping_goods_detail_buyer);
        mGoodsTagLayout = (FlowLayout) findViewById(R.id.goods_tag_layout);

        findViewById(R.id.goods_detail_secretary).setOnClickListener(this);
        findViewById(R.id.goods_detail_add_buy_car).setOnClickListener(this);
        mBuyButton = (TextView) findViewById(R.id.goods_detail_buy);
        mBuyButton.setOnClickListener(this);

        mDetailRl = (RelativeLayout) findViewById(R.id.goods_detail_title_rl);
        mDetailTv = (TextView) findViewById(R.id.goods_detail_title_tv);
        mDetailIv = (ImageView) findViewById(R.id.goods_detail_title_iv);
        mDetailRl.setSelected(true);
        mDetailTv.setSelected(true);
        mDetailIv.setSelected(true);
        mDetailRl.setOnClickListener(this);

        mRecommendRl = (RelativeLayout) findViewById(R.id.goods_recommended_title_rl);
        mRecommendTv = (TextView) findViewById(R.id.goods_recommended_title_tv);
        mRecommendIv = (ImageView) findViewById(R.id.goods_recommended_title_iv);
        mRecommendRl.setOnClickListener(this);

        mWebViewLayout = (FrameLayout) findViewById(R.id.goods_detail_web_view);
        mRecommendedLayout = (GoodsRecommendLayout) findViewById(R.id.goods_recommend_layout);
    }

    private void initData() {
        if (getIntent() == null) {
            finish();
        }

        mGoodsId = getIntent().getIntExtra(GOOD_ID, -1);
        mRelativeId = getIntent().getIntExtra(RELATIVE_ID, -1);
        isCourse = getIntent().getBooleanExtra(IS_COURSE, true);
        mLoadingLayout.setDefaultLoading();
        mScrollView.scrollTo(0, 0);

        createWebView();
        queryShoppingCartCount();
        getGoodsDetail();

        handleDetailTabClick();
    }

    private void createWebView() {
        mWebViewLayout.removeAllViews();

        try {
            mWebView = new WebView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mWebView == null) {
            return;
        }
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebViewClientDefaultImpl webViewClient = new WebViewClientDefaultImpl(this);
        webViewClient.setWebViewClientAddCallback(this);
        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(new WebChromeClientBaseImpl(this));
        mWebView.addJavascriptInterface(new WebViewJsInterface(this, String.valueOf(mGoodsId)), "android");

        mWebViewLayout.addView(mWebView);
    }

    private void destroyWebView() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        mWebViewLayout.removeAllViews();
        mWebView = null;

        CommonUtils.gc();
    }

    private void tryOpenGoodsBuyPanel(int panelType) {
        if (mGoodsDetailBean == null) {
            return;
        }
        boolean isInvalidate = mGoodsDetailBean.getStatus() == CommonConst.GOODS_STATE_INVALIDATE;
        if (isInvalidate) {
            ToastHelper.showToast(R.string.shopping_goods_invalidate_tips);
            return;
        }
        if (mGoodsBuyPanel != null && mGoodsBuyPanel.isShowing()) {
            if (isCourse) {
                mGoodsBuyPanel.setCourseId(mRelativeId);
            } else {
                mGoodsBuyPanel.setVideoId(mRelativeId);
            }
            return;
        }
        mGoodsBuyPanel = new GoodsBuyPanel(this, mGoodsDetailBean, panelType);
        if (isCourse) {
            mGoodsBuyPanel.setCourseId(mRelativeId);
        } else {
            mGoodsBuyPanel.setVideoId(mRelativeId);
        }
        mGoodsBuyPanel.showPanel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_detail_buy:
                tryOpenGoodsBuyPanel(ShoppingConstant.PANEL_TYPE_BUY);
                break;
            case R.id.goods_detail_add_buy_car:
                tryOpenGoodsBuyPanel(ShoppingConstant.PANEL_TYPE_BUY_CAR);
                break;
            case R.id.goods_detail_secretary:
                handleOnServiceClick();
                break;
            case R.id.goods_detail_title_rl:
                handleDetailTabClick();
                break;
            case R.id.goods_recommended_title_rl:
                handleRecommendedTabClick();
                break;
            case R.id.float_view_detail_title_button:
                handleDetailTabClick();
                break;
            case R.id.float_view_recommended_title_button:
                handleRecommendedTabClick();
                break;
            case R.id.back_icon:
                finish();
                break;
            case R.id.shopping_car_tbai:
                handleOnShoppingCarClick();
                break;
            case R.id.share_iv:
                handleOnShareClick();
                break;
            default:
                break;
        }
    }

    private void handleDetailTabClick() {
        if (mDetailRl.isSelected() || mFloatViewDetail.isSelected()) {
            return;
        }
        updateTitleState(true, false);
        mWebViewLayout.setVisibility(View.VISIBLE);
        mRecommendedLayout.setVisibility(View.GONE);
    }


    private void handleRecommendedTabClick() {
        if (mFloatViewRecommended.isSelected() || mRecommendRl.isSelected()) {
            return;
        }
        updateTitleState(false, true);
        mWebViewLayout.setVisibility(View.GONE);
        mRecommendedLayout.setVisibility(View.VISIBLE);
    }

    private void updateTitleState(boolean isDetail, boolean isRecommended) {
        mDetailRl.setSelected(isDetail);
        mDetailTv.setSelected(isDetail);
        mDetailIv.setSelected(isDetail);
        mRecommendRl.setSelected(isRecommended);
        mRecommendTv.setSelected(isRecommended);
        mRecommendIv.setSelected(isRecommended);

        mFloatViewRecommended.setSelected(isRecommended);
        mFloatViewDetail.setSelected(isDetail);
    }

    private void getGoodsDetail() {
        ShoppingRequest.getInstance().getShoppingGoodsDetail(mGoodsId, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (obj == null || !(obj instanceof JTBShoppingGoodsDetail)) {
                    mLoadingLayout.setDefaultNetworkError(true);
                    ToastHelper.showToast(R.string.shopping_network_error_retry);
                    return;
                }

                mGoodsDetailBean = ((JTBShoppingGoodsDetail) obj).getData();

                if (mGoodsDetailBean == null) {
                    mLoadingLayout.setDefaultNetworkError(true);
                    return;
                }

                mLoadingLayout.hide();
                updateBanners();
                mTitleView.setText(mGoodsDetailBean.getTitle());
                updatePriceInfo();

                if (!Utils.listIsNullOrEmpty(mGoodsDetailBean.getTags())) {
                    mGoodsTagLayout.setVisibility(View.VISIBLE);

                    for (String tag : mGoodsDetailBean.getTags()) {
                        createPostageTag(tag);
                    }
                } else {
                    mGoodsTagLayout.setVisibility(View.GONE);
                }

                updateGoodsStatus(mGoodsDetailBean.getStatus());
//                updateShoppingCartNum(mGoodsDetailBean.getCartCount());

                if (mWebView != null) {
                    String detailUrl = HttpUrls.SHOPPING_GOODS_DETAIL_H5 + mGoodsId + "&version=" + SystemHelper.getAppInfo().versionName;
                    mWebView.loadUrl(detailUrl, HeaderManager.defaultHeader());
                }

                getRecommendGoodsList();
            }

            @Override
            public void onError(String errorStr, int code) {
                mLoadingLayout.setDefaultNetworkError(true);
                ToastHelper.showToast(R.string.shopping_network_error_retry);
            }
        });
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

            }
        });
    }

    private void updateBanners() {
        if (mGoodsDetailBean == null) {
            return;
        }

        List<ShoppingGoodsImageUrlBean> bannerImages = mGoodsDetailBean.getImageUrls();

        if (bannerImages == null) {
            bannerImages = new ArrayList<>();
        }

        if (bannerImages.size() <= 0) {
            bannerImages.add(new ShoppingGoodsImageUrlBean());
        }

        mImageCycleView.setImageResources(bannerImages, null);
        mImageCycleView.stopImageCycle();
        mImageCycleView.setCanAutoScroll(false);
    }

    private void updatePriceInfo() {
        mSalePriceTv.setText(String.valueOf(mGoodsDetailBean.getSalePrice()));
        mOriginalPriceTv.setText(String.valueOf(mGoodsDetailBean.getMarketPrice()));
        mBuyerView.setText(ResourceHelper.getString(R.string.shopping_buyer_count, mGoodsDetailBean.getSaleCount()));
    }

    private void createPostageTag(String content) {
        if (Utils.isEmpty(content)) {
            return;
        }
        TextView postageTag = new TextView(this);
        postageTag.setCompoundDrawablePadding(Utils.dp2px(this, 5));
        postageTag.setCompoundDrawablesRelativeWithIntrinsicBounds(ResourceHelper.getDrawable(R.drawable.mall_icon_free), null, null, null);
        postageTag.setText(content);
        postageTag.setTextColor(ResourceHelper.getColor(R.color.color848484));
        postageTag.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.topMargin = Utils.dip2px(mContext, 12);
        layoutParams.bottomMargin = Utils.dip2px(mContext, 12);
        layoutParams.leftMargin = Utils.dip2px(mContext, 10);
        mGoodsTagLayout.addView(postageTag, layoutParams);
    }

    private void updateGoodsStatus(int status) {
        if (status == CommonConst.GOODS_STATE_INVALIDATE) {
//            mGoodsInvalidateView.setVisibility(View.VISIBLE);
            mBuyButton.setText(R.string.shopping_goods_invalidate);
            mBuyButton.setEnabled(false);
        } else {
//            mGoodsInvalidateView.setVisibility(View.GONE);
            mBuyButton.setText(R.string.shopping_goods_buy_now);
            mBuyButton.setEnabled(true);
        }
    }

    private void updateShoppingCartNum(int count) {
        if (mShoppingCar != null) {
            mShoppingCar.setRedTipVisibility(count > 0);
            mShoppingCar.setRedTipText(count > 99 ? "99+" : String.valueOf(count));
        }
    }

    private void getRecommendGoodsList() {
        if (mRecommendedLayout.hasRecommend()) {
            return;
        }
        ShoppingRequest.getInstance().getShoppingRecommend(mGoodsId, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                Log.d(TAG, "getShoppingRecommend success. " + result);

                if (obj == null || !(obj instanceof JTBShoppingRecommend)) {
                    mRecommendedLayout.setupGoodsRecommendedLayout(null);
                    return;
                }

                ShoppingRecommendBean recommendBean = ((JTBShoppingRecommend) obj).getData();

                if (recommendBean == null) {
                    mRecommendedLayout.setupGoodsRecommendedLayout(null);
                    return;
                }

                mRecommendedLayout.setupGoodsRecommendedLayout(recommendBean.getGoods());
            }

            @Override
            public void onError(String errorStr, int code) {
                Log.d(TAG, "getShoppingRecommend error. errorStr : " + errorStr + " ; code : " + code);
                mRecommendedLayout.setupGoodsRecommendedLayout(null);
            }
        });
    }

    @Override
    public void onJsCommand(String command, String... result) {
        if (WebViewJsInterface.COMMAND_COLLECT.equals(command)) {
            // TODO: 2017/7/14 收藏处理
//            CollectHelper.handleJsCollectAction(mWebView, result);
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

    }

    @Override
    public void onAddUrlTypeLoading(int urlType, String value) {
        if (urlType == WebViewConst.URL_TYPE_GOODS) {
        }
    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageStarted(String url) {

    }

    @Override
    public void onReceivedError() {

    }

    @Override
    public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy) {
        int[] location = new int[2];
        mDetailRl.getLocationOnScreen(location);
        mFloatView.setVisibility(location[1] < mHeadView.getHeight() + mStatusHeight ? View.VISIBLE : View.GONE);
    }

    /**
     * 购物车按钮点击事件
     */
    private void handleOnShoppingCarClick() {
        ShoppingCarActivity.startActivity(this);
    }

    /**
     * 客服按钮点击事件
     */
    private void handleOnServiceClick() {
        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            LoginActivity.startActivity(this, LoginActivity.TYPE_FROM_NEED_LOGIN);
            return;
        }
        // TODO: 2017/7/31 客服
        ChatMessageActivity.startActivity(this, mGoodsDetailBean);//1272
    }

    /**
     * 分享按钮点击事件
     */
    private void handleOnShareClick() {
        if (mGoodsDetailBean == null) {
            return;
        }
        showShareDialog(Common.SHARE_GOODS_URL + mGoodsDetailBean.getGoodsId(), "[" + mGoodsDetailBean.getTitle() + "] |滋味Live", "发现有滋有味的生活", mGoodsDetailBean.getThumbUrl(), true);
//        String shareUrl = HttpUrls.H5_URL + "?a=goodsShareNew&id=" + mGoodsId + "&apiver=" + CommonConst.API_VERSION;
//        String shareTitle = mGoodsDetailBean.getTitle();
//        String shareContent = ResourceHelper.getString(R.string.shopping_product_share_content);
        // TODO: 2017/7/14 分享商品
//        ShareIntentBuilder builder = ShareIntentBuilder.obtain();
//        builder.setShareUrl(shareUrl);
//        builder.setShareMineType(ShareIntentBuilder.MIME_TYPE_TEXT);
//        builder.setShareTitle(shareTitle);
//        builder.setShareContent(shareContent);
//        builder.setShareImageUrl(mGoodsDetailBean.getThumbUrl());
//        builder.setShareSourceType(ShareIntentBuilder.SOURCE_TYPE_SHARE_LINK);
//
//        Bundle shareBundle = new Bundle();
//        shareBundle.putString("type", "product");
//        String title = shareTitle;
//        if (StringUtils.isEmpty(title)) {
//            title = String.valueOf(mGoodsId);
//        }
//        shareBundle.putString("spec", title);
//        builder.setStatsBundle(shareBundle);
//
//        ShareParameter.title = title;
//        ShareParameter.objId = mGoodsId;
//        ShareParameter.objType = ShareRequest.TYPE_PRODUCT;//商品
//
//        Message message = Message.obtain();
//        message.what = MsgDef.MSG_SHARE;
//        message.obj = builder.create();
//        MsgDispatcher.getInstance().sendMessage(message);
    }
}
