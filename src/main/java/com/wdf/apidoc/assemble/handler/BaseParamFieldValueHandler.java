package com.wdf.apidoc.assemble.handler;

import com.wdf.apidoc.pojo.desc.BaseInfoDesc;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @Author wangdingfu
 * @Description
 * @Date 2022-06-18 22:45:30
 */
public abstract class BaseParamFieldValueHandler implements ParamValueHandler {

    protected abstract String doGetParamValue(ObjectInfoDesc objectInfoDesc);

    @Override
    public String getParamValue(BaseInfoDesc baseInfoDesc) {
        if (baseInfoDesc instanceof ObjectInfoDesc) {
            return doGetParamValue((ObjectInfoDesc) baseInfoDesc);
        }
        return null;
    }
}
