package com.wdf.fudoc.assemble.handler.impl;

import com.wdf.fudoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.constant.AnnotationConstants;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.AnnotationUtils;
import com.wdf.fudoc.util.ValidateAnnotationUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

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
        if(fuDocContext.isEnableSwagger()){
            //ApiModelProperty注解支持
            Optional<AnnotationData> annotation = objectInfoDesc.getAnnotation(AnnotationConstants.SWAGGER_API_MODEL_PROPERTY);
            String swaggerName = AnnotationUtils.getAnnotationValue(annotation, FuDocConstants.VALUE, FuDocConstants.AnnotationAttr.NAME);
            if (StringUtils.isNotBlank(swaggerName)) {
                return swaggerName;
            }

            //ApiParam注解支持
            annotation = objectInfoDesc.getAnnotation(AnnotationConstants.SWAGGER_API_PARAM);
            swaggerName = AnnotationUtils.getAnnotationValue(annotation, FuDocConstants.AnnotationAttr.NAME, FuDocConstants.VALUE);
            if (StringUtils.isNotBlank(swaggerName)) {
                return swaggerName;
            }
        }

        //获取注释参数名
        String name = objectInfoDesc.getName();
        if (StringUtils.isNotBlank(name)) {
            return name;
        }


        if(fuDocContext.isEnableValidMessage()){
            //获取校验注解信息截取出字段名
            String validMessage = ValidateAnnotationUtils.getValidMessage(objectInfoDesc);
            if (StringUtils.isNotBlank(validMessage)) {
                return validMessage.replace("不能为空", "").replace("not null", "");
            }
        }

        return "";
    }

}
