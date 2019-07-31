package com.laka.live.video.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.help.EventBusManager;
import com.laka.live.ui.fragment.BaseFragment;
import com.laka.live.video.model.bean.MiniVideoBean;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:关注-推荐-最新视频列表类父类Fragment
 */

public abstract class BaseVideoFragment extends BaseFragment implements EventBusManager.OnEventBusListener {

    protected View rootView;
    protected Activity mActivity;
    protected Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            if (setContentView() == 0) {
                throw new IllegalArgumentException("layoutId can't no be null");
            }
            rootView = inflater.inflate(setContentView(), container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        mActivity = getActivity();
        initArguments();
        initView(rootView, savedInstanceState);
        EventBusManager.register(this);
        initData();
        initEvent();
        return rootView;
    }


    /**
     * 配置当前LayoutId
     *
     * @return
     */
    protected abstract int setContentView();

    /**
     * 初始化Argument数据
     */
    protected abstract void initArguments();


    /**
     * 初始化控件，考虑BufferKnife使用
     *
     * @param rootView
     * @param savedInstanceState
     */
    protected abstract void initView(View rootView, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    /**
     * 运用于双击刷新当前列表
     */
    protected void updateVideoListWithAnim() {

    }

    /**
     * 从接口更新数据
     */
    protected void updateVideoList() {

    }

    /**
     * 更新列表数据
     */
    protected void refreshVideoList() {

    }

    /**
     * 本地更新数据（从小视频播放页面传递进来，因为小视频播放页面可能修改了部分数据源的数据）
     *
     * @param videoBeanList
     */
    protected void updateVideoList(List<MiniVideoBean> videoBeanList) {

    }

    /**
     * 加载更多数据，用于小视频播放页面通知加载更多
     */
    protected void loadVideoList() {

    }

    /**
     * 设置页面targetPosition
     *
     * @param position
     */
    protected void scrollToPosition(int position) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
