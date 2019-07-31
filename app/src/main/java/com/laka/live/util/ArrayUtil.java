package com.laka.live.util;

import com.laka.live.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lyf
 * @CreateTime 2018/4/9
 * @Description 数组工具
 **/
public class ArrayUtil {

    /**
     * @return 返回 beanList 里面的url的集合
     */
    public static ArrayList<String> getUrls(List<ImageBean> beanList) {

        ArrayList<String> urls = new ArrayList<>();
        for (ImageBean bean : beanList) {
            urls.add(bean.getUrl());
        }
        return urls;
    }

}
