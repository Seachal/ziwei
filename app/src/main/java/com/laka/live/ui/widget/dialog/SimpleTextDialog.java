package com.laka.live.ui.widget.dialog;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;

/**
 * Created by zwl on 2015/12/24.
 * Email: 1501448275@qq.com
 */
public class SimpleTextDialog extends GenericDialog {
    private LinearLayout mTextContainer;
    private TextView mTextView;
    private TextView mSubTextView;
    private boolean isPortrait;

//    public void setPortrait(boolean isPortrait) {
//        this.isPortrait = isPortrait;
//        if(!isPortrait){
//            mRootView.setRotation(90);
//            ViewGroup.LayoutParams lp =  mRootView.getLayoutParams();
//            lp.width = Utils.dip2px(getContext(),300);
//            lp.height = Utils.dip2px(getContext(),200);
//            mRootView.setLayoutParams(lp);
//        }
//    }

    public SimpleTextDialog(Context context) {
        super(context);
    }

    public void setText(int textRes) {
        setText(ResourceHelper.getString(textRes));
    }

    public void setText(String text) {
        initContainer();
        mTextView.setText(text);
        if (StringUtils.isEmpty(text)) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleTextColor(int res) {
        if (mTitleView != null) {
            mTitleView.setTextColor(ResourceHelper.getColor(res));
        }
    }

    public void setSubText(int textRes) {
        setSubText(ResourceHelper.getString(textRes));
    }

    public void setSubText(String text) {
        initContainer();
        mSubTextView.setText(text);
        if (StringUtils.isEmpty(text)) {
            mSubTextView.setVisibility(View.GONE);
        } else {
            mSubTextView.setVisibility(View.VISIBLE);
        }
    }

    private void initContainer() {
        if (mTextContainer == null) {
            mTextContainer = new LinearLayout(getContext());
            mTextContainer.setOrientation(LinearLayout.VERTICAL);
            addContentView(mTextContainer);

            mTextView = new TextView(getContext());
            mTextView.setVisibility(View.GONE);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_16));
            mTextView.setTextColor(ResourceHelper.getColor(R.color.dialog_text_color));
            mTextView.setMaxWidth((int) (HardwareUtil.getDeviceWidth() * 0.7f));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mTextContainer.addView(mTextView, lp);

            mSubTextView = new TextView(getContext());
            mSubTextView.setVisibility(View.GONE);
            mSubTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_14));
            mSubTextView.setTextColor(ResourceHelper.getColor(R.color.dialog_text_color));
            mTextView.setMaxWidth((int) (HardwareUtil.getDeviceWidth() * 0.7f));
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mTextContainer.addView(mSubTextView, lp);
        }
    }

}
