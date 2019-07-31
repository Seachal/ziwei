package com.laka.live.shopping.goods.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.shopping.bean.ShoppingEvaExprsBean;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;


/**
 * Created by linhz on 2016/5/11.
 * Email: linhaizhong@ta2she.com
 */
public class GoodsEvaluateExprLayout extends LinearLayout {

    private ExprCircleLayout mCircleLayout;
    private TextView mExprTextView;

    public GoodsEvaluateExprLayout(Context context) {
        this(context, null);
    }

    public GoodsEvaluateExprLayout(Context context, AttributeSet set) {
        super(context, set);
        setOrientation(LinearLayout.VERTICAL);
        setWillNotDraw(false);
        initLayout(context);
    }

    private void initLayout(Context context) {
        mCircleLayout = new ExprCircleLayout(context);
        int size = ResourceHelper.getDimen(R.dimen.space_72);
        LayoutParams lp = new LayoutParams(size, size);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        this.addView(mCircleLayout, lp);

        mExprTextView = new TextView(context);
        mExprTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_14));
        mExprTextView.setTextColor(ResourceHelper.getColor(R.color.black));
        mExprTextView.setMaxWidth(R.dimen.space_72);
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = ResourceHelper.getDimen(R.dimen.space_15);
        this.addView(mExprTextView, lp);
    }

    public void setupExprLayout(ShoppingEvaExprsBean exprsBean) {
        mCircleLayout.setupCircleLayout(exprsBean);
        mExprTextView.setText(exprsBean.getText());
    }

    private class ExprCircleLayout extends FrameLayout {
        private TextView mAvgScoreView;
        private TextView mAvgTextView;
        private float mRatio = 0.5f;
        private Paint mPaint = new Paint();
        private int mDimColor;
        private int mLightColor;
        private int mStokeWidth;
        private RectF mDrawRect = new RectF();

        public ExprCircleLayout(Context context) {
            this(context, null);
        }

        public ExprCircleLayout(Context context, AttributeSet set) {
            super(context, set);
            setWillNotDraw(false);
            mPaint.setAntiAlias(true);
            mDimColor = ResourceHelper.getColor(R.color.shopping_goods_expression_ring_bg_color);
            mLightColor = ResourceHelper.getColor(R.color.pink);
            mStokeWidth = ResourceHelper.getDimen(R.dimen.space_4);
            initLayout(context);
        }

        private void initLayout(Context context) {
            LinearLayout parentLayout = new LinearLayout(context);
            parentLayout.setOrientation(LinearLayout.VERTICAL);
            LayoutParams parentLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            parentLp.gravity = Gravity.CENTER;
            this.addView(parentLayout, parentLp);

            mAvgScoreView = new TextView(context);
            mAvgScoreView.setTextColor(ResourceHelper.getColor(R.color.black));
            mAvgScoreView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_14));
            mAvgScoreView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams childLp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            childLp.gravity = Gravity.CENTER_HORIZONTAL;
            parentLayout.addView(mAvgScoreView, childLp);

            View divider = new View(context);
            divider.setBackgroundColor(ResourceHelper.getColor(R.color.divider_color));
            childLp = new LinearLayout.LayoutParams(ResourceHelper.getDimen(R.dimen.space_28),
                    ResourceHelper.getDimen(R.dimen.divider_height));
            childLp.gravity = Gravity.CENTER_HORIZONTAL;
            childLp.topMargin = childLp.bottomMargin = ResourceHelper.getDimen(R.dimen.space_4);
            parentLayout.addView(divider, childLp);

            mAvgTextView = new TextView(context);
            mAvgTextView.setTextColor(ResourceHelper.getColor(R.color.black));
            mAvgTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));
            mAvgTextView.setGravity(Gravity.CENTER);
            childLp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            childLp.gravity = Gravity.CENTER_HORIZONTAL;
            parentLayout.addView(mAvgTextView, childLp);
        }

        public void setupCircleLayout(ShoppingEvaExprsBean exprsBean) {
            mAvgScoreView.setText(exprsBean.getItemValue());
            mAvgTextView.setText(exprsBean.getAvgText());
            float maxValue = StringUtils.parseFloat(exprsBean.getItemCount(), 5);
            float curValue = StringUtils.parseFloat(exprsBean.getItemValue(), 0);
            mRatio = curValue / maxValue;

            this.invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawCircle(canvas);
        }

        private void drawCircle(Canvas canvas) {
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = centerX - mStokeWidth;
            mPaint.setColor(mDimColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStokeWidth);
            canvas.drawCircle(centerX, centerY, radius, mPaint);

            mPaint.setColor(mLightColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mDrawRect.set(centerX - radius, centerY - radius, centerX
                    + radius, centerY + radius);
            canvas.drawArc(mDrawRect, 270, 360 * mRatio, false, mPaint);
        }
    }
}
