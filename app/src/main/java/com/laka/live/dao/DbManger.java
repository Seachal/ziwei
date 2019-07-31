package com.laka.live.dao;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import laka.live.bean.ChatMsg;
import laka.live.bean.ChatSession;
import laka.live.dao.ChatMsgDao;
import laka.live.dao.ChatSessionDao;
import laka.live.dao.DaoMaster;
import laka.live.dao.DaoSession;

/**
 * Created by crazyguan on 2016/4/14.
 */
public class DbManger {
    private static final String TAG = "DbManger";
    private Database db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static DbManger manger;

    //会话相关
    public static final int SESSION_TYPE_UNFOLLOW = 0, SESSION_TYPE_FOLLOW = 1, SESSION_TYPE_MISHU = 2, SESSION_TYPE_STRANGER = 3, SESSION_TYPE_GUANFANG = 4;
    public static final String SESSION_ID_LAKA_MISHU = "mishu"; //滋味Live小秘书会话ID;使用需要在前面加userId
    public static final String SESSION_ID_LAKA_GUANFANG = "guanfang"; //滋味Live官方会话ID;使用需要在前面加userId

    public static final int TYPE_CHAT_MISHU_ATTENTION = 0;
    public static final int TYPE_CHAT_MISHU_SYSTEM = 1;

    public static DbManger getInstance() {
        if (manger == null) {
            manger = new DbManger(LiveApplication.getInstance());
        }
        return manger;
    }

    private DbManger(LiveApplication instance) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        MyOpenHelper helper = new MyOpenHelper(instance, "laka-db", null);
        db = helper.getWritableDb();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private ChatSessionDao getChatSessionDao() {
        return daoSession.getChatSessionDao();
    }

    private ChatMsgDao getChatMsgDao() {
        return daoSession.getChatMsgDao();
    }


    /**
     * 查询当前用户会话列表,是否已关注
     */
    public List<ChatSession> getSessions(int type) {
        String myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        String sessionId = myUserId + "%";
        ChatSessionDao sessionDao = getChatSessionDao();
        QueryBuilder<ChatSession> qb = sessionDao.queryBuilder();
        List<ChatSession> sessions;
        if (type == SESSION_TYPE_FOLLOW) {
            sessions = qb.where(qb.and(ChatSessionDao.Properties.Id.like(sessionId), ChatSessionDao.Properties.Type.gt(SESSION_TYPE_UNFOLLOW))).orderDesc(ChatSessionDao.Properties.Date).list();
            Log.d(TAG, "SESSION_TYPE_FOLLOW getSessions() size=" + sessions.size());
        } else {
            sessions = qb.where(qb.and(ChatSessionDao.Properties.Id.like(sessionId), ChatSessionDao.Properties.Type.eq(type))).orderDesc(ChatSessionDao.Properties.Date).list();
            Log.d(TAG, "SESSION_TYPE_UNFOLLOW getSessions() size=" + sessions.size());
        }
        return new ArrayList<>(sessions);
    }

