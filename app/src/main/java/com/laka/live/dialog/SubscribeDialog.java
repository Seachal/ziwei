package com.laka.live.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.widget.Effects.EffectsType;
import com.laka.live.util.Log;

import framework.ioc.annotation.InjectView;


/**
 * Created by Lyf on 2017/3/28.
 */
public class SubscribeDialog extends BaseDialog {

    @InjectView(id = R.id.singleCourse)
    public TextView singleCourse;
    @InjectView(id = R.id.entireCourse)
    public TextView entireCourse;
    @InjectView(id = R.id.close)
    public ImageView close;
    @InjectView(id = R.id.prompt)
    public TextView prompt;
    @InjectView(id = R.id.secPrompt)
    public TextView secPrompt;

    private int courseCount;
    private View.OnClickListener onClickListener;

    public SubscribeDialog(Context context, View.OnClickListener onClickListener) {
        super(context);
        setContentView(R.layout.dialog_subscribe, CENTER);
        setAnimator(EffectsType.SlideBottom, EffectsType.FadeOut);
        this.onClickListener = onClickListener;
    }

    public void setContent(int courseCount, float discount, double saveCoins) {

        this.prompt.setText("总共" + courseCount + "节课");

        // 小于10折，才有这些
        if (discount < 10) {
            this.secPrompt.setText("购买整套" + discount + "折优惠");
            entireCourse.setText("购买整套课堂(省" + saveCoins +" ");
            insertDrawable(entireCourse,R.drawable.price_icon_diamonds_small2);
            entireCourse.append(" )");
        } else {
            this.secPrompt.setVisibility(View.GONE);
            entireCourse.setText("购买整套课堂");
        }

        // 如果课程数大于1节，就显示购买单节课程的按钮
        singleCourse.setVisibility(courseCount > 1 ? View.VISIBLE : View.GONE);

    }

    // 在TextView的文字中间插入图片
    private void insertDrawable(TextView textView, int id) {

        final SpannableString spannableString = new SpannableString("image");
        //得到drawable对象，即所要插入的图片
        Drawable drawable = mContext.getResources().getDrawable(id);
        // 35是设置这个图片的宽度, 33是设置这个图片的高度
        drawable.setBounds(0, 0, 35, 33);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"image".length()即：5。[0,5)。值得注意的是当我们复制这个图片的时候，实际是复制了"image"这个字符串。
        spannableString.setSpan(span, 0, "image".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);
    }

    public void onClick(View view) {
        onClickListener.onClick(view);
        dismiss();
    }

}
