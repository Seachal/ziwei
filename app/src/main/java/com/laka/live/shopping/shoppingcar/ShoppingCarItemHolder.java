package com.laka.live.shopping.shoppingcar;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.dialog.EditDialog;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.widget.PriceView;
import com.laka.live.shopping.widget.ShoppingCountdownView;
import com.laka.live.ui.widget.SelectorImage;
import com.laka.live.util.AnimationHelper;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UIUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by chenjiawei on 2016/1/4.
 */
public class ShoppingCarItemHolder implements View.OnClickListener {
    public CheckBox mCheckBox;
    private SimpleDraweeView mGoodsImage;
    private TextView mGoodsTitle;
    private TextView mGoodsSpec;
    private TextView mGoodsPanicLeft;
    private TextView mGoodsPanicRight;
    private TextView mGoodsCount;
    private PriceView mGoodsPrice;
    private TextView mNum;
    private EditDialog mEditDialog;
    private ShoppingCountdownView mCountdownView;
    private View mShoppingCarGoods, mEditLayout;

    private LinearLayout mShoppingCarArea;
    private int mPageState;
    private SelectorImage mNumReduceBtn, mNumAddBtn;
    private ShoppingCarGoodsBean mShoppingCarGoodsBean;
    private int mPosition;
    private ItemClickCallBack mItemClickCallBack;
    private List<ShoppingCarGoodsBean> mShoppingCarGoodsBeans;
    private OnGoodsOperationListener mOnGoodsOperationListener;
    Animation animationShow = AnimationHelper.getAlphaAnimationShow(250, true);
    Animation animationHide = AnimationHelper.getAlphaAnimationHide(250, true);

    public void initHolder(final View view, int pageState) {
        mPageState = pageState;
        mCheckBox = (CheckBox) view.findViewById(R.id.shopping_car_ck);
        mShoppingCarGoods = view.findViewById(R.id.ll_shopping_car_goods);
        mShoppingCarArea = (LinearLayout) view.findViewById(R.id.ll_shopping_car);
        mEditLayout = view.findViewById(R.id.edit_layout);
        mGoodsCount = (TextView) view.findViewById(R.id.shopping_car_goods_items_count);
        mGoodsTitle = (TextView) view.findViewById(R.id.shopping_car_goods_items_title);
        mGoodsSpec = (TextView) view.findViewById(R.id.shopping_car_goods_items_spec);
        mGoodsPanicLeft = (TextView) view.findViewById(R.id.shopping_car_goods_items_panic_left);
        mGoodsPanicRight = (TextView) view.findViewById(R.id.shopping_car_goods_items_panic_right);
        mGoodsPrice = (PriceView) view.findViewById(R.id.shopping_car_goods_items_price);
        mCountdownView = (ShoppingCountdownView) view.findViewById(R.id.shopping_car_goods_items_count_down);
        mGoodsImage = (SimpleDraweeView) view.findViewById(R.id.shopping_car_goods_items_img);
        mNumReduceBtn = (SelectorImage) view.findViewById(R.id.shopping_goods_buy_panel_num_reduce);
        mNumAddBtn = (SelectorImage) view.findViewById(R.id.shopping_goods_buy_panel_num_add);
        mNum = (TextView) view.findViewById(R.id.shopping_goods_buy_panel_num);

        mNumAddBtn.setOnClickListener(this);
        view.findViewById(R.id.frameLayout2).setOnClickListener(this);
        mNumReduceBtn.setOnClickListener(this);

        if (mPageState == ShoppingCarConstant.GOODSPAGE) {
            mNumAddBtn.setEnabled(false);
            mNumReduceBtn.setEnabled(false);
            mEditLayout.startAnimation(animationHide);
            mGoodsCount.startAnimation(animationShow);
        } else {
            // 编辑模式
            mNumAddBtn.setEnabled(true);
            mNumReduceBtn.setEnabled(true);
            mEditLayout.startAnimation(animationShow);
            mGoodsCount.startAnimation(animationHide);
            mEditDialog = new EditDialog((FragmentActivity) view.getContext(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(v.getId() == R.id.dialog_commit){

                        mShoppingCarGoodsBeans.get(mPosition).setGoodsCount((int) v.getTag() + "");
                        mOnGoodsOperationListener.onGoodsOperationChange(mShoppingCarGoodsBeans);
                    }

                }
            });
        }

        mNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPageState == ShoppingCarConstant.OPERATIONPAGE) {
                    mEditDialog.setMaxCountNumber(StringUtils.parseInt(mShoppingCarGoodsBeans.get(mPosition).getSurplusCount()));
                    mEditDialog.setMinCountNumber(1);
                    mEditDialog.setCurrentNumber(Integer.valueOf(mShoppingCarGoodsBeans.get(mPosition).getGoodsCount()));
                    mEditDialog.show();
                }
            }
        });
    }

    public void setHolderData(ShoppingCarGoodsBean shoppingCarGoodsBean, Map<Integer, Boolean> isSelected, int position, List<ShoppingCarGoodsBean> shoppingCarGoodsBeans) {
        this.mPosition = position;
        this.mShoppingCarGoodsBean = shoppingCarGoodsBean;
        this.mShoppingCarGoodsBeans = shoppingCarGoodsBeans;
        String iconUrl = shoppingCarGoodsBean.getThumbUrl();
        if (isSelected.size() == 0) {
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(isSelected.get(position));
        }

        if (mPageState == ShoppingCarConstant.OPERATIONPAGE) {
            // 编辑模式.
            mNum.setText(shoppingCarGoodsBean.getGoodsCount());
        }

        if (StringUtils.isNotEmpty(iconUrl)) {
            mGoodsImage.setImageURI(Uri.parse(iconUrl));
        } else {
            mGoodsImage.setImageResource(R.drawable.blank_icon_bigimages);
        }
        mGoodsTitle.setText(shoppingCarGoodsBean.getTitle());
        mGoodsSpec.setText(ResourceHelper.getString(R.string.shopping_car_goods_spec_text) + shoppingCarGoodsBean.getSpecName());
        mGoodsCount.setText(ResourceHelper.getString(R.string.shopping_car_goods_count_text) + mShoppingCarGoodsBeans.get(position).getGoodsCount());
        mGoodsPrice.setText(shoppingCarGoodsBean.getSalePrice());

        int isPanicShopping = StringUtils.parseInt(shoppingCarGoodsBean.getIsPanicShopping());

        if (isPanicShopping == 1) {
            mCountdownView.setStartTime(StringUtils.parseLong(shoppingCarGoodsBean.getSurplusSeconds()));
            mCountdownView.startCountdown();
        } else {
            mGoodsPanicLeft.setVisibility(View.GONE);
            mGoodsPanicRight.setVisibility(View.GONE);
            mCountdownView.setVisibility(View.GONE);
        }


        if (mPageState == ShoppingCarConstant.GOODSPAGE) {
            mNumAddBtn.setEnabled(false);
            mNumReduceBtn.setEnabled(false);
            mEditLayout.setAlpha(0);
            mGoodsCount.setAlpha(1);
        } else {
            // 编辑模式
            mNumAddBtn.setEnabled(true);
            mNumReduceBtn.setEnabled(true);
            mEditLayout.setAlpha(1);
            mGoodsCount.setAlpha(0);
        }

    }

    public int getPageState() {
        return mPageState;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_car_operation_items_img:
                Message message = new Message();
                message.what = MsgDef.MSG_SHOW_GOODS_DETAIL_WINDOW;
                message.obj = mShoppingCarGoodsBean.getGoodsId();
                MsgDispatcher.getInstance().sendMessage(message);
                break;
            case R.id.shopping_goods_buy_panel_num_reduce:
                int reduceGoodsCount = StringUtils.parseInt(mShoppingCarGoodsBeans.get(mPosition).getGoodsCount());
                int reduceOpGoodsCount = reduceGoodsCount - 1;
                if (reduceOpGoodsCount > 0) {
                    mShoppingCarGoodsBeans.get(mPosition).setGoodsCount(reduceOpGoodsCount + "");
                    mOnGoodsOperationListener.onGoodsOperationChange(mShoppingCarGoodsBeans);
                }
                break;
            case R.id.shopping_goods_buy_panel_num_add:
                int addGoodsCount = StringUtils.parseInt(mShoppingCarGoodsBeans.get(mPosition).getGoodsCount());
                int addOpGoodsCount = addGoodsCount + 1;
                if (addOpGoodsCount <= StringUtils.parseInt(mShoppingCarGoodsBeans.get(mPosition).getSurplusCount())) {
                    mShoppingCarGoodsBeans.get(mPosition).setGoodsCount(addOpGoodsCount + "");
                    mOnGoodsOperationListener.onGoodsOperationChange(mShoppingCarGoodsBeans);
                }else{
                    ToastHelper.showToast(ResourceHelper.getString(R.string.shopping_goods_more_than_max_num_tip));
                }
                break;
            default:
                break;
        }
    }

    public interface ItemClickCallBack {
        void onItemClick(int position);
    }

    public void setItemClickCallBack(ItemClickCallBack itemClickCallBack) {
        this.mItemClickCallBack = itemClickCallBack;
    }

    public interface OnGoodsOperationListener {
        void onGoodsOperationChange(List<ShoppingCarGoodsBean> mShoppingCarGoodsBeans);
    }

    public void setOnGoodsOperationListener(OnGoodsOperationListener mOnGoodsOperationListener) {
        this.mOnGoodsOperationListener = mOnGoodsOperationListener;
    }
}
