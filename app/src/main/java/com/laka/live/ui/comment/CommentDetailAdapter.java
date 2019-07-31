package com.laka.live.ui.comment;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.laka.live.bean.BaseComment;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.Common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommentDetailAdapter
 * @Description: 回复详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 1/14/17
 */

public class CommentDetailAdapter extends BaseAdapter<BaseComment, BaseAdapter.ViewHolder> {
    private final static String TAG = CommentDetailAdapter.class.getSimpleName();

    private Context mContext;
    private int ownerUserId;

    public CommentDetailAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<BaseComment> datas) {
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentViewHolder viewHolderReply = new CommentViewHolder(mContext, CommentViewHolder.TYPE_REPLY);
        viewHolderReply.setOwnerUserId(ownerUserId);
        return viewHolderReply;
    }

    @Override
    public void onItemClick(int position) {
        super.onItemClick(position);
        BaseComment reply = mDatas.get(position);
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(Common.REPLY_ID, reply.getId());
        eventMap.put(Common.NICK_NAME, reply.getNickname());

        Log.d(TAG, "onItemClick ." + " ; commentId : " + reply.getId()
                + " ; nickName : " + reply.getNickname());
        EventBusManager.postEvent(eventMap, SubcriberTag.REPLY_COMMENT);
    }

    public void setBaseComment(BaseComment mComment) {
        ownerUserId = mComment.getUserId();
    }
}
