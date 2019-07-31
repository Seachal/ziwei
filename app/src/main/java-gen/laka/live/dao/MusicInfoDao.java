package laka.live.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import laka.live.bean.MusicInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MusicInfo".
*/
public class MusicInfoDao extends AbstractDao<MusicInfo, Long> {

    public static final String TABLENAME = "MusicInfo";

    /**
     * Properties of entity MusicInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Singer = new Property(2, String.class, "singer", false, "SINGER");
        public final static Property Url = new Property(3, String.class, "url", false, "URL");
        public final static Property Code = new Property(4, String.class, "code", false, "CODE");
        public final static Property TotalBytes = new Property(5, Long.class, "totalBytes", false, "TOTAL_BYTES");
        public final static Property BytesWritten = new Property(6, Long.class, "bytesWritten", false, "BYTES_WRITTEN");
        public final static Property MusicFilePath = new Property(7, String.class, "musicFilePath", false, "MUSIC_FILE_PATH");
        public final static Property LyricsFilePath = new Property(8, String.class, "lyricsFilePath", false, "LYRICS_FILE_PATH");
        public final static Property DownloadState = new Property(9, Integer.class, "downloadState", false, "DOWNLOAD_STATE");
        public final static Property LastPlayTime = new Property(10, Long.class, "lastPlayTime", false, "LAST_PLAY_TIME");
    }

    private DaoSession daoSession;


    public MusicInfoDao(DaoConfig config) {
        super(config);
    }
    
    public MusicInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MusicInfo\" (" + //
                "\"id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"SINGER\" TEXT," + // 2: singer
                "\"URL\" TEXT," + // 3: url
                "\"CODE\" TEXT," + // 4: code
                "\"TOTAL_BYTES\" INTEGER," + // 5: totalBytes
                "\"BYTES_WRITTEN\" INTEGER," + // 6: bytesWritten
                "\"MUSIC_FILE_PATH\" TEXT," + // 7: musicFilePath
                "\"LYRICS_FILE_PATH\" TEXT," + // 8: lyricsFilePath
                "\"DOWNLOAD_STATE\" INTEGER," + // 9: downloadState
                "\"LAST_PLAY_TIME\" INTEGER);"); // 10: lastPlayTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MusicInfo\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MusicInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String singer = entity.getSinger();
        if (singer != null) {
            stmt.bindString(3, singer);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(4, url);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(5, code);
        }
 
        Long totalBytes = entity.getTotalBytes();
        if (totalBytes != null) {
            stmt.bindLong(6, totalBytes);
        }
 
        Long bytesWritten = entity.getBytesWritten();
        if (bytesWritten != null) {
            stmt.bindLong(7, bytesWritten);
        }
 
        String musicFilePath = entity.getMusicFilePath();
        if (musicFilePath != null) {
            stmt.bindString(8, musicFilePath);
        }
 
        String lyricsFilePath = entity.getLyricsFilePath();
        if (lyricsFilePath != null) {
            stmt.bindString(9, lyricsFilePath);
        }
 
        Integer downloadState = entity.getDownloadState();
        if (downloadState != null) {
            stmt.bindLong(10, downloadState);
        }
 
        Long lastPlayTime = entity.getLastPlayTime();
        if (lastPlayTime != null) {
            stmt.bindLong(11, lastPlayTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MusicInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String singer = entity.getSinger();
        if (singer != null) {
            stmt.bindString(3, singer);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(4, url);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(5, code);
        }
 
        Long totalBytes = entity.getTotalBytes();
        if (totalBytes != null) {
            stmt.bindLong(6, totalBytes);
        }
 
        Long bytesWritten = entity.getBytesWritten();
        if (bytesWritten != null) {
            stmt.bindLong(7, bytesWritten);
        }
 
        String musicFilePath = entity.getMusicFilePath();
        if (musicFilePath != null) {
            stmt.bindString(8, musicFilePath);
        }
 
        String lyricsFilePath = entity.getLyricsFilePath();
        if (lyricsFilePath != null) {
            stmt.bindString(9, lyricsFilePath);
        }
 
        Integer downloadState = entity.getDownloadState();
        if (downloadState != null) {
            stmt.bindLong(10, downloadState);
        }
 
        Long lastPlayTime = entity.getLastPlayTime();
        if (lastPlayTime != null) {
            stmt.bindLong(11, lastPlayTime);
        }
    }

    @Override
    protected final void attachEntity(MusicInfo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MusicInfo readEntity(Cursor cursor, int offset) {
        MusicInfo entity = new MusicInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // singer
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // url
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // code
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // totalBytes
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // bytesWritten
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // musicFilePath
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // lyricsFilePath
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // downloadState
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10) // lastPlayTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MusicInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSinger(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTotalBytes(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setBytesWritten(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setMusicFilePath(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLyricsFilePath(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDownloadState(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setLastPlayTime(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MusicInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MusicInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MusicInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
