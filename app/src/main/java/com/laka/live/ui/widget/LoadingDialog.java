
package com.laka.live.ui.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.laka.live.R;
import com.laka.live.util.Log;

public class LoadingDialog extends Dialog {
    private static final String TAG = "LoadingDialog";

    private static final int CIRCLE_DIAMETER = 40;

    private CircleLoadingView mLoadingImageView;
    private MaterialProgressDrawable mProgress;

//    private Animation mLoadingAnimation;
//    private TextView loadingTextView;
//    private String msg;


    public LoadingDialog(Context context, int themeResId, String msg) {
        super(context, themeResId);
//        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
        Context context = getContext();
        int bgColor = ContextCompat.getColor(context, R.color.colorFFFAFAFA);
        mLoadingImageView = new CircleLoadingView(getContext(), bgColor, CIRCLE_DIAMETER / 2);

        mProgress = new MaterialProgressDrawable(context, mLoadingImageView);
        mProgress.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorF8C617));
        mProgress.setBackgroundColor(bgColor);
        mProgress.setAlpha(255);
//        mProgress.updateSizes(MaterialProgressDrawable.LARGE);
        mLoadingImageView.setImageDrawable(mProgress);
        mLoadingImageView.setVisibility(View.GONE);

//        loadingTextView = (TextView) loadingView.findViewById(R.id.loadingTextView);
//        if(Utils.isEmpty(msg)){
//            loadingTextView.setVisibility(View.GONE);
//        }else{
//            loadingTextView.setVisibility(View.VISIBLE);
//            loadingTextView.setText(msg);
//        }
        setContentView(mLoadingImageView);
    }

    @Override
    public void show() {
        super.show();
        mLoadingImageView.setVisibility(View.VISIBLE);
        mProgress.start();
//        mLoadingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_loading);
//        mLoadingImageView.startAnimation(mLoadingAnimation);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.d(TAG, "dismiss");
        mLoadingImageView.setVisibility(View.GONE);
        mProgress.stop();
//        mLoadingAnimation.cancel();
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