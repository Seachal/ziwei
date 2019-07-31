package com.laka.live.manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.laka.live.BuildConfig;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.OnlineUserMessage;
import com.laka.live.bean.UserInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.gift.GiftResManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.chat.ChatMessageView;
import com.laka.live.util.CSVFileUtil;
import com.laka.live.util.Common;
import com.laka.live.util.EncryptUtil;
import com.laka.live.util.Log;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.StringUtils;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import laka.live.bean.ChatMsg;

/**
 * Created by luwies on 16/3/12.
 */
public class RoomManager implements SocketClient.SocketListener {

    /*********************************
     * 状态码开始
     *********************************/
    /**
     * 打开房间
     */
    public final static int TLV_CONNECT = 0x000001;
    public final static int TLV_DISCONNECT = 0x000002;


    public final static int TLV_E_DECRYPTION_FAIL = Integer.MIN_VALUE;

    /**
     * 错误码
     */
    // 操作成功
    public final static int TLV_E_OK = 0;
    // RSA私钥解密失败, 服务器会主动断开连接！
    public final static int TLV_E_RSA_PRIVATE_DECRYPT_FAILED = 1;
    // 无效的用户token, 服务器会主动断开连接！
    public final static int TLV_E_INVALID_USER_TOKEN = 2;
    // 房间已经打开
    public final static int TLV_E_ROOM_ALREADY_OPENED = 3;
    // 房间未打开
    public final static int TLV_E_ROOM_NOT_OPENED_YET = 4;
    // 用户不在任何房间
    public final static int TLV_E_USER_NOT_IN_ANY_ROOM = 5;
    // 用户在自己的房间里，必须关闭房间才可退出
    public final static int TLV_E_USER_IN_HIS_OWN_ROOM = 6;
    // 房间不存在
    public final static int TLV_E_ROOM_NOT_EXISTS = 7;
    // 用户在其它房间
    public final static int TLV_E_USER_ALREADY_IN_OTHER_ROOM = 8;
    // 用户不存在
    public final static int TLV_E_USER_NOT_EXISTS = 9;
    // 发送消息为空
    public final static int TLV_E_EMPTY_MESSAGE = 10;
    // 发送礼物不存在
    public final static int TLV_E_GIFT_NOT_EXIST = 11;
    // 发送礼物不存在
    public final static int TLV_E_USER_IN_USER_ROOM = 12;
    // 送礼物钻石不足
    public final static int TLV_E_KAZUAN_NOT_ENOUGH = 13;
    //用户被禁言
    public final static int TLV_E_USER_FORBIDDEN = 14;
    //用户不在房间
    public final static int TLV_E_USER_NOT_IN_THIS_ROOM = 15;
    //用户是管理员
    public final static int TLV_E_USER_IS_ADMIN = 16;
    //用户不是管理员
    public final static int TLV_E_USER_NOT_ADMIN = 17;
    //管理员已满
    public final static int TLV_E_TOO_MANY_ADMIN = 18;
    //用户未被禁言
    public final static int TLV_E_USER_NOT_IN_ROOM_FORBID_SAY = 19;
    //
    public final static int TLV_E_USER_ROOM_OFFLINE = 20;
    //
    public final static int TLV_E_INVALAD_REQUEST_DATA = 21;
    //需要重试
    public final static int TLV_E_TRY_AGAIN_LATER = 22;

    public final static int TLV_E_ANCHOR_NOT_CONFIRM = 24;
    public final static int TLV_E_VIEWER_NOT_CONFIRM = 25;
    public final static int TLV_E_CONDITION_LACK = 26; //条件不足
    public final static int TLV_E_ALREADY_IN_LINK_MIC = 27; //条件不足
    public final static int TLV_E_STREAM_ID_INVALAD = 28; //条件不足
    public final static int TLV_E_LINK_NUM_LIMITED = 29; //连麦上限
    public final static int TLV_E_USER_NOT_VERIFY = 30; //未认证
    public final static int TLV_E_USER_NOT_BUY_COURSE = 31; //未购买课程

    //连麦操作标识
    public final static int TLV_LINK_MIC_DEFAULT = 0;
    public final static int TLV_LINK_MIC_ADD = 1;    //连麦添加
    public final static int TLV_LINK_MIC_DEL = 2;    //连麦删除
    public final static int TLV_LINK_MIC_CONFIRM = 3;    //连麦确认
    public final static int TLV_LINK_MIC_OPEN = 4;    //连麦打开(连麦成功完成)
    public final static int TLV_LINK_MIC_CLOSE = 5;    //连麦关闭
    public final static int TLV_LINK_MIC_REFUSE = 6;    //连麦拒绝
    public final static int TLV_LINK_MIC_FORBID = 7;    //连麦禁止
    public final static int TLV_LINK_VDO_CLOSE = 8;//视频关闭
    public final static int TLV_LINK_VDO_OPEN = 9;//视频打开
    //连麦操作结果
    public final static int TLV_LMR_OK = 0;
    public final static int TLV_LMR_OPERATION_TIMEOUT = 1;    //过程超时
    public final static int TLV_LMR_CONDITION_LACK = 2;    //对方条件不足(不满足连麦条件)

    // 一般读取错误
    public final static int CHAT_ERROR_RECONNECT = SocketClient.ERROR_RECONNECT;
    public final static int CHAT_ERROR_SERVER_DISCONNECT = SocketClient.ERROR_SERVER_DISCONNECT;
    public final static int CHAT_ERROR_READTIMEOUT = SocketClient.ERROR_READTIMEOUT;
    public final static int CHAT_ERROR_WRITETIMEOUT = SocketClient.ERROR_WRITETIMEOUT;

    //服务器错误
    public final static int CHAT_ERROR_SERVER_ABNORMAL = 2000;

    /*********************************
     * 状态码结束
     *********************************/

    public interface OnResultListener {
        /**
         * 直播间Socket握手成功
         */
        void chatDidConnect();

        /**
         * Socket断开链接
         */
        void chatDidDisconnect();

        /**
         * 打开房间成功回调
         */
        void chatDidOpenRoom();

        /**
         * 进入房间成功回调
         *
         * @param anchor 主播信息
         */
        void chatDidEnterRoom(BytesReader.Anchor anchor);

        /**
         * 离开直播间成功
         */
        void chatDidExitRoom();

        /**
         * 关闭房间成功回调
         */
        void chatDidCloseRoom(String roomId);

        /**
         * 有用户进入房间消息回调
         *
         * @param message 消息对象
         */
        void chatUserEnterRoom(BytesReader.EnterRoomMessage message);

        /**
         * 有用户离开房间
         *
         * @param userid 用户ID
         */
        void chatUserExitRoom(String userid);

        /**
         * 接收到房间消息
         *
         * @param message 消息实例对象,参考AudienceMessage定义,可能为null
         */
        void chatDidReceiveMessage(BytesReader.AudienceMessage message);

        /**
         * 接收到点亮消息
         *
         * @param userID 来自用户ID
         */
        void chatReceiveLight(String userID, String nickName, int level);

        /**
         * 接收到点赞消息
         *
         * @param userID 来自用户ID
         * @param count  点赞数
         */
        void chatReceiveLike(String userID, int count);

        /**
         * 接收到礼物消息
         *
         * @param message 礼物消息实例对象,参考GiftMessage定义
         */
        void chatReceiveGift(BytesReader.GiftMessage message);

        /**
         * 接收到弹幕消息
         *
         * @param message 弹幕消息实例对象,参考BulletMessage定义
         */
        void chatReceiveBullet(BytesReader.BulletMessage message);

        /**
         * 查询房间用户信息成功回调
         *
         * @param number    返回用户数
         * @param audiences 用户对象数组,包含的是Audience实例对象,定义参考Audience说明
         */
        void chatDidQueryRoomUser(int number, ArrayList<BytesReader.Audience> audiences);

        /**
         * 查询房间信息成功回调
         *
         * @param coin          主播收到的总钻石数
         * @param audienceCount 当前房间用户数
         */
        void chatDidQueryRoomData(long coin, int audienceCount);

        /**
         * 查询用户信息成功回调
         *
         * @param audience 用户信息对象
         */
        void chatDidQueryUserInfo(BytesReader.Audience audience);

        /**
         * 各种异常
         *
         * @param errcode 错误码
         * @param errMsg  错误描述
         */
        void chatErrorOccur(int errcode, String errMsg);

        /**
         * 操作成功回调
         */
        void chatDidSuccess(int code);

        /**
         * 离线消息回调
         */
        void chatOffline(List<ChatMsg> messages);
    }

    private final static String TAG = "RoomManager";

    public final static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDiSVR3BtCagDzdneC4TS+1WHVF\n" +
            "Uw6ViRm3j2JMmlbq/u/hYoDNHP0Oeaz8Z3NXtu1qf6L1sZFYCACze6fSKJiTEnzS\n" +
            "4abo/MiNl9GsT0gCgpnJBLfDWhscLIswNwEdczWnPWQ4RwIIihK82p6WoKcHLLhq\n" +
            "vXuoD7XI0skf6+bGKwIDAQAB";

    /**
     * 端口号
     */
    private final static int PORT = 80;

    /**
     * 服务器地址
     */
//    private final static String SERVER_HOST = "119.29.143.140";
//    private final static String SERVER_HOST = "192.168.16.7";
    private final static String SERVER_HOST = BuildConfig.ROOM_SERVER_HOST;
    /**
     * 心跳包间隔
     */
    private final static int HEARTBEAT_INTEVAL = 30000;

    /*********************************
     * 协议开始
     *********************************/
    private final static int TLV_PROTOCOL_MAGIC = 0x00564C54;

    // 保持连接
    private final static int TLV_REQ_KEEP_ALIVE = 0x100000A0;
    private final static int TLV_RSP_KEEP_ALIVE = 0x100000A1;

    // 传输AES_KEY
    private final static int TLV_REQ_TRANSFER_AES_KEY = 0x100000A2;
    private final static int TLV_RSP_TRANSFER_AES_KEY = 0x100000A3;
    private final static int TLV_RSP_AES_KEY_FAILED = 0x100000EE;
   /* // 传输加密数据
    private final static int TLV_REQ_TRANSFER_DATA = 0x04000000;
    private final static int TLV_RSP_TRANSFER_DATA = 0x05000000;*/

    //逻辑协议
    private final static int TLV_REQ_OPEN_ROOM = 0x10000000;

    private final static int TLV_RSP_OPEN_ROOM = TLV_REQ_OPEN_ROOM + 1;

    private final static int TLV_REQ_CLOSE_ROOM = TLV_RSP_OPEN_ROOM + 1;

    private final static int TLV_RSP_CLOSE_ROOM = TLV_REQ_CLOSE_ROOM + 1;

    private final static int TLV_REQ_MULTICAST_CLOSE_ROOM = TLV_RSP_CLOSE_ROOM + 1;

    private final static int TLV_RSP_MULTICAST_CLOSE_ROOM = TLV_REQ_MULTICAST_CLOSE_ROOM + 1;

    private final static int TLV_REQ_ENTER_ROOM = TLV_RSP_MULTICAST_CLOSE_ROOM + 1;

    public final static int TLV_RSP_ENTER_ROOM = TLV_REQ_ENTER_ROOM + 1;

    private final static int TLV_REQ_MULTICAST_ENTER_ROOM = TLV_RSP_ENTER_ROOM + 1;

    private final static int TLV_RSP_MULTICAST_ENTER_ROOM = TLV_REQ_MULTICAST_ENTER_ROOM + 1;

    private final static int TLV_REQ_LEAVE_ROOM = TLV_RSP_MULTICAST_ENTER_ROOM + 1;

    public final static int TLV_RSP_LEAVE_ROOM = TLV_REQ_LEAVE_ROOM + 1;

    private final static int TLV_REQ_MULTICAST_LEAVE_ROOM = TLV_RSP_LEAVE_ROOM + 1;

    private final static int TLV_RSP_MULTICAST_LEAVE_ROOM = TLV_REQ_MULTICAST_LEAVE_ROOM + 1;

    private final static int TLV_REQ_ROOM_SAY = TLV_RSP_MULTICAST_LEAVE_ROOM + 1;

    public final static int TLV_RSP_ROOM_SAY = TLV_REQ_ROOM_SAY + 1;

    private final static int TLV_REQ_MULTICAST_ROOM_SAY = TLV_RSP_ROOM_SAY + 1;

    private final static int TLV_RSP_MULTICAST_ROOM_SAY = TLV_REQ_MULTICAST_ROOM_SAY + 1;

    //    private final static int TLV_REQ_SAY = TLV_RSP_MULTICAST_ROOM_SAY;
//
//    private final static int TLV_RSP_SAY = TLV_REQ_SAY + 1;
    public final static int TLV_REQ_SAY = 0x10000012;
    public final static int TLV_RSP_SAY = 0x10000013;

    // 单播系统私信给对方
    private final static int TLV_REQ_SYSTEM_SAY = 0x10000050;

    private final static int TLV_REQ_UNICAST_SAY = TLV_RSP_SAY + 1;

    private final static int TLV_RSP_UNICAST_SAY = TLV_REQ_UNICAST_SAY + 1;

    // 官方公告
    public final static int TLV_REQ_SYSTEM_NOTICE = 0x1000004E;
    public final static int TLV_RSP_SYSTEM_NOTICE = 0x1000004F;

    // 聊天室功能
    // 点亮
    public final static int TLV_REQ_MULTICAST_ROOM_LIGHT = 0x10000032;
    public final static int TLV_RSP_MULTICAST_ROOM_LIGHT = 0x10000033;

    // 点赞
    private final static int TLV_REQ_ROOM_LIKE = 0x10000016;
    public final static int TLV_RSP_ROOM_LIKE = 0x10000017;

