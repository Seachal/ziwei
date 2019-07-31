package com.laka.live.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.Iterator;


/**
 * 先进先出队列，max限定大小。
 * 当add数达到max后，再往这里面add的话，会不断的挤掉最先添加进去的数。
 *
 * @param <E>
 */
public class LimitQueue<E> extends ArrayDeque<E> {

    private int max;

    public LimitQueue(int max) {
        super(max);
        this.max = max;
    }

    @Override
    public boolean add(E e) {

        if (size() == max) {
            remove();
        }

        return super.add(e);

    }

    // 遍历所有值(仅限E为String)
    public String getStringContent() {

        StringBuilder content = new StringBuilder();
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            content.append(iterator.next());
        }
        return content.toString().trim();
    }

}
