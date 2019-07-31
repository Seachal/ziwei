package com.laka.live.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.Product;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/3/29.
 */
public class ProductAdapter extends BaseAdapter<Product, BaseAdapter.ViewHolder> {

    private static final int TYPE_NORMAL = 0;

    private static final int TYPE_FOOTER = 1;

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (count > 0) {
            count += 1;
        }
        return count;
    }

    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product_layout, parent, false);
            return new ViewHolder(view);
        } else {
            Context context = parent.getContext();
            TextView view = new TextView(context);
            view.setTextAppearance(context, R.style.ProductFooter);
            view.setText(R.string.if_prepaid_problems_tips);
            view.setTextColor(ResourceHelper.getColor(R.color.color555555));
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.dip2px(context, 75.3f));
            params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            params.rightMargin = params.leftMargin;
            params.topMargin = Utils.dip2px(context, 7.5f);
            view.setLayoutParams(params);
            return new FooterHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(BaseAdapter.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == TYPE_NORMAL) {
            ViewHolder viewHolder = (ViewHolder) holder;
            super.onBindViewHolder(holder, position);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(position);
                }
            });
        }
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<Product> {

        public TextView count;

        public TextView tips;

        public Button button;

        public View divider;

        public ViewHolder(View itemView) {
            super(itemView);

            count = (TextView) itemView.findViewById(R.id.count);
            tips = (TextView) itemView.findViewById(R.id.tips);
            button = (Button) itemView.findViewById(R.id.btn);

            divider = itemView.findViewById(R.id.divider);
        }


        @Override
        public void update(BaseAdapter adapter, int position, Product product) {
            count.setText(String.valueOf(product.getCoins()));
            tips.setText(product.getDescription());
            button.setText(product.getPrice());

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) divider.getLayoutParams();

            if (position == adapter.getItemCount() - 2) {
                params.leftMargin = 0;
            } else {
                params.leftMargin = Utils.dip2px(itemView.getContext(), 48f);
            }
        }
    }

    public class FooterHolder extends BaseAdapter.ViewHolder<Object> {
        public FooterHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Object o) {

        }
    }
}
