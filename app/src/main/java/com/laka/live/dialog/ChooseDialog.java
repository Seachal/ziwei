package com.laka.live.dialog;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.widget.Effects.EffectsType;
import com.laka.live.util.ResourceHelper;


/**
 * 所有,有提供用户选择的(最少两个选择及以上),都通用这个对话框。
 */
public class ChooseDialog extends BaseDialog implements View.OnClickListener {

    private TextView commit;
    private TextView cancel;
    private TextView title;
    private TextView prompt;

    // 回调
    private View.OnClickListener callback;

    public ChooseDialog(FragmentActivity context, View.OnClickListener callback) {
        super(context);
        setContentView(R.layout.dialog_choose, CENTER);

        setAnimator(EffectsType.FadeIn, EffectsType.FadeOut);

        this.callback = callback;
        title = (TextView) findViewById(R.id.title);
        prompt = (TextView) findViewById(R.id.prompt);
        commit = (TextView) findViewById(R.id.dialog_commit);
        cancel = (TextView) findViewById(R.id.dialog_cancel);
        cancel.setOnClickListener(this);
        commit.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isButtonCancel = true;
                dismiss();
            }
        });
    }

    public ChooseDialog setPromptGravity(int gravity) {
        prompt.setGravity(gravity);
        return this;
    }

    //设置标题
    public ChooseDialog setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    //设置提示内容
    public ChooseDialog setPrompt(String prompt) {
        this.prompt.setText(prompt);
        return this;
    }

    @Override
    public void onClick(View view) {
        callback.onClick(view);
    }

    public ChooseDialog setLeftBtnVisible(boolean isForceUpdate) {

        if (isForceUpdate) {
            cancel.setVisibility(View.GONE);
        } else {
            cancel.setVisibility(View.VISIBLE);
        }

        return this;
    }

    public ChooseDialog setLeftBtnText(String text) {
        cancel.setText(text);
        return this;
    }

    public ChooseDialog setLeftBtnTextColor(int resId) {
        cancel.setTextColor(ResourceHelper.getColor(resId));
        return this;
    }

    public ChooseDialog setLeftBtnBackGround(int resId) {
        cancel.setBackgroundResource(resId);
        return this;
    }

    public ChooseDialog setRightBtnTextColor(int resId) {
        commit.setTextColor(ResourceHelper.getColor(resId));
        return this;
    }

    public ChooseDialog setRightBtnText(String text) {
        commit.setText(text);
        return this;
    }

    public ChooseDialog setLeftBtnListener(View.OnClickListener listener) {
        cancel.setOnClickListener(listener);
        return this;
    }

    public ChooseDialog setRightBtnListener(final View.OnClickListener listener) {
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
                dismiss();
            }
        });
        return this;
    }

}
