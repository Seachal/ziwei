package com.laka.live.util;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.*;

import com.laka.live.BuildConfig;
import com.laka.live.application.LiveApplication;
import com.laka.live.ui.widget.Toast;
import com.taobao.hotfix.HotFixManager;
import com.taobao.hotfix.NewPatchListener;
import com.taobao.hotfix.aidl.DownloadService;

/**
 * Created by chuan on 9/13/16.
 */
public class HotfixUtil {

    private final static String TAG = "HotfixUtil" ;

    private final static String APPID = "71704-1" ;

    public static void initHotFixManager(final Application app){

        HotFixManager.getInstance().initialize(app, BuildConfig.VERSION_NAME_HOTFIX, APPID,null);
        Log.d(TAG , "init Hotfix manager : version-->"+ BuildConfig.VERSION_NAME_HOTFIX);
    }

    public static void queryNewHotPatch(){
        HotFixManager.getInstance().queryNewHotPatch();
        Log.d(TAG , "query new hot patch");
    }
}
