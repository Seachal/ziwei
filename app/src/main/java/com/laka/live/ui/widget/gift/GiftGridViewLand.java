package com.laka.live.ui.widget.gift;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.GiftInfo;
import com.laka.live.gift.AnimProgressBar;
import com.laka.live.gift.GiftResManager;
import com.laka.live.gift.SlashView;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.help.recycleViewDecoration.RecyclerViewItemDecoration;
import com.laka.live.manager.BytesReader;
import com.laka.live.ui.widget.continueButton.ContinueButton;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by crazyguan on 2016/4/6.
 */
public class GiftGridViewLand extends RelativeLayout {
    private static final String TAG = "GiftGridViewLand";
    private Context context;
    private RelativeLayout view;
    private RelativeLayout vGift;
    private ViewPager vpGift;
    private TextView tvGiftKazuan, tvRecharge;
    private Button btnSendGift;
    private ContinueButton btnSendGiftMulti;
    private ArrayList<GiftGridRcvAdapter> giftAdapters = new ArrayList<>();
    public List<GiftInfo> giftList = new ArrayList<>();
    private GiftGridListener giftListener;
    private LinearLayout llKazuan, llSpecial, llDots;
    private SlashView slashView;
    private AnimProgressBar mPbSpecial;
    private ImageView ivLineTop, ivLineBottom;
    private int circleSelRes = R.drawable.dot_circle_white;
    private int circleNorRes = R.drawable.dot_circle_gray;
    private int itemDecorationRes = R.color.white10;
    public static final String[] giftBigAnimNames = new String[]{"", "", "", "firework", "car", "ship", "plane", "xiannv"
            , "", "", "", "ring", "farm", "chedui", "god"};
    public static final String[] giftSmallAnimNames = new String[]{"rose", "momoda", "yue", "rose", "rose", "rose", "rose", "rose"
            , "gua", "motou", "blue", "ring", "rose", "rose", "rose"};
    public static final int[] giftChatIconRes =
            new int[]{R.drawable.live_present_cream, R.drawable.live_present_cookie,
                    R.drawable.live_present_doughnut, R.drawable.live_present_cupcake,
                    R.drawable.live_present_icecream, R.drawable.live_present_piececake,
                    R.drawable.live_present_cake, R.drawable.live_present_bear,
            };
    public static final int[] giftIconRes = giftChatIconRes;
    public int pageSize = 1;
    public int fromPage = 0;
    public static final int PAGE_LIVE_ROOM = 0, PAGE_CHAT_MESSAGE = 1;
    public boolean isNeedChooseDefault = true;
    GiftInfo curChooseGift;
    List<View> giftViews = new ArrayList<>();
    private int curPage = 0;

    boolean isInRoom = true;
    LinearLayout.LayoutParams lpBig, lpSmall;
    ImageView imageView;
    private ImageView tempImageView;
    private LinearLayout viewDots;
    private ImageView[] imageViews = new ImageView[10];
    private int dotWidth;

