package com.wdf.fudoc.apidoc.assemble.handler;

import com.wdf.fudoc.apidoc.assemble.handler.impl.*;
import com.wdf.fudoc.assemble.handler.impl.*;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.desc.BaseInfoDesc;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-06-18 22:40:25
 */
public class ParamValueExecutor {

    private static final Map<ParamValueType, ParamValueHandler> paramValueHandlerMap = new ConcurrentHashMap<>();


    static {
        paramValueHandlerMap.put(ParamValueType.PARAM_NAME, new ParamNameValueHandler());
        paramValueHandlerMap.put(ParamValueType.PARAM_REQUIRE, new ParamRequireValueHandler());
        paramValueHandlerMap.put(ParamValueType.METHOD_TITLE, new FuDocTitleValueHandler());
        paramValueHandlerMap.put(ParamValueType.METHOD_DETAIL_INFO, new FuDocDetailInfoValueHandler());
        paramValueHandlerMap.put(ParamValueType.PARAM_COMMENT, new ParamCommentValueHandler());
        paramValueHandlerMap.put(ParamValueType.PARAM_TYPE_VIEW, new ParamTypeViewValueHandler());
    }


    public static String doGetValue(FuDocContext fuDocContext, ParamValueType paramValueType, BaseInfoDesc baseInfoDesc) {
        ParamValueHandler paramValueHandler = paramValueHandlerMap.get(paramValueType);
        if (Objects.nonNull(paramValueHandler)) {
            return paramValueHandler.getParamValue(fuDocContext, baseInfoDesc);
        }
        return "";
    }
}
