package com.laka.live.help;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.laka.live.application.LiveApplication;
import com.laka.live.util.Log;
import com.tencent.wxop.stat.e;

/**
 * Created by ios on 16/9/2.
 */
public class ActivityLifecycleCb implements Application.ActivityLifecycleCallbacks  {
        private static final String TAG = "MicroMsg.SDK.WXApiImplV10.ActivityLifecycleCb";
        private static final int DELAYED = 800;
        private boolean isForeground;
        private Handler handler;
//        private Context context;
        private Runnable onPausedRunnable;
        private Runnable onResumedRunnable;

        public static ActivityLifecycleCb self;
        public static ActivityLifecycleCb getInstance() {
            if(self==null) {
                self = new ActivityLifecycleCb();
            }

            return  self;
        }

        private ActivityLifecycleCb() {
            this.isForeground = false;
            this.handler = new Handler(Looper.getMainLooper());
            this.onPausedRunnable = new Runnable() {
                public void run() {
                    if(self != null && self.isForeground) {
//                        Log.d("MicroMsg.SDK.WXApiImplV10.ActivityLifecycleCb", "WXStat trigger onBackground");
//                        e.d(ActivityLifecycleCb.this.context, "onBackground_WX");
                        Log.d(TAG," app退到后台");
                        ActivityLifecycleCb.this.isForeground = false;
                        LiveApplication.getInstance().applicationDidEnterBackground();
                        EventBusManager.postEvent(0,SubcriberTag.APP_ENTER_BACKGROUND);
                    }

                }
            };
            this.onResumedRunnable = new Runnable() {
                public void run() {
                    if(self != null && !ActivityLifecycleCb.this.isForeground) {
//                        Log.d("MicroMsg.SDK.WXApiImplV10.ActivityLifecycleCb", "WXStat trigger onForeground");
//                        e.d(ActivityLifecycleCb.this.context, "onForeground_WX");
                        Log.d(TAG," app回到前台");
                        ActivityLifecycleCb.this.isForeground = true;
                        LiveApplication.getInstance().applicationDidEnterForeground();
                        EventBusManager.postEvent(0,SubcriberTag.APP_ENTER_FOREGROUND);
                    }

                }
            };
//            this.context = var1;
        }

        public final void onActivityCreated(Activity var1, Bundle var2) {
        }

        public final void onActivityDestroyed(Activity var1) {
        }

        public final void onActivityPaused(Activity var1) {
            Log.v("MicroMsg.SDK.WXApiImplV10.ActivityLifecycleCb", var1.getComponentName().getClassName() + "  onActivityPaused");
            this.handler.removeCallbacks(this.onResumedRunnable);
            this.handler.postDelayed(this.onPausedRunnable, 800L);
        }

        public final void onActivityResumed(Activity var1) {
            Log.v("MicroMsg.SDK.WXApiImplV10.ActivityLifecycleCb", var1.getComponentName().getClassName() + "  onActivityResumed");
            this.handler.removeCallbacks(this.onPausedRunnable);
            this.handler.postDelayed(this.onResumedRunnable, 800L);
        }

        public final void onActivitySaveInstanceState(Activity var1, Bundle var2) {
        }

        public final void onActivityStarted(Activity var1) {
        }

        public final void onActivityStopped(Activity var1) {
        }

        public final void detach() {
            this.handler.removeCallbacks(this.onResumedRunnable);
            this.handler.removeCallbacks(this.onPausedRunnable);
//            this.context = null;
        }
}
