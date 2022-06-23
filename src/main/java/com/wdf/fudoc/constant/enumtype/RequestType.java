package com.wdf.fudoc.constant.enumtype;

import com.wdf.fudoc.constant.AnnotationConstants;
import lombok.Getter;

/**
 * @author wangdingfu
 * @Descption 请求类型
 * @Date 2022-04-27 21:50:46
 */
@Getter
public enum RequestType {

    GET("GET", new String[]{AnnotationConstants.GET_MAPPING, AnnotationConstants.REQUEST_MAPPING}),
    POST("POST", new String[]{AnnotationConstants.POST_MAPPING, AnnotationConstants.REQUEST_MAPPING}),
    PUT("PUT", new String[]{AnnotationConstants.PUT_MAPPING, AnnotationConstants.PATCH_MAPPING, AnnotationConstants.REQUEST_MAPPING}),
    DELETE("DELETE", new String[]{AnnotationConstants.DELETE_MAPPING, AnnotationConstants.REQUEST_MAPPING});


    private final String requestType;

    private final String[] annotations;


    RequestType(String requestType, String[] annotations) {
        this.requestType = requestType;
        this.annotations = annotations;
    }

    public static RequestType getByAnnotationName(String annotationName) {
        for (RequestType value : RequestType.values()) {
            String[] annotations = value.getAnnotations();
            for (String annotation : annotations) {
                if (annotation.equals(annotationName)) {
                    return value;
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
