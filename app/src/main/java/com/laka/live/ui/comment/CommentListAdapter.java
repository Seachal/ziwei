package com.laka.live.ui.comment;

import android.content.Context;
import android.view.ViewGroup;

import com.laka.live.bean.CommentInfo;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.video.model.bean.MiniVideoBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommentListAdapter
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 20/09/2017
 */

public class CommentListAdapter extends BaseAdapter<CommentInfo, BaseAdapter.ViewHolder> {
    private final static String TAG = CommentDetailAdapter.class.getSimpleName();

    private Course mCourse;
    private MiniVideoBean mVideo;
    private Context mContext;

    public CommentListAdapter(Context context) {
        mContext = context;
    }

    public void setCourse(Course course) {
        mCourse = course;
    }

    public void setVideo(MiniVideoBean video) {
        this.mVideo = video;
    }

    public void setData(List<CommentInfo> datas) {
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentViewHolder viewHolder = new CommentViewHolder(mContext, CommentViewHolder.TYPE_COMMENT_REPLY);
        viewHolder.setCourse(mCourse);
        viewHolder.setVideo(mVideo);
        return viewHolder;
    }

    @Override
    public void onItemClick(int position) {
        super.onItemClick(position);
        CommentInfo commentInfo = mDatas.get(position);
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(Common.REPLY_ID, commentInfo.getId());
        eventMap.put(Common.NICK_NAME, commentInfo.getNickname());

        Log.d(TAG, "onItemClick ." + " ; commentId : " + commentInfo.getId()
                + " ; nickName : " + commentInfo.getNickname());
        EventBusManager.postEvent(eventMap, SubcriberTag.REPLY_COMMENT);

    }
}
