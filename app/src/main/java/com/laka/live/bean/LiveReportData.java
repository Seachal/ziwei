package com.laka.live.bean;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.util.ChannelUtil;
import com.laka.live.util.Common;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luwies on 16/5/30.
 */
public class LiveReportData {

    @Expose
    @SerializedName(Common.TM)
    private String tm;

    @Expose
    @SerializedName(Common.VER)
    private String ver;

    @Expose
    @SerializedName(Common.CHANNEL_INFO)
    private String channelInfo;

    @Expose
    @SerializedName(Common.SN)
    private String sn;

    @Expose
    @SerializedName(Common.UUID)
    private String uuid;

    @Expose
    @SerializedName(Common.DT)
    private String dt;

    @Expose
    @SerializedName(Common.DEVI)
    private String devi;

    @Expose
    @SerializedName(Common.FW)
    private String fw;

    @Expose
    @SerializedName(Common.RESOLUTION)
    private String resolution;

    @Expose
    @SerializedName(Common.PROV)
    private String prov;

    @Expose
    @SerializedName(Common.NT)
    private String nt;

    @Expose
    @SerializedName(Common.ISP)
    private String isp;


    public static LiveReportData getData() {
        LiveReportData data = new LiveReportData();

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmssms");
        data.tm = timeFormat.format(new Date());
        Context context = LiveApplication.getInstance();

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            data.ver = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        data.channelInfo = ChannelUtil.getChannel(context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String sn = "";
        try {
            sn = tm.getSimSerialNumber();
        } catch (Exception e) {

        }

        data.sn = sn;
        data.uuid = AnalyticsReport.getUUID(context);
        data.dt = Common.ANDROID;

        String model = android.os.Build.MODEL;
        data.devi = model;
        String release = android.os.Build.VERSION.RELEASE;
        data.fw = release;
        data.resolution = String.format("%d x %d", Util.getScreenWidth(context), Util.getScreenHeight(context));
        //TODO 地区
        String networkState = "unkown";
        int state = NetworkUtil.getNetworkState(context);
        switch (state) {
            case NetworkUtil.NETWORK_STATE_NONE:
                networkState = "0";
                break;
            case NetworkUtil.NETWORK_STATE_2G:
                networkState = "2G";
                break;
            case NetworkUtil.NETWORK_STATE_3G:
                networkState = "3G";
                break;
            case NetworkUtil.NETWORK_STATE_4G:
                networkState = "4G";
                break;
            case NetworkUtil.NETWORK_STATE_WIFI:
                networkState = "wifi";
                break;
            case NetworkUtil.NETWORK_STATE_UNKNOW:
                networkState = "unkown";
                break;
        }
        data.nt = networkState;
        String isp = "";
        try {
            isp = tm.getSimOperatorName();
        } catch (Exception e) {

        }
        data.isp = isp;

        return data;
    }
}

