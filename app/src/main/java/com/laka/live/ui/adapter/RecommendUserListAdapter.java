package com.laka.live.ui.adapter;

import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangcong
 */
public class RecommendUserListAdapter extends BaseAdapter<ListUserInfo, RecommendUserListAdapter.ViewHolder> {

    private BaseActivity mContext;
    private SparseBooleanArray isUnChecked;


    public RecommendUserListAdapter(BaseActivity baseActivity) {
        super();
        mContext = baseActivity;
        isUnChecked = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_recommend_layout, null);
        return new ViewHolder(view);
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
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mDividerView.getLayoutParams();
        params.setMargins(margin, 0, 0, 0);
        holder.mDividerView.setLayoutParams(params);
    }

    public class ViewHolder extends BaseAdapter.ViewHolder<ListUserInfo> {
        private View mDividerView;
        private CheckBox checkBox;
        private MarkSimpleDraweeView mHeadIconView;
        private TextView mUserNameView;
        private LevelText mUserLevelView;
        private TextView mUserSignView;
        private TextView mIsLivingView;


        public ViewHolder(View itemView) {
            super(itemView);
            mDividerView = itemView.findViewById(R.id.divider);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            mHeadIconView = (MarkSimpleDraweeView) itemView.findViewById(R.id.user_face);
            mUserNameView = (TextView) itemView.findViewById(R.id.name);
            mUserLevelView = (LevelText) itemView.findViewById(R.id.level);
            mUserSignView = (TextView) itemView.findViewById(R.id.desc);
            mIsLivingView = (TextView) itemView.findViewById(R.id.is_living_text);
            mIsLivingView.setVisibility(View.GONE);
        }

        public void update(BaseAdapter adapter, final int position, final ListUserInfo userInfo) {
            checkBox.setChecked(!isUnChecked.get(position));

            int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(userInfo.getAuth()), MarkSimpleDraweeView.SizeType.BIG);
            mHeadIconView.setMark(markId);
            ImageUtil.loadImage(mHeadIconView, userInfo.getAvatar());

            if (StringUtils.isEmpty(userInfo.getNickName())) {
                mUserNameView.setText(ResourceHelper.getString(R.string.nick_name));
                return;
            }

            mUserNameView.setText(userInfo.getNickName());
            int gender = userInfo.getGender();
            Drawable sexDrawable;
            if (gender == ListUserInfo.GENDER_BOY) {
                sexDrawable = ResourceHelper.getDrawable(R.drawable.mine_icon_men);
            } else if (gender == ListUserInfo.GENDER_GIRL) {
                sexDrawable = ResourceHelper.getDrawable(R.drawable.mine_icon_women);
            } else {
                sexDrawable = null;
            }
            mUserNameView.setCompoundDrawablesWithIntrinsicBounds(null, null, sexDrawable, null);


            mUserLevelView.setVisibility(View.GONE);
            mUserLevelView.setLevel(userInfo.getLevel());

            if (StringUtils.isEmpty(userInfo.getDescription())) {
                mUserSignView.setText(ResourceHelper.getString(R.string.default_sign));
                return;
            }
            mUserSignView.setText(userInfo.getDescription());

        }
    }

    public void onItemClick(int position) {
        isUnChecked.put(position, !isUnChecked.get(position));
        notifyDataSetChanged();
    }

    public List<ListUserInfo> getCheckedUserList() {
        List<ListUserInfo> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (!isUnChecked.get(i)) {
                list.add(getItem(i));
            }
        }
        return list;
    }

}
