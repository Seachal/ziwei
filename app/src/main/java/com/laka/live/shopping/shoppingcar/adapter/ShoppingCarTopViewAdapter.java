package com.laka.live.shopping.shoppingcar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.shopping.shoppingcar.model.ShoppingCarTopViewInfo;
import com.laka.live.shopping.shoppingcar.widget.ShoppingCarTopTextView;

import java.util.List;

/**
 * Created by chenjiawei on 2016/3/25.
 */
public class ShoppingCarTopViewAdapter {
    private List<ShoppingCarTopViewInfo> mDatas;

    public ShoppingCarTopViewAdapter(List<ShoppingCarTopViewInfo> mDatas) {
        this.mDatas = mDatas;
    }

    /**
     * 获取数据的条数
     *
     * @return
     */
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取摸个数据
     *
     * @param position
     * @return
     */
    public ShoppingCarTopViewInfo getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 获取条目布局
     *
     * @param parent
     * @return
     */
    public View getView(ShoppingCarTopTextView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_car_top_main, null);
    }

    /**
     * 条目数据适配
     *
     * @param view
     * @param data
     */
    public void setItem(final View view, final ShoppingCarTopViewInfo data) {

        TextView tv = (TextView) view.findViewById(R.id.shopping_car_top_info);
        tv.setText(data.getTitle());

        //你可以增加点击事件
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //比如打开url
//                Toast.makeText(view.getContext(), data.url, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
