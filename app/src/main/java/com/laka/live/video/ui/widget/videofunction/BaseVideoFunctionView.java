package com.laka.live.video.ui.widget.videofunction;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.laka.live.help.EventBusManager;
import com.laka.live.video.model.bean.MiniVideoBean;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author:Rayman
 * @Date:2018/8/20
 * @Description:小视频FunctionView
 */

public abstract class BaseVideoFunctionView extends RelativeLayout implements EventBusManager.OnEventBusListener {

    protected View mRootView;
    protected Context mContext;
    protected Unbinder unbinder;
    protected MiniVideoBean mVideo;
    protected VideoFunctionHelper mHelper;

    public BaseVideoFunctionView(Context context) {
        this(context, null);
    }

    public BaseVideoFunctionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseVideoFunctionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setContentView();
        EventBusManager.register(this);
        initProperties();
        initView();
        initData();
    }

    protected void setContentView() {
        this.mRootView = LayoutInflater.from(mContext).inflate(setLayoutId(), this);
        unbinder = ButterKnife.bind(mRootView);
    }

    protected abstract int setLayoutId();

    protected abstract void initProperties();

    protected abstract void initView();

    protected abstract void initData();

    public void setVideoUIHelper(VideoFunctionHelper mHelper) {
        this.mHelper = mHelper;
    }

    protected void onRelease() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mHelper != null) {
            mHelper.onRelease();
            mHelper = null;
        }
        EventBusManager.unregister(this);
    }
}
