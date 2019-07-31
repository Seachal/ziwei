package com.laka.live.shopping.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.shopping.activity.OrderDetailActivity;
import com.laka.live.shopping.bean.ShoppingOrderListBean;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.order.model.OrderPayHelper;
import com.laka.live.shopping.order.model.OrderResultInfo;
import com.laka.live.shopping.order.tab.OrderItemLayout;
import com.laka.live.shopping.widget.PriceView;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhxu on 2015/12/23.
 * Email:357599859@qq.com
 */
public class OrderRecyclerViewAdapter extends BaseAdapter {//RecyclerView.Adapter<RecyclerView.ViewHolder>
    private static final String TAG = "OrderRecyclerViewAdapter";
    private Context mContext;
    private OrderPayHelper mPayHelper;
    private LayoutInflater mLayoutInflater;
    private List<ShoppingOrderListBean> mOrderList;
    private RecyclerAdapterCallBack mCallBack;
    private final int STATE_WAIT_PAY = 2, STATE_WAIT_DELIVER = 3, STATE_WAIT_RECEIVING = 4, STATE_WAIT_EVALUATE = 7, STATE_CANCEL = 5, STATE_FINSIH = 6;
    public static final String ORDER_CONFIRM_RECEIVING = "confirmOrder";
    public static final String ORDER_CANCEL_ORDER = "cancelOrder";
    public static final String ORDER_DELETE_ORDER = "deleteOrder";
    public OrderRecyclerViewAdapter(Context context, List<ShoppingOrderListBean> orderList) {
        mOrderList = orderList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mPayHelper = new OrderPayHelper(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.order_recycler_view_items, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ShoppingOrderListBean orderList = mOrderList.get(position);
        ViewHolder mHolder = (ViewHolder) holder;
        setStyle(holder, position);
        mHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderResultInfo orderResultInfo = new OrderResultInfo();
                orderResultInfo.orderId = String.valueOf(orderList.getOrderId());
//                Message message = Message.obtain();
//                message.what = MsgDef.MSG_SHOW_ORDER_DETAIL_WINDOW;
//                message.obj = orderResultInfo;
//                MsgDispatcher.getInstance().sendMessage(message);
                OrderDetailActivity.startActivity(mContext, orderResultInfo);
            }
        });
        //订单号
        final String orderNo = orderList.getOrderNo();
        if (!StringUtils.isEmpty(orderNo)) {
            mHolder.tvOrderNum.setText("订单号: " + orderNo);
        }
        //订单详情
        mHolder.listOrder.setupOrderItemLayout(orderList.getGoods(), orderList.getOrderStatus());
        //订单价格
        Log.d(TAG," price="+orderList.getOrderPrice());
        mHolder.tvPrice.setText(orderList.getOrderPrice());
        //待付款->取消订单
        mHolder.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleTextDialog dialog = new SimpleTextDialog(mContext);
                dialog.addTitle(R.string.order_item_cancel_hint_title);
                dialog.setText(R.string.order_item_cancel_hint_text);
                dialog.addYesNoButton(ResourceHelper.getString(R.string.cancel), ResourceHelper.getString(R.string.yes));
                dialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
                dialog.setOnClickListener(new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId != GenericDialog.ID_BUTTON_YES) {
                            if (mCallBack != null) {
                                mCallBack.onRequest(position, ORDER_CANCEL_ORDER);
                            }
                        }
                        return false;
                    }
                });
                dialog.show();
            }
        });
        //待付款->去付款
        mHolder.btToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPayHelper.setCallBack(new OrderPayHelper.OrderPayHelperCallBack() {
                    @Override
                    public void onCommonPay() {
                        CommonConst.ORDERNUM = orderNo;
                        CommonConst.ORDERID = orderList.getOrderId()+"" ;
                        CommonConst.TOTALPRICE = Double.parseDouble(orderList.getOrderPrice());
                    }
                });
                mPayHelper.dialogShow();
            }
        });
        //等待发货->提醒发货
        mHolder.btWaitDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showToast(  R.string.order_hint_deliver, Toast.LENGTH_SHORT);
            }
        });
        //待收货->确认收货
        mHolder.btWaitReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onRequest(position, ORDER_CONFIRM_RECEIVING);
                }
            }
        });

        mHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onRequest(position, ORDER_DELETE_ORDER);
                }
            }
        });

        //查看物流
        mHolder.btnLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG," btnLogistics onClick");
                if (mCallBack != null) {
                    mCallBack.onClickLogistics(orderList.getOrderId()+"");
                }
            }
        });

        mHolder.btnLogistics.setVisibility(View.GONE);
        switch (orderList.getOrderStatus()){
            case STATE_CANCEL:
                mHolder.tvStatus.setText("已取消");
                break;
            case STATE_FINSIH:
                mHolder.tvStatus.setText("交易完成");
                mHolder.btnLogistics.setVisibility(View.VISIBLE);
                break;
            case STATE_WAIT_DELIVER:
                mHolder.tvStatus.setText("等待卖家发货");
                break;
            case STATE_WAIT_RECEIVING:
                mHolder.tvStatus.setText("等待买家收货");
                mHolder.btnLogistics.setVisibility(View.VISIBLE);
                break;
            case STATE_WAIT_PAY:
                mHolder.tvStatus.setText("等待买家付款");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    private void setStyle(RecyclerView.ViewHolder holder, int position) {
        ShoppingOrderListBean orderList = mOrderList.get(position);
        Log.d(TAG," setStyle status="+orderList.getOrderStatus());
        ViewHolder mHolder = (ViewHolder) holder;
        Map<Integer, View> viewMap = mHolder.viewMap;
        View aimView = viewMap.get(orderList.getOrderStatus());
        for (View view : viewMap.values()) {
            view.setVisibility(View.GONE);
        }
        if (aimView != null&&orderList.getOrderStatus()!=STATE_WAIT_DELIVER&&orderList.getOrderStatus()!= STATE_FINSIH) {
            aimView.setVisibility(View.VISIBLE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Map<Integer, View> viewMap = new HashMap<>();
        OrderItemLayout listOrder;
        TextView tvOrderNum, btCancel, btToPay, btWaitDeliver, btWaitReceiving,btnLogistics,tvCancel, tvFinish,tvStatus;
        LinearLayout lyWaitPay, itemLayout;
        PriceView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            listOrder = (OrderItemLayout) itemView.findViewById(R.id.items_order_list);
            tvOrderNum = (TextView) itemView.findViewById(R.id.items_order_no);
            tvPrice = (PriceView) itemView.findViewById(R.id.order_item_price);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.order_item_layout);

            lyWaitPay = (LinearLayout) itemView.findViewById(R.id.order_bottom_wait_pay);
            btnLogistics = (TextView) itemView.findViewById(R.id.order_bottom_logistics);

            btCancel = (TextView) itemView.findViewById(R.id.order_bottom_wait_pay_cancel);
            btToPay = (TextView) itemView.findViewById(R.id.order_bottom_wait_pay_to_pay);

            btWaitDeliver = (TextView) itemView.findViewById(R.id.order_bottom_wait_deliver);
            btWaitReceiving = (TextView) itemView.findViewById(R.id.order_bottom_wait_receiving);
            tvCancel = (TextView) itemView.findViewById(R.id.order_bottom_cancel);
            tvFinish = (TextView) itemView.findViewById(R.id.order_bottom_finish);

            viewMap.put(STATE_WAIT_PAY, lyWaitPay);
            viewMap.put(STATE_WAIT_DELIVER, btWaitDeliver);
            viewMap.put(STATE_WAIT_RECEIVING, btWaitReceiving);
            viewMap.put(STATE_CANCEL, tvCancel);
            viewMap.put(STATE_FINSIH, tvFinish);
        }
    }

    public void setRecyclerAdapterCallBack(RecyclerAdapterCallBack callBack) {
        mCallBack = callBack;
    }

    public interface RecyclerAdapterCallBack {
        void onRequest(int position, String requestType);
        void onClickLogistics(String orderId);
    }



}
