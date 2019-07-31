package com.laka.live.help;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by ios on 16/9/2.
 */
public class ChineseFilter implements InputFilter {
    int length;
    int maxLen;
    public ChineseFilter(int maxLen) {
        this.maxLen = maxLen;
    }
    public int getLength(){
        return length;
    }

    public int getLength(String src){
        int count = 0;
        int dindex = 0;
        while (count <= maxLen && dindex < src.length()) {
            char c = src.charAt(dindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }
        return count/2;
    }

    @Override
    public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
        int dindex = 0;
        int count = 0;

        while (count <= maxLen && dindex < dest.length()) {
            char c = dest.charAt(dindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > maxLen) {
            return dest.subSequence(0, dindex - 1);
        }

        int sindex = 0;
        while (count <= maxLen && sindex < src.length()) {
            char c = src.charAt(sindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }
        if (count > maxLen) {
            sindex--;
        }
        length = count/2;
        return src.subSequence(0, sindex);
    }
}
