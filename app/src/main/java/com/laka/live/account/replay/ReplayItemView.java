package com.laka.live.account.replay;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.Video;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.TimeUtil;

/**
 * Created by zwl on 2016/7/1.
 * Email-1501448275@qq.com
 */
public class ReplayItemView extends LinearLayout {
    private View mContent;
    private SimpleDraweeView mIcon;
    private TextView mTitle;
    private TextView mTime;
    private View mDivider;
    private CheckBox mCheckBox;
    private TextView mReplayCount;
    private TextView mReplayTagText;
    private VideoParams mVideoParams;
    private OnReplayItemClickListener mListener;

    public ReplayItemView(Context context) {
        this(context, null);
    }

    public ReplayItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplayItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        View view = View.inflate(getContext(), R.layout.item_my_replay, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(view, layoutParams);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && !mVideoParams.isEdit) {
                    mListener.onItemClick(mVideoParams.video);
                }
                if (mCheckBox.isChecked()) {
                    mCheckBox.setChecked(false);
                } else {
                    mCheckBox.setChecked(true);
                }
            }
        });
        mReplayTagText = (TextView) view.findViewById(R.id.live_tag);
        mReplayCount = (TextView) view.findViewById(R.id.replay_count);
        mContent = view.findViewById(R.id.content);
        mIcon = (SimpleDraweeView) view.findViewById(R.id.icon);
        mTitle = (TextView) view.findViewById(R.id.title);
        mTime = (TextView) view.findViewById(R.id.time);
        mDivider = view.findViewById(R.id.divider);
        mCheckBox = (CheckBox) view.findViewById(R.id.replay_checkbox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mVideoParams.isChecked = true;
                    DeleteReplayHelper.getInstance().addVideoToDeleteList(mVideoParams);
                } else {
                    mVideoParams.isChecked = false;
                    DeleteReplayHelper.getInstance().removeVideoFromDeleteList(mVideoParams);
                }

                EventBusManager.postEvent(null, SubcriberTag.REFRESH_DELETE_COUNT);
            }
        });
    }

    public void setData(VideoParams videoParams) {
        if (videoParams == null || videoParams.video == null) {
            return;
        }
        mVideoParams = videoParams;
        setVideoAvatar(videoParams);
        mTitle.setText(videoParams.video.getTitle());
        mTime.setText(TimeUtil.getVideoTime(videoParams.video.getTime()));
        setCheckBox(videoParams);
        setReplayCount(videoParams.video);
        setReplayTag(videoParams.video);
    }

    private void setVideoAvatar(VideoParams videoParams) {
        if (StringUtils.isEmpty(videoParams.video.getCover())) {
            ImageUtil.loadResImage(R.drawable.blank_icon_avatar, mIcon);
            return;
        }
        ImageUtil.loadImage(mIcon, videoParams.video.getCover());
    }

    private void setReplayTag(Video video) {
        if (video.getState() == Video.VIDEO_STATE_TRANS_FINISH) {
            mReplayTagText.setText(ResourceHelper.getString(R.string.replay));
            mReplayTagText.setTextColor(ContextCompat.getColor(getContext(), R.color.color62B6E8));
            mReplayTagText.setBackgroundResource(R.drawable.replay_bg_drawable);
            return;
        }
        mReplayTagText.setText(ResourceHelper.getString(R.string.replay_trans_coding));
        mReplayTagText.setTextColor(Color.WHITE);
        mReplayTagText.setBackgroundResource(R.drawable.replay_saving_bg_drawable);
    }

    private void setCheckBox(VideoParams videoParams) {
        if (videoParams.isEdit) {
            mCheckBox.setVisibility(VISIBLE);
        } else {
            mCheckBox.setVisibility(GONE);
        }
        if (videoParams.isChecked) {
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
    }

    private void setReplayCount(Video video) {
        String text = video.getViews() + ResourceHelper.getString(R.string.replay_count_tip);
        mReplayCount.setText(text);
    }

    public void setOnReplayItemClickListener(OnReplayItemClickListener listener) {
        mListener = listener;
    }

    public interface OnReplayItemClickListener {
        void onItemClick(Video video);
    }
}
