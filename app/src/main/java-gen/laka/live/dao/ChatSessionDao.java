package laka.live.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import laka.live.bean.ChatSession;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHAT_SESSION".
*/
public class ChatSessionDao extends AbstractDao<ChatSession, String> {

    public static final String TABLENAME = "CHAT_SESSION";

    /**
     * Properties of entity ChatSession.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property NickName = new Property(2, String.class, "nickName", false, "NICK_NAME");
        public final static Property Avatar = new Property(3, String.class, "avatar", false, "AVATAR");
        public final static Property Level = new Property(4, Integer.class, "level", false, "LEVEL");
        public final static Property Gender = new Property(5, Integer.class, "gender", false, "GENDER");
        public final static Property Auth = new Property(6, Short.class, "auth", false, "AUTH");
        public final static Property Content = new Property(7, String.class, "content", false, "CONTENT");
        public final static Property Date = new Property(8, Long.class, "date", false, "DATE");
        public final static Property UnreadCnt = new Property(9, Integer.class, "unreadCnt", false, "UNREAD_CNT");
        public final static Property Type = new Property(10, Integer.class, "type", false, "TYPE");
    }

    private DaoSession daoSession;


    public ChatSessionDao(DaoConfig config) {
        super(config);
    }
    
    public ChatSessionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAT_SESSION\" (" + //
                "\"id\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: userId
                "\"NICK_NAME\" TEXT," + // 2: nickName
                "\"AVATAR\" TEXT," + // 3: avatar
                "\"LEVEL\" INTEGER," + // 4: level
                "\"GENDER\" INTEGER," + // 5: gender
                "\"AUTH\" INTEGER," + // 6: auth
                "\"CONTENT\" TEXT," + // 7: content
                "\"DATE\" INTEGER," + // 8: date
                "\"UNREAD_CNT\" INTEGER," + // 9: unreadCnt
                "\"TYPE\" INTEGER);"); // 10: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAT_SESSION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChatSession entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(3, nickName);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(4, avatar);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(5, level);
        }
 
        Integer gender = entity.getGender();
        if (gender != null) {
            stmt.bindLong(6, gender);
        }
 
        Short auth = entity.getAuth();
        if (auth != null) {
            stmt.bindLong(7, auth);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
 
        Long date = entity.getDate();
        if (date != null) {
            stmt.bindLong(9, date);
        }
 
        Integer unreadCnt = entity.getUnreadCnt();
        if (unreadCnt != null) {
            stmt.bindLong(10, unreadCnt);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(11, type);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChatSession entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(3, nickName);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(4, avatar);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(5, level);
        }
 
        Integer gender = entity.getGender();
        if (gender != null) {
            stmt.bindLong(6, gender);
        }
 
        Short auth = entity.getAuth();
        if (auth != null) {
            stmt.bindLong(7, auth);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
 
        Long date = entity.getDate();
        if (date != null) {
            stmt.bindLong(9, date);
        }
 
        Integer unreadCnt = entity.getUnreadCnt();
        if (unreadCnt != null) {
            stmt.bindLong(10, unreadCnt);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(11, type);
        }
    }

    @Override
    protected final void attachEntity(ChatSession entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public ChatSession readEntity(Cursor cursor, int offset) {
        ChatSession entity = new ChatSession( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nickName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // avatar
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // level
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // gender
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6), // auth
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // content
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // date
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // unreadCnt
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ChatSession entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAvatar(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLevel(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setGender(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setAuth(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6));
        entity.setContent(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDate(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setUnreadCnt(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setType(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
     }
    
    @Override
    protected final String updateKeyAfterInsert(ChatSession entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(ChatSession entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChatSession entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
