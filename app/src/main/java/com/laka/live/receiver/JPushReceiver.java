package com.laka.live.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.income.MyIncomeActivity;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.account.my.MyCoinsActivity;
import com.laka.live.account.my.MyFansActivity;
import com.laka.live.application.LiveApplication;
import com.laka.live.dao.DbManger;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.DataProvider;
import com.laka.live.ui.activity.MainActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.chat.ChatHomeActivity;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.fragment.FollowFragment;
import com.laka.live.ui.homepage.FollowNewsActivity;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by luwies on 16/4/8.
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TYPE_OPEN = "open";

    private static final String TYPE_GIVE = "give";

    private static final String TAG = "JPushReceiver";

    private static final String EXTRA = "cn.jpush.android.EXTRA";

    private static final String EXTRAS = "extras";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + action + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {

            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
            if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                DataProvider.postJPushId(regId);
            }
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            //刷新首页未读消息
            EventBusManager.postEvent(0, SubcriberTag.REFRESH_HOME_UNREAD_RED);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            /*String extras = bundle.getString(JPushInterface.EXTRA_MESSAGE);

            Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            //打开自定义的Activity
            *//*Intent i = new Intent(context, LoginActivity.class);
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*//*
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            context.startActivity(i);*/

            openNotification(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(action)) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(action)) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.debug(TAG, "[MyReceiver]" + action + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + action);
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.d(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.error(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void openNotification(Context context, Bundle bundle) {
        String json = bundle.getString(EXTRA);
        Log.d(TAG, " openNotification bundle=" + bundle);

        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String type = jsonObject.getString(Common.TYPE);
                String value = jsonObject.getString(Common.VALUE);
                Log.d(TAG, " openNotification type=" + type + " value=" + value);

                if ("1".equals(type)) {
                    Intent intent = new Intent(context, CourseDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bd = new Bundle();
                    bd.putString("courseId", value);
                    bd.putBoolean("isMine", true);
                    intent.putExtras(bd);
                    context.startActivity(intent);
//                    goCourseDetail(context,value);
                } else if ("4".equals(type)) {
                    Intent intent = new Intent(context, CourseDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bd = new Bundle();
                    bd.putString("courseId", value);
                    bd.putBoolean("isMine", false);
                    intent.putExtras(bd);
                    context.startActivity(intent);
                } else if ("2".equals(type)) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra(WebActivity.URL_EXTRA, value);
                    Log.d(TAG, " url=" + value);
                    intent.putExtra(WebActivity.TITLE_EXTRA, "网页推送");
                    context.startActivity(intent);
//                    WebActivity.startActivity(context, value, "网页推送");
                } else if ("3".equals(type)) {
                    if ("1".equals(value)) {//首页
                        goMainActivity(context, MainActivity.TAB_MAIN);
//                        goTopActivity(context, 1);
                    } else if ("2".equals(value)) {//关注
//                        goTopActivity(context, 2);
                    } else if ("3".equals(value)) {//私信
//                        goTopActivity(context, 3);
                        Intent intent = new Intent(context, ChatHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
//                        ChatHomeActivity.startActivity(context);
                    } else if ("4".equals(value)) {//我的
//                        goTopActivity(context, 4);
                        goMainActivity(context, MainActivity.TAB_MINE);
                    } else if ("5".equals(value)) {//我的-我的粉丝
                        Intent intent = new Intent(context, MyFansActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(MyFansActivity.EXTRA_FAN_ID_NAME, AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
                        intent.putExtra(MyFansActivity.EXTRA_FAN_FROM_NAME, FollowFragment.FROM_MY);
                        context.startActivity(intent);
                    } else if ("6".equals(value)) {//我的-我的收益
                        Intent intent = new Intent(context, MyIncomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
//                        MyIncomeActivity.startActivity(context);
                    } else if ("7".equals(value)) {//我的-我的钱包
                        Intent intent = new Intent(context, MyCoinsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
//                        MyCoinsActivity.startActivity(context);
                    } else if ("8".equals(value)) {  //关注资讯
                        Intent intent = new Intent(context, FollowNewsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                } else if (TextUtils.equals(type, TYPE_GIVE)) {
                    String id = jsonObject.getString(Common.USER_ID);
                    goChatActivity(context, id);
                } else {
//                    goTopActivity(context);
                    goMainActivity(context, MainActivity.TAB_MAIN);
                }

//                String type = jsonObject.getString(Common.TYPE);
//                String id;
//                if (TextUtils.equals(type, TYPE_OPEN)) {
//                    id = jsonObject.getString(Common.ROOM_ID);
//                    String streamId = jsonObject.getString(Common.STREAM_ID);
//                    String channelId = jsonObject.getString(Common.CHANNEL_ID);
//                    Log.d(TAG, "收到推送 streamId=" + streamId+" channelId="+channelId);
//                    goLiveRoom(context, id, streamId,channelId);
//                } else if (TextUtils.equals(type, TYPE_GIVE)) {
//                    id = jsonObject.getString(Common.USER_ID);
//                    goChatActivity(context, id);
//                } else {
//                    goTopActivity(context);
//                }

            } catch (JSONException e) {
//                goTopActivity(context);
                goMainActivity(context, MainActivity.TAB_MAIN);
            }
        } else {
//            goTopActivity(context);
            goMainActivity(context, MainActivity.TAB_MAIN);
        }
    }

    private static void goTopActivity(Context context) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(context.getPackageName(), LoginActivity.class.getName());
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private static void goTopActivity(Context context, int tabIndex) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(context.getPackageName(), LoginActivity.class.getName());
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("tabIndex", tabIndex);
        context.startActivity(i);
    }

    /**
     * @param tabId 值必须是MainActivity的三个TAB_XXX常量
     */
    private static void goMainActivity(Context context, int tabId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tabId", tabId);
        context.startActivity(intent);
    }

    private static void goLoginActivity(Context context, Bundle bundle) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(context.getPackageName(), LoginActivity.class.getName());
//        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtras(bundle);
        context.startActivity(i);
    }

    private static void goLiveRoom(Context context, String id, String streamId, String channelId) {
        List<Activity> activities = LiveApplication.getInstance().getActivitys();
        Activity activity = null;
        if (activities != null && activities.isEmpty() == false) {
            activity = activities.get(0);
        }
        Bundle params = new Bundle();
        params.putString(Common.ROOM_ID, id);
        params.putString(Common.STREAM_ID, id);
        params.putString(Common.CHANNEL_ID, id);
//        params.putString(Common.DOWN_URL, downUrl);
        if (AccountInfoManager.getInstance().checkUserIsLogin() && activity != null) {
            if (TextUtils.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), id)) {
                goTopActivity(context);
            } else {
                if (Utils.isEmpty(streamId)) {
                    ToastHelper.showToast(R.string.no_live_streamid);
                    return;
                }
                if (Utils.isEmpty(channelId)) {
                    ToastHelper.showToast(R.string.no_live_channel);
                    return;
                }
                LiveRoomActivity.startPlay(activity, Integer.parseInt(id), false, "", streamId, channelId, "", "", Common.FROM_PUSH, "");
            }
        } else {
            if (Utils.isEmpty(streamId)) {
                ToastHelper.showToast(R.string.no_live_streamid);
                return;
            }
            if (Utils.isEmpty(channelId)) {
                ToastHelper.showToast(R.string.no_live_channel);
                return;
            }
            goLoginActivity(context, params);
        }
    }

    private static void goChatActivity(Context context, String id) {
        List<Activity> activities = LiveApplication.getInstance().getActivitys();
        Activity activity = null;
        if (activities != null && activities.isEmpty() == false) {
            activity = activities.get(0);
        }
        Bundle params = new Bundle();
        params.putString(Common.USER_ID, id);


        if (AccountInfoManager.getInstance().checkUserIsLogin() && activity != null) {
            if (TextUtils.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), id)) {
                goTopActivity(context);
            } else {
                if (!id.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
                    Intent intent = new Intent(activity, ChatMessageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("otherUserId", id);
                    intent.putExtra("otherNickName", "");
                    intent.putExtra("otherAvatar", "");
                    intent.putExtra("sessionType", DbManger.SESSION_TYPE_UNFOLLOW);
                    ActivityCompat.startActivity(activity, intent, null);
//                    ChatMessageActivity.startActivity(activity, id, "", "", DbManger.SESSION_TYPE_UNFOLLOW);
                }

            }
        } else {
            goLoginActivity(context, params);
        }
    }

    private static void goCourseDetail(Context context, String courseId) {
        List<Activity> activities = LiveApplication.getInstance().getActivitys();
        Activity activity = null;
        if (activities != null && activities.isEmpty() == false) {
            activity = activities.get(0);
        }
        Bundle params = new Bundle();
        if (AccountInfoManager.getInstance().checkUserIsLogin() && activity != null) {
            CourseDetailActivity.startActivity(context, courseId);
        } else {
            goLoginActivity(context, params);
        }

    }
}
