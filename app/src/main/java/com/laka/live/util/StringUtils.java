/**
 * Created by linhz on 2016/06/04.
 */

package com.laka.live.util;

import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

    public static boolean isEmpty(String aText) {
        if (aText == null) {
            return true;
        } else if (aText.length() < 50) {
            return aText.trim().length() == 0;
        } else {
            return aText.length() == 0;
        }
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    public static int parseInt(String str, int defValue) {
        int result = defValue;
        if (str == null) {
            return result;
        }
        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            result = defValue;
        }

        return result;
    }

    public static long parseLong(String str) {
        return parseLong(str, 0);
    }

    public static long parseLong(String str, long defValue) {
        long result = defValue;
        if (str == null) {
            return result;
        }
        try {
            result = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
            result = defValue;
        }

        return result;
    }

    public static float parseFloat(String str) {
        return parseFloat(str, 0);
    }

    public static float parseFloat(String str, float defValue) {
        float result = defValue;
        if (str == null) {
            return result;
        }
        try {
            result = Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            result = defValue;
        }

        return result;
    }

    public static boolean parseBoolean(String value) {
        return parseBoolean(value, false);
    }

    public static boolean parseBoolean(String value, boolean aDefault) {
        if (value == null) {
            return aDefault;
        }
        if ("1".equals(value) || "true".equalsIgnoreCase(value)) {
            return true;
        }

        return false;
    }

    public static String[] split(String original, String regex) {
        return split(original, regex, true);
    }

    public static String[] split(String aOriginal, String aRegex, boolean aCanNull) {
        if (isEmpty(aOriginal)) {
            return new String[0];
        }

        if (aRegex == null || aRegex.length() == 0) {
            return new String[]{aOriginal};
        }

        String[] sTarget = null;
        int sTargetLength = 0;
        int sLength = aOriginal.length();
        int sStartIndex = 0;
        int sEndIndex = 0;

        //扫描字符串，确定目标字符串数组的长度
        for (sEndIndex = aOriginal.indexOf(aRegex, 0); sEndIndex != -1 && sEndIndex < sLength;
             sEndIndex = aOriginal.indexOf(aRegex, sEndIndex)) {
            sTargetLength += (aCanNull || sStartIndex != sEndIndex) ? 1 : 0;
            sStartIndex = sEndIndex += sEndIndex >= 0 ? aRegex.length() : 0;
        }

        //如果最后一个标记的位置非字符串的结尾，则需要处理结束串
        sTargetLength += aCanNull || sStartIndex != sLength ? 1 : 0;

        //重置变量值，根据标记拆分字符串
        sTarget = new String[sTargetLength];
        int sIndex = 0;
        for (sIndex = 0, sEndIndex = aOriginal.indexOf(aRegex, 0), sStartIndex = 0;
             sEndIndex != -1 && sEndIndex < sLength;
             sEndIndex = aOriginal.indexOf(aRegex, sEndIndex)) {
            if (aCanNull || sStartIndex != sEndIndex) {
                sTarget[sIndex] = aOriginal.substring(sStartIndex, sEndIndex);
                ++sIndex;
            }
            sStartIndex = sEndIndex += sEndIndex >= 0 ? aRegex.length() : 0;
        }

        //取结束的子串
        if (aCanNull || sStartIndex != sLength) {
            sTarget[sTargetLength - 1] = aOriginal.substring(sStartIndex);
        }

        return sTarget;
    }


    /**
     * Compare two version strings. the version string's format is like
     * "2.1.0.107".
     *
     * @param left
     * @param right
     * @return Return 0 if left and right are equal. Return a positive number if
     * left is higher than right. Return a negative number if left is
     * lower than right.
     */
    public static int compareVersion(String left, String right) {
        if ((left == null && right == null) || (isEmpty(left) && isEmpty(right))) {
            return 0;
        }

        if (right == null || isEmpty(right)) {
            return 1;
        }

        if (left == null || isEmpty(left)) {
            return -1;
        }

        String leftArray[] = split(left, ".");
        String rightArray[] = split(right, ".");

        int compareStep = Math.min(leftArray.length, rightArray.length);
        int leftInt;
        int rightInt;
        for (int i = 0; i < compareStep; i++) {
            leftInt = parseInt(leftArray[i]);
            rightInt = parseInt(rightArray[i]);
            if (leftInt == rightInt) {
                continue;
            }

            return (leftInt - rightInt);
        }

        // their prefix are the same,so the the longer one is higher.
        int compareResult = leftArray.length - rightArray.length;
        return compareResult;
    }

    public static String deleteAll(String str1, String Str2) {
        if (str1 == null)
            return "";
        return str1.replaceAll(Str2, "");
    }

    /**
     * 限制输入框的最大值和小数点的位数
     * @param editText 输入框
     * @param max 输入框的最大数额
     * 小数点的位数默认是1位
     */
    public static boolean limitPoint(EditText editText,
                                     String before, double max) {

        int point = 1; // 限制输入的小数点后的位数

        String text = editText.getText().toString().trim();

        if (text.length() > 0) {

            try {
                if (Double.valueOf(text) > max) {
                    editText.setText(before);
                    editText.setSelection(before.length());
                    return true;
                } else {

                    if (text.contains(".")) {
                        if (text.length() - 1 - text.indexOf(".") > point) {
                            text = text.subSequence(0, text.indexOf(".") + (point + 1)).toString();
                            editText.setText(text);
                            editText.setSelection(text.length());
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        return false;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     * @author lzf
     */
    private static String truncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;
        strURL = strURL.trim();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                for (int i = 1; i < arrSplit.length; i++) {
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }


    /**
     * 截取Url中的所有参数
     * @param URL
     * @return
     */
    public static Map<String, String> splitUrl(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = truncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }


}
