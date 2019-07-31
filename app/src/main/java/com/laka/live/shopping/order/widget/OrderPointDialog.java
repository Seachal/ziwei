package com.laka.live.shopping.order.widget;

import android.content.Context;
import android.view.View;

import com.laka.live.R;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.util.ResourceHelper;


/**
 * Created by zhxu on 2016/1/5.
 * Email:357599859@qq.com
 */
public class OrderPointDialog extends GenericDialog {

    private View mView;

    private Context mContext;

    public OrderPointDialog(Context context, View view) {
        super(context);
        mView = view;
        mContext = context;

        initUI();
    }

    private void initUI() {
        setCanceledOnTouchOutside(false);
        String title = ResourceHelper.getString(R.string.order_main_ta_coin_title);
        setTitle(title);
        addContentView(mView);
        addYesNoButton();
        setRecommendButton(ID_BUTTON_YES);
    }
}
