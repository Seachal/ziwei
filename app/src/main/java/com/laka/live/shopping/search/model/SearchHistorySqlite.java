package com.laka.live.shopping.search.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by linhz on 2016/4/21.
 * Email: linhaizhong@ta2she.com
 */
/*package*/ class SearchHistorySqlite extends SQLiteOpenHelper {
    public static final String DEFAULT_DATABASE_NAME = "search_history";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_HISTORY = "history";
    public static final String ID = "_id";
    public static final String TEXT = "text";
    public static final String TIME = "time";

    public SearchHistorySqlite(Context context) {
        super(context, DEFAULT_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSelectedDatabase(db);
    }

    private void createSelectedDatabase(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE " + TABLE_HISTORY);
        builder.append("(");
        builder.append(ID + " INTEGER PRIMARY KEY,");
        builder.append(TEXT + " TEXT,");
        builder.append(TIME + " INTEGER");
        builder.append(");");

        db.execSQL(builder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}