package com.laka.live.ui.widget.comment;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.BaseComment;
import com.laka.live.bean.CommentInfo;
import com.laka.live.bean.Course;
import com.laka.live.bean.ReplyInfo;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.comment.CommentDetailActivity;
import com.laka.live.ui.comment.CommentViewHolder;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.bean.VideoCommentInfo;

import java.util.List;

/**
 * @ClassName: CommentItemView
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 19/09/2017
 */

public class CommentItemView extends LinearLayout {
    private final static String KEY = "comment_";

    public final static int TYPE_COMMENT_REPLY = 0;

    private final static int PRAISE_COMMENT = 1;  //点赞评论
    private final static int UN_PRAISE_COMMENT = 2;  //取消点赞

    private final static int PX_15 = Utils.dip2px(LiveApplication.getInstance(), 15);

    private Context mContext;

    //通用属性
    private SimpleDraweeView mAvatar;
    private TextView mNickNameTv;
    private TextView mCreateTimeTv;
    private CheckBox mLikeCb;
    private ExpandTextView mContentEtv;
    private View mDivider;

    //视频详情页面， 回复属性
    private LinearLayout mReplyLl;
    private ReplyListView mReplyRlv;
    private TextView mReplyMoreTv;
    private boolean isEnablePrise = true;

    private Course mCourse;
    private MiniVideoBean mVideo;
    private BaseComment mBaseComment;

    /**
     * description:点赞回调
     **/
    private GsonHttpConnection.OnResultListener requestCallBack;

    private int mType = 0;

    private boolean isPraised = false;

    public CommentItemView(Context context, int type) {
        super(context);
        init();

        this.mType = type;
        this.mContext = context;

        initView();
    }

    private void init() {
        setOrientation(VERTICAL);
        setBackgroundColor(ResourceHelper.getColor(R.color.white));
        setPadding(PX_15, PX_15, PX_15, 0);

        setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_item_comment, this);

        mAvatar = findViewById(R.id.avatar_sdv);
        mNickNameTv = findViewById(R.id.nickname_tv);
        mCreateTimeTv = findViewById(R.id.time_tv);

        mLikeCb = findViewById(R.id.like_cb);
        mContentEtv = findViewById(R.id.content_etv);

        mReplyLl = findViewById(R.id.reply_ll);
        mReplyRlv = findViewById(R.id.reply_rlv);
        mReplyMoreTv = findViewById(R.id.more_tv);

