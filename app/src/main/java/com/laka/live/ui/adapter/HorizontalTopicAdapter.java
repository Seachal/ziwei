package com.laka.live.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.Room;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/7/7.
 */
public class HorizontalTopicAdapter extends BaseAdapter<Room, HorizontalTopicAdapter.ViewHolder> {



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horizontal_topic_room, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseAdapter.ViewHolder<Room> {

        private SimpleDraweeView face;

        private TextView name;

        private TextView views;

//        private ImageView light;

        private int divider;
        public ViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            face = (SimpleDraweeView) itemView.findViewById(R.id.face);

            name = (TextView) itemView.findViewById(R.id.name);

            views = (TextView) itemView.findViewById(R.id.views);

            divider = Utils.dip2px(context, 15f);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
            params.leftMargin = divider;
            itemView.setLayoutParams(params);

            int width = Util.getScreenWidth(context) * 390 / 1080;
            params = new RecyclerView.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);

            ViewGroup.MarginLayoutParams faceParams = (ViewGroup.MarginLayoutParams) face.getLayoutParams();
            faceParams.width = width;
            faceParams.height = width;
            face.setLayoutParams(faceParams);

            /*light = (ImageView) itemView.findViewById(R.id.light);
            AnimationDrawable drawable = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.greed_light_5dp);
            drawable.start();
            light.setImageDrawable(drawable);*/
        }

        @Override
        public void update(BaseAdapter adapter, int position, Room room) {
            ImageUtil.loadImage(face, room.getScreenShot());
            name.setText(room.getNickName());

            Context context = itemView.getContext();
            views.setText(room.getViewsCharSequence(context, false , false));
        }
    }
}
