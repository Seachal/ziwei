/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.ChatEntity;

import java.util.ArrayList;
import java.util.List;

import com.laka.live.dao.DbManger;
import com.laka.live.gift.GiftResManager;
import com.laka.live.msg.Msg;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;


import laka.live.bean.ChatMsg;

/**
 * @author kymjs (http://www.kymjs.com/) on 6/8/15.
 */
public class ChatMessageAdapter extends BaseAdapter {

    private static final String TAG ="ChatMessageAdapter";
    private final Context cxt;
    private Activity mActivity;
    private List<ChatMsg> datas = null;
    private ChatMessageView.OnChatItemClickListener listener;
    public String otherAvatar, myAvatar;
    public int sessionType;

    private Handler mUiHandler;

    public ChatMessageAdapter(Context cxt, List<ChatMsg> datas, ChatMessageView.OnChatItemClickListener listener, int sessionType) {
        this.cxt = cxt;
        if (datas == null) {
            datas = new ArrayList<ChatMsg>(0);
        }
        this.datas = datas;
        this.listener = listener;
        this.sessionType = sessionType;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void refresh(List<ChatMsg> datas) {
        if (datas == null) {
            datas = new ArrayList<>(0);
        }
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void superNotifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            superNotifyDataSetChanged();
        } else {
            if (mUiHandler == null) {
                mUiHandler = new Handler(Looper.getMainLooper());
            }
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    superNotifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 0 || position >= datas.size() || datas.get(position) == null) {
            return 0;
        }
        return datas.get(position).getIsSend() ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    ChatMsg lastData;

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        if (sessionType == DbManger.SESSION_TYPE_MISHU) {
            v = getMishuView(position, v);
        } else if (sessionType == DbManger.SESSION_TYPE_GUANFANG) {
            v = getGuanfangView(position, v);
        } else {
            v = getMsgView(position, v);
        }

        return v;
    }


    private View getGuanfangView(final int position, View v) {
        final GuanfangViewHolder holder;
        final ChatMsg data = datas.get(position);
        if (v == null || !(v.getTag() instanceof GuanfangViewHolder)) {
            holder = new GuanfangViewHolder();
            v = View.inflate(cxt, R.layout.item_chat_guanfang, null);
            holder.tvContent = (TextView) v.findViewById(R.id.tv_content);
            holder.tvDate = (TextView) v.findViewById(R.id.tv_date);
            holder.tvName = (TextView) v.findViewById(R.id.tv_name);
            v.setTag(holder);
        } else {
            holder = (GuanfangViewHolder) v.getTag();
        }
        holder.tvName.setText(data.getNickName());
        holder.tvContent.setText(data.getContent());
        holder.tvDate.setText(Utils.getChatDate(data.getDate()));
        return v;
    }

