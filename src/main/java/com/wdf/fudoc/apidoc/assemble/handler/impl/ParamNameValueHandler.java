package com.wdf.fudoc.apidoc.assemble.handler.impl;

import com.wdf.fudoc.apidoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @Description 参数名称获取接口实现
 * @date 2022-06-18 22:37:09
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
