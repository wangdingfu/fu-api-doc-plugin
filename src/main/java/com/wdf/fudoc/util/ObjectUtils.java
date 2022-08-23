package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @Descption 对象工具类
 * @date 2022-05-31 11:10:26
 */
public class ObjectUtils {


    public static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> function) {
        if (CollectionUtils.isNotEmpty(list)) {
            Map<K, V> map = new HashMap<>(list.size());
            for (V v : list) {
                map.put(function.apply(v), v);
            }
            return map;
        }
        return new HashMap<>();
    }


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


    public static <T, R> List<R> listToList(List<T> tList, Function<T, R> function) {
        if (CollectionUtils.isNotEmpty(tList) && Objects.nonNull(function)) {
            return tList.stream().map(function).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }
}
