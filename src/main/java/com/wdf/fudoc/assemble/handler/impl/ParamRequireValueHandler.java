package com.wdf.fudoc.assemble.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.wdf.fudoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.constant.AnnotationConstants;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
        if (annotationDataOptional.isPresent()) {
            JSONObject extInfo = objectInfoDesc.getExtInfo();
            BaseInfoDesc root;
            if (Objects.nonNull(extInfo) && Objects.nonNull(root = extInfo.getObject("root", BaseInfoDesc.class))) {
                Optional<AnnotationData> annotation = root.getAnnotation(AnnotationConstants.VALIDATED);
                if (annotation.isPresent()) {
                    List<Class<?>> groupClassValueList = annotation.get().getValue().getListClassValue();
                    if (CollectionUtils.isNotEmpty(groupClassValueList)) {
                        List<Class<?>> groups = annotationDataOptional.get().getValue(FuDocConstants.AnnotationAttr.GROUPS).getListClassValue();
                        if (CollectionUtils.isNotEmpty(groups)) {
                            return new HashSet<>(groups).containsAll(groupClassValueList) ? YesOrNo.YES.getDesc() : YesOrNo.NO.getDesc();
                        }
                        return YesOrNo.NO.getDesc();
                    }
                    return YesOrNo.YES.getDesc();
                }
            }
        }
        return YesOrNo.NO.getDesc();
    }
}
