

package com.laka.live.shopping.search.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;


import com.laka.live.util.StringUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by linhz on 2016/04/21.
 * Email: linhaizhong@ta2she.com
 */

public class SearchHistoryManager {
    private static final String TAG = "SearchHistorySqliteManager";
    //the max number for query all history info
    private static final int MAX_NUM_DISPLAY = 40;
    //the max number for trigger del old history info
    private static final int MAX_NUM_IN_SQL = 80;

    /* package */static final int MSG_GET_ALL_HISTORY = 100;
    /* package */static final int MSG_ADD_HISTORY = 101;
    /* package */static final int MSG_CLEAR_HISTORY = 102;

    private static final String[] PROJECTIONS = new String[]{SearchHistorySqlite.ID, SearchHistorySqlite.TEXT, SearchHistorySqlite.TIME};
    private static final String ORDER_METHOD = SearchHistorySqlite.TIME + " DESC";

    private Context mContext;
    private SearchHistorySqlite mSqliteHelper;
    private SQLiteDatabase mDatabase;

    private HandlerThread mHandlerThread;
    private InnerHandler mHandler;

    public SearchHistoryManager(Context context) {
        mContext = context;
        mHandlerThread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new InnerHandler(this, mHandlerThread.getLooper());

        ensureSqlite();
    }

