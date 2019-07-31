package com.laka.live.music;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Priority;
import com.laka.live.bean.MusicInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.download.DownloadManager;
import com.laka.live.download.DownloadState;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.util.Md5Util;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import laka.live.dao.MusicInfoDao;

/**
 * Created by luwies on 16/8/12.
 */
public class MusicManager extends DownloadCallback {

    private final static String TAG = "MusicManager";

    public static final int MD5_CHECK_STATUS_CODE = -9999;

    public static final int FILE_NOT_EXITS_STATUS_CODE = -9998;

    public static final int UN_ZIP_ERROR_STATUS_CODE = -9997;

    public static final String MUSIC_PATH = "Laka/Music";

    private static final String MUSIC_SUFFIX = "mp3";

    /**
     * 歌词
     */
    private static final String LYRICS_SUFFIX = "txt";

    private static final String MUSIC_DIR = Environment.getExternalStorageDirectory()
            + File.separator + MUSIC_PATH;

    private static final int MAX_RETRY_TIME = 5;

    private static final int RETRY_INTERVAL = 2;

    private static final int PROGRESS_INTERVAL = 100;

    private static MusicManager sInstance;

    private List<MusicDownloadCallback> mDownloadCallbacks;

    private ConcurrentMap<Long, MusicInfo> mMusicInfos;

    private ConcurrentMap<Integer, MusicInfo> mDownloadingMusicInfos;

    private MusicManager() {
        init();
    }

    public static MusicManager getInstance() {
        if (sInstance == null) {
            synchronized (MusicManager.class) {
                if (sInstance == null) {
                    sInstance = new MusicManager();
                }
            }
        }
        return sInstance;
    }

    private void init() {
        createDir(MUSIC_DIR);
        mDownloadingMusicInfos = new ConcurrentHashMap<>();
        mMusicInfos = new ConcurrentHashMap<>();
        mDownloadCallbacks = new ArrayList<>();
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                MusicInfoDao musicInfoDao = DbManger.getInstance().getDaoSession().getMusicInfoDao();
                List<laka.live.bean.MusicInfo> dbList = musicInfoDao.loadAll();
                MusicInfo musicInfo;
                boolean isMusicFileExists;
                boolean isLyricsFileExists;
                for (laka.live.bean.MusicInfo dbMusicInfo : dbList) {
                    if (dbMusicInfo != null) {

                        String musicTempFile = dbMusicInfo.getMusicFilePath();
                        String lyricsTempFile = dbMusicInfo.getLyricsFilePath();
                        isMusicFileExists = TextUtils.isEmpty(musicTempFile) ? false : new File(musicTempFile).exists();
                        isLyricsFileExists = TextUtils.isEmpty(lyricsTempFile) ? false : new File(lyricsTempFile).exists();
                        musicInfo = MusicInfo.fromDbMusicInfo(dbMusicInfo);
                        long bytesWritten = dbMusicInfo.getBytesWritten();
                        long totalBytes = dbMusicInfo.getTotalBytes();

                        if (bytesWritten == totalBytes && bytesWritten != 0
                                && isMusicFileExists && isLyricsFileExists) {
                            musicInfo.setState(DownloadState.FINISHED);
                        } else {
                            /*if (isMusicFileExists == false || isLyricsFileExists == false) {
                                musicInfo.setBytesWritten(0L);
                                musicInfo.setTotalBytes(0L);
                            }*/
                            musicInfo.setState(DownloadState.STOPPED);
                        }

                        mMusicInfos.put(musicInfo.getId(), musicInfo);
                    }
                }
            }
        });
    }

    private static void createDir(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory() && file.exists() == false) {
            file.mkdirs();
        }
    }

    public void download(MusicInfo musicInfo) {
        if (musicInfo == null) {
            return;
        }
        DownloadRequest request = new DownloadRequest.Builder()
                .url(musicInfo.getUrl())
                .retryTime(MAX_RETRY_TIME)
                .retryInterval(RETRY_INTERVAL, TimeUnit.SECONDS)
                .progressInterval(PROGRESS_INTERVAL, TimeUnit.MILLISECONDS)
                .priority(Priority.HIGH)
                .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI | DownloadRequest.NETWORK_MOBILE)
