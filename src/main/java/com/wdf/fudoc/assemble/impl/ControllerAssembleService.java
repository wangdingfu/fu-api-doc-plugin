package com.wdf.fudoc.assemble.impl;

import com.google.common.collect.Lists;
import com.wdf.fudoc.assemble.AbstractAssembleService;
import com.wdf.fudoc.constant.AnnotationConstants;
import com.wdf.fudoc.constant.enumtype.RequestType;
import com.wdf.fudoc.pojo.bo.AssembleBO;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.util.PathUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wangdingfu
 * @descption: 组装Controller接口文档
 * @date 2022-05-09 23:32:39
 */
public class ControllerAssembleService extends AbstractAssembleService {


    /**
     * 判断当前实现类是否为Controller
     *
     * @param fuDocContext  【FU DOC】全局上下文对象
     * @param classInfoDesc java类信息描述对象
     * @return true:是  false:不是
     */
    @Override
    public boolean isAssemble(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        if (Objects.isNull(classInfoDesc)) {
            return false;
        }
        //判断是否有Controller|RestController注解
        return CollectionUtils.isNotEmpty(classInfoDesc.getMethodList()) && classInfoDesc.isController();
    }

    @Override
    protected AssembleBO doAssembleInfoByClass(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        AssembleBO assembleBO = new AssembleBO();
        if (CollectionUtils.isNotEmpty(classInfoDesc.getMethodList())) {
            classInfoDesc.getAnnotation(AnnotationConstants.REQUEST_MAPPING).ifPresent(annotationData -> {
                assembleBO.setControllerUrlList(annotationData.getValue().getListValue());
            });
        }
        return assembleBO;
    }

    @Override
    protected boolean doAssembleInfoMethod(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData, AssembleBO assembleBO) {
        for (String annotationName : AnnotationConstants.MAPPING) {
            Optional<AnnotationData> annotationOptional = methodInfoDesc.getAnnotation(annotationName);
            if (annotationOptional.isPresent()) {
                AnnotationData annotationData = annotationOptional.get();
                RequestType requestType = RequestType.getByAnnotationName(annotationData.getQualifiedName());
                if (Objects.nonNull(requestType)) {
                    fuDocItemData.setRequestType(requestType.getRequestType());
                }
                fuDocItemData.setUrlList(joinUrl(assembleBO.getControllerUrlList(), annotationData.getValue().getListValue()));
                break;
            }
        }
        return false;
    }

    /**
     * 拼接请求地址(将controller上的请求地址和方法上的请求地址拼接成一个完成的请求地址)
     *
     * @param controllerUrls controller上的请求地址集合
     * @param methodUrlList  方法体上的请求地址
     * @return 该请求存在的请求地址集合
     */
    private List<String> joinUrl(List<String> controllerUrls, List<String> methodUrlList) {
        if (CollectionUtils.isEmpty(controllerUrls)) {
            return methodUrlList;
        }
        List<String> urlList = Lists.newArrayList();
        for (String controllerUrl : controllerUrls) {
            for (String methodUrl : methodUrlList) {
                urlList.add(PathUtils.urlJoin(controllerUrl, methodUrl));
            }
        }
        return urlList;
    }
}
