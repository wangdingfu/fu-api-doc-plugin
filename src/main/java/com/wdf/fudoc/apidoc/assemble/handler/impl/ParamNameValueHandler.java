package com.wdf.fudoc.apidoc.assemble.handler.impl;

import com.wdf.fudoc.apidoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

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
        //如果字段上标识了JsonProperty注解 则字段名应该是JsonProperty注解指定的字段名
        Optional<AnnotationData> jsonPropertyAnnotation = objectInfoDesc.getAnnotation(AnnotationConstants.JSON_PROPERTY);
        if(jsonPropertyAnnotation.isEmpty()){
            return objectInfoDesc.getName();
        }
        String fieldName = jsonPropertyAnnotation.get().constant().stringValue();
        if(StringUtils.isBlank(fieldName)){
            return objectInfoDesc.getName();
        }
        return fieldName;
    }

}
