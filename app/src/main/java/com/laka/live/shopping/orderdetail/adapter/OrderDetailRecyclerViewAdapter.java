package com.laka.live.shopping.orderdetail.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.activity.OrderLogisticsActivity;
import com.laka.live.shopping.bean.ShoppingOrderDetailBean;
import com.laka.live.shopping.bean.ShoppingOrderDetailGoodsBean;
import com.laka.live.shopping.bean.ShoppingOrderLogisticsBean;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.model.TemplateHolder;
import com.laka.live.shopping.order.widget.OrderDialog;
import com.laka.live.shopping.widget.PriceView;
import com.laka.live.shopping.widget.ShoppingCountdownView;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhxu on 2015/12/23.
 * Email:357599859@qq.com
 */
public class OrderDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;

    private Context mContext;

    private List<TemplateHolder> mTemplates;

    private ShoppingOrderDetailBean mOrderDetailBean;

    public final static int TYPE_TOP = 0, TYPE_ADDRESS = 1, TYPE_LOGISTICS = 2, TYPE_GOODS = 3, TYPE_BOTTOM = 4, TYPE_ORDER = 5, TYPE_MESSAGE = 6;

    private final int STATE_2 = 2, STATE_3 = 3, STATE_4 = 4, STATE_5 = 5, STATE_6 = 6, STATE_7 = 7;
    private int mCurrState = STATE_2;

    public OrderDetailRecyclerViewAdapter(Context context, List<TemplateHolder> templates) {
        mContext = context;
        mTemplates = templates;
        mLayoutInflater = LayoutInflater.from(context);
        curPayType = OrderDialog.WECHAT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_TOP) {
            View view = mLayoutInflater.inflate(R.layout.order_detail_top_view, parent, false);
            viewHolder = new TopViewHolder(view);
        } else if (viewType == TYPE_ADDRESS) {
            View view = mLayoutInflater.inflate(R.layout.order_detail_address, parent, false);
            viewHolder = new AddressViewHolder(view);
        } else if (viewType == TYPE_LOGISTICS) {
            View view = mLayoutInflater.inflate(R.layout.order_detail_logistics_items, parent, false);
            viewHolder = new LogisticsViewHolder(view);
        } else if (viewType == TYPE_GOODS) {
            View view = mLayoutInflater.inflate(R.layout.order_detail_goods_items, parent, false);
            viewHolder = new GoodsViewHolder(view);
        } else if (viewType == TYPE_BOTTOM) {
            View view = mLayoutInflater.inflate(R.layout.order_detail_bottom_view, parent, false);
            viewHolder = new BottomViewHolder(view);
        } else if (viewType == TYPE_ORDER) {
            View view = mLayoutInflater.inflate(R.layout.order_detail_bottom_order, parent, false);
            viewHolder = new OrderViewHolder(view);
        } else if (viewType == TYPE_MESSAGE) {
            View view = mLayoutInflater.inflate(R.layout.order_detail_message, parent, false);
            viewHolder = new MessageViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (mTemplates.get(position).getTempType()) {
            case TYPE_TOP:
                TopViewHolder topViewHolder = (TopViewHolder) holder;
                mOrderDetailBean = (ShoppingOrderDetailBean) mTemplates.get(position).getItems();
                int state = mOrderDetailBean.getOrderStatus();
                mCurrState = state;
                topViewHolder.tvStateTime.setVisibility(View.GONE);
                topViewHolder.tvState.setVisibility(View.VISIBLE);
                if (state == STATE_2) {
                    topViewHolder.itemView.setBackgroundResource(R.drawable.mine_img_wait);
                    setTopValue(topViewHolder, R.drawable.order_detail_pay, R.string.order_detail_state2);
                    topViewHolder.tvStateTime.setFinishTxt(ResourceHelper.getString(R.string.order_detail_already_invalid));
                    topViewHolder.tvStateTime.setStopWhenCountdownFinish(true);
                    topViewHolder.tvStateTime.setStartTime(Math.abs(StringUtils.parseLong(mOrderDetailBean.getSurplusSeconds())), R.string.order_detail_total_time);
                    topViewHolder.tvStateTime.startCountdown();
                    topViewHolder.tvStateTime.setVisibility(View.VISIBLE);
                } else if (state == STATE_3) {
                    topViewHolder.itemView.setBackgroundResource(R.drawable.mine_img_wait);
                    setTopValue(topViewHolder, R.drawable.order_detail_had_pay, R.string.order_detail_state3);
                } else if (state == STATE_4) {
                    topViewHolder.itemView.setBackgroundResource(R.drawable.mine_img_transport);
                    topViewHolder.tvStateTime.setFinishTxt(ResourceHelper.getString(R.string.goods_already_receiver));
                    topViewHolder.tvStateTime.setStopWhenCountdownFinish(true);
                    topViewHolder.tvStateTime.setStartTime(Math.abs(StringUtils.parseLong(mOrderDetailBean.getSurplusSeconds())), R.string.order_detail_state4);
                    topViewHolder.tvStateTime.startCountdown();
                    topViewHolder.tvStateTime.setVisibility(View.VISIBLE);
//                    topViewHolder.tvState.setVisibility(View.GONE);
                    topViewHolder.sdvState.setImageResource(R.drawable.order_detail_sent);
                } else if (state == STATE_5) {
                    topViewHolder.itemView.setBackgroundResource(R.drawable.mine_img_transport);
                    setTopValue(topViewHolder, R.drawable.order_detail_no_pay, R.string.order_detail_state5);

                } else if (state == STATE_6) {
                    topViewHolder.itemView.setBackgroundResource(R.drawable.mine_img_transport);
                    setTopValue(topViewHolder, R.drawable.order_detail_buy_succeed, R.string.order_detail_state6);
                } else if (state == STATE_7) {
                    topViewHolder.itemView.setBackgroundResource(R.drawable.mine_img_transport);
                    setTopValue(topViewHolder, R.drawable.order_detail_comment, R.string.order_detail_state7);
                }
                break;
            case TYPE_ADDRESS:
                mOrderDetailBean = (ShoppingOrderDetailBean) mTemplates.get(position).getItems();
                AddressViewHolder addressViewHolder = (AddressViewHolder) holder;
                String receiver = mOrderDetailBean.getReceiver();
                String mobile = mOrderDetailBean.getMobile();
                String address = mOrderDetailBean.getDetailAddr();
                String district = mOrderDetailBean.getDetailDistrict();
                if (!StringUtils.isEmpty(receiver)) {
                    addressViewHolder.tvReceiver.setText(getText(R.string.order_goods_receiver, receiver));
                }
                if (!StringUtils.isEmpty(mobile)) {
                    addressViewHolder.tvMobile.setText(mobile);
                }
                if (!StringUtils.isEmpty(address)) {
                    addressViewHolder.tvAddress.setText(getText(R.string.order_goods_address, district + address));
                }
                break;
            case TYPE_LOGISTICS:
                ShoppingOrderLogisticsBean orderLogisticsBean = (ShoppingOrderLogisticsBean) mTemplates.get(position).getItems();
                LogisticsViewHolder logisticsViewHolder = (LogisticsViewHolder) holder;
                String content = orderLogisticsBean.getLastestInfo();
                String time = orderLogisticsBean.getLatestTime();
                if (orderLogisticsBean.getLogisticsStatus() == STATE_7) {//已发货 但没有物流信息
                    logisticsViewHolder.tvContent.setText(String.format(ResourceHelper.getString(R.string.order_detail_logistics_no), orderLogisticsBean.getLogisticsCompany(), orderLogisticsBean.getLogisticsNum()));
                } else {
                    if (!StringUtils.isEmpty(content)) {
                        logisticsViewHolder.tvContent.setText(content);
                    }
                    if (!StringUtils.isEmpty(time)) {
                        logisticsViewHolder.tvTime.setText(time);
                    }
                }
                logisticsViewHolder.layoutItems.setTag(orderLogisticsBean.getLogisticsId());
                logisticsViewHolder.layoutItems.setOnClickListener(this);
                break;
            case TYPE_GOODS:
                final ShoppingOrderDetailGoodsBean goodsBean = (ShoppingOrderDetailGoodsBean) mTemplates.get(position).getItems();
                GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
                String thumbUrl = goodsBean.getThumbUrl();
                String title = goodsBean.getTitle();
                String specName = goodsBean.getSpecName();
                String price = goodsBean.getGoodsPrice();
                if (!StringUtils.isEmpty(thumbUrl)) {
                    goodsViewHolder.sdvImg.setImageURI(Uri.parse(thumbUrl));
                }
                if (!StringUtils.isEmpty(title)) {
                    goodsViewHolder.tvGoodsName.setText(title);
                }
                if (!StringUtils.isEmpty(specName)) {
                    goodsViewHolder.tvGoodsSpec.setText("规格：" + specName);
                }
                if (!StringUtils.isEmpty(price)) {
//                    goodsViewHolder.tvGoodsPrice.setText(getText(R.string.order_detail_goods_price, price));
                    goodsViewHolder.tvGoodsPrice.setText(price);
                }
                goodsViewHolder.goods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message message = Message.obtain();
                        message.what = MsgDef.MSG_SHOW_GOODS_DETAIL_WINDOW;
                        message.obj = goodsBean.getGoodsId();
                        MsgDispatcher.getInstance().sendMessage(message);
                    }
                });
                goodsViewHolder.tvGoodsCount.setText(getText(R.string.order_detail_goods_count, goodsBean.getGoodsCount()));
                break;
            case TYPE_BOTTOM:
                mOrderDetailBean = (ShoppingOrderDetailBean) mTemplates.get(position).getItems();
                BottomViewHolder bottomViewHolder = (BottomViewHolder) holder;
                String sumPrice = mOrderDetailBean.getGoodsPrice();
                String freight = mOrderDetailBean.getPostageFee();
                String total = mOrderDetailBean.getTotalPrice();
                String coin = mOrderDetailBean.getUsageCoinPrice();
                if (!StringUtils.isEmpty(sumPrice)) {
                    bottomViewHolder.tvGoodsPrice.setText("¥" + sumPrice);
                }
                if (!StringUtils.isEmpty(freight)) {
                    if (Double.parseDouble(freight) == 0) {
                        bottomViewHolder.tvGoodsFreight.setText(ResourceHelper.getString(R.string.order_free_shipping));
                    } else {
                        bottomViewHolder.tvGoodsFreight.setText("¥" + freight);
                    }
                }
                if (!StringUtils.isEmpty(coin)) {
                    bottomViewHolder.tvTaCoin.setText("- ¥" + coin);
                }
                if (!StringUtils.isEmpty(total)) {
//                    bottomViewHolder.tvGoodsSum.setText(Html.fromHtml(String.format(ResourceHelper.getString(R.string.order_detail_goods_total), total)));
                    bottomViewHolder.tvGoodsSum.setText("¥ " + total);
                }
                break;
            case TYPE_ORDER:
                mOrderDetailBean = (ShoppingOrderDetailBean) mTemplates.get(position).getItems();
                OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
                if (mCurrState == STATE_2) {
                    orderViewHolder.llPayType.setVisibility(View.VISIBLE);
                } else {
                    orderViewHolder.llPayType.setVisibility(View.GONE);
                }
                String orderNo = mOrderDetailBean.getOrderNo();
                String orderTime = Utils.LONG_DATE_FORMATER.format(new Date(mOrderDetailBean.getCreatetime() * 1000));
                String surplusSeconds = mOrderDetailBean.getSurplusSeconds();
                if (!StringUtils.isEmpty(orderNo)) {
                    orderViewHolder.tvOrderNo.setText(getText(R.string.order_detail_order_no, orderNo));
                }
                if (!StringUtils.isEmpty(orderTime)) {
                    orderViewHolder.tvCreateTime.setText(getText(R.string.order_detail_order_create, orderTime));
                }
