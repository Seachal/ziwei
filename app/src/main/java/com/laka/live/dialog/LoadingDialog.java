package com.laka.live.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.laka.live.R;


/**
 * Created by Administrator on 2016/7/20.
 */
public class LoadingDialog extends Dialog{



    private TextView loadingTip;

    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_dialog_loading);

        loadingTip = (TextView) findViewById(R.id.loadingTip);

    }

    public TextView getLoadingTip() {
        return loadingTip;
    }

    /**
     * 设置加载提示内容
     */
    public void setLoadingTip(String tipText) {
         loadingTip.setText(tipText);
    }
}