    public GiftGridViewLand(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public GiftGridViewLand(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    private void initUI() {
        view = (RelativeLayout) LayoutInflater.from(context).inflate(
                R.layout.layout_room_gift_land, null);
        tvGiftKazuan = (TextView) view.findViewById(R.id.tv_gift_kazuan);
        tvRecharge = (TextView) view.findViewById(R.id.tv_gift_recharge);
        btnSendGift = (Button) view.findViewById(R.id.btn_send_gift);
        btnSendGiftMulti = (ContinueButton) view.findViewById(R.id.btn_send_gift_multi);
        btnSendGiftMulti.setVisibility(View.GONE);
        vpGift = (ViewPager) view.findViewById(R.id.vp_gift);
        vpGift.setOffscreenPageLimit(3);
        vGift = (RelativeLayout) view.findViewById(R.id.ll_gift);
        llKazuan = (LinearLayout) view.findViewById(R.id.ll_kazuan);
        llSpecial = (LinearLayout) view.findViewById(R.id.ll_special);
        llSpecial.setVisibility(View.GONE);
        mPbSpecial = (AnimProgressBar) view.findViewById(R.id.pb_special);
        slashView = (SlashView) view.findViewById(R.id.slash);
        setBalance(AccountInfoManager.getInstance().getCurrentAccountCoins());

        llDots = (LinearLayout) view.findViewById(R.id.ll_dots);
        ivLineBottom = (ImageView) view.findViewById(R.id.iv_line_bottom);
        ivLineTop = (ImageView) view.findViewById(R.id.iv_line_top);

        giftList = GiftResManager.getInstance().getGiftList();
        if (Utils.listIsNullOrEmpty(giftList)) {
            return;
        }

        pageSize = (int) Math.ceil((double) giftList.size() / (double) 8);

        if (pageSize == 0) {
            pageSize = 1;
        }

        initGiftList();
        addView(view);
    }

    public void setListeners(OnClickListener listener, GiftGridListener giftListener, boolean isInRoom) {
        this.isInRoom = isInRoom;
        this.giftListener = giftListener;
        tvGiftKazuan.setOnClickListener(listener);
        tvRecharge.setOnClickListener(listener);
        btnSendGift.setOnClickListener(listener);
        btnSendGiftMulti.setOnClickListener(listener);
        view.setOnClickListener(listener);

        if (!isInRoom) {
            //改颜色
            vGift.setBackgroundColor(ResourceHelper.getColor(R.color.white));
            ivLineTop.setBackgroundColor(ResourceHelper.getColor(R.color.colorC3C3C3));
            ivLineBottom.setBackgroundColor(ResourceHelper.getColor(R.color.colorECECEC));
            tvGiftKazuan.setTextColor(ResourceHelper.getColor(R.color.color2E2E2E));
            for (GiftGridRcvAdapter adapter : giftAdapters) {
                adapter.setInRoom(false);
                adapter.notifyDataSetChanged();
            }
            itemDecorationRes = R.color.colorECECEC;
            RecyclerViewItemDecoration itemDecoration2 = new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_GRID, ResourceHelper.getColor(itemDecorationRes), 1, 0, 0);
            for (View view : giftViews) {
                RecyclerView rcv = (RecyclerView) view.findViewById(R.id.gridview);
                rcv.removeItemDecoration(itemDecoration);
                rcv.addItemDecoration(itemDecoration2);
            }
            circleNorRes = R.drawable.dot_circle_7777777;
            circleSelRes = R.drawable.dot_circle_7777777_full;
            addDots(pageSize);
        }
    }


    public void show() {
//        if (Utils.listIsNullOrEmpty(giftList)) {
//            ToastHelper.showToast("没有礼物,请稍候");
//            GiftResManager.getInstance().checkGiftUpdate();
//            hide();
//            showOtherElement();
//            return;
//        }

        if (isNeedChooseDefault) {
            isNeedChooseDefault = false;
            if (!Utils.listIsNullOrEmpty(giftList)) {
                setChooseGift(giftList.get(0));
            }
        }
        setVisibility(View.VISIBLE);
        if (!Utils.listIsNullOrEmpty(giftViews)) {
            RecyclerView gv = (RecyclerView) giftViews.get(curPage).findViewById(R.id.gridview);
            GiftGridRcvAdapter adapter = (GiftGridRcvAdapter) gv.getAdapter();
            adapter.isShowing = true;
            adapter.notifyDataSetChanged();
        }

        //防止礼物框消失没显示
        if (btnSendGiftMulti.getVisibility() != View.VISIBLE) {
            llDots.setVisibility(View.VISIBLE);
            ivLineBottom.setVisibility(View.VISIBLE);
            ivLineTop.setVisibility(View.VISIBLE);
            vpGift.setVisibility(View.VISIBLE);
            vGift.setBackgroundColor(ResourceHelper.getColor(R.color.color1B1B2360));
        }
    }

    public void hide() {
        setVisibility(View.GONE);
        if (!Utils.listIsNullOrEmpty(giftViews)) {
            RecyclerView gv = (RecyclerView) giftViews.get(curPage).findViewById(R.id.gridview);
            GiftGridRcvAdapter adapter = (GiftGridRcvAdapter) gv.getAdapter();
            adapter.isShowing = false;
            adapter.notifyDataSetChanged();
        }
    }

    private void initGiftList() {
        itemDecoration = new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_GRID, context.getResources().getColor(R.color.white10), 1, 0, 0);
        for (int i = 0; i < pageSize; i++) {
            View gv = getGridChildView(i);
            giftViews.add(gv);
        }
        vpGift.setAdapter(new ExpressionPagerAdapter(giftViews));
        vpGift.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                Log.d(TAG, " onPageSelected=" + index);
                curPage = index;
                refreshDots(index);
                for (int i = 0; i < pageSize; i++) {
                    RecyclerView gv = (RecyclerView) giftViews.get(i).findViewById(R.id.gridview);
                    GiftGridRcvAdapter adapter = (GiftGridRcvAdapter) gv.getAdapter();
                    if (i == curPage) {
                        adapter.isShowing = true;
                    } else {
                        adapter.isShowing = false;
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        initDots();

    }

    private void refreshDots(int pos) {
        if (tempImageView != null) {
            tempImageView.setBackgroundResource(circleNorRes);// message_read
            tempImageView.setLayoutParams(lpSmall);
        }
        imageViews[pos].setBackgroundResource(circleSelRes);// message_no_read
        imageViews[pos].setLayoutParams(lpBig);
        tempImageView = imageViews[pos];
    }

    private void initDots() {
        dotWidth = Utils.dip2px(context, 5);
        viewDots = (LinearLayout) view.findViewById(R.id.ll_dots);
        // 组装imageView列表
        // 把要轮播的图片添加到列表里调用setViewPagerViews方法添加到自定义的Viewpager里面就可以使用了ßß
        lpBig = new LinearLayout.LayoutParams(dotWidth, dotWidth);
        // lpBig.setMargins(5, 5, 5, 5);
        // lpBig = new LayoutParams(12, 12);
        lpBig.setMargins(dotWidth, dotWidth, dotWidth, dotWidth);//5
        lpBig.gravity = Gravity.CENTER_VERTICAL;
        lpSmall = new LinearLayout.LayoutParams(dotWidth, dotWidth);
        // lpSmall.setMargins(5, 5, 5, 5);
        // lpSmall = new LayoutParams(8, 8);
        lpSmall.setMargins(dotWidth, dotWidth, dotWidth, dotWidth);
        lpSmall.gravity = Gravity.CENTER_VERTICAL;
//        pager.setViewPagerViews(views);
        addDots(pageSize);
    }

    private void addDots(int size) {
        if (viewDots == null) {
            return;
        }
        viewDots.removeAllViews();
        for (int i = 0; i < size; i++) {
            imageView = new ImageView(context);
            // 设置小圆点imageview的参数
            imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(
                    context, 5), Utils.dip2px(context, 5)));// 创建一个宽高均为20
            // 的布局
//            imageView.setPadding(dotWidth, 0,
//                    dotWidth, 0);
            // 将小圆点layout添加到数组中
            Log.d("test", "imageView::" + imageView);
            Log.d("test", "imageViews[i]::" + imageViews[i]);
            imageViews[i] = imageView;
            // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                imageViews[i].setBackgroundResource(circleSelRes);// message_no_read
                imageViews[i].setLayoutParams(lpBig);
                tempImageView = imageViews[i];
            } else {
                imageViews[i].setBackgroundResource(circleNorRes);// message_read
                imageViews[i].setLayoutParams(lpSmall);
            }
            // 将imageviews添加到小圆点视图组
            viewDots.addView(imageViews[i]);
        }
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "mouse_" + x;// biz_screenshot_attitude_pure_
            reslist.add(filename);
        }
        return reslist;
    }


    /**
     * 获取礼物的gridview的子view
     *
     * @param
     * @return
     */
    RecyclerViewItemDecoration itemDecoration;

    private View getGridChildView(final int page) {
        View view = View.inflate(context, R.layout.gift_gridview, null);
        RecyclerView gv = (RecyclerView) view.findViewById(R.id.gridview);
        GridLayoutManager mgr = new GridLayoutManager(context, 8);
        gv.setLayoutManager(mgr);
        gv.addItemDecoration(itemDecoration);
        final List<GiftInfo> list = new ArrayList<>();

        for (int i = page * 8; i < page * 8 + 8; i++) {
            if (giftList.size() > i) {
                list.add(giftList.get(i));
            } else {
                break;
            }
        }

        GiftGridRcvAdapter expressionAdapter = new GiftGridRcvAdapter(context, list);
        gv.setAdapter(expressionAdapter);
        expressionAdapter.setOnItemClickLitener(new GiftGridRcvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                GiftInfo gift = list.get(position);// (GiftInfo) parent.getAdapter().getItem(position);
                HashMap<String, String> parmas = new HashMap<>();
                parmas.put("ID", gift.getId() + "");
                if (fromPage == PAGE_CHAT_MESSAGE) {
                    AnalyticsReport.onEvent(context, LiveReport.MY_LIVE_EVENT_15009, parmas);
                } else {
                    AnalyticsReport.onEvent(context, LiveReport.MY_LIVE_EVENT_11244, parmas);
                }

                if (btnSendGiftMulti.getVisibility() == View.VISIBLE) {
                    hideMultiGiftMode();
                }
                Log.d(TAG, "点击了礼物 gift=" + gift.getId() + " name=" + gift.getName()
                        + " position=" + position);
                setChooseGift(gift);
            }
        });
        giftAdapters.add(expressionAdapter);

        return view;
    }


    /**
     * 选中礼物
     * @param gift
     */
    private void setChooseGift(GiftInfo gift) {

        curChooseGift = gift;

        if (!gift.isChoose()) {

            for (GiftInfo giftModel : giftList) {
                if (gift.getId() == giftModel.getId()) {

                    giftModel.setChoose(true);
                    if (giftListener != null) {
                        giftListener.setChooseGift(giftModel);
                    }

                } else {
                    giftModel.setChoose(false);
                }
            }
            for (GiftGridRcvAdapter adapter : giftAdapters) {
                adapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 打开特效模式
     */
    public void showSpecialGiftMode() {
        GiftInfo specialGift1 = curChooseGift.getSpecialGiveInfos().get(0);
        GiftInfo specialGift2 = curChooseGift.getSpecialGiveInfos().get(1);
        GiftInfo specialGift3 = curChooseGift.getSpecialGiveInfos().get(2);

        //重置mPbSpecial
        mPbSpecial.initProgress(specialGift1.getNum(), specialGift2.getNum(), specialGift3.getNum());
        llSpecial.setVisibility(View.VISIBLE);
        llKazuan.setVisibility(View.INVISIBLE);
        slashView.initProgress(specialGift1.getNum(), specialGift2.getNum(), specialGift3.getNum());
    }

    /**
     * 关闭特效模式
     */
    public void hideSpecialGiftMode() {
        llSpecial.setVisibility(View.GONE);
        llKazuan.setVisibility(View.VISIBLE);
        slashView.stopRoll();
    }

    /**
     * 打开连送模式
     */
    public void showMultiGiftMode() {
        btnSendGift.setVisibility(View.INVISIBLE);
        btnSendGiftMulti.setVisibility(View.VISIBLE);
        btnSendGiftMulti.startTimer();

        if (curChooseGift != null && !Utils.listIsNullOrEmpty(curChooseGift.getSpecialGiveInfos())) {
            //玫瑰特效
            showSpecialGiftMode();
        }

    }

    /**
     * 关闭连送模式
     */
    public void hideMultiGiftMode() {
        btnSendGift.setVisibility(View.VISIBLE);
        btnSendGiftMulti.setVisibility(View.GONE);
        btnSendGiftMulti.stopTimer();
        if (llSpecial.getVisibility() == View.VISIBLE) {
            hideSpecialGiftMode();
        }
        showOtherElement();
    }

    public void startSendGiftMultiTimer() {
        if (mPbSpecial.getVisibility() == View.VISIBLE) {
            Log.d(TAG, "startSendGiftMultiTimer 加一次");
        }

        btnSendGiftMulti.startTimer();
    }

    public void setBalance(double coins) {
//        Log.d(TAG,"REFRESH_LAST_KAZUAN tvGiftKazuan.setText coins="+coins);
        tvGiftKazuan.setText(String.valueOf(coins)+"味豆");
    }


    public interface GiftGridListener {
        void setChooseGift(GiftInfo gift);
    }

    public void refreshSpecialBarProgress(BytesReader.GiftMessage message) {
        if (GiftResManager.getInstance().isSpecialAnimGift(message.giftID)) {
            if (mPbSpecial.getVisibility() == View.VISIBLE) {
                mPbSpecial.setAmount(message.count);
                slashView.setAmount(message.count);
            }
            if (message.count >= 20) {
                hideOtherElement();
            }

        }

    }

    private void hideOtherElement() {
        Log.d(TAG, "连接数达到20隐藏其他元素");
        llDots.setVisibility(View.INVISIBLE);
        ivLineBottom.setVisibility(View.INVISIBLE);
        ivLineTop.setVisibility(View.INVISIBLE);
        vpGift.setVisibility(View.INVISIBLE);
        vGift.setBackgroundColor(ResourceHelper.getColor(R.color.transparent));
        EventBusManager.postEvent(true, SubcriberTag.ROOM_SHOW_COMMENT);
    }

    private void showOtherElement() {
        if (vpGift.getVisibility() != View.VISIBLE) {
            llDots.setVisibility(View.VISIBLE);
            ivLineBottom.setVisibility(View.VISIBLE);
            ivLineTop.setVisibility(View.VISIBLE);
            vpGift.setVisibility(View.VISIBLE);
            vGift.setBackgroundColor(ResourceHelper.getColor(R.color.color1B1B2360));
        }
        if (getVisibility() == View.VISIBLE) {
            EventBusManager.postEvent(false, SubcriberTag.ROOM_SHOW_COMMENT);
        }
    }
}
