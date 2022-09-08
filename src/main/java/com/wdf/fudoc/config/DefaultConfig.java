package com.wdf.fudoc.config;

import com.google.common.collect.Lists;
import com.wdf.fudoc.constant.CommonObjectNames;
import com.wdf.fudoc.pojo.bo.FilterFieldBO;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-08-12 22:37:40
 */
public class DefaultConfig {

    public static boolean sendStatus = false;

    /**
     * 默认需要过滤的字段
     */
    public static final List<FilterFieldBO>
            DEFAULT_FILTER_FIELD_LIST = Lists.newArrayList(
            new FilterFieldBO(CommonObjectNames.HTTP_REQUEST, ""),
            new FilterFieldBO(CommonObjectNames.HTTP_RESPONSE, ""),
            new FilterFieldBO(CommonObjectNames.MYBATIS_PLUS_PAGE, "orders,optimizeCountSql,searchCount,optimizeJoinOfCountSql,countId,maxLimit"));
}
