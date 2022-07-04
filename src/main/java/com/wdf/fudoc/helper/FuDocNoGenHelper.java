package com.wdf.fudoc.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @Descption 接口文档序号生成器
 * @Date 2022-06-23 22:01:19
 */
public class FuDocNoGenHelper {

    /**
     * 维护每一个类或接口生成的接口文档序号（编号的范围仅在一个java类中）
     * <p>
     * key:classId value:接口编号
     */
    private static final Map<String, Integer> DOC_NO_MAP = new ConcurrentHashMap<>();

    private static final Set<String> CLASS_ID_SET = new HashSet<>();

    public synchronized static String genNo(String classId) {
        if (StringUtils.isBlank(classId)) {
            return "0";
        }
        return getClassNo(classId) + "." + getDocNo(classId);
    }

    private synchronized static Integer getClassNo(String classId) {
        CLASS_ID_SET.add(classId);
        return CLASS_ID_SET.size();
    }


    private synchronized static Integer getDocNo(String classId) {
        Integer value = DOC_NO_MAP.get(classId);
        if (Objects.isNull(value)) {
            value = 0;
        }
        value = value + 1;
        DOC_NO_MAP.put(classId, value);
        return value;
    }
}
