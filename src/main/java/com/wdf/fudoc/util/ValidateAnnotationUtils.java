package com.wdf.fudoc.util;

import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.apidoc.pojo.desc.BaseInfoDesc;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Optional;

/**
 * @author wangdingfu
 * @Description 校验注解工具类
 * @date 2022-06-18 22:55:28
 */
public class ValidateAnnotationUtils {


    /**
     * 返回参数是否必填
     *
     * @param baseInfoDesc 描述对象抽象类
     * @return 是|否
     */
    public static String isRequire(BaseInfoDesc baseInfoDesc) {
        Optional<AnnotationData> annotation = baseInfoDesc.getAnnotation(AnnotationConstants.VALID_NOT);
        return annotation.isPresent() ? "是" : "否";
    }


    /**
     * 获取校验注解Message信息
     *
     * @param baseInfoDesc 描述对象抽象类
     * @return message内容
     */
    public static String getValidMessage(BaseInfoDesc baseInfoDesc) {
        if (baseInfoDesc.exists(AnnotationConstants.JAVAX_VALID_NOT_NULL)) {
            return getMessageValue(baseInfoDesc, AnnotationConstants.JAVAX_VALID_NOT_NULL);
        }

        if (baseInfoDesc.exists(AnnotationConstants.JAVAX_VALID_NOT_EMPTY)) {
            return getMessageValue(baseInfoDesc, AnnotationConstants.JAVAX_VALID_NOT_EMPTY);
        }

        if (baseInfoDesc.exists(AnnotationConstants.JAVAX_VALID_NOT_BLANK)) {
            return getMessageValue(baseInfoDesc, AnnotationConstants.JAVAX_VALID_NOT_BLANK);
        }

        if (baseInfoDesc.exists(AnnotationConstants.VALID_NOT_BLANK)) {
            return getMessageValue(baseInfoDesc, AnnotationConstants.VALID_NOT_BLANK);
        }

        if (baseInfoDesc.exists(AnnotationConstants.VALID_NOT_EMPTY)) {
            return getMessageValue(baseInfoDesc, AnnotationConstants.VALID_NOT_EMPTY);
        }

        return FuStringUtils.EMPTY;
    }


    private static String getMessageValue(BaseInfoDesc baseInfoDesc, String annotationName) {
        Optional<AnnotationData> annotation = baseInfoDesc.getAnnotation(annotationName);
        if (annotation.isPresent()) {
            return annotation.get().constant(FuDocConstants.AnnotationAttr.MESSAGE).stringValue();
        }
        return FuStringUtils.EMPTY;
    }


}
