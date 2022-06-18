package com.wdf.apidoc.assemble.handler;

import com.wdf.apidoc.constant.enumtype.ParamValueType;
import com.wdf.apidoc.pojo.desc.BaseInfoDesc;

/**
 * @Author wangdingfu
 * @Description 参数值获取接口
 * @Date 2022-06-18 22:36:11
 */
public interface ParamValueHandler {


    ParamValueType getParamValueType();


    String getParamValue(BaseInfoDesc baseInfoDesc);

}
