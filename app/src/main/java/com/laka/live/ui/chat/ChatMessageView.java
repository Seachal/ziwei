package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.ChatEntity;
import com.laka.live.bean.Course;
import com.laka.live.bean.CourseDetail;
import com.laka.live.bean.GiftInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.config.SystemConfig;
import com.laka.live.dao.DbManger;
import com.laka.live.dao.IDataListener;
import com.laka.live.gift.GiftAudioManager;
import com.laka.live.gift.GiftResManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.help.recycleViewDecoration.RecyclerViewItemDecoration;
import com.laka.live.manager.BytesReader;
import com.laka.live.manager.OnResultListenerAdapter;
import com.laka.live.manager.RoomManager;
import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.LoadMoreFoot;
import com.laka.live.ui.widget.SelectorButton;
import com.laka.live.ui.widget.chatKeyboard.DisplayRules;
import com.laka.live.ui.widget.chatKeyboard.Emojicon;
import com.laka.live.ui.widget.chatKeyboard.KJChatKeyboard;
import com.laka.live.ui.widget.chatKeyboard.OnOperationListener;
import com.laka.live.ui.widget.emoji.EmojiManager;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import laka.live.bean.ChatMsg;

/**
 * Created by ios on 16/6/23.
 */
public class ChatMessageView extends RelativeLayout implements View.OnClickListener, EventBusManager.OnEventBusListener {
    protected static final String TAG = "ChatMessageView";
    protected static final int MODE_FULL_SCREEN = 0, MODE_HALF_SCREEN = 1;
    public int mode = MODE_FULL_SCREEN;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOICE_PHOTO = 2;

    protected Context context;
    private BaseActivity activity;
    private View view;
    ChatMessageRcvAdapter adapter;
    RecyclerView lvChat;

    PtrClassicFrameLayout mPtrLoadmoreWidget;

    List<ChatMsg> datas = new ArrayList<>();
    private KJChatKeyboard box;
    public static String otherUserId;
    private String otherNickName, otherAvatar;//对方内容
    private UserInfo myUserInfo;
    RoomManager roomManger;//房间通讯操作类
    HeadView header;
    HashMap<Integer, ChatMsg> sendingMsgs = new HashMap<>();
    HashMap<Integer, ChatMsg> sendingGifts = new HashMap<>();

