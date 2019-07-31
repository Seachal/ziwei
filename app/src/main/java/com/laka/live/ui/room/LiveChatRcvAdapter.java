package com.laka.live.ui.room;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.ChatEntity;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.help.textspan.NoLineClickSpan;
import com.laka.live.help.textspan.OnSpanClickListener;
import com.laka.live.ui.widget.ClickTextView;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

public class LiveChatRcvAdapter extends
        RecyclerView.Adapter<LiveChatRcvAdapter.ViewHolder> implements OnClickListener {
    private static final String TAG = "RoomLiveChatRcvAdapter";
    Context mContext;
    public String mineUserId;
    public String zhuboUserId;
    //    SparseArray<Drawable> giftResArray = new SparseArray<Drawable>();
    HashMap<Integer, Drawable> giftResArray = new HashMap<>();

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<ChatEntity> mDatas;
    private String COLOR_GIFT_CONTENT = "#ffc40e";
    private String COLOR_LIKE_NAME = "#ffc40e";//FFDF5D
    private String COLOR_COMMENT_NAME = "#ffc40e";//FFDF5D
    private String COLOR_COME_NAME = "#ffc40e";//FFDF5D
    private String COLOR_SYSTEM_CONTENT = "#ff950b";//FD7083
    private String COLOR_ATTENTION_CONTENT = "#FFEC91";
    private String COLOR_SHARE_CONTENT = "#FFEC91";

    public LiveChatRcvAdapter(Context context, List<ChatEntity> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        SimpleDraweeView ivGift;
        TextView tvContent;
        LevelText tvLevel;
        RelativeLayout rlBg;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_live_comment,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.rlBg = (RelativeLayout) view.findViewById(R.id.rl_bg);
        viewHolder.ivGift = (SimpleDraweeView) view.findViewById(R.id.iv_gift);
        viewHolder.tvContent = (TextView) view
                .findViewById(R.id.tv_content);
        viewHolder.tvLevel = (LevelText) view
                .findViewById(R.id.tv_level);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ChatEntity item = mDatas.get(i);
        viewHolder.tvContent.setMovementMethod(ClickTextView.LocalLinkMovementMethod.getInstance());
        viewHolder.tvLevel.setLevel(item.getLevel());
        SpannableString sp;
        String content;
        if (item.getType() == ChatEntity.MSG_TYPE_SYSTEM) {
            viewHolder.tvLevel.setVisibility(View.GONE);
            content = item.getContent();
            if (StringUtils.isEmpty(content)){
                content = "";
            }
            sp = new SpannableString(content);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_SYSTEM_CONTENT)), 0, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
//            viewHolder.tvLevel.setVisibility(View.VISIBLE);
            if (item.getType() == ChatEntity.MSG_TYPE_TEXT) {
                content = item.getSenderName() + ":" + item.getContent();
                sp = MoonUtil.replaceEmoticons(mContext , content ,ImageSpan.ALIGN_BASELINE, MoonUtil.SMALL_SCALE);
            } else if (item.getType() == ChatEntity.MSG_TYPE_LIKE || item.getType() == ChatEntity.MSG_TYPE_COME || item.getType() == ChatEntity.MSG_TYPE_GIFT
                    || item.getType() == ChatEntity.MSG_TYPE_ATTENTION || item.getType() == ChatEntity.MSG_TYPE_SHARE) {
                content = item.getSenderName() + " " + item.getContent();
                sp = new SpannableString(content);
            } else {
                content = item.getSenderName() + item.getContent();
                if (StringUtils.isEmpty(content)){
                    content = "";
                }
                sp = new SpannableString(content);
            }

            if (!Utils.isEmpty(item.getSenderName())) {
//                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#FFC773")),0,item.getSenderName().length()+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                sp = setNamerOnClick(sp, item.getSenderName(), item.getUserId());

            }
        }

//        if(item.getType()==ChatEntity.MSG_TYPE_COME){
//            viewHolder.rlBg.setBackgroundResource(R.drawable.live_bg_tuhao);
//        }else{
//            viewHolder.rlBg.setBackgroundResource(R.color.transparent);
//        }


        String senderName = item.getSenderName();
        boolean isNotEmpty = TextUtils.isEmpty(senderName) == false && TextUtils.isEmpty(content) == false;
        if (item.getType() == ChatEntity.MSG_TYPE_GIFT) {
            //送礼物内容黄色
            if (isNotEmpty) {
                sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_GIFT_CONTENT)), 0, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//item.getSenderName().length()+1
            }
            if (item.getGiftRes() > 0) {
                ImageUtil.loadResImage(item.getGiftRes(),viewHolder.ivGift);
                viewHolder.ivGift.setVisibility(View.VISIBLE);
            } else if(!Utils.isEmpty(item.getGiftUrl())){
                ImageUtil.loadImage( viewHolder.ivGift,item.getGiftUrl());
                viewHolder.ivGift.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.ivGift.setVisibility(View.GONE);
            }
