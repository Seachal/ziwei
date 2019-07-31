package com.laka.live.ui.widget.panel;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.Common;
import com.laka.live.util.UiPreference;

/**
 * Created by luwies on 16/6/25.
 */
public class GPUImageFilterPanel extends BasePanel implements View.OnClickListener {

    private RecyclerView mRecyclerView;

//    private OnFilterListener mOnFilterListener;

    private FilterAdapter mAdapter;

    public GPUImageFilterPanel(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FilterAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        mContentView.findViewById(R.id.cancel).setOnClickListener(this);
        mContentView.findViewById(R.id.sure).setOnClickListener(this);
    }

    @Override
    protected View onCreateContentView() {
        View view = View.inflate(mContext, R.layout.gpuimage_filter_panel_layout, null);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                onCancel();
                break;
            case R.id.sure:
                onSure();
                break;
        }
    }

    private void onCancel() {
       hidePanel();
    }

    private void onSure() {
        FilterAdapter.Item item = mAdapter.getSelectItem();

        //保存选择的filterId;
//        UiPreference.putInt(Common.KEY_FILTER_ID,item.filter.getFilterId());

        /*if (mOnFilterListener != null) {
//            mOnFilterListener.onFilter(item.filter);
        }*/
        hidePanel();
    }

    /*public void setOnFilterListener(OnFilterListener listener) {
        this.mOnFilterListener = listener;
    }*/

    /*public GPUImageFilter getLastFilter() {
        return mAdapter.getSelectItem().filter;
    }

    public interface OnFilterListener {
        void onFilter(GPUImageFilter filter);
    }*/

    private static class FilterAdapter extends BaseAdapter<FilterAdapter.Item, FilterAdapter.ViewHolder> {

        private int mSelectPosition = 0;

        private Context mContext;

        public FilterAdapter(Context context) {
            super();
            mContext = context;
            init();
        }

        private void init() {
            initData();
        }

        private void initData() {
            addItem(R.drawable.lookup_original, R.drawable.for_filter_test, R.string.original);
            addItem(R.drawable.lookup_amatorka, R.drawable.amatorka_filter_preview, R.string.amatory);
            addItem(R.drawable.lookup_miss_etikate, R.drawable.miss_etikate_filter_preview,
                    R.string.miss_etikate);
            addItem(R.drawable.lookup_soft_elegance_1, R.drawable.soft_elegance_1_filter_preview, R.string.soft_elegance_1);
            addItem(R.drawable.lookup_soft_elegance_2, R.drawable.soft_elegance_2_filter_preview, R.string.soft_elegance_2);
            addItem(R.drawable.lookup_gray, R.drawable.gray_filter_preview, R.string.gray);
            addItem(R.drawable.lookup_nostalgia, R.drawable.nostalgia_filter_preview, R.string.nostalgia);

            //获取上次保存的filterId,显示选中
           int curFilterId =  UiPreference.getInt(Common.KEY_FILTER_ID,0);
           if(curFilterId!=0){
               for (int i = 0; i <mDatas.size(); i++) {
                   Item item = mDatas.get(i);
                   /*if(item.filter.getFilterId()==curFilterId){
                       mSelectPosition = i;
                       break;
                   }*/
               }
           }
          notifyDataSetChanged();
        }

        private void addItem(int lookupRes, int previewRes, int nameRes) {
            addItem(lookupRes, previewRes, mContext.getString(nameRes));
        }

        private void addItem(int lookupRes, int previewRes, String name) {
            /*Item item = new Item();
            item.previewBmp = ((BitmapDrawable) mContext.getResources().getDrawable(previewRes)).getBitmap();
            GPUImageLookupFilter filter = new GPUImageLookupFilter();
            filter.setBitmap(((BitmapDrawable) mContext.getResources().getDrawable(lookupRes)).getBitmap());
            filter.setFilterId(lookupRes);
            item.filter = filter;
            item.name = name;
            mDatas.add(item);*/
        }

        public Item getSelectItem() {
            return getItem(mSelectPosition);
        }


        @Override
        protected void onItemClick(int position) {
            super.onItemClick(position);
            int last = mSelectPosition;
            mSelectPosition = position;
            notifyItemChanged(last);
            notifyItemChanged(mSelectPosition);
        }

        @Override
        public FilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(mContext, R.layout.item_image_filter, null);
            return new FilterAdapter.ViewHolder(view);
        }

        class ViewHolder extends BaseAdapter.ViewHolder<Item> {

            private ImageView imageView;

            private TextView name;

            private View fram;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                name = (TextView) itemView.findViewById(R.id.name);
                fram = itemView.findViewById(R.id.frame);
            }

            @Override
            public void update(BaseAdapter adapter, int position, Item item) {

                fram.setVisibility(position == mSelectPosition ? View.VISIBLE : View.GONE);

                imageView.setImageBitmap(item.previewBmp);

                name.setText(item.name);
            }
        }

        static class Item {
            Bitmap previewBmp;
//            GPUImageFilter filter;
            String name;
        }
    }


}
