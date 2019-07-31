package com.laka.live.video.ui.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoConstant;
import com.laka.live.video.model.bean.VideoMaterialBean;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/3
 * @Description: 素材库Adapter
 */

public class VideoMaterialAdapter extends BaseAdapter<VideoMaterialBean, BaseAdapter.ViewHolder> {

    private Context mContext;
    private boolean isMultiSelect = false;
    //    private List<VideoMaterialBean> materialList;
    private int uiType;
    private OnItemClickListener onItemClickListener;

    public VideoMaterialAdapter(Context mContext, @VideoConstant.MATERIAL_UI_TYPE int uiType) {
        this(mContext, uiType, null);
    }

    public VideoMaterialAdapter(Context mContext, @VideoConstant.MATERIAL_UI_TYPE int uiType, List<VideoMaterialBean> materialList) {
        this.mContext = mContext;
        this.uiType = uiType;
        this.isMultiSelect = uiType == VideoConstant.MATERIAL_TYPE_CHOOSE;
//        this.materialList = materialList;
    }

    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_video_material, parent, false);
        VideoMaterialViewHolder viewHolder = new VideoMaterialViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseAdapter.ViewHolder holder, int position) {
        VideoMaterialBean bean = getItem(position);
        holder.update(this, position, bean);
    }

    public void setMultiSelect(boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
        notifyDataSetChanged();
    }

    public int getUiType() {
        return uiType;
    }

    public void updateData(List<VideoMaterialBean> materialList) {
//        this.materialList = materialList;
        super.setmDatas(materialList);
        notifyDataSetChanged();
    }

    public void addData(VideoMaterialBean materialBean) {
        addData(materialBean, 0);
    }

    public void addData(VideoMaterialBean materialBean, int position) {
//        if (materialList != null) {
//            materialList.add(position, materialBean);
//        }
        getmDatas().add(position, materialBean);
        notifyDataSetChanged();
    }

    @Override
    public void remove(int position) {
//        if (Utils.isEmpty(materialList)) {
//            return;
//        }
//        materialList.remove(position);
        if (Utils.isEmpty(getData())) {
            return;
        }
        getData().remove(position);
        notifyDataSetChanged();
    }

    public void removeAll() {
//        materialList = new ArrayList<>();
        super.clear();
        notifyDataSetChanged();
    }

    public List<VideoMaterialBean> getData() {
//        return materialList;
        return getmDatas();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private class VideoMaterialViewHolder extends BaseAdapter.ViewHolder<VideoMaterialBean> implements View.OnClickListener {

        private SwipeMenuLayout mSwipeLayout;
        private ConstraintLayout mClLayout;
        private ImageView mIvCheck;
        private SimpleDraweeView mIvCover;
        private TextView mTvTitle;
        private TextView mTvTime;
        private TextView mTvDelete;

        private int position;
        private VideoMaterialBean item;

        public VideoMaterialViewHolder(View itemView) {
            super(itemView);
            mSwipeLayout = itemView.findViewById(R.id.swipe_video_material);
            mClLayout = itemView.findViewById(R.id.cl_video_material);
            mIvCheck = itemView.findViewById(R.id.iv_video_material_check);
            mIvCover = itemView.findViewById(R.id.iv_video_material_cover);
            mTvTitle = itemView.findViewById(R.id.tv_video_material_title);
            mTvTime = itemView.findViewById(R.id.tv_video_material_time);
            mTvDelete = itemView.findViewById(R.id.tv_material_delete);
        }

        @Override
        public void update(final BaseAdapter adapter, final int position, final VideoMaterialBean item) {
            this.position = position;
            this.item = item;
            if (uiType == VideoConstant.MATERIAL_TYPE_CHOOSE) {
                mSwipeLayout.setSwipeEnable(false);
                mIvCheck.setVisibility(View.VISIBLE);
                final boolean isCheck = item.isCheck();
                mIvCheck.setImageDrawable(isCheck ?
                        ResourceHelper.getDrawable(R.drawable.share_icon_opt_h) :
                        ResourceHelper.getDrawable(R.drawable.share_icon_opt));
//                mIvCheck.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Logger.e("点击Item：" + item);
//                        item.setCheck(!isCheck);
//                        notifyDataSetChanged();
//                    }
//                });
            } else {
                mSwipeLayout.setSwipeEnable(true);
                mIvCheck.setVisibility(View.GONE);
            }

            ImageUtil.loadImage(mIvCover, item.getVideoCover());
            mTvTitle.setText(item.getVideoTitle());
            mTvTime.setText(item.getVideoDurationStr());
            mClLayout.setOnClickListener(this);
            mTvDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cl_video_material:
                    if (uiType == VideoConstant.MATERIAL_TYPE_CHOOSE) {
                        item.setCheck(!item.isCheck());
                        notifyItemChanged(position);
                    }
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, item);
                    }
                    break;
                case R.id.tv_material_delete:
                    if (onItemClickListener != null) {
                        onItemClickListener.onDeleteClick(position, item);
                        //关闭菜单栏
                        mSwipeLayout.quickClose();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(int position, VideoMaterialBean item);

        void onDeleteClick(int position, VideoMaterialBean item);
    }
}
