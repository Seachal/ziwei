package com.laka.live.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.laka.live.application.LiveApplication;
import com.laka.live.util.Log;

import org.greenrobot.greendao.database.Database;
import laka.live.dao.ChatMsgDao;
import laka.live.dao.ChatSessionDao;
import laka.live.dao.DaoMaster;
import laka.live.dao.MusicInfoDao;

/**
 * Created by ios on 16/8/12.
 */
public class MyOpenHelper extends DaoMaster.OpenHelper{
    private static final String TAG ="MyOpenHelper";

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d(TAG, "Upgrading schema from version " + oldVersion + " to " + newVersion + " by change tables");
        MigrationHelper.migrate(db, ChatMsgDao.class, ChatSessionDao.class, MusicInfoDao.class);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //空实现
        Log.d(TAG,"onDowngrade oldVersion="+oldVersion+" newVersion="+newVersion);
    }
}
