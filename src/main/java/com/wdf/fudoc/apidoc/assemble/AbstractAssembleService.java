package com.wdf.fudoc.apidoc.assemble;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.assemble.handler.ParamValueExecutor;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
import com.wdf.fudoc.apidoc.constant.enumtype.MockResultType;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.data.FuDocRootParamData;
import com.wdf.fudoc.apidoc.helper.AssembleHelper;
import com.wdf.fudoc.apidoc.helper.CustomerValueHelper;
import com.wdf.fudoc.apidoc.helper.MockDataHelper;
import com.wdf.fudoc.apidoc.mock.MockDataService;
import com.wdf.fudoc.apidoc.pojo.annotation.*;
import com.wdf.fudoc.apidoc.pojo.bo.AssembleBO;
import com.wdf.fudoc.apidoc.pojo.bo.MockResultBo;
import com.wdf.fudoc.apidoc.pojo.bo.RootParamBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.*;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author wangdingfu
 * @descption: 组装抽象类
 * @date 2022-05-09 23:32:00
 */
public abstract class AbstractAssembleService implements FuDocAssembleService {

    /**
     * 组装【类|接口】层信息
     *
     * @param fuDocContext  【FU DOC】全局上下文对象
     * @param classInfoDesc 类描述信息
     * @return 组装产生的一些参数信息
     */
    protected AssembleBO doAssembleInfoByClass(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        return new AssembleBO();
    }


    /**
     * 将一个方法描述信息对象组装一个具体接口文档所需要的数据对象
     *
     * @param fuDocContext   【FU DOC】全局上下文对象
     * @param methodInfoDesc 方法描述信息
     * @param commonItemData 具体每一个接口渲染的数据对象
     * @param assembleBO     组装参数信息
     * @return true 该方法需要组装成接口文档  false 不组装该方法
     */
    protected abstract boolean doAssembleInfoMethod(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, CommonItemData commonItemData, AssembleBO assembleBO);


    /**
     * 根据java类描述信息组装生成接口文档的数据
     *
     * @param fuDocContext  【FU DOC】全局上下文对象
     * @param classInfoDesc java类描述信息(包含注解、注释、字段等信息)
     * @return 接口文档页面需要渲染的数据
     */
    @Override
    public List<FuDocItemData> assemble(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        List<FuDocItemData> resultList = Lists.newArrayList();
        List<MethodInfoDesc> methodList = classInfoDesc.getMethodList();
        AssembleBO assembleBO = doAssembleInfoByClass(fuDocContext, classInfoDesc);
        if (CollectionUtils.isNotEmpty(methodList)) {
            int classNo = fuDocContext.getClassNo(classInfoDesc.getClassId());
            int docNo = 0;
            for (MethodInfoDesc methodInfoDesc : methodList) {
                FuDocItemData fuDocItemData = new FuDocItemData();
                if (doAssembleInfoMethod(fuDocContext, methodInfoDesc, fuDocItemData, assembleBO)) {
                    fuDocItemData.setDocNo(classNo + "." + (docNo++));
                    //组装公共信息
                    assembleCommonInfo(fuDocContext, methodInfoDesc, fuDocItemData);
                    //设置接口全局唯一标识
                    fuDocItemData.setApiKey(FuDocUtils.genApiKey(FuDocUtils.getModuleId(ModuleUtil.findModuleForPsiElement(classInfoDesc.getPsiClass())), methodInfoDesc.getMethodId()));
                    //组装接口文档相关信息
                    assembleItemInfo(fuDocContext, methodInfoDesc, fuDocItemData);
                    resultList.add(fuDocItemData);
                }
            }
        }
        return resultList;
    }


    /**
     * 【Fu Request】模块组装请求数据
     *
     * @param fuDocContext  【FU DOC】全局上下文对象
     * @param classInfoDesc java类描述信息(包含注解、注释、字段等信息)
     * @return 发起http请求需要的参数
     */
    @Override
    public List<FuDocRootParamData> requestAssemble(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        List<FuDocRootParamData> resultList = Lists.newArrayList();
        List<MethodInfoDesc> methodList = classInfoDesc.getMethodList();
        AssembleBO assembleBO = doAssembleInfoByClass(fuDocContext, classInfoDesc);
        if (CollectionUtils.isNotEmpty(methodList)) {
            for (MethodInfoDesc methodInfoDesc : methodList) {
                FuDocRootParamData rootParamData = new FuDocRootParamData();
                if (doAssembleInfoMethod(fuDocContext, methodInfoDesc, rootParamData, assembleBO)) {
                    //组装公共信息
                    assembleCommonInfo(fuDocContext, methodInfoDesc, rootParamData);
                    assembleFuRequest(fuDocContext, methodInfoDesc, rootParamData);
                    resultList.add(rootParamData);
                }
            }
        }
        return resultList;
    }

