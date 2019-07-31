package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.util.Utils;
import com.lhh.ptrrv.library.footer.loadmore.BaseLoadMoreView;

/**
 * Created by luwies on 16/5/17.
 */
public class LoadMoreView extends BaseLoadMoreView {

    /*private final static long EVERY_FRAME_DURATION = 40L;

    private Paint mPaint;

    private int mProgress = 0;

    private float mHalfW;

    private float mHalfH;

    private long lastFrameTime = 0;*/

    private Context mContext;

    private View mLoadView;

    public LoadMoreView(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);

        mContext = context;

        /*mHalfW = Utils.dip2px(context, 17f);

        mHalfH = Utils.dip2px(context, 5f);

        mPaint = new Paint();*/

        setLoadMorePadding(Utils.dip2px(mContext, 44f));
    }

    public void setLoadMoreView(View view) {
        mLoadView = view;
    }

    @Override
    public void onDrawLoadMore(Canvas c, RecyclerView parent) {


        final int childSize = parent.getChildCount();
        if (childSize > 0) {
            final View child = parent.getChildAt(childSize - 1);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;

            if (mLoadView != null) {
                mLoadView.setTranslationY(top);
            }
        }


    }
}
