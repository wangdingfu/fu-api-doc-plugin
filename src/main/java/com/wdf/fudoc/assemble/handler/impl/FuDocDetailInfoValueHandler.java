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
import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @Author wangdingfu
 * @Description 接口详细内容值获取实现
 * @Date 2022-06-19 22:28:06
 */
public class FuDocDetailInfoValueHandler implements ParamValueHandler {
    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.METHOD_DETAIL_INFO;
    }

    @Override
    public String getParamValue(FuDocContext fuDocContext, BaseInfoDesc baseInfoDesc) {
        //获取swagger注解
        if(fuDocContext.isEnableSwagger()){
            Optional<AnnotationData> annotation = baseInfoDesc.getAnnotation(AnnotationConstants.SWAGGER_API);
            String swaggerName = AnnotationUtils.getAnnotationValue(annotation, FuDocConstants.AnnotationAttr.DESCRIPTION);
            if (StringUtils.isNotBlank(swaggerName)) {
                return swaggerName;
            }
        }

        ApiDocCommentData commentData = baseInfoDesc.getCommentData();
        if (Objects.nonNull(commentData)) {
            return commentData.getCommentDetailInfo();
        }
        return StringUtils.EMPTY;
    }
}
