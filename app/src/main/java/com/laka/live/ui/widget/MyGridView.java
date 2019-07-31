package com.laka.live.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView extends GridView {
	private boolean haveScrollbar = false;
    public MyGridView(Context context) {  
        super(context);  
    }  
  
    public MyGridView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public MyGridView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    /**  
     * 设置是否有scrolllbar
     *   
     */
    public void setHaveScrollbar(boolean haveScrollbar) {  
        this.haveScrollbar = haveScrollbar;  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        if (haveScrollbar == false) {  
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
            super.onMeasure(widthMeasureSpec, expandSpec);  
        } else {  
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        }  
    }  
    
    
    //??????dispatchTouchEvent?��??��?�??�????
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//    if(ev.getAction() == MotionEvent.ACTION_MOVE&&ev.getRawY()!=0){
//    	LogUtil.Log("MyGridView", "???件�?????�件");
//    	return false;
//          }
    return super.dispatchTouchEvent(ev);
    }
    
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		 if(ev.getAction() == MotionEvent.ACTION_MOVE&&ev.getRawY()!=0){
//		    	LogUtil.Log("MyGridView onTouchEvent", "???件�?????�件");
//		    	return false;
//		          }
		return super.onTouchEvent(ev);
	}
    
    
}
