package com.laka.live.ui.room;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.RoomManager;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.adapter.ConnectMicAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.laka.live.help.SubcriberTag.CLEAR_LINK_CLICK_EVENT;
import static com.laka.live.help.SubcriberTag.TLV_RSP_QUERY_LINK_LIST_EVENT;

/**
 * Created by luwies on 16/11/1.
 */

public class ConnectListView extends ConnectListBaseView implements PageListLayout.OnRequestCallBack
        , View.OnClickListener, ConnectMicAdapter.OnRefuseListener {

    private PageListLayout mListLayout;

    private TextView mFoot;

    private ConnectMicAdapter mAdapter;

    private ConnectMicManager connectMicManager;

    public ConnectListView(Context context) {
        this(context, null);
    }

    public ConnectListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConnectListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_connect_list, null);
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(rootView, params);

        initView(rootView);
    }

    private void initView(View rootView) {
        mFoot = (TextView) rootView.findViewById(R.id.foot);
        mFoot.setOnClickListener(this);


        mAdapter = new ConnectMicAdapter(getContext());
        mAdapter.setOnRefuseListener(this);
        mListLayout = (PageListLayout) rootView.findViewById(R.id.listview);
        mListLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mListLayout.setIsReloadWhenEmpty(true);
        mListLayout.setAdapter(mAdapter);
        mListLayout.setOnRequestCallBack(this);
//        mListLayout.loadData();

    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        mOnResultListener = listener;
        RoomManager.getInstance().sendCommand(RoomManager.TLV_REQ_QUERY_LINK_LIST, page);
        Log.error("Room", "send TLV_REQ_QUERY_LINK_LIST ");
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.foot:
                handleFootClick();
                break;
        }
    }

    private void handleFootClick() {
        mFoot.setText(connectMicManager.isOpenConnectMic() ? R.string.open_connect_mic
                : R.string.close_connect_mic);
        connectMicManager.toggleConnectMic();
    }

    @Subscribe
    @Override
    public void onEvent(final PostEvent event) {
        Log.error("ConnectListView", event.tag);
        if (TextUtils.equals(event.tag, TLV_RSP_QUERY_LINK_LIST_EVENT)) {
            handleQueryLinkListRspEvent((List<ConnectUserInfo>) event.event);
        } else if (TextUtils.equals(event.tag, CLEAR_LINK_CLICK_EVENT)) {
            handleClearEvent();
        } else if (TextUtils.equals(event.tag, SubcriberTag.REFRESH_LINK_LIST)) {
            refresh();
        }
    }

    private void handleQueryLinkListRspEvent(List<ConnectUserInfo> list) {
        connectMicManager.setInConnectMic(false);
        connectMicManager.setClearLinkList(true);
        if (list != null) {
            for (ConnectUserInfo info : list) {
                if (info != null) {
                    int state = info.getState();
                    if (state != ConnectUserInfo.STATE_NONE && state !=
                            ConnectUserInfo.STATE_CONNECT_FAIL && state !=
                            ConnectUserInfo.STATE_AUDIENCE_WAITING_CONFIRM) {
                        connectMicManager.setInConnectMic(true);
                        break;
                    }
                }
            }

            for (ConnectUserInfo info : list) {
                if (info != null) {
                    int state = info.getState();
                    if (state == ConnectUserInfo.STATE_ANCHOR_WAITING_CONFIRM || state ==
                            ConnectUserInfo.STATE_AUDIENCE_WAITING_CONFIRM || state ==
                            ConnectUserInfo.STATE_WAITING_SUCCESS) {
                        connectMicManager.setClearLinkList(false);
                        break;
                    }
                }
            }
        }

        onResult(list);
    }

    private void handleClearEvent() {
        /*post(new Runnable() {
            @Override
            public void run() {
                if (connectMicManager.enableClearLinkList()) {
                    mAdapter.clear();
                    mListLayout.showEmpty();
                } else {
                    ToastHelper.showToast(R.string.can_not_clear_connect_mic_tip);
                }
            }
        });*/
        if (mListLayout != null) {
            mListLayout.loadData(true);
        }
    }

    public void setConnectMicManager(ConnectMicManager connectMicManager) {
        this.connectMicManager = connectMicManager;
        mFoot.setText(connectMicManager.isOpenConnectMic() ? R.string.close_connect_mic
                : R.string.open_connect_mic);
        mAdapter.setConnectMicManager(connectMicManager);

        connectMicManager.setInConnectMic(false);
    }

    @Override
    public void refresh() {
        post(new Runnable() {
            @Override
            public void run() {
                if (mAdapter != null) {
                    mListLayout.loadData(mAdapter.isEmpty());
                }
            }
        });
    }

    @Override
    public void onRefuse(int position) {
        ToastHelper.showToast(R.string.refuse_connect_mic_success);
        ConnectUserInfo connectUserInfo = mAdapter.getItem(position);
        if (connectUserInfo != null) {
            AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11263);
            connectMicManager.refuse(connectUserInfo.getIdStr());
        }
        if (mAdapter != null) {
            mAdapter.remove(position);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.isEmpty()) {
                mListLayout.showEmpty();
            }
        }
    }
}
