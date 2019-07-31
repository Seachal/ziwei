package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.adapter.TransactionRecordAdapter;
import com.laka.live.ui.widget.PageListLayout;

/**
 * @ClassName: TransoDetailFragment
 * @Description: 交易明细
 * @Author: chuan
 * @Version: 1.0
 * @Date: 25/07/2017
 */

public class TransoDetailFragment extends BaseFragment implements PageListLayout.OnRequestCallBack {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transo_detail, null);
        PageListLayout listLayout = (PageListLayout) view.findViewById(R.id.page_list_layout);
        listLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        TransactionRecordAdapter adapter = new TransactionRecordAdapter();
        listLayout.setAdapter(adapter);
        listLayout.setOnRequestCallBack(this);
        listLayout.loadData();
        return view;
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.queryTransactionRecordListMsg(this, page, listener);
    }
}
