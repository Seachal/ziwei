
package com.laka.live.shopping.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.widget.LoadLoadingView;
import com.laka.live.ui.widget.SelectorButton;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

/**
 * 没有数据和网络的背景布局
 */
public class LoadingLayout extends LinearLayout implements OnClickListener {
    private TextView mSignView;
    private ImageView mIconView;
    private LoadLoadingView mPBLoading;
    private SelectorButton mLoadAgainButton;
    private OnBtnClickListener mListener;

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.loading_layout, this);
        mIconView = (ImageView) view.findViewById(R.id.bg_icon);
        mSignView = (TextView) view.findViewById(R.id.bg_sign);
        mLoadAgainButton = (SelectorButton) view.findViewById(R.id.btn_load_again);
        mPBLoading = (LoadLoadingView) view.findViewById(R.id.pb_loading);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void setBgContent(int resourceId, String tips, boolean isShowBtn) {
        if (resourceId == 0) {
            mIconView.setImageDrawable(null);
        } else {
            boolean isLoad = R.anim.loading == resourceId;
            if (isLoad) {
                mIconView.setImageDrawable(null);
            } else {
                mIconView.setImageResource(resourceId);
            }
            mSignView.setVisibility(isLoad ? INVISIBLE : VISIBLE);
            mIconView.setVisibility(isLoad ? INVISIBLE : VISIBLE);
            mPBLoading.setVisibility(isLoad ? VISIBLE : INVISIBLE);
        }

        mSignView.setText(tips);
        mLoadAgainButton.setVisibility(isShowBtn ? VISIBLE : GONE);
        mLoadAgainButton.setOnClickListener(this);
    }

    public void setButtonText(String text) {
        mLoadAgainButton.setText(text);
    }

    public void setDefaultLoading() {
        setVisibility(VISIBLE);
        setBgContent(R.anim.loading, ResourceHelper.getString(R.string.loading), false);
    }

    public void setDefaultNetworkError(boolean isShowBtn) {
        setVisibility(VISIBLE);
        int resId = isShowBtn ? R.string.homepage_network_error_retry : R.string.no_network;
        setBgContent(R.drawable.no_network, ResourceHelper.getString(resId), isShowBtn);
        setText("刷新");
    }

    public void setDefaultNoData() {
        setVisibility(VISIBLE);
        setBgContent(R.drawable.no_data_bg, ResourceHelper.getString(R.string.data_no_data), false);
    }

    public void setDefaultDataError(boolean isShowBtn) {
        setVisibility(VISIBLE);
        setBgContent(R.drawable.no_network, ResourceHelper.getString(R.string.data_error), isShowBtn);
        mLoadAgainButton.setVisibility(GONE);
    }

    public void setBtnOnClickListener(OnBtnClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load_again:
                if (mListener != null) {
                    mListener.onClick();
                }
                break;
            default:
                break;
        }
    }

    public interface OnBtnClickListener {
        void onClick();
    }

    // 好记
    public void setText(String msg) {
        setBtnText(msg);
    }

    public void setBtnText(String msg) {
        mLoadAgainButton.setNewText(msg);
    }


}
