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
 * @Description 参数注释说明内容值获取
 * @Date 2022-06-24 22:27:15
 */
public class ParamCommentValueHandler extends BaseParamFieldValueHandler {
    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.PARAM_COMMENT;
    }


    @Override
    protected String doGetParamValue(FuDocContext fuDocContext, ObjectInfoDesc objectInfoDesc) {
        //从swagger注解中获取注释说明
        if (fuDocContext.isEnableSwagger()) {
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

        String docText = objectInfoDesc.getDocText();
        if (StringUtils.isNotBlank(docText)) {
            return docText;
        }
        if (fuDocContext.isEnableValidMessage()) {
            //获取校验注解信息截取出字段名
            String validMessage = ValidateAnnotationUtils.getValidMessage(objectInfoDesc);
            if (StringUtils.isNotBlank(validMessage)) {
                return validMessage.replace("不能为空", "").replace("not null", "");
            }
        }
        return StringUtils.EMPTY;
    }


}
