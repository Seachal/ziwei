package com.laka.live.ui.widget.toggle;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.util.ResourceHelper;

/**
 * Created by Lyf on 2017/9/1.
 * 目前未使用
 */
public class CourseToggle extends ToggleButton implements ViewTreeObserver.OnGlobalLayoutListener {

    private int viewHeight = ResourceHelper.getDimen(R.dimen.space_70);//mView对应的高度
    private View mView;
    private Course mCourse;
    private boolean autoScroll = true;
    private RecyclerView mRecyclerView;

    public CourseToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CourseToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    // 初始化这个控件
    public void initToggle(Course course, boolean autoScroll, RecyclerView recyclerView, View view) {
        setCourse(course);
        setAutoScroll(autoScroll);
        setRecyclerView(recyclerView);
        setView(view);
        // 只要有设置过限时优惠的，默认要打开
        if (course.isOpenLimitDiscount() && !isOpen()) {
            toggleOn();
        }
    }

    @Override
    public void onToggle(final boolean on) {
        super.onToggle(on);

        if (!isAutoScroll()) {
            setAutoScroll(true);
            setHeight(viewHeight);
            return;
        }

        // 设置View监听
        mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        // 动态计算并设置截止时间布局的高度,实现伸缩动画
        getCountDownTimer(on).start();

    }

    // 获取计时器
    private CountDownTimer getCountDownTimer(final boolean on) {
        return new CountDownTimer(200, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                onTickCount(millisUntilFinished, on);
            }

            @Override
            public void onFinish() {
                if (on) {
                    setHeight(viewHeight);
                    mCourse.setDiscountType(Course.OPENEDLIMITISCOUNT );
                } else {
                    setHeight(0);
                    mCourse.setDiscountType( Course.CLOSEDLIMITDISCOUNT);
                    mCourse.setDiscount_time(0); // 关闭时，清空disCount_time
                }
            }
        };
    }

    // 倒计时，动态计算设置view高度
    private void onTickCount(long millisUntilFinished, boolean on) {
        double rate = millisUntilFinished / 200.0;
        final int tempHeight = on ? (int) (viewHeight - viewHeight * rate) : (int) (viewHeight * rate);
        ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
        layoutParams.height = tempHeight;
        mView.setLayoutParams(layoutParams);
        mView.setTag(tempHeight);
    }

    // 设置View的高度
    private void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
        layoutParams.height = height;
        mView.setLayoutParams(layoutParams);
        mView.setTag(height);
    }

    // 绑定view
    public void setView(final View mView) {
        this.mView = mView;
    }

    /**
     * @param y 滚动到指定位置
     */
    private void smoothScrollBy(int y) {
        if (autoScroll) {
            getRecyclerView().smoothScrollBy(0, y);
        }
    }

    @Override
    public void onGlobalLayout() {
        if (mView != null && mView.getTag() != null) {
            if ((int) mView.getTag() == viewHeight && isOpen()) {
                smoothScrollBy(viewHeight - 1);
                mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    public View getView() {
        return mView;
    }

    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course mCourse) {
        this.mCourse = mCourse;
    }

    public boolean isAutoScroll() {
        return autoScroll;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }
}
