package com.laka.live.ui.rankinglist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.laka.live.R;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.RankingUserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.rankinglist.widget.DrawableCenterTextView;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

/**
 * Created by zwl on 2016/6/28.
 * Email-1501448275@qq.com
 */
public class RankingItemView extends LinearLayout {
    public static final int TYPE_FANS = 1;
    public static final int TYPE_COINS = 2;
    public static final int TYPE_COINS_HOST = 3;
    public static final String FROM_TYPE_USER_INFO = "from_user_info";
    public static final String FROM_TYPE_MINE = "from_MINE";
    private LayoutParams mLayoutParams;
    private MarkSimpleDraweeView mHeadView;
    private TextView mFansCountTextView;
    private TextView mIndexTextView;
    private ImageView mIndexImageView;
    private TextView mNickNameTextView;
    private ImageView mSexImageView;
    private LevelText mLevelTextView;
    private TextView mFollowButton;
    private LinearLayout mRootView;

    private RankingUserInfo mUserInfo;
    private OnItemClickListener mListener;

    private boolean mEnableCancelFollow = false;
    private String mFromType;

    public RankingItemView(Context context) {
        this(context, null);
    }

    public RankingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        mRootView = new LinearLayout(getContext());
        mRootView.setOrientation(HORIZONTAL);
        mRootView.setBackgroundResource(R.drawable.list_item_selector);
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mRootView, mLayoutParams);
        addDivider();
        addIndexLayout();
        addHeadView();
        addUserInfoLayout();
        addFollowButtonLayout();

        mRootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnItemClick();
            }
        });
    }

    private void handleOnItemClick() {
        if (mUserInfo == null || mListener == null) {
            return;
        }
        mListener.onItemClick(mUserInfo);
    }

    private void addDivider() {
        View divider = new View(getContext());
        divider.setBackgroundColor(ResourceHelper.getColor(R.color.default_divider));
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen.divider_height));
//        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_14);
        addView(divider, mLayoutParams);
    }

    private void addIndexLayout() {
        FrameLayout indexLayout = new FrameLayout(getContext());
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_14);
        mRootView.addView(indexLayout, mLayoutParams);
        addIndexTextView(indexLayout);
        addIndexImageView(indexLayout);
    }

    private void addIndexTextView(FrameLayout parentView) {
        mIndexTextView = new TextView(getContext());
        mIndexTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));
        mIndexTextView.setTextColor(ResourceHelper.getColor(R.color.color2E2E2E));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        parentView.addView(mIndexTextView, layoutParams);
    }

    private void addIndexImageView(FrameLayout parentView) {
        mIndexImageView = new ImageView(getContext());
        mIndexImageView.setImageResource(R.drawable.share_icon_top1);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        parentView.addView(mIndexImageView, layoutParams);
    }

    private void addHeadView() {
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy mHierarchy = builder
                .setFadeDuration(300)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setRoundingParams(roundingParams)
                .setPlaceholderImage(ContextCompat.getDrawable(getContext(), R.drawable.blank_icon_avatar))
                .build();
        mHeadView = new MarkSimpleDraweeView(getContext());
        mHeadView.setHierarchy(mHierarchy);
        mLayoutParams = new LayoutParams(ResourceHelper.getDimen(R.dimen.space_44),
                ResourceHelper.getDimen(R.dimen.space_44));
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.topMargin = ResourceHelper.getDimen(R.dimen.space_13);
        mLayoutParams.bottomMargin = ResourceHelper.getDimen(R.dimen.space_13);
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_14);
        mRootView.addView(mHeadView, mLayoutParams);
    }

    private void addUserInfoLayout() {
        LinearLayout userInfoLayout = new LinearLayout(getContext());
        userInfoLayout.setOrientation(VERTICAL);
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_14);
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.weight = 1;
        mRootView.addView(userInfoLayout, mLayoutParams);

        LinearLayout userNameLayout = new LinearLayout(getContext());
        userNameLayout.setOrientation(HORIZONTAL);
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        userInfoLayout.addView(userNameLayout, mLayoutParams);
        addNickNameTextView(userNameLayout);
        addSexImageView(userNameLayout);
        addLevelTextView(userNameLayout);

        mFansCountTextView = new TextView(getContext());
        mFansCountTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));
        mFansCountTextView.setTextColor(ResourceHelper.getColor(R.color.colorF76720));
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.topMargin = ResourceHelper.getDimen(R.dimen.space_5);
        userInfoLayout.addView(mFansCountTextView, mLayoutParams);
    }

    private void addNickNameTextView(LinearLayout parentView) {
        mNickNameTextView = new TextView(getContext());
        mNickNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_15));
        mNickNameTextView.setTextColor(ResourceHelper.getColor(R.color.color333333));
        mNickNameTextView.setSingleLine();
        mNickNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;
        mNickNameTextView.setMaxWidth(Utils.dip2px(getContext(), 103f));
        parentView.addView(mNickNameTextView, mLayoutParams);
    }

    private void addSexImageView(LinearLayout parentView) {
        mSexImageView = new ImageView(getContext());
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_4);
        mLayoutParams.gravity = Gravity.CENTER;
        parentView.addView(mSexImageView, mLayoutParams);
