package com.laka.live.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luwies on 16/4/16.
 */
public abstract class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "WebViewActivity";

    private String proxyUserIdPattern = "proxyUserId=[\\d]+";
    private Pattern pattern;
    private Matcher patternMatcher;
    private String sharedUrl;

    private class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            WebViewActivity.this.setProgress(newProgress * 100);
            mProgressBar.setProgress(newProgress);
        }

        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            take();
            return true;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            take();
        }
    }


    private class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.debug(LOG, "onPageStarted: " + url);
            WebViewActivity.this.onPageStarted(url);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.debug(LOG, "onPageStarted: " + url);
//            if ((url.indexOf("http://www.facebook.com/login.php") > -1 ))
//            {
//                if (WebViewActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//                {
//                    mWebView.scrollTo(200, 270);
//                    ((WebView)findViewById(R.id.web_view)).zoomIn();
//                }
//                else
//                {
//                    mWebView.scrollTo(270, 350);
//                }
//
//                ((WebView)findViewById(R.id.web_view)).zoomIn();
//
//            }
            WebViewActivity.this.onPageFinished(url);
            if (AccountInfoManager.getInstance().isLogin()) {
                patternMatcher = pattern.matcher(url);
                StringBuffer buffer = new StringBuffer();
                while (patternMatcher.find()) {
                    //替换
                    patternMatcher.appendReplacement(buffer,
                            "proxyUserId=" + AccountInfoManager.getInstance().getAccountInfo().getIdStr());
                }
                patternMatcher.appendTail(buffer);
                sharedUrl = buffer.toString();
            }
//            Logger.e("输出当前URL：" + url + "\n输出pattern：" + pattern.pattern()
//                    + "\n匹配后的地址：" + patternMatcher.toString()
//                    + "\nBuffer：" + buffer.toString());
            mProgressBar.setVisibility(View.GONE);
        }


    }

    // ----------------------------------------------------------------------------

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 1;// 表单的结果回调</span>
    private static final String LOG = "WebViewActivity";

    private static final String USER_AGENT = "MX_WebView";

    private Uri mLastUrl = null;
    private WebView mWebView;
    private Uri imageUri;

    private ProgressBar mProgressBar;

    private String mTitle = null;

//    private JavaScriptInterface mJsCallManager;

    public void loadUri(Uri url) {
        Log.d(LOG, "loadUrl: " + url);
        if (url == null || StringUtils.isEmpty(url.toString())) {
            return;
        }
        mLastUrl = url;
        mWebView.loadUrl(url.toString());
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        Log.error(LOG, "onCreate");

        CookieSyncManager.createInstance(this);

        setContentView(R.layout.webview_layout);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(getNativeController(), "nativeController");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setUserAgentString(USER_AGENT);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        Intent intent = getIntent();
        CharSequence title = intent.getStringExtra(WebActivity.TITLE_EXTRA);
        sharedUrl = intent.getStringExtra(WebActivity.URL_EXTRA);
        if (!TextUtils.isEmpty(title)) {
            HeadView headView = (HeadView) findViewById(R.id.head);
            headView.setTitle(title);
            mTitle = title.toString();
            headView.setRightIconShow(true);
            headView.setRightIcon(R.drawable.nav_icon_share);
            headView.setRightIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AccountInfoManager.getInstance().isLogin()) {
                        LoginActivity.startActivity(WebViewActivity.this);
                        return;
                    }

                    /**
                     * description:业务逻辑变动，这里需要拼接上参数
                     **/

                    Logger.e("输出分享链接：" + sharedUrl);
                    if (Utils.isHttpUrl(sharedUrl)) {
                        String wxUrlPath = "";
                        try {
                            wxUrlPath = URLEncoder.encode(sharedUrl, "UTF-8");
                            sharedUrl = Common.NEWS_WEB_VIEW_SHARE_URL_PREFIX +
                                    "proxyUserId=" + AccountInfoManager.getInstance().getAccountInfo().getIdStr() +
                                    "&zxsrc=" + wxUrlPath;
                            showShareDialog(sharedUrl, mTitle, "", "", false);
                            Logger.e("输出修改后分享链接：" + sharedUrl);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            /*final JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(this);
            mWebView.addJavascriptInterface(myJavaScriptInterface, "mx_AndroidFunction");*/
        } else {
            fixWebView();
        }

        pattern = Pattern.compile(proxyUserIdPattern);
    }

    @TargetApi(11)
    private void fixWebView() {
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");

//        mJsCallManager = new JavaScriptInterface(this);
    }

    abstract public void onPageStarted(String url);

    abstract public void onPageFinished(String url);

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_icon:
                finish();
                break;
        }
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        WebViewActivity.this.startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) {
                return;
            }
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                if (result == null) {
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            }
        }
    }


    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
    }


    private Object getNativeController() {
        Object insertObj = new Object() {
            @JavascriptInterface
            public boolean closePage() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, " closePage");
                        showToast("提交成功");
                        finish();
                    }
                });
                return true;
            }
        };

        return insertObj;
    }
}
