package com.laka.live.ui.comment;

import android.content.Context;

import com.laka.live.bean.BaseComment;
import com.laka.live.bean.Course;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.comment.CommentItemView;
import com.laka.live.video.model.bean.MiniVideoBean;

/**
 * @ClassName: CommentViewHolder
 * @Description: 评论
 * @Author: chuan
 * @Version: 1.0
 * @Date: 1/14/17
 */

public class CommentViewHolder extends BaseAdapter.ViewHolder<BaseComment> {
    private final static String TAG = CommentViewHolder.class.getSimpleName();

    public final static int TYPE_COMMENT_REPLY = 0;
    public final static int TYPE_REPLY = 1;

    private Course mCourse;
    private MiniVideoBean mVideo;


    public CommentViewHolder(Context context, int type) {
        super(new CommentItemView(context, type));
    }

    public void setCourse(Course course) {
        mCourse = course;
    }

    public void setVideo(MiniVideoBean video) {
        this.mVideo = video;
    }

    @Override
    public void update(BaseAdapter adapter, int position, final BaseComment baseComment) {
        if (baseComment == null || !(itemView instanceof CommentItemView)) {
            return;
        }

        ((CommentItemView) itemView).update(baseComment, mCourse);

        ((CommentItemView) itemView).update(baseComment, mVideo);

    }

    public void setOwnerUserId(int ownerUserId) {
        if (itemView == null || !(itemView instanceof CommentItemView)) {
            return;
        }
        ((CommentItemView) itemView).setOwnerUserId(ownerUserId);

    }
}
