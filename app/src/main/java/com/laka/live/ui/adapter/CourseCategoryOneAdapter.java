package com.laka.live.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.CourseCategoryOneBean;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

public class CourseCategoryOneAdapter extends BaseAdapter<CourseCategoryOneBean, CourseCategoryOneAdapter.CategoriesViewHolder> {

    private Context mContext;

    public CourseCategoryOneAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_course_category_one, parent, false));
    }

    class CategoriesViewHolder extends BaseAdapter.ViewHolder<CourseCategoryOneBean> {

        private TextView nameTv;
        private ImageView imageView;

        CategoriesViewHolder(View itemView) {
            super(itemView);
            float screenWidth = Utils.getScreenWidth(mContext);
            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            imageView = (ImageView) itemView.findViewById(R.id.image_iv);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = (int) (screenWidth) / 4;
            itemView.setLayoutParams(layoutParams);
        }

        @Override
        public void update(BaseAdapter adapter, int position, CourseCategoryOneBean categoryOneBean) {
            nameTv.setText(categoryOneBean.getName());
            ImageUtil.displayCacheImage(imageView, categoryOneBean.getThumbUrl());
        }

    }

}
