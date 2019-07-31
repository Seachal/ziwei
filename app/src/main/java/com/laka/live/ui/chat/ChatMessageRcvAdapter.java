package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.ChatEntity;
import com.laka.live.config.SystemConfig;
import com.laka.live.dao.DbManger;
import com.laka.live.gift.GiftResManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.HorizontalDragLayout;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;

import java.util.List;

import laka.live.bean.ChatMsg;

/**
 * Created by luwies on 16/7/7.
 */
public class ChatMessageRcvAdapter extends BaseAdapter<ChatMsg, ChatMessageRcvAdapter.ViewHolder> {
    private static final String TAG = "ChatMessageRcvAdapter";
    private int mTitlePosition = 0;
    public String otherAvatar, myAvatar;
    public int sessionType;
    public int mishuType = -1;
    public Activity mActivity;
    private Context mContext;
    ChatMsg lastData;
    public int TYPE_MSG_LEFT = 0, TYPE_MSG_RIGHT = 1, TYPE_GUANFANG = 2, TYPE_MISHU_ATTENTION = 3, TYPE_MISHU_SYSTEM = 4, TYPE_MISHU = 5;

//    public ChatMessageRcvAdapter(Context context, List<ChatMsg> datas, int sessionType) {
//        mContext = context;
//        mDatas = datas;
//        this.sessionType = sessionType;
//    }

    private ChatMessageView.OnChatItemClickListener listener;

    public ChatMessageRcvAdapter(Context context, List<ChatMsg> datas, int sessionType, ChatMessageView.OnChatItemClickListener listener) {
        mContext = context;
        mDatas = datas;
        this.sessionType = sessionType;
        this.listener = listener;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMsg item = getItem(position);
//        Log.d(TAG,"getItemViewType sessionType="+sessionType+" getIsSend="+item.getIsSend());
        if (sessionType == DbManger.SESSION_TYPE_FOLLOW || sessionType == DbManger.SESSION_TYPE_UNFOLLOW || sessionType == DbManger.SESSION_TYPE_STRANGER) {
            if (item.getIsSend() != null && item.getIsSend()) {
                return TYPE_MSG_RIGHT;
            } else {
                return TYPE_MSG_LEFT;
            }
        } else if (sessionType == DbManger.SESSION_TYPE_MISHU) {
            if (mishuType == DbManger.TYPE_CHAT_MISHU_ATTENTION) {
                if (item.getType() == DbManger.TYPE_CHAT_MISHU_SYSTEM) {
                    return TYPE_MISHU;
                } else {
                    return TYPE_MISHU_ATTENTION;
                }
            } else {
                return TYPE_MISHU_SYSTEM;
            }
        } else if (sessionType == DbManger.SESSION_TYPE_GUANFANG) {
            return TYPE_GUANFANG;
        }
        return item.getType();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseAdapter.ViewHolder inner = null;
        if (viewType == TYPE_MSG_LEFT) {
            inner = new MsgHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_list_left, null));
        } else if (viewType == TYPE_MSG_RIGHT) {
            inner = new MsgHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_list_right, null));
        } else if (viewType == TYPE_GUANFANG) {
//            inner = new GuanfangHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_guanfang, null));
            inner = new MsgHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_list_left, null));
        } else if (viewType == TYPE_MISHU) {
            inner = new MishuSystemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_session, null));
        } else if (viewType == TYPE_MISHU_ATTENTION) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_mishu, null);
