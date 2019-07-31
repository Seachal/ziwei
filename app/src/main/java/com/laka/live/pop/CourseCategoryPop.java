package com.laka.live.pop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.CourseCategoryOneBean;
import com.laka.live.bean.CourseCategoryTwoBean;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.CourseCategoryOneAdapter;
import com.laka.live.ui.course.CourseClassifyActivity;
import com.laka.live.ui.widget.FlowLayout;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/9/18.
 */
public class CourseCategoryPop extends BasePop {

    @InjectView(id = R.id.tipTv)
    public TextView tipTv;
    @InjectView(id = R.id.flowLayout)
    public FlowLayout mCategoryLayout;

    private OnVisibleChangedListener onVisibleChangedListener;

    private CourseCategoryPop(final Activity mContext) {
        super(mContext);
        setContentView(R.layout.pop_content_classify);
    }

    /**
     * @return 获取一级分类的Pop
     */
    public static CourseCategoryPop getCourseClassifyOnePop(Activity mContext, final List<CourseCategoryOneBean> mData) {
        return new CourseCategoryPop(mContext).getCourseClassifyOnePop(mData);
    }

    /**
     * @return 获取二级分类的Pop
     */
    public static CourseCategoryPop getCourseClassifyTwoPop(Activity mContext, final List<CourseCategoryTwoBean.Category> mData, int selectedPosition) {
        return new CourseCategoryPop(mContext).getCourseClassifyTwoPop(mData, selectedPosition);
    }

    /**
     * @return 生成课程一级分类的Pop
     */
    private CourseCategoryPop getCourseClassifyOnePop(final List<CourseCategoryOneBean> mData) {
        tipTv.setPadding(ResourceHelper.getDimen(R.dimen.space_30),0,0,0);
        mCategoryLayout.setPadding(0,0,0,ResourceHelper.getDimen(R.dimen.space_15));
        mCategoryLayout.removeAllViews();
        for (final CourseCategoryOneBean category : mData) {
            createCategoryOneItem(category);
        }
        return this;
    }

    /**
     * @return 生成课程二分类的Pop
     */
    private CourseCategoryPop getCourseClassifyTwoPop(final List<CourseCategoryTwoBean.Category> mData, int selectedPosition) {
        tipTv.setPadding(ResourceHelper.getDimen(R.dimen.space_15),0,0,0);
        mCategoryLayout.setPadding(ResourceHelper.getDimen(R.dimen.space_15),ResourceHelper.getDimen(R.dimen.space_10),
                ResourceHelper.getDimen(R.dimen.space_15),ResourceHelper.getDimen(R.dimen.space_15));
        mCategoryLayout.removeAllViews();
        for (final CourseCategoryTwoBean.Category category : mData) {
            createCategoryTwoItem(category, mCategoryLayout.getChildCount() == selectedPosition);
        }
        return this;
    }

    // 创建一级CategoryItem
    private void createCategoryOneItem(final CourseCategoryOneBean category) {

        float screenWidth = Utils.getScreenWidth(mContext);

        RelativeLayout categoryItem = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.item_course_category__pop_one, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (screenWidth / 4), ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView imageView = ViewUtils.findById(categoryItem, R.id.image_iv);
        TextView nameTv = ViewUtils.findById(categoryItem, R.id.name_tv);
        nameTv.setText(category.getName());
        ImageUtil.displayCacheImage(imageView, category.getThumbUrl());

        categoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> mEventParams = new HashMap<>();
                mEventParams.put("ID", String.valueOf(category.getId()));
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10228, mEventParams);
                CourseClassifyActivity.startActivity(mContext, category.getName(), category.getId());
                dismiss();
            }
        });

        mCategoryLayout.addView(categoryItem, layoutParams);
    }

    // 创建二级CategoryItem
    private void createCategoryTwoItem(final CourseCategoryTwoBean.Category category, boolean selected) {

        TextView categoryItem = new TextView(mContext);
        int padding = ResourceHelper.getDimen(R.dimen.space_10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = padding;
        layoutParams.rightMargin = padding;
        layoutParams.height = ResourceHelper.getDimen(R.dimen.space_32);
        categoryItem.setSelected(selected);
        categoryItem.setGravity(Gravity.CENTER);
        categoryItem.setPadding(padding, 0, padding, 0);
        categoryItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        categoryItem.setBackgroundResource(R.drawable.bg_yellow_border_selector);
        categoryItem.setText(category.getName());
        categoryItem.setTextColor(ResourceHelper.getColor(selected ? R.color.colorFF950B : R.color.color848484));
        categoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> mEventParams = new HashMap<>();
                mEventParams.put("ID", String.valueOf(category.getId()));
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10266, mEventParams);
                EventBusManager.postEvent(category.getId(), SubcriberTag.CHANGED_CATEGORY);
                dismiss();
            }
        });
        mCategoryLayout.addView(categoryItem, layoutParams);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        if (onVisibleChangedListener != null) {
            onVisibleChangedListener.onVisibleChanged(true);
        }
    }

    public interface OnVisibleChangedListener {
        void onVisibleChanged(boolean visible);
    }

    // 设置显示与隐藏时的监听
    public void setOnVisibleChangedListener(final OnVisibleChangedListener onVisibleChangedListener) {
        this.onVisibleChangedListener = onVisibleChangedListener;
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                onVisibleChangedListener.onVisibleChanged(false);
            }
        });
    }

}
