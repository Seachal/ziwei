package com.laka.live.ui.chat;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.ImageBean;
import com.laka.live.msg.FormulaMsg;
import com.laka.live.photopreview.PhotoPreviewInfo;
import com.laka.live.photopreview.PhotoPreviewPanel;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.ArrayUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ios on 16/6/27.
 */
public class FormulaPanel extends BasePanel {

    private static final String TAG = "FormulaPanel";
    private View parentView;
    private HeadView headView;
    private TextView mFormula;
    private TextView title;
    private TextView emptyView;
    private LinearLayout formulaImageLayout;
    private PhotoPreviewPanel mPreviewPanel;

    public FormulaPanel(Context context) {
        super(context);
        setAlpha(0);

        setParams(FrameLayout.LayoutParams.MATCH_PARENT, Utils.getScreenHeight(mContext) / 2, Gravity.BOTTOM);
        initView(context);
    }

    public FormulaPanel(Context context, boolean isPortrait, boolean isRotation) {
        super(context, false);
        setAlpha(0);

        int width;
        int height;
        int gravity;

        if (isPortrait || !isRotation) {
            width = FrameLayout.LayoutParams.MATCH_PARENT;
            height = Utils.getScreenHeight(mContext) / 2;
            gravity = Gravity.BOTTOM;
        } else {
            width = Utils.getScreenWidth(mContext) / 2;
            height = FrameLayout.LayoutParams.MATCH_PARENT;
            gravity = Gravity.END;

            setAnimation(R.style.SlideRight2LeftAnim);
        }

        setParams(width, height, gravity);

        initPanel();

        if (!isPortrait && !isRotation) {
            parentView.setRotation(90);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parentView.getLayoutParams();
            params.width = mWlps.height;
            params.height = Utils.getScreenWidth(mContext);
            params.gravity = Gravity.CENTER;

            parentView.setLayoutParams(params);
        }

        initView(context);
    }

    //直播间用
    public FormulaPanel(Context context, boolean isPortrait, boolean isCreate, boolean isFrontCamera) {
        super(context, false);
        setAlpha(0);

        Log.d(TAG, " isPortrait=" + isPortrait + " isCreate=" + isCreate + " isFrontCamera=" + isFrontCamera);
        int width;
        int height;
        int gravity;

        if (isPortrait || !isCreate || isCreate) {
            width = FrameLayout.LayoutParams.MATCH_PARENT;
            height = Utils.getScreenHeight(mContext) / 2;
            if (!isPortrait && !isCreate && isFrontCamera) {
                gravity = Gravity.TOP;
                setAnimation(R.style.SlideTop2BottomAnim);
            } else {
                gravity = Gravity.BOTTOM;
            }
        } else {
            width = Utils.getScreenWidth(mContext) / 2;
            height = FrameLayout.LayoutParams.MATCH_PARENT;
            gravity = Gravity.END;
            setAnimation(R.style.SlideRight2LeftAnim);
        }

        setParams(width, height, gravity);

        initPanel();

        if (!isPortrait) {
            if (!isCreate && isFrontCamera) {
                parentView.setRotation(-90);
            } else {
                parentView.setRotation(90);
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parentView.getLayoutParams();
            params.width = mWlps.height;
            params.height = Utils.getScreenWidth(mContext);
            params.gravity = Gravity.CENTER;

            parentView.setLayoutParams(params);
        }

        initView(context);
    }

    private void initView(Context context) {
        headView = (HeadView) parentView.findViewById(R.id.header);
        headView.setBackShow(false);
        title = (TextView) parentView.findViewById(R.id.titleTxt);
        emptyView = (TextView) parentView.findViewById(R.id.emptyView);
        mFormula = (TextView) parentView.findViewById(R.id.formula);
        formulaImageLayout = (LinearLayout) parentView.findViewById(R.id.formulaImage);
        headView.setTipOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });
        mPreviewPanel = new PhotoPreviewPanel(context);

    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.panel_formula, null);
        return parentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
//        int width;
//        int height;
//        int gravity;
//
//        Log.d(TAG, "isPortrait = " + isPortrait);
//
//        if (isPortrait) {
//            width = FrameLayout.LayoutParams.MATCH_PARENT;
//            height = Utils.getScreenHeight(mContext) / 2;
//            gravity = Gravity.BOTTOM;
//        } else {
//            width = Utils.getScreenWidth(mContext) / 2;
//            height = FrameLayout.LayoutParams.MATCH_PARENT;
//            gravity = Gravity.RIGHT;
//        }

//        Log.d(TAG, " width=" + width + " height=" + height);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        lp.gravity = gravity;
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * @param formulaMsg 配方做法
     */
    public void showPanel(final FormulaMsg formulaMsg) {
        if (Utils.isEmpty(formulaMsg) ||
                (Utils.isEmpty(formulaMsg.formula) && Utils.isEmpty(formulaMsg.getFormulaImages()))) {
            emptyView.setVisibility(View.VISIBLE);
            title.setVisibility(View.INVISIBLE);
            formulaImageLayout.setVisibility(View.INVISIBLE);
        } else {
            title.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            if (Utils.isNotEmpty(formulaMsg.formula)) {
                mFormula.setText(formulaMsg.formula);
            }
            formulaImageLayout.setVisibility(View.VISIBLE);
            initFormulaImages(formulaMsg.getFormulaImages());
        }

        AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_15000);

        super.showPanel();
    }

    public void hidePanel() {
        super.hidePanel();
    }

    // 设置配方
    private void initFormulaImages(List<ImageBean> formulaImage) {

        if (formulaImageLayout.getChildCount() == 0) {

            // 显示配方图片
            ArrayList<String> urls = ArrayUtil.getUrls(formulaImage);
            for (int position = 0; position < formulaImage.size(); ++position) {
                ImageBean bean = formulaImage.get(position);
                showImages(urls, position, bean);
            }
        }

    }

    // 显示课程简介图片
    private void showImages(final ArrayList<String> urls, final int position, ImageBean bean) {

        double rotate = (float) bean.getHeight() / bean.getWidth();

        ImageView imageView = new ImageView(mContext);
        int width = ResourceHelper.getDimen(R.dimen.space_310);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(ResourceHelper.getDimen(R.dimen.space_310), (int) (width * rotate));
        params.topMargin = ResourceHelper.getDimen(R.dimen.space_10);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.pic_default);
        formulaImageLayout.addView(imageView);

        ImageUtil.displayImage(imageView, bean.getUrl(), R.drawable.pic_default);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPreviewInfo info = new PhotoPreviewInfo();
                info.photoList = urls;
                info.position = position;
                mPreviewPanel.setupPreviewPanel(info);
                mPreviewPanel.notifyImageViews();
                mPreviewPanel.showPanel();
            }
        });

    }

}
