package com.laka.live.manager;

import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.util.Log;

import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 二进制读取帮助类
 * <p>
 * Created by heller on 16/4/8.
 */
public class BytesReader {

    private final static String TAG = "BytesReader";

    public BytesReader() {

    }

    public GiftMessage getGiftMessage() {
        return new GiftMessage();
    }


    /**
     * 主播类
     */
    public class Anchor {
        // 主播ID
        public String id = null;
        // 主播昵称
        public String nickName = null;
        // 主播头像URL
        public String avatar = null;
        // 主播认证[0:无认证1:普通认证2:明星认证]
        public short auth;
        // 主播等级
        public short level;
        // 主播收到的总金币数
        public long coin = 0;
        // 当前房间总人数
        public int audienceCount = 0;
    }

    /**
     * 用户类
     */
    public class Audience implements Serializable {
        // 用户ID
        public String id = null;
        // 用户昵称
        public String nickName;
        // 用户头像URL
        public String avatar = null;
        // 用户认证
        public char auth;
        // 用户等级
        public short level;
        // 是否已关注
        public short isFollow;
        // 性别
        public String gender;
        // channel id
        public String channelId;
        // stream id
        public String streamId;
        //视频是否打开
        public byte isVideoOpen;

        @Override
        public String toString() {
            return "Audience{" +
                    "auth=" + (int) auth +
                    ", id='" + id + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", isFollow=" + (int) isFollow +
                    ", level=" + level +
                    ", nickName='" + nickName + '\'' +
                    ", gender='" + gender + '\'' +
                    '}';
        }
    }

    /**
     * 用户进入房间消息类
     */
    public class EnterRoomMessage {

        /**
         * 消息列表
         */
        public static final byte PLACE_MSG_LIST = 0;

        /**
         * 弹幕
         */
        public static final byte PLACE_BULLET_SCREEN = 1;

        /**
         * 消息展示的位置
         */
        public byte showPlace;

        // 用户ID
        public String audienceID = null;
        // 用户昵称
        public String nickName = null;
        // 用户等级
        public int level = 0;

        //消息时间
        public int timestamp;
    }

    /**
     * 用户消息类
     */
    public class AudienceMessage {
        // 用户ID
        public String audienceID = null;
        // 用户昵称
        public String nickName = null;
        // 用户等级
        public int level = 1;
        // 消息类型
        public byte type = 0;
        // 消息正文
        public String content = null;
        // 消息时间
        public int time = 0;
        // 消息分类
        public String msgType = "";
        //接收者是否已关注发送者
        public byte follow;
        //性别
        public String gender;
        //图标
        public String avatar;

    }

    /**
     * 礼物消息类
     */
    public static class GiftMessage {
        //标记直播间礼物还是私信礼物
        public int type;
        // 用户ID
        public String audienceID = null;
        // 用户昵称
        public String nickName = null;
        // 用户头像URL
        public String avatar = null;
        // 用户认证
        public short auth;
        // 用户等级
        public short level;
        // 礼物ID
        public String giftID = null;
        //接收者是否已关注发送者
        public byte follow;
        //性别
        public String gender;
        // 连松数
        public int count = 0;
        // 主播收到的总金币数
        public long coin = 0;
        // 时间
        public int time = 0;

        @Override
        public String toString() {
            return "GiftMessage{" +
                    "audienceID='" + audienceID + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", auth=" + auth +
                    ", level=" + level +
                    ", giftID='" + giftID + '\'' +
                    ", count=" + count +
                    ", coin=" + coin +
                    '}';
        }
    }

    /**
     * 弹幕消息类
     */
    public class BulletMessage {
        // 用户ID
        public String audienceID = null;
        // 用户昵称
        public String nickName = null;
        // 用户头像URL
        public String avatar = null;
        // 用户认证
        public short auth;
        // 用户等级
        public short level;
        // 消息正文
        public String content = null;

        public int time;
    }

    private ByteBuffer mBuffer = null;

    public BytesReader(byte[] bytes) {
        mBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }

    public Audience getAudienceObj() {
        return new Audience();
    }

    public GiftMessage getGiftObject() {
        return new GiftMessage();
    }

    public AudienceMessage getAudienceMessageObj() {
        return new AudienceMessage();
    }

