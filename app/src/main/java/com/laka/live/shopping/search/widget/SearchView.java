package com.laka.live.shopping.search.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;


import com.laka.live.R;
import com.laka.live.shopping.search.adapter.SearchHistoryAdapter;
import com.laka.live.shopping.search.info.SearchInfo;
import com.laka.live.shopping.search.model.SearchHistoryCallback;
import com.laka.live.shopping.search.model.SearchHistoryInfo;
import com.laka.live.shopping.search.model.SearchHistoryManager;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gangqing on 2016/4/25.
 * Email:denggangqing@ta2she.com
 */
public class SearchView extends FrameLayout implements AdapterView.OnItemClickListener, SearchHistoryAdapter.AdapterCallBack {
    private SearchHistoryManager mHistoryManager;

    private SearchViewCallBack mCallBack;
    private ListView mListView;
    private SearchHistoryAdapter mAdapter;
    private List<SearchInfo> mSearchInfoList = new ArrayList<>();

    public SearchView(Context context) {
        super(context);
        initView();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setBackgroundColor(ResourceHelper.getColor(R.color.white));
        mHistoryManager = new SearchHistoryManager(getContext());
        mListView = new ListView(getContext());
        mListView.setBackgroundColor(ResourceHelper.getColor(R.color.white));
        mListView.setVerticalScrollBarEnabled(false);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int dimen = ResourceHelper.getDimen(R.dimen.space_15);
        params.setMargins(dimen, 0, dimen, dimen);
        addView(mListView, params);

        mAdapter = new SearchHistoryAdapter(getContext(), mSearchInfoList);
        mAdapter.setCallBack(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public void addHistoryInfo(String text) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        text = text.trim();
        SearchInfo info = null;
        for (SearchInfo tmpInfo : mSearchInfoList) {
            if (tmpInfo.historyInfo != null && text.equals(tmpInfo.historyInfo.text)) {
                info = tmpInfo;
                break;
            }
        }
        if (info != null) {
            mSearchInfoList.remove(info);
        } else {
            info = new SearchInfo();
            SearchHistoryInfo historyInfo = new SearchHistoryInfo();
            historyInfo.text = text;
            info.historyInfo = historyInfo;
        }
        if (mSearchInfoList.size() > 0) {
            mSearchInfoList.add(1, info);
        } else {
            mSearchInfoList.add(0, info);
        }
        mAdapter.notifyDataSetChanged();
        mHistoryManager.addSearchHistory(text);
    }

    public void addHotSearchData(List<String> list) {
        SearchInfo info = new SearchInfo();
        info.viewType = SearchInfo.VIEW_TYPE_HOT;
        info.HotSearchList = list;
        mSearchInfoList.add(0, info);
        mAdapter.notifyDataSetChanged();
    }

    public void tryLoadHistoryData() {
        SearchHistoryCallback callback = new SearchHistoryCallback() {
            @Override
            public void onGetAllHistoryResult(ArrayList<SearchHistoryInfo> list) {
                if (list == null) {
                    return;
                }
                for (SearchHistoryInfo info : list) {
                    SearchInfo searchInfo = new SearchInfo();
                    searchInfo.viewType = SearchInfo.VIEW_TYPE_HISTORY;
                    searchInfo.historyInfo = info;
                    mSearchInfoList.add(searchInfo);
                }
                if (list.size() != 0) {
                    SearchInfo deleteInfo = new SearchInfo();
                    deleteInfo.viewType = SearchInfo.VIEW_TYPE_DELETE;
                    mSearchInfoList.add(deleteInfo);
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        mHistoryManager.getAllHistoryInfo(callback);
    }

    private void showDeleteHistoryDialog() {
        SimpleTextDialog dialog = new SimpleTextDialog(getContext());
        dialog.addTitle(R.string.search_history_delete);
        dialog.setText(R.string.search_history_delete_confirm);
        dialog.addYesNoButton(ResourceHelper.getString(R.string.cancel), ResourceHelper.getString(R.string.yes));
        dialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
        dialog.setOnClickListener(new IDialogOnClickListener() {
            @Override
            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                if (viewId == GenericDialog.ID_BUTTON_NO) {
                    notifyHistoryClear();
                    mHistoryManager.clearHistory();
                }
                return false;
            }
        });
        dialog.show();
    }

    public void notifyHistoryClear() {
        Iterator<SearchInfo> iterator = mSearchInfoList.iterator();
        List<SearchInfo> bufferList = new ArrayList<>();
        while (iterator.hasNext()) {
            SearchInfo next = iterator.next();
            if (next.viewType == SearchInfo.VIEW_TYPE_HISTORY || next.viewType == SearchInfo.VIEW_TYPE_DELETE) {
                bufferList.add(next);
            }
        }
        mSearchInfoList.removeAll(bufferList);
        mAdapter.notifyDataSetChanged();
    }

    private void search(String content) {
        if (mCallBack != null) {
            mCallBack.onSearch(content);
        }
    }

    public void setSearchViewCallBack(SearchViewCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSearchInfoList.get(position).viewType == SearchInfo.VIEW_TYPE_HISTORY) {
//            StatsModel.stats(StatsKeyDef.SEARCH_HISTORY);
            search(mSearchInfoList.get(position).historyInfo.text);
        } else if (mSearchInfoList.get(position).viewType == SearchInfo.VIEW_TYPE_DELETE) {
            showDeleteHistoryDialog();
        }
    }

    @Override
    public void onHotSearchItemClick(String content) {
//        StatsModel.stats(StatsKeyDef.SEARCH_HOT, StatsKeyDef.KEY_SEARCH_HOT, content);
        search(content);
    }

    public interface SearchViewCallBack {
        void onSearch(String content);
    }
}
