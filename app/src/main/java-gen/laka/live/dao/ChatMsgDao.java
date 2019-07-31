package laka.live.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import laka.live.bean.ChatMsg;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ChatMsg".
*/
public class ChatMsgDao extends AbstractDao<ChatMsg, Long> {

    public static final String TABLENAME = "ChatMsg";

    /**
     * Properties of entity ChatMsg.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property SessionId = new Property(1, String.class, "sessionId", false, "SESSION_ID");
        public final static Property UserId = new Property(2, String.class, "userId", false, "USER_ID");
        public final static Property NickName = new Property(3, String.class, "nickName", false, "NICK_NAME");
        public final static Property Avatar = new Property(4, String.class, "avatar", false, "AVATAR");
        public final static Property Content = new Property(5, String.class, "content", false, "CONTENT");
        public final static Property Date = new Property(6, Long.class, "date", false, "DATE");
        public final static Property Level = new Property(7, Integer.class, "level", false, "LEVEL");
        public final static Property Type = new Property(8, Integer.class, "type", false, "TYPE");
        public final static Property State = new Property(9, Integer.class, "state", false, "STATE");
        public final static Property IsSend = new Property(10, Boolean.class, "isSend", false, "IS_SEND");
        public final static Property IsUnread = new Property(11, Boolean.class, "isUnread", false, "IS_UNREAD");
        public final static Property GiftId = new Property(12, Integer.class, "giftId", false, "GIFT_ID");
    }

    private DaoSession daoSession;


    public ChatMsgDao(DaoConfig config) {
        super(config);
    }
    
    public ChatMsgDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ChatMsg\" (" + //
                "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"SESSION_ID\" TEXT," + // 1: sessionId
                "\"USER_ID\" TEXT," + // 2: userId
                "\"NICK_NAME\" TEXT," + // 3: nickName
                "\"AVATAR\" TEXT," + // 4: avatar
                "\"CONTENT\" TEXT," + // 5: content
                "\"DATE\" INTEGER," + // 6: date
                "\"LEVEL\" INTEGER," + // 7: level
                "\"TYPE\" INTEGER," + // 8: type
                "\"STATE\" INTEGER," + // 9: state
                "\"IS_SEND\" INTEGER," + // 10: isSend
                "\"IS_UNREAD\" INTEGER," + // 11: isUnread
                "\"GIFT_ID\" INTEGER);"); // 12: giftId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ChatMsg\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChatMsg entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String sessionId = entity.getSessionId();
        if (sessionId != null) {
            stmt.bindString(2, sessionId);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(4, nickName);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(5, avatar);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
 
        Long date = entity.getDate();
        if (date != null) {
            stmt.bindLong(7, date);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(8, level);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(9, type);
        }
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(10, state);
        }
 
        Boolean isSend = entity.getIsSend();
        if (isSend != null) {
            stmt.bindLong(11, isSend ? 1L: 0L);
        }
 
        Boolean isUnread = entity.getIsUnread();
        if (isUnread != null) {
            stmt.bindLong(12, isUnread ? 1L: 0L);
        }
 
        Integer giftId = entity.getGiftId();
        if (giftId != null) {
            stmt.bindLong(13, giftId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChatMsg entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String sessionId = entity.getSessionId();
        if (sessionId != null) {
            stmt.bindString(2, sessionId);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(4, nickName);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(5, avatar);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
 
        Long date = entity.getDate();
        if (date != null) {
            stmt.bindLong(7, date);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(8, level);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(9, type);
        }
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(10, state);
        }
 
        Boolean isSend = entity.getIsSend();
        if (isSend != null) {
            stmt.bindLong(11, isSend ? 1L: 0L);
        }
 
        Boolean isUnread = entity.getIsUnread();
        if (isUnread != null) {
            stmt.bindLong(12, isUnread ? 1L: 0L);
        }
 
        Integer giftId = entity.getGiftId();
        if (giftId != null) {
            stmt.bindLong(13, giftId);
        }
    }

    @Override
    protected final void attachEntity(ChatMsg entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ChatMsg readEntity(Cursor cursor, int offset) {
        ChatMsg entity = new ChatMsg( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // sessionId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nickName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // avatar
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // content
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // date
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // level
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // type
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // state
            cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0, // isSend
            cursor.isNull(offset + 11) ? null : cursor.getShort(offset + 11) != 0, // isUnread
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12) // giftId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ChatMsg entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSessionId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNickName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAvatar(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setContent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDate(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setLevel(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setType(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setState(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setIsSend(cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0);
        entity.setIsUnread(cursor.isNull(offset + 11) ? null : cursor.getShort(offset + 11) != 0);
        entity.setGiftId(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ChatMsg entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ChatMsg entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChatMsg entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
