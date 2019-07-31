package com.laka.live.ui.search;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laka.live.util.Log;
import com.laka.live.util.UiPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwies on 16/3/23.
 */
public class SearchHisKeyManager {

    private final static int MAX = 10;

    private final static String SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY";
    private List<String> mHistoryKeys;


    public SearchHisKeyManager() {
        mHistoryKeys = readFromFile();
        if (mHistoryKeys == null) {
            mHistoryKeys = new ArrayList();
        }
    }

    public void saveKey(String key) {
        if (TextUtils.isEmpty(key) == false) {
            mHistoryKeys.remove(key);
            mHistoryKeys.add(0, key);
            int size = mHistoryKeys.size();
            if (size > MAX) {
                mHistoryKeys.remove(size - 1);
            }
        }
    }

    public int indexOf(String keyword) {
        return mHistoryKeys.indexOf(keyword);
    }

    public void remove(String key) {
        mHistoryKeys.remove(key);
    }

    public int size() {
        return mHistoryKeys.size();
    }

    public List<String> getAllKeys() {
        return mHistoryKeys;
    }

    public void clear() {
        mHistoryKeys.clear();
    }

    public void saveToFile() {

        Gson gson = new Gson();
        String jsonStr = gson.toJson(mHistoryKeys);
        Log.debug("test", "saveToFile : " + jsonStr);
        UiPreference.putString(SEARCH_HISTORY_KEY, jsonStr);
    }

    public List<String> readFromFile() {
        String jsonStr = UiPreference.getString(SEARCH_HISTORY_KEY, "");
        Log.debug("test", "readFromFile : " + jsonStr);
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        List<String> list = null;
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonStr, List.class);
        } catch (JsonSyntaxException e) {
            Log.error("test", "ex ", e);
        }

        return list;
    }
}
