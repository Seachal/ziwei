package com.laka.live.ui.widget.continueButton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.Log;

import java.text.DecimalFormat;


public class ContinueButton extends Button {
//	 CircularProgressDrawable drawable;
Drawable drawable;
	 Context context;
	AbsoluteSizeSpan span;
	private String TAG = "ContinueButton";

	public ContinueButton(Context context) {
		super(context);
		this.context = context;
		initUI();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ContinueButton(Context context, AttributeSet attrs,
						  int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.context = context;
		initUI();
	}

	public ContinueButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		initUI();
	}

	public ContinueButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initUI();
	}
    Animator currentAnimation;
	private void initUI() {
//		drawable = new CircularProgressDrawable.Builder()
//        .setInnerCircleScale(0.85f)
//        .setRingWidth(getResources().getDimensionPixelSize(R.dimen.drawable_ring_size))
//        .setOutlineColor(getResources().getColor(android.R.color.darker_gray))
//        .setRingColor(Color.parseColor("#FFCC00"))//getResources().getColor(android.R.color.holo_green_light)
//        .setCenterColor(Color.parseColor("#FFCC00"))//getResources().getColor(android.R.color.holo_blue_dark)
//        .create();
//		drawable.centerColorPressed = getResources().getColor(R.color.colorBB9B21);
//		setBackgroundDrawable(drawable);
		setBackgroundResource(R.drawable.live_special_btn_bursts_selector);
		drawable = getBackground();
//		setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				  startTimer();
//			}
//		});
		span = new AbsoluteSizeSpan(25);
		 SpannableString msp = new SpannableString("4s\n连送");
		 msp.setSpan(span, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 setText(msp);

//		setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					drawable.isPressed = true;
//					setTextColor(getResources().getColor(R.color.black));
//					invalidate();
//				}
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					drawable.isPressed = false;
//					setTextColor(getResources().getColor(R.color.color333333));
//					invalidate();
//				}
//				return false;
//			}
//		});
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void startTimer() {
		    setClickable(true);
	    	if (currentAnimation != null) {
                currentAnimation.cancel();
            }
           currentAnimation = prepareStyle2Animation();
           currentAnimation.start();
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void stopTimer() {
		if (currentAnimation != null) {
			currentAnimation.cancel();
		}
		SpannableString msp = new SpannableString("4s\n连送");
		msp.setSpan(span, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		setText(msp);
	}

	   double lastSecond = 4.0;
	   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	   private Animator prepareStyle2Animation() {
	        AnimatorSet animation = new AnimatorSet();

	        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.PROGRESS_PROPERTY,
	                0f, 1f);
	        progressAnimation.setDuration(4100);
	        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
	        progressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator value) {
//					System.out.println("duration="+value.getDuration()+" time="+);
					double time = (double) value.getCurrentPlayTime()/1000;
					double curSecond = 4d-time;
					DecimalFormat df =new DecimalFormat("#####0.0");
					if(curSecond>=0&&curSecond!=lastSecond){
						 SpannableString msp = new SpannableString(df.format(curSecond)+"s\n连送");
						 msp.setSpan(span, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						 setText(msp);
						 lastSecond = curSecond;
						if(lastSecond<0.1){
							Log.d(TAG,"倒计时结束");
							setClickable(false);
							EventBusManager.postEvent(0, SubcriberTag.MULTI_SEND_GIFT_FINISH);
						}else{
//							Log.d(TAG,"倒计时lastSecond="+lastSecond);
						}
					}
				}
			});
//		   progressAnimation.addListener(new AnimatorListenerAdapter() {
//			   @Override
//			   public void onAnimationEnd(Animator animation) {
//				   super.onAnimationEnd(animation);
//				   setClickable(false);
//				   EventBusManager.postEvent(0, SubcriberTag.MULTI_SEND_GIFT_FINISH);
//			   }
//		   });
//	        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(drawable, CircularProgressDrawable.RING_COLOR_PROPERTY,
//	                getResources().getColor(android.R.color.holo_red_dark),
//	                getResources().getColor(android.R.color.holo_green_light));
//	        colorAnimator.setEvaluator(new ArgbEvaluator());
//	        colorAnimator.setDuration(3600);
	       
//	        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(drawable, CircularProgressDrawable.RING_COLOR_PROPERTY,
//	        		 Color.parseColor("#FFCC00"),
//	        		 Color.parseColor("#FFCC00"));
//	      colorAnimator.setEvaluator(new ArgbEvaluator());
//	      colorAnimator.setDuration(8000);
	       
	        animation.play(progressAnimation);
//	        animation.playTogether(progressAnimation, colorAnimator);
	        return animation;
	    }
}
