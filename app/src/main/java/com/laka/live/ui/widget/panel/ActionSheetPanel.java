package com.laka.live.ui.widget.panel;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;

public class ActionSheetPanel extends BasePanel {
    private ActionItemView mCancelView;
    private LinearLayout mContainerLayout;
    private ArrayList<ActionSheetItem> mItemList = new ArrayList<>();

    private OnActionSheetClickListener mListener;

    private int mGravity = Gravity.CENTER;

    public static final String PLAY_REQUEST_VIDEO = "0";// 播放视频
    public static final String PHOTO_REQUEST_TAKE_VIDEO = "1";// 拍摄视频
    public static final String PHOTO_REQUEST_GALLERY = "2";// 从相册中选择
    public static final String DELETE_REQUEST_VIDEO = "3";// 删除视频

    public ActionSheetPanel(Context context) {
        super(context);
    }

    @Override
    protected View onCreateContentView() {
        ActionSheetScrollView scrollView = new ActionSheetScrollView(mContext);
        scrollView.setBackgroundColor(ResourceHelper.getColor(R.color.white70));
        mContainerLayout = new LinearLayout(mContext);
        mContainerLayout.setOrientation(LinearLayout.VERTICAL);
        mCancelView = new ActionItemView(mContext);
        mCancelView.setTextColor(ResourceHelper.getColor(R.color.black));
        mCancelView.setText(ResourceHelper.getString(R.string.cancel));
        mCancelView.setEnableDivider(false);
        mCancelView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });
        scrollView.addView(mContainerLayout);
        return scrollView;
    }

    public void setEnableCancelButton(boolean enable) {
        mCancelView.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void setCancelText(String text) {
        mCancelView.setText(text);
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public void setSheetItemClickListener(OnActionSheetClickListener listener) {
        mListener = listener;
    }

    public void addSheetItem(ActionSheetItem item) {
        mItemList.add(item);
    }

    public void addSheetItems(ArrayList<ActionSheetItem> list) {
        mItemList.addAll(list);
    }

    public void clearSheetItem() {
        mItemList.clear();
    }


    // 是否显示删除按钮
    public void setShowPlay(boolean showPlay) {
        if(showPlay) {

            if(mItemList.size() < 3) {
                ActionSheetItem item = new ActionSheetItem();
                item.id = PLAY_REQUEST_VIDEO;
                item.title = ResourceHelper.getString(R.string.play_video);
                addSheetItem(item);
            }

        }else{
            if(mItemList.size() == 3){
                mItemList.remove(mItemList.size()-1);
            }
        }
    }

    @Override
    public void showPanel() {
        setupPanel();
        super.showPanel();
    }

    private void setupPanel() {
        mContainerLayout.removeAllViewsInLayout();
        LayoutParams lp;
        int size = mItemList.size();
        boolean enableCancel = mCancelView.getVisibility() == View.VISIBLE;
        for (int i = 0; i < size; i++) {
            final ActionSheetItem sheetItem = mItemList.get(i);
            ActionItemView textView = new ActionItemView(mContext);
            textView.setText(sheetItem.title);
            textView.setTextColor(ResourceHelper.getColor(sheetItem.colorResId));
            textView.setTextGravity(mGravity);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onActionSheetItemClick(sheetItem.id);
                    }
                    hidePanel();
                }
            });
            if (i == size - 1) {
                if (!enableCancel) {
                    textView.setEnableDivider(false);
                }
            }
            lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen.space_50));
            mContainerLayout.addView(textView, lp);
        }

        if (enableCancel) {
            lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen.space_50));
            lp.setMargins(0, ResourceHelper.getDimen(R.dimen.space_6), 0, 0);
            mCancelView.setTextGravity(mGravity);
            mContainerLayout.addView(mCancelView, lp);
        }
    }

    private class ActionItemView extends FrameLayout {
        private TextView mTextView;
        private View mDivider;
        private LayoutParams mTextLp;

        public ActionItemView(Context context) {
            super(context);

            mTextView = new TextView(getContext());
            mTextView.setTextColor(ResourceHelper.getColor(R.color.actionsheet_black));
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_16));
            mTextView.setSingleLine();
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.leftMargin = ResourceHelper.getDimen(R.dimen.space_16);
            lp.rightMargin = lp.leftMargin;
            this.addView(mTextView, lp);
            mTextLp = lp;

            mDivider = new View(getContext());
            mDivider.setBackgroundColor(ResourceHelper.getColor(R.color.divider_color));
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen
                    .divider_height));
            lp.gravity = Gravity.BOTTOM;
            this.addView(mDivider, lp);

            this.setBackgroundResource(R.drawable.default_listview_seletor);
        }

        public void setTextGravity(int gravity) {
            mTextLp.gravity = gravity;
        }

        public void setText(String text) {
            mTextView.setText(text);
        }

        public void setTextColor(int color) {
            mTextView.setTextColor(color);
        }

        public void setEnableDivider(boolean enable) {
            mDivider.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    private class ActionSheetScrollView extends ScrollView {

        public ActionSheetScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            int maxHeight = (int) (HardwareUtil.getDeviceHeight() * 0.6f);
            if (measuredHeight > maxHeight) {
                measuredHeight = maxHeight;
                setMeasuredDimension(measuredWidth, measuredHeight);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    public static class ActionSheetItem {
        public String id;
        public String title;
        public int colorResId;

        public ActionSheetItem() {
            colorResId = R.color.black;
        }
    }


    public interface OnActionSheetClickListener {
        void onActionSheetItemClick(String id);
    }

}
