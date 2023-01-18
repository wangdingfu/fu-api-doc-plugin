package com.wdf.fudoc.apidoc.pojo.annotation;

import com.wdf.fudoc.apidoc.constant.enumtype.SpringParamAnnotation;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;

/**
 * @author wangdingfu
 * @date 2022-09-27 18:08:09
 */
public class RequestParamData implements SpringAnnotationData {


    private final String value = "";

    private final String name = "";

    private final boolean required = true;

    private final String defaultValue = "";

    public RequestParamData(AnnotationData annotationData) {
    }

    @Override
    public SpringParamAnnotation get() {
        return SpringParamAnnotation.REQUEST_PARAM;
    }
}