//                if (!StringUtils.isEmpty(surplusSeconds) && mCurrState != STATE_2) {//已取消不能失效
//                    orderViewHolder.layoutOverdue.setVisibility(View.VISIBLE);
//                    orderViewHolder.tvOverdue.setStartTime(Math.abs(StringUtils.parseLong(surplusSeconds)), R.string.order_detail_total_time);
//                    orderViewHolder.tvOverdue.startCountdown();
//                } else {
                orderViewHolder.layoutOverdue.setVisibility(View.GONE);
//                }
                break;
            case TYPE_MESSAGE:
                mOrderDetailBean = (ShoppingOrderDetailBean) mTemplates.get(position).getItems();
                MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
                String message = mOrderDetailBean.getUserRemark();
                if (!StringUtils.isEmpty(message)) {
                    messageViewHolder.tvMessage.setText(message);
                }
                break;
        }
    }

    /**
     * 设置状态
     *
     * @param topViewHolder
     * @param resId
     * @param textResId
     */
    private void setTopValue(TopViewHolder topViewHolder, int resId, int textResId) {
        topViewHolder.tvState.setText(ResourceHelper.getString(textResId));
        topViewHolder.sdvState.setImageResource(resId);
    }

    private CharSequence getText(int resId, Object val) {
        return String.format(ResourceHelper.getString(resId), val);
    }

    @Override
    public int getItemCount() {
        return mTemplates.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mTemplates.get(position).getTempType();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_items) {
//            Message message = Message.obtain();
//            message.what = MsgDef.MSG_SHOW_ORDER_LOGISTICS_WINDOW;
            mOrderDetailBean.selectLogisticsId = v.getTag().toString();
//            message.obj = mOrderDetailBean;
//            MsgDispatcher.getInstance().sendMessage(message)
// ;
            OrderLogisticsActivity.startActivity(mContext, mOrderDetailBean);
        }
