package com.laka.live.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.widget.SelectorButton;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/3/31.
 */

public class ConfirmDialog extends BaseDialog {

    @InjectView(id = R.id.commit)
    public TextView commit;
    @InjectView(id = R.id.cancel)
    public SelectorButton cancel;
    @InjectView(id = R.id.content)
    public TextView content;


    public ConfirmDialog(Context context, View.OnClickListener onClickListener) {
        super(context);
        setContentView(R.layout.dialog_confirm,BOTTOM);
        commit.setOnClickListener(onClickListener);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    // 设置显示的内容
    public void setContent(String content) {
        this.content.setText(content);
    }

}
