
package com.laka.live.ui.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

public class LakaLoading extends LinearLayout {
    private static final String TAG ="LakaLoading" ;
    private ImageView mLoadingImageView;
    private Context mContext;
    private Animation mLoadingAnimation;
    private TextView loadingTextView;
    private String msg;
//    private AnimationDrawable mLoadingAnimationDrawable;
    private MaterialProgressDrawable mProgress;
    public LakaLoading(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public LakaLoading(Context context, AttributeSet attrs) {
        super(context,attrs);
        mContext = context;
        initView();
    }

    public LakaLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        mContext = context;
        initView();
    }

    protected void initView() {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.laka_loading, null);
        mLoadingImageView = (ImageView) loadingView.findViewById(R.id.loadingImageView);
        mProgress = new MaterialProgressDrawable(getContext(), mLoadingImageView);
        mProgress.setColorSchemeColors(ResourceHelper.getColor(R.color.white));
        mProgress.setAlpha(255);
        mLoadingImageView.setImageDrawable(mProgress);

        loadingTextView = (TextView) loadingView.findViewById(R.id.loadingTextView);
//        if(Utils.isEmpty(msg)){
//            loadingTextView.setVisibility(View.GONE);
//        }else{
//            loadingTextView.setVisibility(View.VISIBLE);
//            loadingTextView.setText(msg);
//        }

        addView(loadingView);
    }

    public void show(int textRes) {
        setVisibility(View.VISIBLE);
        if(textRes>0){
            loadingTextView.setText(ResourceHelper.getString(textRes));
        }
//        mLoadingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_loading);
//        mLoadingImageView.startAnimation(mLoadingAnimation);
        new Handler(){}.postDelayed(new Runnable() {
        @Override
        public void run() {
            if(mProgress!=null)
            mProgress.start();
        }
    }, 10);
    }

    public void hide() {
        setVisibility(View.GONE);
        if(mProgress!=null)
        mProgress.stop();
    }

//    public void setText(String text){
//         if(Utils.isEmpty(text)){
//             loadingTextView.setVisibility(View.GONE);
//         }else{
//             loadingTextView.setVisibility(View.VISIBLE);
//             loadingTextView.setText(text);
//         }
//    }
}


//private ImageView  mLoadingImageView;
//private AnimationDrawable mLoadingAnimationDrawable;
//public LoadingDialogSecond(Context context, boolean cancelable,OnCancelListener cancelListener) {
//    super(context, cancelable, cancelListener);
//}
//
//public LoadingDialogSecond(Context context, int theme) {
//    super(context, theme);
//}
//
//public LoadingDialogSecond(Context context) {
//    super(context);
//}
//
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    View loadingView=LayoutInflater.from(getContext()).inflate(R.layout.loading, null);
//    mLoadingImageView=(ImageView) loadingView.findViewById(R.id.loadingImageView);
//    mLoadingImageView.setImageResource(R.anim.loadinganimsecond);
//    setContentView(loadingView);
//}
//
//@Override
//public void show() {
//    super.show();
//    //注意将动画的启动放置在Handler中.否则只可看到第一张图片
//    new Handler(){}.postDelayed(new Runnable() {
//        @Override
//        public void run() {
//            mLoadingAnimationDrawable =(AnimationDrawable) mLoadingImageView.getDrawable();
//            mLoadingAnimationDrawable.start();
//        }
//    }, 10);
//}
//@Override
//public void dismiss() {
//    super.dismiss();
//    //结束帧动画
//    mLoadingAnimationDrawable =(AnimationDrawable) mLoadingImageView.getDrawable();
//    mLoadingAnimationDrawable.stop();
//}