//            ViewGroup.LayoutParams lp =  view.getLayoutParams();
//            Log.d(TAG, " lp高度设置前=" + lp.height);
//            lp.height = Utils.dip2px(mContext, 66);
//            view.setLayoutParams(lp);
//            Log.d(TAG, " lp高度设置后=" + lp.height);
            inner = new MishuHolder(view);
        } else if (viewType == TYPE_MISHU_SYSTEM) {
//            inner = new GuanfangHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_guanfang, null));
            inner = new MsgHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_list_left, null));
        }
        return new ViewHolder(inner);
    }

    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);
    }

    public class ViewHolder extends BaseAdapter.ViewHolder<ChatMsg> {

        private BaseAdapter.ViewHolder innerHolder;

        public ViewHolder(BaseAdapter.ViewHolder holder) {
            super(holder.itemView);
//            if (holder instanceof MishuHolder) {
//                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
//                Log.d(TAG, " lp高度设置前=" + lp.height);
//                lp.height = Utils.dip2px(mContext, 66);
//                holder.itemView.setLayoutParams(lp);
//                Log.d(TAG, " lp高度设置后=" + lp.height);
//            }
            innerHolder = holder;
        }

        @Override
        public void update(BaseAdapter adapter, int position, ChatMsg chatMsg) {
            if (innerHolder != null) {
                innerHolder.update(adapter, position, chatMsg);
            }
        }
    }

    public class GuanfangHolder extends BaseAdapter.ViewHolder<ChatMsg> {
        TextView tvContent, tvDate, tvName;

        public GuanfangHolder(View v) {
            super(v);
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            tvName = (TextView) v.findViewById(R.id.tv_name);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ChatMsg data) {
            tvName.setText(data.getNickName());
            tvContent.setText(data.getContent());
            tvDate.setText(Utils.getChatDate(data.getDate()));
        }
    }

    public class MishuSystemViewHolder extends BaseAdapter.ViewHolder<ChatMsg> {
        SimpleDraweeView face;
        TextView name;
        LevelText level;
        ImageView ivRed;
        TextView tvDate;
        TextView tvUnreadCnt;
        View divider;
        ImageView ivGender;
        TextView desc;

        public MishuSystemViewHolder(View itemView) {
            super(itemView);

            if (itemView instanceof HorizontalDragLayout) {
                ((HorizontalDragLayout) itemView).setIsDragEnable(false);
            }

            face = (SimpleDraweeView) itemView.findViewById(R.id.user_face);
            ivRed = (ImageView) itemView.findViewById(R.id.iv_red);
            name = (TextView) itemView.findViewById(R.id.name);
            level = (LevelText) itemView.findViewById(R.id.level);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvUnreadCnt = (TextView) itemView.findViewById(R.id.tv_unread_cnt);
            ivGender = (ImageView) itemView.findViewById(R.id.iv_gender);
            desc = (TextView) itemView.findViewById(R.id.desc);
            divider = itemView.findViewById(R.id.divider);
        }

        @Override
        public void update(BaseAdapter adapter, int position, final ChatMsg chatMsg) {
            tvDate.setText(Utils.getChatDate(chatMsg.getDate()));
            ImageUtil.loadResImage(R.drawable.dm_icon_ring, face);
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.drawable.rank_icon_degree);
            level.setVisibility(View.GONE);
            name.setTextColor(ResourceHelper.getColor(R.color.colorFFA100));
            tvUnreadCnt.setVisibility(View.GONE);
            if (chatMsg.getIsUnread()) {
                ivRed.setVisibility(View.GONE);
            } else {
                ivRed.setVisibility(View.GONE);
            }

            name.setText(ResourceHelper.getString(R.string.laka_mishu));
            level.setVisibility(View.GONE);

            if (desc != null && !Utils.isEmpty(chatMsg.getContent())) {
                desc.setText(chatMsg.getContent());
            }

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) divider.getLayoutParams();
            layoutParams.leftMargin = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.user_item_divider_margin_left);

            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 0 && chatMsg.getType() == DbManger.TYPE_CHAT_MISHU_SYSTEM) {
                        chatMsg.setIsUnread(false);
                        ChatMessageActivity.startPrivateChatActivity(
                                mContext, chatMsg.getUserId(), ResourceHelper.getString(R.string.laka_mishu), chatMsg.getAvatar(), DbManger.SESSION_TYPE_MISHU, DbManger.TYPE_CHAT_MISHU_SYSTEM);
                    }
                }
            });
        }
    }

    public class MishuHolder extends BaseAdapter.ViewHolder<ChatMsg> {
        MarkSimpleDraweeView ivAvatar;
        TextView tvContent, tvDate;
        View divider;
        View view;

        public MishuHolder(View v) {
            super(v);
            ivAvatar = (MarkSimpleDraweeView) v.findViewById(R.id.user_face);
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            divider = v.findViewById(R.id.divider);

            view = v;
        }


        @Override
        public void update(BaseAdapter adapter, int position, final ChatMsg data) {
            if (itemView != null) {
//                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
//                Log.d(TAG, " lp高度设置前=" + lp.height);
//                lp.height = Utils.dip2px(mContext, 66);
//                itemView.setLayoutParams(lp);
//                Log.d(TAG, " lp高度设置后=" + lp.height);
//                itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(mContext, 66)));
            }
            if (Utils.isEmpty(data.getAvatar())) {
                ImageUtil.loadResImage(R.drawable.dm_icon_ring, ivAvatar);
            } else {
                ImageUtil.loadImage(ivAvatar, data.getAvatar());
            }
            ivAvatar.setTag(data.getNickName());
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = (String) v.getTag();
                    if (!Utils.isEmpty(userId) && (!"0".equals(userId) && !DbManger.SESSION_ID_LAKA_GUANFANG.equals(userId))) {
                        UserInfoActivity.startActivity(mActivity, String.valueOf(userId));
                    }
                }
            });

            tvContent.setText(data.getContent());
            tvDate.setText(Utils.getChatDate(data.getDate()));

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) divider.getLayoutParams();
            if (position == getItemCount() - 1) {
                layoutParams.leftMargin = 0;
            } else {
                layoutParams.leftMargin = mContext.getResources()
                        .getDimensionPixelSize(R.dimen.user_item_divider_margin_left);
            }
        }
    }


    public class MsgHolder extends BaseAdapter.ViewHolder<ChatMsg> {
        View rl_container;
        TextView tv_date;
        SimpleDraweeView img_avatar;
        SimpleDraweeView img_chatimage;
        TextView tv_chatcontent, tvGiftContent, tvGoodDetail;
        SimpleDraweeView ivGift;
        ImageView img_sendfail;
        ProgressBar progress;
        RelativeLayout layout_content;
        RelativeLayout rlItemChat, rlItemGift;

        public MsgHolder(View v) {
            super(v);
            rl_container = v.findViewById(R.id.rl_container);
            layout_content = (RelativeLayout) v.findViewById(R.id.chat_item_layout_content);
            img_avatar = (SimpleDraweeView) v.findViewById(R.id.chat_item_avatar);
            img_chatimage = (SimpleDraweeView) v.findViewById(R.id.chat_item_content_image);
            img_sendfail = (ImageView) v.findViewById(R.id.chat_item_fail);
            progress = (ProgressBar) v.findViewById(R.id.chat_item_progress);
            tv_chatcontent = (TextView) v.findViewById(R.id.chat_item_content_text);
            tv_date = (TextView) v.findViewById(R.id.chat_item_date);
            rlItemChat = (RelativeLayout) v.findViewById(R.id.rl_item_chat);
            rlItemGift = (RelativeLayout) v.findViewById(R.id.rl_item_gift);
            ivGift = (SimpleDraweeView) v.findViewById(R.id.iv_gift);
            tvGiftContent = (TextView) v.findViewById(R.id.tv_gift_content);
            tvGoodDetail = (TextView) v.findViewById(R.id.tv_good_detail);
            tvGoodDetail.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        }

        @Override
        public void update(BaseAdapter adapter, final int position, ChatMsg data) {
            if (data.getType() == null || data.getType() == ChatEntity.MSG_TYPE_TEXT || data.getType() == ChatEntity.MSG_TYPE_PICTURE || sessionType == DbManger.SESSION_TYPE_MISHU) {
                rlItemChat.setVisibility(View.VISIBLE);
                rlItemGift.setVisibility(View.GONE);
            } else if (data.getType() == ChatEntity.MSG_TYPE_GIFT) {
                rlItemChat.setVisibility(View.GONE);
                rlItemGift.setVisibility(View.VISIBLE);
            }

            //5分钟内不显示
            if (position > 0) {
                lastData = getItem(position - 1);
            }
            if (position == 0) {
                tv_date.setVisibility(View.VISIBLE);
                tv_date.setText(Utils.getChatDate(data.getDate()));
            } else if (position > 0 && data.getDate() - lastData.getDate() > 60 * 5) {//* 1000
                tv_date.setVisibility(View.VISIBLE);
                tv_date.setText(Utils.getChatDate(data.getDate()));
            } else {
                tv_date.setVisibility(View.GONE);
            }
//            Log.d(TAG, " data.getDate() - lastData.getDate()=" + (data.getDate() - lastData.getDate()));
            if (tv_date.getVisibility() == View.VISIBLE) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_date.getLayoutParams();
                if (position == 0) {
                layoutParams.topMargin = Utils.dip2px(mContext, 10f);
                } else {
                    layoutParams.topMargin = 0;
                }
            }

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_container.getLayoutParams();
            if (position == getItemCount() - 1) {
                layoutParams.bottomMargin = Utils.dip2px(mContext, 10f);
            } else {
                layoutParams.bottomMargin = 0;
            }
            tvGoodDetail.setVisibility(View.GONE);
            //如果是文本类型，则隐藏图片，如果是图片则隐藏文本
            if (data.getType() == null || data.getType() == ChatEntity.MSG_TYPE_TEXT || sessionType == DbManger.SESSION_TYPE_MISHU) {
                img_chatimage.setVisibility(View.GONE);
                tv_chatcontent.setVisibility(View.VISIBLE);

                String content = data.getContent();
                if (!Utils.isEmpty(content) && content.startsWith("[商品]")) {
                    int indexTitle = content.indexOf("title:");
                    int indexId = content.indexOf("id:");
                    String title = content.substring(indexTitle+6, indexId-1);
                    final String goodId = content.substring(indexId+3);
                    Log.d(TAG, " indexTitle=" + indexTitle + " indexId=" + indexId +
                            " title=" + title + " goodId=" + goodId);
                    MoonUtil.identifyFaceExpression(mContext, tv_chatcontent, new String(Base64.decode(title.getBytes(), Base64.DEFAULT)), ImageSpan.ALIGN_BASELINE, MoonUtil.SMALL_SCALE);
                    tvGoodDetail.setVisibility(View.VISIBLE);
                    tvGoodDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBusManager.postEvent(goodId, SubcriberTag.GO_SHOP_GOOD_DETAIL);
                        }
                    });
                }else  if(!Utils.isEmpty(content) && content.startsWith("[课程]")) {
                    int indexTitle = content.indexOf("title:");
                    int indexId = content.indexOf("id:");
                    String title = content.substring(indexTitle+6, indexId-1);
                    final String goodId = content.substring(indexId+3);
                    Log.d(TAG, " indexTitle=" + indexTitle + " indexId=" + indexId +
                            " title=" + title + " goodId=" + goodId);
                    MoonUtil.identifyFaceExpression(mContext, tv_chatcontent, new String(Base64.decode(title.getBytes(), Base64.DEFAULT)), ImageSpan.ALIGN_BASELINE, MoonUtil.SMALL_SCALE);
                    tvGoodDetail.setVisibility(View.VISIBLE);
                    tvGoodDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBusManager.postEvent(goodId, SubcriberTag.GO_COURSE_DETAIL);
                        }
                    });
                }

                else {
                    MoonUtil.identifyFaceExpression(mContext, tv_chatcontent, data.getContent(), ImageSpan.ALIGN_BASELINE, MoonUtil.SMALL_SCALE);
                }

            } else if (data.getType() == ChatEntity.MSG_TYPE_PICTURE) {
                tv_chatcontent.setVisibility(View.GONE);
                img_chatimage.setVisibility(View.VISIBLE);
                String uri = null;
                ImageUtil.loadImage(img_chatimage, uri);
            } else {
                String giftId = String.valueOf(data.getGiftId());
                boolean isSend = data.getIsSend() == null ? false : data.getIsSend();
                //文字
                tvGiftContent.setText("送给" + (isSend ? "TA" : "你") + GiftResManager.getInstance().getUnitByGiftId(giftId) + GiftResManager.getInstance().getNameByGiftId(giftId));

                //图片
                int giftRes = GiftResManager.getInstance().getChatResByGiftId(giftId);
                String giftUrl = GiftResManager.getInstance().getImageResByGiftId(giftId);
                if (giftRes > 0) {
                    ivGift.setVisibility(View.VISIBLE);
//                  ivGift.setImageResource(giftRes);
                    ImageUtil.loadResImage(giftRes, ivGift);
                } else if (!Utils.isEmpty(giftUrl)) {
                    ivGift.setVisibility(View.VISIBLE);
                    ImageUtil.loadImage(ivGift, giftUrl);
                } else {
                    ivGift.setVisibility(View.INVISIBLE);
                }
            }

            //显示头像
            if (sessionType == DbManger.SESSION_TYPE_GUANFANG) {
                ImageUtil.loadResImage(R.mipmap.ic_launcher, img_avatar);
            } else if (sessionType == DbManger.SESSION_TYPE_MISHU) {
                ImageUtil.loadResImage(R.drawable.dm_icon_ring, img_avatar);
            } else {
                if (data.getIsSend()) {
                    ImageUtil.loadImage(img_avatar, myAvatar);
                } else {
                    ImageUtil.loadImage(img_avatar, otherAvatar);
                }
            }

            //显示状态
            if (data.getIsSend()) {
                int state = data.getState() == null ? ChatEntity.MSG_STATE_SUCCESS : data.getState();
                switch (state) {
                    case ChatEntity.MSG_STATE_SENDING:
                        progress.setVisibility(View.VISIBLE);
                        img_sendfail.setVisibility(View.GONE);
                        break;
                    case ChatEntity.MSG_STATE_FAIL:
                        progress.setVisibility(View.GONE);
                        img_sendfail.setVisibility(View.VISIBLE);
                        img_sendfail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.onFailedClick(position);
                            }
                        });
                        break;
                    default:
                        progress.setVisibility(View.GONE);
                        img_sendfail.setVisibility(View.GONE);
                        break;

                }
            } else {
                progress.setVisibility(View.GONE);
                img_sendfail.setVisibility(View.GONE);
                setLeftHeadIconClick(img_avatar, data);
            }
        }
    }


    public void setLeftHeadIconClick(SimpleDraweeView headIcon, final ChatMsg data) {
        if (data == null) {
            return;
        }
        if (data.getIsSend()) {
            return;
        }
        headIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getUserId().equals(SystemConfig.getInstance().getKefuID())){
                    return;
                }
                if (mActivity != null && StringUtils.isNotEmpty(data.getUserId())) {
                    UserInfoActivity.startActivity(mActivity, data.getUserId());
                }
            }
        });
    }
}
