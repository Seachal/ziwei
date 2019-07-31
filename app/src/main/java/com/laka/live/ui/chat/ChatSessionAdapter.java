package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.ChatEntity;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.help.quickAdapter.BaseAdapterHelper;
import com.laka.live.help.quickAdapter.QuickAdapter;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.HorizontalDragLayout;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.List;

import laka.live.bean.ChatSession;

/**
 * Created by luwies on 16/3/22.
 */
public class ChatSessionAdapter extends QuickAdapter<ChatSession> {

    private Context mContext;
    private Activity mActivity;
    private Handler mUiHandler;

    private OnItemContentClickListener mListener ;

    public interface OnItemContentClickListener{
        void onItemCotentClick(int position);
    }

    public void setOnItemContentClickListener(OnItemContentClickListener listener){
        mListener = listener ;
    }

    public ChatSessionAdapter(Context context , OnItemContentClickListener listener) {
        super(context, R.layout.item_chat_session);
        mContext = context;
        mListener = listener ;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
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
    protected void convert(BaseAdapterHelper helper, final ChatSession item, final int position) {
        //左滑删除
        if (helper.getView() instanceof HorizontalDragLayout){
            ((HorizontalDragLayout) helper.getView()).setIsDragEnable(false);
        }

        SimpleDraweeView face = helper.retrieveView(R.id.user_face);
        ImageView ivRed = helper.retrieveView(R.id.iv_red);
        TextView name = helper.retrieveView(R.id.name);
        LevelText level = helper.retrieveView(R.id.level);
        ImageView ivGender = helper.retrieveView(R.id.iv_gender);
        TextView desc = helper.retrieveView(R.id.desc);
        TextView tvDate = helper.retrieveView(R.id.tv_date);
        TextView tvUnreadCnt = helper.retrieveView(R.id.tv_unread_cnt);
        View divider = helper.retrieveView(R.id.divider);
        tvDate.setText(Utils.getChatDate(item.getDate()));
        face.setOnClickListener(null);

        helper.retrieveView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbManger.getInstance().deleteSession(item , true);
                remove(item);
                notifyDataSetChanged();
            }
        });
        helper.retrieveView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onItemCotentClick(position);
                }
            }
        });

        if (item.getType() != null && item.getType() == DbManger.SESSION_TYPE_STRANGER) {//陌生人消息
            ImageUtil.loadResImage(R.drawable.dm_icon_strange, face);
            ivGender.setVisibility(View.GONE); //隐藏性别和等级
            level.setVisibility(View.GONE);
            name.setTextColor(ResourceHelper.getColor(R.color.color2E2E2E));
            tvUnreadCnt.setVisibility(View.GONE);
            if (item.getUnreadCnt() > 0) {
                ivRed.setVisibility(View.VISIBLE);
            } else {
                ivRed.setVisibility(View.GONE);
            }
            face.setOnClickListener(null);
            face.setClickable(false);
        } else if (item.getType() != null && item.getType() == DbManger.SESSION_TYPE_MISHU) {//滋味Live小秘书
            ImageUtil.loadResImage(R.drawable.dm_icon_ring, face);
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.drawable.rank_icon_degree);
            level.setVisibility(View.GONE);
            name.setTextColor(ResourceHelper.getColor(R.color.colorFFA100));
            tvUnreadCnt.setVisibility(View.GONE);
            if (item.getUnreadCnt() > 0) {
                ivRed.setVisibility(View.VISIBLE);
            } else {
                ivRed.setVisibility(View.GONE);
            }
            face.setOnClickListener(null);
            face.setClickable(false);
        } else if (item.getType() != null && item.getType() == DbManger.SESSION_TYPE_GUANFANG) {//滋味Live官方
            ImageUtil.loadResImage(R.drawable.dm_icon_logo, face);
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.drawable.rank_icon_degree);
            level.setVisibility(View.GONE);
            name.setTextColor(ResourceHelper.getColor(R.color.colorFFA100));
            tvUnreadCnt.setVisibility(View.GONE);
            if (item.getUnreadCnt() != null && item.getUnreadCnt() > 0) {
                ivRed.setVisibility(View.VISIBLE);
            } else {
                ivRed.setVisibility(View.GONE);
            }
            face.setOnClickListener(null);
            face.setClickable(false);
        } else {
            //左滑删除
            if (helper.getView() instanceof HorizontalDragLayout){
                ((HorizontalDragLayout) helper.getView()).setIsDragEnable(true);
            }

            if (Utils.isEmpty(item.getAvatar())) {
                ImageUtil.loadResImage(R.drawable.blank_icon_avatar, face);
            } else {
                ImageUtil.loadImage(face, item.getAvatar());
            }
            ivGender.setVisibility(View.VISIBLE);
            if (item.getGender() != null && item.getGender() == ListUserInfo.GENDER_BOY) {
                ivGender.setImageResource(R.drawable.mine_icon_men);
            } else {
                ivGender.setImageResource(R.drawable.mine_icon_women);
            }
            level.setVisibility(View.VISIBLE);
            name.setTextColor(ResourceHelper.getColor(R.color.color2E2E2E));
            ivRed.setVisibility(View.GONE);
            if (item.getUnreadCnt() == null || item.getUnreadCnt() == 0) {
                tvUnreadCnt.setVisibility(View.GONE);
            } else if (item.getUnreadCnt() <= 99) {
                tvUnreadCnt.setVisibility(View.VISIBLE);
                tvUnreadCnt.setText(String.valueOf(item.getUnreadCnt()));
            } else {
                tvUnreadCnt.setVisibility(View.VISIBLE);
                tvUnreadCnt.setText("99+");
            }
            face.setClickable(true);
            face.setTag(item.getUserId());
            face.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = (String) v.getTag();
                    if (!Utils.isEmpty(userId) && mActivity != null)
                        UserInfoActivity.startActivity(mActivity, userId);
                }
            });

        }

        name.setText(item.getNickName());
        level.setVisibility(View.GONE);
        level.setLevel(item.getLevel() != null ? item.getLevel() : 0);
        if (desc != null && !Utils.isEmpty(item.getContent())) {
//            desc.setText(item.getContent());
            MoonUtil.identifyFaceExpression(mContext, desc, item.getContent(), ImageSpan.ALIGN_BOTTOM, MoonUtil.SMALL_SCALE);
        }


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) divider.getLayoutParams();
        if (position == getCount() - 1) {
            layoutParams.leftMargin = 0;
        } else {
            layoutParams.leftMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.user_item_divider_margin_left);
        }
    }
}