    private int transientId = 0;
    private boolean isMsgUpdate = false;
    //商品相关
    private ShoppingGoodsDetailBean goodsBean;
    private RelativeLayout rlGoods;
    private SimpleDraweeView ivGoods;
    private TextView tvName, tvPrice;
    private SelectorButton btnSend;
    //课程相关
    private Course course;
    private CourseDetail courseDetail;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isMsgUpdate = false;
                    long sendTime = ((long) msg.obj) - 10;
                    //Log.d(TAG , "handle -->currentTime :" + sendTime);
                    handleSendMsgState(sendingMsgs, sendTime);
                    handleSendMsgState(sendingGifts, sendTime);
                    //Log.d(TAG , "handle -->isMsgUpdate :" + isMsgUpdate);
                    if (isSendingQueueEmpty()) {
                        cancelSendTimer();
                    }
                    if (isMsgUpdate) {
                        refreshChat();
                    }
                    break;

            }
        }
    };
    public int isFromSession = -1;

    public void setMode(int mode) {
        this.mode = mode;
    }


    /**
     * 初始化控件
     */
    public ChatMessageView(Context context) {
        super(context);
        initUI();
    }

    public ChatMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChatMessageView);
        int mode = a.getInteger(R.styleable.ChatMessageView_mode, MODE_FULL_SCREEN);
        Log.d(TAG, "设置 mode=" + mode);
        this.mode = mode;
        initUI();
    }

    /**
     * 绑定监听
     *
     * @param
     */
    private void initUI() {
        view = LayoutInflater.from(context).inflate(R.layout.view_chat_messaage, null);

        rlGoods = (RelativeLayout) view.findViewById(R.id.rl_good);
        ivGoods = (SimpleDraweeView) view.findViewById(R.id.iv_pic);
        btnSend = (SelectorButton) view.findViewById(R.id.btn_send_goods);
        btnSend.setOnClickListener(this);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);

        int height;
        if (mode == MODE_FULL_SCREEN) {
            height = LayoutParams.MATCH_PARENT;
        } else {
            height = Utils.getScreenHeight(context) * 2 / 3;
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        addView(view, params);
        header = (HeadView) findViewById(R.id.header);
        header.setBackOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " 点了返回键");
                stop();
                ChatMessageView.this.onClickBack();
            }
        });

        init();
    }

    public void onClickBack() {
        if (mode == MODE_FULL_SCREEN) {
            otherUserId = "";
            Context mContext = header.mContext;
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        } else {
            closeSelf();
        }
    }

    private void closeSelf() {
        Log.d(TAG, " isFromSession=" + isFromSession);

        if (activity != null) {
            activity.hideSoftInput(activity);
        }
        if (isFromSession == 0) {
            EventBusManager.postEvent(isFromSession, SubcriberTag.SHOW_UNFOLLOW_PANEL);
        } else if (isFromSession == 1) {
            EventBusManager.postEvent(isFromSession, SubcriberTag.SHOW_SESSION_PANEL);
        } else if (!isChatMsgType(otherUserId)) {
            EventBusManager.postEvent(isFromSession, SubcriberTag.SHOW_SESSION_PANEL);
        } else {
            setVisibility(GONE);
        }

        otherUserId = "";
    }

    private void showMessage(final ChatMsg message) {
        datas.add(message);
        refreshAdapter();
        goListBottom();
    }

    private void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }


    private RoomManager.OnResultListener roomListener = new OnResultListenerAdapter() {
        private void insertMessage(String message) {
            Log.d(TAG, message);
        }

        @Override
        public void chatDidConnect() {
            insertMessage("Socket连接成功");
//            //更新对方用户数据
//            if (isChatMsgType(otherUserId)) {
//                roomManger.queryUserInfo(otherUserId);
//            }
        }

        @Override
        public void chatDidDisconnect() {
            insertMessage("Socket断开成功");
        }

        @Override
        public void chatErrorOccur(int errcode, final String errMsg) {
//            insertMessage("Socket errcode=" + errcode + " errMsg=" + errMsg);
            if (errcode == RoomManager.CHAT_ERROR_SERVER_DISCONNECT) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        activity.showToast(errMsg);
//                    }
//                });
            } else if (errcode == RoomManager.TLV_E_KAZUAN_NOT_ENOUGH) {
                if (mode == MODE_FULL_SCREEN)
                    activity.showRechargeDialog(errMsg);
            } else {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        activity.showToast(errMsg);
//                    }
//                });
            }
        }

        @Override
        public void chatReceiveGift(BytesReader.GiftMessage message) {
//            insertMessage(String.format("收到礼物 来自:%s id:%s 连送数:%d 用户ID:%s 时间:%d", message.nickName, message.giftID, message.count, message.audienceID, message.time));
//            if (message.type == RoomManager.TLV_REQ_UNICAST_GIVE) {
//                if (isResume()) {
//                    ChatMsg giftMessagee = createGiftMessagee("送" + GiftGridView.getUnitByGiftId(message.giftID + "") + GiftGridView.getNameByGiftId(message.giftID + ""), false, message.time, otherUserId, otherNickName, otherAvatar, Integer.parseInt(message.giftID));
//                    showMessage(giftMessagee);
//                    saveMessage(giftMessagee, false);
//                }
//            }
        }

        @Override
        public void chatDidReceiveMessage(BytesReader.AudienceMessage message) {
//            insertMessage(String.format("收到消息 来自:%s 类型:%d 内容:%s 等级:%d id:%s time:%d  type:%d  ", message.nickName, message.type, message.content, message.level
//                    , message.audienceID, message.time, message.type));
//
//            if (message.type == 0) {
//                if (isResume()) {
//                    if (message.audienceID.equals(otherUserId)) {
//                        Log.d(TAG, "是当前用户消息 插入并显示");
//                        ChatMsg chatMsg = createMessagee(message.content, false, message.time, message.audienceID, otherNickName, otherAvatar);
//                        saveMessage(chatMsg, false);
//                        showMessage(chatMsg);
//                    } else {
//                        Log.d(TAG, "不是当前用户消息 插入但不显示");
//                        ChatMsg chatMsg = createMessagee(message.content, false, message.time, message.audienceID, message.nickName, "");
//                        saveMessage(chatMsg, true);
//                    }
//                }
//            }
        }


        @Override
        public void chatDidSuccess(int code) {
            //Log.d(TAG, "chatDidSuccess code=" + code + " seq=" + seq);
//            if (code == RoomManager.TLV_RSP_SAY) {
//                ChatMsg curChatMsg = sendingMsgs.get(seq);
//                if (curChatMsg != null)
//                    activity.saveMessage(curChatMsg, false);
//                showMessage(curChatMsg);
//                sendingMsgs.remove(seq);
//            } else if (code == RoomManager.TLV_RSP_GIVE) {
//                ChatMsg curGiftMsg = sendingMsgs.get(seqGift);
//                if (curGiftMsg != null)
//                    activity.saveMessage(curGiftMsg, false);
//                showMessage(curGiftMsg);
//                sendingGifts.remove(seqGift);
//            }
        }

        @Override
        public void chatDidQueryUserInfo(BytesReader.Audience audience) {
            Log.d(TAG, "chatDidQueryUserInfo 获取用户信息成功  " + audience + " isChatMsgType=" + isChatMsgType(otherUserId));
//            Log.e(TAG, "chatDidQueryUserInfo is main : " + (Looper.getMainLooper() == Looper.myLooper()));
            if (isChatMsgType(otherUserId)) {
                Log.d(TAG, "更新会话");
                //更新会话
                otherNickName = audience.nickName;
                otherAvatar = audience.avatar;
                if (goodsBean == null && course == null) {
                    header.setTitle(otherNickName);
                } else {
                    header.setTitle("客服");
                }
                adapter.otherAvatar = audience.avatar;
                refreshAdapter();
                DbManger.getInstance().updateSession(AccountInfoManager.getInstance().getCurrentAccountUserIdStr()
                        , audience.id, audience.nickName, audience.avatar, audience.level, (short) audience.auth, (int) audience.isFollow, !Utils.isEmpty(audience.gender) ? Integer.parseInt(audience.gender) : -1);
            }
        }
    };


    public void refreshChat() {
//        if (isChatMsgType(otherUserId)) {
        DbManger.getInstance().getChatMsgBySessionId(AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + otherUserId, 0, new IDataListener() {
            @Override
            public void onSuccess(final Object data, int code, String msg) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<ChatMsg> messages = (List<ChatMsg>) data;
                        if (!Utils.listIsNullOrEmpty(messages)) {
                            Collections.reverse(messages);
                            resetDatas();
                            datas.addAll(messages);
                        }
                        refreshAdapter();
                        goListBottom();
//                        lvChat.scrollToPosition(adapter.getItemCount() - 1);

//                       lvChat.setSelection(adapter.getCount() - 1);
//                       lvChat.scrollToPosition(datas.size()-1);
                    }
                });

            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }


    public void refreshMishuChat() {
        resetDatas();
        if (mishuType == DbManger.TYPE_CHAT_MISHU_ATTENTION) {
            ChatMsg lastSystemMsg = DbManger.getInstance().getLastMishuSystem(AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + "mishu");
            if (lastSystemMsg != null) {
                datas.add(0, lastSystemMsg);
            }
        }

        DbManger.getInstance().getMishuByType(AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + "mishu", mishuType, new IDataListener() {
            @Override
            public void onSuccess(final Object data, int code, String msg) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPtrLoadmoreWidget.refreshComplete();
                        List<ChatMsg> messages = (List<ChatMsg>) data;
                        if (!Utils.listIsNullOrEmpty(messages)) {

                            if (mishuType == DbManger.TYPE_CHAT_MISHU_SYSTEM) {
                                Collections.reverse(messages);
                            }
                            datas.addAll(messages);
                        }
                        refreshAdapter();
                        if (mishuType == DbManger.TYPE_CHAT_MISHU_SYSTEM) {
                            goListBottom();
                        } else {
                            goListTop();
                        }
//                        lvChat.scrollToPosition(0);

                        mPtrLoadmoreWidget.setPullToRefresh(false);

//                       lvChat.setSelection(adapter.getCount() - 1);
//                       lvChat.scrollToPosition(datas.size()-1);
                    }
                });

            }

            @Override
            public void onFailure(int code, String msg) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPtrLoadmoreWidget.refreshComplete();
                        mPtrLoadmoreWidget.setPullToRefresh(false);
                    }
                });
            }
        });
    }

    RecyclerView.ItemDecoration itemDecoration;

    private int sessionType;
    private int mishuType = -1;

    public void initData(final BaseActivity activity, final String userId, String otherNickName, String otherAvatar, int sessionType, int mishuType) {
        box.setFrom(mFrom);
        this.activity = activity;
        Log.d(TAG, "activity :" + activity.toString() + ";" + activity.getLocalClassName());
        otherUserId = userId;
        if (Utils.isEmpty(otherUserId)) {
            Log.d(TAG, "用户不在了");
            activity.showToast(R.string.user_not_here);
            activity.finish();
            return;
        }
        //清空旧数据
        if (!Utils.listIsNullOrEmpty(datas)) {
            resetDatas();
        }

        this.otherNickName = otherNickName;
        this.otherAvatar = otherAvatar;
        this.sessionType = sessionType;
        this.mishuType = mishuType;
        myUserInfo = AccountInfoManager.getInstance().getAccountInfo();
        header.setTitle(otherNickName);
        header.setTipOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.startActivity(activity, userId);
            }
        });
        if (StringUtils.isNotEmpty(otherNickName) && (otherNickName.equals(ResourceHelper.getString(R.string.laka_guanfang)) || otherNickName.equals(ResourceHelper.getString(R.string.laka_mishu)))) {
            header.setTipShow(false);
        } else {
            header.setTipShow(true);
        }
