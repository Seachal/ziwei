package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.laka.live.R;
import com.laka.live.bean.ImageBean;
import com.laka.live.network.upload.UploadManager;
import com.laka.live.util.AnimationHelper;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/7/3.
 * 集加载状态和删除功能的ImageView
 */
public class UDImageView extends LinearLayout {

    private Context mContext;
    private ImageView mDelete;
    private ImageView imageView;
    private boolean animationEnable = false;
    private OnClickListener onDeleteListener;
    private WxCircleLoading mWxCircleLoading;

    public UDImageView(Context context) {
        super(context);
        init(context, null, null);
    }

    public UDImageView(Context context, View.OnClickListener onClickListener) {
        super(context);
        init(context, null, onClickListener);
    }

    public UDImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, null);
    }

    private void init(Context mContext, AttributeSet attrs, View.OnClickListener onClickListener) {

        this.mContext = mContext;
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_ud_image, this);
        mDelete = ViewUtils.findById(view, R.id.UDImageDelete);
        imageView = ViewUtils.findById(view, R.id.imageView);
        mWxCircleLoading = ViewUtils.findById(view, R.id.upLoading);

        // 删除图片，并中断上传
        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onDeleteListener != null) {
                    if (getTag() == null || !animationEnable) {
                        onDeleteListener.onClick(v);
                        return;
                    }
                    Animation animationHide = AnimationHelper.getAlphaAnimationHide(250);
                    animationHide.setFillAfter(true);
                    animationHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            onDeleteListener.onClick(v);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    UDImageView.this.startAnimation(animationHide);
                } else {
                    setDefault();
                    if (isUploading()) {
                        UploadManager.getInstance().cancelUpload();
                    }
                }
            }
        });

    }

    public void setProgress(float progress) {
        mWxCircleLoading.setProgress(progress);
    }

    // 恢复数据时调用的
    public void recovery(ImageBean bean) {
        setTag(bean);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String url = bean.getUrl();
        if (!url.startsWith("http")) {
            url = "http://img.zwlive.lakatv.com/" + bean.getUrl();
        }
        ImageUtil.displayImage(imageView, url, R.drawable.public_photo_add_selector);
        mDelete.setVisibility(VISIBLE);
        mWxCircleLoading.setVisible(GONE);
    }

    public void setLoadingStatus(boolean isLoading) {

        if (isLoading) {
            mDelete.setVisibility(VISIBLE);
            mWxCircleLoading.setVisible(VISIBLE);
        } else {
            mDelete.setVisibility(VISIBLE);
            mWxCircleLoading.setVisible(GONE);
        }

    }

    // 是否正在上传
    public boolean isUploading() {
        return mWxCircleLoading.getVisibility() == VISIBLE;
    }

    // 设置上传失败的样式
    public void setDefault() {
        setTag(null);
        mDelete.setVisibility(GONE);
        mWxCircleLoading.setVisible(GONE);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.public_photo_add_selector);
    }

    public void setImageBitmap(@Nullable Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bitmap);
        }
    }

    // 删除监听
    public void setOnDeleteListener(@Nullable OnClickListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    // 获取图片地址
    public String getImageUrl() {

        if (getTag() == null) {
            return "";
        }

        return (String) getTag();
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        super.setOnClickListener(l);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null)
                    l.onClick(UDImageView.this);
            }
        });
    }

    // 是否启用动画
    public void setAnimationEnable(boolean animationEnable) {
        this.animationEnable = animationEnable;
    }

}
