package com.laka.live.ui.widget;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.model.ShoppingCategoryInfo;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhxu on 2016/4/22.
 * Email:357599859@qq.com
 */
public class HorizontalRecyclerView extends RecyclerView {

    private Context mContext;

    private boolean mIsTouchDown = false;

    private HorizontalRecyclerViewAdapter mAdapter;

    private IHorizontalRecyclerView mCallBack;

    private final static int mCommonDimen = ResourceHelper.getDimen(R.dimen.space_1);

    public HorizontalRecyclerView(Context context) {
        this(context, null);
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }

    public void setList(List<Model> mList) {
        mAdapter = new HorizontalRecyclerViewAdapter(mList);
        setAdapter(mAdapter);
        smoothScrollBy(mCommonDimen * 190, 0);
    }

    private void init(Context context) {
        int padding = ResourceHelper.getDimen(R.dimen.space_20);
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen.space_160)));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(HORIZONTAL);
        setPadding(0, 0, 0, padding / 2);
        setLayoutManager(layoutManager);
        setHasFixedSize(true);
        setItemAnimator(null);
    }

    public void setCallBack(IHorizontalRecyclerView mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            mIsTouchDown = true;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            mIsTouchDown = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isTouchDown() {
        return mIsTouchDown;
    }

    public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener {

        private List<Model> mList = new ArrayList<>();

        public HorizontalRecyclerViewAdapter(List<Model> list) {
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ItemsViewHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ItemsViewHolder _holder = (ItemsViewHolder) holder;
            Model model = mList.get(position);
            String imageUrl = model.imageUrl;
            if (StringUtils.isNotEmpty(imageUrl)) {
                _holder.sdvImage.setImageURI(Uri.parse(imageUrl));
                _holder.sdvImage.setAspectRatio(3 / 2);
            }
            ShoppingCategoryInfo categoryInfo = new ShoppingCategoryInfo();
            categoryInfo.type = model.type;
            categoryInfo.typeId = model.typeId;
            _holder.linearLayout.setTag(categoryInfo);
            _holder.linearLayout.setOnClickListener(this);
            if (position == getItemCount() - 1) {
                _holder.line.setVisibility(GONE);
            } else {
                _holder.line.setVisibility(VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onClick(View v) {
            if (mCallBack != null) {
                mCallBack.onItemsClick((ShoppingCategoryInfo) v.getTag());
            }
        }

        public class ItemsViewHolder extends RecyclerView.ViewHolder {

            LinearLayout linearLayout;
            SimpleDraweeView sdvImage;
            View line;

            public ItemsViewHolder(LinearLayout itemView) {
                super(itemView);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mCommonDimen * 240, LayoutParams.WRAP_CONTENT);
                linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setLayoutParams(layoutParams);
                itemView.addView(linearLayout);
                RippleEffectHelper.addRippleEffectInView(linearLayout);

                sdvImage = new SimpleDraweeView(itemView.getContext());
                linearLayout.addView(sdvImage);

                layoutParams = new LinearLayout.LayoutParams(mCommonDimen * 10, LayoutParams.MATCH_PARENT);
                line = new View(mContext);
                line.setLayoutParams(layoutParams);
                itemView.addView(line);
            }
        }
    }

    public interface IHorizontalRecyclerView {
        void onItemsClick(ShoppingCategoryInfo categoryInfo);
    }

    public class Model {
        public int type;
        public int typeId;
        public String imageUrl;
    }
}
