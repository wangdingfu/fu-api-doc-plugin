package com.wdf.fudoc.apidoc.assemble.impl;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.assemble.AbstractAssembleService;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.pojo.bo.AssembleBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.util.PathUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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


    /**
     * 组装信息-类层信息
     *
     * @param fuDocContext  【FU DOC】全局上下文对象
     * @param classInfoDesc 类描述信息
     * @return 解析出类这一层的信息返回出去 用于在解析方法层时使用
     */
    @Override
    protected AssembleBO doAssembleInfoByClass(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        AssembleBO assembleBO = new AssembleBO();
        if (CollectionUtils.isNotEmpty(classInfoDesc.getMethodList())) {
            classInfoDesc.getAnnotation(AnnotationConstants.REQUEST_MAPPING).ifPresent(annotationData -> {
                assembleBO.setControllerUrlList(annotationData.array().constant().stringValue());
            });
        }
        return assembleBO;
    }


    /**
     * 组织方法层信息
     *
     * @param fuDocContext   【FU DOC】全局上下文对象
     * @param methodInfoDesc 方法描述信息
     * @param fuDocItemData  具体每一个接口渲染的数据对象
     * @param assembleBO     组装参数信息
     * @return true: 该方法可以生成接口文档  false: 该方法过滤掉 不生成接口文档
     */
    @Override
    protected boolean doAssembleInfoMethod(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData, AssembleBO assembleBO) {
        for (String annotationName : AnnotationConstants.MAPPING) {
            Optional<AnnotationData> annotationOptional = methodInfoDesc.getAnnotation(annotationName);
            if (annotationOptional.isPresent()) {
                AnnotationData annotationData = annotationOptional.get();
                String qualifiedName = annotationData.getQualifiedName();
                String requestType;
                if (AnnotationConstants.REQUEST_MAPPING.equals(qualifiedName)) {
                    //requestMapping
                    requestType = annotationData.enumValue(FuDocConstants.AnnotationAttr.METHOD).getEnumValue();
                    if (StringUtils.isBlank(requestType)) {
                        requestType = RequestType.GET.getRequestType();
                    }
                } else {
                    requestType = RequestType.getByAnnotationName(qualifiedName);
                }
                fuDocItemData.setRequestType(requestType);
                fuDocItemData.setUrlList(joinUrl(assembleBO.getControllerUrlList(), annotationData.array().constant().stringValue()));
                return true;
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
