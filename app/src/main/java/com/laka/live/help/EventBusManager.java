package com.laka.live.help;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ios on 16/8/25.
 */
public class EventBusManager {

    /**
     * 注册
     */
    public static void register(OnEventBusListener subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 反注册
     */
    public static void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     * 发送事件
     * 对应的onEvent方法的注解@Subscribe
     */
    public static void postEvent(Object event, String tag) {
        EventBus.getDefault().post(new PostEvent(event, tag));
    }

    public static void postEvent(String msg) {
        EventBus.getDefault().post(new PostEvent(msg));
    }

    public static void postEvent(Object object) {
        postEvent(object, "");
    }

    /**
     * 发送延时事件
     * 对应的onEvent方法的注解@Subscribe(sticky = true)
     */
    public static void postSticky(Object event, String tag) {
        EventBus.getDefault().postSticky(new PostEvent(event, tag));
    }

    public interface OnEventBusListener {
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEvent(PostEvent event);
    }
}
