package com.wdf.fudoc.apidoc.pojo.annotation;

import com.wdf.fudoc.apidoc.constant.enumtype.SpringParamAnnotation;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import lombok.NoArgsConstructor;
import com.wdf.fudoc.util.FuStringUtils;

/**
 * @author wangdingfu
 * @date 2022-09-27 18:09:15
 */
@NoArgsConstructor
public class PathVariableData implements SpringAnnotationData {

    private final String value = FuStringUtils.EMPTY;

    private final String name = FuStringUtils.EMPTY;

    private final boolean required = true;


    public PathVariableData(AnnotationData annotationData) {
    }

    @Override
    public SpringParamAnnotation get() {
        return SpringParamAnnotation.PATH_VARIABLE;
    }
}
