package com.laka.live.manager;

import java.util.ArrayList;
import java.util.List;

import laka.live.bean.ChatMsg;

/**
 * Created by crazyguan on 2016/4/15.
 */
public abstract class OnResultListenerAdapter implements RoomManager.OnResultListener{

    /**
     * 直播间Socket握手成功
     */
    @Override
    public void chatDidConnect(){

    }

    /**
     * Socket断开链接
     */
    @Override
    public void chatDidDisconnect(){

    }

    /**
     * 打开房间成功回调
     */
    @Override
    public void chatDidOpenRoom(){

    }

    /**
     * 进入房间成功回调
     *
     * @param anchor 主播信息
     */
    @Override
    public void chatDidEnterRoom(BytesReader.Anchor anchor){

    }

    /**
     * 离开直播间成功
     */
    @Override
    public void chatDidExitRoom(){

    }

    /**
     * 关闭房间成功回调
     */
    @Override
    public void chatDidCloseRoom(String roomId){

    }

    /**
     * 有用户进入房间消息回调
     *
     * @param message 消息对象
     */
    @Override
    public void chatUserEnterRoom(BytesReader.EnterRoomMessage message){

    }

    /**
     * 有用户离开房间
     *
     * @param userid 用户ID
     */
    @Override
    public void chatUserExitRoom(String userid){

    }

    /**
     * 接收到房间消息
     *
     * @param message 消息实例对象,参考AudienceMessage定义,可能为null
     */
    @Override
    public void chatDidReceiveMessage(BytesReader.AudienceMessage message){

    }

    /**
     * 接收到点亮消息
     *
     * @param userID 来自用户ID
     * @param count  点赞数
     */
    @Override
    public void chatReceiveLike(String userID, int count){

    }

    /**
     * 接收到礼物消息
     *
     * @param message 礼物消息实例对象,参考GiftMessage定义
     */
    @Override
    public void chatReceiveGift(BytesReader.GiftMessage message){

    }

    /**
     * 接收到弹幕消息
     *
     * @param message 弹幕消息实例对象,参考BulletMessage定义
     */
    @Override
    public void chatReceiveBullet(BytesReader.BulletMessage message){

    }

    /**
     * 查询房间用户信息成功回调
     *
     * @param number    返回用户数
     * @param audiences 用户对象数组,包含的是Audience实例对象,定义参考Audience说明
     */
    @Override
    public void chatDidQueryRoomUser(int number, ArrayList<BytesReader.Audience> audiences){

    }

    /**
     * 查询房间信息成功回调
     *
     * @param coin          主播收到的总钻石数
     * @param audienceCount 当前房间用户数
     */
    @Override
    public void chatDidQueryRoomData(long coin, int audienceCount){

    }

    /**
     * 查询用户信息成功回调
     *
     * @param audience          用户信息对象
     */
    @Override
    public void chatDidQueryUserInfo(BytesReader.Audience audience){

    }

    /**
     * 各种异常
     *
     * @param errcode 错误码
     * @param errMsg  错误描述
     */
    @Override
    public void chatErrorOccur(int errcode, String errMsg){

    }

    @Override
    public void chatDidSuccess(int code ){

    }
    /**
     * 接收到点亮消息
     *
     * @param userID 来自用户ID
     */
    @Override
    public void chatReceiveLight(String userID,String nickName,int level){

    }
    /**
     * 离线消息回调
     */
    @Override
    public void chatOffline(List<ChatMsg> messages){

    };
}
