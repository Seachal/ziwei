package com.laka.live.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.OnlineUserInfoItemView;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.ui.room.ConnectMicManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.util.ResourceHelper;

import java.util.List;

import static com.laka.live.help.SubcriberTag.ON_USER_INFO_ITEM_CLICK_EVENT;

/**
 * Created by luwies on 16/10/31.
 */

public class OnlineAdapter extends BaseAdapter<ConnectUserInfo, OnlineAdapter.ViewHolder> {

    private int mAudienceCount;
    private Context mContext;


    public OnlineAdapter(Context context) {
        this.mContext = context;
    }

    public void setAudienceCount(int audienceCount) {
        this.mAudienceCount = audienceCount;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.online_info_layout, null));
    }

    ConnectMicManager connectMicManager;
    public void setConnectMicManager(ConnectMicManager connectMicManager) {
        this.connectMicManager = connectMicManager;
    }
    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);
        AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11265);
        EventBusManager.postEvent(getItem(position), ON_USER_INFO_ITEM_CLICK_EVENT);
    }

    class ViewHolder extends BaseAdapter.ViewHolder<ConnectUserInfo> {

        private OnlineUserInfoItemView itemView;

        private TextView head;

        public ViewHolder(View itemView) {
            super(itemView);
            head = (TextView) itemView.findViewById(R.id.head);
            this.itemView = (OnlineUserInfoItemView) itemView.findViewById(R.id.item_view);
            this.itemView.setConnectMicManager(connectMicManager);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ConnectUserInfo connectUserInfo) {
            itemView.setUserInfoData(connectUserInfo);
            if (position == 0) {
                head.setVisibility(View.VISIBLE);
                head.setText(R.string.live_manager);
            } else {
                ConnectUserInfo pre = getItem(position - 1);
                if (pre.isManager() && connectUserInfo.isManager() == false) {
                    head.setVisibility(View.VISIBLE);
                    head.setText(Html.fromHtml(head.getContext().getString(R.string.audience_count, mAudienceCount)));
                } else {
                    head.setVisibility(View.GONE);
                }
            }


            int dividerMargin;
            if (position == getItemCount() - 1 || getItem(position + 1).isManager() == false) {
                dividerMargin = 0;
            } else {
                dividerMargin = ResourceHelper.getDimen(R.dimen.activity_horizontal_margin);
            }

            itemView.setDividerMargin(dividerMargin, 0, 0, 0);


        }
    }
}
