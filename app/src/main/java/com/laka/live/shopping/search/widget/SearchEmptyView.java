package com.laka.live.shopping.search.widget;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.laka.live.R;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.search.adapter.SearchResultEmptyAdapter;
import com.laka.live.shopping.search.bean.KeywordSearchTopList;
import com.laka.live.shopping.search.info.SearchResultEmptyInfo;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gangqing on 2016/4/22.
 * Email:denggangqing@ta2she.com
 */
public class SearchEmptyView extends FrameLayout implements AdapterView.OnItemClickListener, SearchResultEmptyAdapter.SearchResultEmptyAdapterCallBack {
    private ListView mListView;
    private ClassifyEmptyHotGoodsCallBack mCallBack;
    private List<SearchResultEmptyInfo> mEmptyDataList = new ArrayList<>();
    private SearchResultEmptyAdapter mAdapter;

    public SearchEmptyView(Context context) {
        super(context);
        initView();
    }

    public SearchEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mListView = new ListView(getContext());
        mListView.setDividerHeight(0);
        mListView.setPadding(0, 0, 0, ResourceHelper.getDimen(R.dimen.space_10));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, ResourceHelper.getDimen(R.dimen.space_10));
        addView(mListView, params);

        mAdapter = new SearchResultEmptyAdapter(getContext(), mEmptyDataList);
        mAdapter.setSearchResultEmptyAdapterCallBack(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public void setEmptyData(List<SearchResultEmptyInfo> list) {
        mEmptyDataList.clear();
        mEmptyDataList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mEmptyDataList.get(position).type == SearchResultEmptyInfo.view_type_random_article) {
//            StatsModel.stats(StatsKeyDef.SEARCH_FAIL_RANKLIST, StatsKeyDef.KEY_SEARCH_HOT, mEmptyDataList.get(position).randomTopList.toplistId);
            goToExternalLinks(mEmptyDataList.get(position).randomTopList);
        }
    }

    private void goToExternalLinks(KeywordSearchTopList data) {
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_SHOPPING_TOP_LIST_DETAIL_WINDOW;
        message.arg1 = StringUtils.parseInt(data.toplistId);
        MsgDispatcher.getInstance().sendMessage(message);
    }

    @Override
    public void onHotGoodsItemClick(View v) {
        if (mCallBack != null) {
            String content = ((TextView) v).getText().toString().trim();
//            StatsModel.stats(StatsKeyDef.SEARCH_FAIL_HOT, StatsKeyDef.KEY_SEARCH_HOT, content);
            mCallBack.onHotGoodsItemClick(v);
        }
    }

    public void setClassifyEmptyHotGoodsCallBack(ClassifyEmptyHotGoodsCallBack callBack) {
        mCallBack = callBack;
    }

    public interface ClassifyEmptyHotGoodsCallBack {
        void onHotGoodsItemClick(View v);
    }
}
