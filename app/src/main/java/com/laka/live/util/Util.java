package com.laka.live.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.laka.live.R;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by luwies on 16/3/7.
 */
public class Util {
    private static final String TAG = "Util";
    private static final String PACKAGE = "com.tencent.avsdk";
    //独立模式
//    public static int APP_ID_TEXT = 1400001862;
//    public static String UID_TYPE = "1019";
    //托管模式
//	public static final String APP_ID_TEXT = "1400001692";
//	public static final String UID_TYPE = "884";

    public static final String ACTION_START_CONTEXT_COMPLETE = PACKAGE
            + ".ACTION_START_CONTEXT_COMPLETE";
    public static final String ACTION_CLOSE_CONTEXT_COMPLETE = PACKAGE
            + ".ACTION_CLOSE_CONTEXT_COMPLETE";
    public static final String ACTION_ROOM_CREATE_COMPLETE = PACKAGE
            + ".ACTION_ROOM_CREATE_COMPLETE";
    public static final String ACTION_CLOSE_ROOM_COMPLETE = PACKAGE
            + ".ACTION_CLOSE_ROOM_COMPLETE";
    public static final String ACTION_SURFACE_CREATED = PACKAGE
            + ".ACTION_SURFACE_CREATED";
    public static final String ACTION_MEMBER_CHANGE = PACKAGE
            + ".ACTION_MEMBER_CHANGE";
    public static final String ACTION_SHOW_VIDEO_MEMBER_INFO = PACKAGE
            + ".ACTION_SHOW_VIDEO_MEMBER_INFO";
    public static final String ACTION_VIDEO_SHOW = PACKAGE
            + ".ACTION_VIDEO_SHOW";
    public static final String ACTION_MEMBER_VIDEO_SHOW = PACKAGE
            + ".ACTION_MEMBER_VIDEO_SHOW";
    public static final String ACTION_REQUEST_MEMBER_VIEW = PACKAGE + ".ACTION_REQUEST_MEMBER_VIEW";

    public static final String ACTION_VIDEO_CLOSE = PACKAGE
            + ".ACTION_VIDEO_CLOSE";
    public static final String ACTION_ENABLE_CAMERA_COMPLETE = PACKAGE
            + ".ACTION_ENABLE_CAMERA_COMPLETE";
    public static final String ACTION_SWITCH_CAMERA_COMPLETE = PACKAGE
            + ".ACTION_SWITCH_CAMERA_COMPLETE";
    public static final String ACTION_OUTPUT_MODE_CHANGE = PACKAGE
            + ".ACTION_OUTPUT_MODE_CHANGE";
    public static final String ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE = PACKAGE
            + ".ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE";

    public static final String ACTION_CREATE_GROUP_ID_COMPLETE = PACKAGE
            + ".ACTION_CREATE_GROUP_ID_COMPLETE";

    public static final String ACTION_CREATE_ROOM_NUM_COMPLETE = PACKAGE
            + ".ACTION_CREATE_ROOM_NUM_COMPLETE";

    public static final String ACTION_INSERT_ROOM_TO_SERVER_COMPLETE = PACKAGE + ".ACTION_INSERT_ROOM_TO_SERVER_COMPLETE";
    public static final String ACTION_INVITE_MEMBER_VIDEOCHAT = PACKAGE + ".ACTION_INVITE_MEMBER_VIDEOCHAT";
    public static final String ACTION_CLOSE_MEMBER_VIDEOCHAT = PACKAGE + ".ACTION_CLOSE_MEMBER_VIDEOCHAT";

    /**
     * 强制下线
     */
    public static final String ACTION_FORCE_OFFLINE = PACKAGE + ".ACTION_FORCE_OFFLINE";

    /**
     * 房间信息更新通知
     */
    public static final String ACTION_ROOM_ENDPOINT_UPDATE = PACKAGE + ".ACTION_ROOM_ENDPOINT_UPDATE";
    public static final String ACTION_CAMERA_OPEN_IN_LIVE = PACKAGE
            + ".ACTION_CAMERA_OPEN_IN_LIVE";
    public static final String EXTRA_RELATION_ID = "relationId";
    public static final String EXTRA_AV_ERROR_RESULT = "av_error_result";
    public static final String EXTRA_VIDEO_SRC_TYPE = "videoSrcType";
    public static final String EXTRA_IS_ENABLE = "isEnable";
    public static final String EXTRA_IS_FRONT = "isFront";
    public static final String EXTRA_IDENTIFIER = "identifier";
    public static final String EXTRA_IS_ASKFOR_MEMVIDEO = "askfor_memvideo";
    public static final String EXTRA_SELF_IDENTIFIER = "selfIdentifier";
    public static final String EXTRA_ROOM_ID = "roomId";
    public static final String EXTRA_IS_VIDEO = "isVideo";
    public static final String EXTRA_IDENTIFIER_LIST_INDEX = "QQIdentifier";

    public static final String JSON_KEY_DATA = "data";
    public static final String JSON_KEY_CODE = "code";
    public static final String JSON_KEY_LOGIN_DATA = "logindata";
    public static final String JSON_KEY_VERSION = "version";
    public static final String JSON_KEY_FORCE = "force";
    public static final String JSON_KEY_USER_INFO = "userinfo";

