package com.laka.live.ui.room;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.laka.live.R;
import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.bean.OnlineUserMessage;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.RoomManager;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.adapter.OnlineAdapter;
import com.laka.live.ui.widget.PageListLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by luwies on 16/11/1.
 */

public class OnlineAudienceView extends ConnectListBaseView implements PageListLayout.OnRequestCallBack {

    private PageListLayout mListLayout;

    private OnlineAdapter mAdapter;

    private ConnectMicManager connectMicManager;

    public OnlineAudienceView(Context context) {
        this(context, null);
    }

    public OnlineAudienceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OnlineAudienceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mListLayout = (PageListLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_online_audience, null);
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mListLayout, params);

        initView();
    }

    private void initView() {

        mAdapter = new OnlineAdapter(getContext());
        mListLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mListLayout.setAdapter(mAdapter);
        mListLayout.setIsReloadWhenEmpty(true);
        mListLayout.setOnRequestCallBack(this);
//        mListLayout.loadData(false);
        mListLayout.showData();
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        mOnResultListener = listener;
        RoomManager.getInstance().sendCommand(RoomManager.TLV_REQ_QUERY_ONLINE_LIST, page);
        return null;
    }

    @Subscribe
    @Override
    public void onEvent(final PostEvent event) {
        post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.equals(event.tag, SubcriberTag.TLV_RSP_QUERY_ONLINE_LIST_EVENT)) {
                    OnlineUserMessage message = (OnlineUserMessage) event.event;
                    if (message != null) {
                        mAdapter.setAudienceCount(message.getCount());
                        List<ConnectUserInfo> list = message.getList();
                        onResult(list);
                    } else {
                        mAdapter.setAudienceCount(0);
                        onResult(null);
                    }
                } else if (TextUtils.equals(event.tag, SubcriberTag.REFRESH_ONLINE_LIST)) {
                    refresh();
                }
            }
        });

    }

    public void setConnectMicManager(ConnectMicManager connectMicManager) {
        this.connectMicManager = connectMicManager;
        mAdapter.setConnectMicManager(connectMicManager);
    }

    @Override
    public void refresh() {
        if (mAdapter != null) {
            mListLayout.loadData(mAdapter.isEmpty());
        }
    }
}
