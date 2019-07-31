package com.laka.live.ui.text.style;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by luwies on 16/6/28.
 */
public abstract class CustomClickableSpan extends ClickableSpan {

    private int mLinkColor;

    private boolean isUnderlineText;

    public void setLinkColor(int linkColor) {
        this.mLinkColor = linkColor;
    }

    public void setUnderlineText(boolean underlineText) {
        this.isUnderlineText = underlineText;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mLinkColor);
        ds.setUnderlineText(isUnderlineText);
    }
}