    private final static int TLV_REQ_MULTICAST_ROOM_LIKE = 0x10000018;
    private final static int TLV_RSP_MULTICAST_ROOM_LIKE = 0x10000019;

    // 送礼
    private final static int TLV_REQ_ROOM_GIVE = 0x1000001A;
    public final static int TLV_RSP_ROOM_GIVE = 0x1000001B;

    public final static int TLV_REQ_MULTICAST_ROOM_GIVE = 0x1000001C;
    private final static int TLV_RSP_MULTICAST_ROOM_GIVE = 0x1000001D;

    // 弹幕
    private final static int TLV_REQ_ROOM_BULLET = 0x1000001E;
    public final static int TLV_RSP_ROOM_BULLET = 0x1000001F;

    private final static int TLV_REQ_MULTICAST_ROOM_BULLET = 0x10000020;
    private final static int TLV_RSP_MULTICAST_ROOM_BULLET = 0x10000021;

    // 禁言
    public final static int TLV_REQ_ROOM_FORBID = 0x10000022;
    private final static int TLV_RSP_ROOM_FORBID = 0x10000023;

    public final static int TLV_REQ_MULTICAST_ROOM_FORBID = 0x10000024;
    private final static int TLV_RSP_MULTICAST_ROOM_FORBID = 0x10000025;

    // 私信
    // 送礼
    public final static int TLV_REQ_GIVE = 0x10000026;
    public final static int TLV_RSP_GIVE = 0x10000027;

    public final static int TLV_REQ_UNICAST_GIVE = 0x10000028;
    private final static int TLV_RSP_UNICAST_GIVE = 0x10000029;

    // 查询房间内用户
    private final static int TLV_REQ_QUERY_ROOM_USER = 0x1000002A;
    public final static int TLV_RSP_QUERY_ROOM_USER = 0x1000002B;

    // 查询房间数据
    private final static int TLV_REQ_QUERY_ROOM_DATA = 0x1000002C;
    public final static int TLV_RSP_QUERY_ROOM_DATA = 0x1000002D;

    // 推送离线消息
//    private final static int TLV_REQ_PUSH_OFFLINE_MESSAGE = 0x1000002E;
//    private final static int TLV_RSP_PUSH_OFFLINE_MESSAGE = 0x1000002F;

    // 获取用户信息
    private final static int TLV_REQ_GET_USER_INFO = 0x10000030;
    private final static int TLV_RSP_GET_USER_INFO = 0x10000031;

    //设置房间管理员
    private final static int TLV_REQ_ADD_ROOM_ADMIN = 0x10000038;
    private final static int TLV_RSP_ADD_ROOM_ADMIN = 0x10000039;

    //取消房间管理员
    public final static int TLV_REQ_DEL_ROOM_ADMIN = 0x1000003A;
    public final static int TLV_RSP_DEL_ROOM_ADMIN = 0x1000003B;

    //取消禁言
    private final static int TLV_REQ_ROOM_PERMIT_SAY = 0x10000040;
    private final static int TLV_RSP_ROOM_PERMIT_SAY = 0x10000041;

    public final static int TLV_REQ_MULTICAST_ROOM_PERMIT_SAY = 0x10000042;
    public final static int TLV_RSP_MULTICAST_ROOM_PERMIT_SAY = 0x10000043;

    // 通知设成管理员的那个人
    public final static int TLV_REQ_UNICAST_ADD_ROOM_ADMIN = 0x1000003C;
    public final static int TLV_RSP_UNICAST_ADD_ROOM_ADMIN = 0x1000003D;

    // 通知删除管理员的那个人
    public final static int TLV_REQ_UNICAST_DEL_ROOM_ADMIN = 0x1000003E;
    private final static int TLV_RSP_UNICAST_DEL_ROOM_ADMIN = 0x1000003F;

    //后台控制用户禁止直播
    public final static int TLV_RSP_FORBID_OPEN_ROOM = 0x10000035;

    //获取离线消息
    public final static int TLV_REQ_GET_OFFLINE_MESSAGE = 0x10000044;
    public final static int TLV_RSP_GET_OFFLINE_MESSAGE = 0x10000045;


    //主播暂时离开
    public final static int TLV_REQ_BROADCATER_LEAVE = 0x10000046;
    public final static int TLV_RSP_BROADCATER_LEAVE = 0x10000047;
    //通知房间内用户主播暂时离开
    public final static int TLV_REQ_MULTICAST_BROADCASTER_LEAVE = 0x10000048;
    public final static int TLV_RSP_MULTICAST_BROADCASTER_LEAVE = 0x10000049;
    //主播返回
    public final static int TLV_REQ_BROADCASTER_RETURN = 0x1000004A;
    public final static int TLV_RSP_BROADCASTER_RETURN = 0x1000004B;
    //通知房间内用户主播返回
    public final static int TLV_REQ_MULTICAST_BROADCASTER_RETURN = 0x1000004C;
    public final static int TLV_RSP_MULTICAST_BROADCASTER_RETURN = 0x1000004D;
    //房间用户列表变化广播
    public final static int TLV_REQ_MULTICAST_ROOM_USER = 0x10000052;
    public final static int TLV_RSP_MULTICAST_ROOM_USER = 0x10000053;

    public final static int TLV_REQ_MULTICAST_MSG = 0x10001000;
    public final static int TLV_RSP_MULTICAST_MSG = 0x10001001;
    //发送客户端直播状态
    public final static int TLV_REQ_POST_LIVE_DATA = 0x10000062;
    public final static int TLV_RSP_POST_LIVE_DATA = 0x10000063;

    public final static int TLV_REQ_POST_ANCHOR_DATA = 0x10000056;

    public final static int TLV_RSP_POST_ANCHOR_DATA = 0x10000057;

    public final static int TLV_REQ_MULTICAST_LIVE_DATA = 0x10000058;

    public final static int TLV_REQ_QUERY_LINK_LIST = 0x1000006A;

    public final static int TLV_RSP_QUERY_LINK_LIST = 0x1000006B;

    public final static int TLV_REQ_QUERY_ONLINE_LIST = 0x1000006C;

    public final static int TLV_RSP_QUERY_ONLINE_LIST = 0x1000006D;

    // 清空房间连麦列表
    public final static int TLV_REQ_CLEAR_LINK_LIST = 0x10000068;
    public final static int TLV_RSP_CLEAR_LINK_LIST = 0x10000069;

    // 查询房间连麦用户
    public final static int TLV_REQ_QUERY_LINK_USER = 0x1000006E;
    public final static int TLV_RSP_QUERY_LINK_USER = 0x1000006F;

    // 添加连麦
    public final static int TLV_REQ_ADD_LINK_MIC = 0x10000070;
    public final static int TLV_RSP_ADD_LINK_MIC = 0x10000071;

    // 确认连麦
    public final static int TLV_REQ_CONFIRM_LINK_MIC = 0x10000072;
    public final static int TLV_RSP_CONFIRM_LINK_MIC = 0x10000073;

    // 打开连麦（完成连麦）
    public final static int TLV_REQ_OPEN_LINK_MIC = 0x10000074;
    public final static int TLV_RSP_OPEN_LINK_MIC = 0x10000075;

    // 取消连麦
    public final static int TLV_REQ_CLOSE_LINK_MIC = 0x10000076;
    public final static int TLV_RSP_CLOSE_LINK_MIC = 0x10000077;

    // 单播连麦消息
    public final static int TLV_REQ_UNICAST_LINK_MIC = 0x10000078;
    public final static int TLV_RSP_UNICAST_LINK_MIC = 0x10000079;

    // 广播连麦消息
    public final static int TLV_REQ_MULTICAST_LINK_MIC = 0x1000007A;
    public final static int TLV_RSP_MULTICAST_LINK_MIC = 0x1000007B;

    // 禁止连麦
    public final static int TLV_REQ_FORBID_LINK_MIC = 0x1000007C;
    public final static int TLV_RSP_FORBID_LINK_MIC = 0x1000007D;

    // 拒绝连麦
    public final static int TLV_REQ_REFUSE_LINK_MIC = 0x1000007E;
    public final static int TLV_RSP_REFUSE_LINK_MIC = 0x1000007F;

    /*********************************
     * 协议结束
     *********************************/

    // ASE随机密钥
    private byte[] mAesKey;

    // 用户ID
    private UserInfo mUser = null;

//    private OnResultListener mResultListener = null;

    private HashSet<OnResultListener> mResultListeners;


    private SocketClient mSocket = null;
    private Timer mHeartbearTimer = null;
    private Handler mMainHandler = null;
    private Thread mSocketThread = null;

    public boolean hasAESHandshake = false;

    //是否在房间中
    /*private boolean isInRoom = false;

    public boolean isInRoom() {
        return isInRoom;
    }*/

    private static RoomManager self;

    private onRequestResultCallback mCallback;
    private onRequestAdminResultCallback mAdminResultCallback;

    public static RoomManager getInstance() {
        if (self == null) {
            self = new RoomManager(LiveApplication.getInstance());
        }
        return self;
    }


    private RoomManager(Context context) {
        mMainHandler = new Handler(context.getMainLooper(), new MainHandlerCallback());
        mAesKey = randomAESKey();
        assert (null != mAesKey);

//        Log.d(TAG, String.format("用户ID:%s ase key;%s", mUser.getIdStr(), mAesKey));

        mResultListeners = new HashSet<>();
    }

    public void addResultListener(OnResultListener mResultListener) {
//        this.mResultListener = mResultListener;
        mResultListeners.add(mResultListener);
        Log.d(TAG, "intent addResultListener 监听数量=" + mResultListeners.size());
    }

    public void removeResultListener(OnResultListener mResultListener) {
        mResultListeners.remove(mResultListener);
        Log.d(TAG, "intent removeResultListener 监听数量=" + mResultListeners.size());
    }

    public void cleareResultListener() {
        mResultListeners.clear();
        Log.d(TAG, "intent cleareResultListener 监听数量=" + mResultListeners.size());
    }

    public void startRoom() {
        if (mSocket == null) {
            mUser = AccountInfoManager.getInstance().getAccountInfo();
            if (mUser == null) {
                return;
            }
            mSocket = new SocketClient(SERVER_HOST, PORT);
            mSocket.setListener(this);
            mSocket.startWork();

            mSocketThread = new Thread(mSocket);
            mSocketThread.start();
        }

    }

    //    public void startRoom() {
//
//        mSocket = new SocketClient(SERVER_HOST, PORT);
//        mSocket.setListener(this);
//        mSocket.startWork();
//
//        mSocketThread = new Thread(mSocket);
//        mSocketThread.start();
//    }
    public void stopRoom() {

//        TimeoutException exception = new TimeoutException();
//        Log.error(TAG, "exception : ", exception);

        Log.d(TAG, "stopRoom");
//        if (mResultListeners.size() > 0) {
//            Log.d(TAG, "仍然有activity在监听,不stopRoom");
//            return;
//        }
        stopHeartbertTimer();

        if (mSocket != null) {
            mSocket.closeKey();
            mSocket.stopWork();
            mSocket = null;
        }
        if (null != mSocketThread && mSocketThread.isAlive()) {
            mSocketThread.interrupt();
        }
        mSocketThread = null;
    }

    public boolean isConnected() {
        if (mSocket == null) {
            return false;
        }
//        Log.d(TAG," isConnected state="+mSocket.getConnectState()+" hasAESHandshake="+hasAESHandshake);
        return mSocket.getConnectState() == SocketClient.ConnectState.CONNECTED && hasAESHandshake;
    }

    /**
     * 主线程消息回调
     */
    class MainHandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
//            if (null == mResultListener) return false;
//            if (mResultListeners.size() == 0) {
//                return false;
//            }

            int code = msg.what;

            if (code < TLV_CONNECT) {
                String message = (String) msg.obj;
                Log.error(TAG, message);

//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener) {
////                        mResultListener.chatErrorOccur(msg.what, message);
//                    }
//                }
                return false;
            }

            switch (code) {
                case TLV_CONNECT:
                    hasAESHandshake = true;
                    OnResultListener[] listeners = mResultListeners.toArray(new OnResultListener[0]);
                    for (OnResultListener mResultListener : listeners) {
                        if (null != mResultListener)
                            mResultListener.chatDidConnect();
                    }
                    break;
                case TLV_DISCONNECT:
                    hasAESHandshake = false;
//                    isInRoom = false;
                    OnResultListener[] listeners2 = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                    for (OnResultListener mResultListener : listeners2) {
                        if (null != mResultListener)
                            mResultListener.chatDidDisconnect();
                    }
//                    for (OnResultListener mResultListener : mResultListeners) {
//                        if (null != mResultListener)
//                            mResultListener.chatDidDisconnect();
//                    }
                    break;
                // 错误处理
                case CHAT_ERROR_RECONNECT:
//                    isInRoom = false;
                    break;
                case TLV_E_USER_IN_USER_ROOM:
                case TLV_E_ROOM_NOT_EXISTS:
                case TLV_E_USER_NOT_IN_ANY_ROOM:
                case TLV_E_USER_FORBIDDEN:
                case TLV_E_USER_NOT_IN_THIS_ROOM:
                case TLV_E_USER_IS_ADMIN:
                case TLV_E_USER_NOT_ADMIN:
                case TLV_E_TOO_MANY_ADMIN:
                case CHAT_ERROR_SERVER_ABNORMAL:
                case CHAT_ERROR_SERVER_DISCONNECT:
                case CHAT_ERROR_READTIMEOUT:
                case CHAT_ERROR_WRITETIMEOUT:
                case TLV_E_USER_NOT_BUY_COURSE:
                    OnResultListener[] listeners3 = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                    for (OnResultListener mResultListener : listeners3) {
                        if (null != mResultListener) {
                            String message = (String) msg.obj;
                            mResultListener.chatErrorOccur(msg.what, message);
                        }
                    }
//                    for (OnResultListener mResultListener : mResultListeners) {
//                        if (null != mResultListener) {
//                            String message = (String) msg.obj;
//                            mResultListener.chatErrorOccur(msg.what, message);
//                        }
//                    }
                default:
                    break;
            }

