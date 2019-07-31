package com.laka.live.shopping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.ShoppingHelper;
import com.laka.live.shopping.bean.ShoppingCategoriesBean;
import com.laka.live.shopping.bean.ShoppingCategoriesColumnBean;
import com.laka.live.shopping.model.ShoppingCategoryInfo;
import com.laka.live.shopping.model.TemplateHolder;
import com.laka.live.shopping.widget.ShoppingViewGroup;
import com.laka.live.ui.widget.FlowLayout;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by zhxu on 2016/4/29.
 * Email:357599859@qq.com
 */
public class ShoppingCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int TEMP_GROUP = 1;
    public final static int TEMP_TYPE = 2;

    private Context mContext;

    private List<TemplateHolder> mList;

    private final static int mCommonDimen = ResourceHelper.getDimen(R.dimen.space_1);

    public ShoppingCategoriesAdapter(Context context, List<TemplateHolder> list) {
        mContext = context;
        mList = list;
    }

    private LinearLayout createLinearLayout(int marginTop) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.color.white);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = marginTop;
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int marginTop = mCommonDimen * 15;
        if (viewType == TEMP_GROUP) {
            return new GroupViewHolder(createLinearLayout(marginTop));
        } else if (viewType == TEMP_TYPE) {
            return new TypeViewHolder(createLinearLayout(marginTop));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShoppingCategoriesBean categoriesBean = (ShoppingCategoriesBean) mList.get(position).getItems();
        List<ShoppingCategoriesColumnBean> list = categoriesBean.getColumn();
        int viewType = mList.get(position).getTempType();
        if (viewType == TEMP_GROUP) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            if (list != null) {
                groupViewHolder.viewGroup.setChildCount(list.size());
                groupViewHolder.createItems(list);
            }
        } else if (viewType == TEMP_TYPE) {
            TypeViewHolder typeViewHolder = (TypeViewHolder) holder;
            if (list != null) {
                typeViewHolder.createItems(list);
            }
            String title = categoriesBean.getTitle();
            if (StringUtils.isNotEmpty(title)) {
                typeViewHolder.tvTitle.setText(title);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getTempType();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 组合模块
     */
    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ShoppingViewGroup viewGroup;

        public GroupViewHolder(LinearLayout itemView) {
            super(itemView);
            viewGroup = new ShoppingViewGroup(mContext);
            viewGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            itemView.addView(viewGroup);
        }

        public void createItems(List<ShoppingCategoriesColumnBean> list) {
            if (viewGroup != null && viewGroup.getChildCount() > 0) {
                viewGroup.removeAllViewsInLayout();
            }

            int imageWidth = HardwareUtil.screenWidth / 2;

            for (ShoppingCategoriesColumnBean columnBean : list) {
                ShoppingCategoryInfo categoryInfo = new ShoppingCategoryInfo();
                categoryInfo.title = columnBean.getTitle();
                categoryInfo.type = StringUtils.parseInt(columnBean.getAttrKey());
                categoryInfo.typeId = StringUtils.parseInt(columnBean.getAttrValue());

                FrameLayout frameLayout = new FrameLayout(mContext);
                frameLayout.setTag(categoryInfo);
                frameLayout.setOnClickListener(this);

                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageWidth, imageWidth * 4 / 9);
                layoutParams.gravity = Gravity.CENTER;
                SimpleDraweeView sdvImage = new SimpleDraweeView(mContext);
                sdvImage.setLayoutParams(layoutParams);
                String imageUrl = columnBean.getImageUrl();
                if (StringUtils.isNotEmpty(imageUrl)) {
                    sdvImage.setImageURI(Uri.parse(imageUrl));
                }
                frameLayout.addView(sdvImage);

                layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.CENTER;
                TextView tvTitle = new TextView(mContext);
                tvTitle.setGravity(Gravity.CENTER);
                tvTitle.setLayoutParams(layoutParams);
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_16));
                tvTitle.setTextColor(ResourceHelper.getColor(R.color.white));
                String title = columnBean.getTitle();
                if (StringUtils.isNotEmpty(title)) {
                    tvTitle.setText(title);
                    frameLayout.addView(tvTitle);
                }

                viewGroup.addView(frameLayout);
                RippleEffectHelper.addRippleEffectInView(frameLayout);
            }
        }

        @Override
        public void onClick(View v) {
            ShoppingCategoryInfo categoryInfo = (ShoppingCategoryInfo) v.getTag();
            ShoppingHelper.handleClick(categoryInfo);

            //统计CATEGORY_CLICK
//            StatsModel.stats(StatsKeyDef.CATEGORY_CLICK, "spec3", categoryInfo.title);
        }
    }

    /**
     * 分类模块
     */
    public class TypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FlowLayout flowLayout;
        TextView tvTitle;

        private int margin = (HardwareUtil.screenWidth - mCommonDimen * 60 * 4) / 8;

        public TypeViewHolder(LinearLayout itemView) {
            super(itemView);
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0, mCommonDimen * 10, 0, mCommonDimen * 10);
            linearLayout.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mCommonDimen * 40, mCommonDimen);
            layoutParams.leftMargin = mCommonDimen * 15;
            layoutParams.rightMargin = mCommonDimen * 15;

            linearLayout.addView(createLine(layoutParams));

            tvTitle = new TextView(mContext);
            tvTitle.setGravity(Gravity.CENTER);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_14));
            tvTitle.setTextColor(ResourceHelper.getColor(R.color.shopping_title));
            linearLayout.addView(tvTitle);
            itemView.addView(linearLayout);

            linearLayout.addView(createLine(layoutParams));

            flowLayout = new FlowLayout(mContext);
            flowLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            itemView.addView(flowLayout);
        }

        private TextView createLine(LinearLayout.LayoutParams layoutParams) {
            TextView line = new TextView(mContext);
            line.setLayoutParams(layoutParams);
            line.setBackgroundResource(R.color.shopping_categories_line);
            return line;
        }

        public void createItems(List<ShoppingCategoriesColumnBean> list) {
            flowLayout.removeAllViewsInLayout();
            for (ShoppingCategoriesColumnBean columnBean : list) {
                final ShoppingCategoryInfo categoryInfo = new ShoppingCategoryInfo();
                categoryInfo.title = columnBean.getTitle();
                categoryInfo.type = StringUtils.parseInt(columnBean.getAttrKey());
                categoryInfo.typeId = StringUtils.parseInt(columnBean.getAttrValue());

                LinearLayout itemsView = new LinearLayout(mContext);
                itemsView.setLayoutParams(new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                itemsView.setOrientation(LinearLayout.VERTICAL);
                itemsView.setGravity(Gravity.CENTER);

                int width = mCommonDimen * 60;
                SimpleDraweeView sdvImage = new SimpleDraweeView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
                layoutParams.setMargins(margin, 0, margin, 0);
                sdvImage.setLayoutParams(layoutParams);
                itemsView.addView(sdvImage);
                String imageUrl = columnBean.getImageUrl();
                if (StringUtils.isNotEmpty(imageUrl)) {
                    sdvImage.setImageURI(Uri.parse(imageUrl));
                } else {
                    sdvImage.setBackgroundResource(R.color.black);
                }

                TextView tvType = new TextView(mContext);
                tvType.setGravity(Gravity.CENTER);
                tvType.setMaxLines(1);
                tvType.setPadding(0, margin, 0, margin);
                tvType.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_14));
                tvType.setTextColor(Color.parseColor("#737373"));
                final String title = columnBean.getTitle();
                if (StringUtils.isNotEmpty(title)) {
                    tvType.setText(title);
                    itemsView.addView(tvType);
                }
                flowLayout.addView(itemsView);
                RippleEffectHelper.addRippleEffectInView(itemsView);

                itemsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShoppingHelper.handleClick(categoryInfo);
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            ShoppingCategoryInfo categoryInfo = (ShoppingCategoryInfo) v.getTag();
            ShoppingHelper.handleClick(categoryInfo);

            //统计CATEGORY_CLICK
//            StatsModel.stats(StatsKeyDef.CATEGORY_CLICK, "spec3", categoryInfo.title);
        }
    }
}
