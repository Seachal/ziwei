package com.laka.live.ui.course;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.income.AliPayHelper;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.CourseDetail;
import com.laka.live.bean.OrderOrderPayInfo;
import com.laka.live.bean.PayCourse;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.PayCourseMsg;
import com.laka.live.msg.QueryPayCourseMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.CourseView;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.laka.taste.wxapi.WXHelper;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import framework.ioc.annotation.InjectView;
import framework.utils.GsonTools;

/**
 * Created by Lyf on 2017/4/6.
 * 发布课程
 */

public class PayCourseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @InjectView(id = R.id.back)
    public TextView back;
    @InjectView(id = R.id.title)
    public TextView title;
    @InjectView(id = R.id.discountPrice)
    public TextView mDiscountPrice;
    @InjectView(id = R.id.actualPriceTop)
    public TextView mActualPriceTop;
    @InjectView(id = R.id.discountTv)
    public TextView mDiscountTv;
    @InjectView(id = R.id.savePrice)
    public TextView mSavePrice;
    @InjectView(id = R.id.courses)
    public LinearLayout mCourses;
    @InjectView(id = R.id.commit)
    public TextView mCommit;
    @InjectView(id = R.id.coinsTv)
    public TextView mCoinsTv;
    @InjectView(id = R.id.coinsCB)
    public CheckBox mCoinsCB;
    @InjectView(id = R.id.payLayout)
    public View mPayLayout;
    @InjectView(id = R.id.wxPay)
    public TextView mWxPay;
    @InjectView(id = R.id.aliPay)
    public TextView mAliPay;
    @InjectView(id = R.id.cb_wxPay, click = "")
    public CheckBox mCBWxPay;
    @InjectView(id = R.id.cb_aliPay, click = "")
    public CheckBox mCBAliPay;

    private PayCourse mData;
    private CourseDetail mCourseDetail; // 所有课程,用于支付页面
    public final static int LIVE = 1, VIDEO = 2;
    private final static int WECHAT_PAY = 1, ALIPAY = 2;

    private WXHelper mWXHelper;
    private AliPayHelper mAliPayHelper;
    private boolean isStartedPay = false;

    private static long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_course);
        initView();
    }

    private void initView() {

        showNewDialog("正在加载中...");
        title.setText("支付清单");
        mCoinsCB.setOnCheckedChangeListener(this);
        mCourseDetail = (CourseDetail) getIntent().getSerializableExtra("data");
        getLiveDetail();
        AnalyticsReport.onEvent(this, AnalyticsReport.PAY_COURSE_VIEW);
    }

    // 获取课程详情
    private void getLiveDetail() {

        HashMap<String, String> params = new HashMap<>();
        params.put("trailer_id", String.valueOf(mCourseDetail.course_trailer.id));// 预告id
        params.put("course_ids", GsonTools.toJson(mCourseDetail.course_ids));// 购买的数组
        DataProvider.getPayDetail(this, params, new GsonHttpConnection.OnResultListener<QueryPayCourseMsg>() {

            @Override
            public void onSuccess(QueryPayCourseMsg msg) {
                // 初始化支付清单
                initPayDetail(msg.getData());
                dismissDialog();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                ToastHelper.showToast(errorMsg);
                dismissDialog();
            }

        });

    }

    // 初始化支付清单
    private void initPayDetail(PayCourse data) {

        this.mData = data;
        initCourses(data.getCoursesDetail());
        setPrice(); // 设置价格
    }

    // 设置原总价和优惠价
    private void setPrice() {

        if (mData.getSaveCoins() > 0) {
            // 带负号，用0减
            findViewById(R.id.discountLayout).setVisibility(View.VISIBLE);
            mSavePrice.setText(NumberUtils.splitDoubleStr(0 - mData.getSaveCoins(), "味豆"));
            ViewUtils.setTextColor(mDiscountTv, "套课优惠(<font color='#FF950B'>" + mData.getDiscount() + "折</font>)");
        }
        
        float actualFee;

        // 可扣减味豆,这里服务器给的值是负的
        if (mData.getReduceCoins() < 0) {
            actualFee = mData.getActualTotalFeeUseCoins();
            findViewById(R.id.useCoinsLayout).setVisibility(View.VISIBLE);
            ViewUtils.setTextColor(mCoinsTv, "使用味豆(<font color='#FF950B'>" +NumberUtils.splitDoubleStr(mData.getReduceCoins())  + "</font>)");
        } else {
            actualFee = mData.getActualTotalFee();
            findViewById(R.id.useCoinsLayout).setVisibility(View.GONE);
        }

        if(actualFee > 0) {
            mPayLayout.setVisibility(View.VISIBLE);
        }else{
            mPayLayout.setVisibility(View.GONE);
        }

        // 设置实付价
        ViewUtils.setTextColor(mDiscountPrice, "实付  <font color='#FF950B'>" + NumberUtils.splitDoubleStr(actualFee) + "味豆</font>");
        // 设置实付价,顶部的显示
        this.mActualPriceTop.setText(NumberUtils.splitDoubleStr(actualFee) + " 味豆");

    }

    // 显示要购买的课程
    // 初始化同类课堂
    private void initCourses(List<Course> courseList) {


        if (!Utils.isEmpty(courseList)) {

            mCourses.setVisibility(View.VISIBLE);

            for (int i = 0; i < courseList.size(); ++i) {
                CourseView courseView = new CourseView(this, courseList.get(i));
                courseView.setPriceColor(R.color.color8B8B8B);
                mCourses.addView(courseView);
            }

        }

    }

    // 点击事件
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.commit:
                pay();
                HashMap<String, String> params = getParams();
                params.put("Button_name", mCommit.getText().toString());
                AnalyticsReport.onEvent(this, AnalyticsReport.PAY_COURSE_COMMIT_CLICK, params);
                break;
            case R.id.aliPay:
                mCBAliPay.setChecked(true);
                mCBWxPay.setChecked(false);
                break;
            case R.id.wxPay:
                mCBAliPay.setChecked(false);
                mCBWxPay.setChecked(true);
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    // 购买
    private void pay() {

        if(System.currentTimeMillis() - mLastClickTime < 1000){
            ToastHelper.showToast("正在请求支付,请稍候...");
            return;
        }else{
            mLastClickTime = System.currentTimeMillis();
        }

        HashMap<String, String> params = new HashMap<>();

        if (mCoinsCB.getVisibility() == View.VISIBLE) {
            params.put("usecoins", mCoinsCB.isChecked() ? "1" : "0");
        } else {
            params.put("usecoins", "0");
        }
        if(mPayLayout.getVisibility() == View.VISIBLE) {
            if(mCBWxPay.isChecked()){
                params.put("pay_type", String.valueOf(WECHAT_PAY));
            }else if(mCBAliPay.isChecked()) {
                params.put("pay_type", String.valueOf(ALIPAY));
            }
        }
        params.put("trailer_id", String.valueOf(mCourseDetail.course_trailer.id));// 预告id
        params.put("course_ids", GsonTools.toJson(mCourseDetail.course_ids));// 购买的数组
        DataProvider.payCourse(this, params, new GsonHttpConnection.OnResultListener<PayCourseMsg>() {

            @Override
            public void onSuccess(PayCourseMsg msg) {

                if (msg.getData().getPayStatus() == 1) {
                    paySuccess();
                } else {
                    if (msg.getData().getPayType() == WECHAT_PAY) {
                        doWeChatPay(msg.getData().getWechatPayParams());
                    } else if (msg.getData().getPayType() == ALIPAY) {
                        doAliPay(msg.getData().getAlipayParamsStr());
                    }
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                dismissDialog();
            }

        });

    }

    // 调起微信支付
    private void doWeChatPay(OrderOrderPayInfo payInfo) {

        if (payInfo == null) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.my_get_order_info_fail_tips));
            return;
        }
        if (mWXHelper == null) {
            mWXHelper = new WXHelper(mContext);
        }
        if (!mWXHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }
        setStartedPay(true);
        mWXHelper.sendPayReq(OrderOrderPayInfo.toPayReq(payInfo));
    }

    // 调起支付宝支付
    private void doAliPay(String payInfo) {

        showLoadingDialog("正在加载...");

        setStartedPay(true);

        mAliPayHelper = new AliPayHelper(new AliPayHelper.AliPayCallback() {
            @Override
            public void onSuccess(String payResultInfo) {
                setStartedPay(false);
                dismissLoadingsDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
                // 支付宝支付成功后的回调
                paySuccess();
            }

            @Override
            public void onCancel() {
                setStartedPay(false);
                dismissLoadingsDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
            }

            @Override
            public void onWait() {
                dismissLoadingsDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_wait_tips));
            }

            @Override
            public void onFailed() {
                setStartedPay(false);
                dismissLoadingsDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_fail_tips));
            }
        });

        mAliPayHelper.startPayTask(mContext, payInfo);

    }

    private HashMap<String, String> getParams() {

        HashMap<String, String> params = new HashMap<>();

        if (mData != null)
            params.put("Class_type", mCourseDetail.course.type == LIVE ? "22" : "23");

        return params;
    }

    // 支付成功
    private void paySuccess() {

        // 只有订阅一节课的时候，购买成功页，才会有播放按钮。但又分为直播和视频，如果是视频，就直接观看。
        // 如果是直播，还得判断当前课程是否处于直播状态
        if (mCourseDetail.course.isLive() && mCourseDetail.course.status == Course.LIVING || (mCourseDetail.course_ids.size() == 1)) {
            mCourseDetail.course.setUser(mCourseDetail.user);
            mCourseDetail.course.setRoom(mCourseDetail.room);
            PostSuccessActivity.startActivity(mContext, PostSuccessActivity.PAYCOURSESUCCESS,
                    mCourseDetail.course_trailer.getId(), mCourseDetail.course);
        } else {
            PostSuccessActivity.startActivity(mContext, PostSuccessActivity.PAYCOURSESUCCESS,
                    mCourseDetail.course_trailer.getId(), mCourseDetail.course);
        }
        finish();
        dismissDialog();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        float actualFee;

        // 可扣减味豆,这里服务器给的值是负的
        if (isChecked) {
            actualFee = mData.getActualTotalFeeUseCoins();
        } else {
            actualFee = mData.getActualTotalFee();
        }

        if(actualFee > 0) {
            mPayLayout.setVisibility(View.VISIBLE);
        }else{
            mPayLayout.setVisibility(View.GONE);
        }

        // 设置实付价
        ViewUtils.setTextColor(mDiscountPrice, "实付  <font color='#FF950B'>" + NumberUtils.splitDoubleStr(actualFee) + "味豆</font>");
        // 设置实付价,顶部的显示
        this.mActualPriceTop.setText(NumberUtils.splitDoubleStr(actualFee) + " 味豆");
    }

    public boolean isStartedPay() {
        return isStartedPay;
    }

    public void setStartedPay(boolean startedPay) {
        isStartedPay = startedPay;
    }

    @Subscribe
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (isStartedPay() && SubcriberTag.MSG_RECHARGE_SUCCESS.equals(event.tag)) {
            setStartedPay(false);
            int resultCode = (int) event.event;
            handleOnWeChatPayResultCallback(resultCode);
        } else if (SubcriberTag.CLICK_FLOAT_LIVE.equals(event.tag)) {
            finish();
        }
    }

    // 微信支付成功的回调
    public void handleOnWeChatPayResultCallback(int resultCode) {

        if (resultCode == BaseResp.ErrCode.ERR_OK) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
            paySuccess();
            return;
        }
        if (resultCode == WXHelper.WE_CHAT_PAY_TYPE_CANCEL) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
            return;
        }

    }

}
