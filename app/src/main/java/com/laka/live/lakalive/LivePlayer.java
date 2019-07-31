package com.laka.live.lakalive;

/**
 * Created by luwies on 16/10/8.
 */
public abstract class LivePlayer extends LivePublisher{

    public abstract void startPlay();

    public abstract void stopPlay();

    public abstract void destroy();

    public abstract void reset(String streamId,String channelId);
}
