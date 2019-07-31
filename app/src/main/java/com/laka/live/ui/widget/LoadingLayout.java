
package com.laka.live.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;

/**
 * @author 张武林
 *         没有数据和网络的背景布局
 */
public class LoadingLayout extends LinearLayout implements OnClickListener {
    private Context mContext;
    private TextView mSignView;
    private ImageView mIconView;
    private LoadLoadingView mPBLoading;

    private TextView mLoadAgainButton;
    private OnBtnClickListener mListener;

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == VISIBLE) {
            mPBLoading.start();
        } else {
            mPBLoading.stop();
        }
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.loading_layout, this);
        RelativeLayout bgLayout = (RelativeLayout) view.findViewById(R.id.rl_bg);
        bgLayout.setOnClickListener(this);

        mIconView = (ImageView) view.findViewById(R.id.bg_icon);
        mSignView = (TextView) view.findViewById(R.id.bg_sign);
        mLoadAgainButton = (TextView) view.findViewById(R.id.btn_load_again);
        mPBLoading = (LoadLoadingView) view.findViewById(R.id.pb_loading);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void setBgContent(int resourceId, String tips, boolean isShowBtn) {
        if (resourceId == 0) {
            mIconView.setImageDrawable(null);
        } else {
            boolean isLoad = R.id.pb_loading == resourceId;
            if (R.id.pb_loading == resourceId) {
                mIconView.setImageDrawable(null);
                mPBLoading.start();
            } else {
                mPBLoading.stop();
                mIconView.setImageResource(resourceId);
            }
            mIconView.setVisibility(isLoad ? INVISIBLE : VISIBLE);
            mSignView.setVisibility(isLoad ? INVISIBLE : VISIBLE);
            mPBLoading.setVisibility(isLoad ? VISIBLE : INVISIBLE);
        }
        mSignView.setText(tips);
        mLoadAgainButton.setVisibility(GONE);
    }

    public void setButtonText(String text) {
        mLoadAgainButton.setText(text);
    }

    public void setDefaultLoading() {
        setVisibility(VISIBLE);
        mPBLoading.start();

        mIconView.setVisibility(INVISIBLE);
        mLoadAgainButton.setVisibility(GONE);
        mSignView.setVisibility(GONE);
        mPBLoading.setVisibility(VISIBLE);
    }

    public void setDefaultNetworkError(boolean isShowBtn) {
        setVisibility(VISIBLE);
        int resId = R.string.network_error_tips;
        setBgContent(R.drawable.public_pic_nowifi, mContext.getString(resId), isShowBtn);
    }

    public void setDefaultNoData() {
        setVisibility(VISIBLE);
        setBgContent(R.drawable.public_pic_empty, mContext.getString(R.string.empty_tips), false);
    }

    public void setDefaultDataError(boolean isShowBtn) {
        setVisibility(VISIBLE);
        setBgContent(R.drawable.public_pic_empty, mContext.getString(R.string.empty_tips), isShowBtn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bg:
                if (mListener != null) {
                    setDefaultLoading();
                    mListener.onClick();
                }
                break;
            default:
                break;
        }
    }

    public void setBtnOnClickListener(OnBtnClickListener listener) {
        this.mListener = listener;
    }

    public interface OnBtnClickListener {
        void onClick();
    }

    public void setBtnText(String msg) {
        mLoadAgainButton.setText(msg);
    }
}
