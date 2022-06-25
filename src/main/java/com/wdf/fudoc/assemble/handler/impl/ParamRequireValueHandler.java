package com.wdf.fudoc.assemble.handler.impl;

import com.wdf.fudoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.constant.AnnotationConstants;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.ValidateAnnotationUtils;

import java.util.List;
import java.util.Optional;


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
        Optional<AnnotationData> annotationDataOptional = objectInfoDesc.getAnnotation(AnnotationConstants.VALID_NOT);
        if(annotationDataOptional.isPresent()){
            AnnotationData annotationData = annotationDataOptional.get();
            List<Class<?>> groups = annotationData.getValue("GROUPS").getListClassValue();
        }


        //设置是否必填
        return "否";
    }
}
