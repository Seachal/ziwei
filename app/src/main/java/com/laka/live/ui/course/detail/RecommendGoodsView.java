package com.laka.live.ui.course.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.laka.live.R;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.adapter.AdvertGoodsAdapter;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/9/20.
 */
public class RecommendGoodsView extends BaseDetailView implements View.OnClickListener {

    @InjectView(id = R.id.recyclerView)
    private RecyclerView mRecyclerView;

    /**
     * description:数据配置
     **/
    private List<ShoppingGoodsBaseBean> goodList;
    private int courseId;
    private boolean isEnableScroll = false;

    public RecommendGoodsView(Context context) {
        this(context, null);
    }

    public RecommendGoodsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecommendGoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(this, R.layout.view_recommend_goods);
    }

    @Override
    protected void initData(CourseDetailHelper mHelper) {
        super.initData(mHelper);
        initView(mHelper.getCourse().getRecommendGoods(), Integer.parseInt(mHelper.getCourseId()));
    }

    @Override
    protected void initData(List<ShoppingGoodsBaseBean> goodList, int courseId) {
        super.initData(goodList, courseId);
        this.goodList = goodList;
        this.courseId = courseId;
        initView(goodList, courseId);
    }

    private void initView(List<ShoppingGoodsBaseBean> goodList, int courseId) {

        if (Utils.isEmpty(goodList)) {
            mRootView.findViewById(R.id.emptyView).setVisibility(VISIBLE);
            return;
        } else {
            mRootView.findViewById(R.id.emptyView).setVisibility(GONE);
        }

        List<ShoppingGoodsBaseBean> mData = new ArrayList<>();
        mData.addAll(goodList);
        AdvertGoodsAdapter advertGoodsAdapter = new AdvertGoodsAdapter(mHelper.getActivity(), courseId);
        advertGoodsAdapter.setData(mData);
        mRecyclerView.setLayoutManager(new ScrollGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(advertGoodsAdapter);
        advertGoodsAdapter.notifyDataSetChanged();
    }

    public void enableScroll(boolean isEnable) {
        isEnableScroll = isEnable;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (!isEnableScroll) {
            if (visibility == VISIBLE && mHelper != null) {
                // 解决ScrollView会自动滚到底部的BUG
                mHelper.getTitleBarView().setFocusableInTouchMode(true);
                mHelper.getTitleBarView().requestFocus();
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    // 禁止RecyclerView的滚动,不然会卡顿。
    private class ScrollGridLayoutManager extends StaggeredGridLayoutManager {

        private boolean isScrollEnabled = false;

        public ScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public ScrollGridLayoutManager(int spanCount, int orientation) {
            super(spanCount, orientation);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

}
