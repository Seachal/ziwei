package com.laka.live.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.shopping.viewholder.GoodsViewHolder;
import com.laka.live.util.RippleEffectHelper;

import java.util.List;


/**
 * @Author:Rayman
 * @Date:2018/8/25
 * @Description:这个推荐商品是原本存在的，现在修改扩展，多加上Video的数据。
 **/
public class AdvertGoodsAdapter extends BaseAdapter<ShoppingGoodsBaseBean, BaseAdapter.ViewHolder> {

    private final static int TYPE_COURSE = 2;

    /**
     * description:因为商品携带需要课程ID或者小视频ID，统一更改relativeId
     **/
    private boolean isCourse = true;
    private int relativeId;
    private Context mContext;

    public AdvertGoodsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 保留课程初始化逻辑不变
     *
     * @param mContext
     * @param relativeId
     */
    public AdvertGoodsAdapter(Context mContext, int relativeId) {
        this(mContext, relativeId, true);
    }

    /**
     * 新增扩展
     *
     * @param mContext
     * @param relativeId
     * @param isCourse
     */
    public AdvertGoodsAdapter(Context mContext, int relativeId, boolean isCourse) {
        this(mContext);
        this.relativeId = relativeId;
        this.isCourse = isCourse;
    }

    public void setRelativeId(int relativeId) {
        this.relativeId = relativeId;
    }

    public void setData(List<ShoppingGoodsBaseBean> data) {
        this.mDatas = data;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_COURSE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_COURSE:
                //一样滴，GoodsViewHolder也需要改变
                return new GoodsViewHolder(RippleEffectHelper.addRippleEffectInView(
                        LayoutInflater.from(mContext).inflate(R.layout.item_shopping_good, null)), relativeId, isCourse);
            default:
                throw new IllegalArgumentException();
        }
    }


}
