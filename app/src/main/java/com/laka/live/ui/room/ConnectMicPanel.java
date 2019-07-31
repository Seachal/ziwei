package com.laka.live.ui.room;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.widget.gift.ExpressionPagerAdapter;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.laka.live.help.SubcriberTag.CLEAR_LINK_CLICK_EVENT;

/**
 * Created by luwies on 16/10/27.
 */

public class ConnectMicPanel extends BasePanel implements View.OnClickListener,
        ViewPager.OnPageChangeListener, EventBusManager.OnEventBusListener {

    private ViewPager mViewPager;
//    private com.laka.live.ui.adapter.TabsAdapter mTabsAdapter;

    private ExpressionPagerAdapter mAdapter;

    private List<View> mTabList;

    private View mParentView;

    private List<ConnectListBaseView> mViews;

    private ConnectMicManager connectMicManager;
    private UserInfo mAnchorInfo;

    private View mClearView;

    public ConnectMicPanel(Context context,ConnectMicManager connectMicManager, UserInfo anchorInfo) {
        super(context);
        mAnchorInfo = anchorInfo;
        setAlpha(0);
        this.connectMicManager = connectMicManager;
        initView();
    }


    private void initView() {

        mViewPager = (ViewPager) mParentView.findViewById(R.id.viewpager);

        mViews = new ArrayList<>(2);
        ConnectListView connectListView = new ConnectListView(mContext);
        connectListView.setConnectMicManager(connectMicManager);
        mViews.add(connectListView);
        OnlineAudienceView audienceView = new OnlineAudienceView(mContext);
        audienceView.setConnectMicManager(connectMicManager);
        mViews.add(audienceView);
        mAdapter = new ExpressionPagerAdapter(mViews);

        mViewPager.setOffscreenPageLimit(mViews.size());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);


        mTabList = new ArrayList<>(2);
        CheckedTextView connectTab = (CheckedTextView) mParentView.findViewById(R.id.connect_tab);
        connectTab.setOnClickListener(this);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(connectTab, null, null, null,
                ContextCompat.getDrawable(mContext, R.drawable.connect_mic_tab_bar_selecter));
        connectTab.setText(R.string.connect_mic_list);
        CheckedTextView onlineTab = (CheckedTextView) mParentView.findViewById(R.id.online_tab);
        onlineTab.setText(R.string.online_audience);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(onlineTab, null, null, null,
                ContextCompat.getDrawable(mContext, R.drawable.connect_mic_tab_bar_selecter));
        onlineTab.setOnClickListener(this);
        mTabList.add(connectTab);
        mTabList.add(onlineTab);

        mViewPager.setCurrentItem(0);
        connectTab.setSelected(true);

        mClearView = mParentView.findViewById(R.id.clear);
        mClearView.setOnClickListener(this);
    }

    private static ConnectUserInfo fromUserInfo(UserInfo info) {
        if (info == null) {
            return null;
        }
        ConnectUserInfo connectUserInfo = new ConnectUserInfo();
        connectUserInfo.setId(info.getId());
        connectUserInfo.setAvatar(info.getAvatar());
        connectUserInfo.setNickName(info.getNickName());
        connectUserInfo.setGender(info.getGender());
        connectUserInfo.setUserSig(info.getUserSig());
        connectUserInfo.setAuth(info.getAuth());
        connectUserInfo.setLevel(info.getLevel());
        connectUserInfo.setInConnectMicList(true);
        return connectUserInfo;
    }

    @Override
    protected View onCreateContentView() {
        mParentView = View.inflate(mContext, R.layout.panel_connect_mic, null);
        return mParentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        FrameLayout.LayoutParams params = super.getLayoutParams();
        params.height = Utils.dip2px(mContext, 354f);
        return params;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.online_tab:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.clear:
                AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11264);
                connectMicManager.clearConnectList();
                EventBusManager.postEvent(null, CLEAR_LINK_CLICK_EVENT);
                break;
        }
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        for (ConnectListBaseView view : mViews) {
            if (view != null) {
                view.register();
            }
        }
        EventBusManager.register(this);
        if (mViews != null && mViews.isEmpty() == false) {
            mViews.get(0).refresh();
        }
    }

    @Override
    protected void onHide() {
        super.onHide();

        for (ConnectListBaseView view : mViews) {
            if (view != null) {
                view.unRegister();
            }
        }
        EventBusManager.unregister(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int index = position;
        int i = 0;
        for (View view : mTabList) {
            view.setSelected(i == index);
            i++;
        }

        mClearView.setVisibility(index == 0 ? View.VISIBLE : View.GONE);

        isSelected = true;
    }

    private boolean isSelected = false;

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE && isSelected) {
            isSelected = false;
            mViews.get(mViewPager.getCurrentItem()).refresh();
        }
    }

    @Subscribe
    @Override
    public void onEvent(PostEvent event) {
        if (TextUtils.equals(event.tag, SubcriberTag.SHOW_MESSAGE_PANEL_CHAT)) {
            mParentView.post(new Runnable() {
                @Override
                public void run() {
                    hidePanel();
                }
            });
        }
    }
}
