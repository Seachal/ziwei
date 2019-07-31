package com.laka.live.ui.widget.comment;

/**
 * @ClassName: ReplyListView
 * @Description: 评论回复控件
 * @Author: chuan
 * @Version: 1.0
 * @Date: 11/18/16
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.ReplyInfo;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReplyListView extends LinearLayout implements View.OnClickListener,
        View.OnLongClickListener {
    private static final String TAG = ReplyListView.class.getSimpleName();
    private int mItemColor;

    private OnItemClickListener mItemClickListener;  //回复项的单击事件
    private OnItemLongClickListener mItemLongClickListener;  //回复项的长按事件

    private List<ReplyInfo> mReplyList;  //回复列表

    /**
     * item点击事件监听器
     */
    public interface OnItemClickListener {
        void onItemClick(int itemPosition);
    }

    /**
     * item长按事件监听器
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public ReplyListView(Context context) {
        this(context, null);
    }

    public ReplyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    /**
     * 初始化属性
     *
     * @param attrs 属性参数
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ReplyListView, 0, 0);
        try {
            mItemColor = typedArray.getColor(R.styleable.ReplyListView_item_color,
                    ResourceHelper.getColor(R.color.colorAAAAAA));  //textview的默认颜色
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 创建view
     *
     * @param position 位置
     * @return 生成的view
     */
    private View getView(int position) {
        TextView commentTv = new TextView(getContext());
        commentTv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        commentTv.setTextColor(ResourceHelper.getColor(R.color.color1A1A1A));
        commentTv.setTextSize(14);
        commentTv.setLineSpacing(0f, 1.1f);

        ReplyInfo replyInfo = mReplyList.get(position);

        String nickName = replyInfo.getNickname();
        String toNickName = replyInfo.getToNickname();

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(nickName, replyInfo.getReplyId()));

        Log.d(TAG, " nickName=" + nickName + " toNickName=" + toNickName);
        if (!TextUtils.isEmpty(toNickName) && !toNickName.equals(nickName)) {
            builder.append(" ");
            builder.append(ResourceHelper.getString(R.string.reply));
            builder.append(" ");
            builder.append(setClickableSpan(toNickName, replyInfo.getUserId()));
        }

        builder.append(": ");

        builder.append(MoonUtil.replaceEmoticons(getContext(), replyInfo.getContent(), ImageSpan.ALIGN_BASELINE, MoonUtil.SMALL_SCALE));

        commentTv.setText(builder);
        commentTv.setTag(position);

        commentTv.setOnClickListener(this);
        commentTv.setOnLongClickListener(this);

        return commentTv;
    }

    /**
     * 绑定id和显示内容，处理字符串的点击事件
     *
     * @param textStr 显示内容
     * @param id      绑定id
     * @return SpannableString实体
     */
    @NonNull
    private SpannableString setClickableSpan(String textStr, int id) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(mItemColor) {
                                    @Override
                                    public void onClick(View widget) {

                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    /**
     * OnClickListener接口
     */
    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((int) v.getTag());
        }
    }

    /**
     * OnLongClickListener接口
     */
    @Override
    public boolean onLongClick(View v) {
        if (mItemLongClickListener != null) {
            mItemLongClickListener.onItemLongClick((int) v.getTag());
        }
        return true;
    }

    /**
     * 获取item点击事件监听器
     *
     * @return item点击事件监听器
     */
    public OnItemClickListener getOnItemClickListener() {
        return mItemClickListener;
    }

    /**
     * 设置item点击事件监听器
     *
     * @param itemClickListener item点击事件监听器
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * 获取item长按事件监听器
     *
     * @return item长按事件监听器
     */
    public OnItemLongClickListener getOnItemLongClickListener() {
        return mItemLongClickListener;
    }

    /**
     * 设置item长按事件监听器
     *
     * @param itemLongClickListener item长按事件监听器
     */
    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    /**
     * 得到数据
     *
     * @return 回复列表
     */
    public List<ReplyInfo> getDatas() {
        return mReplyList;
    }

    /**
     * 设置数据
     *
     * @param datas 回复列表
     */
    public void setDatas(List<ReplyInfo> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mReplyList = datas;
        notifyDataSetChanged();
    }

    /**
     * 数据变化时调用，更新界面显示
     */
    public void notifyDataSetChanged() {
        removeAllViews();

        if (Utils.listIsNullOrEmpty(mReplyList)) {
            return;
        }

        for (int i = 0; i < mReplyList.size(); i++) {
            View view = getView(i);

            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getmView()...");
            }

            addView(view, i);
        }
    }

}