            return false;
        }
    }

    @Override
    public void didConnect() {
        Log.d(TAG, "didConnect sendAesKey");
        sendAesKey();
    }

    @Override
    public void didDisconnect() {
        mMainHandler.sendEmptyMessage(TLV_DISCONNECT);
    }

    @Override
    public void didReadData(BufferedInputStream bIn) throws IOException {
        //read length
        //根据长度去读数据
        //解析
        int len = readInt(bIn);
        int code = readInt(bIn);

        if (len <= 8) {
            return;
        }
        len -= 8;
        Log.error(TAG, "服务器回应 : 0x" + Integer.toHexString(code));
        switch (code) {
            case TLV_RSP_KEEP_ALIVE: // 心跳包响应
                handleKeepAliveRsp();
                break;
            case TLV_RSP_TRANSFER_AES_KEY: // 鉴权响应
                handleTransferAESKeyRsp(bIn, len);
                break;
            case TLV_RSP_AES_KEY_FAILED:
                Log.d(TAG, "TLV_RSP_AES_KEY_FAILED");
                break;
            default:
                handleTransferDataRsp(bIn, len, code);
                break;
        }

    }

    @Override
    public void didErroOccur(int code, String message) {
        sendErrorMessage(code, message);
        stopHeartbertTimer();
    }

    private void sendErrorMessage(int code, String message) {
        if (null != mMainHandler) {
            Message msg = new Message();
            msg.obj = message;
            msg.what = code;
            mMainHandler.sendMessage(msg);
        }
    }

    private void handleKeepAliveRsp() throws IOException {
        /*int result = 0;
        result = bIn.read();*/
        Log.error(TAG, "服务器还活着:"/* + result*/);
    }

    private void handleTransferAESKeyRsp(BufferedInputStream bIn, int len) throws IOException {
        if (len < 0) {
            return;
        }
        Log.e(TAG, "handleTransferAESKeyRsp len = " + len);
        byte data[] = new byte[len];
        readBytes(bIn, data);

        data = EncryptUtil.aesDecrypt(data, mAesKey);

        int errorCode;
        if (data == null) {
            errorCode = TLV_E_DECRYPTION_FAIL;
        } else {
            errorCode = bytes2Int(data);
        }
        if (errorCode == TLV_E_OK) {
//            Log.d(TAG," TLV_CONNECT连接成功");
            mMainHandler.sendEmptyMessage(TLV_CONNECT);
            EventBusManager.postEvent(TLV_CONNECT, SubcriberTag.SOCKET_CONNECT_SUCCESS);
            startHeartbeatTimer();
        } else { // 鉴权失败
//            Log.d(TAG," TLV_CONNECT连接失败");
            handleError(errorCode);

        }

//        Log.error(TAG, "receive transfer AESKey errorCode : 0x" + Integer.toHexString(errorCode));
//        Log.debug(TAG, "receive transfer AESKey respone");
    }

    private void handleTransferDataRsp(BufferedInputStream bIn, int len, int code) throws IOException {
        int result = 0;
        Log.debug(TAG, "receive transfer data respone, len = " + len);
        if (len > 0) {
            //获取加密内容
                /*buffer = Utils.allocate(len);
                result = socketChannel.read(buffer);

                if (result < len) {
                    return result;
                }
                buffer.flip();
                buffer.get(data, 0, len);
                */
            if (len > 1024 * 1024 * 10) {
                Log.d(TAG, "数据包长度异常 len=" + len);
                return;
            }


            byte[] data = new byte[len];
            readBytes(bIn, data);

            byte[] content = EncryptUtil.aesDecrypt(data, mAesKey);
            if (content == null) {
                return;
            }

            long start = System.currentTimeMillis();
            handleDecrtptedData(code, content);
            long end = System.currentTimeMillis();
            Log.error(TAG, "handleDecrtptedData used time : " + (end - start) + "ms");
            /*Message msg = new Message();
            msg.what = TLV_RSP_TRANSFER_DATA;
            msg.obj = content;
            mMainHandler.sendMessage(msg);*/
        } else if (len == 0) {
            return;
        }
    }

    private void handleDecrtptedData(int rspcode, byte[] content) {
        BytesReader reader = new BytesReader(content);

        if (rspcode == TLV_RSP_OPEN_ROOM || rspcode == TLV_E_ROOM_ALREADY_OPENED) { // 打开房间 主播创建房间返回已经打开视作正常流程
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                String systemNotice = reader.readString();
                if (!Utils.isEmpty(systemNotice)) {
                    EventBusManager.postEvent(systemNotice, SubcriberTag.ADD_SYSTEM_NOTICE);
                }
//                isInRoom = true;
                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatDidOpenRoom();
                            }
                        });
                    }
                }
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidOpenRoom();
//                            }
//                        });
//                    }
//                }
            } else {
//                isInRoom = false;
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_ENTER_ROOM) {
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                Log.debug(TAG, "进入房间成功");
//                isInRoom = true;

                final BytesReader.Anchor anchor = reader.readAnchor();

                // 需要显示的房间人数
                final int audienceShowNumber = reader.readInt();

                final ArrayList<BytesReader.Audience> audiences = new ArrayList<>();
                for (int i = 0; i < audienceShowNumber; i++) {
                    BytesReader.Audience object = reader.readAudience();
                    if (null != object && !Utils.isEmpty(object.id))
                        audiences.add(object);
                }
                String systemNotice = reader.readString();
                if (!Utils.isEmpty(systemNotice)) {
                    EventBusManager.postEvent(systemNotice, SubcriberTag.ADD_SYSTEM_NOTICE);
                }

                final OnResultListener[] listeners;
                synchronized (mResultListeners) {
                    listeners = mResultListeners.toArray(new OnResultListener[0]);
                }
                for (final OnResultListener mResultListener : listeners) {
                    if (null != anchor && null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatDidEnterRoom(anchor);
                            }
                        });


                        if (null != mResultListener) {
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mResultListener.chatDidQueryRoomUser(audienceShowNumber, audiences);
                                }
                            });

                        }
                    }
                }
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (null != anchor && null != mResultListener) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidEnterRoom(anchor);
//                            }
//                        });
//
//
//                        if (null != mResultListener) {
//                            mMainHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mResultListener.chatDidQueryRoomUser(audienceShowNumber, audiences);
//                                }
//                            });
//
//                        }
//                    }
//                }
            } else {
//                isInRoom = false;
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_CLOSE_ROOM) {
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                final String roomId = reader.readString();
//                isInRoom = false;
                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatDidCloseRoom(roomId);
                            }
                        });
                    }
                }
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidCloseRoom();
//                            }
//                        });
//                    }
//                }
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_LEAVE_ROOM) {
            int errcode = reader.readInt();
            Log.d(TAG, "退出房间返回 TLV_RSP_LEAVE_ROOM errcode=" + errcode);
            if (errcode == TLV_E_OK) {
//                isInRoom = false;
                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatDidExitRoom();
                            }
                        });
                    }
                }
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidExitRoom();
//                            }
//                        });
//                    }
//                }
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_REQ_MULTICAST_CLOSE_ROOM) { // 房间关闭
//            isInRoom = false;
            final String roomId = reader.readString();
            final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
            for (final OnResultListener mResultListener : listeners) {
                if (null != mResultListener) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultListener.chatDidCloseRoom(roomId);
                        }
                    });
                }
            }
//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatDidCloseRoom();
//                        }
//                    });
//                }
//            }
        } else if (rspcode == TLV_RSP_ROOM_SAY) { // 发送消息回应
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                Log.debug(TAG, "发送消息成功");
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_ROOM_LIKE) {
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                Log.debug(TAG, "点赞成功");
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_ROOM_GIVE) {
            int errcode = reader.readInt();
            final long kazuan = reader.readLong();//int64
            int reqId = reader.readInt();
            Log.d(TAG, "TLV_RSP_ROOM_GIVE errcode=" + errcode
            +" kazuan="+kazuan+" reqId="+reqId);
//            if (errcode != TLV_E_OK) {
//                EventBusManager.postEvent(reqId, SubcriberTag.SEND_CHAT_GIFT_FAIL);
//            }

            if (errcode == TLV_E_OK) {
//                final long kazuan = reader.readLong();//int64
                Log.debug(TAG, "送礼成功 剩余钻石=" + kazuan);
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        EventBusManager.postEvent(translateKazuan(kazuan), SubcriberTag.REFRESH_LAST_KAZUAN);
                    }
                });
            } else if (errcode == TLV_E_KAZUAN_NOT_ENOUGH) {
                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "2");
                            }
                        });
                    }
                }
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "2");
//                            }
//                        });
//                    }
//                }
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_SAY) {
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                int reqId = reader.readInt();
                Log.debug(TAG, "私信发送成功 reqId=" + reqId);
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidSuccess(TLV_RSP_SAY);
//                            }
//                        });
//                    }
//                }
                EventBusManager.postEvent(reqId, SubcriberTag.SEND_CHAT_MSG_SUCCESS);
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_GIVE) {
            int errcode = reader.readInt();
            final long kazuan = reader.readLong();//int64
            int reqId = reader.readInt();
            if (errcode != TLV_E_OK) {
                EventBusManager.postEvent(reqId, SubcriberTag.SEND_CHAT_GIFT_FAIL);
            }
            if (errcode == TLV_E_OK) {
                Log.debug(TAG, "私信送礼成功 剩余钻石=" + kazuan
                        + " reqId=" + reqId);
                EventBusManager.postEvent(translateKazuan(kazuan), SubcriberTag.REFRESH_LAST_KAZUAN);
                EventBusManager.postEvent(reqId, SubcriberTag.SEND_CHAT_GIFT_SUCCESS);
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidSuccess(TLV_RSP_GIVE);
//                            }
//                        });
//                    }
//                }
            } else if (errcode == TLV_E_KAZUAN_NOT_ENOUGH) {

                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "");
                            }
                        });
                    }
                }

//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "");
//                            }
//                        });
//                    }
//                }
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_ROOM_BULLET) {
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                final long kazuan = reader.readLong();//int64
                Log.debug(TAG, "发送弹幕成功 剩余钻石=" + kazuan);
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        EventBusManager.postEvent(translateKazuan(kazuan), SubcriberTag.REFRESH_LAST_KAZUAN);
                    }
                });
            } else if (errcode == TLV_E_KAZUAN_NOT_ENOUGH) {
                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "1");
                            }
                        });
                    }
                }
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "1");
//                            }
//                        });
//                    }
//
//                }
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_REQ_MULTICAST_ENTER_ROOM) { // 有用户进入房间
            final BytesReader.EnterRoomMessage message = reader.readEnterRoomMessage();

            final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
            for (final OnResultListener mResultListener : listeners) {
                if (null != mResultListener) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultListener.chatUserEnterRoom(message);
                        }
                    });
                }
            }

//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatUserEnterRoom(message);
//                        }
//                    });
//                }
//            }
        } else if (rspcode == TLV_REQ_MULTICAST_LEAVE_ROOM) { // 有用户离开房间
            Log.d(TAG, "处理用户进出房间消息");
            // 1.用户ID
            final String userID = reader.readString();
            final int time = reader.readInt();

            final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
            for (final OnResultListener mResultListener : listeners) {
                if (null != mResultListener) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultListener.chatUserExitRoom(userID);
                        }
                    });
                }
            }

//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatUserExitRoom(userID);
//                        }
//                    });
//                }
//            }
        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_LIKE) {
            // 1.用户ID
            final String userID = reader.readString();
            final int count = reader.readInt();

            final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
            for (final OnResultListener mResultListener : listeners) {
                if (null != mResultListener) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultListener.chatReceiveLike(userID, count);
                        }
                    });
                }
            }

//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatReceiveLike(userID, count);
//                        }
//                    });
//                }
//            }
        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_LIGHT) {
            //点亮
            final String userID = reader.readString();
            final int count = reader.readInt();
            final String nickName = reader.readString();
            final int level = reader.readInt();


            final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
            for (final OnResultListener mResultListener : listeners) {
                if (null != mResultListener) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultListener.chatReceiveLight(userID, nickName, level);
                        }
                    });
                }
            }
//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatReceiveLight(userID, nickName, level);
//                        }
//                    });
//                }
//            }
        } else if (rspcode == TLV_REQ_UNICAST_GIVE) {
            Log.d(TAG, "TLV_REQ_UNICAST_GIVE收到私信礼物1");
            final BytesReader.GiftMessage message = reader.getGiftObject();
            message.time = reader.readInt();
            message.audienceID = reader.readString();
            message.giftID = reader.readString();
            message.follow = reader.readByte();
            message.gender = reader.readString();
            message.avatar = reader.readString();
            message.nickName = reader.readString();
            message.level = reader.readShort();
            message.type = TLV_REQ_UNICAST_GIVE;
//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatReceiveGift(message);
//                        }
//                    });
//                }
//            }

            if ("0".equals(message.giftID)) {
                Log.d(TAG, "收到空礼物消息");
                return;
            }

            //todo 模拟收到神秘礼物
//            if(BuildConfig.DEBUG){
//                if("24".equals(message.giftID)){
//                    message.giftID = "30";
//                }
//            }

            Log.d(TAG, String.format("收到礼物 来自:%s id:%s 连送数:%d 用户ID:%s  等级:%d", message.nickName, message.giftID, message.count, message.audienceID, message.level));
            ChatMsg giftMessagee = BaseActivity.createGiftMessagee(GiftResManager.getInstance().getReceiveGiftContent(message.giftID), false, message.time, message.audienceID, "", "", Integer.parseInt(message.giftID));
            boolean isUnread = true;
            if (message.audienceID.equals(ChatMessageView.otherUserId)) {
                isUnread = false;
            }
            BaseActivity.saveMessage(giftMessagee, isUnread, message.follow, message.level, Common.GENDER_MAN.equals(message.gender) ? ListUserInfo.GENDER_BOY : ListUserInfo.GENDER_GIRL);
            sendEvent(new PostEvent(giftMessagee, SubcriberTag.RECEIVE_CHAT_GIFT));
        } else if (rspcode == TLV_RSP_GET_OFFLINE_MESSAGE) {
            int count = reader.readInt();
            Log.d(TAG, "TLV_RSP_GET_OFFLINE_MESSAGE 收到离线消息返回 count=" + count);
            handleOfflineMessage(reader, count);

            if (count > 0) {
                Log.d(TAG, "还有离线消息，3秒后再获取");
                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOffineMessage(5);
                    }
                }, 3000);
            }
        } else if (rspcode == TLV_RSP_SYSTEM_NOTICE) {
            int status = reader.readInt();
            if (status == TLV_E_OK) {
                int count = reader.readInt();
                Log.d(TAG, "TLV_RSP_SYSTEM_NOTICE 收到官方公告返回 count=" + count);
                handleGuanfang(reader, count);
            }
        }

