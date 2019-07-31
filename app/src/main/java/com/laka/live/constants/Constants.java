package com.laka.live.constants;

/**
 * Created by Lyf on 2017/5/31.
 */

public interface Constants {

    int TYPE_HEAD = 100; // list的head类型
    int TYPE_ITEM = 101; // 普通item类型
    int TYPE_ADD = 102; // list的add类型
    int TYPE_EDIT = 103; // 编辑类型

    int MESSAGE_SUCCESS = 200; //  上传成功
    int MESSAGE_PROGRESS = 201;// 上传中
    int MESSAGE_FAIL = 203; // 上传失败。

    int ADD_TRAILER = 300; // 添加预告
    int PLAY_TRAILER = 301; // 播放预告

    int REQUEST_ADD_COURSE = 1000; // 添加课程的返回
    int REQUEST_EDIT_COURSE = 1001; // 修改课程的返回

    String PACKAGE_NAME = "com.laka.live";
}
