package com.laka.live.network;

import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.UserInfo;
import com.laka.live.msg.JoinLiveMsg;
import com.laka.live.msg.Msg;
import com.laka.live.msg.OpenLiveMsg;
import com.laka.live.msg.OrderInfoMsg;
import com.laka.live.msg.QcloudSigMsg;
import com.laka.live.msg.QueryGiftsMsg;
import com.laka.live.msg.RoomRecvMsg;
import com.laka.live.msg.RoomTagMsg;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

import java.util.HashMap;

/**
 * Created by crazyguan on 2016/3/23.
 * 直播间接口调用类
 */
public class DataProviderRoom {

    /**
     * 手机上禁止直播
     *
     * @param listener
     */
    public static void roomManageOp(Object tag, String userId,String type,
                                    GsonHttpConnection.OnResultListener listener) {
        if (AccountInfoManager.getInstance().getAccountInfo() == null) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.USER_ID, userId);
        params.put(Common.TYPE, type);
        GsonHttpConnection.getInstance().post(tag, Common.ROOM_MANAGE_OP,
                params, Msg.class, listener);
    }

    /**
     * 动态礼物
     *
     * @param tag
     * @param listener
     */
    public static void joinLive(Object tag,String userId,String channelId,
                                  GsonHttpConnection.OnResultListener<JoinLiveMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.USER_ID, userId);
        params.put(Common.CHANNEL_ID, channelId);
        GsonHttpConnection.getInstance().post(tag, Common.JOIN_LIVE_URL,
                params, JoinLiveMsg.class, listener);
    }

    /**
     * 动态礼物
     *
     * @param tag
     * @param params
     * @param listener
     */
    public static void queryGifts(Object tag, HashMap<String, String> params,
                                    GsonHttpConnection.OnResultListener<QueryGiftsMsg> listener) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        GsonHttpConnection.getInstance().post(tag, Common.QUERY_GIFTS_URL,
                params, QueryGiftsMsg.class, listener);
    }


    /**
     * 获取腾讯云视频sig
     *
     * @param tag
     * @param params
     * @param listener
     */
    public static void getQcloudSig(Object tag, HashMap<String, String> params,
                                    GsonHttpConnection.OnResultListener listener) {

        if (params == null) {
            params = new HashMap<>();
        }
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        GsonHttpConnection.getInstance().post(tag, Common.GET_QCLOUD_SIG_URL,
                params, QcloudSigMsg.class, listener);
    }

    /**
     * 设置腾讯云视频
     *
     * @param tag
     * @param listener
     */
    public static void setQcloud(Object tag, String userId, String fileId, String save,
                                 GsonHttpConnection.OnResultListener listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.USER_ID, userId);
        params.put(Common.VID, fileId);
        if (!Utils.isEmpty(save)) {
            params.put(Common.SAVE, save);
        }
        GsonHttpConnection.getInstance().post(tag, Common.SET_QCLOUD_URL,
                params, Msg.class, listener);
    }

    /**
     * 设置旁路直播channel_id (已废弃)
     *
     * @param tag
     * @param listener
     */
    public static void setChannelId(Object tag, String channel_id,
                                    GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.CHANNEL_ID, channel_id);
        GsonHttpConnection.getInstance().post(tag, Common.SET_CHANNEL_ID,
                params, Msg.class, listener);
    }

    /**
     * 设置直播相关信息
     *
     * @param tag
     * @param listener
     */
    public static void setChannel(Object tag, String task_id, String save,
                                  GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.USER_ID, AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
        params.put(Common.TASK_ID, task_id);
        params.put(Common.SAVE, save);
        GsonHttpConnection.getInstance().post(tag, Common.SET_CHANNEL,
                params, Msg.class, listener);
    }

    /**
     * 查询观看人数，获得金币
     *
     * @param tag
     * @param listener
     */
    public static void queryRoomRecv(Object tag, String userId,
                                     GsonHttpConnection.OnResultListener listener) {
        if (AccountInfoManager.getInstance().getAccountInfo() == null) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        UserInfo userInfo = AccountInfoManager.getInstance().getAccountInfo();
        if (userInfo != null) {
            params.put(Common.TOKEN, userInfo.getToken());
        }
        params.put(Common.USER_ID, userId);
        GsonHttpConnection.getInstance().post(tag, Common.QUERY_ROOM_RECV_URL,
                params, RoomRecvMsg.class, listener);
    }


    /**
     * 获取随机标签
     *
     * @param tag
     * @param listener
     */
    public static void queryRoomTag(Object tag,
                                    GsonHttpConnection.OnResultListener listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        GsonHttpConnection.getInstance().get(tag, Common.QUERY_ROOM_TAG_URL,
                params, RoomTagMsg.class, listener);
    }


    /**
     * 获取订单信息
     *
     * @param tag
     * @param listener
     */
    public static void getOrderInfo(Object tag, int product_id,
                                    GsonHttpConnection.OnResultListener listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.PRODUCT_ID, String.valueOf(product_id));
        GsonHttpConnection.getInstance().post(tag, Common.GET_ORDER_INFO_URL,
                params, OrderInfoMsg.class, listener);
    }


}
