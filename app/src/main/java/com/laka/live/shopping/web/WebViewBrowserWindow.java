package com.laka.live.shopping.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.laka.live.R;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.network.HeaderManager;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ThreadManager;


/**
 * Created by gangqing on 2015/12/11.
 * Email:denggangqing@ta2she.com
 */
public class WebViewBrowserWindow extends DefaultWindow {
    private String mTitle;
    private String mUrl;
    private WebView mWebView;
    private LoadingLayout mLoadingLayout;

    public WebViewBrowserWindow(Context context, IDefaultWindowCallBacks callBacks) {
        super(context, callBacks);
        onCreateContent();
    }

    private View onCreateContent() {
        View view = View.inflate(getContext(), R.layout.account_web_view_browser, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

        mWebView = (WebView) view.findViewById(R.id.account_web_view);
        mLoadingLayout = (LoadingLayout) view.findViewById(R.id.account_web_view_loading);
        mLoadingLayout.setBgContent(R.anim.loading, getResources().getString(R.string.homepage_loading), false);
        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
            @Override
            public void onClick() {
                mWebView.loadUrl(mUrl, HeaderManager.defaultHeader());
            }
        });
        return view;
    }

    @Override
    protected void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
            setupWebView(mUrl);
        }
    }

    public void setupWebViewWindow(WebViewBrowserParams params) {
        if (params == null) {
            return;
        }
        mTitle = params.title;
        mUrl = params.url;
        if (StringUtils.isNotEmpty(mTitle)) {
            setTitle(mTitle);
        }
    }

    private void setupWebView(String path) {// 设置主页面的WebView
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebViewJsInterface(new WebViewJsInterface.JsInterfaceCallback() {
            @Override
            public void onJsCommand(String command, String... result) {
                if (WebViewJsInterface.COMMAND_COLLECT.equals(command)) {
                    // TODO: 2017/7/14  CollectHelper
//                    CollectHelper.handleJsCollectAction(mWebView, result);
                }
            }
        }, null), "android");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String strUrl) {
                if (strUrl.lastIndexOf("tel:") > -1) {
                    callService();
                    return true;
                } else if (strUrl.lastIndexOf("mailto:") > -1) {
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse(strUrl));
                    try {
                        getContext().startActivity(Intent.createChooser(data, ResourceHelper.getString(R.string.account_setting_helper_mail)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                view.loadUrl(strUrl, HeaderManager.defaultHeader());
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String strUrl, Bitmap favicon) {
                super.onPageStarted(view, strUrl, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String strUrl) {
                super.onPageFinished(view, strUrl);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mLoadingLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setBgContent(R.drawable.no_network, getResources().getString(R.string
                        .homepage_network_error_retry), true);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 90) {
                    ThreadManager.removeRunnable(mGoneLoadingLayoutRunnable);
                    ThreadManager.postDelayed(ThreadManager.THREAD_UI, mGoneLoadingLayoutRunnable, 500);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (StringUtils.isNotEmpty(title) && StringUtils.isEmpty(mTitle)) {
                    mTitle = title;
                    setTitle(title);
                }
            }
        });
        mWebView.loadUrl(path, HeaderManager.defaultHeader());// 打开浏览器
    }

    private Runnable mGoneLoadingLayoutRunnable = new Runnable() {
        @Override
        public void run() {
            mLoadingLayout.setVisibility(View.GONE);
        }
    };

    private void callService() {
        SimpleTextDialog dialog = new SimpleTextDialog(getContext());
        dialog.addTitle(R.string.account_alert_dialog_title);
        dialog.setText(R.string.account_alert_dialog_message);
        dialog.addYesNoButton(ResourceHelper.getString(R.string.account_alert_dialog_negative),
                ResourceHelper.getString(R.string.account_alert_dialog_positive));
        dialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
        dialog.setOnClickListener(new IDialogOnClickListener() {
            @Override
            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                if (viewId == GenericDialog.ID_BUTTON_YES) {
                    Uri uri = Uri.parse(ResourceHelper.getString(R.string.account_alert_dialog_uri));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(uri);
                    try {
                        getContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        dialog.show();
    }
}
