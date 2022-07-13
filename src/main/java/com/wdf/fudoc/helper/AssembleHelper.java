package com.wdf.fudoc.helper;

import com.google.common.collect.Lists;
import com.wdf.fudoc.assemble.handler.ParamValueExecutor;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 组装接口文档帮助类
 * @Date 2022-06-23 21:23:01
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
                FuDocParamData fuDocParamData = null;
                if (filter(objectInfoDesc)) {
                    fuDocParamData = new FuDocParamData();
                    String parentParamNo = Objects.nonNull(parent) ? parent.getParamNo() : StringUtils.EMPTY;
                    fuDocParamData.setParentParamNo(parentParamNo);
                    fuDocParamData.setParamNo(StringUtils.isNotBlank(parentParamNo) ? parentParamNo + i : i + "");
                    fuDocParamData.setParamName(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.PARAM_NAME, objectInfoDesc));
                    fuDocParamData.setParamDesc(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.PARAM_COMMENT, objectInfoDesc));
                    fuDocParamData.setParamType(ParamValueExecutor.doGetValue(fuDocContext, ParamValueType.PARAM_TYPE_VIEW, objectInfoDesc));
                    if (Objects.nonNull(parent)) {
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
     * 过滤哪些参数是不需要渲染的
     *
     * @param objectInfoDesc 参数描述信息
     * @return true 需要渲染出去  false 直接过滤该参数
     */
    private static boolean filter(ObjectInfoDesc objectInfoDesc) {
        if (StringUtils.isBlank(objectInfoDesc.getName())) {
            return false;
        }
        if (objectInfoDesc.getBooleanValue(FuDocConstants.ModifierProperty.FINAL)) {
            return false;
        }
        if (objectInfoDesc.getBooleanValue(FuDocConstants.ModifierProperty.STATIC)) {
            return false;
        }
        if (objectInfoDesc.getBooleanValue(FuDocConstants.ExtInfo.IS_ATTR)) {
            return true;
        }
        return false;
    }
}