//        else if (v.getId() == R.id.layout_items) {
//            Message message = Message.obtain();
//            message.what = MsgDef.MSG_SHOW_PRODUCT_DETAIL_WINDOW;
//            message.obj = String.valueOf(v.getTag());
//            MsgDispatcher.getInstance().sendMessage(message);
//        }
    }


    public static class TopViewHolder extends RecyclerView.ViewHolder {
        ImageView sdvState;
        TextView tvState;
        ShoppingCountdownView tvStateTime;

        public TopViewHolder(View itemView) {
            super(itemView);
            sdvState = (ImageView) itemView.findViewById(R.id.sdv_state);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            tvStateTime = (ShoppingCountdownView) itemView.findViewById(R.id.tv_state_time);
        }
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvReceiver, tvMobile, tvAddress;
        ImageView ivAddressAdd;

        public AddressViewHolder(View itemView) {
            super(itemView);
            tvReceiver = (TextView) itemView.findViewById(R.id.tv_receiver);
            tvMobile = (TextView) itemView.findViewById(R.id.tv_mobile);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            ivAddressAdd = (ImageView) itemView.findViewById(R.id.tv_address_add);
            ivAddressAdd.setVisibility(View.GONE);
        }
    }

    public static class LogisticsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutItems;
        TextView tvContent, tvTime;

        public LogisticsViewHolder(View itemView) {
            super(itemView);
            layoutItems = (LinearLayout) itemView.findViewById(R.id.layout_items);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    public static class GoodsViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvImg;
        TextView tvGoodsName, tvGoodsSpec, tvGoodsCount;
        LinearLayout goods;
        PriceView tvGoodsPrice;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            sdvImg = (SimpleDraweeView) itemView.findViewById(R.id.sdv_img);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsSpec = (TextView) itemView.findViewById(R.id.tv_goods_spec);
            tvGoodsPrice = (PriceView) itemView.findViewById(R.id.tv_goods_price);
            tvGoodsCount = (TextView) itemView.findViewById(R.id.tv_goods_count);
            goods = (LinearLayout) itemView.findViewById(R.id.order_detail_goods);
        }
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        TextView tvGoodsPrice, tvGoodsFreight, tvGoodsSum, tvTaCoin;

        public BottomViewHolder(View itemView) {
            super(itemView);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tvGoodsFreight = (TextView) itemView.findViewById(R.id.tv_goods_freight);
            tvGoodsSum = (TextView) itemView.findViewById(R.id.tv_goods_sum_pay);
            tvTaCoin = (TextView) itemView.findViewById(R.id.tv_ta_coin);
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutOverdue, llPayType;
        TextView tvOrderNo, tvCreateTime;
        ShoppingCountdownView tvOverdue;
        CheckBox mCBWxPay, mCBAliPay;
        TextView mWxPay, mAliPay;

        public OrderViewHolder(View itemView) {
            super(itemView);
            layoutOverdue = (LinearLayout) itemView.findViewById(R.id.layout_overdue);
            tvOrderNo = (TextView) itemView.findViewById(R.id.tv_order_no);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_order_create);
            tvOverdue = (ShoppingCountdownView) itemView.findViewById(R.id.tv_order_overdue);
            llPayType = (LinearLayout) itemView.findViewById(R.id.ll_pay_type);
            llPayType.setVisibility(View.GONE);
            mCBWxPay = (CheckBox) itemView.findViewById(R.id.cb_wxPay);
            mCBAliPay = (CheckBox) itemView.findViewById(R.id.cb_aliPay);
            mWxPay = (TextView) itemView.findViewById(R.id.wxPay);
            mAliPay = (TextView) itemView.findViewById(R.id.aliPay);
            mWxPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCBAliPay.setChecked(false);
                    mCBWxPay.setChecked(true);
                    curPayType = OrderDialog.WECHAT; // 设置支付类型
                }
            });
            mAliPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCBAliPay.setChecked(true);
                    mCBWxPay.setChecked(false);
                    curPayType = OrderDialog.ALIPAY;
                }
            });
        }
    }

    public static int curPayType = OrderDialog.WECHAT;

    public int getCurPayType() {
        return curPayType;
    }
}
