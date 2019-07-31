package com.laka.live.shopping.widget;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.bean.ShoppingBrandBean;
import com.laka.live.shopping.bean.ShoppingBrandGoodsBean;
import com.laka.live.shopping.bean.newversion.ShoppingCategoryBean;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.util.AnimationHelper;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by zhxu on 2016/5/3.
 * Email:357599859@qq.com
 */
public class ShoppingPanel extends LinearLayout {

    private Context mContext;

    private final static int TYPE_BRAND = 2;
    private final static int TYPE_TOP_CATE = 3;

    private final static int mCommonDimen = ResourceHelper.getDimen(R.dimen.space_1);

    private SimpleDraweeView mSdvImage;

    private TextView mTvDesc, mTvTitle, mTitle;

    private LinearLayout mExprLayout, mGoodsLayout;

    private View mView;

    private IShoppingPanel mCallBack;

    private int padding = (HardwareUtil.screenWidth - mCommonDimen * 45 * 4) / 8;

    public ShoppingPanel(Context context) {
        this(context, null);
    }

    public ShoppingPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        int _padding = mCommonDimen * 15;
        setBackgroundResource(R.color.white);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        setPadding(0, _padding, 0, _padding);
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.VERTICAL);
        createContentView();
    }

    public void setViews(View view, TextView title) {
        mView = view;
        mTitle = title;
        mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidden();
            }
        });
    }

    public void setCallBack(IShoppingPanel mCallBack) {
        this.mCallBack = mCallBack;
    }

