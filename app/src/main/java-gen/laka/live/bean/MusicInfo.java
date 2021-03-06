package laka.live.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.DaoException;
import laka.live.dao.DaoSession;
import laka.live.dao.MusicInfoDao;

/**
 * Entity mapped to table "MusicInfo".
 */
@Entity(nameInDb="MusicInfo",active = true,createInDb = true)
public class MusicInfo {
    @Id
    @Property(nameInDb="id")
    private Long id;
    private String title;
    private String singer;
    private String url;
    private String code;
    private Long totalBytes;
    private Long bytesWritten;
    private String musicFilePath;
    private String lyricsFilePath;
    private Integer downloadState;
    private Long lastPlayTime;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 906190205)
    private transient MusicInfoDao myDao;
    @Generated(hash = 284420482)
    public MusicInfo(Long id, String title, String singer, String url, String code, Long totalBytes,
            Long bytesWritten, String musicFilePath, String lyricsFilePath, Integer downloadState,
            Long lastPlayTime) {
        this.id = id;
        this.title = title;
        this.singer = singer;
        this.url = url;
        this.code = code;
        this.totalBytes = totalBytes;
        this.bytesWritten = bytesWritten;
        this.musicFilePath = musicFilePath;
        this.lyricsFilePath = lyricsFilePath;
        this.downloadState = downloadState;
        this.lastPlayTime = lastPlayTime;
    }
    @Generated(hash = 1735505054)
    public MusicInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSinger() {
        return this.singer;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Long getTotalBytes() {
        return this.totalBytes;
    }
    public void setTotalBytes(Long totalBytes) {
        this.totalBytes = totalBytes;
    }
    public Long getBytesWritten() {
        return this.bytesWritten;
    }
    public void setBytesWritten(Long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }
    public String getMusicFilePath() {
        return this.musicFilePath;
    }
    public void setMusicFilePath(String musicFilePath) {
        this.musicFilePath = musicFilePath;
    }
    public String getLyricsFilePath() {
        return this.lyricsFilePath;
    }
    public void setLyricsFilePath(String lyricsFilePath) {
        this.lyricsFilePath = lyricsFilePath;
    }
    public Integer getDownloadState() {
        return this.downloadState;
    }
    public void setDownloadState(Integer downloadState) {
        this.downloadState = downloadState;
    }
    public Long getLastPlayTime() {
        return this.lastPlayTime;
    }
    public void setLastPlayTime(Long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1297458161)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMusicInfoDao() : null;
    }


}
