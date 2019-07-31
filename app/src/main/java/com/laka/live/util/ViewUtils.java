package com.laka.live.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.*;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.ui.widget.PageListLayout;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

/**
 * Created by Lyf on 2017/3/29.
 */

public class ViewUtils {


    /**
     * 便捷findView
     *
     * @param root
     * @param resId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findById(View root, int resId) {
        if (root != null) {
            return (T) root.findViewById(resId);
        }
        return null;
    }

    /**
     * 设置部分字体的删除线
     */
    public static void setPartTextDeleteLine(TextView textView, int start, int end) {

        SpannableString msp = new SpannableString(textView.getText().toString());
        //设置删除线
        msp.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(msp);

    }

    /**
     * 获取动态添加的TextView的宽度
     */
    public static int gettMeasuredWidth(TextView textView) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(spec, spec);
        return textView.getMeasuredWidth();
    }

    // 设置字体颜色
    public static void setTextColor(TextView textView, String text) {
        textView.setText(Html.fromHtml(text));
    }


    /**
     * 设置部分字体的颜色
     */
    public static void setPartTextColor(TextView textView, int color, int start, int end) {

        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(ResourceHelper.getColor(color));
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    /**
     * 设置部分字体的颜色
     */
    public static void setPartHintColor(TextView textView, int color, int start, int end) {

        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getHint().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(ResourceHelper.getColor(color));
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setHint(builder);
    }

    // 设置字体颜色
    public static void setHintColor(TextView textView, String text) {
        textView.setHint(Html.fromHtml(text));
    }

    public static int getLiveStatus(Course course) {

        if (course.isLive()) {

            switch (course.getStatus()) {

                case Course.CREATINGPLAYBACK:// 生成回放中
                    return R.color.colorD6D8DB;
                case Course.CREATEDPLAYBACK: // 已生成回放
                case Course.PLAYVIDEO: // 课程视频已可播放
                    return R.color.color3A92FE;
                case Course.LIVING:// 直播中
                    return R.color.color3BC36B;

                case Course.CHANGETIME: // 调整直播时间
                    return R.color.colorF76720;

                case Course.NOTSTART: // 未开播
                    return R.color.colorF76720;

                case Course.ALREADY: // 准备中
                    return R.color.colorF76720;
                default:
                    return R.color.color3BC36B;
            }
        } else {
            return R.color.color3BC36B;
        }

    }

    // 禁用控件
    public static void disable(TextView view) {
        view.setTextColor(ResourceHelper.getColor(R.color.color777777));
        view.setEnabled(false);
    }


    /**
     * 获取字体样式
     *
     * @param ttf 放在assets目录下fonts目录下的ttf文件的名字比如: “123.ttf”
     */
    public static Typeface getTypeface(Context context, String ttf) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/" + ttf);
    }

    /**
     * 强制EditText获取焦点并显示软键盘
     */
    public static void forcedShowInputMethod(EditText editText) {
        editText.clearFocus();
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static void hideInputMethod(View view, Activity mActivity) {

        InputMethodManager imm = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        IBinder token = view.getWindowToken();
        boolean success = imm.hideSoftInputFromWindow(token, 0);
        if (!success) {
            Object serverObject = ReflectionHelper.getFieldValue(imm, "mServedView");
            if (serverObject instanceof View) {
                token = ((View) serverObject).getWindowToken();
            }
            success = imm.hideSoftInputFromWindow(token, 0);
        }
    }

    public static void handleDoubleClick(final PageListLayout listView) {

        RecyclerView recyclerView = listView.getRecyclerView();
        int offset = recyclerView.computeVerticalScrollOffset();

        if (offset > 0) {
            listView.smoothScrollToPosition(0);
            listView.addOnScrollListener(new PullToRefreshRecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        listView.removeOnScrollListener();

                        int offset = recyclerView.computeVerticalScrollOffset();
                        if (offset <= 0) {
                            listView.autoRefresh(false);
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                }

                @Override
                public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        } else {
            listView.autoRefresh();
        }
    }

    /**
     * 监听输入法的显示与隐藏状态的改变
     * @param contentView Activity的根View
     * @param listener 监听
     */
    public static void setKeyBoardStateListener(final View contentView, final keyboardListener listener) {

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    listener.onKeyBoardChange(true);
                } else {
                    listener.onKeyBoardChange(false);
                }

            }
        });
    }

    public interface keyboardListener {
        void onKeyBoardChange(boolean isVisible);
    }

    /**
     * 获取隐藏View的宽高
     */
    public static int[] getGoneViewSize(View view) {
        int size[] = new int[2];
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.EXACTLY);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        size[0] = view.getMeasuredWidth();
        size[1] = view.getMeasuredHeight();
        return size;
    }

    /**
     * 获取隐藏View的宽度
     */
    public static int getGoneViewWidth(View view){
        return getGoneViewSize(view)[0];
    }

    /**
     * 获取隐藏View的高度
     */
    public static int getGoneViewHeight(View view){
        return getGoneViewSize(view)[1];
    }

    public static void setLayoutParams(ViewGroup view, int height) {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }
}
