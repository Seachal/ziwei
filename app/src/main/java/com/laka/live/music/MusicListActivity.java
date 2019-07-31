package com.laka.live.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.EditText;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.MusicInfo;
import com.laka.live.download.DownloadState;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.SearchHotKeywordMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.search.SearchActivity;
import com.laka.live.ui.widget.FlowLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by luwies on 16/8/11.
 */
public class MusicListActivity extends BaseActivity implements MusicListAdapter.OnRemoveListener, View.OnClickListener {

    private final static int MAX_HOT_WORD_COUNT = 8;

    private RecyclerView mRecyclerView;

    private MusicListAdapter mAdapter;

    private View mMusicEmptyView;

    private FlowLayout mFlowLayout;

    private List<String> mHotKeywords;

    public static void startActivity(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, MusicListActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_music_list_layout);

        init();
    }

    private void init() {
        initTitle();

        mAdapter = new MusicListAdapter(true, this);
        mAdapter.setOnRemoveListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setItemAnimator(null);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                final List<MusicInfo> musicInfos = MusicManager.getInstance().getMusicInfos();
                if (musicInfos == null || musicInfos.isEmpty()) {
                    loadHotword();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMusicEmptyView();
                        }
                    });
                } else {
                    Collections.sort(musicInfos, mComparator);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addAll(musicInfos);
                            mAdapter.updateState();
                        }
                    });
                }
            }
        });

        MusicManager.getInstance().addDownloadCallback(mDownloadCallback);
    }

    private void initTitle() {
        findViewById(R.id.back_icon).setOnClickListener(this);
        findViewById(R.id.click_view).setOnClickListener(this);
        EditText mEtSearch = (EditText) findViewById(R.id.search_input);
        mEtSearch.setEnabled(false);
        mEtSearch.setHint(R.string.music_search_hint);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_icon:
                finish();
                break;
            case R.id.click_view:
//                AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11257);
                if (mHotKeywords == null) {
                    MusicSearchActivity.startActivity(this, "", null);
                } else {
                    MusicSearchActivity.startActivity(this, "", new ArrayList<>(mHotKeywords));
                }

                break;
        }
    }

    private void initMusicEmptyView() {
        ViewStub viewStub = (ViewStub) findViewById(R.id.view_empty);
        mMusicEmptyView = viewStub.inflate();
        mFlowLayout = (FlowLayout) mMusicEmptyView.findViewById(R.id.hot_tag_layout);
    }

    private void showMusicEmptyView() {
        if (mMusicEmptyView == null) {
            initMusicEmptyView();
        }
        mMusicEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        mFlowLayout.setVisibility(mFlowLayout.getChildCount() > 0 ? View.VISIBLE : View.GONE);
    }

    private void showMusicListView() {
        if (mMusicEmptyView != null) {
            mMusicEmptyView.setVisibility(View.GONE);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private MusicInfo findMusicInfoWithDownloadId(int downloadId) {
        List<MusicInfo> musicInfos = mAdapter.getAll();
        for (MusicInfo info : musicInfos) {
            if (info != null && info.getDownloadId() == downloadId) {
                return info;
            }
        }
        return null;
    }

    private Comparator<MusicInfo> mComparator = new Comparator<MusicInfo>() {
        @Override
        public int compare(MusicInfo lhs, MusicInfo rhs) {
            if (lhs == rhs) {
                return 0;
            }
            if (lhs.getState() == DownloadState.STARTED && rhs.getState() == DownloadState.STARTED) {
                if (lhs.getLastPlayTime() > rhs.getLastPlayTime()) {
                    return -1;
                } else if (lhs.getLastPlayTime() < rhs.getLastPlayTime()) {
                    return 1;
                }
            } else {
                if (lhs.getState() == DownloadState.STARTED) {
                    return -1;
                } else if (rhs.getState() == DownloadState.STARTED) {
                    return 1;
                } else {
                    if (lhs.getState() != DownloadState.FINISHED) {
                        return -1;
                    } else if (rhs.getState() != DownloadState.FINISHED) {
                        return 1;
                    } else {
                        if (lhs.getLastPlayTime() > rhs.getLastPlayTime()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            }
            return 0;
        }
    };

    private void loadHotword() {
        DataProvider.getSongHotWord(this, MAX_HOT_WORD_COUNT, new GsonHttpConnection.OnResultListener<SearchHotKeywordMsg>() {
            @Override
            public void onSuccess(SearchHotKeywordMsg searchHotKeywordMsg) {
                mHotKeywords = searchHotKeywordMsg.getList();
                if (mHotKeywords == null || mHotKeywords.isEmpty()) {
                    return;
                }

                if (mAdapter.isEmpty() == false) {
                    return;
                }

                for (final String content : mHotKeywords) {
                    if (TextUtils.isEmpty(content) == false) {
                        SearchActivity.createHotTag(MusicListActivity.this, mFlowLayout, content, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MusicSearchActivity.startActivity(MusicListActivity.this, content, new ArrayList<>(mHotKeywords));
                            }
                        });
                    }
                }

                showMusicEmptyView();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }

    private MusicDownloadCallback mDownloadCallback = new MusicDownloadCallback() {

        @Override
        public void onTaskAdded(MusicInfo musicInfo) {
            if (musicInfo == null) {
                return;
            }

            try {
                List<MusicInfo> musicInfos = mAdapter.getAll();

                for (MusicInfo temp : musicInfos) {
                    if (temp != null && temp.getId() == musicInfo.getId()) {
                        temp.setDownloadId(musicInfo.getDownloadId());
                        return;
                    }
                }

                if (musicInfos.isEmpty()) {
                    musicInfos.add(musicInfo);
                    return;
                }
                int size = musicInfos.size();
                MusicInfo tmpInfo;
                int i = 0;
                for (; i < size; i++) {
                    tmpInfo = musicInfos.get(i);
                    if (tmpInfo.getState() != DownloadState.STARTED) {
                        musicInfos.add(i, musicInfo);
                        break;
                    }
                }
                if (i == size) {
                    musicInfos.add(musicInfo);
                }
            } finally {
                mAdapter.notifyDataSetChanged();
                showMusicListView();
            }

        }

        @Override
        public void onStart(int downloadId, long totalBytes) {
            super.onStart(downloadId, totalBytes);
            final MusicInfo musicInfo = findMusicInfoWithDownloadId(downloadId);
            if (musicInfo == null) {
                return;
            }
            musicInfo.setTotalBytes(totalBytes);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<MusicInfo> musicInfos = mAdapter.getAll();
                    musicInfos.remove(musicInfo);
                    musicInfos.add(0, musicInfo);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onStop(int downloadId) {
            super.onStop(downloadId);
            final MusicInfo musicInfo = findMusicInfoWithDownloadId(downloadId);
            if (musicInfo == null) {
                return;
            }
            musicInfo.setState(DownloadState.STOPPED);
            List<MusicInfo> datas = mAdapter.getAll();
            if (datas != null && datas.isEmpty() == false) {
                int index = datas.indexOf(musicInfo);
                if (index >= 0 && index < datas.size()) {
                    mAdapter.notifyItemChanged(index);
                }
            }
        }

        @Override
        public void onRetry(int downloadId) {
            super.onRetry(downloadId);
        }

        @Override
        public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
            super.onProgress(downloadId, bytesWritten, totalBytes);
            final MusicInfo musicInfo = findMusicInfoWithDownloadId(downloadId);
            if (musicInfo == null) {
                return;
            }
            musicInfo.setBytesWritten(bytesWritten);
            musicInfo.setTotalBytes(totalBytes);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<MusicInfo> list = mAdapter.getAll();
                    int i = list.indexOf(musicInfo);
                    mAdapter.notifyItemChanged(i);
                }
            });
        }

        @Override
        public void onSuccess(int downloadId, String mp3FilePath, String lyricsFilePAth) {
            super.onSuccess(downloadId, mp3FilePath, lyricsFilePAth);
            final MusicInfo musicInfo = findMusicInfoWithDownloadId(downloadId);
            if (musicInfo == null) {
                return;
            }
            musicInfo.setState(DownloadState.FINISHED);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<MusicInfo> list = mAdapter.getAll();
                    list.remove(musicInfo);
                    MusicInfo tempInfo;
                    int size = list.size();
                    int i = 0;
                    for (; i < size; i++) {
                        tempInfo = list.get(i);
                        if (tempInfo != null && tempInfo.getState() != DownloadState.STARTED) {
                            list.add(i, musicInfo);
                            break;
                        }
                    }
                    if (i == size) {
                        list.add(musicInfo);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(int downloadId, int statusCode, String errMsg) {
            super.onFailure(downloadId, statusCode, errMsg);
            final MusicInfo musicInfo = findMusicInfoWithDownloadId(downloadId);
            if (musicInfo == null) {
                return;
            }
            musicInfo.setState(DownloadState.ERROR);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<MusicInfo> list = mAdapter.getAll();
                    int i = list.indexOf(musicInfo);
                    mAdapter.notifyItemChanged(i);
                }
            });
        }
    };

    @Override
    public void onRemove() {
        if (mAdapter.isEmpty()) {
            if (mHotKeywords == null || mHotKeywords.isEmpty()) {
                loadHotword();
            }
            showMusicEmptyView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (SubcriberTag.MUSIC_PLAY_EVENT.equals(event.tag)) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11256);

        mAdapter.updateState();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.getInstance().removeDownloadCallback(mDownloadCallback);
    }

}
