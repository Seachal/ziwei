
package com.laka.live.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;

import com.google.gson.Gson;
import com.laka.live.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    public static final int DEFAULT_REQUEST_CODE = 0x1234;
    public static SimpleDateFormat YM_FORMATER = new SimpleDateFormat("yyMM");
    //    public static SimpleDateFormat YMD_DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat YMD_DATE_FORMATER2 = new SimpleDateFormat("yy-MM-dd");
    public static SimpleDateFormat LONG_DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat YMD_DATE_FORMATER = new SimpleDateFormat("yyyy.MM.dd");

    public static SimpleDateFormat HOUR_MINUTE_FORMATER = new SimpleDateFormat("HH:mm");

    public static SimpleDateFormat WEEKDAY_FORMATER = new SimpleDateFormat("EEEE");
    public static SimpleDateFormat HOUR_MINUTE_SESOND_FORMATER = new SimpleDateFormat("mm:ss.SS");
    public static SimpleDateFormat MINUTE_SESOND_FORMATER = new SimpleDateFormat("mm:ss");
    public static SimpleDateFormat SHORT_DATE_FORMATER = new SimpleDateFormat("MM.dd HH:mm");
    private static final String TAG = "Utils";

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || "null".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断字符串是否为空 包括 "0" "0.0"
     */
    public static boolean isEmpty2(String str) {
        return str == null || "".equals(str) || "null".equals(str) || "0".equals(str)
                || "0.0".equals(str);
    }

    /**
     * 判断字符串是否为空
     */
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断字符串是否为空
     */
    public static <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    /**
     * 判断字符串是否为邮箱
     */
    public static boolean isEmail(String str) {
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher mc = pattern.matcher(str);
        return mc.matches();
    }

    /**
     * 判断是否是手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        // Pattern p = Pattern
        // .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断List是否为空
     */
    public static boolean listIsNullOrEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断map是否为空
     */
    public static boolean mapIsNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty() || map.size() == 0;
    }

    /**
     * 判断object是否为空
     */
    public static boolean objectIsNull(Object object) {
        return object == null;
    }

    /**
     * 获取文件后缀
     */
    public static String getFileType(String fileUri) {
        File file = new File(fileUri);
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return fileType;
    }

    // a integer to xx:xx:xx
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                // LogUtil.Log("secToTime", "hour="+hour);
                if (hour > 99)// 99
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String secToFullTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                // LogUtil.Log("secToTime", "hour="+hour);
                if (hour > 99)// 99
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * @param date1
     * @param date2
     * @return long
     * @description 计算两个时间的时间差
     * @history created by dengwenguang on Mar 28, 2011
     */
    public static long diffBetweenTwoTime(Date date1, Date date2) {
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        return time1 - time2;
    }

    // 返回2001-01-01 00:00:00经过的秒数
    public static long timeIntervalSinceReferenceDate(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));// 时区
            Date oldDate = sdf.parse("2001-01-01 00:00:00");
            long secs = date.getTime() - oldDate.getTime();
            return secs / 1000 + 600;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较两个时间大小
     **/
    public static boolean compareDate(String firstDate, String secondDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fir = df.parse(firstDate);
            Date sec = df.parse(secondDate);
            if (sec.after(fir)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取一周前的时间
     */
    public static Date getBeforeWeekDate(Calendar cal) {
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 8);

        return cal.getTime();
    }

    /**
     * 获取前一天的时间
     */
    public static Date getBeforeDate(Calendar cal) {
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
        return cal.getTime();
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale   （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale    （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue, float fontScale) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return px2sp(pxValue, fontScale);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 判断是不是字符串
    public static boolean isStrAndLetter(String str) {
        // StringBuffer sb=new StringBuffer();
        // StringBuffer sb2=new StringBuffer();
        System.out.println(str);
        boolean flag = true;
        for (int i = 0; i < str.length(); i++) {
            // System.out.println(str.substring(i,i+1).matches("[\\u4e00-\\u9fbf]+"));
            if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fbf]+")
                    || str.substring(i, i + 1).matches("[a-zA-Z]")) {
            } else {
                return false;
            }
        }
        return flag;
    }

    // 获取群发分隔符
    public static String getSmsSeparator() {
        if (Build.MODEL.contains("HTC") || Build.MODEL.contains("C8500")
                || Build.MODEL.contains("C8600") || Build.MODEL.contains("C8800")
                || Build.MODEL.contains("C8650")) {
            return ";";
        }
        return ",";
    }

    // public static boolean isSuccess(ResultMsg result) {
    // return result != null
    // && result.ERROR_CODE_NONE.equals(result.getErrorCode());
    // }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean isAvaiableSpace(int sizeMb) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            // File file = new File(sdcard);
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpare = (blocks * blockSize) / (1024 * 1024);
            // int availableSpare = (int)
            // (statFs.getBlockSize()*((long)statFs.getAvailableBlocks()-4))/(1024*1024);//以比特计算
            // 换算成MB
            System.out.println("availableSpare = " + availableSpare);
            if (sizeMb > availableSpare) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static String covertUrlToName(String downloadUrl) {
        int index = downloadUrl.lastIndexOf("/");
        String name = downloadUrl.substring(index);
        return name;
    }

    public static boolean checkIsPhone(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取包信息.
     *
     * @param context the context
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            String packageName = context.getPackageName();
            info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();

        } else {
            mResources = context.getResources();
        }
        // DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5,
        // xdpi=160.421, ydpi=159.497}
        // DisplayMetrics{density=2.0, width=720, height=1280,
        // scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }


    /**
     * 聊天时间显示
     *
     * @param time 时间戳
     * @return
     */
    static long oneDaySecond = 1000 * 60 * 60 * 24;

    public static String getChatDate(long second) {//计算用毫秒
        long time = second * 1000;
        StringBuffer sb = new StringBuffer();

        long nowTime = System.currentTimeMillis();
        long distance = nowTime - time;
        Date timeDate = new Date(time);

        if (distance < oneDaySecond) {
            //当天的消息，以每5分钟为一个跨度的显示时间；
            sb.append(HOUR_MINUTE_FORMATER.format(timeDate));
//            com.laka.live.util.Log.d(TAG,"显示时间="+LONG_DATE_FORMATER.format(timeDate));
        } else if (distance >= oneDaySecond && distance <= oneDaySecond * 2) {
            //24-48小时显示昨日
            sb.append(ResourceHelper.getString(R.string.yesterday));
        } else if (distance < oneDaySecond * 7 && distance > oneDaySecond * 2) {
            //消息超过1天、小于1周，显示星期+
            sb.append(WEEKDAY_FORMATER.format(timeDate));
        } else {
            //消息大于1周，显示手机收发时间的日期
            sb.append(YMD_DATE_FORMATER2.format(timeDate));
        }
        return sb.toString();
    }


    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(long timeStr) {
        StringBuffer sb = new StringBuffer();
        // long t = Long.parseLong(timeStr);
        long t = timeStr;
        long time = System.currentTimeMillis() - (t);// *1000
        long mill = (long) Math.ceil(time / 1000);// 秒前
        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    public static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }


    public static <T> T dataToResult(Object data, Class<T> mClazz) {
        Gson mGson = new Gson();
        // LogUtil.Log(TAG, "dataToResult data="+data.toString());
        return mGson.fromJson(data.toString(), mClazz);
    }


    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "kb");
    }

    // public static Map<String, Object> urlSplit(String data){
    // StringBuffer strbuf = new StringBuffer();
    // StringBuffer strbuf2 = new StringBuffer();
    // Map<String ,Object> map = new HashMap<String,Object>();
    // for(int i =0;i<data.length();i++){
    //
    // if(data.substring(i,i+1).equals("=")){
    //
    // for(int n=i+1;n<data.length();n++){
    // if(data.substring(n,n+1).equals("&")|| n ==data.length()-1){
    // map.put(strbuf.toString(), strbuf2);
    // strbuf =new StringBuffer("");
    // strbuf2 =new StringBuffer("");
    // i=n;
    // break;
    // }
    // strbuf2.append(data.substring(n,n+1));
    // }
    // continue;
    // }
    // strbuf.append(data.substring(i,i+1));
    // }
    //
    // return map;
    // }

    /**
     * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        // 每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 解析出url请求的路径，包括页面
     *
     * @param strURL url地址
     * @return url路径
     */
    public static String UrlPage(String strURL) {
        String strPage = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 0) {
            if (arrSplit.length > 1) {
                if (arrSplit[0] != null) {
                    strPage = arrSplit[0];
                }
            }
        }

        return strPage;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    public static String minuteToHM(int min) {
        if (min < 60) {
            return min + "分";
        } else {
            int hour = min / 60;
            int fen = min % 60;
            return hour + "时" + fen + "分";
        }
    }

    public static String minuteToHMFull(int min) {
        if (min < 60) {
            return min + "分钟";
        } else if (min < 60 * 24) {
            int hour = min / 60;
            int fen = min % 60;
            return hour + "小时" + fen + "分钟";
        } else {
            int hour = min / 60;
            int day = hour / 24;
            hour = hour - day * 24;
            int fen = min % 60;
            return day + "天" + hour + "小时" + fen + "分钟";
        }
    }

    /**
     * 秒单位换算(不足1分钟的返回具体秒，超过的直接返回分钟数)
     *
     * @param second
     * @return
     */
    public static String secondToMin(int second) {
        if (second < 60) {
            return second + "秒";
        } else {
            int min = second / 60;
            return min + "分钟";
        }
    }

    public static Bitmap createVideoThumbnail(String filePath) {
        // MediaMetadataRetriever is available on API Level 8
        // but is hidden until API Level 10
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();

            Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, filePath);

            // The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= 9) {
                return (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null)
                        return bitmap;
                }
                return (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } catch (InstantiationException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (InvocationTargetException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (ClassNotFoundException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (NoSuchMethodException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (IllegalAccessException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 1;
        if (context != null) {
            try {
                versionCode = context.getPackageManager().
                        getPackageInfo(context.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {

            }
        }
        return versionCode;
    }

    /**
     * 调起打电话的应用
     */
    public static void call(Context context, String phone) {
        if (context == null || TextUtils.isEmpty(phone)) {
            return;
        }
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + phone));
        context.startActivity(phoneIntent);
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static ByteBuffer allocate(int capacity) {
        return ByteBuffer.allocate(capacity).order(ByteOrder.LITTLE_ENDIAN);
    }

    //查找前置摄像头ID
    public static int getFrontCameraId() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    //查找后置摄像头ID
    public static int getBackCameraId() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    public static long getCurrentTimeSecond() {
        return System.currentTimeMillis() / 1000;
    }


    public static long timeStart = 0;
    public static long timeEnd = 0;

    public static void resetTimeCount() {
        timeStart = 0;
        timeEnd = 0;
    }

    public static void doTimeCountStart() {
        timeStart = System.currentTimeMillis();
        com.laka.live.util.Log.d("Room" + TAG, "开始计时 timeStart=" + timeStart);
    }

    public static void doTimeCountEnd() {
        timeEnd = System.currentTimeMillis();
        com.laka.live.util.Log.d("Room" + TAG, "结束计时 耗时=" + (timeEnd - timeStart));
        resetTimeCount();
    }

    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    public static int getDisplayOrientation(int degrees, int cameraId) {
        // See android.hardware.Camera.setDisplayOrientation for
        // documentation.
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }

        return result;
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * DeCompress the ZIP to the path
     *
     * @param zipFileString name of ZIP
     * @param outPathString path to be unZIP
     * @throws Exception
     */
    public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {

                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    public static void unZipFolder(String fileName, String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
//            szName = fileName;
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + fileName);
                folder.mkdirs();
            } else {

                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    public static String getLauncherTopApp(Context context) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
        if (null != appTasks && !appTasks.isEmpty()) {
            return appTasks.get(0).topActivity.getPackageName();
        }
//        } else {
//            AppChecker appChecker = new AppChecker();
//            String packageName = appChecker.getForegroundApp(context);
//            return packageName;
//        }
        return "";
    }


    public static void requestUsageStatsPermission(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(context)) {
            context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean hasUsageStatsPermission(Context context) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }

        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }


    /**
     * 求给定双精度数组中值的最大值
     *
     * @param inputData 输入数据数组
     * @return 运算结果, 如果输入值不合法，返回为-1
     */
    public static double getMax(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double max = inputData[0];
        for (int i = 0; i < len; i++) {
            if (max < inputData[i])
                max = inputData[i];
        }
        return max;
    }

    /**
     * 求求给定双精度数组中值的最小值
     *
     * @param inputData 输入数据数组
     * @return 运算结果, 如果输入值不合法，返回为-1
     */
    public static double getMin(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double min = inputData[0];
        for (int i = 0; i < len; i++) {
            if (min > inputData[i])
                min = inputData[i];
        }
        return min;
    }

    /**
     * 求给定双精度数组中值的和
     *
     * @param inputData 输入数据数组
     * @return 运算结果
     */
    public static double getSum(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum = sum + inputData[i];
        }

        return sum;

    }

    /**
     * 求给定双精度数组中值的数目
     * <p>
     * Data 输入数据数组
     *
     * @return 运算结果
     */
    public static int getCount(double[] inputData) {
        if (inputData == null)
            return -1;

        return inputData.length;
    }

    /**
     * 求给定双精度数组中值的平均值
     *
     * @param inputData 输入数据数组
     * @return 运算结果
     */
    public static double getAverage(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double result;
        result = getSum(inputData) / len;

        return result;
    }

    /**
     * 求给定双精度数组中值的平方和
     *
     * @param inputData 输入数据数组
     * @return 运算结果
     */
    public static double getSquareSum(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double sqrsum = 0.0;
        for (int i = 0; i < len; i++) {
            sqrsum = sqrsum + inputData[i] * inputData[i];
        }


        return sqrsum;
    }

    /**
     * 求给定双精度数组中值的方差
     *
     * @param inputData 输入数据数组
     * @return 运算结果
     */
    public static double getVariance(double[] inputData) {
        int count = getCount(inputData);
        double sqrsum = getSquareSum(inputData);
        double average = getAverage(inputData);
        double result;
        result = (sqrsum - count * average * average) / count;

        return result;
    }

    public static double getVariance(List<Double> inputData) {
        double[] data = new double[inputData.size()];
        for (int i = 0; i < inputData.size(); i++) {
            data[i] = inputData.get(i);
        }
        return getVariance(data);
    }

    public static double getStandardDiviation(List<Double> inputData) {
        double[] data = new double[inputData.size()];
        for (int i = 0; i < inputData.size(); i++) {
            data[i] = inputData.get(i);
        }
        return getStandardDiviation(data);
    }

    /**
     * 求给定双精度数组中值的标准差
     *
     * @param inputData 输入数据数组
     * @return 运算结果
     */
    public static double getStandardDiviation(double[] inputData) {
        double result;
        //绝对值化很重要
        result = Math.sqrt(Math.abs(getVariance(inputData)));

        return result;

    }

    public static int readUsage() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();

            String[] toks = load.split(" ");

            long currTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]);
            long currIdle = Long.parseLong(toks[5]);

            float usage = 0;
            long total = 0;
            long idle = 0;
            usage = (currTotal - total) * 100.0f / (currTotal - total + currIdle - idle);
            total = currTotal;
            idle = currIdle;
            com.laka.live.util.Log.d(TAG, "onPublishQulityUpdate usage=" + usage + " total=" + total + " idle=" + idle);
            return (int) usage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static void requestPermission(Context context, String permission) {
        if (checkPermission(context, permission)) {
            return;
        }


    }


    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param filePath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }


    /**
     * 对startActivity的简单封装
     */
    public static void startActivity(Context from, Class<?> clazz) {
        startActivity(from, clazz, (Bundle) null);
    }

    public static void startActivity(Context from, Class<?> clazz,
                                     Bundle extras) {
        Intent i = new Intent(from, clazz);

        if (extras != null) {
            i.putExtras(extras);
        }

        from.startActivity(i);
    }

    public static void startActivity(Context from, Class<?> clazz, String title) {
        Bundle extras = new Bundle();
        extras.putString("title", title);
        startActivity(from, clazz, extras);
    }

    /**
     * 对startActivity的简单封装
     */
    public static void startActivity(Activity from, Class<?> clazz) {
        startActivity(from, clazz, (Bundle) null);
    }

    public static void startActivity(Activity from, Class<?> clazz, String title) {
        Bundle extras = new Bundle();
        extras.putString("title", title);
        startActivity(from, clazz, extras);
    }

    /**
     * @param code 携带一个整型数据,可用来标识角色类型、其它单一整型数据等.
     */
    public static void startActivity(Activity from, Class<?> clazz, int code) {
        Bundle extras = new Bundle();
        extras.putInt("code", code);
        startActivity(from, clazz, extras);
    }

    /**
     * @param code 携带一个整型数据,可用来标识角色类型、其它单一整型数据等.
     */
    public static void startActivity(Context from, Class<?> clazz, int code) {
        Bundle extras = new Bundle();
        extras.putInt("code", code);
        startActivity(from, clazz, extras);
    }

    public static void startActivity(Activity from, Class<?> clazz,
                                     Bundle extras) {
        Intent i = new Intent(from, clazz);

        if (extras != null) {
            i.putExtras(extras);
        }

        from.startActivity(i);
    }

    public static void startTopActivity(Activity from, Class<?> clazz,
                                        Bundle extras) {
        Intent i = new Intent(from, clazz);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (extras != null) {
            i.putExtras(extras);
        }
        from.startActivity(i);
    }

    public static void startActivityForResult(Activity from, Class<?> clazz,
                                              Bundle extras, int requestCode) {

        startActivityForResult(from, clazz, requestCode, extras);
    }

    public static void startActivityForResult(Activity from, Class<?> clazz,
                                              int requestCode) {

        startActivityForResult(from, clazz, requestCode, null);
    }

    public static void startActivityForResult(Activity from, Class<?> clazz,
                                              String title, int requestCode) {
        Bundle extras = new Bundle();
        extras.putString("title", title);
        startActivityForResult(from, clazz, requestCode, extras);
    }

    public static void startActivityForResult(Activity from, Class<?> clazz,
                                              String title) {
        Bundle extras = new Bundle();
        extras.putString("title", title);
        startActivityForResult(from, clazz, DEFAULT_REQUEST_CODE, extras);
    }

    public static void startActivityForResult(Activity from, Class<?> clazz,
                                              Bundle extras) {
        startActivityForResult(from, clazz, DEFAULT_REQUEST_CODE, extras);
    }

    public static void startActivityForResult(Activity from, Class<?> clazz,
                                              int requestCode, Bundle extras) {
        Intent i = new Intent(from, clazz);
        if (extras != null) {
            i.putExtras(extras);
        }
        from.startActivityForResult(i, requestCode);
    }

    /**
     * 这个方法确保Dialog关闭的时候,不会抛出空指针异常
     *
     * @param dialog
     */
    public static void dismiss(Dialog dialog) {

        // 如果对话框不为空,就退出
        if (dialog != null)
            dialog.dismiss();
    }

    /**
     * 判断是否为偶数
     *
     * @param number 要判断的数字
     * @return true 偶数；false 奇数
     */
    public static boolean judgeOdd(int number) {
        return number % 2 == 0;
    }

    public static int retureMaxNum(int[] num) {
        if (num == null || num.length == 0) {
            return 0;
        }

        if (num.length == 1) {
            return num[0];
        }

        int max = num[0];

        for (int i : num) {
            if (i > max) {
                max = i;
            }
        }

        return max;
    }

    public static int retureMinNum(int[] num) {
        if (num == null || num.length == 0) {
            return 0;
        }

        if (num.length == 1) {
            return num[0];
        }

        int min = num[0];

        for (int i : num) {
            if (i < min) {
                min = i;
            }
        }

        return min;
    }

    public static String getCommentDate(long second) {
        long time = second * 1000;
        return SHORT_DATE_FORMATER.format(time);
    }

    public static String float2String(float value) {
        if (value - (int) value == 0) {
            return String.valueOf((int) value);
        } else {
            return String.valueOf(value);
        }

    }

    public static boolean isHttpUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.startsWith("http") || url.startsWith("https")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取StatusBar高度
     *
     * @return StatusBar高度 pixels
     */
    public static int getStatusBarHeight(Context context) {
        int mStatusBarHeight = 0;
        if (mStatusBarHeight <= 0) {
            int resourceId = context.getApplicationContext().getResources().getIdentifier(
                    "status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusBarHeight = context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return mStatusBarHeight;
    }
}
