package com.laka.live.ui.room.roommanagerlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.laka.live.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/6/7.
 * Email-1501448275@qq.com
 */
public class LiveManagerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnCancelManagerListener mListener;
    private List<UserInfo> mManagerList = new ArrayList<>();

    public LiveManagerListAdapter(Context context, List<UserInfo> managerList) {
        this.mContext = context;
        this.mManagerList = managerList;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setLiveManagerViewData((LiveManagerViewHolder) holder, mManagerList.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = new LiveManagerViewHolder(new LiveManagerView(mContext));
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mManagerList.size();
    }

    private void setLiveManagerViewData(LiveManagerViewHolder managerViewHolder, UserInfo userInfo) {
        managerViewHolder.mLiveManagerView.setManagerData(userInfo);
        managerViewHolder.mLiveManagerView.setOnCancelManagerListener(new LiveManagerView.OnCancelManagerListener() {
            @Override
            public void onCancel(UserInfo userInfo) {
                handleOnCancelManager(userInfo);
            }
        });
    }

    private void handleOnCancelManager(UserInfo userInfo) {
        if (mListener != null) {
            mListener.refresh(userInfo);
        }
    }

    public class LiveManagerViewHolder extends RecyclerView.ViewHolder {
        LiveManagerView mLiveManagerView;

        public LiveManagerViewHolder(View itemView) {
            super(itemView);
            mLiveManagerView = (LiveManagerView) itemView;
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                            , LinearLayout.LayoutParams.WRAP_CONTENT);
            mLiveManagerView.setLayoutParams(layoutParams);
        }
    }

    public void setOnCancelManagerListener(OnCancelManagerListener listener) {
        mListener = listener;
    }

    public interface OnCancelManagerListener {
        void refresh(UserInfo userInfo);
    }
}
