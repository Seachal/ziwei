package com.laka.live.listener;

import com.facebook.drawee.controller.ControllerListener;

/**
 * @Author:summer
 * @Date:2018/12/3
 * @Description: Fresco 图片加载框架的适配器
 */
public abstract class IControllerListener implements ControllerListener {

    @Override
    public void onSubmit(String id, Object callerContext) {

    }

    @Override
    public void onIntermediateImageSet(String id, Object imageInfo) {

    }

    @Override
    public void onIntermediateImageFailed(String id, Throwable throwable) {

    }

    @Override
    public void onRelease(String id) {

    }
}