//        else if (rspcode == TLV_REQ_PUSH_OFFLINE_MESSAGE) {//已废弃
//            int count = reader.readInt();
//            Log.d(TAG, "TLV_REQ_PUSH_OFFLINE_MESSAGE 收到离线消息条目推送 count=" + count);
//            if(count>0)
//            getOffineMessage(count);
//        }
//        else if(rspcode==TLV_RSP_UNICAST_GIVE){
//            Log.d(TAG,"TLV_REQ_UNICAST_GIVE收到私信礼物2");
//            BytesReader.GiftMessage message = reader.readGiftMessage();
//            for (OnResultListener mResultListener:  mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatReceiveGift(message);
//            }
//        }
        else if (rspcode == TLV_REQ_MULTICAST_ROOM_GIVE) {
            final BytesReader.GiftMessage message = reader.readGiftMessage();
            message.type = TLV_REQ_MULTICAST_ROOM_GIVE;
//            //todo 模拟收到神秘礼物
//            if(BuildConfig.DEBUG){
//                if("24".equals(message.giftID)){
//                    message.giftID = "30";
//                }
//            }
//            if ("40".equals(message.giftID)) {//默认礼物图不显示
//                message.giftID = "9999";
//            }

            final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
            for (final OnResultListener mResultListener : listeners) {
                if (null != mResultListener) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultListener.chatReceiveGift(message);
                        }
                    });
                }
            }
//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatReceiveGift(message);
//                        }
//                    });
//                }
//            }
        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_SAY) { // 多播消息
            final BytesReader.AudienceMessage message = reader.readAudienceMessage();

            final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
            for (final OnResultListener mResultListener : listeners) {
                if (null != mResultListener) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultListener.chatDidReceiveMessage(message);
                        }
                    });
                }
            }
//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatDidReceiveMessage(message);
//                        }
//                    });
//                }
//            }
        } else if (rspcode == TLV_REQ_UNICAST_SAY) { //单播消息
            final BytesReader.AudienceMessage message = reader.getAudienceMessageObj();
            message.time = reader.readInt();
            message.audienceID = reader.readString();
            message.content = reader.readString();
            message.follow = reader.readByte();
            message.gender = reader.readString();
            message.avatar = reader.readString();
            message.nickName = reader.readString();
            message.level = reader.readShort();
//            message.msgType = reader.readString();
            ChatMsg chatMsg = BaseActivity.createMessagee(message.content, false, message.time, message.audienceID, message.nickName, message.avatar);
            boolean isUnread = true;
            if (!Utils.isEmpty(message.audienceID) && message.audienceID.equals(ChatMessageView.otherUserId)) {
                isUnread = false;
            }
            BaseActivity.saveMessage(chatMsg, isUnread, message.follow, message.level, Common.GENDER_MAN.equals(message.gender) ? ListUserInfo.GENDER_BOY : ListUserInfo.GENDER_GIRL);
            Log.d(TAG, String.format("收到消息 来自:%s 类型:%d 内容:%s 等级:%d id:%s time:%d  type:%d  %s", message.nickName, message.type, message.content, message.level
                    , message.audienceID, message.time, message.type, "follow=" + message.follow + " gender=" + message.gender + " avatar=" + message.avatar
                            + " nickName=" + message.nickName + " level=" + message.level));
            sendEvent(new PostEvent(chatMsg, SubcriberTag.RECEIVE_CHAT_MSG));
        } else if (rspcode == TLV_REQ_SYSTEM_SAY) {// // 单播系统私信给对方
            final BytesReader.AudienceMessage message = reader.getAudienceMessageObj();
            message.time = reader.readInt();
            message.audienceID = reader.readString();
            message.msgType = reader.readString();
            message.content = reader.readString();
            message.follow = reader.readByte();
            message.gender = reader.readString();
            message.avatar = reader.readString();
            message.nickName = reader.readString();
            message.level = reader.readShort();
            Log.d(TAG, String.format("收到系统消息 来自:%s 类型:%d 内容:%s 等级:%d id:%s time:%d  type:%d  %s", message.nickName, message.type, message.content, message.level
                    , message.audienceID, message.time, message.type, "follow=" + message.follow + " gender=" + message.gender + " avatar=" + message.avatar
                            + " nickName=" + message.nickName + " level=" + message.level + " msgType=" + message.msgType));
            if (!Utils.isEmpty(message.msgType) && ("system".equals(message.msgType) || "attention".equals(message.msgType))) {
                ChatMsg chatMsg = new ChatMsg();
                chatMsg.setNickName(message.audienceID);
                chatMsg.setAvatar(message.avatar);
                chatMsg.setContent(message.content);
                chatMsg.setDate((long) message.time);
                chatMsg.setUserId(DbManger.SESSION_ID_LAKA_MISHU);

                if ("system".equals(message.msgType)) {
                    chatMsg.setType(DbManger.TYPE_CHAT_MISHU_SYSTEM);
                } else {
                    chatMsg.setType(DbManger.TYPE_CHAT_MISHU_ATTENTION);
                }
                BaseActivity.saveMessage(chatMsg, true, message.follow, message.level, Common.GENDER_MAN.equals(message.gender) ? ListUserInfo.GENDER_BOY : ListUserInfo.GENDER_GIRL);
                EventBusManager.postEvent(0, SubcriberTag.RECEIVE_SYSTEM_MSG);
                EventBusManager.postEvent(1l, SubcriberTag.REFRESH_BOTTOM_UNREAD_RED);
            }
            EventBusManager.postEvent(0, SubcriberTag.REFRESH_CHAT_SESSION);
        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_BULLET) { // 多播消息
            final BytesReader.BulletMessage message = reader.readBulletMessage();

            final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
            for (final OnResultListener mResultListener : listeners) {
                if (null != mResultListener) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultListener.chatReceiveBullet(message);
                        }
                    });
                }
            }
//            for (final OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener) {
//                    mMainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mResultListener.chatReceiveBullet(message);
//                        }
//                    });
//                }
//            }
        } else if (rspcode == TLV_RSP_QUERY_ROOM_USER) {
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                // 需要显示的房间人数
                final int audienceShowNumber = reader.readInt();

                final ArrayList<BytesReader.Audience> audiences = new ArrayList<>();
                for (int i = 0; i < audienceShowNumber; i++) {
                    BytesReader.Audience object = reader.readAudience();
                    if (null != object && !Utils.isEmpty(object.id))
                        audiences.add(object);
                }

                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatDidQueryRoomUser(audienceShowNumber, audiences);
                            }
                        });
                    }
                }
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidQueryRoomUser(audienceShowNumber, audiences);
//                            }
//                        });
//                    }
//                }
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_QUERY_ROOM_DATA) {
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                final long coin = reader.readLong();
                final int audienceCount = reader.readInt();
                short connectCnt = reader.readShort();//房间可连麦人数 主播关闭连麦或未达到连麦条件时为0
                byte isCanConnect = reader.readByte();//自己是否满足连麦
                byte isOpenConnect = reader.readByte();//自己是否打开连麦

                Bundle bd = new Bundle();
                bd.putInt(Common.CONNECT_CNT, connectCnt);
                bd.putInt(Common.IS_CAN_CONNECT, isCanConnect);
                bd.putInt(Common.IS_OPEN_CONNECT, isOpenConnect);
                EventBusManager.postEvent(bd, SubcriberTag.ROOM_LINK_DATA);
                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatDidQueryRoomData(coin, audienceCount);
                            }
                        });
                    }
                }

//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidQueryRoomData(coin, audienceCount);
//                            }
//                        });
//                    }
//                }
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_GET_USER_INFO) {
            int errcode = reader.readInt();
//            Log.d(TAG,"TLV_RSP_GET_USER_INFO 获取用户信息返回 errcode="+errcode);
            if (errcode == TLV_E_OK) {
                final BytesReader.Audience object = reader.getAudienceObj();
                object.id = reader.readString();
                object.nickName = reader.readString();
                object.avatar = reader.readString();
//                object.auth = reader.readByte();
                object.auth = reader.readChar();
                object.level = reader.readShort();
                object.isFollow = reader.readByte();
                object.gender = reader.readString();

                final OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
                for (final OnResultListener mResultListener : listeners) {
                    if (null != mResultListener) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultListener.chatDidQueryUserInfo(object);
                            }
                        });
                    }
                }
