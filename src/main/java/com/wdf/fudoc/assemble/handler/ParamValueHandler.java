package com.wdf.fudoc.assemble.handler;

import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;

/**
 * @Author wangdingfu
 * @Description 参数值获取接口
 * @Date 2022-06-18 22:36:11
 */
public interface ParamValueHandler {


    ParamValueType getParamValueType();


    String getParamValue(FuDocContext fuDocContext, BaseInfoDesc baseInfoDesc);

}
