package com.laka.live.ui.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.HotTopicMsg;
import com.laka.live.msg.TopicListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import framework.ioc.annotation.InjectView;
import framework.utils.GsonTools;

public class AddTopicActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "AddTopicActivity";


    @InjectView(id = R.id.tip)
    public TextView tip;
    @InjectView(id = R.id.back)
    public ImageView back;
    @InjectView(id = R.id.save)
    public TextView save;

    private List<String> datats = new ArrayList<>();
    private RecyclerView mRecycleView;

    // private EditText mSearchInput;
    private HotTopicAdapter mAdapter;
    private List<String> selected;

    private final static int MAX_TOPICS = 3;

    public static void startActivity(Context activity, ArrayList<String> data) {
        if (activity != null) {
            Intent intent = new Intent(activity, AddTopicActivity.class);
            intent.putStringArrayListExtra("data", data);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        init();
    }


    private void init() {

        selected = getIntent().getStringArrayListExtra("data");

        if (selected == null) {
            selected = new ArrayList<>();
        }

        Log.log(GsonTools.toJson(selected));

        mRecycleView = (RecyclerView) findViewById(R.id.rcv_topic);
        GridLayoutManager mgr = new GridLayoutManager(this, 2);
        mRecycleView.setLayoutManager(mgr);
        mAdapter = new HotTopicAdapter(this, datats, selected);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new HotTopicAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String item = datats.get(position);
                if(selected.contains(item)) {
                    selected.remove(item);
                }else{
                    if (selected.size() >= MAX_TOPICS) {
                        selected.remove(0);
                    }
                    selected.add(item);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        tip.setText("最多只能选择" + MAX_TOPICS + "个话题");
        ViewUtils.setPartTextColor(tip, R.color.colorFF950B, 6, tip.getText().length() - 3);
        loadHotTopic();
    }



    private void loadHotTopic() {
        DataProvider.queryHotTopic(this, new GsonHttpConnection.OnResultListener<HotTopicMsg>() {
            @Override
            public void onSuccess(HotTopicMsg result) {
                List<String> list = result.getList();
                if (Utils.listIsNullOrEmpty(list)) {
                    Log.d(TAG, "loadHotTopic onSuccess null");
                } else {
                    Log.d(TAG, "loadHotTopic onSuccess size=" + list.size());
                    datats.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "loadHotTopic onFail");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                //确认选择
                AnalyticsReport.onEvent(AddTopicActivity.this, LiveReport.MY_LIVE_EVENT_11207);
                hideSoftInput(this);
                EventBusManager.postEvent(selected, SubcriberTag.ADD_TOPIC);
                finish();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            search(TYPE_SEARCH_FROM_INPUT);
//            return true;
//        }
//        return false;
//    }
//
//
//    private void search(String from) {
//        String key = mSearchInput.getText().toString();
//        search(key, from);
//
//    }
//
//    private void search(String keyword, String from) {
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11205);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
