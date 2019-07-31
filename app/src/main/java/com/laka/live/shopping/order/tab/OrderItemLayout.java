package com.laka.live.shopping.order.tab;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.bean.ShoppingOrderGoodsBean;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by gangqing on 2016/5/13.
 * Email:denggangqing@ta2she.com
 */
public class OrderItemLayout extends LinearLayout {
    private static final int ORDER_STATUE_WAIT_EVALUATE = 7;
    private int mOrderStatus;

    public OrderItemLayout(Context context) {
        this(context, null);
    }

    public OrderItemLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public void setupOrderItemLayout(List<ShoppingOrderGoodsBean> list, int orderStatus) {
        mOrderStatus = orderStatus;
        this.removeAllViewsInLayout();
        if (list == null) {
            return;
        }
        int size = list.size();
        ShoppingOrderGoodsBean bean;
        LayoutParams lp;
        for (int i = 0; i < size; i++) {
            bean = list.get(i);
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(createOrderItem(bean), lp);
            if (i != size - 1) {
                lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen.space_05));
                addView(createLine(), lp);
            }
        }
    }

    private View createLine() {
        View view = new View(getContext());
        view.setBackgroundColor(ResourceHelper.getColor(R.color.shopping_common_line));
        return view;
    }

    private View createOrderItem(final ShoppingOrderGoodsBean goodsBean) {
        View view = View.inflate(getContext(), R.layout.order_list_items, null);
        SimpleDraweeView sdvImage = (SimpleDraweeView) view.findViewById(R.id.items_order_img);
        TextView tvCount = (TextView) view.findViewById(R.id.items_order_count);
        TextView tvPrice = (TextView) view.findViewById(R.id.items_order_price);
        TextView tvTitle = (TextView) view.findViewById(R.id.items_order_title);
        TextView tvEvaluate = (TextView) view.findViewById(R.id.order_deal_with);

        final String thumbUrl = goodsBean.getThumbUrl();
        final String title = goodsBean.getTitle();
        final String price = goodsBean.getGoodsPrice();
        //图片
        if (!StringUtils.isEmpty(thumbUrl)) {
            sdvImage.setImageURI(Uri.parse(thumbUrl));
            sdvImage.setAspectRatio(1);
        }
        //商品名称
        if (!StringUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        //数量
        tvCount.setText("x" + goodsBean.getGoodsCount());
        //价格
        if (!StringUtils.isEmpty(price)) {
            tvPrice.setText("¥" + price);
        }

        //规格
        TextView tvGoodsSpec = (TextView) view.findViewById(R.id.tv_goods_spec);
        tvGoodsSpec.setText("规格:"+goodsBean.getSpecName());

        //测评
        int postId = goodsBean.getPostId();
        if (mOrderStatus == ORDER_STATUE_WAIT_EVALUATE && postId == 0) {
            tvEvaluate.setVisibility(View.VISIBLE);
            tvEvaluate.setText(ResourceHelper.getString(R.string.order_review_send_evaluate));
            tvEvaluate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    sentEvaluateEvent(goodsBean);
                }
            });
        } else if (mOrderStatus == ORDER_STATUE_WAIT_EVALUATE && postId != 0) {
            tvEvaluate.setVisibility(View.VISIBLE);
            tvEvaluate.setText(ResourceHelper.getString(R.string.order_review_edit_evaluate));
            tvEvaluate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editEvaluateEvent(goodsBean);
                }
            });
        } else {
            tvEvaluate.setVisibility(View.GONE);
        }
        return view;
    }

    //发测评
    private void sentEvaluateEvent(ShoppingOrderGoodsBean goodsBean) {
        // TODO: 2017/7/14 发测评
//        EvaluateWindowParams helper = new EvaluateWindowParams();
//        ShoppingOrderDetailGoodsBean bean = new ShoppingOrderDetailGoodsBean();
//        bean.setGoodsId(String.valueOf(goodsBean.getGoodsId()));
//        bean.setGoodsCount(goodsBean.getGoodsCount());
//        bean.setGoodsPrice(goodsBean.getGoodsPrice());
//        bean.setLogisticsId(goodsBean.getLogisticsId());
//        bean.setTitle(goodsBean.getTitle());
//        bean.setThumbUrl(goodsBean.getThumbUrl());
//        bean.setOrderGoodsId(StringUtils.parseInt(goodsBean.getOrderGoodsId()));
//        bean.setSpecName(goodsBean.getSpecName());
//        bean.setCateId(goodsBean.getCateId());
//        helper.goodsBean = bean;
//        helper.fromType = EvaluateWindowParams.FROM_TYPE_ORDER;
//        Message message = new Message();
//        message.what = MsgDef.MSG_START_EVALUATE;
//        message.obj = helper;
//        MsgDispatcher.getInstance().sendMessage(message);
    }

    //编辑测评
    private void editEvaluateEvent(ShoppingOrderGoodsBean goodsBean) {
        // TODO: 2017/7/14 编辑测评 
//        OpenPostDetailHelper helper = new OpenPostDetailHelper();
//        helper.setPostId(goodsBean.getPostId());
//        helper.setPostTypeFromWhere(CommunityConstant.POST_TYPE_FROM_POST);
//
//        Message message = Message.obtain();
//        message.what = MsgDef.MSG_SHOW_COMMUNITY_POST_DETAIL_WINDOW;
//        message.arg1 = goodsBean.getPostId();
//        message.obj = helper;
//        MsgDispatcher.getInstance().sendMessage(message);
    }


}
