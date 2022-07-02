package com.wdf.fudoc.assemble.handler.impl;

import com.wdf.fudoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.fudoc.constant.AnnotationConstants;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
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

    /**
     * 获取当前参数是否必填值
     *
     * @param fuDocContext   全局上下文
     * @param objectInfoDesc 参数描述对象
     * @return 是否必填属性的值
     */
    @Override
    protected String doGetParamValue(FuDocContext fuDocContext, ObjectInfoDesc objectInfoDesc) {
        //获取当前参数的根节点
        ObjectInfoDesc rootInfoDesc = fuDocContext.getByDescId(objectInfoDesc.getRootId());
        if (Objects.isNull(rootInfoDesc)) {
            return YesOrNo.NO.getDesc();
        }
        Optional<AnnotationData> annotation = rootInfoDesc.getAnnotation(AnnotationConstants.VALIDATED);
        if (annotation.isPresent()) {
            Integer referenceParamId = objectInfoDesc.getValue("referenceParamId", Integer.class);
            //获取父级参数是否使用了“@Valid”注解标识
            ObjectInfoDesc parentInfoDesc = fuDocContext.getByDescId(referenceParamId);
            if (Objects.nonNull(parentInfoDesc) && rootInfoDesc.getAnnotation(AnnotationConstants.VALID).isEmpty()) {
                //存在父对象 且父对象没有标识@Valid 注解 则当前对象不受校验注解控制
                return YesOrNo.NO.getDesc();
            }

            //有“@Validated()”注解
            Optional<AnnotationData> annotationDataOptional = objectInfoDesc.getAnnotation(AnnotationConstants.VALID_NOT);
            if (annotationDataOptional.isPresent()) {
                //有标识必填注解 需要进一步判断group
                List<String> groupClassNameList = annotation.get().array().clazz().className();
                if (CollectionUtils.isEmpty(groupClassNameList)) {
                    //没指定哪一个group 则标识了必填注解的参数都为必填
                    return YesOrNo.YES.getDesc();
                }
                List<String> groups = annotationDataOptional.get().array(FuDocConstants.AnnotationAttr.GROUPS).clazz().className();
                return new HashSet<>(groups).containsAll(groupClassNameList) ? YesOrNo.YES.getDesc() : YesOrNo.NO.getDesc();
            }
        }
        //请求入参中没有表示“@Validated()”注解 则直接返回不必填
        return YesOrNo.NO.getDesc();
    }
}
