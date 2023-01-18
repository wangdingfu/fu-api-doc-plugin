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
import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @author wangdingfu
 * @Description 接口详细内容值获取实现
 * @date 2022-06-19 22:28:06
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
