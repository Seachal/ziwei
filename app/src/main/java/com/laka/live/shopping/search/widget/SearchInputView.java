package com.laka.live.shopping.search.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.laka.live.R;
import com.laka.live.ui.widget.AlphaImageView;
import com.laka.live.util.StringUtils;


/**
 * Created by gangqing on 2016/4/21.
 * Email:denggangqing@ta2she.com
 */
public class SearchInputView extends FrameLayout {
    private InputViewListener mInputViewListener;
    private EditText mEditText;
    private boolean mIsNeedTextChangeSearch = false;

    public SearchInputView(Context context) {
        super(context);
        init();
    }

    public SearchInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setNeedTextChangedSearch(boolean isNeedTextChangeSearch) {
        mIsNeedTextChangeSearch = isNeedTextChangeSearch;
    }

    private void init() {
        View rootView = View.inflate(getContext(), R.layout.search_input_layout, null);
        //返回按钮
        AlphaImageView back = (AlphaImageView) rootView.findViewById(R.id.search_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputViewListener != null) {
                    mInputViewListener.onBackClick();
                }
            }
        });
        //删除按钮
        final AlphaImageView delete = (AlphaImageView) rootView.findViewById(R.id.search_delete);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
                if (mInputViewListener != null) {
                    mInputViewListener.onDeleteClick();
                }
            }
        });
        //输入框
        mEditText = (EditText) rootView.findViewById(R.id.search_content);
        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    search(mEditText.getText().toString(), true);
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    delete.setVisibility(VISIBLE);
                } else {
                    delete.setVisibility(GONE);
                }
                if (mIsNeedTextChangeSearch) {
                    String content = mEditText.getText().toString();
                    search(content, false);
                }
            }
        });
        //搜索按钮
        AlphaImageView search = (AlphaImageView) rootView.findViewById(R.id.search_ico);
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditText.getText().toString();
                search(content, true);
            }
        });
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(rootView, params);
    }

    private void search(String content, boolean isClickSearchIco) {
        if (mInputViewListener != null) {
            mInputViewListener.onSearchClick(content, isClickSearchIco);
        }
    }

    public void setHintText(String text) {
        mEditText.setHint(text);
    }

    public void setEditText(String text) {
        if (StringUtils.isNotEmpty(text)) {
            mEditText.setText(text);
            mEditText.setSelection(text.length());
        }
    }

    public void setSearchInputListener(InputViewListener listener) {
        mInputViewListener = listener;
    }

    public interface InputViewListener {
        void onSearchClick(String content, boolean isClickSearchIco);

        void onBackClick();

        void onDeleteClick();
    }
}
