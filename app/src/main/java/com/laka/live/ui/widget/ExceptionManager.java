package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;

/**
 * Created by luwies on 16/9/19.
 */
public abstract class ExceptionManager {

    private Context mContext;

    private View mContentView;

    private View mException;

    private View mErrorView;

    private View mLoadingView;

    private TextView mErrorTextView;

    private CharSequence mEmptyTipText;

    private CharSequence mNetworkTipText;

    private Drawable mEmptyDrawable;

    private Drawable mNetworkDrawable;

    private CharSequence mDataExceptionTipText;

    private Drawable mDataExceptionDrawable;

    private boolean isReloadWhenEmpty = true;

    private PageListLayout.ErrorState mErrorState;

    public ExceptionManager(View contentView, View exception) {
        this.mContentView = contentView;
        this.mException = exception;
        mContext = mContentView.getContext();
        init();
    }

    private void init() {
        mEmptyTipText = mContext.getText(R.string.empty_tips);

        mNetworkTipText = mContext.getText(R.string.network_error_tips);

        mEmptyDrawable = ContextCompat.getDrawable(mContext, R.drawable.public_pic_empty);

        mNetworkDrawable = ContextCompat.getDrawable(mContext, R.drawable.public_pic_nowifi);

        mDataExceptionTipText = mContext.getText(R.string.empty_tips);
        mDataExceptionDrawable = ContextCompat.getDrawable(mContext, R.drawable.public_pic_empty);

        mErrorView = mException.findViewById(R.id.error_layout);
        mErrorTextView = (TextView) mErrorView.findViewById(R.id.tip);

        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mErrorState == PageListLayout.ErrorState.ERROR_STATE_EMPTY && isReloadWhenEmpty == false) {
                    return;
                }
                loadData();
            }
        });

        mLoadingView = mException.findViewById(R.id.loading);

    }

    public void setIsReloadWhenEmpty(boolean isReloadWhenEmpty) {
        this.isReloadWhenEmpty = isReloadWhenEmpty;
    }

    public abstract void loadData();

    public void showEmpty() {
        mErrorState = PageListLayout.ErrorState.ERROR_STATE_EMPTY;
        showError();
        if (mErrorView != null) {
            mErrorTextView.setText(mEmptyTipText);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorTextView, null, mEmptyDrawable,
                    null, null);
        }

    }

    public void showDataExcaption() {
        mErrorState = PageListLayout.ErrorState.ERROR_STATE_DATA_EXCEPTION;
        showError();
        if (mErrorView != null) {
            mErrorTextView.setText(mDataExceptionTipText);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorTextView, null, mDataExceptionDrawable,
                    null, null);
        }
    }

    public void showData() {
        mErrorState = null;
        mContentView.setVisibility(View.VISIBLE);

        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    private void showError() {
        mContentView.setVisibility(View.GONE);
        mException.setVisibility(View.VISIBLE);

        if (mErrorView != null) {
            mErrorView.setVisibility(View.VISIBLE);
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    public void showNetWorkError() {
        mErrorState = PageListLayout.ErrorState.ERROR_STATE_NETWORK_ERROR;
        showError();
        if (mErrorView != null) {
            mErrorTextView.setText(mNetworkTipText);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorTextView, null, mNetworkDrawable,
                    null, null);
        }
    }

    public void showLoading() {
        mErrorState = null;
        mContentView.setVisibility(View.GONE);
        mException.setVisibility(View.VISIBLE);

        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }
}
