package com.wdf.fudoc.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 对象工具类
 * @Date 2022-05-31 11:10:26
 */
public class ObjectUtils {


    @SafeVarargs
    public static <T> List<T> newArrayList(T... tList) {
        List<T> resultList = Lists.newArrayList();
        if (Objects.nonNull(tList) && tList.length > 0) {
            for (T t : tList) {
                if (Objects.nonNull(t)) {
                    resultList.add(t);
                }
            }
        }
        return resultList;
    }
}
