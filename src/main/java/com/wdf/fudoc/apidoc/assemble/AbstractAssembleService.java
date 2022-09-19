package com.wdf.fudoc.apidoc.assemble;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.assemble.handler.ParamValueExecutor;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.helper.AssembleHelper;
import com.wdf.fudoc.apidoc.helper.CustomerValueHelper;
import com.wdf.fudoc.apidoc.helper.MockDataHelper;
import com.wdf.fudoc.apidoc.pojo.bo.AssembleBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.common.constant.FuDocConstants;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
     * @param fuDocItemData  具体每一个接口渲染的数据对象
     * @param assembleBO     组装参数信息
     * @return true 该方法需要组装成接口文档  false 不组装该方法
     */
    protected abstract boolean doAssembleInfoMethod(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData, AssembleBO assembleBO);


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
                    resultList.add(fuDocItemData);
                }
            }
        }
        return resultList;
    }


    protected void assembleCommonInfo(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData) {
        //组装接口标题等参数
        ApiDocCommentData commentData = methodInfoDesc.getCommentData();
        if (Objects.nonNull(commentData)) {
            fuDocItemData.setTitle(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.METHOD_TITLE, methodInfoDesc));
            fuDocItemData.setDetailInfo(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.METHOD_DETAIL_INFO, methodInfoDesc));
        }

        //组装请求参数
        List<ObjectInfoDesc> requestList = methodInfoDesc.getRequestList();
        if (CollectionUtils.isNotEmpty(requestList)) {
            fuDocItemData.setRequestParams(AssembleHelper.assembleParamData(fuDocContext, requestList, null));
            //mock请求参数数据
            RequestType requestType = RequestType.getRequestType(fuDocItemData.getRequestType());
            fuDocItemData.setRequestExample(MockDataHelper.mockRequestData(requestType, requestList));
        }

        //组装返回参数
        ObjectInfoDesc response = methodInfoDesc.getResponse();
        if (Objects.nonNull(response)) {
            fuDocItemData.setResponseParams(AssembleHelper.assembleParamData(fuDocContext, Lists.newArrayList(response), null));
            //mock返回结果数据
            fuDocItemData.setResponseExample(MockDataHelper.mockJsonData(Lists.newArrayList(response)));
        }
        Map<String, Object> fuDoc = CustomerValueHelper.customerValue(methodInfoDesc, fuDocContext);
        fuDoc.put(FuDocConstants.API_ID, methodInfoDesc.getMethodId());
        //组装扩展数据
        fuDocItemData.setFudoc(fuDoc);

    }

}
