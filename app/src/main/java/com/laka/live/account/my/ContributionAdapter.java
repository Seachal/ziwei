package com.laka.live.account.my;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.laka.live.bean.RankingUserInfo;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.rankinglist.RankingItemView;
import com.laka.live.ui.rankinglist.RankingUserInfoParams;
import com.laka.live.util.StringUtils;

/**
 * Created by luwies on 16/3/31.
 * zwl update
 */
public class ContributionAdapter extends BaseAdapter<RankingUserInfo, ContributionAdapter.ViewHolder> {

    private Activity mActivity;
    private String mFromType;

    public ContributionAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setFromType(String fromType) {
        mFromType = fromType;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RankingItemView itemView = new RankingItemView(parent.getContext());
        return new ViewHolder(itemView);
    }

    public class ViewHolder extends BaseAdapter.ViewHolder<RankingUserInfo> {

        private RankingItemView mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = (RankingItemView) itemView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mItemView.setLayoutParams(layoutParams);
        }

        @Override
        public void update(BaseAdapter adapter, int position, final RankingUserInfo listUserInfo) {

            RankingUserInfoParams rankingUserInfoParams = new RankingUserInfoParams();
            rankingUserInfoParams.index = position + 1;
            rankingUserInfoParams.userInfo = listUserInfo;
            rankingUserInfoParams.itemType = RankingItemView.TYPE_COINS;
            if (StringUtils.isNotEmpty(mFromType) && mFromType.equals(RankingItemView.FROM_TYPE_USER_INFO)) {
                rankingUserInfoParams.itemFromType = RankingItemView.FROM_TYPE_USER_INFO;
            } else {
                rankingUserInfoParams.itemFromType = RankingItemView.FROM_TYPE_MINE;
            }
            mItemView.setItemViewData(rankingUserInfoParams);
            mItemView.setEnableCancelFollow(true);
            mItemView.setOnItemClickListener(new RankingItemView.OnItemClickListener() {
                @Override
                public void onItemClick(RankingUserInfo userInfo) {
                    UserInfoActivity.startActivity(mActivity, String.valueOf(listUserInfo.getId()));
                }
            });
        }
    }
}
