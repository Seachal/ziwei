package com.laka.live.ui.widget.toggle;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Lyf on 2017/9/1.
 * 自定义 toogleButton ，目前未使用
 */

public class ToggleButton extends com.zcw.togglebutton.ToggleButton implements com.zcw.togglebutton.ToggleButton.OnToggleChanged {

    private boolean isOpen = false;
    private OnToggleChanged onToggleChanged;

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnToggleChanged(this);
    }

    @Override
    public void setOnToggleChanged(OnToggleChanged onToggleChanged) {
        super.setOnToggleChanged(onToggleChanged);
        this.onToggleChanged = onToggleChanged;
    }

    @Override
    public void onToggle(boolean on) {
        setOpen(on);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

}
