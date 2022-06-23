package com.wdf.fudoc.assemble;

import com.google.common.collect.Lists;
import com.wdf.fudoc.assemble.handler.ParamValueExecutor;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.constant.enumtype.RequestType;
import com.wdf.fudoc.helper.AssembleHelper;
import com.wdf.fudoc.helper.FuDocNoGenHelper;
import com.wdf.fudoc.helper.MockDataHelper;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 组装抽象类
 * @date 2022-05-09 23:32:00
 */
public abstract class AbstractAssembleService implements FuDocAssembleService {


    @Override
    public List<FuDocItemData> assemble(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        List<FuDocItemData> resultList = Lists.newArrayList();
        List<MethodInfoDesc> methodList = classInfoDesc.getMethodList();
        if (CollectionUtils.isNotEmpty(methodList)) {
            for (MethodInfoDesc methodInfoDesc : methodList) {
                FuDocItemData fuDocItemData = new FuDocItemData();
                fuDocItemData.setDocNo(FuDocNoGenHelper.genNo(methodInfoDesc.getMethodId()));
                assembleCommonInfo(fuDocContext, methodInfoDesc, fuDocItemData);
                resultList.add(fuDocItemData);
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
    }

}
