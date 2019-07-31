package com.laka.live.ui.room;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.laka.live.R;
import com.laka.live.bean.UserInfo;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Utils;

import java.util.List;

public class SeeingUserAdapter extends
        RecyclerView.Adapter<SeeingUserAdapter.ViewHolder> implements OnClickListener {
    private static final String TAG = "SeeingUserAdapter";
    Context context;
    public boolean isMine = false;
    public String mineUserId;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<UserInfo> mDatas;

    public SeeingUserAdapter(Context context, List<UserInfo> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
//         CircleImageView mImg;
        MarkSimpleDraweeView mImg;
        ImageView ivHeart;
//		TextView mTxt;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_seeing_user,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (MarkSimpleDraweeView) view
                .findViewById(R.id.id_index_gallery_item_image);


        viewHolder.ivHeart = (ImageView) view
                .findViewById(R.id.iv_heart);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
//		viewHolder.mImg.setImageResource(mDatas.get(i));
        UserInfo item = mDatas.get(i);
        String head = item.getAvatar();
//	    Log.d(TAG, "观众 id="+item.getId()+"头像="+head);

        if(MarkSimpleDraweeView.getAuthType(item.getAuth()) != MarkSimpleDraweeView.AuthType.NONE||item.getLevel()>1){
            int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(item.getAuth()),
                    MarkSimpleDraweeView.SizeType.SMALL, item.getLevel());
            viewHolder.mImg.setMark(markId);
        }else{
            viewHolder.mImg.setMark(null);
        }




//        if(item.getLevel()==1){
//            viewHolder.mImg.setVisibility(View.GONE);
//        }

        if (!Utils.isEmpty(head)) {
            ImageUtil.loadImage( viewHolder.mImg,head);
        } else {
            ImageUtil.loadResImage(R.drawable.blank_icon_avatar, viewHolder.mImg);
        }

        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });
        }

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }


}
