package com.laka.live.account;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

/**
 * Created by zwl on 2016/7/25.
 * Email-1501448275@qq.com
 */
public class UserInfoItemView extends LinearLayout {

    public static final int ITEM_TYPE_DEFAULT = 0;
    public static final int ITEM_TYPE_SEARCH = 1;
    public static final int ITEM_TYPE_USER_FANS = 2;
    public static final int ITEM_TYPE_MY_FANS = 3;
    public static final int ITEM_TYPE_MY_FOLLOW = 4;
    public static final int ITEM_TYPE_USER_FOLLOW = 5;

    protected MarkSimpleDraweeView mHeadIconView;
    protected TextView mUserNameView;
//    private LevelText mUserLevelView;
    private TextView mUserSignView;
    protected TextView mFollowButton;
    private View mDividerView;
    protected TextView mIsLivingView;

    private ImageView mSexView;

    private int mItemType;
    protected ListUserInfo mUserInfo;
    private OnFollowButtonClickListener mListener;

    protected Context mContext;

    public UserInfoItemView(Context context) {
        this(context, null);
    }

    public UserInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        View view = View.inflate(getContext(), R.layout.list_user_info_layout, null);
        LayoutParams layoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen.space_70));
        addView(view, layoutParam);

        mHeadIconView = (MarkSimpleDraweeView) view.findViewById(R.id.user_face);
        mUserNameView = (TextView) view.findViewById(R.id.name);
//        mUserLevelView = (LevelText) view.findViewById(R.id.level);
        mUserSignView = (TextView) view.findViewById(R.id.desc);
        mFollowButton = (TextView) view.findViewById(R.id.follow_text);
        mDividerView = view.findViewById(R.id.divider);
        mIsLivingView = (TextView) view.findViewById(R.id.is_living_text);
        mSexView = (ImageView) view.findViewById(R.id.sex);

        mFollowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnFollowButtonClick();
            }
        });
    }

    private void handleOnFollowButtonClick() {
        if (mListener == null) {
            return;
        }
        if (mUserInfo == null) {
            return;
        }
        if (mUserInfo.isFollow() && (mItemType == ITEM_TYPE_USER_FANS || mItemType == ITEM_TYPE_USER_FOLLOW)) {
            return;
        }
        if (mUserInfo.isFollow() && mItemType == ITEM_TYPE_SEARCH) {
            return;
        }
        mListener.onClick();
    }

    public void setUserInfoData(ListUserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        mUserInfo = userInfo;
        setHeadIconView(userInfo);
        setUserNameView(userInfo);
//        setUserLevelView(userInfo);
        setUserSignView(userInfo);
        setFollowButton(userInfo);
        setIsLivingView(userInfo);
//        setUserSexView(userInfo);
    }

    public void setItemType(int itemType) {
        mItemType = itemType;
    }

    protected void setHeadIconView(ListUserInfo userInfo) {
        //取消等级认证信息
        int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(userInfo.getStarVerified(),
                userInfo.getVerified()), MarkSimpleDraweeView.SizeType.BIG);
        mHeadIconView.setMark(markId);
        ImageUtil.loadImage(mHeadIconView, userInfo.getAvatar());
    }

    protected void setUserNameView(ListUserInfo userInfo) {
        if (StringUtils.isEmpty(userInfo.getNickName())) {
            mUserNameView.setText(ResourceHelper.getString(R.string.nick_name));
            return;
        }
        mUserNameView.setText(userInfo.getNickName());
    }

//    protected void setUserLevelView(ListUserInfo userInfo) {
//        if (mItemType == ITEM_TYPE_SEARCH) {
//            mUserLevelView.setVisibility(GONE);
//        } else {
//            mUserLevelView.setVisibility(VISIBLE);
//            mUserLevelView.setLevel(userInfo.getLevel());
//        }
//    }

//    protected void setUserSexView(ListUserInfo userInfo) {
//
//        if (mItemType == ITEM_TYPE_SEARCH) {
//            mSexView.setVisibility(GONE);
//        } else {
//            int gender = userInfo.getGender();
//            Drawable sexDrawable;
//            if (gender == ListUserInfo.GENDER_BOY) {
//                sexDrawable = ResourceHelper.getDrawable(R.drawable.mine_icon_men);
//            } else if (gender == ListUserInfo.GENDER_GIRL) {
//                sexDrawable = ResourceHelper.getDrawable(R.drawable.mine_icon_women);
//            } else {
//                mSexView.setVisibility(GONE);
//                return;
//            }
//            mSexView.setVisibility(VISIBLE);
//            mSexView.setImageDrawable(sexDrawable);
//        }
//
//    }

    protected void setUserSignView(ListUserInfo userInfo) {
        if (StringUtils.isEmpty(userInfo.getDescription())) {
            mUserSignView.setText(ResourceHelper.getString(R.string.default_sign));
            return;
        }
        mUserSignView.setText(userInfo.getDescription());
    }

    private void setFollowButton(ListUserInfo userInfo) {
        if (userInfo.isMyself()) {
            mFollowButton.setVisibility(GONE);
            return;
        }
        mFollowButton.setVisibility(VISIBLE);
        if (userInfo.isFollow()) {
            mFollowButton.setSelected(true);

            mFollowButton.setText(ResourceHelper.getString(R.string.has_follow));
            mFollowButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            mFollowButton.setSelected(false);
            mFollowButton.setCompoundDrawablesWithIntrinsicBounds(
                    ResourceHelper.getDrawable(R.drawable.btn_icon_follow), null, null, null);
            mFollowButton.setText(ResourceHelper.getString(R.string.follow));
        }
    }

    private void setIsLivingView(ListUserInfo userInfo) {
        if (mItemType == ITEM_TYPE_SEARCH && userInfo.isLive()) {
            mIsLivingView.setVisibility(View.VISIBLE);
            return;
        }
        mIsLivingView.setVisibility(View.GONE);
    }

    public void setDividerMargin(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDividerView.getLayoutParams();
        params.setMargins(left, top, right, bottom);
    }

    public void setOnFollowButtonClickListener(OnFollowButtonClickListener listener) {
        mListener = listener;
    }

    public interface OnFollowButtonClickListener {
        void onClick();
    }
}
