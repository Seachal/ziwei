package com.laka.live.ui.widget.chatKeyboard;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.laka.live.util.Log;

/**
 * Created by crazyguan on 2016/5/5.
 */
public class InputRelativeLayout extends RelativeLayout {
    private static final String TAG ="RoomIMMListenerRelativeLayout";
    private InputWindowListener listener;

    public InputRelativeLayout(Context context) {
        super(context);
    }

    public InputRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (listener != null) {
            boolean isShow = (oldh - h) > 200;
            if (isShow) {
                Log.d(TAG,"input window show oldh="+oldh+" h="+h);
                listener.show();
            } else{
                Log.d(TAG,"input window hiddin oldh="+oldh+" h="+h);
                listener.hidden();
            }
        }
    }

    public void setListener(InputWindowListener listener) {
        this.listener = listener;
    }
}