//                for (final OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener) {
//                        mMainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mResultListener.chatDidQueryUserInfo(object);
//                            }
//                        });
//                    }
//                }
            } else {
                handleError(errcode);
            }
        } else if (rspcode == TLV_RSP_FORBID_OPEN_ROOM) {
            handleForbidOpenRoom(reader);
        } else if (rspcode == TLV_RSP_ROOM_FORBID) {
            handleOnRoomForbidden(reader);
        } else if (rspcode == TLV_RSP_ADD_ROOM_ADMIN) {
            handleOnResultAddRoomAdmin(reader);
        } else if (rspcode == TLV_RSP_DEL_ROOM_ADMIN) {
            handleOnResultRemoveRoomAdmin(reader);
        } else if (rspcode == TLV_RSP_ROOM_PERMIT_SAY) {
            handleOnRoomCancelForbidden(reader);
        } else if (rspcode == TLV_REQ_UNICAST_ADD_ROOM_ADMIN) {
            handleOnNotifyWhoIsToBeAdmin(reader);
        } else if (rspcode == TLV_REQ_UNICAST_DEL_ROOM_ADMIN) {
            handleOnNotifyWhoCancelAdmin(reader);
        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_PERMIT_SAY) {
            handleNotifyWhoIsCancelForbidden(reader);
        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_FORBID) {
            handleNotifyWhoIsForbidden(reader);
        } else if (rspcode == TLV_REQ_MULTICAST_BROADCASTER_LEAVE) {
            EventBusManager.postEvent(0, SubcriberTag.NOTICE_ZHUBO_LEAVE);
        } else if (rspcode == TLV_REQ_MULTICAST_BROADCASTER_RETURN) {
            EventBusManager.postEvent(0, SubcriberTag.NOTICE_ZHUBO_RETURN);
        } else if (rspcode == TLV_REQ_MULTICAST_MSG) {
            handleMulticast(content);
        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_USER) {
            Log.d(TAG, " TLV_REQ_MULTICAST_ROOM_USER 房间用户列表变化广播");
            EventBusManager.postEvent(0, SubcriberTag.ROOM_USER_LIST_CHANGE);
        } else if (rspcode == TLV_RSP_QUERY_LINK_LIST) {
            handleQueryLinkList(reader);
        } else if (rspcode == TLV_RSP_QUERY_ONLINE_LIST) {
            handleQueryOnlineList(reader);
        } else if (rspcode == TLV_RSP_FORBID_LINK_MIC) {
            Log.d(TAG, " TLV_RSP_FORBID_LINK_MIC   ");
            int errcode = reader.readInt();
//            if(errcode==TLV_E_USER_FORBIDDEN){
//
//            }
            if (errcode == TLV_E_OK) {

            }
        } else if (rspcode == TLV_REQ_MULTICAST_LINK_MIC) {
            Log.d(TAG, " TLV_REQ_MULTICAST_LINK_MIC 接收广播连麦消息 ");
            Bundle bd = new Bundle();
            int sign = reader.readInt();
            String userId = reader.readString();
            bd.putInt(Common.SIGN, sign);
            bd.putString(Common.USER_ID, userId);
            EventBusManager.postEvent(bd, SubcriberTag.LINK_MIC_MULTICAST);

        } else if (rspcode == TLV_RSP_ADD_LINK_MIC) {// 添加连麦返回
            int errcode = reader.readInt();
            Log.d(TAG, " TLV_RSP_ADD_LINK_MIC 添加连麦返回");
            Bundle bd = new Bundle();
            bd.putInt(Common.ERROE_CODE, errcode);
            if (errcode == TLV_E_OK) {
                String userId = reader.readString();
                short status = reader.readShort();
                Log.d(TAG, " 添加连麦成功 userId=" + userId + " status=" + status);
                bd.putString(Common.USER_ID, userId);
                bd.putShort(Common.STATUS, status);
            } else {
                String errorMsg = reader.readString();
                bd.putString(Common.ERROR_MSG, errorMsg);
                handleError(errcode);
            }
            EventBusManager.postEvent(bd, SubcriberTag.ADD_LINK_MIC_RSP);
        } else if (rspcode == TLV_REQ_UNICAST_LINK_MIC) {// 单播连麦消息 （当粉丝请求连麦成功后会单播连麦消息给主播）
            Bundle bd = new Bundle();
            Log.d(TAG, " TLV_REQ_UNICAST_LINK_MIC 单播连麦消息 ");
            int sign = reader.readInt();
            String userId = reader.readString();
            int result = reader.readInt();
            short status = -1;
            if (result == 0) {
                status = reader.readShort();//连麦状态 0 未开始连麦， 1 主播确认连麦等待观众确认，2，观众确认连麦等待主播确认， 3 连麦已确认等待连麦成功，7 连麦成功，14 连麦失败
            }
            bd.putInt(Common.SIGN, sign);
            bd.putString(Common.USER_ID, userId);
            bd.putInt(Common.RESULT, result);
            bd.putShort(Common.STATUS, status);

            EventBusManager.postEvent(bd, SubcriberTag.LINK_MIC_UNICAST);
        } else if (rspcode == TLV_RSP_CONFIRM_LINK_MIC) {// 确认连麦 返回
            int errcode = reader.readInt();
            Log.d(TAG, " TLV_RSP_CONFIRM_LINK_MIC 确认连麦 返回 errcode+" + errcode);
            Bundle bd = new Bundle();
            bd.putInt(Common.ERROE_CODE, errcode);
            if (errcode == TLV_E_OK) {
                String userId = reader.readString();
                short status = reader.readShort();
                Log.d(TAG, " 确认连麦成功 userId=" + userId + " status=" + status);
                bd.putString(Common.USER_ID, userId);
                bd.putShort(Common.STATUS, status);
            } else {
                String errorMsg = reader.readString();
                bd.putString(Common.ERROR_MSG, errorMsg);
                handleError(errcode);
            }
            EventBusManager.postEvent(bd, SubcriberTag.CONFIRM_LINK_MIC_RSP);


        } else if (rspcode == TLV_RSP_REFUSE_LINK_MIC) {// 拒绝连麦 返回
            int errcode = reader.readInt();
            Log.d(TAG, " TLV_RSP_REFUSE_LINK_MIC 拒绝连麦 返回 errcode=" + errcode);
            Bundle bd = new Bundle();
            bd.putInt(Common.ERROE_CODE, errcode);
            if (errcode == TLV_E_OK) {

            } else {
                handleError(errcode);
            }
            EventBusManager.postEvent(bd, SubcriberTag.REFUSE_LINK_MIC_RSP);


        } else if (rspcode == TLV_RSP_OPEN_LINK_MIC) {// 打开/完成连麦（粉丝端请求）返回
            int errcode = reader.readInt();
            Log.d(TAG, " TLV_RSP_OPEN_LINK_MIC 打开/完成连麦 返回 errcode=" + errcode);
            Bundle bd = new Bundle();
            bd.putInt(Common.ERROE_CODE, errcode);
            if (errcode == TLV_E_OK) {
                String userId = reader.readString();
                Log.d(TAG, " 打开/完成连麦 userId=" + userId);
                bd.putString(Common.USER_ID, userId);
            } else {
                handleError(errcode);
            }
            EventBusManager.postEvent(bd, SubcriberTag.OPEN_LINK_MIC_RSP);
        } else if (rspcode == TLV_RSP_CLOSE_LINK_MIC) {
            int errcode = reader.readInt();
            Log.d(TAG, " TLV_RSP_CLOSE_LINK_MIC 取消/断开连麦 返回 errcode=" + errcode);
            Bundle bd = new Bundle();
            bd.putInt(Common.ERROE_CODE, errcode);
            if (errcode == TLV_E_OK) {
                String userId = reader.readString();
                Log.d(TAG, " 取消/断开连麦 返回 userId=" + userId);
                bd.putString(Common.USER_ID, userId);
            } else {
                handleError(errcode);
            }
            EventBusManager.postEvent(bd, SubcriberTag.CLOSE_LINK_MIC_RSP);
        } else if (rspcode == TLV_RSP_QUERY_LINK_USER) {
            int errcode = reader.readInt();
            Log.d(TAG, " TLV_RSP_QUERY_LINK_USERQUERY_LINK_USER 查询房间连麦用户 返回");
            Bundle bd = new Bundle();
            bd.putInt(Common.ERROE_CODE, errcode);
            if (errcode == TLV_E_OK) {
                final int audienceShowNumber = reader.readInt();
                final ArrayList<BytesReader.Audience> audiences = new ArrayList<>();
                for (int i = 0; i < audienceShowNumber; i++) {
                    BytesReader.Audience object = reader.readLinkUser();
                    Log.d(TAG, " 有连麦用户 userId=" + object.id + " channelId=" + object.channelId
                            + " streamId=" + object.streamId + " isOpen=" + object.isVideoOpen);
                    if (null != object && !Utils.isEmpty(object.id))
                        audiences.add(object);
                }
                bd.putSerializable("list", audiences);
                EventBusManager.postEvent(bd, SubcriberTag.QUERY_LINK_USER);
            } else {
                handleError(errcode);
            }

        } else if (rspcode == TLV_RSP_CLEAR_LINK_LIST) {
            int errcode = reader.readInt();
            Log.d(TAG, " TLV_RSP_CLEAR_LINK_LIST 清空房间连麦列表 返回");
            Bundle bd = new Bundle();
            bd.putInt(Common.ERROE_CODE, errcode);
            if (errcode == TLV_E_OK) {

            } else {
                handleError(errcode);
            }
            EventBusManager.postEvent(bd, SubcriberTag.CLEAR_LINK_LIST_RSP);
        } else if (rspcode == TLV_RSP_POST_LIVE_DATA) {
            int errcode = reader.readInt();
            if (errcode == TLV_E_OK) {
                Log.d(TAG, " TLV_RSP_POST_LIVE_DATA 操作成功 返回");
            } else {
                Log.d(TAG, " TLV_RSP_POST_LIVE_DATA 操作失败 返回");
            }
        } else if (rspcode == TLV_REQ_MULTICAST_LIVE_DATA) {
            Log.d(TAG, " TLV_REQ_MULTICAST_LIVE_DATA 广播直播状态");
            int sign = reader.readInt();
            String id = reader.readString();
            short tag = reader.readShort();
            Log.d(TAG, " sign=" + sign + " id=" + id + " tag=" + tag);

            if (sign == 1) {
                EventBusManager.postEvent((int) tag, SubcriberTag.LIVE_CAMERA_CHANGE);
            }
        }
    }

    public static Double translateKazuan(long kazuan) {
        double weidou = 0;
        int rate = UiPreference.getInt(Common.COIN_DISPLAY_RATE, 1);
        int decimal = UiPreference.getInt(Common.COIN_DISPLAY_DECIMAL, 0);
        weidou = NumberUtils.splitDouble((double) kazuan / (double) rate, decimal);
        Log.d(TAG, " rate=" + rate + " decimal=" + decimal + " kazuan=" + kazuan + " weidou=" + weidou);
        return weidou;
    }

    private void handleQueryLinkList(BytesReader reader) {
        if (reader != null) {
            int errorCode = reader.readInt();
            if (errorCode == TLV_E_OK) {
                int count = reader.readInt();
                List<ConnectUserInfo> list = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    list.add(reader.readLinkUserInfo());
                }
                EventBusManager.postEvent(list, SubcriberTag.TLV_RSP_QUERY_LINK_LIST_EVENT);
            } else {
                EventBusManager.postEvent(null, SubcriberTag.TLV_RSP_QUERY_LINK_LIST_EVENT);
            }
        }
    }

    private void handleQueryOnlineList(BytesReader reader) {
        if (reader != null) {
            int errorCode = reader.readInt();
            if (errorCode == TLV_E_OK) {

                OnlineUserMessage message = new OnlineUserMessage();
                message.setCount(reader.readInt());
                int count = reader.readInt();
                List<ConnectUserInfo> list = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    list.add(reader.readOnlineUserInfo());
                }
                message.setList(list);
                EventBusManager.postEvent(message, SubcriberTag.TLV_RSP_QUERY_ONLINE_LIST_EVENT);
            } else {
                EventBusManager.postEvent(null, SubcriberTag.TLV_RSP_QUERY_ONLINE_LIST_EVENT);
            }
        }
    }

    //chuan
//    private boolean isCurrentUser(String senderId){
//        if (senderId.equals(ChatMessageActivity.otherUserId)){
//            Log.d(TAG , "ChatMessageActivity.otherUserId :"+ChatMessageActivity.otherUserId);
//            return true ;
//        }
//
//        if (senderId.equals(ChatMessageView.otherUserId)){
//            Log.d(TAG , "ChatMessageView.otherUserId :" + ChatMessageView.otherUserId);
//            return true ;
//        }
//        return false ;
//    }

    private void handleMulticast(byte[] content) {
        BytesReader reader = new BytesReader(content);

        while (reader.hasRemaining()) {
            int len = reader.readInt();
            Log.error(TAG, "handleMulticast : len = " + len);
            if (len <= 0) {
                break;
            }
            int code = reader.readInt();
            Log.error(TAG, "handleMulticast : code = " + Integer.toHexString(code));
            if (code == -1) {
                break;
            }
            int dataLen = len - 8;
            Log.error(TAG, "handleMulticast dataLen = " + dataLen);
            byte[] castData;
            if (dataLen <= 0) {
                castData = new byte[0];
            } else {
                castData = reader.readBytes(dataLen);
                if (castData == null) {
                    break;
                }
            }
            handleDecrtptedData(code, castData);
        }

    }

    private void handleGuanfang(BytesReader reader, int count) {
        boolean isUnread = false;
        if (count > 0) {
            if (count == 1) {
                isUnread = true;
            }

            for (int i = 0; i < count; i++) {
                ChatMsg chatMsg = new ChatMsg();
                chatMsg.setId((long) reader.readInt());
                chatMsg.setNickName(reader.readString());
                chatMsg.setContent(reader.readString());
                chatMsg.setDate((long) reader.readInt());
                chatMsg.setUserId(DbManger.SESSION_ID_LAKA_GUANFANG);
                BaseActivity.saveMessage(chatMsg, isUnread);
                Log.d(TAG, "收到公告 title=" + chatMsg.getNickName() + " content=" + chatMsg.getContent());
            }
            EventBusManager.postEvent(0, SubcriberTag.REFRESH_CHAT_SESSION);
        }
    }

    private void handleOfflineMessage(BytesReader reader, int count) {
        if (count > 0) {
            boolean isNeedRefresh = false;
            List<ChatMsg> msgs = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String data = reader.readString();
//                Log.d(TAG, " data=" + data);
//                     String[] datas = data.split("\\|");
                String[] datas = CSVFileUtil.fromCSVLine(data, 4);

                ChatMsg chatMsg = new ChatMsg();
                String userId = datas[0];

                int gender = 0, follow = 0, level = 0;
                if ("0".equals(userId)) {
                    Log.d(TAG, "TLV_RSP_GET_OFFLINE_MESSAGE 收到离线系统消息 ");
//                    Log.d(TAG, "TLV_RSP_GET_OFFLINE_MESSAGE 收到离线系统消息" +
//                            "id=" + datas[0] + " time=" + datas[1] + " type=" + datas[2] + " content=" +
//                            datas[3]);
                } else {
                    Log.d(TAG, "TLV_RSP_GET_OFFLINE_MESSAGE 收到离线消息");
//                    Log.d(TAG, "TLV_RSP_GET_OFFLINE_MESSAGE 收到离线消息" +
//                            "id=" + datas[0] + " time=" + datas[1] + " type=" + datas[2] + " content=" +
//                            datas[3] + " follow=" + datas[4] + " gender=" + datas[5] + " avatar=" + datas[6]
//                            + " nickname=" + datas[7] + " level=" + datas[8]);
                }

                if (userId.equals(ChatMessageActivity.otherUserId)) {
                    isNeedRefresh = true;
                }

                chatMsg.setUserId(userId);
                long dataTime = 0;
                try {
                    dataTime = Long.parseLong(datas[1]);//保存是秒
                } catch (NumberFormatException e) {

                }
//                Log.d(TAG,"时间="+Utils.LONG_DATE_FORMATER.format(new Date(dataTime))
//                +" dataTime="+dataTime);
                chatMsg.setDate(dataTime);
                String type = datas[2];
                if (!"0".equals(userId)) {
                    chatMsg.setAvatar(datas[6]);
                    chatMsg.setNickName(datas[7]);
                    try {
                        follow = Integer.parseInt(datas[4]);
                    } catch (NumberFormatException e) {
                        follow = 0;
                    }

                    try {
                        gender = Integer.parseInt(datas[5]);
                    } catch (NumberFormatException e) {
                        gender = 1;
                    }
                    try {
                        level = Integer.parseInt(datas[8]);
                    } catch (NumberFormatException e) {
                        level = 1;
                    }
                }
                chatMsg.setIsSend(false);
                chatMsg.setIsUnread(true);
                if (type.equals("say")) {
                    chatMsg.setContent(datas[3]);
                    chatMsg.setType(0);
                    msgs.add(chatMsg);
                    DbManger.getInstance().addChatMsg(chatMsg, follow, level, gender, true);
                } else if (type.equals("give")) {
                    chatMsg.setType(1);
                    String giftId = datas[3];
                    if ("0".equals(giftId)) {
                        continue;
                    }
                    chatMsg.setGiftId(Integer.parseInt(giftId));
                    chatMsg.setContent(GiftResManager.getInstance().getReceiveGiftContent(giftId));
                    msgs.add(chatMsg);
                    DbManger.getInstance().addChatMsg(chatMsg, follow, level, gender, true);
                } else if (type.equals("system")) {
                    chatMsg.setContent(datas[3]);
                    chatMsg.setUserId(DbManger.SESSION_ID_LAKA_MISHU);
                    chatMsg.setType(DbManger.TYPE_CHAT_MISHU_SYSTEM);
                    chatMsg.setNickName(datas[0]);
                    Log.d(TAG, "收到system title=" + chatMsg.getNickName() + " content=" + chatMsg.getContent()
                            + " Avatar=" + chatMsg.getAvatar());
                    msgs.add(chatMsg);
                    DbManger.getInstance().addChatMsg(chatMsg);
                } else if (type.equals("attention")) {
                    chatMsg.setContent(datas[3]);
                    chatMsg.setUserId(DbManger.SESSION_ID_LAKA_MISHU);
                    chatMsg.setType(DbManger.TYPE_CHAT_MISHU_ATTENTION);
                    chatMsg.setNickName(datas[0]);
                    Log.d(TAG, "收到attention title=" + chatMsg.getNickName() + " content=" + chatMsg.getContent()
                            + " Avatar=" + chatMsg.getAvatar());
                    msgs.add(chatMsg);
                    DbManger.getInstance().addChatMsg(chatMsg);
                }
            }
//            DbManger.getInstance().addChatMsgs(msgs);
            //存入数据库
//                for (OnResultListener mResultListener:  mResultListeners) {
//                    if (null != mResultListener)
//                        mResultListener.chatOffline(msgs);
//                }
            if (isNeedRefresh) {
                sendEvent(new PostEvent(0, SubcriberTag.REFRESH_CHAT_MESSGAE));
            }
            sendEvent(new PostEvent(0, SubcriberTag.REFRESH_CHAT_SESSION));
            EventBusManager.postEvent(1l, SubcriberTag.REFRESH_BOTTOM_UNREAD_RED);
        } else {
            Log.d(TAG, "TLV_RSP_GET_OFFLINE_MESSAGE 收到离线消息 count=0");
        }
    }

    //后台控制用户禁止直播
    private void handleForbidOpenRoom(BytesReader reader) {
        int errorCode = reader.readInt();
        Log.d(TAG, " handleForbidOpenRoom errorCode=" + errorCode);
//        if (errorCode != TLV_E_OK) {
//            handleError(errorCode);
//            return;
//        }
        EventBusManager.postEvent(TLV_E_USER_FORBIDDEN, SubcriberTag.FORBID_OPEN_ROOM);

    }

    private void sendEvent(final PostEvent postEvent) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                EventBusManager.postEvent(postEvent.event, postEvent.tag);
            }
        });
    }

    private void handleNotifyWhoIsCancelForbidden(BytesReader reader) {
        final UserInfo userInfo = new UserInfo();
        userInfo.setId(StringUtils.parseInt(reader.readString()));
        userInfo.setNickName(reader.readString());
        if (mCallback == null) {
            return;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(TLV_REQ_MULTICAST_ROOM_PERMIT_SAY, userInfo);
            }
        });
    }

    private void handleNotifyWhoIsForbidden(BytesReader reader) {
        final UserInfo userInfo = new UserInfo();
        userInfo.setId(StringUtils.parseInt(reader.readString()));
        userInfo.setNickName(reader.readString());
        int time = reader.readInt();//时间戳
        if (mCallback == null) {
            return;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(TLV_REQ_MULTICAST_ROOM_FORBID, userInfo);
            }
        });
    }

    private void handleOnNotifyWhoCancelAdmin(BytesReader reader) {
        final UserInfo userInfo = null;
        if (mCallback == null) {
            return;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(TLV_REQ_UNICAST_DEL_ROOM_ADMIN, userInfo);
            }
        });
    }

    private void handleOnNotifyWhoIsToBeAdmin(BytesReader reader) {
        if (mCallback == null) {
            return;
        }
//        int time = reader.readInt();
        final UserInfo userInfo = null;
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(TLV_REQ_UNICAST_ADD_ROOM_ADMIN, userInfo);
            }
        });
    }

    //取消管理员权限处理
    private void handleOnResultRemoveRoomAdmin(BytesReader reader) {
        int errorCode = reader.readInt();
        if (errorCode != TLV_E_OK) {
            handleError(errorCode);
            return;
        }
        final UserInfo userInfo = null;
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onSuccess(RequestType.REQUEST_TYPE_REMOVE_ADMIN, userInfo);
                }
                if (mAdminResultCallback != null) {
                    mAdminResultCallback.onSuccess(RequestType.REQUEST_TYPE_REMOVE_ADMIN, userInfo);
                }
            }
        });


    }

    //添加管理员结果处理
    private void handleOnResultAddRoomAdmin(BytesReader reader) {
        int errorCode = reader.readInt();
        if (errorCode != TLV_E_OK) {
            handleError(errorCode);
            return;
        }
        final UserInfo userInfo = null;
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onSuccess(RequestType.REQUEST_TYPE_ADD_ADMIN, userInfo);
                }
                if (mAdminResultCallback != null) {
                    mAdminResultCallback.onSuccess(RequestType.REQUEST_TYPE_ADD_ADMIN, userInfo);
                }
            }
        });

    }


    //禁言处理
    private void handleOnRoomForbidden(BytesReader reader) {
        int errorCode = reader.readInt();
        if (errorCode != TLV_E_OK) {
            handleError(errorCode);
            return;
        }
        if (mCallback == null) {
            return;
        }
        final UserInfo userInfo = null;
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(RequestType.REQUEST_TYPE_FORBIDDEN, userInfo);
            }
        });


    }

    //取消禁言处理
    private void handleOnRoomCancelForbidden(BytesReader reader) {
        int errorCode = reader.readInt();
        if (errorCode != TLV_E_OK) {
            handleError(errorCode);
            return;
        }
        if (mCallback == null) {
            return;
        }
        final UserInfo userInfo = null;
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(RequestType.REQUEST_TYPE_CANCEL_FORBIDDEN, userInfo);
            }
        });

    }

