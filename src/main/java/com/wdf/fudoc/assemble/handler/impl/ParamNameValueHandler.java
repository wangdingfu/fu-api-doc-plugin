package com.wdf.fudoc.assemble.handler.impl;

import com.wdf.fudoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;

/**
 * @Author wangdingfu
 * @Description 参数名称获取接口实现
 * @Date 2022-06-18 22:37:09
 */
public class ParamNameValueHandler extends BaseParamFieldValueHandler {


    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.PARAM_NAME;
    }

    @Override
    protected String doGetParamValue(FuDocContext fuDocContext, ObjectInfoDesc objectInfoDesc) {
        return objectInfoDesc.getName();
    }

}