//    public void setupItems(ShoppingCategoryBean categoryBean, int type, int typeId) {
//        removeAllView();
//        List<ShoppingBrandGoodsBean> goodsList = categoryBean.getCateExpr();
//        if (goodsList != null) {
//            for (ShoppingBrandGoodsBean goodsBean : goodsList) {
//                createItems(goodsBean.getImageUrl(), goodsBean.getTitle(), 0);
//            }
//        }
//        List<ShoppingBrandBean> brandList = categoryBean.getCateBrand();
//        if (brandList != null && !brandList.isEmpty()) {
//            ShoppingBrandBean _brandBean = brandList.get(0);
//            int position = 0;
//            for (ShoppingBrandBean brandBean : brandList) {
//                if (brandBean.getType() == type && brandBean.getTypeId() == typeId) {
//                    setGoodsItems(brandBean, type != TYPE_TOP_CATE);
//                    mTitle.setText(brandBean.getName());
//                    if (mCallBack != null) {
//                        mCallBack.onScroll(position);
//                    }
//                }
//                position++;
//            }
//            if (mTvDesc.getText().length() == 0) {
//                setGoodsItems(_brandBean, type != TYPE_TOP_CATE);
//                mTitle.setText(_brandBean.getName());
//            }
//        }
//        mExprLayout.setVisibility(type != TYPE_BRAND ? View.VISIBLE : View.GONE);
//        mGoodsLayout.setVisibility(type != TYPE_BRAND ? View.GONE : View.VISIBLE);
//        mTvTitle.setText(type != TYPE_BRAND ? R.string.shopping_category_expr : R.string.shopping_category_brand);
//    }

    private void setGoodsItems(ShoppingBrandBean brandBean, boolean isShow) {
        setItems(brandBean.getImageUrl(), brandBean.getDescription(), isShow);
        List<ShoppingBrandGoodsBean> goods = brandBean.getList();
        if (goods != null) {
            for (ShoppingBrandGoodsBean goodsBean : goods) {
                createItems(goodsBean.getThumbUrl(), goodsBean.getTitle(), goodsBean.getGoodsId());
            }
        }
    }

    private void setItems(String imageUrl, String desc, boolean isShow) {
        if (!isShow) {
            mSdvImage.setVisibility(GONE);
        } else {
            mSdvImage.setVisibility(VISIBLE);
            if (StringUtils.isNotEmpty(imageUrl)) {
                mSdvImage.setImageURI(Uri.parse(imageUrl));
            } else {
                mSdvImage.setBackgroundResource(R.color.black);
            }
        }
        if (StringUtils.isNotEmpty(desc)) {
            mTvDesc.setText(desc);
        }
    }

    private void removeAllView() {
        if (mExprLayout != null && mExprLayout.getChildCount() > 0) {
            mExprLayout.removeAllViewsInLayout();
        }
        if (mGoodsLayout != null && mGoodsLayout.getChildCount() > 0) {
            mGoodsLayout.removeAllViewsInLayout();
        }
    }

    private void createItems(String imageUrl, String title, final int goodsId) {
        int width = mCommonDimen * 45;
        LinearLayout itemsView = new LinearLayout(mContext);
        itemsView.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        itemsView.setOrientation(LinearLayout.VERTICAL);
        itemsView.setGravity(Gravity.CENTER);

        SimpleDraweeView sdvImage = new SimpleDraweeView(mContext);
        LayoutParams layoutParams = new LayoutParams(width, width);
        sdvImage.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        sdvImage.setLayoutParams(layoutParams);
        itemsView.addView(sdvImage);
        if (StringUtils.isNotEmpty(imageUrl)) {
            sdvImage.setImageURI(Uri.parse(imageUrl));
        }

        TextView tvType = new TextView(mContext);
        tvType.setGravity(Gravity.CENTER);
        tvType.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_14));
        tvType.setTextColor(Color.parseColor("#737373"));
        tvType.setPadding(0, mCommonDimen * 5, 0, 0);
        tvType.setMaxLines(1);
        itemsView.addView(tvType);
        if (StringUtils.isNotEmpty(title)) {
            tvType.setText(title);
        }

        if (goodsId != 0) {
            itemsView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hidden();
                    openProductDetailByGoodsId(goodsId);
                }
            });
            mGoodsLayout.addView(itemsView);
        } else {
            mExprLayout.addView(itemsView);
        }

        RippleEffectHelper.addRippleEffectInView(itemsView);
    }

    public void createContentView() {
        int width = mCommonDimen * 54;
        mSdvImage = new SimpleDraweeView(mContext);
        LayoutParams layoutParams = new LayoutParams(width, width);
        layoutParams.bottomMargin = mCommonDimen * 13;
        addView(mSdvImage, layoutParams);

        mTvDesc = new TextView(mContext);
        mTvDesc.setTextColor(ResourceHelper.getColor(R.color.title_text_color));
        mTvDesc.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_14));
        mTvDesc.setGravity(Gravity.LEFT);
        mTvDesc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mTvDesc.setPadding(padding, 0, padding, 0);
        addView(mTvDesc);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(0, mCommonDimen * 10, 0, mCommonDimen * 10);
        linearLayout.setGravity(Gravity.CENTER);
        addView(linearLayout);

        layoutParams = new LayoutParams(mCommonDimen * 110, mCommonDimen);
        linearLayout.addView(createLine(layoutParams));

        mTvTitle = new TextView(mContext);
        mTvTitle.setGravity(Gravity.CENTER);
        mTvTitle.setPadding(mCommonDimen * 15, 0, mCommonDimen * 15, 0);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));
        mTvTitle.setTextColor(ResourceHelper.getColor(R.color.shopping_title));
        mTvTitle.setText(R.string.shopping_category_expr);
        linearLayout.addView(mTvTitle);

        linearLayout.addView(createLine(layoutParams));

        mExprLayout = createLinearLayout();
        addView(mExprLayout);

        mGoodsLayout = createLinearLayout();
        addView(mGoodsLayout);
    }

    private LinearLayout createLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;
    }

    private TextView createLine(LayoutParams layoutParams) {
        TextView line = new TextView(mContext);
        line.setLayoutParams(layoutParams);
        line.setBackgroundResource(R.color.shopping_categories_line);
        return line;
    }

    public void show() {
        clearAnimation();
        if (getVisibility() == GONE) {
            TranslateAnimation translateAnimation = AnimationHelper.getTranslateDownVisible();
            startAnimation(translateAnimation);
            setVisibility(VISIBLE);
        } else {
            hidden();
        }
    }

    private void hidden() {
        clearAnimation();
        TranslateAnimation translateAnimation = AnimationHelper.getTranslateUpHidden();
        startAnimation(translateAnimation);
        setVisibility(GONE);
    }

    /**
     * 跳转商品详细
     *
     * @param goodsId
     */
    private void openProductDetailByGoodsId(int goodsId) {
        Message msg = Message.obtain();
        msg.what = MsgDef.MSG_SHOW_GOODS_DETAIL_WINDOW;
        msg.obj = goodsId + "";
        MsgDispatcher.getInstance().sendMessage(msg);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            mView.setVisibility(View.VISIBLE);
            mTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourceHelper.getDrawable(R.drawable.shopping_category_up), null);
        } else {
            mView.setVisibility(View.GONE);
            mTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourceHelper.getDrawable(R.drawable.shopping_category_down), null);
        }
    }

    public interface IShoppingPanel {
        void onScroll(int position);
    }
}
