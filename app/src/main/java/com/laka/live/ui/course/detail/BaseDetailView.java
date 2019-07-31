package com.laka.live.ui.course.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;

import java.util.List;

import framework.ioc.InjectUtil;

/**
 * Created by Lyf on 2017/9/12.
 */
public class BaseDetailView extends RelativeLayout {

    protected View mRootView;
    protected Context mContext;
    protected CourseDetailHelper mHelper;

    public BaseDetailView(Context context) {
        this(context, null);
    }

    public BaseDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * @param clickListener 确保其所有子类有onClick事件用来接收点击事件
     */
    protected void setContentView(OnClickListener clickListener, int layoutId) {
        this.mRootView = LayoutInflater.from(mContext).inflate(layoutId, this);
        InjectUtil.injectView(clickListener, mRootView, true);
    }

    // 设置数据
    protected void initData(CourseDetailHelper mHelper) {
        this.mHelper = mHelper;
    }

    protected void initData(List<ShoppingGoodsBaseBean> goodList, int courseId) {
        this.mHelper = null;
    }

    // 点击事件(点击事件从这里传递给Helper)
    protected void onClick(View view) {
        mHelper.onClick(view);
    }

}