    private void ensureSqlite() {
        if (mSqliteHelper == null || mDatabase == null) {
            try {
                mSqliteHelper = new SearchHistorySqlite(mContext);
                mDatabase = mSqliteHelper.getWritableDatabase();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void clearHistory() {
        clearHistory(null);
    }

    public void clearHistory(SearchHistoryCallback callback) {
        SearchHistoryOptResult optResult = new SearchHistoryOptResult();
        optResult.callback = callback;
        Message msg = Message.obtain();
        msg.what = MSG_CLEAR_HISTORY;
        msg.obj = optResult;
        mHandler.sendMessage(msg);
    }

    private void doClearHistory(Message msg) {
        if (!(msg.obj instanceof SearchHistoryOptResult)) {
            return;
        }
        SearchHistoryOptResult optResult = (SearchHistoryOptResult) msg.obj;
        if (mDatabase == null) {
            optResult.success = false;
            optResult.sendResult(msg);
            return;
        }
        try {
            String sql = "DELETE FROM " + SearchHistorySqlite.TABLE_HISTORY + ";";
            mDatabase.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            optResult.success = false;
        }
        optResult.sendResult(msg);
    }

    public void getAllHistoryInfo(SearchHistoryCallback callback) {
        if (callback == null) {
            return;
        }
        SearchHistoryOptResult optResult = new SearchHistoryOptResult();
        optResult.callback = callback;
        Message msg = Message.obtain();
        msg.what = MSG_GET_ALL_HISTORY;
        msg.obj = optResult;
        mHandler.sendMessage(msg);
    }

    private void doGetAllHistoryInfo(Message msg) {
        if (!(msg.obj instanceof SearchHistoryOptResult)) {
            return;
        }
        SearchHistoryOptResult optResult = (SearchHistoryOptResult) msg.obj;
        if (mDatabase == null) {
            optResult.success = false;
            optResult.sendResult(msg);
            return;
        }
        optResult.extras = queryAllHistoryInfo(MAX_NUM_DISPLAY);
        optResult.sendResult(msg);
    }

    public void addSearchHistory(String text) {
        addSearchHistory(text, null);
    }

    public void addSearchHistory(String text, SearchHistoryCallback callback) {
        SearchHistoryOptResult optResult = new SearchHistoryOptResult();
        optResult.callback = callback;
        optResult.extras = text;
        Message msg = Message.obtain();
        msg.what = MSG_ADD_HISTORY;
        msg.obj = optResult;
        mHandler.sendMessage(msg);
    }

    public void doAddSearchHistory(Message msg) {
        if (!(msg.obj instanceof SearchHistoryOptResult)) {
            return;
        }
        SearchHistoryOptResult optResult = (SearchHistoryOptResult) msg.obj;
        if (mDatabase == null) {
            optResult.success = false;
            optResult.sendResult(msg);
            return;
        }

        String text = (String) optResult.extras;
        if (StringUtils.isEmpty(text)) {
            optResult.success = false;
            optResult.sendResult(msg);
            return;
        }
        Cursor cursor = null;
        try {
            cursor = mDatabase.query(SearchHistorySqlite.TABLE_HISTORY, PROJECTIONS, SearchHistorySqlite.TEXT + "=?",
                    new String[]{text}, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                //update history
                int id = cursor.getInt(cursor.getColumnIndex(SearchHistorySqlite.ID));
                try {
                    ContentValues contentValues = createContentValue(text);
                    mDatabase.update(SearchHistorySqlite.TABLE_HISTORY, contentValues, SearchHistorySqlite.ID + "="
                            + id, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    optResult.success = false;
                }
            } else {
                // add new history
                try {
                    ContentValues contentValues = createContentValue(text);
                    mDatabase.insert(SearchHistorySqlite.TABLE_HISTORY, null, contentValues);
                } catch (Exception e) {
                    e.printStackTrace();
                    optResult.success = false;
                }

                checkDelOldHistoryInfo();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        optResult.sendResult(msg);
    }

    private ArrayList<SearchHistoryInfo> queryAllHistoryInfo(int maxNum) {
        ArrayList<SearchHistoryInfo> list = new ArrayList<>();
        if (mDatabase == null) {
            return list;
        }
        Cursor cursor = null;
        try {
            String limit = null;
            if (maxNum > 0) {
                limit = String.valueOf(maxNum);
            }
            cursor = mDatabase.query(SearchHistorySqlite.TABLE_HISTORY, PROJECTIONS,
                    null, null, null, null, ORDER_METHOD, limit);
            if (cursor == null) {
                return list;
            }
            SearchHistoryInfo historyInfo;
            while (cursor.moveToNext()) {
                historyInfo = new SearchHistoryInfo();
                historyInfo.text = cursor.getString(cursor.getColumnIndex(SearchHistorySqlite.TEXT));
                historyInfo.id = cursor.getInt(cursor.getColumnIndex(SearchHistorySqlite.ID));
                historyInfo.time = cursor.getLong(cursor.getColumnIndex(SearchHistorySqlite.TIME));
                list.add(historyInfo);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private void checkDelOldHistoryInfo() {
        if (mDatabase == null) {
            return;
        }
        ArrayList<SearchHistoryInfo> list = queryAllHistoryInfo(-1);
        if (list.size() >= MAX_NUM_IN_SQL) {
            long oldItemItem = list.get(MAX_NUM_DISPLAY).time;
            try {
                mDatabase.delete(SearchHistorySqlite.TABLE_HISTORY, SearchHistorySqlite.TIME + "<" + oldItemItem, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        try {
            if (mDatabase != null) {
                mDatabase.close();
            }
            mDatabase = null;
            if (mSqliteHelper != null) {
                mSqliteHelper.close();
            }
            mSqliteHelper = null;

        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            mHandlerThread.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ContentValues createContentValue(String text) {
        ContentValues values = new ContentValues();
        values.put(SearchHistorySqlite.TEXT, text);
        values.put(SearchHistorySqlite.TIME, System.currentTimeMillis());
        return values;
    }


    private static class InnerHandler extends Handler {
        private SoftReference<SearchHistoryManager> mManager;

        InnerHandler(SearchHistoryManager manager, Looper looper) {
            super(looper);
            mManager = new SoftReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg) {

            SearchHistoryManager manager = mManager.get();
            if (manager == null) {
                return;
            }

            switch (msg.what) {
                case MSG_ADD_HISTORY:
                    manager.doAddSearchHistory(msg);
                    break;
                case MSG_GET_ALL_HISTORY:
                    manager.doGetAllHistoryInfo(msg);
                    break;
                case MSG_CLEAR_HISTORY:
                    manager.doClearHistory(msg);
                    break;
            }
        }
    }

}
