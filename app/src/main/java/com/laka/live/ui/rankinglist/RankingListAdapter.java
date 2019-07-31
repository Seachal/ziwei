package com.laka.live.ui.rankinglist;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.RankingUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/6/28.
 * Email-1501448275@qq.com
 */
public class RankingListAdapter extends BaseAdapter<RankingUserInfoParams, BaseAdapter.ViewHolder<RankingUserInfoParams>> {

    private final static int TYPE_TOP_THREE = 0;

    private final static int TYPE_NORMAL = 1;

    private final static int TYPE_EMPTY = 2;

    private Activity mActivity;

    private int mItemType;

    private int mType;

    public RankingListAdapter(Activity activity, int itemType, int type) {
        mActivity = activity;
        mItemType = itemType;
        mType = type;
    }

    @Override
    public int getItemViewType(int position) {
        int size = super.getItemCount();
        if (size <= 3) {
            if (position <= 0) {
                return TYPE_TOP_THREE;
            }
            return TYPE_EMPTY;
        } else {
            if (position <= 0) {
                return TYPE_TOP_THREE;
            }
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        int size = super.getItemCount();
        if (size <= 0) {
            return 0;
        }
        if (size <= 3) {
            return 2;
        }
        return size - 2;
    }

    @Override
    public RankingUserInfoParams getItem(int position) {
        if (position <= 0) {
            super.getItem(position);
        }
        return super.getItem(position + 2);
    }

    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseAdapter.ViewHolder viewHolder;

        if (viewType == TYPE_TOP_THREE) {
            viewHolder = new TopThreeViewHolder(
                    LayoutInflater.from(mActivity).inflate(R.layout.rank_top_three_layout, null), mItemType);
        } else if (viewType == TYPE_EMPTY){
            viewHolder = new EmptyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.error_layout,
                    null), mType);
        } else {
            viewHolder = new NormalViewHolder(new RankingItemView(mActivity), mItemType);
        }
        return viewHolder;
    }

    private static class TopThreeViewHolder extends BaseAdapter.ViewHolder<RankingUserInfoParams> {

        private List<TopItemHolder> mTopItemHolders;


        public TopThreeViewHolder(View itemView, int itemType) {
            super(itemView);
            Context context = itemView.getContext();
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.dip2px(context, 210f));
            params.topMargin = Utils.dip2px(context, 58f);
            itemView.setLayoutParams(params);

            mTopItemHolders = new ArrayList<>(3);
            mTopItemHolders.add(new TopItemHolder(itemView.findViewById(R.id.first), 0, itemType));
            mTopItemHolders.add(new TopItemHolder(itemView.findViewById(R.id.second), 1, itemType));
            mTopItemHolders.add(new TopItemHolder(itemView.findViewById(R.id.third), 2, itemType));
        }

        @Override
        public void update(BaseAdapter adapter, int position, RankingUserInfoParams params) {

            List<RankingUserInfoParams> rankingDataList = adapter.getAll();
            int size = rankingDataList.size();
            for (int i = 0; i < 3; i++) {
                TopItemHolder holder = mTopItemHolders.get(i);
                if (i < size) {
                    RankingUserInfoParams userInfoParams = rankingDataList.get(i);
                    holder.update(userInfoParams);
                } else {
                    holder.update(null);
                }

            }
        }
    }

    private static class TopItemHolder implements View.OnClickListener {

        int index;

        View itemView;

        View contentView;

        ImageView medal;

        View faceLayout;

        MarkSimpleDraweeView face;

        ImageView crown;

        TextView name;

        ImageView sex;

        LevelText levelText;

        TextView amount;

        TextView mFollowButton;

        RankingUserInfo mUserInfo;

        boolean mEnableCancelFollow = false;

        int itemType;

        public TopItemHolder(View itemView, int index, int itemType) {
            this.itemView = itemView;
            this.index = index;
            this.itemType = itemType;
            contentView = itemView.findViewById(R.id.content);
            contentView.setOnClickListener(this);
            medal = (ImageView) itemView.findViewById(R.id.medal);
            faceLayout = itemView.findViewById(R.id.face_layout);
            face = (MarkSimpleDraweeView) itemView.findViewById(R.id.face);
            crown = (ImageView) itemView.findViewById(R.id.crown);
            name = (TextView) itemView.findViewById(R.id.name);
            sex = (ImageView) itemView.findViewById(R.id.sex);
            levelText = (LevelText) itemView.findViewById(R.id.level);
            amount = (TextView) itemView.findViewById(R.id.amount);
            mFollowButton = (TextView) itemView.findViewById(R.id.follow_text);

            Context context = itemView.getContext();
            int width = (Util.getScreenWidth(context) - Utils.dip2px(context, 7f) * 2) / 3;
            ViewGroup.MarginLayoutParams contentParams = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) faceLayout.getLayoutParams();
            int faceMarginTop = 0;
            if (index == 0) {
                layoutParams.topMargin = Utils.dip2px(context, 30f);
                crown.setVisibility(View.VISIBLE);
                faceMarginTop = Utils.dip2px(context, 16f);

                contentParams.topMargin = Utils.dip2px(context, 4.5f);
            } else {
                layoutParams.topMargin = Utils.dip2px(context, 25f);
                crown.setVisibility(View.GONE);
                contentParams.topMargin = Utils.dip2px(context, 8f);
            }
            ViewGroup.MarginLayoutParams faceParams = (ViewGroup.MarginLayoutParams) face.getLayoutParams();
            faceParams.topMargin = faceMarginTop;

            int medalRes;
            switch (index) {
                case 0:
                    contentView.setBackgroundResource(R.drawable.share_lists_bg01);
                    medalRes = R.drawable.share_icon_medal01;
                    width += Utils.dip2px(context, 10f);
                    break;
                case 1:
                    medalRes = R.drawable.share_icon_medal02;
                    break;
                case 2:
                    medalRes = R.drawable.share_icon_medal03;
                    break;
                default:
                    medalRes = R.drawable.share_icon_medal01;
                    break;
            }
            contentParams.width = width;
            medal.setImageResource(medalRes);

        }

        void update(RankingUserInfoParams params) {
            if (params == null || params.isEmpty) {
                ImageUtil.loadImage(face, "");
                name.setText(R.string.hot_rent);
                sex.setImageResource(R.drawable.rank_icon_question);
                levelText.setBackgroundResource(R.drawable.rank_icon_zero);
                amount.setText("0");
                amount.setTextColor(ContextCompat.getColor(contentView.getContext(), R.color.color777777));
                mFollowButton.setVisibility(View.GONE);
                return;
            }

            params.itemType = itemType;
            RankingUserInfo userInfo = params.userInfo;
            mUserInfo = userInfo;

            ImageUtil.loadImage(face, userInfo.getAvatar());
            MarkSimpleDraweeView.AuthType authType = MarkSimpleDraweeView.getAuthType(userInfo.getStarVerified(),
                    userInfo.getVerified());
            face.setMark(authType, MarkSimpleDraweeView.SizeType.BIG);
            name.setText(userInfo.getNickName());
            if (userInfo.getGender() == UserInfo.GENDER_BOY) {
                sex.setImageResource(R.drawable.mine_icon_men);
            } else {
                sex.setImageResource(R.drawable.mine_icon_women);
            }
            levelText.setLevel(userInfo.getLevel());

            amount.setTextColor(ContextCompat.getColor(contentView.getContext(), R.color.colorF76720));
            setFansCountTextView(userInfo, params.itemType);
            setFollowButtonState(userInfo);

            mFollowButton.setVisibility(View.VISIBLE);
            mFollowButton.setOnClickListener(this);

        }

        private void setFansCountTextView(RankingUserInfo useInfo, int itemType) {
            /*String content = "";
            if (itemType == RankingItemView.TYPE_COINS) {
                content = String.valueOf(useInfo.getExtra_coins()) + ResourceHelper.getString(R.string.ranking_coins);
            } else if (itemType == RankingItemView.TYPE_FANS) {
                content = String.valueOf(useInfo.getFans()) + ResourceHelper.getString(R.string.ranking_fans);
            } else if (itemType == RankingItemView.TYPE_COINS_HOST) {
                content = String.valueOf(useInfo.getExtra_coins()) + ResourceHelper.getString(R.string.ranking_coins_host);
            }
            amount.setText(Html.fromHtml(content));*/
            String content = "";
            if (itemType == RankingItemView.TYPE_COINS) {
                content = String.valueOf(useInfo.getExtra_coins());
            } else if (itemType == RankingItemView.TYPE_FANS) {
                content = String.valueOf(useInfo.getFans());
            } else if (itemType == RankingItemView.TYPE_COINS_HOST) {
                content = String.valueOf(useInfo.getExtra_coins());
            }
            amount.setText(content);
        }

        private void setFollowButtonState(RankingUserInfo userInfo) {

            if (userInfo.isMyself()) {
                mFollowButton.setVisibility(View.GONE);
                return;
            }
            mFollowButton.setVisibility(View.VISIBLE);
            if (userInfo.getFollow() == RankingListConstant.RANKING_LIST_ITEM_HAVE_FOLLOW) {
                mFollowButton.setSelected(true);
                mFollowButton.setText(ResourceHelper.getString(R.string.follow_button_is_already_follow));
                mFollowButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                mFollowButton.setBackgroundResource(R.drawable.ranking_already_follow_button_selector);
            } else {
                mFollowButton.setSelected(false);
                mFollowButton.setText(ResourceHelper.getString(R.string.follow_button_content));
                mFollowButton.setCompoundDrawablesWithIntrinsicBounds(ResourceHelper.getDrawable(R.drawable.btn_icon_follow), null, null, null);
//                mFollowButton.setBackgroundResource(R.drawable.ranking_follow_button_bg_selector);
            }
        }

        private void tryAddFollow() {
            if (mUserInfo == null) {
                return;
            }
            DataProvider.follow(this, mUserInfo.getId(), new GsonHttpConnection.OnResultListener<Msg>() {
                @Override
                public void onSuccess(Msg msg) {
                    if (msg.getCode() == Msg.TLV_OK) {
                        mUserInfo.setFollow(RankingListConstant.RANKING_LIST_ITEM_HAVE_FOLLOW);
                        setFollowButtonState(mUserInfo);
                        EventBusManager.postEvent(mUserInfo, SubcriberTag.REFRESH_RANKING_LIST_DATA);
                    }
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    ToastHelper.showToast(R.string.follow_fail);
                }
            });
        }

        private void tryCancelFollow() {
            if (mUserInfo == null) {
                return;
            }
            DataProvider.unFollow(this, mUserInfo.getId(), new GsonHttpConnection.OnResultListener<Msg>() {
                @Override
                public void onSuccess(Msg msg) {
                    if (msg.getCode() == Msg.TLV_OK) {
                        mUserInfo.setFollow(RankingListConstant.RANKING_LIST_ITEM_NOT_FOLLOW);
                        setFollowButtonState(mUserInfo);
                        EventBusManager.postEvent(mUserInfo, SubcriberTag.REFRESH_RANKING_LIST_DATA);
                    }
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    ToastHelper.showToast(R.string.cancel_follow_fail);
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.follow_text:
                    handleOnFollowButtonClick();
                    break;
                case R.id.content:
                    handleItemClick();
                    break;
            }
        }

        private void handleItemClick() {
            if (mUserInfo != null) {
                UserInfoActivity.startActivity((Activity) itemView.getContext(),
                        String.valueOf(mUserInfo.getId()));
            }
        }

        private void handleOnFollowButtonClick() {
            if (mUserInfo == null) {
                return;
            }
            if (mUserInfo.getFollow() == RankingListConstant.RANKING_LIST_ITEM_HAVE_FOLLOW) {
                if (mEnableCancelFollow) {
                    tryCancelFollow();
                }
                return;
            }
            tryAddFollow();
        }

        void setVisibility(int visibility) {
            itemView.setVisibility(visibility);
        }
    }

    private static class NormalViewHolder extends BaseAdapter.ViewHolder<RankingUserInfoParams> implements
            RankingItemView.OnItemClickListener {

        RankingItemView mItemView;

        int itemType;

        public NormalViewHolder(RankingItemView itemView, int itemType) {
            super(itemView);
            mItemView = itemView;
            this.itemType = itemType;
        }

        @Override
        public void update(BaseAdapter adapter, int position, RankingUserInfoParams params) {
            params.itemType = itemType;
            mItemView.setItemViewData(params);
            mItemView.setOnItemClickListener(this);
        }

        @Override
        public void onItemClick(RankingUserInfo userInfo) {
            UserInfoActivity.startActivity((Activity) mItemView.getContext(),
                    String.valueOf(userInfo.getId()));
        }
    }

    private static class EmptyViewHolder extends BaseAdapter.ViewHolder<RankingUserInfoParams> {

        TextView textView;

        public EmptyViewHolder(View itemView, int type) {
            super(itemView);

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = Utils.dip2px(itemView.getContext(), 51f);
            itemView.setLayoutParams(params);

            textView = (TextView) itemView.findViewById(R.id.tip);

//            int res = R.drawable.default_icon_follow;
//            if (type == RankingListConstant.RANKING_TAB_TYPE_SEND) {
//                textView.setText(R.string.send_rank_empty_tip);
//                res = R.drawable.default_icon_follow;
//            } else if (type == RankingListConstant.RANKING_TAB_TYPE_FANS) {
//                textView.setText(R.string.normal_rank_empty_tip);
//                res = R.drawable.default_icon_fans;
//            } else if (type == RankingListConstant.RANKING_TAB_TYPE_ACCEPT) {
//                textView.setText(R.string.normal_rank_empty_tip);
//                res = R.drawable.default_icon_follow;
//            } else {
//                textView.setText(R.string.normal_rank_empty_tip);
//            }

            textView.setText(R.string.empty_tips);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, 0, R.drawable.public_pic_empty, 0, 0);
        }

        @Override
        public void update(BaseAdapter adapter, int position, RankingUserInfoParams params) {

        }
    }
}
