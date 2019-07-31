package com.laka.live.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.List;

/**
 * @ClassName: CourseTagView
 * @Description: 课程标签控件
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/2/17
 */

public class CourseTagView extends LinearLayout {
    public CourseTagView(Context context) {
        this(context, null);
    }

    public CourseTagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void updateData(List<String> tags) {
        if (Utils.listIsNullOrEmpty(tags)) {
            return;
        }

        removeAllViews();

        for (String tag : tags) {
            addView(createItemView(tag));
        }

    }

    private View createItemView(String tag) {
        TextView item = new TextView(getContext());
        item.setText(tag);
        item.setTextColor(ResourceHelper.getColor(R.color.color848484));
        item.setBackground(ResourceHelper.getDrawable(R.drawable.index_icon_line));
        item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        item.setGravity(Gravity.CENTER);

        int width = Utils.dip2px(getContext(), 24);
        LinearLayout.LayoutParams params = new LayoutParams(width, width);
        params.rightMargin = Utils.dip2px(getContext(), 10);
        params.gravity = Gravity.CENTER;
        item.setLayoutParams(params);

        return item;
    }
}
