package com.laka.live.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/8/23.
 */
public class NumberUtils {

    private static final String TAG = "NumberUtils";

    /**
     * 保留n位
     * 如果小数点为0，那就不显示小数点
     */
    public static Double splitDouble(double number, int n) {

        if (number == 0)
            return number;

//        if(n==0){
//            return (float) ((int)number);
//        }

        StringBuilder sb = new StringBuilder();
        if (n > 0) {
            sb.append("######.");
            for (int i = 0; i < n; i++) {
                sb.append("#");
            }
        } else {
            sb.append("######");
        }

//        DecimalFormat df = new DecimalFormat("######.#");
        DecimalFormat df = new DecimalFormat(sb.toString());
        String str = df.format(number);
//        Log.d(TAG," DecimalFormat="+sb.toString()+" str="+str+" number="+number);
        return Double.valueOf(str);

    }

    /**
     * 保留一位小数点
     * 如果小数点为0，那就不显示小数点,值是四舍五入
     */
    public static float splitOnePoint(float number) {

        if (number == 0)
            return number;

        DecimalFormat df = new DecimalFormat("######.#");
        String str = df.format(number);

        return Float.valueOf(str);

    }

    /**
     * 保留两位小数点
     * 如果第二位小数点为0，那就不显示第二位小数点,值是四舍五入
     */
    public static float splitTwoPoint(float number) {

        if (number == 0)
            return number;

        DecimalFormat df = new DecimalFormat("######.##");
        String str = df.format(number);

        return Float.valueOf(str);

    }

    /**
     * @return 将number转换成带1位小数的字符串, 值是四舍五入(3.02会变成3, 3.05会变成3.1)
     */
    public static String splitDoubleStr(float number) {
        DecimalFormat df = new DecimalFormat("######.##");
        return df.format(number);
    }

    /**
     * @return 将number转换成带1位小数的字符串, 值是向上取整(3.02会变成3.1)
     * 这里有个细节，如果传进来的值是float型，那么0.x0 == 0.x+1(比如0.30会变成0.4)
     * 所以，传进来的值，必须是double。不能传个float进来。
     */
    public static String splitAndUpDoubleStr(double number) {
        DecimalFormat df = new DecimalFormat("######.#");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(number);
    }

    /**
     * @return 将number转换成整数, 值是向上取整(3.02会变成4)
     */
    public static String splitAndUIntStr(double number) {
        DecimalFormat df = new DecimalFormat("######");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(number);
    }

    /**
     * @return 将number转换成带1位小数的字符串, 值是四舍五入
     */
    public static String splitDoubleStr(String startWiths, float number) {
        DecimalFormat df = new DecimalFormat("######.#");
        return startWiths + df.format(number);
    }

    /**
     * @return 将number转换成带1位小数的字符串, 值是四舍五入
     */
    public static String splitDoubleStr(float number, String endWiths) {
        DecimalFormat df = new DecimalFormat("######.#");
        return df.format(number) + " " + endWiths;
    }

    /**
     * 课程价格的显示格式
     *
     * @return 将number转换成带1位小数的字符串
     */
    public static String getCoursePriceFormat(float number) {
        if (number == 0)
            return "免费";
        DecimalFormat df = new DecimalFormat("######.#");
        return df.format(number);
    }

    /**
     * 课程价格的显示格式
     *
     * @return 将number转换成带1位小数的字符串
     */
    public static String getCoursePriceFormat(float number, String endWiths) {
        if (number == 0)
            return "免费";
        DecimalFormat df = new DecimalFormat("######.#");
        return df.format(number) + " " + endWiths;
    }

    /**
     * 根据传入的数据按照万分转换
     *
     * @param countStr
     * @return
     */
    public static String getVideoSummeryCount(String countStr) {
        String result;
        int count = Integer.valueOf(countStr);
        if (count < 10000) {
            result = countStr;
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(".0");
            result = decimalFormat.format(count / 10000) + "万";
        }
        return result;
    }
}
