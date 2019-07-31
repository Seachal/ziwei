package com.laka.live.ui.viewholder;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.bean.Topic;
import com.laka.live.constants.Constants;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.PostCourseAdapter;
import com.laka.live.ui.topic.AddTopicActivity;
import com.laka.live.ui.widget.WxCircleLoading;
import com.laka.live.util.FastBlur;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;

import java.util.ArrayList;

/**
 * Created by Lyf on 2017/9/1.
 */

public class PostCourseHeaderViewHolder extends BaseAdapter.ViewHolder<Course> {

    private TextView tip;
    private TextView topics;
    private TextView player;
    private TextView blurText;
    private ImageView blurImage;
    private ImageView blurLayout;
    private ImageView deleteTrailer;
    private WxCircleLoading circleLoading;
    private PostCourseAdapter mAdapter;

    public PostCourseHeaderViewHolder(View itemView, PostCourseAdapter mAdapter) {
        super(itemView);

        this.mAdapter = mAdapter;
        tip = ViewUtils.findById(itemView, R.id.tip);
        player = ViewUtils.findById(itemView, R.id.player);
        topics = ViewUtils.findById(itemView, R.id.topics);
        blurText = ViewUtils.findById(itemView, R.id.blurText);
        blurImage = ViewUtils.findById(itemView, R.id.blurImage);
        blurLayout = ViewUtils.findById(itemView, R.id.blurLayout);
        deleteTrailer = ViewUtils.findById(itemView, R.id.deleteTrailer);
        circleLoading = ViewUtils.findById(itemView, R.id.circleLoading);
    }

    @Override
    public void update(BaseAdapter adapter, int position, final Course course) {

        setTopics(course); // 设置话题

        if (Utils.isNotEmpty(course.getVideo_url())) {
            setPlayView(course); // 已上传完视频
        } else if (course.isUpLoading()) {
            setLoading(course); // 正在上传视频
        } else {
            setDefault(); // 当前没视频也没在上传
        }

    }

    // 设置播放样式
    private void setPlayView(final Course course) {

        if (blurImage.getTag() == null) {

            if (TextUtils.isEmpty(course.getLocalVideoUrl())) {
                blurImage.setTag(course.getVideo_url());
                //为什么是加载视频课程的封面？
//                ImageUtil.displayImage(blurImage, course.getSnapshot_url(), R.mipmap.icon_living_bg);
                ImageUtil.displayImage(blurImage, course.getCover_url(), R.mipmap.icon_living_bg);
            } else {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(course.getLocalVideoUrl(),
                        MediaStore.Video.Thumbnails.MICRO_KIND);
                blurImage.setImageBitmap(bitmap);
                blurImage.setTag(course.getLocalVideoUrl());
            }
        }

        tip.setVisibility(View.GONE);
        blurText.setVisibility(View.GONE);
        blurLayout.setVisibility(View.GONE);
        circleLoading.setVisible(View.GONE);
        player.setVisibility(View.VISIBLE);
        deleteTrailer.setVisibility(View.VISIBLE);
        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusManager.postEvent(Constants.PLAY_TRAILER, SubcriberTag.CLICK_TRAILER);
            }
        });

        deleteTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course.setProgress(0);
                course.setVideo_url(null);
                blurImage.setImageResource(R.mipmap.icon_living_bg);
                mAdapter.notifyItemChanged(0);
            }
        });
    }

    // 设置上传样式
    private void setLoading(Course course) {

        tip.setVisibility(View.GONE);
        player.setVisibility(View.GONE);
        blurText.setVisibility(View.GONE);
        blurLayout.setVisibility(View.GONE);
        circleLoading.setVisible(View.VISIBLE);
        deleteTrailer.setVisibility(View.GONE);
        circleLoading.setProgress(course.getProgress());
    }

    // 设置默认图
    private void setDefault() {

        player.setVisibility(View.GONE);
        circleLoading.setVisible(View.GONE);
        tip.setVisibility(View.VISIBLE);
        blurText.setVisibility(View.VISIBLE);
        blurLayout.setVisibility(View.VISIBLE);
        deleteTrailer.setVisibility(View.GONE);
        // 高斯化处理
        if (blurImage.getTag(R.id.is_do_blur) == null) {
            FastBlur.doPartBlur(mAdapter.getContext(), blurImage, blurLayout);
        }
        blurLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusManager.postEvent(Constants.ADD_TRAILER, SubcriberTag.CLICK_TRAILER);
            }
        });
    }

    // 设置话题
    private void setTopics(final Course course) {

        if (Utils.isEmpty(course.getTopics())) {
            topics.setText("");
            ViewUtils.setPartHintColor(topics, R.color.color333333, 1, topics.getHint().length() - 1);
        } else {

            StringBuilder sb = new StringBuilder();
            for (Topic topic : course.getTopics()) {
                sb.append(topic.getFormatName("#FFC401"));
            }
            ViewUtils.setTextColor(topics, sb.toString());
        }
        topics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> topicsList = new ArrayList<>();
                for (Topic topic : course.getTopics()) {
                    topicsList.add(topic.getName().replaceAll("#", ""));
                }
                AddTopicActivity.startActivity(mAdapter.getContext(), topicsList);
            }
        });
    }
}
