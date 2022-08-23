package com.wdf.fudoc.assemble.handler;

import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @date 2022-06-18 22:45:30
 */
public abstract class BaseParamFieldValueHandler implements ParamValueHandler {

    protected abstract String doGetParamValue(FuDocContext fuDocContext, ObjectInfoDesc objectInfoDesc);

    @Override
    public String getParamValue(FuDocContext fuDocContext, BaseInfoDesc baseInfoDesc) {
        if (baseInfoDesc instanceof ObjectInfoDesc) {
            return doGetParamValue(fuDocContext, (ObjectInfoDesc) baseInfoDesc);
        }
        return null;
    }
}
