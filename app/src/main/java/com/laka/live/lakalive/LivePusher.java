package com.laka.live.lakalive;

import android.text.TextUtils;
import android.view.View;

import com.laka.live.zego.widgets.ViewLive;
import com.zego.zegoliveroom.ZegoLiveRoom;

import java.util.LinkedList;

/**
 * Created by luwies on 16/10/8.
 */
public abstract class LivePusher extends LivePublisher {


    public abstract void startPreview();

    public abstract void startPublish();

    public abstract void stopPreview();

    public abstract void stopPublish();

    public abstract void destroy();

    public abstract void setLiveId(String userName,String streamId,String channelId);

    public abstract boolean enableBeautifying(int beauty);

    public abstract boolean enableMic(boolean enable);

    /**
     * 开关手电筒
     * @param enable
     * @return
     */
    public abstract boolean enableTorch(boolean enable);

    public abstract boolean setFilter(int index);

    public abstract boolean setFrontCam(boolean bFront);


}
