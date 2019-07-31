package com.laka.live.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.ShoppingHelper;
import com.laka.live.shopping.bean.ShoppingTopBean;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by zhxu on 2016/4/29.
 * Email:357599859@qq.com
 */
public class ShoppingTopListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<ShoppingTopBean> mList;

    private final static int mCommonDimen = ResourceHelper.getDimen(R.dimen.space_1);

    public ShoppingTopListAdapter(Context context, List<ShoppingTopBean> list) {
        mContext = context;
        mList = list;
    }

    private LinearLayout createLinearLayout(int marginTop) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.color.white);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = marginTop;
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int marginTop = mCommonDimen * 10;
        return new ItemViewHolder(createLinearLayout(marginTop));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ShoppingTopBean topBean = mList.get(position);
        ItemViewHolder typeViewHolder = (ItemViewHolder) holder;
        String imageUrl = topBean.getImageUrl();
        if (StringUtils.isNotEmpty(imageUrl)) {
            typeViewHolder.sdvImage.setImageURI(Uri.parse(imageUrl));
        } else {
            typeViewHolder.sdvImage.setBackgroundResource(R.color.mask_color);
        }
        typeViewHolder.sdvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingHelper.showShoppingTopListDetailWindow(topBean.getToplistId());

                //统计CATEGORY_RANKLIST
//                StatsModel.stats(StatsKeyDef.CATEGORY_RANKLIST, StatsKeyDef.SPEC_KEY, topBean.getToplistId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView sdvImage;

        public ItemViewHolder(LinearLayout itemView) {
            super(itemView);
            int screenWidth = HardwareUtil.screenWidth;
            sdvImage = new SimpleDraweeView(mContext);
            itemView.addView(sdvImage, new LinearLayout.LayoutParams(screenWidth, screenWidth / 2));
            RippleEffectHelper.addRippleEffectInView(sdvImage);
        }
    }
}
