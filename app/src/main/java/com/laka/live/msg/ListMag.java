package com.laka.live.msg;

import java.util.List;

/**
 * Created by luyi on 2015/9/7.
 */
public abstract class ListMag<T> extends Msg {

    public abstract List<T> getList();

    public boolean isEmpty() {
        List list = getList();
        return list == null || list.isEmpty();
    }
}
