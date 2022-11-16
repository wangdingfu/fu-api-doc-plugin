package com.wdf.fudoc.apidoc.helper;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.assemble.handler.ParamValueExecutor;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 组装接口文档帮助类
 * @date 2022-06-23 21:23:01
 */
public class AssembleHelper {


    /**
     * 组装接口文档参数【请求参数|响应参数】 用于渲染到markdown文本上
     *
     * @param objectInfoDescList 请求参数描述信息集合
     * @param parent             父级参数 为null则代表本次组装为一级参数
     * @return 返回已经组装完毕可以用于渲染到markdown文本上的数据
     */
    public static List<FuDocParamData> assembleParamData(FuDocContext fuDocContext, List<ObjectInfoDesc> objectInfoDescList, FuDocParamData parent) {
        List<FuDocParamData> resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(objectInfoDescList)) {
            for (int i = 0; i < objectInfoDescList.size(); i++) {
                ObjectInfoDesc objectInfoDesc = objectInfoDescList.get(i);
                FuDocParamData fuDocParamData = new FuDocParamData();
                if (exclude(objectInfoDesc)) {
                    continue;
                }
                if (objectInfoDesc.getBooleanValue(FuDocConstants.ExtInfo.IS_ATTR)) {
                    String parentParamNo = Objects.isNull(parent) ? StringUtils.EMPTY : parent.getParamNo();
                    fuDocParamData.setParentParamNo(parentParamNo);
                    fuDocParamData.setParamNo(StringUtils.isNotBlank(parentParamNo) ? parentParamNo + i : i + "");
                    fuDocParamData.setParamName(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.PARAM_NAME, objectInfoDesc));
                    fuDocParamData.setParamDesc(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.PARAM_COMMENT, objectInfoDesc));
                    fuDocParamData.setParamType(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.PARAM_TYPE_VIEW, objectInfoDesc));
                    Object value = objectInfoDesc.getValue();
                    fuDocParamData.setParamValue(Objects.nonNull(value) ? value.toString() : StringUtils.EMPTY);
                    fuDocParamData.setFudoc(CustomerValueHelper.customerValue(objectInfoDesc, fuDocContext));
                    if (Objects.nonNull(parent) && StringUtils.isNotBlank(parent.getParamNo())) {
                        String paramPrefix = parent.getParamPrefix();
                        fuDocParamData.setParamPrefix(StringUtils.isBlank(paramPrefix) ? "└─" : "&emsp;&ensp;" + paramPrefix);
                    }
                    fuDocParamData.setParamRequire(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.PARAM_REQUIRE, objectInfoDesc));
                    resultList.add(fuDocParamData);
                }
                List<ObjectInfoDesc> childList = objectInfoDesc.getChildList();
                if (CollectionUtils.isNotEmpty(childList)) {
                    resultList.addAll(assembleParamData(fuDocContext, childList, fuDocParamData));
                }
            }
        }
        return resultList;
    }

    /**
     * 排除参数（过滤不需要渲染到接口文档的参数）
     *
     * @param objectInfoDesc 参数描述对象
     * @return true 该参数需要排除 无需渲染到接口文档
     */
    private static boolean exclude(ObjectInfoDesc objectInfoDesc) {
        return StringUtils.isBlank(objectInfoDesc.getName()) || objectInfoDesc.getBooleanValue(FuDocConstants.ModifierProperty.FINAL) || objectInfoDesc.getBooleanValue(FuDocConstants.ModifierProperty.STATIC);
    }
}
