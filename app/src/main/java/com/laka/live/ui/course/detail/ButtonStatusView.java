package com.laka.live.ui.course.detail;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/9/12.
 */
public class ButtonStatusView extends BaseDetailView implements View.OnClickListener {

    @InjectView(id = R.id.leftBtn)
    private TextView leftBtn;
    @InjectView(id = R.id.rightBtn)
    private TextView rightBtn;

    public ButtonStatusView(Context context) {
        this(context, null);
    }

    public ButtonStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(this, R.layout.view_course_button_status);
    }

    @Override
    protected void initData(CourseDetailHelper mHelper) {
        super.initData(mHelper);
        setButtonStatus();
    }

    // 显示底部按钮
    private void setButtonStatus() {

        // 根据不同状态，显示按钮的文本
        if (mHelper.isMyCourse()) {

            setMyCourseStatus(mHelper.getCourse().getStatus());

            if (mHelper.getCourse().getStatus() == Course.CANCEL) {
                leftBtn.setVisibility(View.GONE);
            }

        } else {
            if (mHelper.getCourse().hasBuy()) {
                setOtherCourseStatus(mHelper.getCourse().getStatus());
            } else {

                if (!mHelper.getCourseDetail().hasSimilarCourse()) {

                    rightBtn.setVisibility(View.GONE);
                    leftBtn.setBackgroundResource(R.drawable.yellow_btn_selector);

                    if (mHelper.getCourseTrailer().getSavedCoins() > 0) {

                        //判断课程是否免费
                        StringBuffer buyHint = new StringBuffer("购买本节课程 (省");
                        int spanStartIndex = buyHint.toString().length() - 2;
                        if (mHelper.getCourse().getPrice() - mHelper.getCourseTrailer().getSavedCoins() == 0) {
                            buyHint.insert(0, "免费");
                            spanStartIndex += 2;
                        }
                        leftBtn.setText(buyHint.toString() + NumberUtils.splitDoubleStr(mHelper.getCourseTrailer().getSavedCoins()) + "味豆)");
                        Spannable span = new SpannableString(leftBtn.getText());
                        span.setSpan(new ForegroundColorSpan(Color.WHITE), spanStartIndex, leftBtn.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        span.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), spanStartIndex, leftBtn.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
                        span.setSpan(new AbsoluteSizeSpan((int) (11 * mContext.getResources().getDisplayMetrics().density + 0.5f)), spanStartIndex,
                                leftBtn.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        leftBtn.setText(span);
                    } else {
                        StringBuffer buyHint = new StringBuffer("购买本节课程");
                        if (mHelper.getCourse().getPrice() - mHelper.getCourseTrailer().getSavedCoins() == 0) {
                            buyHint.insert(0, "免费");
                        }
                        leftBtn.setText(buyHint.toString());
                    }

                } else {
                    float totalCount = 0f;
                    for (Course similarCourse : mHelper.getCourseDetail().getSimilarCourses()) {
                        totalCount += similarCourse.getPrice();
                    }
                    //存在折扣的情况下，需要遍历当前套课课程，判断价格是否为免费
                    if (mHelper.getCourseTrailer().getSavedCoins() > 0) {
                        // 没有买->购买课堂
                        StringBuffer buyHint = new StringBuffer("购买套课 (省");
                        int spanStartIndex = buyHint.toString().length() - 2;
                        //需要遍历套课的价格和折扣。判断是否免费
                        if (totalCount - mHelper.getCourseTrailer().getSavedCoins() == 0) {
                            buyHint.insert(0, "免费");
                            spanStartIndex += 2;
                        }

                        if (mHelper.getCourse().getPrice() == 0) {
                            leftBtn.setText("免费购买本节课程");
                        } else {
                            leftBtn.setText("购买本节课程");
                        }

                        rightBtn.setText(buyHint.toString() + mHelper.getCourseTrailer().getSavedCoins() + "味豆)");
                        Spannable span = new SpannableString(rightBtn.getText());
                        span.setSpan(new ForegroundColorSpan(Color.WHITE), spanStartIndex, rightBtn.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        span.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), spanStartIndex, rightBtn.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
                        span.setSpan(new AbsoluteSizeSpan((int) (11 * mContext.getResources().getDisplayMetrics().density + 0.5f)), spanStartIndex,
                                rightBtn.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rightBtn.setText(span);
                    } else {
                        //当前套课没折扣的情况下---可能存在当前课程为免费，但是其他套课不为免费的情况
                        //和套课全部为免费的情况
                        if (totalCount == 0) {
                            rightBtn.setText("免费购买套课");
                            leftBtn.setText("免费购买本节课程");
                        } else {
                            if (mHelper.getCourse().getPrice() == 0) {
                                leftBtn.setText("免费购买本节课程");
                            } else {
                                leftBtn.setText("购买本节课程");
                            }
                            rightBtn.setText("购买套课");
                        }
                    }
                }
            }
        }

    }

    // 对外方法，可以用来刷新我的按钮
    public void updateMyCourseStatus(int status) {
        setMyCourseStatus(status);  // 在这可以做一些刷新我的按钮的之前的事的处理
    }

    // 根据不同的按钮状态，设置我的课程状态
    private void setMyCourseStatus(int status) {

        leftBtn.setText("编辑课堂");  // 左边的按钮，统一显示编辑课堂

        switch (status) {
            case Course.CANCEL: // 已取消
                rightBtn.setEnabled(false);
                rightBtn.setText("已取消");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.NOTSTART: // 即将开播
                rightBtn.setEnabled(false);
                rightBtn.setText("即将开播");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.CHANGETIME: // 调整直播时间，一样去编辑课堂
                rightBtn.setEnabled(false);
                rightBtn.setText("开播时间调整");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.CREATINGPLAYBACK: // 生成回放中
                rightBtn.setEnabled(false);
                rightBtn.setText("回放保存中");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.CREATEDPLAYBACK: // 已生成回放
                rightBtn.setText("观看回放");
                rightBtn.setEnabled(true);
                rightBtn.setBackground(ResourceHelper.getDrawable(R.drawable.yellow_btn_selector));
                break;
            case Course.ALREADY: // 准备中
                rightBtn.setEnabled(true);
                rightBtn.setBackground(ResourceHelper.getDrawable(R.drawable.yellow_btn_selector));
                rightBtn.setText("开始直播");
                break;
            case Course.LIVING: // 直播中
                rightBtn.setEnabled(true);
                rightBtn.setBackground(ResourceHelper.getDrawable(R.drawable.yellow_btn_selector));
                rightBtn.setText("继续直播");
                break;
            case Course.PLAYVIDEO: // 课程视频已可播放
                rightBtn.setText("观看视频");
                rightBtn.setEnabled(true);
                rightBtn.setBackground(ResourceHelper.getDrawable(R.drawable.yellow_btn_selector));
                break;
            default:
                break;
        }

    }

    // 根据不同的按钮状态，设置其他主播的课程状态
    private void setOtherCourseStatus(int status) {

        // 已购买的只需显示右边一个按钮
        leftBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.VISIBLE);

        switch (status) {
            case Course.CANCEL: // 已取消
                rightBtn.setEnabled(false);
                rightBtn.setText("课程已取消");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.NOTSTART: // 未开播
                rightBtn.setEnabled(false);
                rightBtn.setText("即将开播");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.CHANGETIME: // 调整直播时间，一样去编辑课堂
                rightBtn.setEnabled(false);
                rightBtn.setText("开播时间调整");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.CREATINGPLAYBACK: // 生成回放中
                rightBtn.setEnabled(false);
                rightBtn.setText("回放保存中");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.ALREADY: // 准备中
                rightBtn.setEnabled(false);
                rightBtn.setText("即将开播");
                rightBtn.setBackgroundResource(R.color.colorCCCCCC);
                break;
            case Course.CREATEDPLAYBACK: // 已生成回放
                StringBuffer replyHint = new StringBuffer("观看课程回放");
                if (mHelper.getCourse().getPrice() - mHelper.getCourseTrailer().getSavedCoins() == 0) {
                    replyHint.insert(0, "免费");
                }
                rightBtn.setText(replyHint);
                rightBtn.setEnabled(true);
                rightBtn.setBackground(ResourceHelper.getDrawable(R.drawable.yellow_btn_selector));
                break;
            case Course.LIVING: // 直播中
                rightBtn.setText("观看直播");
                rightBtn.setEnabled(true);
                rightBtn.setBackground(ResourceHelper.getDrawable(R.drawable.yellow_btn_selector));
                break;
            case Course.PLAYVIDEO: // 视频类型课程
                rightBtn.setText("观看视频");
                rightBtn.setEnabled(true);
                rightBtn.setBackground(ResourceHelper.getDrawable(R.drawable.yellow_btn_selector));
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

}
