package com.laka.live.shopping.goods.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.photopreview.PhotoPreviewInfo;
import com.laka.live.shopping.CommunityConstant;
import com.laka.live.shopping.OpenPostDetailHelper;
import com.laka.live.shopping.bean.ShoppingCommentBean;
import com.laka.live.shopping.bean.ShoppingCommentImageBean;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhz on 2016/5/10.
 * Email: linhaizhong@ta2she.com
 */
public class GoodsCommentsLayout extends LinearLayout {
    public static final int MAX_COMMENTS = 6;

    private CommentsLayoutListener mListener;

    public GoodsCommentsLayout(Context context) {
        this(context, null);
    }

    public GoodsCommentsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setListener(CommentsLayoutListener listener) {
        mListener = listener;
    }

    public void setupCommentsLayout(List<ShoppingCommentBean> list) {
        removeAllViewsInLayout();
        if (list == null || list.isEmpty()) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        int size = Math.min(list.size(), MAX_COMMENTS);
        for (int i = 0; i < size; i++) {
            addCommentItem(list.get(i));
        }

        if (list.size() > MAX_COMMENTS) {
            addMoreButton();
        }

    }

    private void addCommentItem(final ShoppingCommentBean bean) {
        View view = View.inflate(getContext(), R.layout.shopping_goods_comment_item_layout, null);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCommentItemClick(bean);
            }
        });
        this.addView(view);
        RippleEffectHelper.addRippleEffectInView(view);

        SimpleDraweeView userIcon = (SimpleDraweeView) view.findViewById(R.id.shopping_goods_comment_item_user_icon);
        String url = bean.getIconUrl();
        if (StringUtils.isNotEmpty(url)) {
            userIcon.setImageURI(Uri.parse(url));
        } else {
            userIcon.setImageURI(Uri.parse("res://drawable/" + R.drawable.blank_icon_avatar));
        }

        TextView useName = (TextView) view.findViewById(R.id.shopping_goods_comment_item_user_name);
        String nameText = bean.getNickName();
        String ageText = bean.getAge();
        if (ageText != null) {
            nameText = nameText + " | " + ageText;
        }
        String marriage = bean.getMarriage();
        if (marriage != null) {
            nameText = nameText + " | " + marriage;
        }
        if (StringUtils.isNotEmpty(nameText)) {
            useName.setText(nameText);
        } else {
            useName.setText(R.string.shopping_product_anony);
        }

        TextView createTime = (TextView) view.findViewById(R.id.shopping_goods_comment_item_time);
        createTime.setText(bean.getCreateTime());

        TextView contentView = (TextView) view.findViewById(R.id.shopping_goods_comment_item_text);
        contentView.setText(bean.getContent());

        View imagesLayout = view.findViewById(R.id.shopping_goods_comment_item_image_layout);
        final List<ShoppingCommentImageBean> imageBeanList = bean.getImages();
        if (imageBeanList == null || imageBeanList.isEmpty()) {
            imagesLayout.setVisibility(View.GONE);
        } else {
            imagesLayout.setVisibility(View.VISIBLE);
            SimpleDraweeView image1 = (SimpleDraweeView) view.findViewById(R.id.shopping_goods_comment_item_image_1);
            SimpleDraweeView image2 = (SimpleDraweeView) view.findViewById(R.id.shopping_goods_comment_item_image_2);
            SimpleDraweeView image3 = (SimpleDraweeView) view.findViewById(R.id.shopping_goods_comment_item_image_3);

            final String url1 = imageBeanList.get(0).getThumbImageUrl();
            if (url1 != null) {
                image1.setVisibility(View.VISIBLE);
                image1.setImageURI(Uri.parse(url1));
            } else {
                image1.setVisibility(View.INVISIBLE);
            }
            image1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleImageClick(imageBeanList, url1);
                }
            });

            if (imageBeanList.size() >= 2) {
                final String url2 = imageBeanList.get(1).getThumbImageUrl();
                if (url2 != null) {
                    image2.setVisibility(View.VISIBLE);
                    image2.setImageURI(Uri.parse(url2));
                } else {
                    image2.setVisibility(View.INVISIBLE);
                }
                image2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleImageClick(imageBeanList, url2);
                    }
                });
            } else {
                image2.setVisibility(View.INVISIBLE);
            }

            if (imageBeanList.size() >= 3) {
                final String url3 = imageBeanList.get(2).getThumbImageUrl();
                if (url3 != null) {
                    image3.setVisibility(View.VISIBLE);
                    image3.setImageURI(Uri.parse(url3));
                    image3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleImageClick(imageBeanList, url3);
                        }
                    });
                } else {
                    image3.setVisibility(View.INVISIBLE);
                }
            } else {
                image3.setVisibility(View.INVISIBLE);
            }
        }

        String myScoreStr = bean.getMyScore();
        RatingBar myScoreRatingBar = (RatingBar) view.findViewById(R.id.shopping_goods_comment_item_ratting_self);
        myScoreRatingBar.setRating(StringUtils.parseFloat(myScoreStr));

        TextView viewNum = (TextView) view.findViewById(R.id.shopping_goods_comment_item_view_num);
        viewNum.setText(bean.getReadCount());

        TextView commentNum = (TextView) view.findViewById(R.id.shopping_goods_comment_item_comment_num);
        commentNum.setText(bean.getReplyCount());

    }

    private void addMoreButton() {
        FrameLayout parent = new FrameLayout(getContext());
        parent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMoreCommentClick();
                }
            }
        });
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                ResourceHelper.getDimen(R.dimen.space_80));
        this.addView(parent, lp);
        RippleEffectHelper.addRippleEffectInView(parent);

        TextView textView = new TextView(getContext());
        textView.setText(R.string.shopping_goods_comments_more);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_13));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.community_details_next, 0);
        textView.setCompoundDrawablePadding(ResourceHelper.getDimen(R.dimen.space_10));
        textView.setTextColor(ResourceHelper.getColor(R.color.shopping_goods_comments_text_color));
        textView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams childLp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        childLp.gravity = Gravity.CENTER;
        parent.addView(textView, childLp);


    }

    private void handleImageClick(List<ShoppingCommentImageBean> imageBeanList, String curPath) {
        ArrayList<String> imageList = new ArrayList<>(imageBeanList.size());
        int position = 0;
        for (int i = 0; i < imageBeanList.size(); i++) {
            ShoppingCommentImageBean bean = imageBeanList.get(i);
            if (curPath.equals(bean.getThumbImageUrl())) {
                position = i;
            }
            imageList.add(bean.getThumbImageUrl());
        }

        PhotoPreviewInfo info = new PhotoPreviewInfo();
        info.photoList = imageList;
        info.position = position;

        Message msg = Message.obtain();
        msg.what = MsgDef.MSG_SHOW_PHOTO_PREVIEW_WINDOW;
        msg.obj = info;
        MsgDispatcher.getInstance().sendMessage(msg);

    }

    private void handleCommentItemClick(ShoppingCommentBean bean) {
        int id = StringUtils.parseInt(bean.getPostId());
        if (id == 0) {
            return;
        }
        OpenPostDetailHelper helper = new OpenPostDetailHelper();
        helper.setPostId(id);
        helper.setPostTypeFromWhere(CommunityConstant.POST_TYPE_FROM_POST);

        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_COMMUNITY_POST_DETAIL_WINDOW;
        message.arg1 = id;
        message.obj = helper;
        MsgDispatcher.getInstance().sendMessage(message);

//        StatsModel.stats(StatsKeyDef.RPODUCT_DETAIL_COMMENTS);
    }

    public interface CommentsLayoutListener {
        void onMoreCommentClick();
    }
}
