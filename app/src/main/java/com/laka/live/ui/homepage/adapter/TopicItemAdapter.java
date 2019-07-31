package com.laka.live.ui.homepage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.HotTopic;
import com.laka.live.ui.activity.TopicRoomListActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

/**
 * @ClassName: TopicItemAdapter
 * @Description: 热门话题
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/1/17
 */

public class TopicItemAdapter extends BaseAdapter<HotTopic, BaseAdapter.ViewHolder> {
    public final static int TYPE_HOME = 0;
    public final static int TYPE_TOPIC = 1;

    private Context mContext;
    private boolean mReverseShow;
    private int mType = TYPE_HOME;

    public TopicItemAdapter(Context context, int type) {
        this.mContext = context;
        this.mReverseShow = (type == TYPE_HOME);
        this.mType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicItemViewHolder(LayoutInflater.
                from(mContext).inflate(R.layout.item_topic_item, parent, false));
    }

    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);

        HotTopic hotTopic = getItem(position);
        if (hotTopic == null) {
            return;
        }

        switch (mType) {
            case TYPE_HOME:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10241);
                break;
            default:
                break;
        }

        TopicRoomListActivity.startActivity(mContext, hotTopic.getId(), hotTopic.getName(), AnalyticsReport.HOME_VIDEO_VIEW_ID);
    }

    private class TopicItemViewHolder extends BaseAdapter.ViewHolder<HotTopic> {
        private TextView mTitleTv;
        private TextView mNumberTv;
        private SimpleDraweeView mThumbSdv;
        private ImageView mIconIv;
        private LinearLayout mInfoLl;

        TopicItemViewHolder(View itemView) {
            super(itemView);

            mTitleTv = (TextView) itemView.findViewById(R.id.title_tv);
            mNumberTv = (TextView) itemView.findViewById(R.id.number_tv);
            mThumbSdv = (SimpleDraweeView) itemView.findViewById(R.id.thumb_sdv);
            mIconIv = (ImageView) itemView.findViewById(R.id.icon_iv);
            mInfoLl = (LinearLayout) itemView.findViewById(R.id.info_ll);

            if (mType == TYPE_TOPIC) {
                int height = (Utils.getScreenWidth(mContext) - Utils.dip2px(mContext, 45)) / 14;

                RelativeLayout.LayoutParams thumbParams = (RelativeLayout.LayoutParams) mThumbSdv.getLayoutParams();
                thumbParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                thumbParams.height = height * 5;
                mThumbSdv.setLayoutParams(thumbParams);

                RelativeLayout.LayoutParams infoParams = (RelativeLayout.LayoutParams) mInfoLl.getLayoutParams();
                infoParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                infoParams.height = height * 4;
                mInfoLl.setLayoutParams(infoParams);

                RelativeLayout.LayoutParams iconParams = (RelativeLayout.LayoutParams) mIconIv.getLayoutParams();
                iconParams.topMargin = height * 5 - iconParams.height / 2;
                iconParams.removeRule(RelativeLayout.CENTER_VERTICAL);
                mIconIv.setLayoutParams(iconParams);
            }
        }

        @Override
        public void update(BaseAdapter adapter, int position, HotTopic hotTopic) {
            if (hotTopic == null) {
                return;
            }

            ImageUtil.loadImage(mThumbSdv, hotTopic.getCoverUrl());
            mTitleTv.setText(hotTopic.getName());
            mNumberTv.setText(ResourceHelper.getString(R.string.session_number, hotTopic.getCourseCount()));

            if (!mReverseShow) {
                return;
            }

            RelativeLayout.LayoutParams infoParams = (RelativeLayout.LayoutParams) mInfoLl.getLayoutParams();
            RelativeLayout.LayoutParams thumbParams = (RelativeLayout.LayoutParams) mThumbSdv.getLayoutParams();
            if (Utils.judgeOdd(position)) {
                infoParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                infoParams.removeRule(RelativeLayout.BELOW);
                thumbParams.addRule(RelativeLayout.BELOW, R.id.info_ll);
                thumbParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
//                mIconIv.setRotation(0f);
            } else {
                thumbParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                thumbParams.removeRule(RelativeLayout.BELOW);
                infoParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                infoParams.addRule(RelativeLayout.BELOW, R.id.thumb_sdv);
//                mIconIv.setRotation(180f);
            }
        }
    }

}