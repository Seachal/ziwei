package com.laka.live.application;

import android.app.Activity;

import com.laka.live.update.UpdateCheckManager;
import com.laka.live.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luwies on 16/8/6.
 */
public class LiveActivityManager {

    private static LiveActivityManager sInstance;

    private ArrayList<Activity> mActivityList;

    private LiveActivityManager() {
        mActivityList = new ArrayList<>();
    }

    public static LiveActivityManager getInstance() {
        if (sInstance == null) {
            synchronized (LiveActivityManager.class) {
                if (sInstance == null) {
                    sInstance = new LiveActivityManager();
                }
            }
        }
        return sInstance;
    }

    public void exitApp(Activity currentActivity) {
        UpdateCheckManager.getInstance().setIgnoreUpdateNow(false);
        if (currentActivity == null) {
            return;
        }
        for (Activity activity : mActivityList) {
            if (activity != currentActivity) {
                activity.finish();
            }
        }

        currentActivity.finish();
    }

    public void addActivity(Activity activity) {
        if (mActivityList != null) {
            mActivityList.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (mActivityList != null) {
            mActivityList.remove(activity);
        }
    }

    public Activity getTopActivity() {
        if (mActivityList == null || mActivityList.isEmpty()) {
            return null;
        }
        return mActivityList.get(mActivityList.size() - 1);
    }

    public List<Activity> getActivitys() {
        return mActivityList;
    }

    /**
     * 关掉指定的页面
     *
     * @param cs 类名.class
     * @return
     */
    public void finishActivity(Class... cs) {

        for (Class cls : cs) {
            finishActivity(cls);
        }

    }

    /**
     * 关掉指定的页面
     *
     * @param cs 类名.class
     * @return
     */
    public boolean finishActivity(Class cs) {

        int beforeRemoveCount = mActivityList.size();

        Activity activity = getActivityByClass(cs);

        if (activity != null) {
            popActivity(activity);
        }

        // 删除后数量比删除前的小,就代表删除成功
        return mActivityList.size() < beforeRemoveCount;

    }


    /**
     * 不断关闭栈顶的activity,直接所传入的页面的class为止。
     * eg:依次打开A-B-C-D-E-F。然后，在F页面调用popUntilActivity(B.class);
     * 则会依次关掉F-E-D-C，到B页面停止。
     */
    public void popUntilActivity(Class... cs) {
        List<Activity> list = new ArrayList<>();
        for (int i = mActivityList.size() - 1; i >= 0; i--) {
            Activity ac = mActivityList.get(i);
            boolean isTop = false;
            for (int j = 0; j < cs.length; j++) {
                if (ac.getClass().equals(cs[j])) {
                    isTop = true;
                    break;
                }
            }
            if (!isTop) {
                list.add(ac);
            } else break;
        }
        for (Iterator<Activity> iterator = list.iterator(); iterator.hasNext(); ) {
            Activity activity = iterator.next();
            Log.log("getShortClassName:" + activity.getComponentName().getShortClassName());
            popActivity(activity);
        }
    }

    /**
     * @param stopCls    不断关闭栈顶的activity,直接所传入的页面的class为止。
     *                   eg:依次打开A-B-C-D-E-F。然后，在F页面调用popUntilActivity(B.class);
     *                   则会依次关掉F-E-D-C，到B页面停止。
     * @param ignoredCls 遇到这个ignoredCls就跳过，不关闭。
     */
    public void popAndIgnoredActivity(Class stopCls, Class... ignoredCls) {

        List<Activity> finishActivities = new ArrayList<>();

        for (int i = mActivityList.size() - 1; i >= 0; i--) {

            boolean isTop = false;
            Activity finishActivity = mActivityList.get(i);

            if (finishActivity.getClass().equals(stopCls)) {
                isTop = true;
            }

            if(isTop) {
                // 当遇到stopCls时跳出循环
                break;
            }else {

                boolean isIgnored = false;

                for (int j = 0; j < ignoredCls.length; j++) {
                    if (finishActivity.getClass().equals(ignoredCls[j])) {
                        isIgnored = true;
                        break;
                    }
                }
                // 当遇到ignoredCls时,不加入关闭队列
                if (!isIgnored) {
                    finishActivities.add(finishActivity);
                }
            }

        }

        for (Iterator<Activity> iterator = finishActivities.iterator(); iterator.hasNext(); ) {
            Activity activity = iterator.next();
            Log.log("getShortClassName:" + activity.getComponentName().getShortClassName());
            popActivity(activity);
        }
    }

    /**
     * 弹出activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        removeActivity(activity);
        activity.finish();
    }

    public Activity getActivityByClass(Class cs) {
        for (Activity ac : mActivityList) {
            if (ac.getClass().equals(cs)) {
                return ac;
            }
        }
        return null;
    }


}
