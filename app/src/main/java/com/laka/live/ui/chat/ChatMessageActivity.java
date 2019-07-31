package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.CourseDetail;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.bean.ShoppingOrderDetailGoodsBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.UnicornHelper;
import com.laka.live.util.Utils;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFUserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import framework.utils.GsonTools;

/**
 * Created by crazyguan on 2016/3/30.
 * 私信聊天
 */
public class ChatMessageActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ChatMessageActivity";

    ChatMessageView messgeView;
    public static String otherUserId;
    private String otherNickName, otherAvatar;//对方内容
    private int sessionType, mishuType = -1;
    private ShoppingGoodsDetailBean goodsBean;//相关商品
    private CourseDetail course;//课程详情


    /**
     * 在线客服
     * @param activity
     */
    public static void startActivity(Activity activity) {
        UnicornHelper.openServiceActivity(activity, null);
    }

    /**
     * 在线客服
     * @param course 带课程详情
     */
    public static void startActivity(Activity activity,CourseDetail course) {

        UnicornHelper.openServiceActivity(activity, new ProductDetail.Builder()
                .setTitle(course.getCourse().getTitle())
                .setShow(1)
                .setNote("")
                .setExt("")
                .setDesc(course.getCourse().getSummary())
                .setUrl(Common.SHARE_COURSE_URL + course.getCourse().getId())
                .setAlwaysSend(true)
                .setPicture(course.getCourse().getCover_url())
                .build());

    }

    /**
     * 在线客服
     * @param goodsBean 带商品详情
     */
    public static void startActivity(Activity activity, ShoppingGoodsDetailBean goodsBean) {

        UnicornHelper.openServiceActivity(activity, new ProductDetail.Builder()
                .setShow(1)
                .setNote("")
                .setExt("")
                .setDesc("")
                .setAlwaysSend(true)
                .setUrl(Common.SHARE_GOODS_URL + goodsBean.getGoodsId())
                .setTitle(goodsBean.getTitle())
                .setPicture(goodsBean.getThumbUrl())
                .build());

    }

    // 私信
    public static void startPrivateChatActivity(Activity activity, String otherUserId, String otherNickName, String otherAvatar, int sessionType) {
        if (TextUtils.isEmpty(otherUserId)) {
            return;
        }
        Log.d(TAG, " startActivity otherUserId=" + otherUserId + " otherNickName=" + otherNickName + " sessionType=" + sessionType);
        if (activity != null) {
            Intent intent = new Intent(activity, ChatMessageActivity.class);
            intent.putExtra("otherUserId", otherUserId);
            intent.putExtra("otherNickName", otherNickName);
            intent.putExtra("otherAvatar", otherAvatar);
            intent.putExtra("sessionType", sessionType);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    // 私信
    public static void startPrivateChatActivity(Context activity, String otherUserId,
                                                String otherNickName, String otherAvatar,
                                                int sessionType, int mishuChatType) {
        if (TextUtils.isEmpty(otherUserId)) {
            return;
        }

        Log.d(TAG, " startActivity otherUserId=" + otherUserId + " otherNickName=" + otherNickName + " sessionType=" + sessionType);
        if (activity != null) {
            Intent intent = new Intent(activity, ChatMessageActivity.class);
            intent.putExtra("otherUserId", otherUserId);
            intent.putExtra("otherNickName", otherNickName);
            intent.putExtra("otherAvatar", otherAvatar);
            intent.putExtra("sessionType", sessionType);
            intent.putExtra("mishuType", mishuChatType);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        Log.d("ChatMessageView", "chatMsgActivity");
        init();
        initData();
    }


    private void init() {
        messgeView = (ChatMessageView) findViewById(R.id.message_view);
        messgeView.start();
        AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_15006);
    }


    private void initData() {
        otherUserId = getIntent().getStringExtra("otherUserId");
        if (Utils.isEmpty(otherUserId)) {
            showToast("用户不存在");
            finish();
        }

        otherNickName = getIntent().getStringExtra("otherNickName");
        if (otherNickName == null) {
            otherNickName = "";
        }
        otherAvatar = getIntent().getStringExtra("otherAvatar");
        if (otherAvatar == null) {
            otherAvatar = "";
        }
        sessionType = getIntent().getIntExtra("sessionType", 0);
        mishuType = getIntent().getIntExtra("mishuType", -1);
        messgeView.setFrom(Common.FROM_PRIVATE_MSG);
        messgeView.initData(this, otherUserId, otherNickName, otherAvatar, sessionType, mishuType);

        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            if (bd.containsKey("goodsBean")) {
                goodsBean = (ShoppingGoodsDetailBean) bd.getSerializable("goodsBean");
                Log.d(TAG, "有商品");
                messgeView.showGood(goodsBean);
            } else {
                Log.d(TAG, "没商品");
            }
            if (bd.containsKey("course")) {
                course = (CourseDetail) bd.getSerializable("course");
                Log.d(TAG, "有课程");
                messgeView.showCourse(course);
            } else {
                Log.d(TAG, "没课程");
            }

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_send:
//                addChat();
//                break;
//            case R.id.iv_face://表情
//
//                break;
        }
    }

    /**
     * 聊天列表中对内容的点击事件监听
     */
    public interface OnChatItemClickListener {
        void onPhotoClick(int position);

        void onTextClick(int position);

        void onFaceClick(int position);
    }

    /**
     * @return 聊天列表内存点击事件监听器
     */
    private OnChatItemClickListener getOnChatItemClickListener() {
        return new OnChatItemClickListener() {
            @Override
            public void onPhotoClick(int position) {

            }

            @Override
            public void onTextClick(int position) {
            }

            @Override
            public void onFaceClick(int position) {
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        otherUserId = "";
        messgeView.stop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果软键盘弹起，收起
//            if (box.emoticonPickerView.getVisibility() == View.VISIBLE) {
//                box.hideLayout();
//                return true;
//            } else if (box.isKeyboradShow) {
//                box.hideKeyboard(this);
//                return true;
//            }
        }
        return super.onKeyDown(keyCode, event);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (SubcriberTag.SOCKET_NOT_CONNECT.equals(event.tag)) {
            if (isResume()) {
                showToast(R.string.service_not_connect);
            }
        } else if (SubcriberTag.CLICK_FLOAT_LIVE.equals(event.tag)) {
            finish();
        }
    }
}
