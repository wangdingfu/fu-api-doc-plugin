package com.wdf.fudoc.apidoc.assemble.handler;

import com.wdf.fudoc.apidoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.desc.BaseInfoDesc;

/**
 * 参数值获取接口
 *
 * @author wangdingfu
 * @date 2022-06-18 22:36:11
 */
public interface ParamValueHandler {


    ParamValueType getParamValueType();


    String getParamValue(FuDocContext fuDocContext, BaseInfoDesc baseInfoDesc);

}
