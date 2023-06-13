package com.wdf.fudoc.apidoc.constant.enumtype;

import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import icons.FuDocIcons;
import lombok.Getter;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @Descption 请求类型
 * @date 2022-04-27 21:50:46
 */
@Getter
public enum RequestType {

    ALL("ALL", FuDocIcons.ALL_DARK,AnnotationConstants.REQUEST_MAPPING),
    GET("GET", FuDocIcons.GET_DARK,AnnotationConstants.GET_MAPPING),
    POST("POST", FuDocIcons.POST_DARK,AnnotationConstants.POST_MAPPING),
    PUT("PUT", FuDocIcons.PUT_DARK,AnnotationConstants.PUT_MAPPING),
    PATCH("PATCH", FuDocIcons.PUT_DARK,AnnotationConstants.PATCH_MAPPING),
    DELETE("DELETE", FuDocIcons.DELETE_DARK,AnnotationConstants.DELETE_MAPPING);


    private final String requestType;

    private final Icon icon;

    private final String annotations;



    RequestType(String requestType, Icon icon, String annotations) {
        this.requestType = requestType;
        this.icon = icon;
        this.annotations = annotations;
    }

    public static RequestType getByAnnotationName(String annotationName) {
        for (RequestType value : RequestType.values()) {
            String annotation = value.getAnnotations();
            if (annotation.equals(annotationName)) {
                return value;
            }
        }
        return RequestType.ALL;
    }

    public static RequestType getRequestType(String requestType) {
        for (RequestType value : RequestType.values()) {
            if (value.getRequestType().equals(requestType)) {
                return value;
            }
        }
        return null;
    }


    public static String[] getItems(){
        return Arrays.stream(RequestType.values()).filter(f->!RequestType.ALL.equals(f)).map(RequestType::getRequestType).toList().toArray(new String[]{});
    }
}
