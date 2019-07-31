package com.laka.live.ui.course.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.help.PostEvent;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.ToastHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.jzvd.Jzvd;
import framework.ioc.annotation.InjectExtra;

/**
 * Created by Lyf on 2017/6/5.
 * getCourseDetail() 获取课程详情
 * CourseDetailHelper 进行业务逻辑处理
 */
public class CourseDetailActivity extends BaseActivity {

    @InjectExtra(name = "courseId")
    public String courseId; // 课程Id

    // Activity的业务和布局的辅助类,Activity只做事件分发
    private CourseDetailHelper mHelper;

    public static void startActivity(Context context, String courseId) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("courseId", courseId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        mHelper = new CourseDetailHelper(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取课程详情的数据
        mHelper.getData(courseId);
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.title:
            case R.id.trailerCover:
                mHelper.onTrailerClick(); // 播放预告
                break;
            case R.id.leftBtn: // 左边按钮点击事件
                mHelper.onLeftBtnClick();
                break;
            case R.id.rightBtn: // 右边按钮点击事件
                mHelper.onRightBtnClick();
                break;
            case R.id.userInfoView: // 跳到个人主页
                mHelper.onUserInfoClick();
                break;
            case R.id.formulaLocked: // 点击配方锁，跳到支付页面
                mHelper.onFormulaLockedClick();
                break;
            case R.id.share:
            case R.id.proxyShare:
                mHelper.onShareClick(); // 分享课程
                break;
            case R.id.iv_service:
                mHelper.onServiceClick(); // 点击客服
                break;
            case R.id.back:
                finish();
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        mHelper.onEvent(event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; ++i) {

            if (permissions[i].equals(Manifest.permission.RECORD_AUDIO) && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                ToastHelper.showToast("没有录音权限");
                return;
            } else if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                ToastHelper.showToast("没有摄像头权限");
                return;
            }

        }

        // 如果有这两个权限，就可以开直播
        if (mHelper.getCourseStatus() == Course.LIVING) {
            mHelper.startLive(); // 如果是正在直播的，就不用再查了，直接开始直播
        } else {
            mHelper.queryLivingState();// 需要查询有没有在直播
        }

    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        } else if (mHelper.getChatKeyboard().getVisibility() == View.VISIBLE) {
            mHelper.getChatKeyboard().release();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.onDestroy();
    }

}