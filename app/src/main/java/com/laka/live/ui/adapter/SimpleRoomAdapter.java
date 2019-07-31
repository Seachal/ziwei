package com.laka.live.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.BaseRoom;
import com.laka.live.bean.Room;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/6/28.
 */
public class SimpleRoomAdapter extends BaseAdapter<Room, SimpleRoomAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_simple_room, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<Room> {

        private LevelText levelText;

        private SimpleDraweeView screenshot;

        public ViewHolder(View itemView) {
            super(itemView);

            int screentW = Utils.getScreenWidth(itemView.getContext());
            int w = (screentW
                    - itemView.getContext().getResources().getDimensionPixelSize(R.dimen.feature_divider_width)
                    * 2) / 3;
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    w);
            itemView.setLayoutParams(params);
            levelText = (LevelText) itemView.findViewById(R.id.level);
            screenshot = (SimpleDraweeView) itemView.findViewById(R.id.screenshot);

//            FrameLayout.LayoutParams screenshotParams = new FrameLayout.LayoutParams(w, w);
            ViewGroup.LayoutParams screenshotParams =  screenshot.getLayoutParams();
            screenshotParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            screenshotParams.height = w;
            screenshot.setLayoutParams(screenshotParams);
        }

        @Override
        public void update(BaseAdapter adapter, int position, final Room room) {
            levelText.setLevel(room.getLevel());
            ImageUtil.loadImage(screenshot, room.getScreenShot());

            screenshot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseRoom.enterRoom(screenshot.getContext(), room, Common.FROM_NEWST);
                }
            });
        }
    }
}
