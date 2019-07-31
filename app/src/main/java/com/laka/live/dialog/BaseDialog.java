package com.laka.live.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;
import com.laka.live.R;
import com.laka.live.ui.widget.Effects.EffectsType;

import framework.ioc.InjectUtil;
import framework.ioc.annotation.InjectView;
import framework.thread.Task;
import framework.thread.ThreadWorker;

/**
 * @Author:summer
 * @Date:2018/11/30
 * @Description: 这里重写了dismiss() 方法，延时了250ms 才会调用父类的dismiss() 关闭窗口，
 * 在一些提示框中，一点击确认就会finish activity，但是dialog却延时250ms才dismiss
 * 因此会报一个窗体泄漏的错误log（目前发现的有 AddVideoActivity、PostCourseActivity）
 */
public class BaseDialog extends Dialog implements DialogInterface.OnShowListener, View.OnTouchListener {

    //上下文
    public Context mContext;
    //存放子类布局的容器
    @InjectView(id = R.id.content)
    public RelativeLayout content;
    // 子类对话框的布局
    protected View subView;
    @InjectView(id = R.id.dialog_base)
    public RelativeLayout backGround;
    // 动画类
    protected BaseEffects animator;
    //进入动画
    protected EffectsType contentInAnimator = EffectsType.SlideBottom;
    //退出动画
    protected EffectsType contentOutAnimator = EffectsType.SlideOutBottom;
    //动画的时长
    protected final static int DismissDuration = 250;
    //触摸Dialog外围是否退出
    protected boolean isCancelOnTouchOutSide = true;
    // 进度圈
    protected ProgressDialog progressDialog;
    //子类布局的位置
    protected final static int TOP = 0, BOTTOM = 1, LEFT = 2, RIGHT = 3, CENTER = 4;
    // 关闭dialog 时是否进行延时并执行动画
    protected boolean isShowCancelAnim = true;
    // 记录是否是点击了窗体右上角关闭按钮
    protected boolean isButtonCancel;

    public BaseDialog(Context context) {
        super(context, R.style.BaseDialog);
        super.setContentView(R.layout.dialog_base);
        this.mContext = context;
        InjectUtil.inject(this);

        //监听对话框显示(在这需要加一个动画效果)
        setOnShowListener(this);
        // 用户点击对话框外围的事件
        backGround.setOnTouchListener(this);

    }

    // 默认放在底部
    @Override
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, BOTTOM);
    }

    /**
     * @param rule 可选值有TOP、BOTTOM、LEFT、RIGHT
     *             (这四个值在BaseDialog里定义为常量)
     *             或是 RelativeLayout的常量值
     */
    public void setContentView(int layoutResID, int rule) {

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(content.getLayoutParams());

        //设置对话框的位置
        switch (rule) {
            case TOP:
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case BOTTOM:
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            case CENTER:
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                break;
            default:
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
        }

        content.setLayoutParams(params);

        subView = LayoutInflater
                .from(getContext()).inflate(layoutResID, null);

        content.addView(subView);

        InjectUtil.injectView(this, subView);

    }


    //当点击非对话框的部分背景时，是否退出，true就是退出。
    public void setCanceledOnTouchOutside(boolean cancel) {
        isCancelOnTouchOutSide = cancel;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        // 显示对话框进入动画
        if (contentInAnimator != null)
            startAnimator(contentInAnimator, DismissDuration);
    }

    @Override
    public void dismiss() {
        // 是否显示退出动画
        if (!isShowCancelAnim && !isButtonCancel) {
            super.dismiss();
            return;
        }
        isButtonCancel = false;
        // 对话框退出动画
        if (contentOutAnimator != null)
            startAnimator(contentOutAnimator, DismissDuration);

        // 等动画结束后再退出
        ThreadWorker.execute(new Task(mContext) {
            @Override
            public void doInUI(Object obj, Integer what) {
                BaseDialog.super.dismiss();
            }
        }, DismissDuration);
    }

    //执行动画代码
    protected void startAnimator(EffectsType type, int duration) {

        // 避免多次执行相同的动画
        if (animator != null && animator.getAnimatorSet().isRunning()) {
            return;
        } else {
            animator = type.getAnimator();
            animator.setDuration(Math.abs(duration));
            animator.start(content);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            // 用户点击对话框外围没松手的事件
            // Utils.logD("ACTION_DOWN");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            // 用户点击对话框外围并松手的事件
            if (isCancelOnTouchOutSide && view.getId() == R.id.dialog_base)
                dismiss();
        } else {
            // 其它事件不处理
            // Utils.logD("MotionEvent:"+motionEvent.getAction());
        }

        return false;
    }

    //设置对话框进入和退出的动画效果
    public BaseDialog setAnimator(EffectsType contentInAnimator, EffectsType contentOutAnimator) {
        this.contentInAnimator = contentInAnimator;
        this.contentOutAnimator = contentOutAnimator;
        return this;
    }

    public void setShowCancelAnim(boolean showCancelAnim) {
        isShowCancelAnim = showCancelAnim;
    }
}
