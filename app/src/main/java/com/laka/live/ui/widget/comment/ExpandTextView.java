package com.laka.live.ui.widget.comment;

/**
 * @ClassName: ExpandTextView
 * @Description: 可扩展的文本控件
 * @Author: chuan
 * @Version: 1.0
 * @Date: 11/18/16
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.util.Log;


public class ExpandTextView extends LinearLayout implements View.OnClickListener {
    private final static String TAG = ExpandTextView.class.getSimpleName();

    public final static int DEFAULT_MAX_LINES = 4;  //默认显示的文本行数
    public final static int DEFAULT_CONTENT_COLOR = 0xff000000; //默认显示的文本颜色
    public final static int DEFAULT_CONTENT_SIZE = 15;  //默认显示的文本大小

    private TextView mContentTv;
    private TextView mPlusTv;

    private int mShowLines;
    private int mContentColor;
    private int mContentSize;

    private ExpandStatusListener mExpandStatusListener;  //文本扩展状态监听
    private boolean isExpand = false;  //设置文本是否可扩展

    /**
     * 文本扩展状态监听器
     */
    public interface ExpandStatusListener {

        void statusChange(boolean isExpand);
    }

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView();
    }

    /**
     * 初始化UI
     */
    private void initView() {
        inflate(getContext(), R.layout.view_expand_text, this);

        mContentTv = (TextView) findViewById(R.id.content_tv);
        mContentTv.setTextColor(mContentColor);
        mContentTv.setTextSize(mContentSize);

        if (mShowLines > 0) {
            mContentTv.setMaxLines(mShowLines);
        }

        mPlusTv = (TextView) findViewById(R.id.plus_tv);
        mPlusTv.setOnClickListener(this);
    }

    /**
     * 初始化设置
     *
     * @param attrs 设置
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandTextView, 0, 0);

        try {
            mShowLines = typedArray.getInt(R.styleable.ExpandTextView_showLines, DEFAULT_MAX_LINES);
            mContentColor = typedArray.getColor(R.styleable.ExpandTextView_contentTextColor, DEFAULT_CONTENT_COLOR);
            mContentSize = typedArray.getDimensionPixelSize(R.styleable.ExpandTextView_contentTextSize, DEFAULT_CONTENT_SIZE);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 处理扩展按钮的点击事件
     */
    private void handleOnExpandClick() {
        if (isExpand) {
            mContentTv.setMaxLines(mShowLines);
            mPlusTv.setText(R.string.expand);
            setExpand(false);
        } else {
            mContentTv.setMaxLines(Integer.MAX_VALUE);
            mPlusTv.setText(R.string.un_expand);
            setExpand(true);
        }

        //通知外部状态已变更
        if (mExpandStatusListener != null) {
            mExpandStatusListener.statusChange(isExpand());
        }
    }

    /**
     * OnClickListener接口实现
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_tv:
                handleOnExpandClick();
                break;
            default:
                Log.d(TAG, "unhandle click . view : " + v);
                break;
        }
    }

    /**
     * 设置文本
     *
     * @param content      文本
     * @param isTransoFace 是否转换表情 ， true 转换 ， false 不转换
     */
    public void setText(final CharSequence content, boolean isTransoFace) {
        mContentTv.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        // 避免重复监听
                        mContentTv.getViewTreeObserver().removeOnPreDrawListener(this);

                        int linCount = mContentTv.getLineCount();
                        if (linCount > mShowLines) {

                            if (isExpand) {
                                mContentTv.setMaxLines(Integer.MAX_VALUE);
                                mPlusTv.setText(R.string.un_expand);
                            } else {
                                mContentTv.setMaxLines(mShowLines);
                                mPlusTv.setText(R.string.expand);
                            }

                            mPlusTv.setVisibility(View.VISIBLE);

                        } else {
                            mPlusTv.setVisibility(View.GONE);
                        }

                        return true;
                    }
                });

        if (isTransoFace) {
            MoonUtil.identifyFaceExpression(getContext(), mContentTv, content.toString(), ImageSpan.ALIGN_BOTTOM, MoonUtil.SMALL_SCALE);
        } else {
            mContentTv.setText(content);
        }

    }

    /**
     * 设置文字大小
     *
     * @param size 文字大小
     */
    public void setTextSize(int size) {
        mContentTv.setTextSize(size);
    }

    /**
     * 返回当前扩展状态
     *
     * @return true 已扩展 ； false 未扩展
     */
    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 设置是否已扩展
     *
     * @param isExpand true 已扩展 ； false 未扩展
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    /**
     * 设置扩展状态改变监听器
     *
     * @param listener 扩展状态改变监听器
     */
    public void setmExpandStatusListener(ExpandStatusListener listener) {
        mExpandStatusListener = listener;
    }

}
