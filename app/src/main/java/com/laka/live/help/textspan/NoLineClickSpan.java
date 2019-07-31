package com.laka.live.help.textspan;


import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.laka.live.R;
import com.laka.live.util.Log;

public class NoLineClickSpan extends  ClickableSpan{
	private static final String TAG = "NoLineClickSpan";
	Context context;
    OnSpanClickListener listener;
    public NoLineClickSpan(Context context, OnSpanClickListener listener) {
        super();
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
//        ds.setColor(context.getResources().getColor(R.color.color007AFF));
//        ds.setColor(Color.parseColor("#FFC773"));
        ds.setUnderlineText(false); //去掉下划线
    }

    @Override
    public void onClick(View widget) { 
    	Log.d(TAG, "onClick");
    	listener.OnClick(widget);    //点击超链接时调用
    }

}
