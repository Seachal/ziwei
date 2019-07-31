package com.laka.live.shopping.xrecyclerview;

/**
 * Created by zhxu on 2016/4/21.
 * Email:357599859@qq.com
 */
interface BaseRefreshHeader {
    void onMove(float delta, int dragY);

    boolean releaseAction();

    void refreshComplete();

    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;
}
