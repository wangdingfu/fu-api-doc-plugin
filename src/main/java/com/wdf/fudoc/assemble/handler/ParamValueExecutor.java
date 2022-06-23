package com.wdf.fudoc.assemble.handler;

import com.wdf.fudoc.assemble.handler.impl.FuDocDetailInfoValueHandler;
import com.wdf.fudoc.assemble.handler.impl.FuDocTitleValueHandler;
import com.wdf.fudoc.assemble.handler.impl.ParamNameValueHandler;
import com.wdf.fudoc.assemble.handler.impl.ParamRequireValueHandler;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wangdingfu
 * @Description
 * @Date 2022-06-18 22:40:25
 */
public class ParamValueExecutor {

    private static final Map<ParamValueType, ParamValueHandler> paramValueHandlerMap = new ConcurrentHashMap<>();


    static {
        paramValueHandlerMap.put(ParamValueType.PARAM_NAME, new ParamNameValueHandler());
        paramValueHandlerMap.put(ParamValueType.PARAM_REQUIRE, new ParamRequireValueHandler());
        paramValueHandlerMap.put(ParamValueType.METHOD_TITLE, new FuDocTitleValueHandler());
        paramValueHandlerMap.put(ParamValueType.METHOD_DETAIL_INFO, new FuDocDetailInfoValueHandler());
    }


    public static String doGetValue(FuDocContext fuDocContext, ParamValueType paramValueType, BaseInfoDesc baseInfoDesc) {
        ParamValueHandler paramValueHandler = paramValueHandlerMap.get(paramValueType);
        if (Objects.nonNull(paramValueHandler)) {
            return paramValueHandler.getParamValue(baseInfoDesc);
        }
        return "";
    }
}
