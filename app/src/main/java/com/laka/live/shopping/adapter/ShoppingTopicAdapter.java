package com.laka.live.shopping.adapter;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.viewholder.GoodsViewHolder;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.Utils;

import java.util.List;

/**
 * @ClassName: ShoppingTopicAdapter
 * @Description: 商城专题
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class ShoppingTopicAdapter extends BaseAdapter<Object, BaseAdapter.ViewHolder> {
    private final static int TYPE_THUMB = 0;
    private final static int TYPE_DATA = 1;

    private Context mContext;

    public ShoppingTopicAdapter(Context context, List<Object> data) {
        mDatas = data;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_THUMB;
            default:
                return TYPE_DATA;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DATA:
                return new GoodsViewHolder(RippleEffectHelper.addRippleEffectInView(
                        LayoutInflater.from(mContext).inflate(R.layout.item_shopping_good, null)));
            default:
                return new ThumbViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.item_shopping_topic_thumb, null));
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params != null
                && params instanceof StaggeredGridLayoutManager.LayoutParams)
            switch (holder.getItemViewType()) {
                case TYPE_THUMB:
                    ((StaggeredGridLayoutManager.LayoutParams) params).rightMargin = Utils.dip2px(mContext, 6);
                    ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
                    break;
                default:
                    ((StaggeredGridLayoutManager.LayoutParams) params).rightMargin = 0;
                    break;
            }
    }

    private class ThumbViewHolder extends BaseAdapter.ViewHolder<String> {
        private SimpleDraweeView mThumbSdv;

        public ThumbViewHolder(View itemView) {
            super(itemView);

            mThumbSdv = (SimpleDraweeView) itemView.findViewById(R.id.thumb_sdv);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mThumbSdv.getLayoutParams();
            params.height = Utils.getScreenWidth(mContext) * 4 / 7;
            mThumbSdv.setLayoutParams(params);
        }

        @Override
        public void update(BaseAdapter adapter, int position, String s) {
            if (Utils.isEmpty(s)) {
                return;
            }

            ImageUtil.loadImage(mThumbSdv, s);
        }
    }
}
