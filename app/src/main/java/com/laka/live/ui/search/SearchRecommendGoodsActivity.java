package com.laka.live.ui.search;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.msg.Msg;
import com.laka.live.msg.SearchRecommendGoodsMsg;
import com.laka.live.msg.SearchHotKeywordMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.SearchGoodsAdapter;
import com.laka.live.ui.widget.FlowLayout;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lyf on 2017/7/21.
 */

public class SearchRecommendGoodsActivity extends BaseActivity implements TextView.OnEditorActionListener,
        View.OnClickListener, SearchHistoryAdapter.OnItemClickListener {

    private final static String SEARCH_TYPE = "serch_type";
    private final static String TYPE_SEARCH_FROM_INPUT = "0";
    private final static String TYPE_SEARCH_FROM_INPUT_ID = "1";
    private final static String TYPE_SEARCH_FROM_INPUT_WORD = "2";
    private final static String TYPE_SEARCH_FROM_HOT = "3";
    private final static String TYPE_SEARCH_FROM_HISTORY = "4";
    private final static int DEFAULT_DURATION = 200;


    private PageListLayout mSearchResult;

    private TextView mSearch;
    private EditText mSearchInput;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private String mKeyword;
    private SearchGoodsAdapter mResultAdapter;
    private ImageView mClearInputButton;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, SearchRecommendGoodsActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    public static void startActivity(Activity activity, ArrayList<ShoppingGoodsBaseBean> checkedList) {
        if (activity != null) {
            Intent intent = new Intent(activity, SearchRecommendGoodsActivity.class);
            intent.putExtra("checkedList", checkedList);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods);
        init();
    }


    private void init() {
        findViewById(R.id.back_icon).setOnClickListener(this);
        mClearInputButton = (ImageView) findViewById(R.id.clear_button);
        mClearInputButton.setOnClickListener(this);

        mSearchResult = (PageListLayout) findViewById(R.id.search_result);
        mResultAdapter = new SearchGoodsAdapter(SearchGoodsAdapter.FROM_SEARCH_RECOMMEND_GOODS);
        if (getIntent().getSerializableExtra("checkedList") != null) {
            mResultAdapter.setCheckedList((ArrayList<ShoppingGoodsBaseBean>) getIntent().getSerializableExtra("checkedList"));
        }
        mSearchResult.setAdapter(mResultAdapter);
        mSearchResult.setLayoutManager(new LinearLayoutManager(this));
        mResultAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // handleOnSearchResultItemClick(position);
            }
        });

        // 默认page从1开始
        // mSearchResult.setDefaultPage(1);

        mSearchResult.setOnRequestCallBack(new PageListLayout.OnRequestCallBack() {
            @Override
            public String request(int page, GsonHttpConnection.OnResultListener listener) {
                return DataProvider.searchGoods(mContext, mKeyword, page, listener);
            }
        });

        mSearchResult.setOnResultListener(new PageListLayout.OnResultListener<SearchRecommendGoodsMsg>() {
            @Override
            public void onResult(SearchRecommendGoodsMsg msg) {


                if (msg == null || msg.isEmpty()) {
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.SEARCH_NULL_SHOW_EVENT_ID);
                } else {
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.SEARCH_NOT_NULL_SHOW_EVENT_ID);
                }
            }
        });


        mSearchHistoryAdapter = new SearchHistoryAdapter(this);
        mSearchHistoryAdapter.setOnItemClickListener(this);

        mSearch = (TextView) findViewById(R.id.cancel);
        mSearch.setOnClickListener(this);
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mSearchInput.setHint("请输入商品名称");
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handleOnInputContentChanged(s);
            }
        });

        mSearchInput.setOnEditorActionListener(this);
        mSearchInput.setOnClickListener(this);

        showSearchHome();
        loadHotKeyword();

        // 进来默认进行搜索(没输入搜索关键字，代表搜索默认列表)
        search(TYPE_SEARCH_FROM_INPUT);

    }

    private void loadHotKeyword() {
        DataProvider.getSearchHotKeyword(this, new GsonHttpConnection.OnResultListener<SearchHotKeywordMsg>() {
            @Override
            public void onSuccess(SearchHotKeywordMsg msg) {
                handleOnRequestResultHotTagSuccess(msg);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }

    private void handleOnRequestResultHotTagSuccess(SearchHotKeywordMsg msg) {
        if (msg.getCode() != Msg.TLV_OK) {
            return;
        }
        List<String> list = msg.getList();
        if (list == null || list.isEmpty()) {
            return;
        }
//        for (final String content : list) {
//            createHotTag(this, mHotTagLayout, content, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    search(content, TYPE_SEARCH_FROM_HOT);
//                    AnalyticsReport.onEvent(mContext, AnalyticsReport.SEARCH_HOT_WORD_ITEM_CLICK_EVENT_ID);
//                }
//            });
//        }
    }

    public static void createHotTag(Context context, FlowLayout flowLayout, final String content, View.OnClickListener listener) {
        TextView tagTextView = new TextView(context);
        tagTextView.setPadding(ResourceHelper.getDimen(R.dimen.space_5),
                ResourceHelper.getDimen(R.dimen.space_5),
                ResourceHelper.getDimen(R.dimen.space_5),
                ResourceHelper.getDimen(R.dimen.space_5));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = ResourceHelper.getDimen(R.dimen.space_5);
        layoutParams.rightMargin = ResourceHelper.getDimen(R.dimen.space_5);
        layoutParams.bottomMargin = ResourceHelper.getDimen(R.dimen.space_5);
        layoutParams.topMargin = ResourceHelper.getDimen(R.dimen.space_5);
        flowLayout.addView(tagTextView, layoutParams);
        tagTextView.setTextColor(ResourceHelper.getColorStateList(R.color.hot_tag_text_color_selector));
        tagTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_14));
        tagTextView.setBackgroundResource(R.drawable.hot_tag_bg_selector);
        tagTextView.setText(content);
        tagTextView.setOnClickListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                search(TYPE_SEARCH_FROM_INPUT);
                break;
            case R.id.back_icon:
                finish();
                break;
            case R.id.search_input:
                if (mSearchResult.getVisibility() == View.VISIBLE && mSearchResult.isEmpty()) {
                    AnalyticsReport.onEvent(this, AnalyticsReport.SEARCH_NULL_EDIT_CLICK_EVENT_ID);
                }
                break;
            case R.id.clear_button:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_11001);
                handleOnClearInputButton();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search(TYPE_SEARCH_FROM_INPUT);
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(String keyword) {
        search(keyword, TYPE_SEARCH_FROM_HISTORY);
    }

    @Override
    public void onClearItemClick() {

    }

    @Override
    public void onItemClearButtonClick(int size) {

    }

    private void handleOnClearInputButton() {
        if (mSearchInput == null) {
            return;
        }
        mSearchInput.setText("");
    }

    private void handleOnInputContentChanged(Editable editable) {
        if (editable == null) {
            return;
        }
        if (editable.toString().length() == 0) {
            mClearInputButton.setVisibility(View.INVISIBLE);
            return;
        }
        if (mClearInputButton.isShown()) {
            return;
        }
//        startShowActivity(mClearInputButton);
        mClearInputButton.setVisibility(View.VISIBLE);
    }

    private void startShowActivity(View view) {
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 0.5f, 1f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 0.5f, 1f);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setDuration(DEFAULT_DURATION);
        animationSet.play(objectAnimatorX).with(objectAnimatorY);
        animationSet.start();
    }

    private void search(String from) {
        String key = mSearchInput.getText().toString();
        search(key, from);

    }

    private void search(String keyword, String from) {
        if (keyword == null) {
            keyword = "";
        }
        mSearchHistoryAdapter.addItem(keyword);
        showSearchResult();
        mKeyword = keyword;
        mSearchInput.setText(mKeyword);
        mSearchInput.setSelection(mKeyword.length());
        mSearchResult.loadData(true);
        hideSoftInput(this);

        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.equals(from, TYPE_SEARCH_FROM_INPUT)) {
            try {
                StringUtils.parseInt(keyword);
                from = TYPE_SEARCH_FROM_INPUT_ID;
            } catch (NumberFormatException e) {
                from = TYPE_SEARCH_FROM_INPUT_WORD;
            }
        }
        params.put(SEARCH_TYPE, from);
        AnalyticsReport.onEvent(this, AnalyticsReport.SEARCH_BUTTON_CLICK_EVENT_ID, params);
    }

    private void showSearchHome() {
        mSearchResult.setVisibility(View.GONE);
    }

    private void showSearchResult() {
        mSearchResult.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSearchHistoryAdapter.save();
    }

}
