package com.laka.live.analytics;

import android.net.Uri;
import android.text.TextUtils;

import com.laka.live.BuildConfig;
import com.laka.live.application.LiveApplication;
import com.laka.live.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by luwies on 16/8/30.
 */
public class DNSReport implements Runnable {

    private static final String TAG = "DNSReport";

    private final static String EVENT_ID = new StringBuilder(AnalyticsReport.DEFAULT_VIEW_ID)
            .append(AnalyticsReport.SEPARATOR)
            .append("15450")
            .append(AnalyticsReport.SEPARATOR)
            .append(AnalyticsReport.NETWORK_ENVIRONMENT_MONITORING)
            .toString();

    private String mHost;

    public DNSReport(String hostName) {
        this.mHost = hostName;
    }

    public void report() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            Log.error(TAG, "start resolved host : " + mHost);
            InetAddress inetAddress = InetAddress.getByName(mHost);
            inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            Log.error("DNSReport", "UnknownHostException : ", e);
        } catch (Exception e) {

        }
        long end = System.currentTimeMillis();

        long usedTime = end - start;

        Log.error("DNSReport", "usedTime : " + usedTime);

        if (usedTime <= 500L) {
            return;
        }

        AnalyticsReport.onEventValue(LiveApplication.getInstance(), EVENT_ID, null, (int) usedTime);
    }

    public static void reportHttpDNSResolvedTime(String uriStr) {

        if (BuildConfig.DEBUG) {
            return;
        }

        if (TextUtils.isEmpty(uriStr)) {
            return;
        }
        Uri uri = Uri.parse(uriStr);
        new DNSReport(uri.getHost()).report();
    }

}