//        init();
        Log.d(TAG, "myUserId=" + AccountInfoManager.getInstance().getCurrentAccountUserId()
                + " otherUserId=" + otherUserId + " otherNickName=" + otherNickName
                + " otherAvatar=" + otherAvatar + " sessionType=" + sessionType);
        adapter.sessionType = sessionType;
        adapter.mishuType = mishuType;
        adapter.otherAvatar = otherAvatar;
        adapter.myAvatar = AccountInfoManager.getInstance().getCurrentAccountUserAvatar();
        if (sessionType == DbManger.SESSION_TYPE_MISHU) {
            refreshMishuChat();
        } else {
            refreshChat();
        }

        if (itemDecoration != null) {
            lvChat.removeItemDecoration(itemDecoration);
        }

        if (isChatMsgType(userId)) {
            box.setVisibility(View.VISIBLE);
            //预加载表情图片
            EmojiManager.initDrawables(context);
            itemDecoration = new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL, context.getResources().getColor(R.color.transparent), Utils.dip2px(getContext(), 15), 0, 0);
            lvChat.addItemDecoration(itemDecoration);
        } else {
            box.setVisibility(View.GONE);
            LayoutParams lp = (LayoutParams) mPtrLoadmoreWidget.getLayoutParams();
            lp.height = LayoutParams.MATCH_PARENT;
            lvChat.setLayoutParams(lp);
            if (userId.equals(DbManger.SESSION_ID_LAKA_MISHU) && mishuType == DbManger.TYPE_CHAT_MISHU_SYSTEM) {
                itemDecoration = new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL, context.getResources().getColor(R.color.transparent), Utils.dip2px(getContext(), 30), 0, 0);
                lvChat.addItemDecoration(itemDecoration);
            } else if (userId.equals(DbManger.SESSION_ID_LAKA_MISHU)) {
                itemDecoration = new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL, context.getResources().getColor(R.color.transparent), 0, 0, 0);
                lvChat.addItemDecoration(itemDecoration);
            } else if (userId.equals(DbManger.SESSION_ID_LAKA_GUANFANG)) {
                itemDecoration = new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL, context.getResources().getColor(R.color.transparent), Utils.dip2px(getContext(), 30), 0, 0);
                lvChat.addItemDecoration(itemDecoration);
            }
        }

        if (!isInit) {
            isInit = true;
            roomManger = RoomManager.getInstance();
            roomManger.addResultListener(roomListener);
            roomManger.startRoom();
        }
        //更新对方用户数据
        if (isChatMsgType(otherUserId)) {
            Log.d(TAG, "需要更新用户数据");
            roomManger.queryUserInfo(otherUserId);
        } else {
            Log.d(TAG, "不需要更新用户数据");
        }

        if (adapter != null) {
            adapter.setActivity(activity);
        }
        if (box != null) {
            box.setActivity(activity);
        }
        if(otherUserId.equals(SystemConfig.getInstance().getKefuID())){
            header.setTipShow(false);
        }else{
            header.setTipShow(true);
        }
    }


    public void initData(final BaseActivity activity, final String userId, String otherNickName, String otherAvatar, int sessionType) {
        this.initData(activity, userId, otherNickName, otherAvatar, sessionType, -1);
    }

    private boolean isChatMsgType(String userId) {
        return !userId.equals(DbManger.SESSION_ID_LAKA_MISHU) && !userId.equals(DbManger.SESSION_ID_LAKA_GUANFANG);
    }

    private boolean isInit = false;

    private void resetDatas() {
        datas.clear();
        adapter.notifyDataSetChanged();
//        datas.add(new ChatMsg());//为了解决时间错乱第一行不要
    }

    LinearLayoutManager linearLayoutManager;

    private LoadMoreFoot mLoadMoreHeader;

    private void init() {
//        adapter = new ChatMessageAdapter(context, datas, getOnChatItemClickListener(), DbManger.SESSION_TYPE_FOLLOW);
//        lvChat = (ListView) findViewById(R.id.chat_listview);
        adapter = new ChatMessageRcvAdapter(context, datas, DbManger.SESSION_TYPE_FOLLOW, getOnChatItemClickListener());

        mPtrLoadmoreWidget = (PtrClassicFrameLayout) findViewById(R.id.ptr_loadmore_widget);
        mLoadMoreHeader = new LoadMoreFoot(context);
        mPtrLoadmoreWidget.setHeaderView(mLoadMoreHeader);
        mPtrLoadmoreWidget.setLoadingMinTime(1000);
        mPtrLoadmoreWidget.addPtrUIHandler(new PtrUIHandler() {
            @Override
            public void onUIReset(PtrFrameLayout ptrFrameLayout) {

            }

            @Override
            public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {

            }

            @Override
            public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                mLoadMoreHeader.showLoading();
            }

            @Override
            public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {
                mLoadMoreHeader.hideLoading();
                lvChat.scrollBy(0, 0 - 100);
            }

            @Override
            public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean b, byte b1, PtrIndicator ptrIndicator) {

            }
        });
        mPtrLoadmoreWidget.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
                return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (sessionType == DbManger.SESSION_TYPE_MISHU) {
                    refreshMishuChat();
                } else {
                    DbManger.getInstance().getChatMsgBySessionId(AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + otherUserId, datas.size(), new IDataListener() {
                        @Override
                        public void onSuccess(final Object data, int code, String msg) {
                            Log.d(TAG, " getChatMsgBySessionId onSuccess");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPtrLoadmoreWidget.refreshComplete();
                                    List<ChatMsg> messages = (List<ChatMsg>) data;
                                    if (!Utils.listIsNullOrEmpty(messages)) {
                                        Collections.reverse(messages);
                                        datas.addAll(0, messages);
                                        adapter.notifyItemRangeInserted(0, messages.size() - 1);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            Log.d(TAG, " getChatMsgBySessionId onFailure");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPtrLoadmoreWidget.refreshComplete();
                                }
                            });
                        }
                    });
                }
            }
        });

        lvChat = (RecyclerView)

                findViewById(R.id.chat_listview);
        lvChat.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()

        {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (box != null) {
                    box.hideKeyboard(context);
                    box.hideLayout();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        linearLayoutManager = new

                LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        lvChat.setLayoutManager(linearLayoutManager);
        lvChat.setItemAnimator(null);//取消item出现动画

//        setLvChatHeight();

//        adapter = new ChatMessageRcvAdapter(this, datas, getOnChatItemClickListener());
//        lvChat = (RecyclerView) findViewById(R.id.chat_listview);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        lvChat.addItemDecoration(new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL, getResources().getColor(R.color.transparent), Utils.dip2px(this,30), 0, 0));
//        lvChat.setLayoutManager(linearLayoutManager);
//        lvChat.setItemAnimator(null);//取消item出现动画

//        lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvChat.setAdapter(adapter);

//        ivFace = (ImageView) findViewById(R.id.iv_face);
//        ivFace.setOnClickListener(this);
//        ivGift = (ImageView) findViewById(R.id.iv_gift);
//        ivGift.setOnClickListener(this);

        box = (KJChatKeyboard)

                findViewById(R.id.chat_msg_input_box);
        box.setActivity(activity);
        box.setFrom(mFrom);

        initMessageInputToolBox();

        goListBottom();
    }

    private void initMessageInputToolBox() {
        box.setOnOperationListener(new OnOperationListener() {

            @Override
            public void sendGift(GiftInfo giftInfo) {

                Log.d(TAG, "sendGift id=" + giftInfo.getId() + " otherUserId=" + otherUserId );

                //特效音量
                GiftAudioManager.getInstance().playSound(R.raw.send_gift_audio);
                ;


                ChatMsg curGiftMsg = activity.createGiftMessagee("送" + GiftResManager.getInstance().getUnitByGiftId(giftInfo.getId() + "") + GiftResManager.getInstance().getNameByGiftId(giftInfo.getId() + ""), true, Utils.getCurrentTimeSecond(), otherUserId, otherNickName, otherAvatar, giftInfo.getId());

                if (NetworkUtil.isNetworkOk(context)) {
                    curGiftMsg.setState(ChatEntity.MSG_STATE_SENDING);
                    sendingGifts.put(transientId, curGiftMsg);
                    transientId++;
                } else {
                    curGiftMsg.setState(ChatEntity.MSG_STATE_FAIL);
                }
                showMessage(curGiftMsg);
                activity.saveMessage(curGiftMsg, false);
            }

            @Override
            public void send(String content) {
                ChatMsg curChatMsg = activity.createMessagee(content, true, Utils.getCurrentTimeSecond(), otherUserId, otherNickName, otherAvatar);

                if (NetworkUtil.isNetworkOk(context)) {
                    curChatMsg.setState(ChatEntity.MSG_STATE_SENDING);

                    sendingMsgs.put(transientId, curChatMsg);
                    transientId++;
                } else {
                    curChatMsg.setState(ChatEntity.MSG_STATE_FAIL);
                }
                showMessage(curChatMsg);
                activity.saveMessage(curChatMsg, false);
            }

            @Override
            public void selectedEmoji(Emojicon emoji) {
                box.getEditTextBox().append(emoji.getValue());
            }

            @Override
            public void selectedBackSpace(Emojicon back) {
                DisplayRules.backspace(box.getEditTextBox());
            }

            @Override
            public void selectedFunction(int index) {
                switch (index) {
                    case FUNCATION_CHOICE_PHOTO:
                        goToAlbum();
                        break;
                    case FUNCATION_TAKE_PHOTO:
                        goToCamera();
                        break;
                }
            }
        });
    }

    private void goToAlbum() {
        if (!Util.choicePhoto(activity, 0)) {
            activity.showToast(R.string.can_not_find_gallery);
        }
    }

    private File mImageFile;

    private void goToCamera() {
        try {
            mImageFile = Util.createImageFile(activity, Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Util.takePhoto(activity, mImageFile, REQUEST_TAKE_PHOTO);
    }

    private boolean isSendingQueueEmpty() {
        if (sendingMsgs.isEmpty() && sendingGifts.isEmpty()) {
            return true;
        }
        return false;
    }

    private Timer mSendMsgTimer;

    private void startSendTimer() {
        if (mSendMsgTimer == null) {
            Log.d(TAG, "create Timer");
            mSendMsgTimer = new Timer();
            mSendMsgTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = Utils.getCurrentTimeSecond();
                    handler.sendMessage(msg);
                }
            }, 10000, 1000);
        }
    }

    private void cancelSendTimer() {
        if (mSendMsgTimer == null) {
            return;
        }
        mSendMsgTimer.cancel();
        mSendMsgTimer = null;
    }

    private void handleSendMsgState(Map map, long sendTime) {
        if (map.isEmpty()) {
            return;
        }
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ChatMsg chatMsg = (ChatMsg) entry.getValue();
            if (chatMsg.getDate() <= sendTime) {
                chatMsg.setState(ChatEntity.MSG_STATE_FAIL);
                activity.updateMessage(chatMsg);
                iterator.remove();
                isMsgUpdate = true;
            }
        }

    }


    private void goListBottom() {
        post(new Runnable() {
            @Override
            public void run() {
                int position = adapter.getItemCount() - 1;
                Log.d(TAG, "goListBottom-->position:" + position);

                if (position >= 0) {
                    lvChat.scrollToPosition(position);
                }
            }
        });
    }

    private void goListTop() {
        post(new Runnable() {
            @Override
            public void run() {
                lvChat.scrollToPosition(0);
            }
        });
    }

    /**
     * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
     *
     * @return 会隐藏输入法键盘的触摸事件监听器
     */
