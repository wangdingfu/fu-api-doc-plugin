package com.wdf.fudoc.apidoc.constant.enumtype;

import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import lombok.Getter;

/**
 * @author wangdingfu
 * @Descption 请求类型
 * @date 2022-04-27 21:50:46
 */
@Getter
public enum RequestType {

    GET("GET", new String[]{AnnotationConstants.GET_MAPPING}),
    POST("POST", new String[]{AnnotationConstants.POST_MAPPING}),
    PUT("PUT", new String[]{AnnotationConstants.PUT_MAPPING, AnnotationConstants.PATCH_MAPPING}),
    DELETE("DELETE", new String[]{AnnotationConstants.DELETE_MAPPING});


    private final String requestType;

    private final String[] annotations;


    RequestType(String requestType, String[] annotations) {
        this.requestType = requestType;
        this.annotations = annotations;
    }

    public static String getByAnnotationName(String annotationName) {
        for (RequestType value : RequestType.values()) {
            String[] annotations = value.getAnnotations();
            for (String annotation : annotations) {
                if (annotation.equals(annotationName)) {
                    return value.getRequestType();
                }
            }
        }
        return null;
    }

    public static RequestType getRequestType(String requestType) {
        for (RequestType value : RequestType.values()) {
            if (value.getRequestType().equals(requestType)) {
                return value;
            }
        }
        return null;
    }
}
