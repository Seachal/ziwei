package com.laka.live.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by luwies on 16/3/4.
 */
public class UiPreference {

    private static Context mContext = null;
    private static SharedPreferences mSharedPreferences = null;

    public static void init(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void putLong(String keyString, long object) {
        mSharedPreferences.edit().putLong(keyString, object).commit();
    }

    public static void putBoolean(String keyString, boolean object) {
        mSharedPreferences.edit().putBoolean(keyString, object).commit();
    }

    public static void putInt(String keyString, int object) {
        mSharedPreferences.edit().putInt(keyString, object).commit();
    }

    public static void putString(String keyString, String object) {
        mSharedPreferences.edit().putString(keyString, object).commit();
    }

    public static void putFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).commit();
    }

    public static String getString(String keyString, String def) {
        return mSharedPreferences.getString(keyString, def);
    }

    public static int getInt(String keyString, int def) {
        return mSharedPreferences.getInt(keyString, def);
    }

    public static long getLong(String keyString, long def) {
        return mSharedPreferences.getLong(keyString, def);
    }

    public static boolean getBoolean(String keyString, boolean def) {
        return mSharedPreferences.getBoolean(keyString, def);
    }

    public static float getFloat(String keyString, float def) {
        return mSharedPreferences.getFloat(keyString, def);
    }

    public static void saveObject(String key, Serializable object) {
        if (object == null) {
            putString(key, "");
            return;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

            String personBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            putString(key, personBase64);
        } catch (IOException e) {
        }
    }

    public static Object getObject(String key) {
        String str = getString(key, "");
        Object object = null;
        if (TextUtils.isEmpty(str) == false) {
            byte[] base64Bytes = Base64.decode(str, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    base64Bytes);
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(bais);
                object = ois.readObject();
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            }
        }
        return object;
    }
}
