package com.wdf.fudoc.assemble.handler.impl;

import com.wdf.fudoc.assemble.handler.ParamValueHandler;
import com.wdf.fudoc.constant.AnnotationConstants;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;
import com.wdf.fudoc.util.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @Author wangdingfu
 * @Description 接口标题获取接口实现
 * @Date 2022-06-19 22:25:18
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
            Optional<AnnotationData> annotation = baseInfoDesc.getAnnotation(AnnotationConstants.SWAGGER_API);
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
