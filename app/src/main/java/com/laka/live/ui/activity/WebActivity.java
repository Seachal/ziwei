package com.laka.live.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.laka.live.R;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

/**
 * Created by luwies on 16/4/16.
 * H5 页面，通过外部传入 url 即可
 */
public class WebActivity extends WebViewActivity {
    public static final String LOG = "WebActivity";
    public static final String URL_EXTRA = "url";
    public static final String TITLE_EXTRA = "title";
    private static final String TAG = "WebViewActivity";


    /**
     * Execute a getSession facebook method and on result exit the activity
     * returning a result containing session information.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.debug(LOG, "onCreate");

        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.createInstance(this);


        Intent intent = getIntent();

        Log.debug(LOG, "Recieved Intent:" + intent.toString());

        String path = intent.getStringExtra(URL_EXTRA);
        if (StringUtils.isEmpty(path)) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.error_url));
            return;
        }
        Uri url = Uri.parse(path);
        loadUri(url);
    }

    public static void startActivity(Context context, String url, CharSequence title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(URL_EXTRA, url);
        intent.putExtra(TITLE_EXTRA, title);
        context.startActivity(intent);
    }

    /**
     * @param url
     * @return
     */
    @Override
    public void onPageStarted(String url) {
    }

    /*
     * (non-Javadoc)
     * @see
     * com.googlecode.statusinator2.facebook.AuthorizationActivity#onPageFinished
     * (java.lang.String)
     */
    @Override
    public void onPageFinished(String url) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onStop() {
        super.onStop();
        CookieSyncManager.getInstance().stopSync();
    }

}
