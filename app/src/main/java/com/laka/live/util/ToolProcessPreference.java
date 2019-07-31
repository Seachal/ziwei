package com.laka.live.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.laka.live.provider.ToolProcessPreferenceProvider;

/**
 * Created by luwies on 16/3/4.
 */
public class ToolProcessPreference {
    private static Context sContext = null;
    private static SharedPreferences sSharedPreferences = null;

    public static void init(Context context) {
        sContext = context;
        sSharedPreferences = sContext.getSharedPreferences(sContext.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void putLong(String keyString, long object) {
        if(RuntimeCheck.isToolProcess()) {
            sSharedPreferences.edit().putLong(keyString, object).commit();
        } else {
            ToolProcessPreferenceProvider.setLongValue(keyString, object);
        }
    }

    public static void putBoolean(String keyString, boolean object) {
        if(RuntimeCheck.isToolProcess()) {
            sSharedPreferences.edit().putBoolean(keyString, object).commit();
        } else {
            ToolProcessPreferenceProvider.setBooleanValue(keyString, object);
        }
    }

    public static void putInt(String keyString, int object) {
        if(RuntimeCheck.isToolProcess()) {
            sSharedPreferences.edit().putInt(keyString, object).commit();
        } else {
            ToolProcessPreferenceProvider.setIntValue(keyString, object);
        }
    }

    public static void putString(String keyString, String object) {
        if(RuntimeCheck.isToolProcess()) {
            sSharedPreferences.edit().putString(keyString, object).commit();
        } else {
            ToolProcessPreferenceProvider.setStringValue(keyString, object);
        }
    }

    public static void putFloat(String key, float value) {
        if(RuntimeCheck.isToolProcess()) {
            sSharedPreferences.edit().putFloat(key, value).commit();
        } else {
            ToolProcessPreferenceProvider.setFloatValue(key, value);
        }
    }

    public static String getString(String keyString, String def) {
        if(RuntimeCheck.isToolProcess()) {
            return sSharedPreferences.getString(keyString, def);
        } else {
            return ToolProcessPreferenceProvider.getStringValue(keyString, def);
        }
    }

    public static int getInt(String keyString, int def) {
        if(RuntimeCheck.isToolProcess()) {
            return sSharedPreferences.getInt(keyString, def);
        } else {
            return ToolProcessPreferenceProvider.getIntValue(keyString, def);
        }
    }

    public static long getLong(String keyString, long def) {
        if(RuntimeCheck.isToolProcess()) {
            return sSharedPreferences.getLong(keyString, def);
        } else {
            return ToolProcessPreferenceProvider.getLongValue(keyString, def);
        }
    }

    public static boolean getBoolean(String keyString, boolean def) {
        if(RuntimeCheck.isToolProcess()) {
            return sSharedPreferences.getBoolean(keyString, def);
        } else {
            return ToolProcessPreferenceProvider.getBooleanValue(keyString, def);
        }
    }

    public static float getFloat(String keyString, float def) {
        if(RuntimeCheck.isToolProcess()) {
            return sSharedPreferences.getFloat(keyString, def);
        } else {
            return ToolProcessPreferenceProvider.getFloatValue(keyString, def);
        }
    }
}