        mDivider = findViewById(R.id.divider);
    }

    public void update(BaseComment baseComment, Course course) {
        this.mCourse = course;
        update(baseComment);
    }

    public void update(BaseComment baseComment, MiniVideoBean miniVideoBean) {
        this.mVideo = miniVideoBean;
        enablePrise(false);
        update(baseComment);
    }

    public void update(BaseComment baseComment) {
        if (baseComment == null) {
            setVisibility(GONE);
            return;
        }

        this.mBaseComment = baseComment;

        setVisibility(VISIBLE);

        final String key = KEY + mBaseComment.getId();

        isPraised = UiPreference.getBoolean(key, false);

        ImageUtil.loadImage(mAvatar, mBaseComment.getAvatar());
        mNickNameTv.setText(mBaseComment.getNickname());
        mCreateTimeTv.setText(Utils.getCommentDate(mBaseComment.getCerateTime()));
        mLikeCb.setText(String.valueOf(mBaseComment.getPraiseCount()));
        mLikeCb.setChecked(isPraised);
        mLikeCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    mLikeCb.setChecked(false);
                    LoginActivity.startActivity(mContext, LoginActivity.TYPE_FROM_NEED_LOGIN);
                    return;
                }

                int praiseCount = mBaseComment.getPraiseCount();
                if (isPraised) {
                    mLikeCb.setChecked(false);
                    mLikeCb.setText(String.valueOf(praiseCount - 1));
                    mBaseComment.setPraiseCount(praiseCount - 1);
                } else {
                    mLikeCb.setChecked(true);
                    mLikeCb.setText(String.valueOf(praiseCount + 1));
                    mBaseComment.setPraiseCount(praiseCount + 1);
                }

                if (mCourse != null) {
                    DataProvider.praiseComment(mContext, mBaseComment.getId(),
                            isPraised ? UN_PRAISE_COMMENT : PRAISE_COMMENT,
                            requestCallBack);
                }

                if (mVideo != null) {
                    DataProvider.praiseVideoComment(mContext, mVideo.getVideoId(), requestCallBack);
                }

            }
        });

        requestCallBack = new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                ToastHelper.showToast(isPraised ? "取消点赞成功" : "点赞成功");

                isPraised = !isPraised;
                UiPreference.putBoolean(key, isPraised);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                ToastHelper.showToast(isPraised ? "取消点赞失败" : "点赞失败");

                int praiseCount = mBaseComment.getPraiseCount();
                if (!isPraised) {
                    mLikeCb.setChecked(false);
                    mLikeCb.setText(String.valueOf(praiseCount - 1));
                    mBaseComment.setPraiseCount(praiseCount - 1);
                } else {
                    mLikeCb.setChecked(true);
                    mLikeCb.setText(String.valueOf(praiseCount + 1));
                    mBaseComment.setPraiseCount(praiseCount + 1);
                }
            }
        };

        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.startActivity(mContext, String.valueOf(mBaseComment.getUserId()));
            }
        });

        switch (mType) {
            case TYPE_COMMENT_REPLY:
                showReplyList(mBaseComment);
                break;
            default:
                showReply(mBaseComment);
                break;

        }

    }

    private void showReplyList(final BaseComment comment) {
        mReplyLl.setVisibility(View.VISIBLE);

        mContentEtv.setText(comment.getContent(), true);

        if (!(comment instanceof CommentInfo) && !(comment instanceof VideoCommentInfo)) {
            return;
        }

        CommentInfo commentInfo = (CommentInfo) comment;
        final List<ReplyInfo> list = commentInfo.getReplys();

        if (commentInfo.getReplyCount() > 0) {
            mReplyMoreTv.setVisibility(View.VISIBLE);
            mReplyMoreTv.setText(ResourceHelper.getString(
                    R.string.look_more_reply, commentInfo.getReplyCount()));
            mReplyMoreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCourse != null) {
                        CommentDetailActivity.startActivity(mContext, mCourse, comment);
                    }

                    if (mVideo != null) {
                        CommentDetailActivity.startActivity(mContext, mVideo, comment);
                    }
                }
            });
        } else {
            mReplyMoreTv.setVisibility(View.GONE);
        }

        if (!Utils.listIsNullOrEmpty(list)) {
            mReplyRlv.setVisibility(View.VISIBLE);
            mReplyRlv.setDatas(list);
            mReplyRlv.setOnItemClickListener(new ReplyListView.OnItemClickListener() {
                @Override
                public void onItemClick(int itemPosition) {
                    if (mCourse != null) {
                        CommentDetailActivity.startActivity(mContext, mCourse, comment);
                    }

                    if (mVideo != null) {
                        CommentDetailActivity.startActivity(mContext, mVideo, comment);
                    }
                }
            });

        } else {
            mReplyRlv.setVisibility(View.GONE);
            mReplyRlv.setDatas(list);
        }
    }

    private void showReply(BaseComment comment) {
        mReplyLl.setVisibility(View.GONE);

        if (!(comment instanceof ReplyInfo)) {
            return;
        }

        ReplyInfo reply = (ReplyInfo) comment;

        SpannableStringBuilder replyBuilder = new SpannableStringBuilder();

        String toNickname = reply.getToNickname();

        if (!Utils.isEmpty(toNickname) && reply.getParentId() != reply.getReplyId()) {
//            if(mType!=CommentViewHolder.TYPE_REPLY){
            boolean isNeedReplyUser = true;
            if (mType == CommentViewHolder.TYPE_REPLY) {
                if (reply.getUserId() == ownerUserId) {
                    isNeedReplyUser = false;
                }
            }
            if (isNeedReplyUser) {
                replyBuilder.append(ResourceHelper.getString(R.string.reply_r_blank))
                        .append(" ").append(toNickname).append(": ");
                replyBuilder.setSpan(new ForegroundColorSpan(ResourceHelper.getColor(R.color.colorAAAAAA)),
                        0, toNickname.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        replyBuilder.append(MoonUtil.replaceEmoticons(mContext, reply.getContent(), ImageSpan.ALIGN_BOTTOM, MoonUtil.SMALL_SCALE));
        mContentEtv.setText(replyBuilder, false);
    }

    int ownerUserId = 0;

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    private void praiseComment(int commentId, int flag) {
        DataProvider.praiseComment(mContext, commentId, flag, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                ToastHelper.showToast("点赞成功");
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }

    public void enablePrise(boolean isEnablePrise) {
        this.isEnablePrise = isEnablePrise;
        if (!isEnablePrise) {
            mLikeCb.setVisibility(GONE);
        }
    }
}
