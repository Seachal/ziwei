package com.laka.live.shopping.widget;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.ShoppingCacheManager;
import com.laka.live.shopping.bean.ShoppingHomeCommonListBean;
import com.laka.live.ui.widget.AlphaTextView;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

/**
 * Created by zhxu on 2016/5/6.
 * Email:357599859@qq.com
 */
public class ShoppingGetItPanel extends BasePanel {

    private SimpleDraweeView mSdvImage;
    private TextView mTvTitle, mTvDesc, mTvCheck, mTvFeel;
    private CheckBox mCheckBox;

    private String mCurrColumnId;

    public ShoppingGetItPanel(Context context) {
        super(context);
        int margin = ResourceHelper.getDimen(R.dimen.space_35);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.leftMargin = margin;
        layoutParams.rightMargin = margin;
        getContentView().setLayoutParams(layoutParams);
        getContentView().setBackgroundResource(R.drawable.community_live_info_title_bg);
    }

    public void setItems(final ShoppingHomeCommonListBean bean, final ImageView ivGetIt, AlphaTextView textView) {
        mTvFeel = textView;
        String imageUrl = bean.getImageUrl();
        if (StringUtils.isNotEmpty(imageUrl)) {
            mSdvImage.setImageURI(Uri.parse(imageUrl));
        }
        final String title = bean.getTitle();
        if (StringUtils.isNotEmpty(title)) {
            mTvTitle.setText(title);
        }
        String desc = bean.getDescription();
        if (StringUtils.isNotEmpty(desc)) {
            mTvDesc.setText(desc);
        }
        if (ivGetIt != null) {
            int display = ivGetIt.getVisibility();
            mTvCheck.setVisibility(display);
            mCheckBox.setChecked(display == View.VISIBLE);
        }

        mCurrColumnId = bean.getColumnId();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int display = isChecked ? View.VISIBLE : View.GONE;
                mTvCheck.setVisibility(display);
                ivGetIt.setVisibility(display);
                mTvFeel.setText(isChecked ? R.string.shopping_like : R.string.shopping_what);
                cacheData(bean.getColumnId(), isChecked);
                if (isShowing()) {
                    hidePanel();
                }

                //统计INDEX_POCKER_PRODUCT_LIKE
//                StatsModel.stats(StatsKeyDef.INDEX_POCKER_PRODUCT_LIKE, StatsKeyDef.SPEC_KEY, title);
            }
        });
    }

    private void cacheData(String columnId, boolean isCheck) {
        ShoppingCacheManager.CacheData cacheData = ShoppingCacheManager.getCategoryGetItData(mContext);
        String columnIds = "";
        if (cacheData != null) {
            columnIds = cacheData.data;
        }

        String[] array = new String[0];
        String like = columnId + ":1";
        String unlike = columnId + ":0";

        if (!isCheck) {
            boolean isCache = false;
            if (StringUtils.isNotEmpty(columnIds)) {
                array = columnIds.substring(1).split("#");
            }
            for (String str : array) {
                if (str.equals(like)) {
                    columnIds = columnIds.replace(like, unlike);
                    isCache = true;
                } else if (str.equals(unlike)) {
                    isCache = true;
                }
            }
            if (!isCache) {
                columnIds = columnIds + "#" + unlike;
            }
        } else {
            columnIds = columnIds + "#" + like;
        }
        ShoppingCacheManager.cacheCategoryGetItData(mContext, columnIds);
    }

    @Override
    protected View onCreateContentView() {
        View contentView = View.inflate(mContext, R.layout.shopping_get_it_panel, null);
        mSdvImage = (SimpleDraweeView) contentView.findViewById(R.id.product_image);
        mSdvImage.setAspectRatio(1f);
        mTvTitle = (TextView) contentView.findViewById(R.id.product_title);
        mTvDesc = (TextView) contentView.findViewById(R.id.product_desc);
        mTvCheck = (TextView) contentView.findViewById(R.id.product_check_text);
        mCheckBox = (CheckBox) contentView.findViewById(R.id.product_check);
        return contentView;
    }

    @Override
    public void hidePanel() {
        super.hidePanel();
        if (!mCheckBox.isChecked()) {
            cacheData(mCurrColumnId, false);
            mTvFeel.setText(R.string.shopping_unlike);
        }
    }
}
