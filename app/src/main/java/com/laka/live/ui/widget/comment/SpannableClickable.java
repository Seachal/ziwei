package com.laka.live.ui.widget.comment;

/**
 * @ClassName: SpannableClickable
 * @Description: 可扩展可点击的控件
 * @Author: chuan
 * @Version: 1.0
 * @Date: 11/22/16
 */

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;


public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {
    private int mTextColor; //text颜色

    protected SpannableClickable(int textColor) {
        if (textColor <= 0) {
            mTextColor = R.color.colorAAAAAA;
        } else {
            mTextColor = textColor;
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        //ds.setColor(mTextColor);
        ds.setColor(ResourceHelper.getColor(R.color.colorAAAAAA));
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
