package com.wdf.apidoc.assemble.handler;

import com.wdf.apidoc.constant.enumtype.ParamDataType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wangdingfu
 * @Description
 * @Date 2022-06-17 22:41:22
 */
public class ParamDataExecutor {

    private Map<ParamDataType,FuDocParamDataFillerHandler> paramDataFillerHandlerMap = new ConcurrentHashMap<>();


}
