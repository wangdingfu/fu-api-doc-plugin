package com.wdf.fudoc.assemble.handler.impl;

import com.wdf.fudoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @Description 参数显示的类型值获取
 * @date 2022-06-24 22:32:38
 */
public class ParamTypeViewValueHandler extends BaseParamFieldValueHandler {
    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.PARAM_TYPE_VIEW;
    }


    @Override
    protected String doGetParamValue(FuDocContext fuDocContext, ObjectInfoDesc objectInfoDesc) {
        return objectInfoDesc.getTypeView();
    }


}
