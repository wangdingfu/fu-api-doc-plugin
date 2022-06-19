package com.wdf.apidoc.assemble.handler.impl;

import com.wdf.apidoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.apidoc.constant.enumtype.ParamValueType;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.apidoc.util.ValidateAnnotationUtils;


/**
 * @Author wangdingfu
 * @Description 参数是否必填参数值获取实现
 * @Date 2022-06-18 22:44:33
 */
public class ParamRequireValueHandler extends BaseParamFieldValueHandler {
    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.PARAM_REQUIRE;
    }

    @Override
    protected String doGetParamValue(ObjectInfoDesc objectInfoDesc) {
        //设置是否必填
        return ValidateAnnotationUtils.isRequire(objectInfoDesc);
    }
}
