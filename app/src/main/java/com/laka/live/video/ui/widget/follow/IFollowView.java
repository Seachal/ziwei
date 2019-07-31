package com.laka.live.video.ui.widget.follow;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:关注View合约类
 */

public interface IFollowView {

    void initProperties(Context context, AttributeSet attributeSet);

    void initView(Context context);

    void setFollowState(boolean isFollow);

    boolean getFollowState();

    /**
     * 关注
     */
    void follow();

    /**
     * 取消关注
     */
    void unFollow();

}