    /**
     * 根据会话Id查询聊天记录
     */
    public void getChatMsgBySessionId(final String sessionId, final IDataListener iDataListener) {
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                updateSession(sessionId);
                ChatMsgDao chatMsgDao = getChatMsgDao();
                List<ChatMsg> chatMsgs = chatMsgDao.queryBuilder().where(ChatMsgDao.Properties.SessionId.eq(sessionId)).orderAsc(ChatMsgDao.Properties.Date).list();
                Log.d(TAG, "getChatMsgBySessionId() size=" + chatMsgs.size() + " sessionId=" + sessionId);
                iDataListener.onSuccess(new ArrayList<>(chatMsgs), 0, "");
            }
        });
    }

    /**
     * 根据会话Id查询聊天记录，跳过多少条
     */
    public void getChatMsgBySessionId(final String sessionId, final int offset, final IDataListener iDataListener) {
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                updateSession(sessionId);
                ChatMsgDao chatMsgDao = getChatMsgDao();
                List<ChatMsg> chatMsgs = chatMsgDao.queryBuilder().where(ChatMsgDao.Properties.SessionId.eq(sessionId)).orderDesc(ChatMsgDao.Properties.Date).offset(offset).limit(20).list();
                Log.d(TAG, "getChatMsgBySessionId() size=" + chatMsgs.size() + " sessionId=" + sessionId + " offset=" + offset);
                iDataListener.onSuccess(new ArrayList<>(chatMsgs), 0, "");
            }
        });
    }

    /**
     * 根据类型查询秘书聊天记录，跳过多少条
     */
    public void getMishuByType(final String sessionId, final int type, final IDataListener iDataListener) {
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                updateSession(sessionId);
                ChatMsgDao chatMsgDao = getChatMsgDao();
                List<ChatMsg> chatMsgs = chatMsgDao.queryBuilder().where(
                        chatMsgDao.queryBuilder()
                                .and(ChatMsgDao.Properties.SessionId.eq(sessionId), ChatMsgDao.Properties.Type.eq(type)))
                        .orderDesc(ChatMsgDao.Properties.Date)
                        .list();

                Log.d(TAG, "getMishuByType() size=" + chatMsgs.size() + " sessionId=" + sessionId );
                iDataListener.onSuccess(new ArrayList<>(chatMsgs), 0, "");
            }
        });
    }

    /**
     * 查询最新的一条秘书消息
     */
    public ChatMsg getLastMishuSystem(final String sessionId) {
        updateSession(sessionId);
        ChatMsgDao chatMsgDao = getChatMsgDao();
        ChatMsg chatMsg = chatMsgDao.queryBuilder().where(
                chatMsgDao.queryBuilder()
                        .and(ChatMsgDao.Properties.SessionId.eq(sessionId), ChatMsgDao.Properties.Type.eq(DbManger.TYPE_CHAT_MISHU_SYSTEM)))
                .orderDesc(ChatMsgDao.Properties.Date)
                .offset(0)
                .limit(1)
                .unique();

        Log.d(TAG, "getLastMishuSystem() chatMsg=" + chatMsg);
        return chatMsg;
    }

    /**
     * 批量插入私信
     */
    public void addChatMsgs(final List<ChatMsg> chatMsgs) {
        if (Utils.listIsNullOrEmpty(chatMsgs)) {
            return;
        }
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < chatMsgs.size(); i++) {
                    ChatMsg chatMsg = chatMsgs.get(i);
                    addChatMsg(chatMsg);
                }
                Log.d(TAG, "批量插入私信 成功 =" + chatMsgs.size());
            }
        });
    }

    //初始化滋味Live小秘书和滋味Live官方
    public void initLakaSession(String myUserId) {
        ChatSessionDao sessionDao = getChatSessionDao();
        String mishuSessionId = myUserId + SESSION_ID_LAKA_MISHU;
        String guanfangSessionId = myUserId + SESSION_ID_LAKA_GUANFANG;
        List<ChatSession> sessions = sessionDao.queryBuilder().where(ChatSessionDao.Properties.Id.eq(mishuSessionId)).list();
        if (Utils.listIsNullOrEmpty(sessions)) {
            Log.d(TAG, "没有滋味Live小秘书和滋味Live官方 创始化");
            List<ChatSession> initSessions = new ArrayList<>();
            ChatSession mishuSession = new ChatSession();
            mishuSession.setId(mishuSessionId);
            mishuSession.setUserId(SESSION_ID_LAKA_MISHU);
            mishuSession.setNickName(ResourceHelper.getString(R.string.laka_mishu));
            mishuSession.setDate(System.currentTimeMillis());
            mishuSession.setType(SESSION_TYPE_MISHU);
            mishuSession.setContent("我是你的小秘书");
            initSessions.add(mishuSession);
            sessionDao.insert(mishuSession);
            ChatSession guanfangSession = new ChatSession();
            guanfangSession.setId(guanfangSessionId);
            guanfangSession.setUserId(SESSION_ID_LAKA_GUANFANG);
            guanfangSession.setNickName(ResourceHelper.getString(R.string.laka_guanfang));
            guanfangSession.setDate(System.currentTimeMillis());
            guanfangSession.setType(SESSION_TYPE_GUANFANG);
            guanfangSession.setContent("欢迎来到滋味Live的世界");
            initSessions.add(guanfangSession);
            sessionDao.insert(guanfangSession);
        } else {
            Log.d(TAG, "有滋味Live小秘书和滋味Live官方 ");
        }

    }

    public void addChatMsg(final ChatMsg chatMsg) {
        addChatMsg(chatMsg, 0, 0, 0, false);
    }

    /**
     * 插入私信
     */
    public void addChatMsg(final ChatMsg chatMsg, final int follow, final int level, final int gender, final boolean isUpdateUserInfo) {
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                //找是否存在会话 sessionId = "我的ID"+"对方ID"
                ChatSessionDao sessionDao = getChatSessionDao();
                String myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
                String sessionId = myUserId + chatMsg.getUserId();
                List<ChatSession> sessions = sessionDao.queryBuilder().where(ChatSessionDao.Properties.Id.eq(sessionId)).list();

                ChatSession session;
                if (!Utils.listIsNullOrEmpty(sessions)) {
                    //存在更新会话
                    session = sessions.get(0);
                    if (!Utils.isEmpty(chatMsg.getAvatar()))
                        session.setAvatar(chatMsg.getAvatar());
                    session.setUserId(chatMsg.getUserId());
                    session.setContent(chatMsg.getContent());
                    session.setDate(chatMsg.getDate());

                    //区分类型
                    if (SESSION_ID_LAKA_GUANFANG.equals(chatMsg.getUserId())) {
//                        session.setType(DbManger.SESSION_TYPE_GUANFANG);
//                        chatMsg.setNickName(ResourceHelper.getString(R.string.laka_guanfang));
                        session.setNickName(ResourceHelper.getString(R.string.laka_guanfang));
                    } else if (SESSION_ID_LAKA_MISHU.equals(chatMsg.getUserId())) {
                        Log.d(TAG, " 更新xiaomishu session=" + chatMsg.getNickName());
//                        session.setType(DbManger.SESSION_TYPE_MISHU);
//                        chatMsg.setNickName(ResourceHelper.getString(R.string.laka_mishu));
                        session.setNickName(ResourceHelper.getString(R.string.laka_mishu));
                    } else {
                        if (!Utils.isEmpty(chatMsg.getNickName()))
                            session.setNickName(chatMsg.getNickName());

                        //更新更多数据
                        if (isUpdateUserInfo) {
                            session.setLevel(level);
                            session.setGender(gender);
                            session.setType(follow);
                        }
                    }
                    if (chatMsg.getIsUnread() != null && chatMsg.getIsUnread() == true) {
                        session.setUnreadCnt(session.getUnreadCnt() + 1);//未读数+1
                        Log.d(TAG,"未读数+1");
                    } else {
                        session.setUnreadCnt(0);
                        Log.d(TAG,"未读数设为0");
                    }

                    sessionDao.update(session);
                    Log.d(TAG, "存在更新会话 id=" + session.getId() + " content=" + session.getContent());
                } else {
                    //不存在创建会话
                    session = new ChatSession();
                    session.setId(sessionId);
                    session.setUserId(chatMsg.getUserId());

                    //更新更多数据
                    if (isUpdateUserInfo) {
                        session.setLevel(level);
                        session.setGender(gender);
                        session.setType(follow);
                    } else {
                        session.setType(SESSION_TYPE_UNFOLLOW);
                    }

                    if (!Utils.isEmpty(chatMsg.getAvatar()))
                        session.setAvatar(chatMsg.getAvatar());

                    session.setContent(chatMsg.getContent());
                    session.setDate(chatMsg.getDate());

                    if (session.getLevel() == null) {
                        session.setLevel(0);
                    }
                    if (session.getDate() == null) {
                        session.setDate(Utils.getCurrentTimeSecond());
                    }

                    //区分类型
                    if (SESSION_ID_LAKA_GUANFANG.equals(chatMsg.getUserId())) {
                        session.setType(DbManger.SESSION_TYPE_GUANFANG);
                        session.setNickName(ResourceHelper.getString(R.string.laka_guanfang));
                    } else if (SESSION_ID_LAKA_MISHU.equals(chatMsg.getUserId())) {
                        session.setType(DbManger.SESSION_TYPE_MISHU);
                        session.setNickName(ResourceHelper.getString(R.string.laka_mishu));
                    } else {
                        if (Utils.isEmpty(chatMsg.getNickName())) {
                            getUserInfo(chatMsg.getUserId());
                        }
                        if (!Utils.isEmpty(chatMsg.getNickName()))
                            session.setNickName(chatMsg.getNickName());
                    }
                    if (chatMsg.getIsUnread() != null && chatMsg.getIsUnread() == true) {
                        session.setUnreadCnt(1);//未读数+1
                    } else {
                        session.setUnreadCnt(0);
                    }
                    sessionDao.insert(session);
                    Log.d(TAG, "创建会话成功 id=" + session.getId());
                }

                //插入消息
                ChatMsgDao chatMsgDao = getChatMsgDao();
                chatMsg.setSessionId(sessionId);
                if (chatMsg.getType() == null) {
                    chatMsg.setType(0);
                }
                if (chatMsg.getLevel() == null) {
                    chatMsg.setLevel(0);
                }
                if (chatMsg.getIsSend() == null) {
                    chatMsg.setIsSend(false);
                }
                if (chatMsg.getIsUnread() == null) {
                    chatMsg.setIsUnread(true);
                }
                //捕获未知异常（可以不加）
                try {
                    if (SESSION_ID_LAKA_GUANFANG.equals(chatMsg.getUserId())) {
                        session.setType(DbManger.SESSION_TYPE_GUANFANG);
                        chatMsgDao.insertOrReplace(chatMsg);
                    } else if (SESSION_ID_LAKA_MISHU.equals(chatMsg.getUserId())) {
                        session.setType(DbManger.SESSION_TYPE_MISHU);
                        chatMsgDao.insertOrReplace(chatMsg);
                    } else {
                        chatMsgDao.insertOrReplace(chatMsg);
                        if (chatMsg.getIsSend()) {
                            EventBusManager.postEvent(chatMsg, SubcriberTag.ADD_CHAT_MSG_SUCCESS);
                        }
                    }

                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw, true));
                    Log.d(TAG, "add msg报错:"
                            + sw.toString());
                }
                if (chatMsg.getType() == 0) {
                    Log.d(TAG, "插入私信文字成功 id=" + chatMsg.getId() + " myUserId=" + myUserId);
                } else {
                    Log.d(TAG, "插入私信礼物成功 id=" + chatMsg.getId() + " myUserId=" + myUserId);
                }
            }
        });
    }

    /**
     * 更新聊天消息
     *
     * @param chatMsg
     */
    public void updateMessage(ChatMsg chatMsg) {
        ChatMsgDao msgDao = getChatMsgDao();
        msgDao.update(chatMsg);
    }

    /**
     * 更新会话
     */
    public void updateSession(ChatSession session) {
        ChatSessionDao sessionDao = getChatSessionDao();
        sessionDao.update(session);

    }

    /**
     * 更新会话
     */
    public void updateSession(String myUserId, String otherUserId, String nickName, String avatar, short level, short auth, int type, int gender) {
        Log.d(TAG, "updateSession sessionId=" + myUserId + otherUserId + " nickName=" + nickName + " avatar=" + avatar);
        ChatSessionDao sessionDao = getChatSessionDao();
        List<ChatSession> sessions = sessionDao.queryBuilder().where(ChatSessionDao.Properties.Id.eq(myUserId + otherUserId)).list();
        ChatSession session;

        boolean isNeedRefresh = false;

        if (!Utils.listIsNullOrEmpty(sessions)) {
            //存在更新会话
            session = sessions.get(0);
            session.setAvatar(avatar);
            session.setNickName(nickName);
            session.setLevel((int) level);
            session.setAuth(auth);
            session.setType(type);
            if (gender >= 0)
                session.setGender(gender);
            sessionDao.update(session);

            isNeedRefresh = true;
            Log.d(TAG, "存在更新会话");
        } else {
            Log.d(TAG, "没有会话不需要存在更新");
        }

        //同时更新小秘书昵称
        String sessionId = myUserId + SESSION_ID_LAKA_MISHU;
        List<ChatSession> xiaomishuSessions = sessionDao.queryBuilder().where(ChatSessionDao.Properties.Id.eq(sessionId)).list();
        if (!Utils.listIsNullOrEmpty(xiaomishuSessions)) {
            ChatSession sessionXiaomishu = xiaomishuSessions.get(0);
            Log.d(TAG, " sessionXiaomishu otherUserId=" + otherUserId
                    + " sessionXiaomishu.getNickName()=" + sessionXiaomishu.getNickName());

            String content = sessionXiaomishu.getContent();
            isNeedRefresh = true;

            ChatMsgDao chatMsgDao = getChatMsgDao();
            QueryBuilder<ChatMsg> qb = chatMsgDao.queryBuilder();
            List<ChatMsg> chatMsgs = qb.where(qb.and(ChatMsgDao.Properties.SessionId.eq(sessionId), ChatMsgDao.Properties.NickName.eq(otherUserId))).list();
            Log.d(TAG, " 刷新小秘书昵称 chatMsgs=" + chatMsgs.size());
            if (!Utils.listIsNullOrEmpty(chatMsgs)) {
                for (int i = 0; i < chatMsgs.size(); i++) {
                    ChatMsg msg = chatMsgs.get(i);
                    String msgContent = msg.getContent();
                    if (!Utils.isEmpty(msgContent) && msgContent.contains(ResourceHelper.getString(R.string.follow))) {
                        msgContent = nickName + ResourceHelper.getString(R.string.follow_you);
                        msg.setContent(msgContent);
                    }
                }
                chatMsgDao.updateInTx(chatMsgs);
            }
        }


        if (isNeedRefresh) {
            //通知ui更新
            EventBusManager.postEvent(0, SubcriberTag.REFRESH_CHAT_SESSION);
        }
    }


    /**
     * 获取某个会话
     */
    public ChatSession getSessionBySessionId(String sessionId) {
        ChatSessionDao sessionDao = getChatSessionDao();
        List<ChatSession> sessions = sessionDao.queryBuilder().where(ChatSessionDao.Properties.Id.eq(sessionId)).list();
        if (!Utils.listIsNullOrEmpty(sessions)) {
            return sessions.get(0);
        }else{
            return null;
        }
    }

    /**
     * 更新会话
     */
    public void updateSession(String sessionId) {
        Log.d(TAG, "updateSession sessionId=" + sessionId);
        ChatSessionDao sessionDao = getChatSessionDao();
        List<ChatSession> sessions = sessionDao.queryBuilder().where(ChatSessionDao.Properties.Id.eq(sessionId)).list();
        ChatSession session;
        if (!Utils.listIsNullOrEmpty(sessions)) {
            //存在更新会话
            session = sessions.get(0);
            session.setUnreadCnt(0);
            sessionDao.update(session);
        }

    }

    /**
     * 删除会话
     *
     * @param session
     */
    public void deleteSession(final ChatSession session, final boolean deleteMsg) {
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                ChatSessionDao sessionDao = getChatSessionDao();
                sessionDao.delete(session);

                if (deleteMsg) {
                    deleteMsg(session.getId());
                }
            }
        });

    }

    public void deleteMsg(final String sessionId) {

        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                ChatMsgDao chatMsgDao = getChatMsgDao();

                DeleteQuery<ChatMsg> dq = chatMsgDao.queryBuilder().where(ChatMsgDao.Properties.SessionId.eq(sessionId)).buildDelete();
                dq.executeDeleteWithoutDetachingEntities();
            }
        });

    }

    /**
     * 获取用户资料
     */
    private void getUserInfo(String userId) {
        DataProvider.getUserInfo(this, userId, true, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                Log.d(TAG, " getUserInfo onSuccess");
                if (userMsg.isSuccessFul()) {
                    UserInfo info = userMsg.getUserInfo();
                    if (info != null) {
                        String myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
                        updateSession(myUserId, info.getId() + "", info.getNickName(), info.getAvatar(), (short) info.getLevel(), info.getAuth(), info.getFollow(), info.getGender());
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }

    /**
     * 获取未关注未读会话数
     */
    public long getUnfollowUnreadCnt() {
        String myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        String sessionId = myUserId + "%";
        ChatSessionDao sessionDao = getChatSessionDao();
        QueryBuilder<ChatSession> qb = sessionDao.queryBuilder();
        qb.where(qb.and(ChatSessionDao.Properties.Id.like(sessionId), ChatSessionDao.Properties.Type.eq(0), ChatSessionDao.Properties.UnreadCnt.gt(0)));//session type unreadCnt
        long count = qb.buildCount().count();
        return count;
    }


    /**
     * 获取未读会话数
     */
    public long getTotalUnreadCnt() {
        String myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        String sessionId = myUserId + "%";
        ChatSessionDao sessionDao = getChatSessionDao();
        QueryBuilder<ChatSession> qb = sessionDao.queryBuilder();
        qb.where(qb.and(ChatSessionDao.Properties.Id.like(sessionId), ChatSessionDao.Properties.UnreadCnt.gt(0)));
        long count = qb.buildCount().count();
        return count;
    }



    public ChatSession getLastStrangerSession() {
        String myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        String sessionId = myUserId + "%";

        ChatSessionDao sessionDao = getChatSessionDao();
        QueryBuilder<ChatSession> qb = sessionDao.queryBuilder();
        qb.limit(1);
        qb.orderDesc(ChatSessionDao.Properties.Date);
        List<ChatSession> list = qb.where(qb.and(ChatSessionDao.Properties.Id.like(sessionId), ChatSessionDao.Properties.Type.eq(0))).list();
        Log.d(TAG, " getLastStrangerSession list=" + list.size());
        if (!Utils.listIsNullOrEmpty(list)) {
            ChatSession session = list.get(0);
            ChatSession strangerSession = new ChatSession();
            strangerSession.setNickName(ResourceHelper.getString(R.string.stranger_msg));
            strangerSession.setType(SESSION_TYPE_STRANGER);
            strangerSession.setUserId("");
            strangerSession.setContent(session.getContent());
            strangerSession.setDate(session.getDate());
            return strangerSession;
        } else {
            return null;
        }
    }

    /**
     * 忽略未读
     */
    public void ingoreUnreadCnt(final int type) {
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                String myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
                String sessionId = myUserId + "%";
                ChatSessionDao sessionDao = getChatSessionDao();
                QueryBuilder<ChatSession> qb = sessionDao.queryBuilder();
                List<ChatSession> sessions = qb.where(qb.and(ChatSessionDao.Properties.Id.like(sessionId), ChatSessionDao.Properties.Type.eq(type), ChatSessionDao.Properties.UnreadCnt.gt(0))).list();
                if (!Utils.listIsNullOrEmpty(sessions)) {
                    for (ChatSession item :
                            sessions) {
                        item.setUnreadCnt(0);
                    }
                    sessionDao.updateInTx(sessions);
                }
            }
        });
    }

    /**
     * 忽略所有未读
     */
    public void ingoreAllUnreadCnt() {
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                String myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
                String sessionId = myUserId + "%";
                ChatSessionDao sessionDao = getChatSessionDao();
                QueryBuilder<ChatSession> qb = sessionDao.queryBuilder();
                List<ChatSession> sessions = qb.where(qb.and(ChatSessionDao.Properties.Id.like(sessionId), ChatSessionDao.Properties.UnreadCnt.gt(0))).list();
                if (!Utils.listIsNullOrEmpty(sessions)) {
                    for (ChatSession item :
                            sessions) {
                        item.setUnreadCnt(0);
                    }
                    sessionDao.updateInTx(sessions);
                }
            }
        });
    }


    public void deleteMsg(ChatMsg chatMsg) {
        chatMsg.delete();
    }
}
