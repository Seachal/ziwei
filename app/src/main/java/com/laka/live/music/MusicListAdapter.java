package com.laka.live.music;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.MusicInfo;
import com.laka.live.download.DownloadState;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.HorizontalDragLayout;
import com.laka.live.ui.widget.MusicDownLoadBtn;
import com.laka.live.util.ToastHelper;

import java.io.File;
import java.util.List;

/**
 * Created by luwies on 16/8/11.
 */
public class MusicListAdapter extends BaseAdapter<MusicInfo, MusicListAdapter.ViewHolder> {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private OnRemoveListener mOnRemoveListener;

    private boolean isDragEnable;

    private Context mContext;

    public MusicListAdapter(boolean isDragEnable, Context mContext) {
        this.isDragEnable = isDragEnable;
        this.mContext = mContext;
    }

    public void setOnRemoveListener(OnRemoveListener listener) {
        mOnRemoveListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_layout, null);
        if (view instanceof HorizontalDragLayout) {
            ((HorizontalDragLayout) view).setIsDragEnable(isDragEnable);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        View view = holder.itemView;
        if (view instanceof HorizontalDragLayout) {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRemove(position);
                }
            });
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(position);
                }
            });
        }

    }

    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);
        MusicInfo musicInfo = getItem(position);
        DownloadState state = musicInfo.getState();
        switch (state) {
            case NONE:
                //download
                download(musicInfo, position);
                break;
            case STARTED:
            case WAITING:
                //stop
                stopDownload(musicInfo, position);
                break;
            case FINISHED:
                AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11258);
                //go to play
                MusicManager.getInstance().playMusic(musicInfo);
                break;
            case STOPPED:
            case ERROR:
                //continue
                download(musicInfo, position);
                break;

        }

    }

    private void download(MusicInfo musicInfo, int position) {

        if (musicInfo != null && checkDownloadUrl(musicInfo.getUrl())) {
            MusicManager.getInstance().download(musicInfo);
            notifyItemChanged(position);
        } else {
            ToastHelper.showToast(R.string.live_manager_data_error_code);
        }
    }

    private static boolean checkDownloadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        return HTTP.equals(scheme) || HTTPS.equals(scheme);
    }

    private void stopDownload(MusicInfo musicInfo, int position) {
        if (musicInfo != null) {
            MusicManager.getInstance().stopDownload(musicInfo);
            notifyItemChanged(position);
        }
    }

    private void onRemove(int position) {
        MusicInfo musicInfo = mDatas.remove(position);
//        MusicManager.getInstance().stopDownload(musicInfo);
        notifyDataSetChanged();
        if (mOnRemoveListener != null) {
            mOnRemoveListener.onRemove();
        }
        MusicManager.getInstance().deleteMusic(musicInfo);
    }

    public void updateState() {
        List<MusicInfo> list = getAll();
        boolean isMusicFileExists;
        boolean isLyricsFileExists;
        for (MusicInfo musicInfo : list) {
            if (musicInfo != null) {
                DownloadState state = musicInfo.getState();
                if (state == DownloadState.FINISHED) {

                    String musicTempFile = musicInfo.getMusicFilePath();
                    String lyricsTempFile = musicInfo.getLyricsFilePath();
                    isMusicFileExists = TextUtils.isEmpty(musicTempFile) ? false : new File(musicTempFile).exists();
                    isLyricsFileExists = TextUtils.isEmpty(lyricsTempFile) ? false : new File(lyricsTempFile).exists();
                    if (isMusicFileExists == false || isLyricsFileExists == false) {
                        musicInfo.setBytesWritten(0L);
                        musicInfo.setTotalBytes(0L);
                        musicInfo.setState(DownloadState.STOPPED);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<MusicInfo> {

        View content;

        private TextView musicName;

        private TextView singer;

        private MusicDownLoadBtn downLoadBtn;

        private View bottomDivider;

        View delete;

        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            musicName = (TextView) itemView.findViewById(R.id.music_name);
            singer = (TextView) itemView.findViewById(R.id.singer);
            downLoadBtn = (MusicDownLoadBtn) itemView.findViewById(R.id.progress_btn);
            bottomDivider = itemView.findViewById(R.id.bottom_divider_line);
            delete = itemView.findViewById(R.id.delete);
        }

        @Override
        public void update(BaseAdapter adapter, int position, MusicInfo musicInfo) {
            int lastIndex = adapter.getItemCount() - 1;
            if (position == 0 && position != lastIndex) {
                bottomDivider.setVisibility(View.GONE);
            } else {
                bottomDivider.setVisibility(View.VISIBLE);
            }
            Context context = musicName.getContext();
            musicName.setText(musicInfo.getTitle());
            singer.setText(context.getString(R.string.singer_content, musicInfo.getSinger()));
            long total = musicInfo.getTotalBytes();
            long written = musicInfo.getBytesWritten();
            int progress;
            if (total <= 0L) {
                progress = 0;
            } else {
                progress = (int) (written * 100L / total);
                if (progress > 100) {
                    progress = 100;
                }
            }
            downLoadBtn.updateState(musicInfo.getState(), progress);
        }
    }

    public interface OnRemoveListener {
        void onRemove();
    }
}