//        mSexImageView.setVisibility(GONE);
    }

    private void addLevelTextView(LinearLayout parentView) {
//        mLevelTextView = new LevelText(getContext());
        mLevelTextView = (LevelText) LayoutInflater.from(parentView.getContext()).inflate(R.layout.user_level_widgit, null);
        /*mLevelTextView.setTextAppearance(getContext(), R.style.user_level);
        mLevelTextView.setPadding(0,
                0,
                ResourceHelper.getDimen(R.dimen.space_2),
                0);*/
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ResourceHelper.getDimen(R.dimen.space_14));
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_4);
        mLayoutParams.gravity = Gravity.CENTER;
        parentView.addView(mLevelTextView, mLayoutParams);
//        mLevelTextView.setVisibility(GONE);
    }

    private void addFollowButtonLayout() {
        mFollowButton = new DrawableCenterTextView(getContext());
        mFollowButton.setText(ResourceHelper.getString(R.string.follow_button_content));
        mFollowButton.setGravity(Gravity.CENTER_VERTICAL);
        mFollowButton.setTextColor(ResourceHelper.getColorStateList(R.color.ranking_follow_button_text_color_selector));
        mFollowButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));
        mFollowButton.setCompoundDrawablePadding(ResourceHelper.getDimen(R.dimen.space_5));
        mFollowButton.setCompoundDrawablesWithIntrinsicBounds(ResourceHelper.getDrawable(R.drawable.btn_icon_follow_selector), null, null, null);
        mFollowButton.setBackgroundResource(R.drawable.ranking_follow_button_bg_selector);
        mLayoutParams = new LayoutParams(ResourceHelper.getDimen(R.dimen.space_54), ResourceHelper.getDimen(R.dimen.space_22));
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.rightMargin = ResourceHelper.getDimen(R.dimen.space_14);
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_14);
        mRootView.addView(mFollowButton, mLayoutParams);

        mFollowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnFollowButtonClick();
            }
        });
    }

    private void handleOnFollowButtonClick() {
        if (mUserInfo.getFollow() == RankingListConstant.RANKING_LIST_ITEM_HAVE_FOLLOW) {
            if (mEnableCancelFollow) {
                tryCancelFollow();
            }
            return;
        }
        tryAddFollow();
    }

    public void setItemViewData(RankingUserInfoParams userInfoParams) {
        if (userInfoParams == null) {
            return;
        }
        if (userInfoParams.userInfo == null) {
            return;
        }
        mFromType = userInfoParams.itemFromType;
        mUserInfo = userInfoParams.userInfo;
        setIndexContent(userInfoParams.index);
        setNickNameTextView(userInfoParams.userInfo);
        setHeadView(userInfoParams.userInfo);
        setLevelTextView(userInfoParams.userInfo);
        setSexImageView(userInfoParams.userInfo);
        setFansCountTextView(userInfoParams.userInfo, userInfoParams.itemType);
        setFollowButtonState(userInfoParams.userInfo);
    }

    public void setEnableCancelFollow(boolean enableCancelFollow) {
        mEnableCancelFollow = enableCancelFollow;
    }

    private void setIndexContent(int index) {
        mIndexTextView.setVisibility(INVISIBLE);
        mIndexImageView.setVisibility(VISIBLE);
        if (index == 1) {
            mIndexImageView.setImageResource(R.drawable.share_icon_top1);
        } else if (index == 2) {
            mIndexImageView.setImageResource(R.drawable.share_icon_top2);
        } else if (index == 3) {
            mIndexImageView.setImageResource(R.drawable.share_icon_top3);
        } else {
            mIndexImageView.setVisibility(INVISIBLE);
            mIndexTextView.setVisibility(VISIBLE);
            mIndexTextView.setText(String.valueOf(index));
        }
    }

    private void setNickNameTextView(RankingUserInfo useInfo) {
        if (StringUtils.isEmpty(useInfo.getNickName())) {
            mNickNameTextView.setText(ResourceHelper.getString(R.string.live_manager_default_nick_name));
            return;
        }
        mNickNameTextView.setText(useInfo.getNickName());
    }

    private void setHeadView(RankingUserInfo useInfo) {
        final int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(useInfo.getStarVerified(),
                useInfo.getVerified()), MarkSimpleDraweeView.SizeType.BIG);
        mHeadView.setMark(markId);
        ImageUtil.loadImage(mHeadView, useInfo.getAvatar());
    }

    private void setSexImageView(RankingUserInfo useInfo) {
        Drawable sexDrawable;
        if (useInfo.getGender() == ListUserInfo.GENDER_BOY) {
            sexDrawable = ResourceHelper.getDrawable(R.drawable.mine_icon_men);
        } else if (useInfo.getGender() == ListUserInfo.GENDER_GIRL) {
            sexDrawable = ResourceHelper.getDrawable(R.drawable.mine_icon_women);
        } else {
            sexDrawable = null;
        }
        mSexImageView.setImageDrawable(sexDrawable);
    }

    private void setLevelTextView(RankingUserInfo useInfo) {
        mLevelTextView.setLevel(useInfo.getLevel());
    }

    private void setFansCountTextView(RankingUserInfo useInfo, int itemType) {
        String content = "";
        if (itemType == TYPE_COINS) {
            content = String.valueOf(useInfo.getExtra_coins()) + ResourceHelper.getString(R.string.ranking_coins);
        } else if (itemType == TYPE_FANS) {
            content = String.valueOf(useInfo.getFans()) + ResourceHelper.getString(R.string.ranking_fans);
        } else if (itemType == TYPE_COINS_HOST) {
            content = String.valueOf(useInfo.getExtra_coins()) + ResourceHelper.getString(R.string.ranking_coins_host);
        }
        mFansCountTextView.setText(Html.fromHtml(content));
    }

    private void setFollowButtonState(RankingUserInfo userInfo) {

        if (userInfo.isMyself()) {
            mFollowButton.setVisibility(GONE);
            return;
        }
        mFollowButton.setVisibility(VISIBLE);

        if (userInfo.getFollow() == RankingListConstant.RANKING_LIST_ITEM_HAVE_FOLLOW) {
            mFollowButton.setSelected(true);
            mFollowButton.setText(ResourceHelper.getString(R.string.follow_button_is_already_follow));
            mFollowButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//            mFollowButton.setBackgroundResource(R.drawable.ranking_already_follow_button_selector);
        } else {
            mFollowButton.setSelected(false);
            mFollowButton.setText(ResourceHelper.getString(R.string.follow_button_content));
            mFollowButton.setCompoundDrawablesWithIntrinsicBounds(ResourceHelper.getDrawable(R.drawable.btn_icon_follow), null, null, null);
//            mFollowButton.setBackgroundResource(R.drawable.ranking_follow_button_bg_selector);
        }
    }

    private void tryAddFollow() {
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(RankingUserInfo userInfo);
    }
}