    public static final String EXTRA_LIVE_VIDEO_INFO = "LiveVideoInfo";
    public static final String EXTRA_USER_PHONE = "userphone";
    public static final String EXTRA_PASSWORD = "password";
    public static final String EXTRA_USER_NAME = "username";
    public static final String EXTRA_USER_SIG = "usersig";
    public static final String EXTRA_SEX = "sex";
    public static final String EXTRA_CONSTELLATION = "constellation";
    public static final String EXTRA_PRAISE_NUM = "praisenum";
    public static final String EXTRA_VIEWER_NUM = "viewernum";
    public static final String EXTRA_SIGNATURE = "signature";
    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_HEAD_IMAGE_PATH = "headimagepath";
    public static final String EXTRA_GROUP_ID = "groupid";
    public static final String EXTRA_PROGRAM_ID = "programid";
    public static final String EXTRA_ROOM_NUM = "roomnum";
    public static final String EXTRA_LIVE_TITLE = "livetitle";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_LIVEPHONE = "livephone";
    public static final String EXTRA_LEAVE_MODE = "leave_mode";
    public static final String EXTRA_REPLAYID = "replayid";
    public static final String EXTRA_RECORDTIME = "duration";

    public static final int SHOW_RESULT_CODE = 10000;
    public static final int EDIT_RESULT_CODE = 20000;
    public static final int VIEW_RESULT_CODE = 30000;

    public static final int AUDIO_VOICE_CHAT_MODE = 0;
    public static final int AUDIO_MEDIA_PLAY_RECORD = AUDIO_VOICE_CHAT_MODE + 1;
    public static final int AUDIO_MEDIA_PLAYBACK = AUDIO_MEDIA_PLAY_RECORD + 1;

    public static final int TRUSTEESHIP = 1;
    public static final int INTEGERATE = TRUSTEESHIP + 1;

    public static final int ENV_FORMAL = 0;
    public static final int ENV_TEST = ENV_FORMAL + 1;

    public static ProgressDialog newProgressDialog(Context context, int titleId) {
        ProgressDialog result = new ProgressDialog(context);
        result.setTitle(titleId);
        result.setIndeterminate(true);
        result.setCancelable(true);


        return result;
    }

    public static AlertDialog newErrorDialog(Context context, int titleId) {
        return new AlertDialog.Builder(context)
                .setTitle(titleId)
                .setMessage(R.string.error_code_prefix)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).create();
    }

    public static void switchWaitingDialog(Context ctx,
                                           ProgressDialog waitingDialog, int dialogId, boolean isToShow) {
        if (isToShow) {
            if (waitingDialog == null || !waitingDialog.isShowing()) {
                if (ctx instanceof Activity) {
                    Activity ctx2 = (Activity) ctx;
                    if (ctx2.isFinishing() == true) return;
                    ((Activity) ctx).showDialog(dialogId);
                }
            }
        } else {
            if (waitingDialog != null && waitingDialog.isShowing()) {
                waitingDialog.dismiss();
            }
        }
    }


    /**
     * 获取网络类型
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static void copy(Context context, String strToCopy) {
        if (context == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(strToCopy);
        } else {
            ClipboardManager clipboard = (ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(null, strToCopy));

        }
    }

    /**
     * 拍照
     *
     * @param activity
     * @param file
     * @param requestCode
     */
    public static void takePhoto(Activity activity, File file, int requestCode) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 从相册选择照片
     *
     * @param activity
     * @param file
     * @param requestCode
     */
    public static void choicePhoto(Activity activity, File file, boolean isCrop, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        intent.setType("image/*");
        intent.putExtra("crop", isCrop);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 选择相片不裁剪
     *
     * @param activity
     * @param requestCode
     */
    public static boolean choicePhoto(Activity activity, int requestCode) {
        boolean result = true;
        Intent intent;
        Log.d(TAG," choicePhoto SDK_INT="+Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT <= 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            result = false;
        }
        return result;
    }

    /**
     * 裁剪相片
     *
     * @param activity
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public static void cropImageUri(Activity activity, Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        Log.d("qygxsq" , "start_crop");
        activity.startActivityForResult(intent, requestCode);
    }

    public static File createImageFile(Context context, File storageDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        if (storageDir == null || storageDir.exists() == false) {
            storageDir = context.getExternalCacheDir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                if (idx == -1) {
                    return null;
                }
                result = cursor.getString(idx);
                cursor.close();
            }
        } catch (Exception e) {
            return "";
        }
        return result;
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
        } else {
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        return size;
    }

    public static int getScreenWidth(Context context) {
        return getScreenSize(context).x;
    }

    public static int getScreenHeight(Context context) {
        return getScreenSize(context).y;
    }

    public static float getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static void delectAllCache(Context context) {
        if (context == null) {
            return;
        }
        try {
            deleteFolderFile(context.getCacheDir(), false);
            deleteFolderFile(context.getExternalCacheDir(), false);

            Fresco.getImagePipeline().clearDiskCaches();
        } catch (IOException e) {

        }
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @return
     */
    public static void deleteFolderFile(File file, boolean deleteThisPath)
            throws IOException {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {// 处理目录
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFolderFile(files[i], true);
            }
        }
        if (deleteThisPath) {
            if (!file.isDirectory()) {// 如果是文件，删除
                file.delete();
            } else {// 目录
                if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                    file.delete();
                }
            }
        }
    }

    public static File getSDFile() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir;

    }

    public static String md5(String string) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(string.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