//    private void handleTransferDataRspMainThread(Message msg) {
//        byte[] content = (byte[]) msg.obj;
//        BytesReader reader = new BytesReader(content);
//
//        int rspcode = reader.readInt();
//        if (rspcode == TLV_RSP_OPEN_ROOM) { // 打开房间
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                isInRoom = true;
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener)
//                        mResultListener.chatDidOpenRoom();
//                }
//            } else {
//                isInRoom = false;
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_ENTER_ROOM) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                Log.debug(TAG, "进入房间成功");
//                isInRoom = true;
//
//                BytesReader.Anchor anchor = reader.readAnchor();
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (null != anchor && null != mResultListener) {
//                        mResultListener.chatDidEnterRoom(anchor);
//
//                        // 需要显示的房间人数
//                        int audienceShowNumber = reader.readInt();
//
//                        ArrayList<BytesReader.Audience> audiences = new ArrayList<>();
//                        for (int i = 0; i < audienceShowNumber; i++) {
//                            BytesReader.Audience object = reader.readAudience();
//                            if (null != object)
//                                audiences.add(object);
//                        }
//
//                        if (null != mResultListener)
//                            mResultListener.chatDidQueryRoomUser(audienceShowNumber, audiences);
//                    }
//                }
//            } else {
//                isInRoom = false;
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_CLOSE_ROOM) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                isInRoom = false;
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener)
//                        mResultListener.chatDidCloseRoom();
//                }
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_LEAVE_ROOM) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                isInRoom = false;
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener)
//                        mResultListener.chatDidExitRoom();
//                }
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_REQ_MULTICAST_CLOSE_ROOM) { // 房间关闭
//            isInRoom = false;
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatDidCloseRoom();
//            }
//        } else if (rspcode == TLV_RSP_ROOM_SAY) { // 发送消息回应
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                Log.debug(TAG, "发送消息成功");
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_ROOM_LIKE) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                Log.debug(TAG, "点赞成功");
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_ROOM_GIVE) {
//            int errcode = reader.readInt();
//            Log.d(TAG, "TLV_RSP_ROOM_GIVE errcode=" + errcode);
//            if (errcode == TLV_E_OK) {
//                int kazuan = reader.readInt();
//                Log.debug(TAG, "送礼成功 剩余钻石=" + kazuan);
//                EventBusManager.postEvent(new PostEvent(kazuan, SubcriberTag.REFRESH_LAST_KAZUAN));
//            } else if (errcode == TLV_E_KAZUAN_NOT_ENOUGH) {
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null)
//                        mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "");
//                }
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_SAY) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                Log.debug(TAG, "私信发送成功");
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null)
//                        mResultListener.chatDidSuccess(TLV_RSP_SAY);
//                }
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_GIVE) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                int kazuan = reader.readInt();
//                Log.debug(TAG, "私信送礼成功 剩余钻石=" + kazuan);
//                EventBusManager.postEvent(new PostEvent(kazuan, SubcriberTag.REFRESH_LAST_KAZUAN));
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null)
//                        mResultListener.chatDidSuccess(TLV_RSP_GIVE);
//                }
//            } else if (errcode == TLV_E_KAZUAN_NOT_ENOUGH) {
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null)
//                        mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "");
//                }
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_ROOM_BULLET) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                int kazuan = reader.readInt();
//                Log.debug(TAG, "发送弹幕成功 剩余钻石=" + kazuan);
//                EventBusManager.postEvent(new PostEvent(kazuan, SubcriberTag.REFRESH_LAST_KAZUAN));
//            } else if (errcode == TLV_E_KAZUAN_NOT_ENOUGH) {
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (mResultListener != null)
//                        mResultListener.chatErrorOccur(TLV_E_KAZUAN_NOT_ENOUGH, "");
//                }
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_REQ_MULTICAST_ENTER_ROOM) { // 有用户进入房间
//            BytesReader.EnterRoomMessage message = reader.readEnterRoomMessage();
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatUserEnterRoom(message);
//            }
//        } else if (rspcode == TLV_REQ_MULTICAST_LEAVE_ROOM) { // 有用户离开房间
//            Log.d(TAG, "处理用户进出房间消息");
//            // 1.用户ID
//            String userID = reader.readString();
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatUserExitRoom(userID);
//            }
//        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_LIKE) {
//            // 1.用户ID
//            String userID = reader.readString();
//            int count = reader.readInt();
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatReceiveLike(userID, count);
//            }
//        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_LIGHT) {
//            //点亮
//            String userID = reader.readString();
//            int count = reader.readInt();
//            String nickName = reader.readString();
//            int level = reader.readInt();
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatReceiveLight(userID, nickName, level);
//            }
//        } else if (rspcode == TLV_REQ_UNICAST_GIVE) {
//            Log.d(TAG, "TLV_REQ_UNICAST_GIVE收到私信礼物1");
//            BytesReader.GiftMessage message = reader.getGiftObject();
//            message.time = reader.readInt();
//            message.audienceID = reader.readString();
//            message.giftID = reader.readString();
//            message.type = TLV_REQ_UNICAST_GIVE;
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatReceiveGift(message);
//            }
//        } else if (rspcode == TLV_REQ_PUSH_OFFLINE_MESSAGE) {
//            int count = reader.readInt();
//            Log.d(TAG, "TLV_REQ_PUSH_OFFLINE_MESSAGE 收到离线消息 count=" + count);
//            if (count > 0) {
//                boolean isNeedRefresh = false;
//                boolean isUnread = true;
//                List<ChatMsg> msgs = new ArrayList<>();
//                for (int i = 0; i < count; i++) {
//                    String data = reader.readString();
//                    Log.d(TAG, " data=" + data);
////                     String[] datas = data.split("\\|");
//                    String[] datas = CSVFileUtil.fromCSVLine(data, 4);
//                    Log.d(TAG, "TLV_REQ_PUSH_OFFLINE_MESSAGE 收到离线消息" +
//                            "id=" + datas[0] + " time=" + datas[1] + " type=" + datas[2] + " content=" +
//                            datas[3]);
//                    ChatMsg chatMsg = new ChatMsg();
//                    String userId = datas[0];
//                    if (userId.equals(ChatMessageActivity.otherUserId)) {
//                        isNeedRefresh = true;
//                        isUnread = false;
//                    }
//
//                    chatMsg.setIsUnread(isUnread);
//                    chatMsg.setUserId(userId);
//                    chatMsg.setDate(Long.parseLong(datas[1]) * 1000);
//                    String type = datas[2];
//                    if (type.equals("say")) {
//                        chatMsg.setContent(datas[3]);
//                        chatMsg.setType(0);
//                    } else {
//                        chatMsg.setType(1);
//                        String giftId = datas[3];
//                        chatMsg.setGiftId(Integer.parseInt(giftId));
//                        chatMsg.setContent(GiftGridView.getReceiveGiftContent(giftId));
//                    }
//                    msgs.add(chatMsg);
//                }
//                DbManger.getInstance().addChatMsgs(msgs);
//                //存入数据库
////                for (OnResultListener mResultListener:  mResultListeners) {
////                    if (null != mResultListener)
////                        mResultListener.chatOffline(msgs);
////                }
//                if (isNeedRefresh) {
//                    EventBusManager.postEvent(new PostEvent(0, SubcriberTag.REFRESH_CHAT_MESSGAE));
//                }
//
//            } else {
//                Log.d(TAG, "TLV_REQ_PUSH_OFFLINE_MESSAGE 收到离线消息 count=0");
//            }
//
//        }
////        else if(rspcode==TLV_RSP_UNICAST_GIVE){
////            Log.d(TAG,"TLV_REQ_UNICAST_GIVE收到私信礼物2");
////            BytesReader.GiftMessage message = reader.readGiftMessage();
////            for (OnResultListener mResultListener:  mResultListeners) {
////                if (null != mResultListener)
////                    mResultListener.chatReceiveGift(message);
////            }
////        }
//        else if (rspcode == TLV_REQ_MULTICAST_ROOM_GIVE) {
//            BytesReader.GiftMessage message = reader.readGiftMessage();
//            message.type = TLV_REQ_MULTICAST_ROOM_GIVE;
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatReceiveGift(message);
//            }
//        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_SAY) { // 多播消息
//            BytesReader.AudienceMessage message = reader.readAudienceMessage();
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatDidReceiveMessage(message);
//            }
//        } else if (rspcode == TLV_REQ_UNICAST_SAY) { //单播消息
//            BytesReader.AudienceMessage message = reader.getAudienceMessageObj();
//            message.time = reader.readInt();
//            message.audienceID = reader.readString();
//            message.content = reader.readString();
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatDidReceiveMessage(message);
//            }
//        } else if (rspcode == TLV_REQ_MULTICAST_ROOM_BULLET) { // 多播消息
//            BytesReader.BulletMessage message = reader.readBulletMessage();
//            for (OnResultListener mResultListener : mResultListeners) {
//                if (null != mResultListener)
//                    mResultListener.chatReceiveBullet(message);
//            }
//        } else if (rspcode == TLV_RSP_QUERY_ROOM_USER) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                // 需要显示的房间人数
//                int audienceShowNumber = reader.readInt();
//
//                ArrayList<BytesReader.Audience> audiences = new ArrayList<>();
//                for (int i = 0; i < audienceShowNumber; i++) {
//                    BytesReader.Audience object = reader.readAudience();
//                    if (null != object)
//                        audiences.add(object);
//                }
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener)
//                        mResultListener.chatDidQueryRoomUser(audienceShowNumber, audiences);
//                }
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_QUERY_ROOM_DATA) {
//            int errcode = reader.readInt();
//            if (errcode == TLV_E_OK) {
//                int coin = reader.readInt();
//                int audienceCount = reader.readInt();
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener)
//                        mResultListener.chatDidQueryRoomData(coin, audienceCount);
//                }
//            } else {
//                handleError(errcode);
//            }
//        } else if (rspcode == TLV_RSP_GET_USER_INFO) {
//            int errcode = reader.readInt();
////            Log.d(TAG,"TLV_RSP_GET_USER_INFO 获取用户信息返回 errcode="+errcode);
//            if (errcode == TLV_E_OK) {
//                BytesReader.Audience object = reader.getAudienceObj();
//                object.id = reader.readString();
//                object.nickName = reader.readString();
//                object.avatar = reader.readString();
//                object.auth = reader.readByte();
//                object.level = reader.readShort();
//                object.isFollow = reader.readByte();
//                for (OnResultListener mResultListener : mResultListeners) {
//                    if (null != mResultListener)
//                        mResultListener.chatDidQueryUserInfo(object);
//                }
//            } else {
//                handleError(errcode);
//            }
//        }
//    }

    private void handleError(final int errorCode) {
        handleError(errorCode, 0);
    }

    // 错误处理
    private void handleError(final int errorCode, int respCode) {
        Log.d(TAG, "handleError errorCode=" + errorCode);
        // TODO: 错误处理
        String message = "";
        switch (errorCode) {
            case TLV_E_TRY_AGAIN_LATER:
                EventBusManager.postEvent(respCode, SubcriberTag.SOCKET_TRY_AGAIN_LATER);
                break;
            case TLV_E_USER_ROOM_OFFLINE:
                message = "用户不在房间";
                EventBusManager.postEvent(0, SubcriberTag.USER_ROOM_OFFINE);
                break;
            case TLV_E_RSA_PRIVATE_DECRYPT_FAILED:
                // TODO:服务器解密失败
                message = "RSA私钥解密失败";
                stopRoom();
                Log.error(TAG, message);
                break;
            case TLV_E_INVALID_USER_TOKEN:
                // TODO:Token失效
                message = "无效的用户token";
                stopRoom();
                EventBusManager.postEvent(0, SubcriberTag.UNLOGIN);
                break;
            case TLV_E_USER_IN_USER_ROOM:
                break;
            case TLV_E_ROOM_ALREADY_OPENED:
                // TODO:房间已经打开
                message = "房间已经打开";
                break;
            case TLV_E_ROOM_NOT_OPENED_YET:
                // TODO:房间未打开
                message = "房间未打开";
                break;
            case TLV_E_USER_NOT_IN_ANY_ROOM:
                // TODO:用户不在任何房间
                message = "用户不在任何房间";
                break;
            case TLV_E_USER_IN_HIS_OWN_ROOM:
                // TODO:前一个房间没关闭
                message = "用户在自己的房间里，必须关闭房间才可退出";
                break;
            case TLV_E_ROOM_NOT_EXISTS:
                // TODO:房间尚未创建
                message = "房间不存在";
                break;
            case TLV_E_USER_ALREADY_IN_OTHER_ROOM:
                // TODO:用户在其它房间
                message = "用户在其它房间";
//                exitRoom();
                break;
            case TLV_E_USER_NOT_EXISTS:
                // TODO:用户不存在
                message = "用户不存在";
                break;
            case TLV_E_EMPTY_MESSAGE:
                // TODO:发送消息为空
                message = "发送消息为空";
                break;
            case TLV_E_GIFT_NOT_EXIST:
                // TODO:发送礼物不存在
                message = "发送礼物不存在";
                break;
            case TLV_E_USER_FORBIDDEN:
                break;
            case TLV_E_USER_NOT_IN_THIS_ROOM:
                break;
            case TLV_E_USER_IS_ADMIN:
                break;
            case TLV_E_USER_NOT_ADMIN:
                break;
            case TLV_E_TOO_MANY_ADMIN:
                break;
            case TLV_E_USER_NOT_IN_ROOM_FORBID_SAY:
                break;
            default:
                message = "未知错误";
                break;
        }

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onFailed(errorCode);
                }
                if (mAdminResultCallback != null) {
                    mAdminResultCallback.onFailed(errorCode);
                }
            }
        });

        sendErrorMessage(errorCode, message);
    }

    // 开始发送心跳包
    private void startHeartbeatTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendKeepAlive();
            }
        };


        mHeartbearTimer = new Timer(true);
        mHeartbearTimer.schedule(timerTask, HEARTBEAT_INTEVAL, HEARTBEAT_INTEVAL);
    }

    // 停止发送心跳包
    private void stopHeartbertTimer() {
        if (mHeartbearTimer != null) {
            mHeartbearTimer.cancel();
            mHeartbearTimer = null;
        }
    }

    /**
     * 创建房间
     */
    public void createRoom(String title, String location, String cover, long courseId) {
        Log.d(TAG, "createRoom title=" + title + " location=" + location + " cover=" + cover + ",courseId:" + courseId);
        if (TextUtils.isEmpty(title)) title = "";
        if (TextUtils.isEmpty(location)) location = "";
        if (TextUtils.isEmpty(cover)) cover = "";
        ArrayList<Object> params = new ArrayList<>();
        params.add(title);
        params.add(location);
        params.add(cover);
        params.add(courseId);
        sendCommand(TLV_REQ_OPEN_ROOM, params);
    }

    /**
     * 关闭房间
     */
    public void closeRoom() {
//        assert (null != mUser.getToken());
        if (mUser == null || Utils.isEmpty(mUser.getToken())) {
            return;
        }

//        if (!checkInRoom()) {
//            return;
//        }
//        if(mResultListeners.size()>0){
//            Log.d(TAG,"仍然有activity在监听,不closeRoom");
//            return;
//        }

        ArrayList<String> params = new ArrayList();
        params.add(mUser.getToken());
        sendCommand(TLV_REQ_CLOSE_ROOM, params);
    }

    /**
     * 进入房间
     */
    public void enterRoom(String roomID) {
        assert (null != roomID);

        ArrayList<String> params = new ArrayList();
        params.add(roomID);
        sendCommand(TLV_REQ_ENTER_ROOM, params);
    }

    /**
     * 离开房间
     */
    public void exitRoom() {
        assert (null != mUser.getToken());
        if (!checkInRoom()) {
            return;
        }
//        if(mResultListeners.size()>0){
//            Log.d(TAG,"仍然有activity在监听,不exitRoom");
//            return;
//        }
        ArrayList<String> params = new ArrayList();
        if (mUser != null) {
            params.add(mUser.getToken());
        }
        sendCommand(TLV_REQ_LEAVE_ROOM, params);
    }

    /**
     * 聊天室功能
     * 发言/点赞/送礼
     *
     * @param message
     */
    public void sendMessage(String message) {
        if (TextUtils.isEmpty(message)) return;

//        if (!checkInRoom()) {
//            return;
//        }

        if (!isConnected()) {
            EventBusManager.postEvent(0, SubcriberTag.SOCKET_NOT_CONNECT);
        }

        ArrayList<String> params = new ArrayList();
        params.add(message);
        sendCommand(TLV_REQ_ROOM_SAY, params);
    }

    /**
     * 点亮
     *
     * @param count 点亮次数
     */
    public void like(int count) {
//        Log.d(TAG," 点赞 like="+count);
//        if (!checkInRoom()) {
//            return;
//        }
        sendCommand(TLV_REQ_ROOM_LIKE, count);
    }

    /**
     * 送礼
     *
     * @param giftID 礼物ID
     */
    public void sendGift(String giftID) {
//        if (!checkInRoom()) {
//            return;
//        }
        assert (null != giftID);

        if (!isConnected()) {
            EventBusManager.postEvent(0, SubcriberTag.SOCKET_NOT_CONNECT);
        }

        ArrayList<String> params = new ArrayList();
        params.add(giftID);
        sendCommand(TLV_REQ_ROOM_GIVE, params);
    }

    /**
     * 发送弹幕
     *
     * @param message
     */
    public void sendBullet(String message) {
//        if (!checkInRoom()) {
//            return;
//        }
        assert (null != message);


        if (!isConnected()) {
            EventBusManager.postEvent(0, SubcriberTag.SOCKET_NOT_CONNECT);
        }

        ArrayList<String> params = new ArrayList();
        params.add(message);
        sendCommand(TLV_REQ_ROOM_BULLET, params);
    }

    /**
     * 获取离线消息
     *
     * @param size
     */
    public void getOffineMessage(int size) {
        Log.d(TAG, " getOffineMessage size=" + size);
        sendCommand(TLV_REQ_GET_OFFLINE_MESSAGE, size);
    }

    /**
     * 获取官方公告
     */
    public void getGuanfangNotice() {
        Log.d(TAG, "getGuanfangNotice 获取官方公告");
        sendCommand(TLV_REQ_SYSTEM_NOTICE);
    }

    /**
     * 发送禁言,暂时不知道有什么鸟用
     */
    public void sendForbid(String forBiddenUserId) {
        List<String> params = new ArrayList<>();
        params.add(forBiddenUserId);
        sendCommand(TLV_REQ_ROOM_FORBID, params);
    }

    public void sendCancelForbidden(String userId) {
        List<String> params = new ArrayList<>();
        params.add(userId);
        sendCommand(TLV_REQ_ROOM_PERMIT_SAY, params);
    }

    /**
     * 添加房间管理员
     *
     * @param userId 用户id
     */
    public void sendAddRoomAdmin(String userId) {
        List<String> params = new ArrayList<>();
        params.add(userId);
        sendCommand(TLV_REQ_ADD_ROOM_ADMIN, params);
    }

    /**
     * 删除房间管理员
     *
     * @param userId 用户id
     */
    public void sendRemoveRoomAdmin(String userId) {
        List<String> params = new ArrayList<>();
        params.add(userId);
        sendCommand(TLV_REQ_DEL_ROOM_ADMIN, params);
    }

    /**
     * 查询房间观众信息,接口是通过回调chatDidQueryRoomUser异步返回
     *
     * @param page 页数,暂定一页20,从0开始....
     */
    public void queryRoomUser(int page) {
//        if (!checkInRoom()) {
//            return;
//        }
//        assert (page >= 0);
//        Log.d(TAG, " queryRoomUser 查询房间观众信息");
        sendCommand(TLV_REQ_QUERY_ROOM_USER, page);
    }

    /**
     * 查询房间信息,钻石数,观众总数等
     */
    public void queryRoomData() {
//        if (!checkInRoom()) {
//            return;
//        }
//        Log.d(TAG, " queryRoomData 查询房间信息");
        sendCommand(TLV_REQ_QUERY_ROOM_DATA);
    }

    /**
     * 私信功能
     * 发言/送礼
     *
     * @param message
     */
    public void inboxMessage(String message, String toUserID, int reqId) {
        assert (null != message);
        assert (null != toUserID);

        if (TextUtils.isEmpty(message)) return;
//        if (!checkInRoom()) {
//            return;
//        }
//        if (!isConnected()) {
//            Log.d(TAG, "未连接，不发消息");
//            return;
//        }
        if (!isConnected()) {
            EventBusManager.postEvent(0, SubcriberTag.SOCKET_NOT_CONNECT);
        }

        Log.d(TAG, "发送私信 message=" + message);
        ArrayList<Object> params = new ArrayList();
        params.add(toUserID);
        params.add(message);
        params.add(reqId);
        sendCommand(TLV_REQ_SAY, params);
    }

    /**
     * 私信送礼
     *
     * @param giftID 礼物ID
     */
    public void inboxGift(String giftID, String toUserID, int reqId) {
        assert (null != giftID);
//        if (!isConnected()) {
//            Log.d(TAG, "未连接，不送礼");
//            return;
//        }
        if (!isConnected()) {
            EventBusManager.postEvent(0, SubcriberTag.SOCKET_NOT_CONNECT);
        }

        ArrayList<Object> params = new ArrayList();
        params.add(toUserID);
        params.add(giftID);
        params.add(reqId);
        sendCommand(TLV_REQ_GIVE, params);
    }

    /**
     * 查询用户信息,结果通过回调异步返回
     *
     * @param userID 用户ID
     */
    public void queryUserInfo(String userID) {
//        if (!isConnected()) {
//            return;
//        }
//        Log.d(TAG,"queryUserInfo userID="+userID);
        ArrayList<String> params = new ArrayList();
        params.add(userID);
        sendCommand(TLV_REQ_GET_USER_INFO, params);
    }


    /**
     * 主播暂时离开
     */
    public void zhuboTemporaryLeave() {
        Log.d(TAG, " 主播暂时离开");
        sendCommand(TLV_REQ_BROADCATER_LEAVE);
    }


    /**
     * 主播返回
     */
    public void zhuboReturn() {
        Log.d(TAG, " 主播返回");
        sendCommand(TLV_REQ_BROADCASTER_RETURN);
    }

    /**
     * 发送客户端直播状态
     */
    public void postLiveData(short type, short status) {
        Log.d(TAG, " postLiveData type=" + type + " status=" + status);
        List<Object> list = new ArrayList<>();
        list.add(type);
        list.add(status);
        sendCommand(TLV_REQ_POST_LIVE_DATA, true, list);
    }

    /**
     * 添加连麦
     * userId 主播添加连麦时为对应用户id，粉丝请求连麦时传空字符串
     */
    public void addLinkMic(String userId) {
        Log.d(TAG, " addLinkMic  userId=" + userId);
        List<Object> list = new ArrayList<>();
        list.add(0);
        list.add(userId);
        sendCommand(TLV_REQ_ADD_LINK_MIC, true, list);
    }

    /**
     * 确认连麦
     * userId 主播确认连麦时为对应用户id，粉丝确认连麦时传空字符串
     */
    public void confirmLinkMic(String userId) {
        Log.d(TAG, " confirmLinkMic  userId=" + userId);
        List<Object> list = new ArrayList<>();
        list.add(0);
        list.add(userId);
        sendCommand(TLV_REQ_CONFIRM_LINK_MIC, true, list);
    }

    /**
     * 拒绝连麦
     * userId 主播拒绝连麦时为对应用户id，粉丝确认连麦时传空字符串
     */
    public void refuseLinkMic(String userId) {
        Log.d(TAG, " refuseLinkMic  userId=" + userId);
        List<Object> list = new ArrayList<>();
        list.add(0);
        list.add(userId);
        sendCommand(TLV_REQ_REFUSE_LINK_MIC, true, list);
    }


    /**
     * 打开连麦（完成连麦）
     * userId 主播完成连麦时为对应用户id，粉丝完成连麦时传空字符串
     */
    public void openLinkMic(String userId, int result) {
        Log.d(TAG, " openLinkMic  userId=" + userId);
        List<Object> list = new ArrayList<>();
        list.add(0);
        list.add(userId);
        list.add(result);
        sendCommand(TLV_REQ_OPEN_LINK_MIC, true, list);
    }

    /**
     * 拒绝/取消/断开连麦
     * userId 主播添断开麦时为对应用户id，粉丝断开连麦时传空字符串
     */
    public void closeLinkMic(String userId) {
        Log.d(TAG, " closeLinkMic  userId=" + userId);
        List<Object> list = new ArrayList<>();
        list.add(0);
        list.add(userId);
        sendCommand(TLV_REQ_CLOSE_LINK_MIC, true, list);
    }

    /**
     * 禁止连麦
     * userId 禁播用户id
     */
    public void forbidLinkMic(String userId) {
        Log.d(TAG, " forbidLinkMic  userId=" + userId);
        List<Object> list = new ArrayList<>();
        list.add(userId);
        sendCommand(TLV_REQ_FORBID_LINK_MIC, true, list);
    }

    /**
     * 查询房间连麦用户(成功)
     */
    public void queryLinkUsers(int page) {
        Log.d(TAG, " queryLinkUsers  page=" + page);
        List<Object> list = new ArrayList<>();
        list.add(page);
        sendCommand(TLV_REQ_QUERY_LINK_USER, true, list);
    }

    /**
     * 清空房间连麦列表
     */
    public void clearLinkList() {
        Log.d(TAG, " clearLinkList ");
        List<Object> list = new ArrayList<>();
        sendCommand(TLV_REQ_CLEAR_LINK_LIST, true, list);
    }

    /**
     * @param isRetry 是否推流重试
     * @param cpuRate cpu占用率 0-100
     * @param fps     0-60
     * @param upSpeed 上行速率(0-65535) (kbps)
     */
    public void sendAnchorData(boolean isRetry, byte cpuRate, byte fps, short upSpeed) {
        Log.d(TAG, "上报 sendAnchorData isRetry=" + isRetry + " cpuRate=" + cpuRate + " fps=" + fps + " upSpeed=" + upSpeed);
        int dataFlag = 0;
        byte retry = isRetry ? (byte) 1 : (byte) 0;
        int networdState = NetworkUtil.getNetworkState(LiveApplication.application);
        byte netState;
        switch (networdState) {
            case NetworkUtil.NETWORK_STATE_2G:
            case NetworkUtil.NETWORK_STATE_3G:
            case NetworkUtil.NETWORK_STATE_4G:
                netState = (byte) 1;
                break;
            case NetworkUtil.NETWORK_STATE_WIFI:
                netState = (byte) 2;
                break;
            default:
                netState = (byte) 0;
                break;

        }

        List<Object> list = new ArrayList<>();
        list.add(dataFlag);
        list.add(retry);
        list.add(netState);
        list.add(cpuRate);
        list.add(fps);
        list.add(upSpeed);
        sendCommand(TLV_REQ_POST_ANCHOR_DATA, false, list);
    }

    //连麦相关开始


    //连麦相关结束

    private void write(ByteBuffer buffer) {
        if (null != mSocket) {
            mSocket.write(buffer);
        } else {
            Log.d(TAG, " write mSocket==null");
        }
    }


    public void sendCommand(int type, List params) {
        sendCommand(type, true, params);
    }

    public void sendCommand(int type, boolean isEncrypt, List params) {
        if (params != null && params.isEmpty() == false) {
            sendCommand(type, isEncrypt, params.toArray());
        } else {
            sendCommand(type);
        }
    }

    public void sendCommand(int type, Object... params) {
        sendCommand(type, true, params);
    }

    private void sendCommand(int type, boolean isEncrypt, Object... params) {
        byte[] sendParams = null;
        if (params != null && params.length > 0) {
            sendParams = packCommand(isEncrypt, params);
        }

        int len = 8 + (sendParams == null ? 0 : sendParams.length);
        ByteBuffer buffer = ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(len);
        buffer.putInt(type);
        if (sendParams != null) {
            buffer.put(sendParams);
        }
        write(buffer);
    }

    private void sendKeepAlive() {

        /*int code = TLV_REQ_KEEP_ALIVE;
        int len = 0;

        ByteBuffer buffer = ByteBuffer.allocate(8 + len).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(len);
        buffer.putInt(code);
        buffer.limit();

        Log.debug(TAG, "发送心跳包");
        write(buffer);*/
        Log.debug(TAG, "发送心跳包");

        sendCommand(TLV_REQ_KEEP_ALIVE);
    }

    private void sendAesKey() {
        String token = mUser.getToken();
        byte[] tokenBytes = token.getBytes(Charset.forName("US-ASCII"));
        int tokenLen = tokenBytes.length;
        byte[] tokenLenBytes = int2Bytes(tokenLen);
        byte[] bytes = new byte[128];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
        /*byte[] zero = {0, 0, 0, 0};
        System.arraycopy(zero, 0, bytes, 0, zero.length);*/
        final int paddingSize = 64;
        padding(bytes, paddingSize);
        System.arraycopy(mAesKey, 0, bytes, paddingSize, mAesKey.length);
        System.arraycopy(tokenLenBytes, 0, bytes, paddingSize + mAesKey.length, tokenLenBytes.length);
        System.arraycopy(tokenBytes, 0, bytes, paddingSize + mAesKey.length + tokenLenBytes.length, tokenBytes.length);

        PublicKey publicKey = EncryptUtil.getPublicKey(PUBLIC_KEY);
        //添加平台标识
        addPlatform(bytes, paddingSize);

        //添加协议版本号
        addVersionCode(bytes, paddingSize);

        bytes = EncryptUtil.rsaEncryptData(bytes, publicKey);

        int len = bytes.length;
        int code = TLV_REQ_TRANSFER_AES_KEY;
        byte[] codeBytes = int2Bytes(code);

        len += codeBytes.length + 4;
        byte[] lenBytes = int2Bytes(len);

        ByteBuffer buffer = ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(lenBytes); //发送数据长度
        buffer.put(codeBytes); //发送功能码
        buffer.put(bytes); //发送数据
        buffer.limit();

        Log.debug(TAG, "发送随机AES KEY length=" + mAesKey.length);
        write(buffer);
    }

    private void addPlatform(byte[] bytes, int index) {

        byte[] tmp = short2Bytes((short) 1);
        int start = index - 6;
        for (int i = 0; i < tmp.length; i++) {
            bytes[start + i] = tmp[i];
        }
    }

    private void addVersionCode(byte[] bytes, int index) {
        int versionCode = Utils.getVersionCode(LiveApplication.getInstance());
        byte[] versionBytes = int2Bytes(versionCode);

        int start = index - 4;
        for (int i = start; i < index; i++) {
            bytes[i] = versionBytes[i - start];
        }
    }

    private void padding(byte[] bytes, int size) {
        for (int i = 0; i < size; i++) {
            bytes[i] = 0;
        }
    }

    private void appendString(ByteBuffer byteBuffer, String string) {
        byte[] stringBytes = string.getBytes(Charset.forName("UTF-8"));
        byteBuffer.putInt(stringBytes.length);
        byteBuffer.put(stringBytes);
    }

    private int getParamsLength(Object... params) {
        int bytesCount = 128;
        if (params != null) {
            for (Object param : params) {
                if (param instanceof String) {
                    byte[] stringBytes = ((String) param).getBytes(Charset.forName("UTF-8"));
                    bytesCount += stringBytes.length;
                } else if (param instanceof Integer) {
                    bytesCount += 4;
                } else if (param instanceof Byte) {
                    bytesCount++;
                } else if (param instanceof Short) {
                    bytesCount += 2;
                }

            }
        }
        return bytesCount;
    }

    private byte[] packCommand(boolean isEncrypt, Object... params) {
        if (params == null || params.length <= 0) {
            return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(getParamsLength(params)).order(ByteOrder.LITTLE_ENDIAN);

        if (null != params) {
            for (Object param : params) {
                if (param instanceof String) {
                    appendString(byteBuffer, (String) param);
                } else if (param instanceof Integer) {
                    byteBuffer.putInt((int) param);
                } else if (param instanceof Byte) {
                    byteBuffer.put((byte) param);
                } else if (param instanceof Short) {
                    byteBuffer.putShort((short) param);
                } else if (param instanceof Long) {
                    byteBuffer.putLong((Long) param);
                }
            }
        }

        byte[] result;
        if (isEncrypt) {
            result = EncryptUtil.aesEncrypt(byteBuffer.array(), mAesKey);
            ;
        } else {
            result = byteBuffer.array();
        }

        return result;
    }

    private int readInt(BufferedInputStream bIn) throws IOException {

        byte bytes[] = new byte[4];

        readBytes(bIn, bytes);

        ByteBuffer buffer = Utils.allocate(4);
        buffer.put(bytes);

        buffer.flip();
        int result = buffer.getInt();
        Log.error(TAG, "read int = " + result);

        return result;
    }

    private void readBytes(BufferedInputStream bIn, byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return;
        }
        int len = data.length;
        int hasRead = 0;
        int result;
        while (hasRead < len) {
            result = bIn.read(data, hasRead, len - hasRead);
            if (result == -1) {
                throw new IOException();
            }
            hasRead += result;
        }
//        try {
//
//        }catch (Exception e){
//            Log.d(TAG," readBytes Exception");
//        }

    }

    private static byte[] short2Bytes(short s) {
        byte bytes[] = new byte[2];
        bytes[1] = (byte) (s >> 8);
        bytes[0] = (byte) (s >> 0);
        return bytes;
    }

    private static byte[] int2Bytes(int i) {
        byte bytes[] = new byte[4];
        bytes[3] = (byte) (i >> 24);
        bytes[2] = (byte) (i >> 16);
        bytes[1] = (byte) (i >> 8);
        bytes[0] = (byte) (i >> 0);
        return bytes;
    }

    private static int bytes2Int(byte[] bytes) {
        return ((((bytes[3] & 0xff) << 24)
                | ((bytes[2] & 0xff) << 16)
                | ((bytes[1] & 0xff) << 8) | ((bytes[0] & 0xff) << 0)));
    }

    public static byte[] randomAESKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(EncryptUtil.AES);
            kgen.init(128);
            SecretKey secretKey = kgen.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            Log.error(TAG, "ex ", e);
        }
        return null;
    }

    /**
     * 检测是否连接正常且在房间中
     */
    private boolean checkInRoom() {
        if (mAesKey != null /*&& isInRoom()*/) {
            return true;
        }
        startRoom();
        OnResultListener[] listeners = (OnResultListener[]) mResultListeners.toArray(new OnResultListener[0]);
        for (OnResultListener mResultListener : listeners) {
            if (null != mResultListener)
                mResultListener.chatErrorOccur(CHAT_ERROR_SERVER_DISCONNECT, "连接中，请稍候再试");
        }
//        for (OnResultListener mResultListener : mResultListeners) {
//            if (null != mResultListener) {
//                mResultListener.chatErrorOccur(CHAT_ERROR_SERVER_DISCONNECT, "连接中，请稍候再试");
//            }
//        }
        Log.d(TAG, "checkInRoom false 不发送数据");
        return false;
    }

    public void setOnRequestResultCallback(onRequestResultCallback callback) {
        this.mCallback = callback;
    }

    public void setOnRequestAdminResultCallback(onRequestAdminResultCallback adminResultCallback) {
        this.mAdminResultCallback = adminResultCallback;
    }

    public interface onRequestResultCallback {
        void onFailed(int errorCode);

        void onSuccess(int requestType, Object object);
    }

    public interface onRequestAdminResultCallback {
        void onFailed(int errorCode);

        void onSuccess(int requestType, Object object);
    }
}