package com.laka.live.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.account.ConnectMicUserInfoItemView;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.ui.room.ConnectMicManager;
import com.laka.live.ui.widget.HorizontalDragLayout;

import static com.laka.live.help.SubcriberTag.ON_USER_INFO_ITEM_CLICK_EVENT;

/**
 * Created by luwies on 16/10/31.
 */

public class ConnectMicAdapter extends BaseAdapter<ConnectUserInfo, ConnectMicAdapter.ViewHolder> {

    ConnectMicManager connectMicManager;

    private OnRefuseListener mOnRefuseListener;

    private Context mContext;
    public ConnectMicAdapter(Context mContext) {
       this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_mic_user_info_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(null);
    }

    public void setConnectMicManager(ConnectMicManager connectMicManager) {
        this.connectMicManager = connectMicManager;
    }

    class ViewHolder extends BaseAdapter.ViewHolder<ConnectUserInfo> implements View.OnClickListener {

//        private HorizontalDragLayout itemView;

        private ConnectMicUserInfoItemView infoItemView;


        public ViewHolder(View itemView) {
            super(itemView);
//            this.itemView = (HorizontalDragLayout) itemView;
            infoItemView = (ConnectMicUserInfoItemView) itemView.findViewById(R.id.user_info_layout);
            infoItemView.setConnectMicManager(connectMicManager);
            infoItemView.setOnClickListener(this);
            itemView.findViewById(R.id.refuse).setOnClickListener(this);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ConnectUserInfo connectUserInfo) {
            infoItemView.setUserInfoData(connectUserInfo);

            int state = connectUserInfo.getState();
            ((HorizontalDragLayout) itemView).setIsDragEnable(state == ConnectUserInfo.STATE_NONE ||
            state == ConnectUserInfo.STATE_AUDIENCE_WAITING_CONFIRM || state == ConnectUserInfo.STATE_CONNECT_FAIL);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refuse:
                    onRefuse(getAdapterPosition());
                    break;
                case R.id.user_info_layout:
                    AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11261);
                    EventBusManager.postEvent(getItem(getAdapterPosition()), ON_USER_INFO_ITEM_CLICK_EVENT);
                    break;

            }
        }
    }

    public void setOnRefuseListener(OnRefuseListener listener) {
        mOnRefuseListener = listener;
    }

    private void onRefuse(int position) {
        if (mOnRefuseListener != null) {
            mOnRefuseListener.onRefuse(position);
        }
    }

    public interface OnRefuseListener {
        void onRefuse(int position);
    }
}
