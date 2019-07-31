package com.laka.live.pop;

import android.Manifest;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.dialog.NewLoadingDialog;
import com.laka.live.msg.OpenLiveMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.course.AddNewsActivity;
import com.laka.live.ui.course.PostCourseActivity;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import framework.ioc.annotation.InjectView;
import framework.utils.PermissionUtils;

/**
 * Created by Lyf on 2017/6/7.
 */
public class PostCoursePop extends BasePop {

    @InjectView(id = R.id.live)
    public TextView live;
    @InjectView(id = R.id.video)
    public TextView video;
    @InjectView(id = R.id.news)
    public TextView news;
    @InjectView(id = R.id.testLive)
    public TextView testLive;

    private NewLoadingDialog loadingNewDialog;

    private PostCoursePop(Activity context) {
        super(context);
        setContentView(R.layout.pop_post_type);
    }

    public static PostCoursePop getPostCoursePop(Activity activity) {
        return new PostCoursePop(activity);
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.live:
                // 直播课堂
                Utils.startActivity(mContext, PostCourseActivity.class, Course.LIVE);
                break;
            case R.id.video:
                // 视频课堂
                Utils.startActivity(mContext, PostCourseActivity.class, Course.VIDEO);
                break;
            case R.id.news:
                // 资讯课堂
                Utils.startActivity(mContext, AddNewsActivity.class);
                break;
            case R.id.testLive:
                // 直播测试
                getTestPushUrl();
                break;
        }
        dismiss();

    }

    /**
     * 直播测试
     */
    public void getTestPushUrl() {

        showNewDialog("正在加载测试直播间数据...");

        DataProvider.openTestLive(mContext, 1, new GsonHttpConnection.OnResultListener<OpenLiveMsg>() {
            @Override
            public void onSuccess(OpenLiveMsg openLiveMsg) {
                if (loadingNewDialog != null) {
                    loadingNewDialog.dismiss();
                }
                if (openLiveMsg.isSuccessFul()) {
                    if (PermissionUtils.hasGrant(mContext, Manifest.permission.RECORD_AUDIO)
                            && PermissionUtils.hasGrant(mContext, Manifest.permission.CAMERA)) {
                        LiveRoomActivity.startTestLive(mContext, openLiveMsg.getCourseId()); // 有权限开始直播
                    } else {
                        PermissionUtils.checkPermissions(mContext, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
                    }

                } else {
                    ToastHelper.showToast("创建直播测试失败");
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                if (loadingNewDialog != null) {
                    loadingNewDialog.dismiss();
                }
                ToastHelper.showToast("创建直播测试失败");
            }
        });

    }


    // 显示新版进度圈
    public void showNewDialog(String msg) {

        if (loadingNewDialog == null) {
            loadingNewDialog = new NewLoadingDialog(mContext);
            loadingNewDialog.setCanceledOnTouchOutside(false);
        }

        // 如果已经在显示
        if (!loadingNewDialog.isShowing())
            loadingNewDialog.show();

    }


}