//    private View.OnTouchListener getOnTouchListener() {
//        return new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                box.hideLayout();
//                if (box != null){
//                    box.hideKeyboard(context);
//                    box.hideEmojiLayout();
//                }
//
//                //隐藏
//                return true;
//            }
//        };
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_goods:
                if (course != null) {
                    doSendCourse();
                } else if (goodsBean != null) {
                    doSendGoods();
                }
                break;
//            case R.id.btn_send:
//                addChat();
//                break;
//            case R.id.iv_face://表情
//
//                break;
        }
    }

    private void doSendGoods() {
        if (goodsBean == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        try {
            String title = Base64.encodeToString(goodsBean.getTitle().getBytes("UTF-8"), Base64.DEFAULT);
            sb.append("[商品]").append("?").append("title:").append(title).append("&id:").append(goodsBean.getGoodsId());

            Log.d(TAG, " doSendGoods content=" + sb.toString()
                    + " title=" + title);
            ChatMsg curChatMsg = activity.createMessagee(sb.toString(), true, Utils.getCurrentTimeSecond(), otherUserId, otherNickName, otherAvatar);
            if (NetworkUtil.isNetworkOk(context)) {
                curChatMsg.setState(ChatEntity.MSG_STATE_SENDING);
                sendingMsgs.put(transientId, curChatMsg);
                transientId++;
            } else {
                curChatMsg.setState(ChatEntity.MSG_STATE_FAIL);
            }
            showMessage(curChatMsg);
            activity.saveMessage(curChatMsg, false);
            hideGood();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(TAG,"发送失败");
        }
    }


    private void doSendCourse() {
        if (course == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        try {
            String title = Base64.encodeToString(course.getTitle().getBytes("UTF-8"), Base64.DEFAULT);
            sb.append("[课程]").append("?").append("title:").append(title).append("&id:").append(course.getId());

            Log.d(TAG, " doSendCourse content=" + sb.toString()
                    + " title=" + title);
            ChatMsg curChatMsg = activity.createMessagee(sb.toString(), true, Utils.getCurrentTimeSecond(), otherUserId, otherNickName, otherAvatar);
            if (NetworkUtil.isNetworkOk(context)) {
                curChatMsg.setState(ChatEntity.MSG_STATE_SENDING);
                sendingMsgs.put(transientId, curChatMsg);
                transientId++;
            } else {
                curChatMsg.setState(ChatEntity.MSG_STATE_FAIL);
            }
            showMessage(curChatMsg);
            activity.saveMessage(curChatMsg, false);
            hideGood();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(TAG, "发送失败");
        }
    }


    int mFrom;

    public void setFrom(int from) {
        mFrom = from;
    }

    public void showGood(ShoppingGoodsDetailBean goodsBean) {
        this.goodsBean = goodsBean;
        rlGoods.setVisibility(View.VISIBLE);
        ImageUtil.loadImage(ivGoods, goodsBean.getThumbUrl());
        tvName.setText(goodsBean.getTitle());
        tvPrice.setText("¥ " + goodsBean.getSalePrice());
        header.setTitle("客服");
        btnSend.setText("发送宝贝");
    }

    public void hideGood() {
        rlGoods.setVisibility(View.GONE);
    }

    public void showCourse(CourseDetail courseDetail) {
        this.courseDetail = courseDetail;
        this.course = courseDetail.course;
        rlGoods.setVisibility(View.VISIBLE);
        ImageUtil.loadImage(ivGoods, course.getCover_url());
        tvName.setText(course.getTitle());

        if (courseDetail.hasSimilarCourse()) {
            tvPrice.setText(NumberUtils.splitDoubleStr(course.getPrice()) + "味豆");
        } else {
            tvPrice.setText(NumberUtils.splitDoubleStr(course.getPrice() - courseDetail.course_trailer.savedCoins) + "味豆");
        }




        header.setTitle("客服");
        btnSend.setText("发送课程");
    }

    public void hideCourse() {
        rlGoods.setVisibility(View.GONE);
    }


    /**
     * 聊天列表中对内容的点击事件监听
     */
    public interface OnChatItemClickListener {
        void onPhotoClick(int position);

        void onTextClick(int position);

        void onFaceClick(int position);

        void onFailedClick(int position);
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

            @Override
            public void onFailedClick(int position) {
                if (NetworkUtil.isNetworkOk(context)) {
                    ChatMsg chatMsg = datas.get(position);
                    chatMsg.setState(ChatEntity.MSG_STATE_SENDING);
                    switch (chatMsg.getType()) {
                        case ChatEntity.MSG_TYPE_GIFT:
                            sendingGifts.put(chatMsg.getId().intValue(), chatMsg);
                            break;
                        default:
                            sendingMsgs.put(chatMsg.getId().intValue(), chatMsg);
                            break;
                    }
                    sendChatMsg(chatMsg);
                    datas.remove(position);
                    datas.add(position, chatMsg);
                    refreshAdapter();
                    Log.d(TAG, "resend.");
                }
            }
        };
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果软键盘弹起，收起
            if (box.emoticonPickerView.getVisibility() == View.VISIBLE) {
                box.hideLayout();
                return true;
            } else if (box.isKeyboradShow) {
                box.hideKeyboard(activity);
                return true;
            }
        }

        if (mode == MODE_HALF_SCREEN) {
            //返回会话pannel
            closeSelf();
            return true;
        }

        return super.onKeyDown(keyCode, event);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        if (SubcriberTag.REFRESH_LAST_KAZUAN.equals(event.tag)) {
            double result = (double) event.event;
            Log.d(TAG, "REFRESH_LAST_KAZUAN coins=" + result);
            if (box != null && box.giftGridView != null) {
                box.giftGridView.setBalance(result);
            }
            AccountInfoManager.getInstance().updateCurrentAccountCoins(result);
        } else if (SubcriberTag.REFRESH_CHAT_MESSGAE.equals(event.tag)) {
//            refreshChat();
        } else if (SubcriberTag.RECEIVE_CHAT_GIFT.equals(event.tag)) {
            ChatMsg msg = (ChatMsg) event.event;
            if (msg.getUserId().equals(otherUserId)) {
                showMessage(msg);
            }
        } else if (SubcriberTag.RECEIVE_CHAT_MSG.equals(event.tag)) {
            ChatMsg msg = (ChatMsg) event.event;
            if (msg.getUserId().equals(otherUserId)) {
                showMessage(msg);
            }
        } else if (SubcriberTag.SEND_CHAT_MSG_SUCCESS.equals(event.tag)) {
            int reqMsg = (int) event.event;
            ChatMsg curChatMsg = sendingMsgs.get(reqMsg);
            if (curChatMsg != null) {
                curChatMsg.setState(ChatEntity.MSG_STATE_SUCCESS);
                curChatMsg.setDate(Utils.getCurrentTimeSecond());
                activity.updateMessage(curChatMsg);
                refreshChat();
                sendingMsgs.remove(reqMsg);
                if (isSendingQueueEmpty()) {
                    cancelSendTimer();
                }
            }
        } else if (SubcriberTag.SEND_CHAT_GIFT_SUCCESS.equals(event.tag)) {
            int reqGift = (int) event.event;
            ChatMsg curGiftMsg = sendingGifts.get(reqGift);
            if (curGiftMsg != null) {
                curGiftMsg.setState(ChatEntity.MSG_STATE_SUCCESS);
                curGiftMsg.setDate(Utils.getCurrentTimeSecond());
                activity.updateMessage(curGiftMsg);
                refreshChat();
                sendingGifts.remove(reqGift);
                if (isSendingQueueEmpty()) {
                    cancelSendTimer();
                }
            }
        }else if (SubcriberTag.SEND_CHAT_GIFT_FAIL.equals(event.tag)) {
            //删除失败记录
            int reqGift = (int) event.event;
            ChatMsg curGiftMsg = sendingGifts.get(reqGift);
            if (curGiftMsg != null) {
//                curGiftMsg.setState(ChatEntity.MSG_STATE_SUCCESS);
//                curGiftMsg.setDate(Utils.getCurrentTimeSecond());
//                activity.updateMessage(curGiftMsg);
                activity.deleteMessage(curGiftMsg);
                refreshChat();
                sendingGifts.remove(reqGift);
                if (isSendingQueueEmpty()) {
                    cancelSendTimer();
                }
            }
        }else if (SubcriberTag.SEND_CHAT_GIFT_NOT_ENOUGH.equals(event.tag)) {
            if (mode == MODE_FULL_SCREEN)
                activity.showRechargeDialog("2");
        }

        else if (SubcriberTag.ADD_CHAT_MSG_SUCCESS.equals(event.tag)) {
            ChatMsg chatMsg = (ChatMsg) event.event;
            if (chatMsg == null) {
                return;
            }
            if (checkContainMsg(chatMsg, sendingMsgs) || checkContainMsg(chatMsg, sendingGifts)) {
                sendChatMsg((ChatMsg) event.event);
            }
        }
        else if(SubcriberTag.GO_SHOP_GOOD_DETAIL.equals(event.tag)){
            String goodId = (String) event.event;
            ShoppingGoodsDetailActivity.startActivity(activity,Integer.parseInt(goodId));
        } else if (SubcriberTag.GO_COURSE_DETAIL.equals(event.tag)) {
            String goodId = (String) event.event;
            CourseDetailActivity.startActivity(activity, goodId);
        }
    }

    private boolean checkContainMsg(ChatMsg chatMsg, Map map) {
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ChatMsg msg = (ChatMsg) entry.getValue();
            if (chatMsg.getDate().equals(msg.getDate())) {
                map.remove(entry.getKey());
                map.put(chatMsg.getId().intValue(), chatMsg);
                return true;
            }
        }
        return false;
    }

    private void sendChatMsg(ChatMsg chatMsg) {
        if (chatMsg == null) {
            return;
        }
        int id = chatMsg.getId().intValue();
        switch (chatMsg.getType()) {
            case ChatEntity.MSG_TYPE_GIFT:
                roomManger.inboxGift(String.valueOf(chatMsg.getGiftId()), otherUserId, id);
                break;
            default:
                roomManger.inboxMessage(chatMsg.getContent(), otherUserId, id);
                break;
        }
        startSendTimer();
    }

    public void stop() {
        EventBusManager.unregister(this);
        if (roomManger != null) {
            roomManger.removeResultListener(roomListener);
        }
        if (box != null) {
            box.stop();
        }
         otherUserId = "";
    }

    public void start() {
        AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_15006);
        EventBusManager.register(this);
        if (box != null) {
            box.start();
        }
    }

}
