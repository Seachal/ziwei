package com.laka.live.util;

import com.laka.live.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by gangqing on 2015/12/28.
 * Email:denggangqing@ta2she.com
 */
public class TimeUtil {
    private static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat mLiveDateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat mPostDateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
    private static final SimpleDateFormat mTimeDataFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat mWeekDataFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private static final SimpleDateFormat mSecondDataFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat mLiveDateFormat2 = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());


    public static String getSystemTime() {
        String date = "";
        date = mSimpleDateFormat.format(new Date());
        return date;
    }

    public static String formatTime(String time, DateFormat dateFormat) {
        if (dateFormat == null) {
            dateFormat = mDateFormat;
        }
        Date myDate = null;
        try {
            myDate = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(myDate);
    }

    /**
     * 获取网络时间
     *
     * @return
     */
    public static String getTime() {
        // 取得资源对象
        URL url;
        URLConnection uc = null;
        try {
            url = new URL("http://www.bjtime.cn");
            // 生成连接对象
            uc = url.openConnection();
            // 发出连接
            uc.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long time = uc.getDate();
        Date date = new Date(time);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getLiveTime(String currentTime) {
        if (StringUtils.isEmpty(currentTime)) {
            return currentTime;
        }
        try {
            Date date = mSimpleDateFormat.parse(currentTime);
            return mLiveDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentTime;
    }

    public static String getPostTime(String currentTime) {
        if (StringUtils.isEmpty(currentTime)) {
            return currentTime;
        }
        try {
            Date date = mSimpleDateFormat.parse(currentTime);
            return mPostDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentTime;
    }

    public static String getFormatMillisTime(String millis) {
        Calendar calendar = Calendar.getInstance();
        long time = StringUtils.parseLong(millis);
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        return getCDTime(date);
    }

    public static String getFormatMillisTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        return getCDTime(date);
    }

    public static String getCDTime(Date oldTime) {
        String time = "";
        if (oldTime == null) {
            return time;
        }
        String nowTime = getSystemTime();
        Date now, date;
        try {
            now = mSimpleDateFormat.parse(nowTime);
            date = oldTime;
            long cdTime = now.getTime() - date.getTime();
            long day = cdTime / (24 * 60 * 60 * 1000);
            long hour = (cdTime / (60 * 60 * 1000) - day * 24);
            long min = ((cdTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long second = (cdTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if (day >= 1) {
                if (day < 2) {
                    time = "昨天";
                } else {
                    time = mDateFormat.format(oldTime);
                }
            } else {
                if (hour > 0 && hour < 24) {
                    time = hour + "小时前";
                } else {
                    if (min > 0 && min < 60) {
                        time = min + "分钟前";
                    } else if ((second >= 0 && second <= 60)) {
                        time = "刚刚";
                    } else {
                        time = mDateFormat.format(oldTime);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getVideoTime(String timeString) {
        String time = "";
        if (timeString == null) {
            return time;
        }
        Date date;
        try {
            Calendar currentDate = new GregorianCalendar();
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            long todayStart = currentDate.getTime().getTime();
            long dayTime = (24 * 60 * 60 * 1000);
            date = mSimpleDateFormat.parse(timeString);
            long dateTime = date.getTime();
            if (dateTime > todayStart) {
                time = ResourceHelper.getString(R.string.today_tips) + mSecondDataFormat.format(date);
            } else if (dateTime > todayStart - dayTime) {
                time = ResourceHelper.getString(R.string.yesterday_tips) + mSecondDataFormat.format(date);
            } else if (dateTime > todayStart - 6 * dayTime) {
                time = mWeekDataFormat.format(date);
            } else {
                time = mLiveDateFormat.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            time = timeString;
        }
        return time;
    }

    /**
     * 获取时间差
     *
     * @param oldTime
     * @return
     */
    public static String getCDTime(String oldTime) {
        String time = "";
        if (StringUtils.isEmpty(oldTime)) {
            return time;
        }
        try {
            Date date = mSimpleDateFormat.parse(oldTime);
            return getCDTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long getDiffTime(long oldTime) {
        return System.currentTimeMillis() / 1000 - oldTime;
    }

    public static String getCDTime(long oldTime) {
        String time = "";
        if (oldTime == 0) {
            return time;
        }
        String nowTime = getSystemTime();
        Date now, date;
        try {
            now = mSimpleDateFormat.parse(nowTime);
            date = mSimpleDateFormat.parse(mSimpleDateFormat.format(new Date(oldTime * 1000)));
            long cdTime = now.getTime() - date.getTime();
            long day = cdTime / (24 * 60 * 60 * 1000);
            long hour = (cdTime / (60 * 60 * 1000) - day * 24);
            long min = ((cdTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long second = (cdTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if (day >= 1) {
                if (day < 2) {
                    time = "昨天";
                } else {
                    time = mDateFormat.format(new Date(oldTime * 1000));
                }
            } else {
                if (hour > 0 && hour < 24) {
                    time = hour + "小时前";
                } else {
                    if (min > 0 && min < 60) {
                        time = min + "分钟前";
                    } else if ((second >= 0 && second <= 60)) {
                        time = "刚刚";
                    } else {
                        time = mDateFormat.format(new Date(oldTime * 1000));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 按格式"yyyy-MM-dd HH:mm:ss"取得时间
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 按格式format取得时间
     * 假若后台传递的是标准的时间戳（秒），那么本地需要三位数，因为java的时间戳是毫秒制的
     *
     * @return
     */
    public static String getTime(String format, long timeStamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(timeStamp);
    }

    /**
     * 判断是否为同一天，以0点为界，不是以时差24小时为界
     */
    public static boolean isToday(long timeStamp) {
        // 今天0点的时间
        long todayTimestamp = getDayBeginTimestamp();
        if ((timeStamp - todayTimestamp) < 3600000 * 24) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取今天0点的Timestamp
     *
     * @return
     */
    public static long getDayBeginTimestamp() {
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        Date date2 = new Date(date.getTime() - gc.get(gc.HOUR_OF_DAY) * 60 * 60
                * 1000 - gc.get(gc.MINUTE) * 60 * 1000 - gc.get(gc.SECOND)
                * 1000);
        return new Timestamp(date2.getTime()).getTime();
    }

    /**
     * 获取时分秒
     *
     * @param time 秒
     * @return
     */
    public static String getTime(int time) {

        String timeStr;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    /**
     * 获取带年月日的时间
     *
     * @param time 秒
     * @return
     */
    public static String getTimeWithStr(long time) {

        String timeStr;
        long second, minute, hour;

        if (time <= 0) {
            return "00:00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    /**
     * @param time
     * @return 返回折扣时间
     */
    public static long[] getDiscountTime(long times[], long time) {

        if (times != null) {
            // 减少运算,一般用户不会在一个界面等超过一个小时
            if (times[3] > 0) {
                --times[3];
                return times;
            } else if (times[2] > 0) {
                --times[2];
                times[3] = 59;
            } else {
                return getDiscountTime(time);
            }
            return times;
        } else {
            return getDiscountTime(time);
        }
    }

    /**
     * @param timeStamp
     * @return 返回折扣时间
     */
    public static long[] getDiscountTime(long timeStamp) {

        long times[] = new long[]{0, 0, 0, 0};

        long day = timeStamp / (24 * 60 * 60 * 1000);
        long hour = (timeStamp / (60 * 60 * 1000) - day * 24);
        long minute = ((timeStamp / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (timeStamp / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (timeStamp <= 0) {
            return times;
        } else {
            times[0] = day > 99 ? 99 : day;
            times[1] = hour;
            times[2] = minute;
            times[3] = second;
        }

        return times;
    }

    /**
     * @param time
     * @return 返回折扣时间
     */
    public static long[] getCoursesDiscountTime(long times[], long time) {

        if (times != null) {
            // 减少运算,一般用户不会在一个界面等超过一个小时
            // 所以，只要4个判断就够了。超过一个小时的，按正常运算走。
            if (times[7] > 0) {
                --times[7];
                return times;
            } else if (times[6] > 0) {
                --times[6];
                times[7] = 9;
            } else if (times[5] > 0) {
                --times[5];
                times[6] = 5;
                times[7] = 9;
            } else if (times[4] > 0) {
                --times[4];
                times[5] = 9;
                times[6] = 5;
                times[7] = 9;
            } else {
                return getCoursesDiscountTime(time);
            }
            return times;
        } else {
            return getCoursesDiscountTime(time);
        }
    }

    /**
     * @param timeStamp
     * @return 返回折扣时间
     */
    public static long[] getCoursesDiscountTime(long timeStamp) {

        long times[] = new long[]{0, 0, 0, 0, 0, 0, 0, 0};

        long second, minute, hour, day;

        if (timeStamp <= 0) {
            return times;
        } else {
            minute = timeStamp / 60;
            if (minute < 60) {
                // 小于1小时
                second = timeStamp % 60;
                times[4] = minute / 10;
                times[5] = minute % 10;
                times[7] = second % 10;
            } else if ((minute / 60) < 24) {
                // 小于1天。
                hour = minute / 60;
                minute = minute % 60;
                second = timeStamp - hour * 3600 - minute * 60;
                times[2] = hour / 10;
                times[3] = hour % 10;
                times[4] = minute / 10;
                times[5] = minute % 10;
                times[6] = second / 10;
                times[7] = second % 10;
            } else {
                // 大于1天
                day = minute / 60 / 24;
                if (day > 99) {
                    day = 99;
                }
                hour = minute / 60;
                minute = minute % 60;
                second = timeStamp - hour * 3600 - minute * 60;
                times[0] = day / 10;
                times[1] = day % 10;
                times[2] = (hour % 24) / 10;
                times[3] = (hour % 24) % 10;
                times[4] = minute / 10;
                times[5] = minute % 10;
                times[6] = second / 10;
                times[7] = second % 10;
            }
        }
        return times;
    }

    public static String getLiveTime2(long currentTime) {
        try {
            return mLiveDateFormat2.format(currentTime * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeMax3(long oldTime) {
        String time = "";
        if (oldTime == 0) {
            return time;
        }
        String nowTime = getSystemTime();
        Date now, date;
        try {
            now = mSimpleDateFormat.parse(nowTime);
            date = mSimpleDateFormat.parse(mSimpleDateFormat.format(new Date(oldTime * 1000)));
            long cdTime = now.getTime() - date.getTime();
            long day = cdTime / (24 * 60 * 60 * 1000);
            long hour = (cdTime / (60 * 60 * 1000) - day * 24);
            long min = ((cdTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long second = (cdTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if (day >= 1) {
                if (day >= 3) {
                    time = "3天前";
                } else {
                    time = day + "天前";
                }
            } else {
                if (hour > 0 && hour < 24) {
                    time = hour + "小时前";
                } else {
                    if (min > 0 && min < 60) {
                        time = min + "分钟前";
                    } else if ((second >= 0 && second <= 60)) {
                        time = "刚刚";
                    } else {
                        time = mDateFormat.format(new Date(oldTime * 1000));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        return mSimpleDateFormat;
    }

    /**
     * 秒数转换 00：00：00显示
     *
     * @param secondStr
     * @return
     */
    public static String covertSecondToHMS(String secondStr) {
        int time = Integer.valueOf(secondStr);
        int hour = time / 3600;

        StringBuffer buffer = new StringBuffer();
        if (hour >= 10) {
            buffer.append(hour + ":");
        } else {
            buffer.append("0" + hour + ":");
        }

        buffer.append(covertSecondToMS(secondStr));
        return buffer.toString();
    }

    /**
     * 秒数转换 00：00显示
     *
     * @param secondStr
     * @return
     */
    public static String covertSecondToMS(String secondStr) {
        int time = Integer.valueOf(secondStr);
        int minute = time / 60 % 60;
        int second = time % 60;

        StringBuffer buffer = new StringBuffer();

        if (minute >= 10) {
            buffer.append(minute + ":");
        } else {
            buffer.append("0" + minute + ":");
        }

        if (second >= 10) {
            buffer.append(second);
        } else {
            buffer.append("0" + second);
        }

        return buffer.toString();
    }
}
