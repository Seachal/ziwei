package com.laka.live.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class TimeLimitTextView extends AppCompatTextView {
    private final static String ICON = "icon ";

    public TimeLimitTextView(Context context) {
        this(context, null);
    }

    public TimeLimitTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLimitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setIncludeFontPadding(false);
    }

    public void setTimeLimitText(Course course) {

        int drawableId = -1;
        int limitType = course.getTime_limit_type();
        String text = course.getTitle();

//        if (limitType == Common.TIME_LIMIT_TYPE_CUT || limitType == Common.TIME_LIMIT_TYPE_FREE
//                || Utils.isNotEmpty(course.getAgentProfitratio())) {

        if (limitType == Common.TIME_LIMIT_TYPE_FREE) {
            if (Utils.isNotEmpty(course.getAgentProfitratio())) {
                drawableId = R.drawable.public_label_both_free;
            } else {
                drawableId = R.drawable.public_label_free;
            }
        } else if (limitType == Common.TIME_LIMIT_TYPE_CUT) {
            if (Utils.isNotEmpty(course.getAgentProfitratio())) {
                drawableId = R.drawable.public_label_both_sale;
            } else {
                drawableId = R.drawable.public_label_sale;
            }
        } else if (Utils.isNotEmpty(course.getAgentProfitratio())) {
            drawableId = R.drawable.public_label_prize;
        }

        if (drawableId != -1) {
            SpannableStringBuilder spannableString = new SpannableStringBuilder(ICON);
//            VerticalImageSpan imageSpan = new VerticalImageSpan(getContext(), drawableId);
            ImageSpan imageSpan = new ImageSpan(getContext(), drawableId);
            spannableString.setSpan(imageSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.append(text);
            setText(spannableString);
        } else {
            setText(text);
        }
    }

    public float getMeasureTextWidth() {
        return getPaint().measureText(getText().toString());
    }
}
