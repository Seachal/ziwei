package com.laka.live.ui.room.roommanagerlist;

import com.laka.live.bean.UserInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/13.
 * Email-1501448275@qq.com
 */
public class ManagerHelperParams implements Serializable {
    public int fromType;
    public List<UserInfo> managers = new ArrayList<>();
}
