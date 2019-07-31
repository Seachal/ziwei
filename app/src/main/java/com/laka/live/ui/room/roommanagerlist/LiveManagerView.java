package com.laka.live.ui.room.roommanagerlist;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.util.ResourceHelper;

/**
 * Created by zwl on 2016/6/7.
 * Email-1501448275@qq.com
 */
public class LiveManagerView extends LinearLayout {
    private SimpleDraweeView mHeadView;
    private TextView mNameView;
    private LevelText mLevelView;
    private ImageView mSexView;
    private TextView mToolsButton;
    private LayoutParams mLayoutParams;
    private UserInfo mUserInfo;
    private OnCancelManagerListener mListener;

    public LiveManagerView(Context context) {
        this(context, null);
    }

    public LiveManagerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveManagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
//        setBackgroundColor(ResourceHelper.getColor(R.color.transparent15));
        setBackgroundResource(R.drawable.list_item_selector);
        LinearLayout rootView = new LinearLayout(getContext());
        rootView.setOrientation(HORIZONTAL);
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(rootView, mLayoutParams);
        addHeadView(rootView);
        addUserInfoLayout(rootView);
        addToolButton(rootView);
        addDividerView();
    }

    private void addDividerView() {
        View view = new View(getContext());
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen.divider_height));
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_68);
        view.setBackgroundColor(ResourceHelper.getColor(R.color.default_divider));
        addView(view, mLayoutParams);
    }

    private void addNameView(LinearLayout parentView) {
        mNameView = new TextView(getContext());
        mNameView.setTextColor(ResourceHelper.getColor(R.color.color333333));
        mNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_15));

        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;
        parentView.addView(mNameView, mLayoutParams);
    }

    private void addSexView(LinearLayout parentView) {
        mSexView = new ImageView(getContext());
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_5);
        parentView.addView(mSexView, mLayoutParams);
    }

    private void addLevelView(LinearLayout parentView) {
//        mLevelView = new LevelText(getContext());
        mLevelView = (LevelText) LayoutInflater.from(parentView.getContext()).inflate(R.layout.user_level_widgit, null);
        mLevelView.setPadding(
                ResourceHelper.getDimen(R.dimen.space_2),
                0,
                ResourceHelper.getDimen(R.dimen.space_2),
                0);
        mLevelView.setTextAppearance(getContext(), R.style.user_level);
        mLevelView.setCompoundDrawablePadding(ResourceHelper.getDimen(R.dimen.space_2));
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_5);
        parentView.addView(mLevelView, mLayoutParams);
    }

    private void addHeadView(LinearLayout parentView) {
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy mHierarchy = builder
                .setFadeDuration(300)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setRoundingParams(roundingParams)
                .setPlaceholderImage(ContextCompat.getDrawable(getContext(), R.drawable.blank_icon_avatar))
                .build();
        mHeadView = new SimpleDraweeView(getContext());
        mHeadView.setHierarchy(mHierarchy);
        mLayoutParams = new LayoutParams(ResourceHelper.getDimen(R.dimen.space_40),
                ResourceHelper.getDimen(R.dimen.space_40));
        mLayoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_14);
        mLayoutParams.rightMargin = ResourceHelper.getDimen(R.dimen.space_14);
        mLayoutParams.topMargin = ResourceHelper.getDimen(R.dimen.space_10);
        mLayoutParams.bottomMargin = ResourceHelper.getDimen(R.dimen.space_10);
        parentView.addView(mHeadView, mLayoutParams);
    }

    private void addToolButton(LinearLayout parentView) {
        mToolsButton = new TextView(getContext());
        mToolsButton.setTextColor(ResourceHelper.getColor(R.color.color666666));
        mToolsButton.setBackgroundResource(R.drawable.room_manager_button_bg_selector);
        mToolsButton.setPadding(ResourceHelper.getDimen(R.dimen.space_10),
                ResourceHelper.getDimen(R.dimen.space_5),
                ResourceHelper.getDimen(R.dimen.space_10),
                ResourceHelper.getDimen(R.dimen.space_5));
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.rightMargin = ResourceHelper.getDimen(R.dimen.space_5);
        parentView.addView(mToolsButton, mLayoutParams);
        mToolsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnToolsButtonClick();
            }
        });
    }

    private void addUserInfoLayout(LinearLayout parentView) {
        LinearLayout infoLayout = new LinearLayout(getContext());
        infoLayout.setOrientation(HORIZONTAL);
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.weight = 1;
        parentView.addView(infoLayout, mLayoutParams);
        addNameView(infoLayout);
        addSexView(infoLayout);
        addLevelView(infoLayout);
    }

    private void handleOnToolsButtonClick() {
        if (mUserInfo.isAdministrator()) {
            if (mListener != null) {
                mListener.onCancel(mUserInfo);
            }
            return;
        }
        if (mListener != null) {
            mListener.onCancel(mUserInfo);
        }
    }

    public void setManagerData(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        setHeadView(userInfo);
        setNameView(userInfo);
        setToolsButtonContent(userInfo);
        setLevelView(userInfo);
        setSexView(userInfo);
    }

    private void setHeadView(UserInfo userInfo) {
        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            return;
        }
        mHeadView.setImageURI(Uri.parse(userInfo.getAvatar()));
    }

    private void setNameView(UserInfo userInfo) {
        if (TextUtils.isEmpty(userInfo.getNickName())) {
            mNameView.setText(ResourceHelper.getString(R.string.live_manager_default_nick_name));
            return;
        }
        mNameView.setText(userInfo.getNickName());
    }

    private void setToolsButtonContent(UserInfo userInfo) {
        if (userInfo.isAdministrator()) {
            mToolsButton.setText(ResourceHelper.getString(R.string.live_manager_cancel_manager));
        } else {
            mToolsButton.setText(ResourceHelper.getString(R.string.live_manager_add_to_be_manager));
        }
    }

    private void setSexView(UserInfo userInfo) {
        mSexView.setVisibility(VISIBLE);
        if (userInfo.getGender() == ListUserInfo.GENDER_BOY) {
            mSexView.setImageResource(R.drawable.mine_icon_men);
            return;
        }
        if (userInfo.getGender() == ListUserInfo.GENDER_GIRL) {
            mSexView.setImageResource(R.drawable.mine_icon_women);
            return;
        }
        mSexView.setVisibility(GONE);
    }

    private void setLevelView(UserInfo userInfo) {
        mLevelView.setLevel(userInfo.getLevel());
    }

    public void setOnCancelManagerListener(OnCancelManagerListener listener) {
        mListener = listener;
    }

    public interface OnCancelManagerListener {
        void onCancel(UserInfo userInfo);
    }
}