    /**
     * 读取8个字节长度的整型
     * 读取异常会返回-1
     *
     * @return 整型数据
     */
    public long readLong() {
        if (isOutOfLimit(4) == false) {
            return mBuffer.getLong();
        }
        return -1;
    }

    /**
     * 读取4个字节长度的整型
     * 读取异常会返回-1
     *
     * @return 整型数据
     */
    public int readInt() {
        if (isOutOfLimit(4) == false) {
            return mBuffer.getInt();
        }
        return -1;
    }

    /**
     * 读取2个字节长度的短整型
     * 读取异常会返回-1
     *
     * @return 短整型数据
     */
    public short readShort() {
        if (isOutOfLimit(2) == false) {
            return mBuffer.getShort();
        } else {
            return -1;
        }
    }

    /**
     * 读取2个字节长度的短整型
     * 读取异常会返回-1
     *
     * @return 短整型数据
     */
    public char readChar() {
        if (isOutOfLimit(2) == false) {
            return mBuffer.getChar();
        }
        return ' ';
    }

    private boolean isOutOfLimit(int size) {
        int position = mBuffer.position();
        int limit = mBuffer.limit();
        return position > limit - size;
    }

    /**
     * 读取1个字节长度的字节
     * 读取异常会返回-1
     *
     * @return 字节数据
     */
    public byte readByte() {
        if (isOutOfLimit(1) == false) {
            return mBuffer.get();
        }
        return -1;
    }

    /**
     * 读取字符串,偏移长度是:sizeof(字符长度(4个直接的32位整型) + 字符)
     * 读取异常会返回null
     *
     * @return 字符串
     */
    public String readString() {
        int length = readInt();
        if (length <= 0) return null;

        if (isOutOfLimit(length)) {
            return null;
        }

        try {
            byte[] bytes = new byte[length];
            mBuffer.get(bytes);
            return new String(bytes);
        } catch (OutOfMemoryError e) {

        }

        return null;
    }

    /**
     * 读取主播对象,字段参考Anchor定义,读取异常会返回nil
     *
     * @return 实例对象
     */
    public Anchor readAnchor() {
        try {
            Anchor object = new Anchor();
            object.id = readString();
            object.nickName = readString();
            object.avatar = readString();
            object.auth = readShort();
            object.level = readShort();
            object.coin = readLong();
            object.audienceCount = readInt();

            return object;
        } catch (BufferUnderflowException e) {
            Log.error(TAG, e.getLocalizedMessage());
        } catch (IndexOutOfBoundsException e) {
            Log.error(TAG, e.getLocalizedMessage());
        }

        return null;
    }

    /**
     * 读取用户进入消息对象,字段参考EnterRoomMessage定义,读取异常会返回nil
     *
     * @return 实例对象
     */
    public EnterRoomMessage readEnterRoomMessage() {
        try {
            EnterRoomMessage object = new EnterRoomMessage();
            object.showPlace = readByte();
            object.audienceID = readString();
            object.nickName = readString();
            object.level = readInt();
            object.timestamp = readInt();
            return object;
        } catch (BufferUnderflowException e) {
            Log.error(TAG, e.getLocalizedMessage());
        } catch (IndexOutOfBoundsException e) {
            Log.error(TAG, e.getLocalizedMessage());
        }

        return null;
    }

    /**
     * 读取用户对象,字段参考Audience定义,读取异常会返回nil
     *
     * @return 实例对象
     */
    public Audience readLinkUser() {
        try {
            Audience object = new Audience();
            object.id = readString();
            object.channelId = readString();
            object.streamId = readString();
            object.avatar = readString();
            object.nickName = readString();
            object.auth = readChar();
            object.level = readShort();
            object.isVideoOpen = readByte();
            return object;
        } catch (BufferUnderflowException e) {
            Log.error(TAG, e.getLocalizedMessage() + "");
        } catch (IndexOutOfBoundsException e) {
            Log.error(TAG, e.getLocalizedMessage() + "");
        }

        return null;
    }

    /**
     * 读取用户对象,字段参考Audience定义,读取异常会返回nil
     *
     * @return 实例对象
     */
    public Audience readAudience() {
        try {
            Audience object = new Audience();
            object.id = readString();
            object.avatar = readString();
//            object.auth = readShort();
            object.auth = readChar();
            object.level = readShort();
//            object.isFollow = readChar();
            return object;
        } catch (BufferUnderflowException e) {
            Log.error(TAG, e.getLocalizedMessage() + "");
        } catch (IndexOutOfBoundsException e) {
            Log.error(TAG, e.getLocalizedMessage() + "");
        }

        return null;
    }

