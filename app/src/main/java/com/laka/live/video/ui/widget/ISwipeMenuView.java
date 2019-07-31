package com.laka.live.video.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:删除列表契约类
 */

public interface ISwipeMenuView<T> {

    int DIRECTION_LEFT = 1;
    int DIRECTION_RIGHT = -1;

    @IntDef({
            DIRECTION_LEFT,
            DIRECTION_RIGHT
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface DIRECTION {
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attributeSet
     * @return
     */
    void initProperty(Context context, @Nullable AttributeSet attributeSet);

    /**
     * 初始化View
     *
     * @return
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 设置Adapter
     *
     * @param adapter
     * @return
     */
    ISwipeMenuView setAdapter(BaseAdapter adapter);

    /**
     * 设置EmptyView
     *
     * @param view
     * @return
     */
    ISwipeMenuView setEmptyView(View view);

    /**
     * 设置LoadingView
     *
     * @param view
     * @return
     */
    ISwipeMenuView setLoadingView(View view);

    /**
     * 设置ErrorView
     *
     * @param view
     * @return
     */
    ISwipeMenuView setErrorView(View view);

    /**
     * 设置Direction
     *
     * @param direction
     * @return
     */
    ISwipeMenuView setMenuDirection(@DIRECTION int direction);

    /**
     * 设置删除Menu宽度
     *
     * @param width
     * @return
     */
    ISwipeMenuView setMenuWidth(int width);

    /**
     * 设置Menu背景颜色
     *
     * @param color
     * @return
     */
    ISwipeMenuView setMenuColor(Color color);

    /**
     * 设置Menu文字
     *
     * @param text
     * @return
     */
    ISwipeMenuView setMenuText(String text);

    /**
     * 设置Menu文字颜色
     *
     * @param color
     * @return
     */
    ISwipeMenuView setMenuTextColor(Color color);

    /**
     * 设置Menu文字大小
     *
     * @param textSize
     * @return
     */
    ISwipeMenuView setMenuTextSize(int textSize);

    /**
     * 设置Item之间的Divider
     *
     * @param divider
     * @return
     */
    ISwipeMenuView setListDivider(Drawable divider);

    /**
     * 设置Item之间divider高度
     *
     * @param height
     * @return
     */
    ISwipeMenuView seDividerHeight(int height);

    /**
     * 设置点击Listener
     *
     * @param listener
     * @return
     */
    ISwipeMenuView setClickListener(OnItemClickListener<T> listener);

    /**
     * 点击Listener
     *
     * @param <T>
     */
    interface OnItemClickListener<T> {

        /**
         * Item点击事件
         *
         * @param position
         * @param item
         */
        void onClick(int position, T item);

        /**
         * Item删除事件
         *
         * @param position
         * @param item
         */
        void onDelete(int position, T item);
    }
}
