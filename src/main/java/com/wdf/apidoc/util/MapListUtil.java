package com.wdf.apidoc.util;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @descption: map工具类
 * @date 2022-05-25 23:35:34
 */
public class MapListUtil<K, T> {

    private final Map<K, List<T>> resultMap = new ConcurrentHashMap<>();

    public MapListUtil() {
    }

    public static <K, T> MapListUtil<K, T> getInstance() {
        return new MapListUtil<>();
    }


    public static <K, T> MapListUtil<K, T> getInstance(List<T> list, Function<T, K> function) {
        MapListUtil<K, T> instance = getInstance();
        list.forEach(f -> instance.add(function.apply(f), f));
        return instance;
    }

    public void foreach(BiConsumer<K, List<T>> action) {
        resultMap.forEach(action);
    }

    public void add(K key, T t) {
        if (Objects.isNull(key) || Objects.isNull(t)) {
            return;
        }
        List<T> resultList = resultMap.get(key);
        if (Objects.isNull(resultList)) {
            resultList = Lists.newArrayList();
            resultMap.put(key, resultList);
        }
        resultList.add(t);
    }

    public void add(K key, List<T> tList) {
        if (Objects.isNull(key) || CollectionUtils.isEmpty(tList)) {
            return;
        }
        List<T> resultList = resultMap.get(key);
        if (Objects.isNull(resultList)) {
            resultList = Lists.newArrayList();
            resultMap.put(key, resultList);
        }
        resultList.addAll(tList);
    }


    public List<T> get(K key) {
        if (Objects.nonNull(key) && resultMap.containsKey(key)) {
            return resultMap.get(key);
        }
        return Lists.newArrayList();
    }

    public Set<T> getValues() {
        Set<T> resultSet = Sets.newHashSet();
        resultMap.forEach((key, value) -> resultSet.addAll(value));
        return resultSet;
    }

    public Set<K> getKeys() {
        return resultMap.keySet();
    }

    public Map<K, List<T>> get() {
        return resultMap;
    }
}