//            viewHolder.tvContent.setCompoundDrawables(null, null, getGiftRes(item.getGiftRes()), null);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(tvContent,null,null,getGiftRes(item.getGiftRes()),null);
        } else if (item.getType() == ChatEntity.MSG_TYPE_ATTENTION || item.getType() == ChatEntity.MSG_TYPE_SHARE) {
            if (isNotEmpty) {
                sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_GIFT_CONTENT)), 0, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//item.getSenderName().length()+1
            }
            viewHolder.ivGift.setVisibility(View.GONE);
        } else if (item.getType() == ChatEntity.MSG_TYPE_LIKE) {
            if (isNotEmpty) {
                sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_LIKE_NAME)), 0, item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            viewHolder.ivGift.setVisibility(View.VISIBLE);
            viewHolder.ivGift.setImageResource(getLikeRes());
//            viewHolder.tvContent.setCompoundDrawables(null, null, getLike(item.getUserId()), null);
        } else if (item.getType() == ChatEntity.MSG_TYPE_TEXT) {
            if (isNotEmpty) {
                sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_COMMENT_NAME)), 0, item.getSenderName().length() + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            viewHolder.ivGift.setVisibility(View.GONE);
//            viewHolder.tvContent.setCompoundDrawables(null, null, null, null);
        } else if (item.getType() == ChatEntity.MSG_TYPE_COME) {
            if (isNotEmpty) {
                sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_COME_NAME)), 0, item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            viewHolder.ivGift.setVisibility(View.GONE);
//            viewHolder.tvContent.setCompoundDrawables(null, null, null, null);
        } else if (item.getType() == ChatEntity.MSG_TYPE_FORBID_SAY) {
            if (isNotEmpty) {
                sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_SYSTEM_CONTENT)), item.getSenderName().length(), content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            viewHolder.ivGift.setVisibility(View.GONE);
//            viewHolder.tvContent.setCompoundDrawables(null, null, null, null);
        }
//        else if (item.getType() == ChatEntity.MSG_TYPE_COME) {
//            sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_COME_NAME)),  0, item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
////            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            viewHolder.tvContent.setCompoundDrawables(null, null, null, null);
//        }
        else {
            viewHolder.ivGift.setVisibility(View.GONE);
//            viewHolder.tvContent.setCompoundDrawables(null, null, null, null);
        }
        viewHolder.tvContent.setText(sp);


        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });
        }


        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "scroll comment onTouch ACTION_DOWN");
                        EventBusManager.postEvent(false, SubcriberTag.SET_COMMENT_AUTO_SCROLL);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "scroll comment onTouch ACTION_UP");
                        EventBusManager.postEvent(true, SubcriberTag.SET_COMMENT_AUTO_SCROLL);
                        break;
                }

                return false;
            }
        });

    }


    private SpannableString setNamerOnClick(SpannableString sp, String senderName, final String userId) {
        NoLineClickSpan mNoUnderlineSpan = new NoLineClickSpan(mContext,
                new OnSpanClickListener() {
                    @Override
                    public void OnClick(View v) {
                        Log.d(TAG, "点击昵称弹窗 userId=" + userId);
                        EventBusManager.postEvent(userId, SubcriberTag.SHOW_USER_POP);
                    }
                });
        sp.setSpan(mNoUnderlineSpan, 0, senderName.length() + 1,//冒号
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return sp;
    }

    private Drawable getGiftRes(int giftRes) {
//        if(giftResArray.indexOfKey(giftRes)!=-1){
//            return giftResArray.get(giftRes);
//        }
        if (giftResArray.containsKey(giftRes)) {
            return giftResArray.get(giftRes);
        }
        Drawable drawable = mContext.getResources().getDrawable(giftRes);
//        Log.d(TAG,"getGiftRes  height="+drawable.getMinimumHeight()+" width="+drawable.getMinimumWidth());
        drawable.setBounds(0, 0, Utils.dip2px(mContext, 20), Utils.dip2px(mContext, 20));//必须设置图片大小，否则不显示
        giftResArray.put(giftRes, drawable);
        return drawable;
    }

    int[] likeRes = new int[]{R.drawable.live_icon_like_1, R.drawable.live_icon_like_2, R.drawable.live_icon_like_3
            , R.drawable.live_icon_like_4, R.drawable.live_icon_like_5, R.drawable.live_icon_like_6, R.drawable.live_icon_like_kiwi
            , R.drawable.live_icon_like_lemon, R.drawable.live_icon_like_popsicle, R.drawable.live_icon_like_watermelon, R.drawable.live_icon_like_zan01, R.drawable.live_icon_like_zan02, R.drawable.live_icon_like_zan03};

    private Drawable getLike(String userId) {
//        int other = 0;
//        if (!Utils.isEmpty(userId)) {
//            other = Integer.parseInt(userId);
//        }
//        int zhubo = 0;
//        if (!Utils.isEmpty(zhuboUserId)) {
//            zhubo = Integer.parseInt(zhuboUserId);
//        }
//        int giftRes = (other + zhubo) % 6;
        int giftRes = (int) Math.round(Math.random() * (likeRes.length - 1));
        Drawable drawable = ContextCompat.getDrawable(mContext, likeRes[giftRes]);
        drawable.setBounds(0, 0, Utils.dip2px(mContext, 16), Utils.dip2px(mContext, 16));
//         Log.d(TAG,"点亮了 giftRes="+likeRes[giftRes]);
        return drawable;
    }


    private int getLikeRes() {
        int giftRes = (int) Math.round(Math.random() * (likeRes.length - 1));
        return likeRes[giftRes];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }


}
