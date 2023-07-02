package com.wdf.fudoc.apidoc.assemble.handler.impl;

import com.wdf.fudoc.apidoc.assemble.handler.ParamValueHandler;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apidoc.pojo.desc.BaseInfoDesc;
import com.wdf.fudoc.util.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @author wangdingfu
 * @Description 接口标题获取接口实现
 * @date 2022-06-19 22:25:18
 */
public class FuDocTitleValueHandler implements ParamValueHandler {
    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.METHOD_TITLE;
    }

    @Override
    public String getParamValue(FuDocContext fuDocContext, BaseInfoDesc baseInfoDesc) {
        //获取swagger注解
        if(fuDocContext.isEnableSwagger()){
            Optional<AnnotationData> annotation = baseInfoDesc.getAnnotation(AnnotationConstants.SWAGGER_API_OPERATION);
            String swaggerName = AnnotationUtils.getAnnotationValue(annotation, FuDocConstants.VALUE, FuDocConstants.AnnotationAttr.NAME);
            if (StringUtils.isNotBlank(swaggerName)) {
                return swaggerName;
            }
        }

        //获取注释内容返回
        ApiDocCommentData commentData = baseInfoDesc.getCommentData();
        if(Objects.nonNull(commentData)){
            return commentData.getCommentTitle();
        }
        return StringUtils.EMPTY;
    }
}