    private View getMishuView(final int position, View v) {
        final MishuViewHolder holder;
        final ChatMsg data = datas.get(position);
        if (v == null || !(v.getTag() instanceof MishuViewHolder)) {
            holder = new MishuViewHolder();
            v = View.inflate(cxt, R.layout.item_chat_mishu, null);
            holder.ivAvatar = (MarkSimpleDraweeView) v.findViewById(R.id.user_face);
            holder.tvContent = (TextView) v.findViewById(R.id.tv_content);
            holder.tvDate = (TextView) v.findViewById(R.id.tv_date);
            v.setTag(holder);
        } else {
            holder = (MishuViewHolder) v.getTag();
        }
//        final int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(info.getStarVerified(),
//                info.getVerified()), info.getLevel(), MarkSimpleDraweeView.SizeType.MIDDLE);
//        face.setMark(markId);
//        ImageUtil.loadImage(holder.ivAvatar, data.getCover());
        if(Utils.isEmpty(data.getAvatar())){
            ImageUtil.loadResImage(R.drawable.dm_icon_ring,holder.ivAvatar);
        }else{
            ImageUtil.loadImage(holder.ivAvatar,data.getAvatar());
        }


        holder.ivAvatar.setTag(data.getNickName());
        holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = (String) v.getTag();
                if(!Utils.isEmpty(userId)&&!"0".equals(userId)){
                    UserInfoActivity.startActivity(mActivity,String.valueOf(userId));
                }
            }
        });


        holder.tvContent.setText(data.getContent());
        holder.tvDate.setText(Utils.getChatDate(data.getDate()));
        return v;
    }


    @NonNull
    private View getMsgView(final int position, View v) {
        final ViewHolder holder;
        final ChatMsg data = datas.get(position);
        if (v == null || !(v.getTag() instanceof ViewHolder)) {
            holder = new ViewHolder();
            if (data.getIsSend()) {
                v = View.inflate(cxt, R.layout.item_chat_list_right, null);
            } else {
                v = View.inflate(cxt, R.layout.item_chat_list_left, null);
            }
            holder.rl_container = v.findViewById(R.id.rl_container);
            holder.layout_content = (RelativeLayout) v.findViewById(R.id.chat_item_layout_content);
            holder.img_avatar = (SimpleDraweeView) v.findViewById(R.id.chat_item_avatar);
            holder.img_chatimage = (SimpleDraweeView) v.findViewById(R.id.chat_item_content_image);
            holder.img_sendfail = (ImageView) v.findViewById(R.id.chat_item_fail);
            holder.progress = (ProgressBar) v.findViewById(R.id.chat_item_progress);
            holder.tv_chatcontent = (TextView) v.findViewById(R.id.chat_item_content_text);
            holder.tv_date = (TextView) v.findViewById(R.id.chat_item_date);
            holder.rlItemChat = (RelativeLayout) v.findViewById(R.id.rl_item_chat);
            holder.rlItemGift = (RelativeLayout) v.findViewById(R.id.rl_item_gift);
            holder.ivGift = (SimpleDraweeView) v.findViewById(R.id.iv_gift);
            holder.tvGiftContent = (TextView) v.findViewById(R.id.tv_gift_content);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (data.getType()==null||data.getType() == ChatEntity.MSG_TYPE_TEXT) {
            holder.rlItemChat.setVisibility(View.VISIBLE);
            holder.rlItemGift.setVisibility(View.GONE);
        } else if (data.getType() == ChatEntity.MSG_TYPE_GIFT) {
            holder.rlItemChat.setVisibility(View.GONE);
            holder.rlItemGift.setVisibility(View.VISIBLE);
        }

        //5分钟内不显示
        if (position > 0)
            lastData = (ChatMsg) getItem(position - 1);

        if (position == 0) {
            holder.tv_date.setVisibility(View.VISIBLE);
            holder.tv_date.setText(Utils.getChatDate(data.getDate()));
        } else if (position > 0 && data.getDate() - lastData.getDate() > 1000 * 60 * 5 * 1000) {
            holder.tv_date.setVisibility(View.VISIBLE);
            holder.tv_date.setText(Utils.getChatDate(data.getDate()));
        } else {
            holder.tv_date.setVisibility(View.GONE);
        }
        if (holder.tv_date.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.tv_date.getLayoutParams();
            if (position == 0) {
                layoutParams.topMargin = Utils.dip2px(cxt, 10f);
            } else {
                layoutParams.topMargin = 0;
            }
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.rl_container.getLayoutParams();
        if (position == getCount() - 1) {
            layoutParams.bottomMargin = Utils.dip2px(cxt, 10f);
        } else {
            layoutParams.bottomMargin = 0;
        }

//        holder.tv_date.setText(StringUtils.friendlyTime(StringUtils.getDataTime("yyyy-MM-dd " +
//                "HH:mm:ss")));

        //如果是文本类型，则隐藏图片，如果是图片则隐藏文本
        if (data.getType() == ChatEntity.MSG_TYPE_TEXT) {
            holder.img_chatimage.setVisibility(View.GONE);
            holder.tv_chatcontent.setVisibility(View.VISIBLE);
//            holder.tv_chatcontent.setText(data.getContent());
            MoonUtil.identifyFaceExpression(cxt, holder.tv_chatcontent, data.getContent(), ImageSpan.ALIGN_BASELINE, MoonUtil.SMALL_SCALE);
//            if (data.getContent().contains("href")) {
//                holder.tv_chatcontent = UrlUtils.handleHtmlText(holder.tv_chatcontent, data
//                        .getContent());
//            } else {
//                holder.tv_chatcontent = UrlUtils.handleText(holder.tv_chatcontent, data
//                        .getContent());
//            }
        } else {
            String giftId = String.valueOf(data.getGiftId());
            boolean isSend = data.getIsSend()!=null?data.getIsSend():true;
            //文字
            holder.tvGiftContent.setText("送给" + (isSend ? "TA" : "你") + GiftResManager.getInstance().getUnitByGiftId(giftId) + GiftResManager.getInstance().getNameByGiftId(giftId));

            //图片
            if(GiftResManager.getInstance().getChatResByGiftId(giftId)>0){
                ImageUtil.loadResImage(GiftResManager.getInstance().getChatResByGiftId(giftId),holder.ivGift);
            }else{
                ImageUtil.loadImage(holder.ivGift,GiftResManager.getInstance().getImageResByGiftId(giftId));
            }


            //如果内存缓存中有要显示的图片，且要显示的图片不是holder复用的图片，则什么也不做，否则显示一张加载中的图片
//            if (kjb.getMemoryCache(data.getContent()) != null && data.getContent() != null &&
//                    data.getContent().equals(holder.img_chatimage.getTag())) {
//            } else {
//                holder.img_chatimage.setImageResource(R.drawable.default_head);
//            }
//            kjb.display(holder.img_chatimage, data.getContent(), 300, 300);
        }

        //如果是表情或图片，则不显示气泡，如果是图片则显示气泡
//        if (data.getType() != ChatEntity.MSG_TYPE_TEXT) {
//            holder.layout_content.setBackgroundResource(android.R.color.transparent);
//        } else {
//            if (data.getIsSend()) {
//                holder.layout_content.setBackgroundResource(R.drawable.chat_to_bg_selector);
//            } else {
//                holder.layout_content.setBackgroundResource(R.drawable.chat_from_bg_selector);
//            }
//        }

        //显示头像
        if (data.getIsSend()) {
            ImageUtil.loadImage(holder.img_avatar, myAvatar);
        } else {
            ImageUtil.loadImage(holder.img_avatar, otherAvatar);
        }

        if (listener != null) {
            holder.tv_chatcontent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTextClick(position);
                }
            });
//            holder.img_chatimage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    switch (data.getType()) {
//                        case ChatEntity.MSG_TYPE_PHOTO:
//                            listenr.onPhotoClick(position);
//                            break;
//                        case ChatEntity.MSG_TYPE_FACE:
//                            listener.onFaceClick(position);
//                            break;
//                    }
//                }
//            });e
        }

        holder.progress.setVisibility(View.GONE);
        holder.img_sendfail.setVisibility(View.GONE);
        //消息发送的状态
