package com.laka.live.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.MusicInfo;
import com.laka.live.download.DownloadState;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.MusicMsg;
import com.laka.live.msg.SearchHotKeywordMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.search.SearchActivity;
import com.laka.live.ui.widget.FlowLayout;
import com.laka.live.ui.widget.PageListLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MusicSearchActivity extends BaseActivity implements PageListLayout.OnRequestCallBack, PageListLayout.OnResultListener<MusicMsg>
        , View.OnClickListener {

    private static final String EXTRA_KEYWORD = "extra_keyword";

    private static final String EXTRA_HOT_WORDS = "EXTRA_HOT_WORDS";

    private static final int LIMIT = 20;
    private static final int MAX_HOT_WORD_COUNT = 8;

    private ArrayList<String> mHotwords;

    private String mKeyword;

    private PageListLayout mPageListLayout;

    private MusicListAdapter mAdapter;
    private View mMusicEmptyView;
    private FlowLayout mFlowLayout;

    private EditText mSearchInput;
    private boolean isFirstResume;

    public static void startActivity(Context context, String keyword, ArrayList<String> hotwords) {
        if (context != null) {
            Intent intent = new Intent(context, MusicSearchActivity.class);
            intent.putExtra(EXTRA_KEYWORD, keyword);
            intent.putStringArrayListExtra(EXTRA_HOT_WORDS, hotwords);
            context.startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_music_search);
        MusicManager.getInstance().addDownloadCallback(mMusicDownloadCallback);

        init();
    }

    private void init() {

        initTitle();

        Intent intent = getIntent();
        mHotwords = intent.getStringArrayListExtra(EXTRA_HOT_WORDS);
        mKeyword = intent.getStringExtra(EXTRA_KEYWORD);

        mPageListLayout = (PageListLayout) findViewById(R.id.list);
        mAdapter = new MusicListAdapter(false,this);

        mPageListLayout.setLayoutManager(new LinearLayoutManager(this));
        mPageListLayout.setIsReloadWhenEmpty(false);
        mPageListLayout.setLoadMoreCount(LIMIT);
        mPageListLayout.setAdapter(mAdapter);
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.setOnResultListener(this);
        mPageListLayout.setVisibility(View.GONE);
        mPageListLayout.getRecyclerView().setItemAnimator(null);

        mSearchInput = (EditText) findViewById(R.id.search_input);
        mSearchInput.setHint(R.string.music_search_hint);
        if (mHotwords != null && mHotwords.isEmpty() == false) {
            initMusicEmptyView();
            addHotKeywords();
            mMusicEmptyView.setVisibility(View.GONE);
        }

        findViewById(R.id.root).setOnClickListener(this);


        mSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AnalyticsReport.onEvent(MusicSearchActivity.this, LiveReport.MY_LIVE_EVENT_11257);
                    searchFromInput();
                    return true;
                }
                return false;
            }
        });

        if (TextUtils.isEmpty(mKeyword) == false) {
            search();
        }
    }

    private void initMusicEmptyView() {
        ViewStub viewStub = (ViewStub) findViewById(R.id.view_empty);
        mMusicEmptyView = viewStub.inflate();
        mFlowLayout = (FlowLayout) mMusicEmptyView.findViewById(R.id.hot_tag_layout);
        TextView tip = (TextView) mMusicEmptyView.findViewById(R.id.tip);
        tip.setText(R.string.music_search_empty_tip);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(tip, R.drawable.live_icon_music_seach,
                0, 0, 0);

    }

    private void initTitle() {
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.back_icon).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard(this);
        switch (v.getId()) {
            case R.id.cancel:
                AnalyticsReport.onEvent(MusicSearchActivity.this, LiveReport.MY_LIVE_EVENT_11257);
                searchFromInput();
                break;
            case R.id.back_icon:
                finish();
                break;
        }
    }

    private void addHotKeywords() {
        for (final String content : mHotwords) {
            if (TextUtils.isEmpty(content) == false) {
                SearchActivity.createHotTag(this, mFlowLayout, content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mKeyword = content;
                        search();
                    }
                });
            }
        }
        mFlowLayout.setVisibility(mFlowLayout.getChildCount() > 0 ? View.VISIBLE : View.GONE);
    }

    private void showMusicEmptyView() {
        if (mMusicEmptyView == null) {
            initMusicEmptyView();
        }
        mMusicEmptyView.setVisibility(View.VISIBLE);
        mPageListLayout.setVisibility(View.GONE);

        mFlowLayout.setVisibility(mFlowLayout.getChildCount() > 0 ? View.VISIBLE : View.GONE);
    }

    private void showMusicListView() {
        if (mMusicEmptyView != null) {
            mMusicEmptyView.setVisibility(View.GONE);
        }
        mPageListLayout.setVisibility(View.VISIBLE);
    }

    private void searchFromInput() {
        String input = mSearchInput.getText().toString();
        if (TextUtils.isEmpty(input) == false) {
            mKeyword = input;
            search();
        }
    }

    private void search() {
        mPageListLayout.loadData(true);
        showMusicListView();
        hideKeyboard(this);
        mSearchInput.setText(mKeyword);
        mSearchInput.setSelection(mKeyword.length());
    }

    private void loadHotword() {
        DataProvider.getSongHotWord(this, MAX_HOT_WORD_COUNT, new GsonHttpConnection.OnResultListener<SearchHotKeywordMsg>() {
            @Override
            public void onSuccess(SearchHotKeywordMsg searchHotKeywordMsg) {
                List<String> keywords = searchHotKeywordMsg.getList();
                if (keywords == null || keywords.isEmpty()) {
                    return;
                }

                mHotwords = new ArrayList<>(keywords);

                if (mAdapter.isEmpty() == false) {
                    return;
                }

                showMusicEmptyView();
                addHotKeywords();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.searchSongs(this, page, LIMIT, mKeyword, listener);
    }

    @Override
    public void onResult(MusicMsg msg) {
        AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11259);
        if (msg == null || msg.getList() == null || msg.getList().isEmpty()) {
            if (mHotwords == null || mHotwords.isEmpty()) {
                loadHotword();
            }
            showMusicEmptyView();
        } else {
            showMusicListView();

        }
    }

    private int findMusicInfoWithDownloadId(int downloadId) {
        List<MusicInfo> musicInfos = mAdapter.getAll();
        int index = 0;
        for (MusicInfo info : musicInfos) {
            if (info != null && info.getDownloadId() == downloadId) {
                return index;
            }
            index++;
        }
        return index;
    }

    private MusicDownloadCallback mMusicDownloadCallback = new MusicDownloadCallback() {

        @Override
        public void onStart(int downloadId, long totalBytes) {
            super.onStart(downloadId, totalBytes);
            int index = findMusicInfoWithDownloadId(downloadId);
            MusicInfo musicInfo = mAdapter.getItem(index);
            if (musicInfo != null) {
                musicInfo.setState(DownloadState.STARTED);
                musicInfo.setTotalBytes(totalBytes);
                mAdapter.notifyItemChanged(index);
            }
        }

        @Override
        public void onFailure(int downloadId, int statusCode, String errMsg) {
            super.onFailure(downloadId, statusCode, errMsg);
            int index = findMusicInfoWithDownloadId(downloadId);
            MusicInfo musicInfo = mAdapter.getItem(index);
            if (musicInfo != null) {
                musicInfo.setState(DownloadState.ERROR);
                mAdapter.notifyItemChanged(index);
            }


        }

        @Override
        public void onSuccess(int downloadId, String musicFilePath, String lyricsFilePath) {
            super.onSuccess(downloadId, musicFilePath, lyricsFilePath);
            int index = findMusicInfoWithDownloadId(downloadId);
            MusicInfo musicInfo = mAdapter.getItem(index);
            if (musicInfo != null) {
                musicInfo.setMusicFilePath(musicFilePath);
                musicInfo.setLyricsFilePath(lyricsFilePath);
                musicInfo.setState(DownloadState.FINISHED);
                mAdapter.notifyItemChanged(index);
            }
        }

        @Override
        public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
            super.onProgress(downloadId, bytesWritten, totalBytes);
            int index = findMusicInfoWithDownloadId(downloadId);

            MusicInfo musicInfo = mAdapter.getItem(index);
            if (musicInfo != null) {
                musicInfo.setBytesWritten(bytesWritten);
                musicInfo.setTotalBytes(totalBytes);
                musicInfo.setState(DownloadState.STARTED);
                mAdapter.notifyItemChanged(index);
            }
        }

        @Override
        public void onRetry(int downloadId) {
            super.onRetry(downloadId);
        }
    };

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
        if (isFirstResume == false) {
            mAdapter.updateState();
        }
        isFirstResume = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.getInstance().removeDownloadCallback(mMusicDownloadCallback);
    }
}
