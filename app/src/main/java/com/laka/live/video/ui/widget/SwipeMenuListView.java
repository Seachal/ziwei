package com.laka.live.video.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.laka.live.R;
import com.laka.live.shopping.widget.SwipeListView;
import com.laka.live.util.PhotoUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoConstant;
import com.laka.live.video.model.bean.VideoMaterialBean;
import com.laka.live.video.ui.activity.VideoMaterialActivity;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:删除ListView
 */

public class SwipeMenuListView<T> extends FrameLayout implements ISwipeMenuView<T> {

    public final String TAG = this.getClass().getSimpleName();

    /**
     * description:UI配置
     **/
    private SwipeListView swipeListView;
    private View emptyView;
    private View loadingView;
    private View errorView;
    private Drawable mDivider;
    private int dividerHeight;
    private Context context;

    /**
     * description:删除配置
     **/
    private int menuWidth;
    private int menuColor;
    private int menuTextColor;
    private int menuTextSize;
    private int menuDirection;

    /**
     * description:数据配置
     **/
    private BaseAdapter mAdapter;

    public SwipeMenuListView(@NonNull Context context) {
        this(context, null);
    }

    public SwipeMenuListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initProperty(context, attrs);
        initView();
        initData();
    }

    @Override
    public void initProperty(Context context, @Nullable AttributeSet attributeSet) {

    }

    @Override
    public void initView() {
        swipeListView = new SwipeListView(context);
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//                SwipeMenuItem openItem = new SwipeMenuItem(mContext);
//                openItem.setBackground(new ColorDrawable(Color.parseColor("#ff4a57")));
//                openItem.setWidth(ResourceHelper.getDimen(R.dimen.space_80));
//                openItem.setTitle("删除");
//                openItem.setTitleSize(14);
//                openItem.setTitleColor(Color.WHITE);
//                menu.addMenuItem(openItem);
//            }
//        };
//
//        mSwipeListView.setMenuCreator(creator);
//        mSwipeListView.setDivider(new ColorDrawable(ResourceHelper.getColor(R.color.divider_F5F5F5)));
//        mSwipeListView.setDividerHeight(Utils.dp2px(this, 5));
//        mSwipeListView.setOnMenuItemClickListener(new com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                //删除
//                mAdapter.remove(position);
//                return false;
//            }
//        });
//
//        mSwipeListView.setSwipeDirection(com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_LEFT);
//
//        mSwipeListView.setOnItemPositionClickListener(new SwipeListView.onItemPositionClickListener() {
//            @Override
//            public void onClickPosition(int position) {
//                if (mAdapter.getUiType() == VideoConstant.MATERIAL_TYPE_CHOOSE) {
//                    Intent intent = new Intent();
//                    VideoMaterialBean materialBean = mAdapter.getData().get(position);
//                    materialBean.setCheck(true);
//                    mAdapter.notifyDataSetChanged();
//                    intent.putExtra(VideoConstant.MATERIAL_EXTRA_ITEM, materialBean);
//                    setResult(PhotoUtil.MATERIAL_STORE, intent);
//                    VideoMaterialActivity.this.finish();
//                }
//            }
//        });
    }

    @Override
    public void initData() {

    }

    @Override
    public ISwipeMenuView setAdapter(BaseAdapter adapter) {
        return null;
    }

    @Override
    public ISwipeMenuView setEmptyView(View view) {
        return null;
    }

    @Override
    public ISwipeMenuView setLoadingView(View view) {
        return null;
    }

    @Override
    public ISwipeMenuView setErrorView(View view) {
        return null;
    }

    @Override
    public ISwipeMenuView setMenuDirection(int direction) {
        return null;
    }

    @Override
    public ISwipeMenuView setMenuWidth(int width) {
        return null;
    }

    @Override
    public ISwipeMenuView setMenuColor(Color color) {
        return null;
    }

    @Override
    public ISwipeMenuView setMenuText(String text) {
        return null;
    }

    @Override
    public ISwipeMenuView setMenuTextColor(Color color) {
        return null;
    }

    @Override
    public ISwipeMenuView setMenuTextSize(int textSize) {
        return null;
    }

    @Override
    public ISwipeMenuView setListDivider(Drawable divider) {
        return null;
    }

    @Override
    public ISwipeMenuView seDividerHeight(int height) {
        return null;
    }

    @Override
    public ISwipeMenuView setClickListener(OnItemClickListener<T> listener) {
        return null;
    }
}
