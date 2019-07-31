package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;


/**
 * Created by luwies on 16/3/22.
 * zwl update 2016/7/29
 */
public class LevelText extends TextView {
    private int offsetX = 10;

    public LevelText(Context context) {
        this(context, null);
    }

    public LevelText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LevelText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        offsetX = ResourceHelper.getDimen(R.dimen.space_10);
    }

    public static int getLevelBgColor(int level) {
        int bgId;
        if (level <= 10) {
            bgId = R.drawable.rank_icon_leaf;
        } else if (level <= 20) {
            bgId = R.drawable.rank_icon_star;
        } else if (level <= 30) {
            bgId = R.drawable.rank_icon_moon;
        } else if (level <= 40) {
            bgId = R.drawable.rank_icon_sun;
        } else if (level <= 45) {
            bgId = R.drawable.rank_icon_diamonds;
        } else if (level <= 50) {
            bgId = R.drawable.rank_icon_king06;
        } else if (level <= 55) {
            bgId = R.drawable.rank_icon_king05;
        } else if (level <= 60) {
            bgId = R.drawable.rank_icon_king04;
        } else if (level <= 65) {
            bgId = R.drawable.rank_icon_king03;
        } else if (level <= 99) {
            bgId = R.drawable.rank_icon_king02;
        } else {
            bgId = R.drawable.rank_icon_king01;
        }
        return bgId;
    }

    private int getTextColor(int level) {
        int colorId;
        if (level <= 65) {
            colorId = R.color.white;
        } else if (level <= 99) {
            colorId = R.color.colorFFC05D;
        } else {
            colorId = R.color.colorFEE43C;
        }
        return colorId;
    }

    public void setLevel(int level) {
        setText(String.valueOf(level));
        int bgId = getLevelBgColor(level);
        setTextColor(ResourceHelper.getColor(getTextColor(level)));
        setBackgroundResource(bgId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate((getWidth() - offsetX) / 4, 0);
        super.onDraw(canvas);
    }
}
