package com.laka.live.ui.course.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.ImageBean;
import com.laka.live.photopreview.PhotoPreviewInfo;
import com.laka.live.photopreview.PhotoPreviewPanel;
import com.laka.live.ui.widget.CourseView;
import com.laka.live.ui.widget.course.CoursesDiscountTimeView;
import com.laka.live.util.ArrayUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/9/12.
 */
public class CourseDetailView extends BaseDetailView implements View.OnClickListener {

    @InjectView(id = R.id.discount)
    private TextView mDiscountTv;
    @InjectView(id = R.id.courseSummary)
    private TextView mCourseSummary;
    @InjectView(id = R.id.formulaLocked)
    private TextView mFormulaLocked;
    @InjectView(id = R.id.courseFormula)
    private TextView mCourseFormula;
    @InjectView(id = R.id.formulaImage)
    private LinearLayout mFormulaImageLayout;
    @InjectView(id = R.id.similarCourse)
    public LinearLayout mSimilarCourse;
    @InjectView(id = R.id.summaryImages)
    private LinearLayout mSummaryImages;
    @InjectView(id = R.id.coursesDiscountTimeView)
    private CoursesDiscountTimeView mCoursesDiscountTimeView;

    private PhotoPreviewPanel mPreviewPanel;

    public CourseDetailView(Context context) {
        this(context, null);
    }

    public CourseDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(this, R.layout.view_course_detail);
    }

    @Override
    protected void initData(CourseDetailHelper mHelper) {
        super.initData(mHelper);

        if (!mHelper.isRefresh()) {
            initCourses();
            initSummaryImages();
            mPreviewPanel = new PhotoPreviewPanel(mContext);
        }

        // 配方
        initFormulaImages();

        // 设置套课折扣
        setSimilarCourseDiscount();

        // 设置空布局
        setEmptyView();
    }

    private void setEmptyView() {

        if (mHelper.getCourseDetail().hasSimilarCourse()
                || Utils.isNotEmpty(mHelper.getCourse().getSummaryImages())
                || Utils.isNotEmpty(mHelper.getCourse().getSummary())
                || Utils.isNotEmpty(mHelper.getCourse().getFormula())
                || Utils.isNotEmpty(mHelper.getCourse().getFormulaImages())) {
            mRootView.findViewById(R.id.emptyView).setVisibility(GONE);
        } else {
            mRootView.findViewById(R.id.emptyView).setVisibility(VISIBLE);
        }

    }

    // 设置课程简介
    private void initSummaryImages() {

        // 显示课程简介
        if (Utils.isNotEmpty(mHelper.getCourse().getSummaryImages()) || Utils.isNotEmpty(mHelper.getCourse().getSummary())) {
            mRootView.findViewById(R.id.summaryLayout).setVisibility(View.VISIBLE);
            mCourseSummary.setText(mHelper.getCourse().getSummary());
            ArrayList<String> urls = ArrayUtil.getUrls(mHelper.getCourse().getSummaryImages());
            for (int position = 0; position < mHelper.getCourse().getSummaryImages().size(); ++position) {
                ImageBean bean = mHelper.getCourse().getSummaryImages().get(position);
                showImages(urls, position, mSummaryImages,null, bean.getUrl(), bean.getWidth(), bean.getHeight());
            }
        }
    }

    // 设置配方
    private void initFormulaImages() {

        if (Utils.isNotEmpty(mHelper.getCourse().getFormula())
                || Utils.isNotEmpty(mHelper.getCourse().getFormulaImages())) {

            mRootView.findViewById(R.id.formulaLayout).setVisibility(View.VISIBLE);

            if(mFormulaImageLayout.getChildCount() == 0) {
                if (mHelper.getCourse().hasBuy() || mHelper.isMyCourse()) {
                    mCourseFormula.setText(mHelper.getCourse().getFormula());
                    mFormulaLocked.setVisibility(View.GONE);

                    // 显示配方图片
                    ArrayList<String> urls = ArrayUtil.getUrls(mHelper.getCourse().getFormulaImages());
                    for (int position = 0; position < mHelper.getCourse().getFormulaImages().size(); ++position) {
                        ImageBean bean = mHelper.getCourse().getFormulaImages().get(position);
                        mFormulaImageLayout.setVisibility(View.VISIBLE);
                        showImages(urls, position, mFormulaImageLayout,null, bean.getUrl(), bean.getWidth(), bean.getHeight());
                    }
                }
            }

        }
    }

    // 显示课程简介图片
    private void showImages(final ArrayList<String> urls, final int position, LinearLayout linearLayout,ImageView imageView, final String url, int width, int height) {

        double rotate = (float) height / width;

        if (imageView == null) {

            imageView = new ImageView(mContext);
            width = ResourceHelper.getDimen(R.dimen.space_310);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(ResourceHelper.getDimen(R.dimen.space_310), (int) (width * rotate));
            params.topMargin = ResourceHelper.getDimen(R.dimen.space_10);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.drawable.pic_default);
            linearLayout.addView(imageView);
        } else {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = ResourceHelper.getDimen(R.dimen.space_310);
            params.height = (int) (params.width * rotate);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.pic_default);
            imageView.setLayoutParams(params);
        }

        ImageUtil.displayImage(imageView, url, R.drawable.pic_default);

        imageView.setOnClickListener(new OnClickListener() {
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


    // 初始化同类课堂
    private void initCourses() {

        if (mHelper.getCourseDetail().hasSimilarCourse()) {

            mSimilarCourse.setVisibility(VISIBLE);

            for (int count = 0; count < mHelper.getCourseDetail().getSimilarCourses().size(); ++count) {

                final Course course = mHelper.getCourseDetail().getSimilarCourses().get(count);
                CourseView courseView = new CourseView(mContext, course);
                courseView.setCourseNum(count);
                // item点击跳转
                courseView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.COURSE_DETAIL_SIMILAR_CLICK, mHelper.getEventParams());
                        CourseDetailActivity.startActivity(mContext, course.getId());
                        mHelper.getActivity().finish();
                    }
                });
                // 添加课程
                mSimilarCourse.addView(courseView);
            }

        } else {
            mSimilarCourse.setVisibility(GONE);
        }

    }

    // 设置套课折扣
    private void setSimilarCourseDiscount() {

        // 如果是套课就显示
        if (mHelper.getCourseDetail().hasSimilarCourse()) {
            mSimilarCourse.setVisibility(View.VISIBLE);
            mCoursesDiscountTimeView.setCoursesDiscountTimer(mHelper);
        } else {
            mSimilarCourse.setVisibility(View.GONE);
        }

        // 显示折扣,如果有
        if (mHelper.getCourseTrailer().isDiscount()) {
            mDiscountTv.setVisibility(VISIBLE);
            mDiscountTv.setText(NumberUtils.getCoursePriceFormat(mHelper.getCourseTrailer().getDiscount(), "折"));
        } else {
            mDiscountTv.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

}
