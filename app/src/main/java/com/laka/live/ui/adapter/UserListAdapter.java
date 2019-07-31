package com.laka.live.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.account.UserInfoItemView;
import com.laka.live.account.follow.FollowRequestHelper;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.LoadingDialogHelper;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;

import framework.utils.GsonTools;

/**
 * Created by luwies on 16/3/22.
 * zwl update 16/7/25
 */
public class UserListAdapter extends BaseAdapter<ListUserInfo, UserListAdapter.ViewHolder> {

    public static final String FROM_SEARCH = "search";
    public static final String FROM_MY_FOLLOW = "my_follow";
    public static final String FROM_MY_FANS = "my_fans";
    public static final String FROM_USER_INFO_FOLLOW = "user_info_follow";
    public static final String FROM_USER_INFO_FANS = "user_info_fans";

    private BaseActivity mContext;
    private String mFromTag;
    private FollowRequestHelper mFollowHelper;

    public UserListAdapter(BaseActivity baseActivity, String fromTag) {
        super();
        mContext = baseActivity;
        mFromTag = fromTag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserInfoItemView itemView = new UserInfoItemView(parent.getContext());
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int margin;
        if (position == getItemCount() - 1) {
            margin = 0;
        } else {
            margin = ResourceHelper.getDimen(R.dimen.space_14);
        }
        holder.mItemView.setDividerMargin(margin, 0, 0, 0);
    }

    public class ViewHolder extends BaseAdapter.ViewHolder<ListUserInfo> {

        private UserInfoItemView mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = (UserInfoItemView) itemView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mItemView.setLayoutParams(layoutParams);
        }

        public void update(BaseAdapter adapter, final int position, final ListUserInfo info) {

            if (mFromTag.equals(FROM_SEARCH)) {
                mItemView.setItemType(UserInfoItemView.ITEM_TYPE_SEARCH);
            } else if (mFromTag.equals(FROM_MY_FANS)) {
                mItemView.setItemType(UserInfoItemView.ITEM_TYPE_MY_FANS);
            } else if (mFromTag.equals(FROM_MY_FOLLOW)) {
                mItemView.setItemType(UserInfoItemView.ITEM_TYPE_MY_FOLLOW);
            } else if (mFromTag.equals(FROM_USER_INFO_FANS)) {
                mItemView.setItemType(UserInfoItemView.ITEM_TYPE_USER_FANS);
            } else if (mFromTag.equals(FROM_USER_INFO_FOLLOW)) {
                mItemView.setItemType(UserInfoItemView.ITEM_TYPE_USER_FOLLOW);
            } else {
                mItemView.setItemType(UserInfoItemView.ITEM_TYPE_DEFAULT);
            }

            mItemView.setUserInfoData(info);
            mItemView.setOnFollowButtonClickListener(new UserInfoItemView.OnFollowButtonClickListener() {
                @Override
                public void onClick() {
                    reportFollowClick(!info.isFollow());
                    handleOnFollowButtonClick(info);
                }
            });
        }

        private void handleOnFollowButtonClick(final ListUserInfo info) {
            LoadingDialogHelper.showLoadingDialog(mContext);
            if (mFollowHelper == null) {
                mFollowHelper = new FollowRequestHelper();
            }
            mFollowHelper.setAutoToastFailTips(true);
            mFollowHelper.startRequest(mContext, info.getId(), info.isFollow(),
                    new FollowRequestHelper.FollowRequestCallback() {
                        @Override
                        public void requestSuccess(boolean isCancelFollow) {
                            LoadingDialogHelper.closeLoadingDialog();
                            if (isCancelFollow) {
                                info.setFollow(ListUserInfo.NO_FOLLOW);
                            } else {
                                info.setFollow(ListUserInfo.FOLLOWED);
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void requestFailed(boolean isCancelFollow, int errorCode, String errorMsg) {
                            LoadingDialogHelper.closeLoadingDialog();
                        }
                    });
        }

        private void reportFollowClick(boolean isFollow) {
            if (TextUtils.equals(mFromTag, FROM_SEARCH)) {
                AnalyticsReport.onEvent(mContext, AnalyticsReport.SEARCH_RESULT_FOLLOW_BUTTON_CLICK_EVENT_ID);
            } else if (TextUtils.equals(mFromTag, FROM_MY_FOLLOW)) {
            } else if (TextUtils.equals(mFromTag, FROM_MY_FANS)) {
            } else if (TextUtils.equals(mFromTag, FROM_USER_INFO_FOLLOW)) {
                if (isFollow) {
                } else {
                }
            } else if (TextUtils.equals(mFromTag, FROM_USER_INFO_FANS)) {
                if (isFollow) {
                } else {
                }
            }
        }
    }

}
