package com.wdf.fudoc.assemble.handler.impl;

import com.wdf.fudoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.ValidateAnnotationUtils;


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
    protected String doGetParamValue(FuDocContext fuDocContext, ObjectInfoDesc objectInfoDesc) {
        //判断当前校验注解是否生效

        //设置是否必填
        return ValidateAnnotationUtils.isRequire(objectInfoDesc);
    }
}
