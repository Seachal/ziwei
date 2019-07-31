package com.laka.live.shopping.adapter;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.activity.ShoppingTopicActivity;
import com.laka.live.shopping.bean.newversion.GoodsCate;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingHomeCateBean;
import com.laka.live.shopping.bean.newversion.ShoppingHomeTitleBean;
import com.laka.live.shopping.bean.newversion.ShoppingHomeTopics;
import com.laka.live.shopping.viewholder.GoodsViewHolder;
import com.laka.live.shopping.widget.HomeClassifyView;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.Utils;

import java.util.List;

/**
 * @ClassName: ShoppingHomeAdapter
 * @Description: 商城首页数据适配器
 * @Author: chuan
 * @Version: 1.0
 * @Date: 17/07/2017
 */

public class ShoppingHomeAdapter<T> extends BaseAdapter<T, BaseAdapter.ViewHolder> {
    private final static int TYPE_WRONG = -1;
    private final static int TYPE_CATE = 0;
    private final static int TYPE_TITLE = 1;
    private final static int TYPE_GOODS = 2;
    private final static int TYPE_TOPIC = 3;

    private Context mContext;

    public ShoppingHomeAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params != null
                && params instanceof StaggeredGridLayoutManager.LayoutParams)
            switch (holder.getItemViewType()) {
                case TYPE_CATE:
                case TYPE_TITLE:
                case TYPE_TOPIC:
                    ((StaggeredGridLayoutManager.LayoutParams) params).rightMargin = Utils.dip2px(mContext, 6);
                    ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
                    break;
                default:
                    ((StaggeredGridLayoutManager.LayoutParams) params).rightMargin = 0;
                    break;
            }
    }

    @Override
    public int getItemViewType(int position) {
        T data = getItem(position);
        if (data instanceof ShoppingHomeCateBean) {
            return TYPE_CATE;
        } else if (data instanceof ShoppingHomeTitleBean) {
            return TYPE_TITLE;
        } else if (data instanceof ShoppingGoodsBaseBean) {
            return TYPE_GOODS;
        } else if (data instanceof ShoppingHomeTopics) {
            return TYPE_TOPIC;
        } else {
            return TYPE_WRONG;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CATE:
                return new ClassifyViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_home_classify, null));
            case TYPE_TITLE:
                return new TitleViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_shopping_home_title, null, false));
            case TYPE_GOODS:
                return new GoodsViewHolder(RippleEffectHelper.addRippleEffectInView(
                        LayoutInflater.from(mContext).inflate(R.layout.item_shopping_good, null)));
            case TYPE_TOPIC:
                return new TopicViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_shopping_home_topic, null));
            default:
                throw new IllegalArgumentException();
        }
    }


    //一级分类
    private class ClassifyViewHolder extends BaseAdapter.ViewHolder<ShoppingHomeCateBean> {

        private LinearLayout mLineOne;
        private LinearLayout mLineTwo;

        public ClassifyViewHolder(View itemView) {
            super(itemView);

            mLineOne = (LinearLayout) itemView.findViewById(R.id.line_one);
            mLineTwo = (LinearLayout) itemView.findViewById(R.id.line_two);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ShoppingHomeCateBean shoppingHomeCateBean) {
            List<GoodsCate> goodsCates = shoppingHomeCateBean.getGoodsCates();
            GoodsCate goodsCate;
            HomeClassifyView classifyView;
            mLineTwo.setVisibility(View.GONE);
            mLineOne.removeAllViews();
            mLineTwo.removeAllViews();
            for (int p = 0; p < goodsCates.size(); p++) {
                goodsCate = goodsCates.get(p);
                classifyView = new HomeClassifyView(mContext);
                classifyView.updateData(goodsCate);
                if (p <= 4) {
                    mLineOne.addView(classifyView);
                } else {
                    mLineTwo.setVisibility(View.VISIBLE);
                    mLineTwo.addView(classifyView);
                }
            }
        }
    }

    //标题
    private class TitleViewHolder extends BaseAdapter.ViewHolder<ShoppingHomeTitleBean> {
        private TextView mTitleTv;
        private ImageView mIconIv;

        public TitleViewHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.title_tv);
            mIconIv = (ImageView) itemView.findViewById(R.id.icon_iv);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ShoppingHomeTitleBean shoppingHomeTitleBean) {
            if (shoppingHomeTitleBean.getType() == ShoppingHomeTitleBean.TYPE_HOT_GOODS) {
                mTitleTv.setText("热门商品");
                mIconIv.setImageDrawable(ResourceHelper.getDrawable(R.drawable.mall_icon_commodity));
            } else {
                mTitleTv.setText("优选专题");
                mIconIv.setImageDrawable(ResourceHelper.getDrawable(R.drawable.mall_icon_special));
            }
        }
    }

    //话题
    private class TopicViewHolder extends BaseAdapter.ViewHolder<ShoppingHomeTopics>
            implements View.OnClickListener {
        private SimpleDraweeView mThumbSdv;
        private TextView mTitleTv;

        private ShoppingHomeTopics mTopic;

        public TopicViewHolder(View itemView) {
            super(itemView);

            mThumbSdv = (SimpleDraweeView) itemView.findViewById(R.id.thumb_sdv);
            mTitleTv = (TextView) itemView.findViewById(R.id.title_tv);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mThumbSdv.getLayoutParams();
            params.height = Utils.getScreenWidth(mContext) * 4 / 7;
            mThumbSdv.setLayoutParams(params);

            mTitleTv.setOnClickListener(this);
            mThumbSdv.setOnClickListener(this);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ShoppingHomeTopics shoppingHomeTopics) {
            mTopic = shoppingHomeTopics;

            if (shoppingHomeTopics == null) {
                return;
            }

            ImageUtil.loadImage(mThumbSdv, shoppingHomeTopics.getCoverUrl());
            mTitleTv.setText(shoppingHomeTopics.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (mTopic != null) {
                ShoppingTopicActivity.startActivity(mContext, mTopic.getTitle(), mTopic.getTopicId());
            }
        }
    }

}