    /**
     * 组装接口文档相关信息
     */
    protected void assembleItemInfo(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData) {
        //组装请求参数
        List<ObjectInfoDesc> requestList = methodInfoDesc.getRequestList();
        if (CollectionUtils.isNotEmpty(requestList)) {
            fuDocItemData.setRequestParams(AssembleHelper.assembleParamData(fuDocContext, requestList, null));
        }
        //组装返回参数
        ObjectInfoDesc response = methodInfoDesc.getResponse();
        if (Objects.nonNull(response)) {
            fuDocItemData.setResponseParams(AssembleHelper.assembleParamData(fuDocContext, Lists.newArrayList(response), null));
        }
        //mock数据
        MockDataService mockDataService = ServiceHelper.getService(MockDataService.class);
        MockResultBo mockResultBo = mockDataService.mockData(methodInfoDesc, fuDocItemData);
        fuDocItemData.setRequestExample(mockResultBo.getRequestExample());
        fuDocItemData.setRequestExampleType(mockResultBo.getRequestExampleType());
        fuDocItemData.setResponseExample(mockResultBo.getResponseExample());
        fuDocItemData.setResponseExampleType(mockResultBo.getResponseExampleType());
        //组装扩展数据
        fuDocItemData.setFudoc(CustomerValueHelper.customerValue(methodInfoDesc, fuDocContext));
    }

    /**
     * 组装公共参数
     */
    protected void assembleCommonInfo(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, CommonItemData commonItemData) {
        //组装接口标题等参数
        ApiDocCommentData commentData = methodInfoDesc.getCommentData();
        if (Objects.nonNull(commentData)) {
            String title = ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.METHOD_TITLE, methodInfoDesc);
            commonItemData.setTitle(StringUtils.isNotBlank(title) ? title : PsiClassUtils.getMethodName(methodInfoDesc.getPsiMethod()));
            commonItemData.setDetailInfo(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.METHOD_DETAIL_INFO, methodInfoDesc));


        }
    }


    /**
     * 组装【Fu Request】数据
     */
    private void assembleFuRequest(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, FuDocRootParamData fuDocRootParamData) {
        //设置api唯一标识
        fuDocRootParamData.setApiId(methodInfoDesc.getMethodId());
        fuDocRootParamData.setPsiMethod(methodInfoDesc.getPsiMethod());
        List<RootParamBO> rootParamBOList = Lists.newArrayList();
        fuDocRootParamData.setRootParamBOList(rootParamBOList);
        List<ObjectInfoDesc> requestList = methodInfoDesc.getRequestList();
        if (CollectionUtils.isNotEmpty(requestList)) {
            RequestType requestType = RequestType.getRequestType(fuDocRootParamData.getRequestType());
            //查找根节点请求参数
            for (ObjectInfoDesc objectInfoDesc : requestList) {
                if (!objectInfoDesc.getBooleanValue(FuDocConstants.ExtInfo.ROOT)) {
                    continue;
                }
                RootParamBO rootParamBO = new RootParamBO();
                rootParamBO.setSpringAnnotationData(findRootParamSpringAnnotation(objectInfoDesc));
                rootParamBO.setFuDocParamDataList(AssembleHelper.assembleParamData(fuDocContext, Lists.newArrayList(objectInfoDesc), null));
                rootParamBO.setMockData(MockDataHelper.mockRequestData(requestType, Lists.newArrayList(objectInfoDesc)));
                rootParamBOList.add(rootParamBO);
            }
        }
    }


    private SpringAnnotationData findRootParamSpringAnnotation(ObjectInfoDesc objectInfoDesc) {
        //根节点
        if (objectInfoDesc.exists(AnnotationConstants.REQUEST_BODY)) {
            return new RequestBodyData();
        }
        if (objectInfoDesc.exists(AnnotationConstants.PATH_VARIABLE)) {
            return new PathVariableData(objectInfoDesc.get(AnnotationConstants.PATH_VARIABLE));
        }
        if (objectInfoDesc.exists(AnnotationConstants.REQUEST_PARAM)) {
            return new RequestParamData(objectInfoDesc.get(AnnotationConstants.REQUEST_PARAM));
        }
        return new DefaultAnnotationData();
    }


}
