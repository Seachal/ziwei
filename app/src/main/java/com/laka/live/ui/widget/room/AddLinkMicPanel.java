package com.laka.live.ui.widget.room;

import android.Manifest;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.audio.BgmManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.room.ConnectMicManager;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;
import com.tencent.karaoke.common.media.audiofx.Reverb;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ios on 16/6/27.
 */
public class AddLinkMicPanel extends BasePanel implements View.OnClickListener,EventBusManager.OnEventBusListener{

    private static final String TAG = "AddLinkMicPanel";
    private View parentView;
    private Button btnAddLink;
    private TextView tvTips;
    private ConnectMicManager connectMicManager;
//    private Base activity;
    public AddLinkMicPanel(Context context,ConnectMicManager connectMicManager) {
        super(context);
        this.connectMicManager = connectMicManager;
        EventBusManager.register(this);
        setAlpha(0);
        initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_link:
                if (connectMicManager.isApplying) {
                    connectMicManager.closeLinkMic(""); //取消连麦
                    AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11277);
                } else {
                    if (Utils.checkPermission(mContext , Manifest.permission.RECORD_AUDIO)
                            && Utils.checkPermission(mContext , Manifest.permission.CAMERA)){
                        connectMicManager.addConnectMic("");//请求连麦
                        AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11270);
                    }else {
                        ToastHelper.showToast("请先授权摄像头和麦克风");
                        EventBusManager.postEvent(0,SubcriberTag.REQUEST_LIVE_PERMISSION );
                    }
                }
                break;
        }
    }

    private void initView() {
        btnAddLink = (Button) parentView.findViewById(R.id.btn_add_link);
        btnAddLink.setOnClickListener(this);
        tvTips = (TextView) parentView.findViewById(R.id.tv_tips);

        refreshStatus();
    }

    private void refreshStatus() {
        if(connectMicManager.isApplying){
            btnAddLink.setText(R.string.cancel_link);
            tvTips.setText(R.string.apply_link_mic_laka_zhubo);
            btnAddLink.setBackgroundResource(R.drawable.round_f65843_white);
            btnAddLink.setTextColor(ResourceHelper.getColor(R.color.colorF65843));
        }else{
            btnAddLink.setText(R.string.i_want_link);
            tvTips.setText(R.string.link_mic_laka_zhubo);
            btnAddLink.setBackgroundResource(R.drawable.round_f65843);
            btnAddLink.setTextColor(ResourceHelper.getColor(R.color.white));
        }
    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.panel_add_link_mic, null);
        return parentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = Utils.dip2px(mContext,240);
        Log.d(TAG, " width=" + width + " height=" + height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.BOTTOM;
        return lp;
    }


    public void hidePanel() {
        super.hidePanel();
        EventBusManager.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        String tag = event.tag;
        if (SubcriberTag.REFRESH_LINK_MIC_PANEL.equals(tag)) {
            refreshStatus();
        }
    }

    @Override
    public void showPanel() {
        super.showPanel();
        if(connectMicManager.isApplying){
            AnalyticsReport.onEvent(mContext , LiveReport.MY_LIVE_EVENT_11274);
        }else{

        }

    }
}