    public Audience readAudienceFollow() {
        try {
            Audience object = new Audience();
            object.id = readString();
            object.avatar = readString();
//            object.auth = readShort();
            object.auth = readChar();
            object.level = readShort();
            object.isFollow = readShort();
            return object;
        } catch (BufferUnderflowException e) {
            Log.error(TAG, e.getLocalizedMessage() + "");
        } catch (IndexOutOfBoundsException e) {
            Log.error(TAG, e.getLocalizedMessage() + "");
        }

        return null;
    }

    /**
     * 读取用户消息对象,字段参考AudienceMessage定义,读取异常会返回nil
     *
     * @return 实例对象
     */
    public AudienceMessage readAudienceMessage() {
        try {
            AudienceMessage object = new AudienceMessage();
            object.audienceID = readString();
            object.nickName = readString();
            object.level = readInt();
            object.type = readByte();
            object.content = readString();
            object.time = readInt();
            return object;
        } catch (BufferUnderflowException e) {
            Log.error(TAG, e.getLocalizedMessage());
        } catch (IndexOutOfBoundsException e) {
            Log.error(TAG, e.getLocalizedMessage());
        }

        return null;
    }

    /**
     * 读取礼物消息对象,字段参考GiftMessage定义,读取异常会返回nil
     *
     * @return 实例对象
     */
    public GiftMessage readGiftMessage() {
        try {
            GiftMessage object = new GiftMessage();
            object.audienceID = readString();
            object.nickName = readString();
            object.avatar = readString();
            object.auth = readShort();
            object.level = readShort();
            object.giftID = readString();
            object.count = readInt();
            object.coin = readLong();//int64
            object.time = readInt();
            return object;
        } catch (BufferUnderflowException e) {
            Log.error(TAG, e.getLocalizedMessage());
        } catch (IndexOutOfBoundsException e) {
            Log.error(TAG, e.getLocalizedMessage());
        }

        return null;
    }

    /**
     * 读取弹幕消息对象,字段参考BulletMessage定义,读取异常会返回nil
     *
     * @return 实例对象
     */
    public BulletMessage readBulletMessage() {
        try {
            BulletMessage object = new BulletMessage();
            object.audienceID = readString();
            object.nickName = readString();
            object.avatar = readString();
            object.auth = readShort();
            object.level = readShort();
            object.content = readString();
            object.time = readInt();

            return object;
        } catch (BufferUnderflowException e) {
            Log.error(TAG, e.getLocalizedMessage());
        } catch (IndexOutOfBoundsException e) {
            Log.error(TAG, e.getLocalizedMessage());
        }

        return null;
    }

    public boolean hasRemaining() {
        return mBuffer == null ? false : mBuffer.hasRemaining();
    }

    public byte[] readBytes(int len) {
        if (mBuffer == null) {
            return null;
        }

        if (isOutOfLimit(len)) {
            return null;
        }
        byte[] bytes = new byte[len];
        mBuffer.get(bytes);
        return bytes;
    }

    public ConnectUserInfo readLinkUserInfo() {
        ConnectUserInfo info = new ConnectUserInfo();
        try {
            info.setId(Integer.parseInt(readString()));
        } catch (NumberFormatException e) {

        }
        info.setAvatar(readString());
        info.setNickName(readString());
        try {
            info.setGender(Integer.parseInt(readString()));
        } catch (NumberFormatException e) {

        }
        info.setDescription(readString());
        info.setAuth(readShort());
        info.setLevel(readShort());
        info.setState(readShort());


        return info;
    }

    public ConnectUserInfo readOnlineUserInfo() {
        ConnectUserInfo info = new ConnectUserInfo();
        try {
            info.setId(Integer.parseInt(readString()));
        } catch (NumberFormatException e) {

        }
        info.setAvatar(readString());
        info.setNickName(readString());
        try {
            info.setGender(Integer.parseInt(readString()));
        } catch (NumberFormatException e) {

        }
        info.setUserSig(readString());
        info.setAuth(readShort());
        info.setLevel(readShort());
        info.setManager(readByte());
        info.setInConnectMicList(readByte());


        return info;
    }
}
