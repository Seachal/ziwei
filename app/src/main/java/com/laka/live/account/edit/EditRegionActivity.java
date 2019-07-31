package com.laka.live.account.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laka.live.R;
import com.laka.live.bean.City;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.RegionAdapter;
import com.laka.live.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditRegionActivity extends BaseActivity implements RegionAdapter.OnItemClickListener {

    private final static int REQUEST_CODE = 0;
    private final static String EXTRA_CITIES = "EXTRA_CITIES";
    public final static String EXTRA_FATHER = "EXTRA_FATHER";
    public final static String EXTRA_CITY_NAME = "EXTRA_CITY_NAME";
    private final static String  CITY_FILE = "city.json";
    private final static String CHARSET_NAME = "utf-8";

    private String mFather;

    public static void startActivityForResult(Activity activity, int requestCode) {
        startActivityForResult(activity, null, null,requestCode);
    }

    public static void startActivityForResult(Activity activity, String father ,String [] ciyies, int requestCode) {
        if (activity != null) {
            Intent intent = new Intent(activity, EditRegionActivity.class);
            intent.putExtra(EXTRA_FATHER, father);
            intent.putExtra(EXTRA_CITIES, ciyies);
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_region);

        init();
    }

    private void init() {
        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        RegionAdapter regionAdapter = new RegionAdapter(this);
        list.setAdapter(regionAdapter);
        regionAdapter.addAll(loadData());

        regionAdapter.setOnItemClickListener(this);
    }

    private List<City> loadData() {
        Intent intent = getIntent();
        String [] cityNames = intent.getStringArrayExtra(EXTRA_CITIES);
        mFather = intent.getStringExtra(EXTRA_FATHER);
        List<City> cities;
        if (cityNames == null) {
            cities = loadFromJson();
        } else {
            cities = fromExtra(cityNames);
        }
        return cities;
    }

    private List<City> loadFromJson() {
        StringBuilder sb = null;
        List<City> cities = null;
        try {
            InputStream inputStream = getAssets().open(CITY_FILE);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            sb = new StringBuilder();
            byte buffer [] = new byte[1024];
            int len = 0;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len, CHARSET_NAME));
            }
        } catch (IOException e) {
            Log.error("test", "ex ", e);
        }
        String jsonStr = sb.toString();
        Log.debug("test", "city jsonStr : " + jsonStr);
        if (sb != null) {
            Gson gson = new Gson();
            cities = gson.fromJson(jsonStr, new TypeToken<List<City>>(){}.getType());
        }
        return cities;
    }

    private List<City> fromExtra(String [] cityNames) {
        List<City> cities = new ArrayList<>(cityNames.length);
        for (String name : cityNames) {
            if (TextUtils.isEmpty(name) == false) {
                City city = new City();
                city.setName(name);
                cities.add(city);
            }
        }
        return cities;
    }

    private long mLastClickTime = 0 ;
    @Override
    public void onItemClick(City city) {
        long currentTime = System.currentTimeMillis() ;
        if (currentTime - mLastClickTime <= 500 ){
            mLastClickTime = currentTime ;
            return;
        }
        if (city.hasSub()) {
            List<String> subCityNames = city.getCities();
            String [] cityNames = new String[subCityNames.size()];
            city.getCities().toArray(cityNames);
            EditRegionActivity.startActivityForResult(this, city.getName() ,cityNames, REQUEST_CODE);
        } else {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CITY_NAME, city.getName());
            intent.putExtra(EXTRA_FATHER, mFather);
            setResult(RESULT_OK, intent);
            finish();
        }

        mLastClickTime = currentTime ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
