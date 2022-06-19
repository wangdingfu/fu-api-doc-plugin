package com.wdf.apidoc.assemble.impl;

import com.google.common.collect.Lists;
import com.intellij.openapi.components.Service;
import com.wdf.apidoc.assemble.AbstractAssembleService;
import com.wdf.apidoc.assemble.handler.ParamValueExecutor;
import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.constant.enumtype.ParamValueType;
import com.wdf.apidoc.constant.enumtype.RequestType;
import com.wdf.apidoc.helper.MockDataHelper;
import com.wdf.apidoc.pojo.data.AnnotationData;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.apidoc.pojo.desc.MethodInfoDesc;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.apidoc.util.PathUtils;
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
     * @param classInfoDesc java类信息描述对象
     * @return true:是  false:不是
     */
    @Override
    public boolean isAssemble(ClassInfoDesc classInfoDesc) {
        if (Objects.isNull(classInfoDesc)) {
            return false;
        }
        //判断是否有Controller|RestController注解
        return CollectionUtils.isNotEmpty(classInfoDesc.getMethodList()) && classInfoDesc.isController();
    }

    /**
     * Controller类组装成接口文档
     *
     * @param classInfoDesc Controller类描述信息
     * @return 接口集合
     */
    @Override
    public List<FuApiDocItemData> assemble(ClassInfoDesc classInfoDesc) {
        List<FuApiDocItemData> resultList = Lists.newArrayList();
        List<MethodInfoDesc> methodList = classInfoDesc.getMethodList();
        if (CollectionUtils.isNotEmpty(methodList)) {
            //获取Controller类上的请求路径
            List<String> controllerUrlList = Lists.newArrayList();
            classInfoDesc.getAnnotation(AnnotationConstants.REQUEST_MAPPING).ifPresent(annotationData ->
                    controllerUrlList.addAll(annotationData.getValue().getListValue()));
            //解析方法
            int apiDocNo = 0;
            for (MethodInfoDesc methodInfoDesc : methodList) {
                if (methodInfoDesc.exists(AnnotationConstants.MAPPING)) {
                    FuApiDocItemData fuApiDocItemData = assembleItemApiDoc(methodInfoDesc, controllerUrlList);
                    apiDocNo++;
                    fuApiDocItemData.setApiDocNo(apiDocNo + "");
                    resultList.add(fuApiDocItemData);
                }
            }
        }
        return resultList;
    }


    /**
     * 组装接口文档（具体接口）
     *
     * @param methodInfoDesc    controller请求方法描述信息
     * @param controllerUrlList controller类上的请求地址集合
     * @return controller类里具体一个请求生成的接口文档
     */
    private FuApiDocItemData assembleItemApiDoc(MethodInfoDesc methodInfoDesc, List<String> controllerUrlList) {
        FuApiDocItemData fuApiDocItemData = new FuApiDocItemData();
        ApiDocCommentData commentData = methodInfoDesc.getCommentData();
        if (Objects.nonNull(commentData)) {
            fuApiDocItemData.setTitle(ParamValueExecutor.doGetValue(ParamValueType.METHOD_TITLE, methodInfoDesc));
            fuApiDocItemData.setDetailInfo(ParamValueExecutor.doGetValue(ParamValueType.METHOD_DETAIL_INFO, methodInfoDesc));
        }
        RequestType requestType = null;
        for (String annotationName : AnnotationConstants.MAPPING) {
            Optional<AnnotationData> annotationOptional = methodInfoDesc.getAnnotation(annotationName);
            if (annotationOptional.isPresent()) {
                AnnotationData annotationData = annotationOptional.get();
                requestType = RequestType.getByAnnotationName(annotationData.getQualifiedName());
                if (Objects.nonNull(requestType)) {
                    fuApiDocItemData.setRequestType(requestType.getRequestType());
                }
                fuApiDocItemData.setUrlList(joinUrl(controllerUrlList, annotationData.getValue().getListValue()));
                break;
            }
        }
        if (Objects.nonNull(requestType)) {
            //设置请求参数
            List<ObjectInfoDesc> requestList = methodInfoDesc.getRequestList();
            if (CollectionUtils.isNotEmpty(requestList)) {
                fuApiDocItemData.setRequestParams(buildFuApiDocParamData(requestList));
                //mock请求参数数据
                fuApiDocItemData.setRequestExample(MockDataHelper.mockRequestData(requestType, requestList));
            }
        }
        ObjectInfoDesc response = methodInfoDesc.getResponse();
        if (Objects.nonNull(response)) {
            fuApiDocItemData.setResponseParams(buildFuApiDocParamData(Lists.newArrayList(response)));
            //mock返回结果数据
            fuApiDocItemData.setResponseExample(MockDataHelper.mockJsonData(Lists.newArrayList(response)));
        }
        return fuApiDocItemData;
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
