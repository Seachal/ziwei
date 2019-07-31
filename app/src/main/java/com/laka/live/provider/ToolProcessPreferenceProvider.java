package com.laka.live.provider;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;

import com.laka.live.application.LiveApplication;
import com.laka.live.util.Log;
import com.laka.live.util.RuntimeCheck;
import com.laka.live.util.ToolProcessPreference;

/**
 * Created by luwies on 16/3/4.
 */
public class ToolProcessPreferenceProvider extends ContentProvider {
    public static final Uri CONFIG_CONTENT_URI = Uri.parse("content://com.laka.live.provider.tool");

    private static final int LENGTH_CONTENT_URI = CONFIG_CONTENT_URI.toString().length() + 1;

    @Override
    public boolean onCreate() {
        RuntimeCheck.setToolProcess();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    private static String EXTRA_TYPE = "type";
    private static String EXTRA_KEY = "key";
    private static String EXTRA_VALUE = "value";

    private static final int TYPE_BOOLEAN = 1;
    private static final int TYPE_INT = 2;
    private static final int TYPE_LONG = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_FLOAT = 5;


    private static ContentResolver getCr() {
        return LiveApplication.getInstance().getContentResolver();
    }


    private static boolean s_bFixedSysBug = false;
    private static Object s_LockFixedBug = new Object();
    private static ContentProviderClient s_cpClientFixer = null;

    private static void FixProviderSystemBug() {
        synchronized (s_LockFixedBug) {
            if (s_bFixedSysBug)
                return;
            s_bFixedSysBug = true;

            if ((Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 18)
                    || (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 10)) {
                s_cpClientFixer = getCr().acquireContentProviderClient(CONFIG_CONTENT_URI);
            }
        }
    }

    public static void setBooleanValue(String key, boolean value) {
        ContentValues contetvalues = new ContentValues();

        contetvalues.put(EXTRA_TYPE, TYPE_BOOLEAN);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        getCr().update(CONFIG_CONTENT_URI, contetvalues, null, null);
    }

    public static void setLongValue(String key, long value) {
        ContentValues contetvalues = new ContentValues();

        contetvalues.put(EXTRA_TYPE, TYPE_LONG);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        getCr().update(CONFIG_CONTENT_URI, contetvalues, null, null);
    }

    public static void setIntValue(String key, int value) {
        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_INT);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        getCr().update(CONFIG_CONTENT_URI, contetvalues, null, null);
    }

    public static void setStringValue(String key, String value) {
        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_STRING);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        getCr().update(CONFIG_CONTENT_URI, contetvalues, null, null);
    }

    public static void setFloatValue(String key, float value) {
        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_STRING);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);

        FixProviderSystemBug();
        getCr().update(CONFIG_CONTENT_URI, contetvalues, null, null);
    }

    public static long getLongValue(String key, long defValue) {
        FixProviderSystemBug();

        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_LONG);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getCr().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            //fix crash http://trace.cm.ijinshan.com/index/lists?thever=0&field=dumpkey&field_content=4198996165&date=20140708&version=&=
            return defValue;
        }

        if (result == null)
            return defValue;

        return Long.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    public static boolean getBooleanValue(String key, boolean defValue) {
        FixProviderSystemBug();

        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_BOOLEAN);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getCr().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            //fix crash http://trace.cm.ijinshan.com/index/lists?thever=0&field=dumpkey&field_content=4198996165&date=20140708&version=&=
            return defValue;
        }

        if (result == null)
            return defValue;

        return Boolean.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    public static int getIntValue(String key, int defValue) {
        FixProviderSystemBug();

        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_INT);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getCr().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            //fix crash http://trace.cm.ijinshan.com/index/lists?thever=0&field=dumpkey&field_content=4198996165&date=20140708&version=&=
            return defValue;
        }

        if (result == null)
            return defValue;

        return Integer.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    public static String getStringValue(String key, String defValue) {
        FixProviderSystemBug();

        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_STRING);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getCr().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            //fix crash http://trace.cm.ijinshan.com/index/lists?thever=0&field=dumpkey&field_content=4198996165&date=20140708&version=&=
            return defValue;
        }

        if (result == null)
            return defValue;

        return String.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    public static float getFloatValue(String key, float defValue) {
        FixProviderSystemBug();

        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_FLOAT);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, defValue);
        Uri result = null;

        try {
            result = getCr().insert(CONFIG_CONTENT_URI, contetvalues);
        } catch (IllegalArgumentException e) {
            //fix crash http://trace.cm.ijinshan.com/index/lists?thever=0&field=dumpkey&field_content=4198996165&date=20140708&version=&=
            return defValue;
        }

        if (result == null)
            return defValue;

        return Float.valueOf(result.toString().substring(LENGTH_CONTENT_URI));
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.error("ConfigProvider read", values.toString());
        RuntimeCheck.checkToolProcess();

        String res = "";
        int nType = values.getAsInteger(EXTRA_TYPE);
        if (nType == TYPE_BOOLEAN) {
            res += ToolProcessPreference.getBoolean(
                    values.getAsString(EXTRA_KEY),
                    values.getAsBoolean(EXTRA_VALUE));
        } else if (nType == TYPE_STRING) {
            res += ToolProcessPreference.getString(
                    values.getAsString(EXTRA_KEY),
                    values.getAsString(EXTRA_VALUE));
        } else if (nType == TYPE_INT) {
            res += ToolProcessPreference.getInt(
                    values.getAsString(EXTRA_KEY),
                    values.getAsInteger(EXTRA_VALUE));
        } else if (nType == TYPE_LONG) {
            res += ToolProcessPreference.getLong(
                    values.getAsString(EXTRA_KEY),
                    values.getAsLong(EXTRA_VALUE));
        } else if (nType == TYPE_FLOAT) {
            res += ToolProcessPreference.getFloat(
                    values.getAsString(EXTRA_KEY),
                    values.getAsFloat(EXTRA_VALUE));
        }

        return Uri.parse(CONFIG_CONTENT_URI.toString() + "/" + res);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        Log.error("ConfigProvider write", values.toString());

        RuntimeCheck.checkToolProcess();

        int nType = values.getAsInteger(EXTRA_TYPE);
        if (nType == TYPE_BOOLEAN) {
            ToolProcessPreference.putBoolean(
                    values.getAsString(EXTRA_KEY),
                    values.getAsBoolean(EXTRA_VALUE));
        } else if (nType == TYPE_STRING) {
            ToolProcessPreference.putString(
                    values.getAsString(EXTRA_KEY),
                    values.getAsString(EXTRA_VALUE));
        } else if (nType == TYPE_INT) {
            ToolProcessPreference.putInt(
                    values.getAsString(EXTRA_KEY),
                    values.getAsInteger(EXTRA_VALUE));
        } else if (nType == TYPE_LONG) {
            ToolProcessPreference.putLong(
                    values.getAsString(EXTRA_KEY),
                    values.getAsLong(EXTRA_VALUE));
        } else if (nType == TYPE_FLOAT) {
            ToolProcessPreference.putFloat(
                    values.getAsString(EXTRA_KEY),
                    values.getAsFloat(EXTRA_VALUE));
        }
        return 1;
    }
}