//                .destinationDirectory(MUSIC_DIR)
                .downloadCallback(this)
                .build();
        int downloadId = DownloadManager.getInstance().add(request);
        if (downloadId != -1) {
            //-1表示已经在下载队列里了
            //插入数据库
            musicInfo.setDownloadId(downloadId);
            musicInfo.setState(DownloadState.WAITING);
            saveToDb(musicInfo);
            mDownloadingMusicInfos.put(downloadId, musicInfo);
            mMusicInfos.put(musicInfo.getId(), musicInfo);
            for (MusicDownloadCallback callback : mDownloadCallbacks) {
                callback.onTaskAdded(musicInfo);
            }
        } else {

            musicInfo.setState(DownloadState.WAITING);
        }
    }

    public void stopDownload(MusicInfo musicInfo) {
        if (musicInfo != null) {
            DownloadManager.getInstance().cancel(musicInfo.getDownloadId());
            musicInfo.setState(DownloadState.STOPPED);
            for (MusicDownloadCallback callback : mDownloadCallbacks) {
                callback.onStop(musicInfo.getDownloadId());
            }

            MusicInfo localInfo = mDownloadingMusicInfos.remove(musicInfo.getDownloadId());
            if (localInfo != null) {
                localInfo.setState(DownloadState.STOPPED);
            }
        }
    }

    public void deleteMusic(MusicInfo musicInfo) {
        if (musicInfo != null) {
            DownloadManager.getInstance().cancel(musicInfo.getDownloadId());
            mMusicInfos.remove(musicInfo.getId());
            mDownloadingMusicInfos.remove(musicInfo.getDownloadId());
            deleteFromDb(musicInfo);
            String musicFile = musicInfo.getMusicFilePath();

            File parentFile = null;
            if (TextUtils.isEmpty(musicFile) == false) {
                File file = new File(musicFile);
                parentFile = file.getParentFile();
                file.delete();
            }
            String lyricsFile = musicInfo.getLyricsFilePath();
            if (TextUtils.isEmpty(lyricsFile) == false) {
                File file = new File(lyricsFile);
                parentFile = file.getParentFile();
                file.delete();
            }
            if (parentFile != null) {
                parentFile.delete();
            }
        }

    }

    private void deleteFromDb(final MusicInfo musicInfo) {
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                MusicInfoDao musicInfoDao = DbManger.getInstance().getDaoSession().getMusicInfoDao();
                laka.live.bean.MusicInfo dbMusicInfo = MusicInfo.toDbMusicInfo(musicInfo);
                musicInfoDao.delete(dbMusicInfo);
            }
        });
    }

    private void saveToDb(final MusicInfo musicInfo) {
        if (musicInfo == null) {
            return;
        }
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                laka.live.bean.MusicInfo dbMusicInfo = MusicInfo.toDbMusicInfo(musicInfo);
                MusicInfoDao musicInfoDao = DbManger.getInstance().getDaoSession().getMusicInfoDao();
                musicInfoDao.insertOrReplace(dbMusicInfo);
            }
        });

    }

    public void addDownloadCallback(MusicDownloadCallback callback) {
        if (callback != null) {
            mDownloadCallbacks.add(callback);
        }
    }

    public void removeDownloadCallback(MusicDownloadCallback callback) {
        if (callback != null) {
            mDownloadCallbacks.remove(callback);
        }
    }

    private boolean check(MusicInfo musicInfo, String filePath) {
        if (musicInfo == null) {
            return false;
        }
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        String md5Code = musicInfo.getCode();
        String fileMd5Code = Md5Util.md5sum(filePath);
        if (TextUtils.isEmpty(md5Code) || TextUtils.isEmpty(fileMd5Code)) {
            return false;
        }
        return md5Code.equalsIgnoreCase(fileMd5Code);
    }

    public List<MusicInfo> getMusicInfos() {
        return new ArrayList<>(mMusicInfos.values());
    }

    public void playMusic(MusicInfo musicInfo) {
        if (musicInfo == null) {
            return;
        }

        musicInfo.setLastPlayTime(System.currentTimeMillis());
        saveToDb(musicInfo);

        EventBusManager.postEvent(musicInfo, SubcriberTag.MUSIC_PLAY_EVENT);
    }

    public MusicInfo getMusicInfo(long id) {
        return mMusicInfos.get(id);
    }

    @Override
    public void onStart(int downloadId, long totalBytes) {
        super.onStart(downloadId, totalBytes);
        MusicInfo musicInfo = mDownloadingMusicInfos.get(downloadId);
        if (musicInfo != null) {
            musicInfo.setState(DownloadState.STARTED);
            saveToDb(musicInfo);

            for (DownloadCallback callback : mDownloadCallbacks) {
                callback.onStart(downloadId, totalBytes);
            }
        }
    }

    @Override
    public void onRetry(int downloadId) {
        super.onRetry(downloadId);

        for (DownloadCallback callback : mDownloadCallbacks) {
            callback.onRetry(downloadId);
        }
    }

    @Override
    public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
        super.onProgress(downloadId, bytesWritten, totalBytes);
        MusicInfo musicInfo = mDownloadingMusicInfos.get(downloadId);
        if (musicInfo != null) {
            musicInfo.setBytesWritten(bytesWritten);
            musicInfo.setTotalBytes(totalBytes);
            saveToDb(musicInfo);
        }

        for (DownloadCallback callback : mDownloadCallbacks) {
            callback.onProgress(downloadId, bytesWritten, totalBytes);
        }
    }

    private void postOnFailure(final int downloadId, final int statusCode, final String errMsg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(downloadId, statusCode, errMsg);
            }
        });
    }

    @Override
    public void onSuccess(final int downloadId, final String filePath) {
        super.onSuccess(downloadId, filePath);
        final MusicInfo musicInfo = mDownloadingMusicInfos.get(downloadId);
        if (musicInfo != null) {
            //解压校验
            BackgroundThread.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (check(musicInfo, filePath) == false) {
                            postOnFailure(downloadId, MD5_CHECK_STATUS_CODE, "md5 check fail");
                        } else {
                            File musicDir = new File(MUSIC_DIR + File.separator + musicInfo.getId());
                            musicDir.delete();
                            if (musicDir.exists() == false) {
                                musicDir.mkdirs();
                            }
                            try {
                                Utils.unZipFolder(filePath, musicDir.getAbsolutePath());
                            } catch (Exception e) {
                                postOnFailure(downloadId, UN_ZIP_ERROR_STATUS_CODE, "unzip error");
                                return;
                            }

                            FileFilter mp3FileFilter = new FileNameExtensionFilter("mp3 file", MUSIC_SUFFIX);
                            FileFilter lyricsFileFilter = new FileNameExtensionFilter("lyrics file", LYRICS_SUFFIX);
                            File[] mp3Files = musicDir.listFiles(mp3FileFilter);
                            File[] lyricsFiles = musicDir.listFiles(lyricsFileFilter);

                            if (mp3Files == null || mp3Files.length == 0 || lyricsFiles == null || lyricsFiles.length == 0) {
                                postOnFailure(downloadId, FILE_NOT_EXITS_STATUS_CODE, "file not exits");
                                return;
                            }

                            final File mp3File = mp3Files[0];
                            final File lyricsFile = lyricsFiles[0];
                            musicInfo.setMusicFilePath(mp3File.getAbsolutePath());
                            musicInfo.setLyricsFilePath(lyricsFile.getAbsolutePath());
                            musicInfo.setLastPlayTime(System.currentTimeMillis());
                            musicInfo.setState(DownloadState.FINISHED);
                            saveToDb(musicInfo);

                            mDownloadingMusicInfos.remove(downloadId);

                            Handler handler = new Handler(Looper.getMainLooper());
                            for (final MusicDownloadCallback callback : mDownloadCallbacks) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(downloadId, mp3File.getAbsolutePath(), lyricsFile.getAbsolutePath());
                                    }
                                });
                            }
                        }
                    } finally {
                        File zip = new File(filePath);
                        zip.delete();
                    }

                }
            });


        }

    }

    @Override
    public void onFailure(int downloadId, int statusCode, String errMsg) {
        super.onFailure(downloadId, statusCode, errMsg);
        MusicInfo musicInfo = mDownloadingMusicInfos.get(downloadId);
        if (musicInfo != null) {
            musicInfo.setState(DownloadState.ERROR);
            saveToDb(musicInfo);
        }

        for (DownloadCallback callback : mDownloadCallbacks) {
            callback.onFailure(downloadId, statusCode, errMsg);
        }

    }

}
