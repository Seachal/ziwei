package com.laka.live.ui.widget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by luwies on 16/3/4.
 */
public class Toast extends android.widget.Toast{
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public Toast(Context context) {
        super(context);
    }

    public static android.widget.Toast makeText(Context context, CharSequence text, int duration) {
/*        android.widget.Toast toast = new android.widget.Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(text);
        toast.setView(view);
        toast.setDuration(android.widget.Toast.LENGTH_SHORT);*/

        //return toast;

        android.widget.Toast toast = android.widget.Toast.makeText(context, text, duration);

        return toast;
    }

    public static android.widget.Toast makeText(Context context, int resId, int duration) {
        String text = context.getString(resId);
        return makeText(context, text, duration);
    }
}
