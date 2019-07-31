package com.laka.live.ui.widget.room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.ChatEntity;
import com.laka.live.manager.BytesReader;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.gift.GiftAnimManger;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import laka.live.bean.ChatMsg;

/**
 * Created by crazyguan on 2016/4/6.
 */
public class RoomDanmuView extends RelativeLayout {
    private static final String TAG = "RoomDanmuView";
    private Context context;
    private RelativeLayout view;
    private int DANMU_COUNT = 2;
    private ArrayList<View> mVDanmus;//弹幕集合
    private boolean[] isAvailable;//标记弹幕是否可用；
    private Queue<ChatEntity> queue = new LinkedBlockingQueue<>();//等待队列

    public void setDanmuCount(int count) {
        this.DANMU_COUNT = count;
    }

    public RoomDanmuView(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public RoomDanmuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    Handler handler = new Handler() {

    };

    private void initUI() {
        view = (RelativeLayout) LayoutInflater.from(context).inflate(
                R.layout.view_danmu, null);
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(view, lp1);

        //初始化弹幕
        mVDanmus = new ArrayList<>();
        isAvailable = new boolean[DANMU_COUNT];
        for (int i = 0; i < DANMU_COUNT; i++) {
            isAvailable[i] = true;
            View mDanmu = LayoutInflater.from(context).inflate(R.layout.item_room_danmu, null);
            mDanmu.setVisibility(View.GONE);
            mDanmu.setTag(i);//标记编号
            mVDanmus.add(mDanmu);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, Utils.dip2px(context, 36));
            if (i % 2 != 0) {
                lp.setMargins(0, Utils.dip2px(context,48), 0, 0);//38
            }
            view.addView(mDanmu, lp);
        }
    }

    public void addDanmu(ChatEntity msg) {
        int availableIndex = -1;
        availableIndex = getAvailableIndex();
        if (availableIndex >= 0) {
            Log.d(TAG, "弹幕可用编号=" + availableIndex);
            showDanmu(msg, availableIndex);
        } else {
            Log.d(TAG, "所有弹幕不可用 加入等待队列");
            //加入等待队列
            queue.add(msg);
            return;
        }
    }

    private int getAvailableIndex() {
        int availableIndex = -1;
        for (int i = 0; i < DANMU_COUNT; i++) {
            if (isAvailable[i]) {
                availableIndex = i;
                break;
            }
        }
        return availableIndex;
    }

    private void checkQueue() {
        ChatEntity msg = queue.poll();
        if (msg != null) {
            int availableIndex = getAvailableIndex();
            if (availableIndex >= 0) {
                Log.d(TAG, "弹幕可用编号=" + availableIndex);
                showDanmu(msg, availableIndex);
            }
        } else {
            //全部播放完了
            Log.d(TAG, "全部弹幕播放完了");
        }
    }

    private void showDanmu(ChatEntity msg, int availableIndex) {
        final View mDanmu = mVDanmus.get(availableIndex);//获取弹幕
        //填充数据
        TextView tvName = (TextView) mDanmu.findViewById(R.id.tv_name);
        TextView tvContent = (TextView) mDanmu.findViewById(R.id.tv_content);
        SimpleDraweeView ivFace = (SimpleDraweeView) mDanmu.findViewById(R.id.user_face);
        LevelText tvLevel = (LevelText) mDanmu.findViewById(R.id.level);

        RelativeLayout rlDanmu = (RelativeLayout) mDanmu.findViewById(R.id.rl_danmu);

        tvName.setText(msg.getSenderName());
        tvContent.setText(msg.getContent());
        ImageUtil.loadImage(ivFace, msg.getFromUserAvatar());
        tvLevel.setLevel(msg.getLevel());
        mDanmu.setVisibility(View.VISIBLE);

        float nameLength = 0;
        float contentLength = 0;
        Paint p = new Paint();
        p.setTextSize(Utils.dip2px(context, 13));

        if (!Utils.isEmpty(msg.getSenderName())) {
//            nameLength = msg.getSenderName().length()+2;
            nameLength = p.measureText(msg.getSenderName()) + Utils.dip2px(context, 30);
        }
        if (!Utils.isEmpty(msg.getContent())) {
//            contentLength = msg.getContent().length();
            contentLength = p.measureText(msg.getContent()) + 15;
            Log.d(TAG, " measureText=" + p.measureText(msg.getContent()) + " measure=" + tvContent.getMeasuredWidth());
        }

        float length = nameLength > contentLength ? nameLength : contentLength;
        float screenWidth = Utils.getScreenWidth(context);
        float start = screenWidth;
//        float end = -Utils.dip2px(context,13*length+64);
        float end = -(Utils.dip2px(context, 64) + length);


//        ViewGroup.LayoutParams lpParent  =  getLayoutParams();
//        lpParent.width = (int) -end;
//        setLayoutParams(lpParent);
//
//        ViewGroup.LayoutParams lp = mDanmu.getLayoutParams();
//        lp.width = (int) -end;
//        mDanmu.setLayoutParams(lp);
//
//        ViewGroup.LayoutParams lpRl = rlDanmu.getLayoutParams();
//        lpRl.width = (int) -end;
//        rlDanmu.setLayoutParams(lpRl);
//
//
//        ViewGroup.LayoutParams lpContent = tvContent.getLayoutParams();
//        lpContent.width = (int) -end;
//        tvContent.setLayoutParams(lpContent);
//        tvContent.setText(msg.getContent());

        long duration = (long) ((-end + screenWidth) / Utils.dip2px(context, 100)) * 1000;
        Log.d(TAG, "设置弹幕view长度=" + (-end) + " duration=" + duration + " screenwidth=" + screenWidth);
//        long duration = (long) ((-end / screenWidth) * 1000 * 5);
//        long duration = -end*15;
//        long speed = 60l;
//        speed += random()%20;
//        long duration = (-end+screenWidth) / speed;

        Log.d(TAG, " 开始位置=" + start + " 结束位置=" + end);
        ObjectAnimator showAnim = ObjectAnimator.ofFloat(mDanmu, "x", start, end).setDuration(duration);
        final int finalAvailableIndex = availableIndex;
        showAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAvailable[finalAvailableIndex] = true;
                mDanmu.setVisibility(View.GONE);
                checkQueue();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAvailable[finalAvailableIndex] = false;
            }
        });
        showAnim.start();
    }


}
