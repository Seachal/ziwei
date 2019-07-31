package com.laka.live.ui.course.detail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.qiyukf.unicorn.api.Unicorn;

import java.util.HashMap;

import framework.ioc.annotation.InjectView;
import laka.live.bean.ChatSession;

/**
 * Created by Lyf on 2017/9/12.
 */
public class TitleBarView extends BaseDetailView implements View.OnClickListener {

    private static final String TAG = "TitleBar";

    @InjectView(id = R.id.title)
    private TextView title;
    @InjectView(id = R.id.titleBar)
    private View titleBar;

    private ChatSession mChatSession; // 客服会话

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(this,R.layout.view_course_detail_title);
    }

    @Override
    protected void initData(CourseDetailHelper mHelper) {
        super.initData(mHelper);
        setTitle();
        refreshUnread();
        onEvent(mHelper.getCourseId());
    }

    // 设置标题
    private void setTitle() {

        // 如果没有预告，就不显示播放按钮
        if (Utils.isEmpty(mHelper.getCourseTrailer().getVideoUrl())) {
            title.setText(mHelper.getCourse().isLive() ? "直播详情" : "视频详情");
        } else {
            title.setText("立即播放");
            Drawable drawable = ResourceHelper.getDrawable(R.drawable.nav_icon_play);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            title.setCompoundDrawablePadding(ResourceHelper.getDimen(R.dimen.space_5));
            title.setCompoundDrawables(drawable, null, null, null);
        }

        mHelper.getScrollView().setGradientTitleBar(mContext, mHelper.getCourse().getAgentProfitratio()
                ,this, mHelper.getRootView().findViewById(R.id.mask));
    }


    // 设置未读数
    public void setUnreadCount(int unreadCount) {

        if (mChatSession != null) {
            mChatSession.setUnreadCnt(unreadCount);
        }
    }


    // 刷新未读数
    public void refreshUnread() {

//        if (mChatSession == null) {
//            mChatSession = DbManger.getInstance().getSessionBySessionId(AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + SystemConfig.getInstance().getKefuID());
//        }
//
        int count = Unicorn.getUnreadCount();
        TitleBarActionItem ivService = mHelper.getScrollView().getIvService();
        Log.d(TAG, " refreshUnread count=" + count);
        if (ivService != null) {
            ivService.setRedTipVisibility(count > 0);
            ivService.setRedTipText(count > 99 ? "99+" : String.valueOf(count));
        } else {
            Log.d(TAG, " refreshUnread ivService==null");
        }

    }

    // 点击客服按钮
    public void onServiceClick() {
        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            mHelper.getActivity().goLogin();
            return;
        }
        setUnreadCount(0);
        refreshUnread();
        ChatMessageActivity.startActivity(mHelper.getActivity(), mHelper.getCourseDetail());
    }

    // 友盟事件
    private void onEvent(String courseId) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.ID, courseId);
        AnalyticsReport.onEvent(mContext, AnalyticsReport.COURSE_DETAIL_SHOW_EVENT_ID, params);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

}
