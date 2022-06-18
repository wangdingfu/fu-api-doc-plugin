package com.wdf.apidoc.assemble.handler.impl;

import com.wdf.apidoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.constant.enumtype.ParamValueType;
import com.wdf.apidoc.pojo.data.AnnotationData;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

import java.util.Optional;

/**
 * @Author wangdingfu
 * @Description
 * @Date 2022-06-18 22:44:33
 */
public class ParamRequireValueHandler extends BaseParamFieldValueHandler {
    @Override
    public ParamValueType getParamValueType() {
        return null;
    }

    @Override
    protected String doGetParamValue(ObjectInfoDesc objectInfoDesc) {
        //设置是否必填
        Optional<AnnotationData> annotation = objectInfoDesc.getAnnotation(AnnotationConstants.VALID_NOT);
        return annotation.isPresent() ? "是" : "否";
    }
}
