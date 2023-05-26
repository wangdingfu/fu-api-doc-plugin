package com.wdf.fudoc.common.enumtype;

import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-05-26 13:46:07
 */
@Getter
public enum ControllerAnnotation {

    CONTROLLER("Controller", AnnotationConstants.CONTROLLER),
    REST_CONTROLLER("RestController", AnnotationConstants.REST_CONTROLLER);

    private final String name;

    private final String qualifiedName;

    ControllerAnnotation(String name, String qualifiedName) {
        this.name = name;
        this.qualifiedName = qualifiedName;
    }
}
