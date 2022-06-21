package com.wdf.apidoc.util;

import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.constant.FuDocConstants;
import com.wdf.apidoc.pojo.data.AnnotationData;
import com.wdf.apidoc.pojo.desc.BaseInfoDesc;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @Author wangdingfu
 * @Description 校验注解工具类
 * @Date 2022-06-18 22:55:28
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
        if (baseInfoDesc.exists(AnnotationConstants.VALID_NOT_NULL)) {
            return getMessageValue(baseInfoDesc, AnnotationConstants.VALID_NOT_NULL);
        }

        if (baseInfoDesc.exists(AnnotationConstants.VALID_NOT_BLANK)) {
            return getMessageValue(baseInfoDesc, AnnotationConstants.VALID_NOT_BLANK);
        }

        if (baseInfoDesc.exists(AnnotationConstants.VALID_NOT_EMPTY)) {
            return getMessageValue(baseInfoDesc, AnnotationConstants.VALID_NOT_EMPTY);
        }

        return StringUtils.EMPTY;
    }


    private static String getMessageValue(BaseInfoDesc baseInfoDesc, String annotationName) {
        Optional<AnnotationData> annotation = baseInfoDesc.getAnnotation(annotationName);
        if (annotation.isPresent()) {
            return annotation.get().getValue(FuDocConstants.AnnotationAttr.MESSAGE).getStringValue();
        }
        return StringUtils.EMPTY;
    }


}
