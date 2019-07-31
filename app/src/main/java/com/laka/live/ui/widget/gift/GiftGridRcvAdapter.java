package com.laka.live.ui.widget.gift;


import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.base.AnimatedDrawable;
import com.laka.live.R;
import com.laka.live.bean.GiftInfo;
import com.laka.live.bean.PayProduct;
import com.laka.live.gift.GiftResManager;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.List;

public class GiftGridRcvAdapter extends
        RecyclerView.Adapter<GiftGridRcvAdapter.ViewHolder> implements OnClickListener {
    private static final String TAG = "GiftGridRcvAdapter";
    Context mContext;
    AnimationDrawable curAnimDrawable;
    public boolean isShowing = false;
    public boolean isInRoom= true;
    public void setInRoom(boolean isInRoom) {
        this.isInRoom = isInRoom;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    Handler handler = new Handler();

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<GiftInfo> mDatas;

    public GiftGridRcvAdapter(Context context, List<GiftInfo> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        SimpleDraweeView ivGift;
        TextView tvKazuan, tvMulti, tvExperience;
        RelativeLayout rlSelectedBg, rlBg;
        ImageView ivLine;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View convertView = mInflater.inflate(R.layout.item_gift_grid,
                viewGroup, false);
        ViewHolder holder = new ViewHolder(convertView);

        holder.ivGift = (SimpleDraweeView) convertView.findViewById(R.id.iv_gift);
        holder.tvKazuan = (TextView) convertView.findViewById(R.id.tv_kazuan);
        holder.rlSelectedBg = (RelativeLayout) convertView.findViewById(R.id.rl_selected_bg);
        holder.rlBg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);
        holder.tvMulti = (TextView) convertView.findViewById(R.id.tv_multi);
        holder.tvExperience = (TextView) convertView.findViewById(R.id.tv_experience);
        holder.ivLine = (ImageView) convertView.findViewById(R.id.iv_line_bottom);
        return holder;
    }

    GiftInfo curItem;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        GiftInfo item = mDatas.get(i);

        if(!isInRoom){
//            TextPaint tp = holder.tvKazuan.getPaint();
//            tp.setFakeBoldText(true);
            holder.tvKazuan.setTextColor(ResourceHelper.getColor(R.color.color2E2E2E));
            holder.tvExperience.setTextColor(ResourceHelper.getColor(R.color.white));
        }

        if (item.isMulti()) {
            holder.tvMulti.setVisibility(View.INVISIBLE);
        } else {
            holder.tvMulti.setVisibility(View.INVISIBLE);
        }

//        if("40".equals(item.getIdStr())){
//            holder.tvKazuan.setText("敬请期待");
//        }else{

//            if(item.getKazuan()>=1f){
//                holder.tvKazuan.setText((int)item.getKazuan()+"味豆");
//            }else{
//                holder.tvKazuan.setText(String.format(ResourceHelper.getString(R.string.n_laka_zuan), NumberUtils.splitDouble(item.getKazuan(),1)+""));
//                Log.d(TAG," 转换前="+item.getKazuan());
                holder.tvKazuan.setText(String.format(ResourceHelper.getString(R.string.n_laka_zuan), NumberUtils.splitOnePoint((float) item.getKazuan())));
//                Log.d(TAG," 转换后="+NumberUtils.getCoursePriceFormat((float) item.getKazuan()));
//            }

//        }

        if (item.isChoose()) {
//            holder.rlSelectedBg.setVisibility(View.VISIBLE);
            holder.rlBg.setBackgroundResource(R.drawable.rect_3px_fbb903);
        } else {
//            holder.rlSelectedBg.setVisibility(View.GONE);
            holder.rlBg.setBackgroundResource(R.color.transparent);
        }
        holder.tvExperience.setText(String.format(ResourceHelper.getString(R.string.experience_add_d), item.getExperience()));

        if (i > 3) {
            holder.ivLine.setVisibility(View.VISIBLE);
        } else {
            holder.ivLine.setVisibility(View.GONE);
        }

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, i);
                }
            });
        }


        //都用静态图
        ImageUtil.loadResImage(item.getImageRes(),holder.ivGift);

//        if (item.isChoose() && isShowing) {
//
//            //动态图
//            curItem = item;
//            String animPath = GiftResManager.getInstance().getSmallAnimPath(item.getIdStr());
//
//            if (!Utils.isEmpty(animPath) ){
//                if(animPath.startsWith("giftAnims")){
//                    ImageUtil.loadAssetsImage(animPath, holder.ivGift, null);
//                }else{
//                    ImageUtil.loadLocalImage(animPath, holder.ivGift,null);
//                }
//            }else{
//                int giftRes = GiftResManager.getInstance().getChatResByGiftId(item.getIdStr());
//                String giftUrl = GiftResManager.getInstance().getImageResByGiftId(item.getIdStr());
//                if(giftRes>0){
//                    ImageUtil.loadResImage(giftRes,holder.ivGift);
//                }else if(!Utils.isEmpty(giftUrl)){
//                    ImageUtil.loadImage( holder.ivGift,giftUrl);
//                }
//            }
//        } else {
//
//            //静态图
//            int giftRes = GiftResManager.getInstance().getChatResByGiftId(item.getIdStr());
//            String giftUrl = GiftResManager.getInstance().getImageResByGiftId(item.getIdStr());
//            if(giftRes>0){
//                ImageUtil.loadResImage(giftRes,holder.ivGift);
//            }else if(!Utils.isEmpty(giftUrl)){
//                ImageUtil.loadImage( holder.ivGift,giftUrl);
//            }
//
//
//        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }


    public OnItemClickLitener getmOnItemClickLitener() {
        return mOnItemClickLitener;
    }

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
