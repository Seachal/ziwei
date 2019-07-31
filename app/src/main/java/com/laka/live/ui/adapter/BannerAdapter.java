package com.laka.live.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.util.ImageUtil;

import java.util.List;

/**
 * Created by luwies on 16/4/16.
 */
public class BannerAdapter extends PagerAdapter implements View.OnClickListener {

    /**
     * 用于存放广告图片
     */
    private List<SimpleDraweeView> advertList;
    private List<String> mUrls;

    private OnItemClickListener mListener;

    private Context mContext = null;

    public BannerAdapter(Context context) {
        this(context, null, null);
    }

    public BannerAdapter(Context context, List<SimpleDraweeView> advertList, List<String> urls) {
        mContext = context;
        this.advertList = advertList;
        this.mUrls = urls;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public int getCount() {
        return mUrls == null ? 0 : mUrls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final SimpleDraweeView imageView = advertList.get(position);
        if (position < mUrls.size()) {
            String url = mUrls.get(position);
            ImageUtil.loadImage(imageView, url);
//            ImageUtil.loadImage(imageView, "http://7xrkjc.com2.z0.glb.qiniucdn.com/a57035b63cceee7cjwro1oont0erey.jpg");
            imageView.setOnClickListener(this);
            imageView.setTag(position);
        }

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position < advertList.size()) {
            ImageView imageView = advertList.get(position);
            container.removeView(imageView);
        }
    }

    @Override
    public void onClick(View v) {
        Object position = v.getTag();
        onItemClick((int) position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private void onItemClick(int position) {
        if (mListener != null) {
            mListener.onItemClick(position);
        }
    }

    public void setAdvertList(List<SimpleDraweeView> list, List<String> urls) {
        advertList = list;
        mUrls = urls;
    }

}
