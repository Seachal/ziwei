package com.laka.live.shopping.goods.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.bean.ShoppingEvaluatorBean;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.ui.widget.AlphaImageView;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by zhangwulin on 2016/3/16.
 * email 1501448275@qq.com
 */
public class GoodsEvaluatorListView extends LinearLayout {

    private LinearLayout mEvaluatorLayout;
    private LayoutParams mLayoutParams;
    private String mGoodsId;

    public GoodsEvaluatorListView(Context context) {
        this(context, null);
    }

    public GoodsEvaluatorListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsEvaluatorListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        LinearLayout rootView = new LinearLayout(getContext());
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.bottomMargin = ResourceHelper.getDimen(R.dimen.space_10);
        rootView.setOrientation(HORIZONTAL);
        addView(rootView, mLayoutParams);

        AlphaImageView mPrevButton = new AlphaImageView(getContext());
        mPrevButton.setVisibility(INVISIBLE);
        int padding = ResourceHelper.getDimen(R.dimen.space_10);
        mPrevButton.setPadding(padding, padding, padding, padding);
        mPrevButton.setImageDrawable(ResourceHelper.getDrawable(R.drawable.community_details_next));
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mLayoutParams.gravity = Gravity.CENTER;
        rootView.addView(mPrevButton, mLayoutParams);

        mEvaluatorLayout = new LinearLayout(getContext());
        mEvaluatorLayout.setOrientation(HORIZONTAL);
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.weight = 1;
        rootView.addView(mEvaluatorLayout, mLayoutParams);

        AlphaImageView mNextButton = new AlphaImageView(getContext());
        mNextButton.setPadding(padding, padding, padding, padding);
        mNextButton.setImageDrawable(ResourceHelper.getDrawable(R.drawable.community_details_next));
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mLayoutParams.gravity = Gravity.CENTER;
        rootView.addView(mNextButton, mLayoutParams);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOnButtonClick();
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerOnButtonClick();
            }
        });
    }

    public void setEvaluatorLayoutData(List<ShoppingEvaluatorBean> evaluatorBeans) {
        mEvaluatorLayout.removeAllViewsInLayout();
        if (evaluatorBeans == null || evaluatorBeans.size() == 0) {
            setVisibility(GONE);
            return;
        }
        for (int i = 0; i < evaluatorBeans.size(); i++) {
            if (i < 5) {
                tryAddEvaluatorView(evaluatorBeans.get(i));
            }
        }
    }

    public void setGoodsId(String goodsId) {
        this.mGoodsId = goodsId;
    }

    private void tryAddEvaluatorView(final ShoppingEvaluatorBean evaluatorBean) {
        FrameLayout mEvaluatorFrameLayout = new FrameLayout(getContext());
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.weight = 1;
        mEvaluatorLayout.addView(mEvaluatorFrameLayout, mLayoutParams);

        FrameLayout.LayoutParams mFlp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mFlp.gravity = Gravity.TOP | Gravity.CENTER;

        SimpleDraweeView mHeadIcon = new SimpleDraweeView(getContext());
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);

        GenericDraweeHierarchy hierarchy = builder.setFadeDuration(300).setRoundingParams(roundingParams).build();
        mHeadIcon.setHierarchy(hierarchy);
        mFlp = new FrameLayout.LayoutParams(ResourceHelper.getDimen(R.dimen.space_40), ResourceHelper.getDimen(R.dimen.space_40));
        int margin = ResourceHelper.getDimen(R.dimen.space_10);
        mFlp.setMargins(margin, margin, margin, margin);
        mFlp.gravity = Gravity.CENTER;
        mEvaluatorFrameLayout.addView(mHeadIcon, mFlp);
        mHeadIcon.setImageURI(Uri.parse(evaluatorBean.getHeadimageUrl()));
    }

    private void handleUserInfoOnClick(final ShoppingEvaluatorBean evaluatorBean) {
//        StatsModel.stats(StatsKeyDef.COMMUNITY_CARE_USER);
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_USER_INFO_WINDOW;
        message.arg1 = StringUtils.parseInt(evaluatorBean.getUserId());
        MsgDispatcher.getInstance().sendMessage(message);
    }

    private void handlerOnButtonClick() {
        Message message = Message.obtain();
        message.obj = mGoodsId;
        message.what = MsgDef.MSG_SHOW_EVALUATOR_LIST_WINDOW;
        MsgDispatcher.getInstance().sendMessage(message);
    }
}