//        switch (data.getState()) {
//            case ChatEntity.MSG_STATE_FAIL:
//                holder.progress.setVisibility(View.GONE);
//                holder.img_sendfail.setVisibility(View.VISIBLE);
//                break;
//            case ChatEntity.MSG_STATE_SUCCESS:
//                holder.progress.setVisibility(View.GONE);
//                holder.img_sendfail.setVisibility(View.GONE);
//                break;
//            case ChatEntity.MSG_STATE_SENDING:
//                holder.progress.setVisibility(View.VISIBLE);
//                holder.img_sendfail.setVisibility(View.GONE);
//                break;
//        }
        setLeftHeadIconClick(holder.img_avatar, data);
        return v;
    }

    private void setLeftHeadIconClick(SimpleDraweeView headIcon, final ChatMsg data) {
        if (data == null) {
            return;
        }
        if (data.getIsSend()) {
            return;
        }
        headIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity != null && StringUtils.isNotEmpty(data.getUserId())) {
                    UserInfoActivity.startActivity(mActivity, data.getUserId());
                }
            }
        });
    }

    static class ViewHolder {
        View rl_container;
        TextView tv_date;
        SimpleDraweeView img_avatar;
        SimpleDraweeView img_chatimage ;
        TextView tv_chatcontent, tvGiftContent;
        SimpleDraweeView ivGift;
        ImageView img_sendfail;
        ProgressBar progress;
        RelativeLayout layout_content;
        RelativeLayout rlItemChat, rlItemGift;
    }

    static class MishuViewHolder {
        MarkSimpleDraweeView ivAvatar;
        TextView tvContent, tvDate;
    }

    static class GuanfangViewHolder {
        TextView tvContent, tvDate, tvName;
    }
}
