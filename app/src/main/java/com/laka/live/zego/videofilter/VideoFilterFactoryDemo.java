package com.laka.live.zego.videofilter;


import com.zego.zegoavkit2.videofilter.ZegoVideoFilter;
import com.zego.zegoavkit2.videofilter.ZegoVideoFilterFactory;

/**
 * Created by robotding on 16/12/3.
 * ZegoApiManager 有使用到该类，当前类使用 VideoFilterSurfaceTextureDemo，VideoFilterSurfaceTextureDemo 使用 ZegoImageFilter
 */

public class VideoFilterFactoryDemo extends ZegoVideoFilterFactory {
    private int mode = 5;
    private ZegoVideoFilter mFilter = null;

    @Override
    public ZegoVideoFilter create() {
        switch (mode) {
            case 0:
                mFilter = new VideoFilterMemDemo();
                break;
            case 1:
                mFilter = new VideoFilterSurfaceTextureDemo();
                break;
            case 2:
                mFilter = new VideoFilterHybridDemo();
                break;
            case 3:
                mFilter = new VideoFilterGlTexture2dDemo();
                break;
            case 4:
                mFilter = new VideoFilterSurfaceTextureDemo2();
                break;
            case 5:
                mFilter = new VideoFilterI420MemDemo();
                break;
            default:
                break;
        }

        return mFilter;
    }

    @Override
    public void destroy(ZegoVideoFilter vf) {
        mFilter = null;
    }
}
