package com.wdf.fudoc.apidoc.pojo.annotation;

import com.wdf.fudoc.apidoc.constant.enumtype.SpringParamAnnotation;

/**
 * @author wangdingfu
 * @date 2022-09-27 18:07:29
 */
public class RequestBodyData implements SpringAnnotationData {

    @Override
    public SpringParamAnnotation get() {
        return SpringParamAnnotation.REQUEST_BODY;
    }
}
