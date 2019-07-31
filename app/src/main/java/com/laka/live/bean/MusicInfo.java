package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.download.DownloadState;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/8/15.
 */
public class MusicInfo {

    @Expose
    @SerializedName(Common.ID)
    private long id;

    @Expose
    @SerializedName(Common.TITLE)
    private String title;

    @Expose
    @SerializedName(Common.SINGER)
    private String singer;

    @Expose
    @SerializedName(Common.URL)
    private String url;

    /**
     * 检验码
     */
    @Expose
    @SerializedName(Common.CODE)
    private String code;

    private long totalBytes;
    private long bytesWritten;
    private int downloadId;

    private String musicFilePath;

    /**
     * 歌词路径
     */
    private String lyricsFilePath;

    private DownloadState state = DownloadState.STOPPED;

    private laka.live.bean.MusicInfo dbMusicInfo;

    private long lastPlayTime;

    public MusicInfo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public DownloadState getState() {
        return state;
    }

    public void setState(DownloadState state) {
        this.state = state;
    }

    public String getMusicFilePath() {
        return musicFilePath;
    }

    public void setMusicFilePath(String musicFilePath) {
        this.musicFilePath = musicFilePath;
    }

    public String getLyricsFilePath() {
        return lyricsFilePath;
    }

    public void setLyricsFilePath(String lyricsFilePath) {
        this.lyricsFilePath = lyricsFilePath;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public static laka.live.bean.MusicInfo toDbMusicInfo(MusicInfo musicInfo) {
        if (musicInfo == null) {
            return null;
        }

        if (musicInfo.dbMusicInfo == null) {
            musicInfo.dbMusicInfo = new laka.live.bean.MusicInfo();
        }

        laka.live.bean.MusicInfo dbMusicInfo = musicInfo.dbMusicInfo;
        dbMusicInfo.setId(musicInfo.getId());
        dbMusicInfo.setTitle(musicInfo.getTitle());
        dbMusicInfo.setSinger(musicInfo.getSinger());
        dbMusicInfo.setUrl(musicInfo.getUrl());
        dbMusicInfo.setCode(musicInfo.getCode());
        dbMusicInfo.setTotalBytes(musicInfo.getTotalBytes());
        dbMusicInfo.setBytesWritten(musicInfo.getBytesWritten());
        dbMusicInfo.setMusicFilePath(musicInfo.getMusicFilePath());
        dbMusicInfo.setLyricsFilePath(musicInfo.getLyricsFilePath());
        dbMusicInfo.setDownloadState(musicInfo.getState().value());
        dbMusicInfo.setLastPlayTime(musicInfo.getLastPlayTime());
        return dbMusicInfo;
    }

    public static MusicInfo fromDbMusicInfo(laka.live.bean.MusicInfo dbMusicInfo) {
        if (dbMusicInfo == null) {
            return null;
        }

        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setId(dbMusicInfo.getId());
        musicInfo.setTitle(dbMusicInfo.getTitle());
        musicInfo.setSinger(dbMusicInfo.getSinger());
        musicInfo.setUrl(dbMusicInfo.getUrl());
        musicInfo.setCode(dbMusicInfo.getCode());
        musicInfo.setTotalBytes(dbMusicInfo.getTotalBytes());
        musicInfo.setBytesWritten(dbMusicInfo.getBytesWritten());
        musicInfo.setMusicFilePath(dbMusicInfo.getMusicFilePath());
        musicInfo.setLyricsFilePath(dbMusicInfo.getLyricsFilePath());
        musicInfo.setState(DownloadState.valueOf(dbMusicInfo.getDownloadState()));
        musicInfo.setLastPlayTime(dbMusicInfo.getLastPlayTime());
        return musicInfo;
    }

    public static void copyLocalInfo(MusicInfo networdInfo, MusicInfo localInfo) {
        if (networdInfo != null && localInfo != null) {
            networdInfo.setTotalBytes(localInfo.getTotalBytes());
            networdInfo.setBytesWritten(localInfo.getBytesWritten());
            networdInfo.setDownloadId(localInfo.getDownloadId());
            networdInfo.setMusicFilePath(localInfo.getMusicFilePath());
            networdInfo.setLyricsFilePath(localInfo.getLyricsFilePath());
            networdInfo.setState(localInfo.getState());
        }
    }
}
