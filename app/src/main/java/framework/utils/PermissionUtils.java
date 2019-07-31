package framework.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/3/22.
 */

public class PermissionUtils {


    // 权限请求的标识码(通用码)
    public final static int REQUEST_PERMISSION_CODE = 10000;
    // 读文件权限
    public final static int READ_EXTERNAL_STORAGE = 10001;
    // 写文件权限
    public final static int WRITE_EXTERNAL_STORAGE = 10002;
    // 读取设备信息
    public final static int READ_PHONE_STATE = 10003;
    // 录音权限
    public final static int RECORD_AUDIO = 10004;


    /**
     * 遍历查询多个权限
     * return true 代表拥有所有权限 , false 代表尚有权限没有,需要申请
     */
    public static boolean checkPermissions(Activity context, String... permissions) {

        List<String> list = new ArrayList<>();

        for (String permission : permissions) {
            // 遍历添加未授权的权限---部分手机会判断出问题，例如oppo
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }

        if (list.isEmpty()) {
            return true;
        } else {
            ActivityCompat.requestPermissions(context, list.toArray(new String[list.size()]), REQUEST_PERMISSION_CODE);
            return false;
        }
    }

    /**
     * 没权限时，就自动申请，有就不用。
     */
    public static void requestWithoutPermission(Activity context,
                                                String permission,
                                                int requestCode) {

        if (Build.VERSION.SDK_INT >= 23
                && hasNotGrant(context, permission))
            ActivityCompat.requestPermissions(context,
                    new String[]{permission},
                    requestCode);

    }

    public static void requestPermission(Activity context,
                                         String permission,
                                         int requestCode) {
        ActivityCompat.requestPermissions(context,
                new String[]{permission},
                requestCode);
    }

    /**
     * 判断某个权限我们有没有(true 有，false 没有)
     */
    public static boolean hasGrant(Context context, String permission) {
        return Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断某个权限我们有没有(true 没有，false 有)
     */
    public static boolean hasNotGrant(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 调这个方法的前提，是您必须确保几组权限都是被拒绝或询问状态。
     * 免得，被允许的权限，还被重新申请。
     * 建议，调另一个方法，一次一个，或者有新需求的时候，再另外封装。
     *
     * @param context
     * @param permission
     * @param requestCode
     */
    public static void requestWithoutPermission(Activity context, String[] permission, int requestCode) {

        ActivityCompat.requestPermissions(context,
                permission,
                requestCode);

    }

    /**
     * Android 6.0 判断用户是拒绝还是允许权限
     *
     * @param permissions   所申请的所有权限名的集合
     * @param grantResults  所申请的权限的结果的集合
     * @param permissionStr 具体哪个权限的名字
     */
    public static boolean grantResult(String[] permissions,
                                      int[] grantResults, String permissionStr) {
        //遍历权限回调
        for (int index = 0; index < permissions.length; ++index) {
            //权限类型
            String permission = permissions[index];
            //定位权限
            if (permission.equals(permissionStr)) {
                //用户允许授权
                if (PackageManager.PERMISSION_GRANTED == grantResults[index]) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        //找不到一样的权限，默认拒绝。
        return false;
    }


    /**
     * 处理权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults return 所有权限是否通过 true通过 false没通过
     */
    public static boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults == null) {
            return true;
        }

        for (int i = 0; i < grantResults.length; ++i) {

            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ToastHelper.showToast("请授权读写SD权限!");
                } else if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ToastHelper.showToast("请授权读写SD权限!");
                } else if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    ToastHelper.showToast("请授权打开摄像头权限!");
                }
                return false;
            }
        }

        return true;

    }

}
