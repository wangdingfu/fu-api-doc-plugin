package com.wdf.fudoc.apidoc.pojo.annotation;

import com.wdf.fudoc.apidoc.constant.enumtype.SpringParamAnnotation;

/**
 * @author wangdingfu
 * @date 2022-09-27 19:50:56
 */
public class DefaultAnnotationData implements SpringAnnotationData{

    @Override
    public SpringParamAnnotation get() {
        return SpringParamAnnotation.NONE;
    }
}
