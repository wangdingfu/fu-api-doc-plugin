package com.wdf.fudoc.assemble;

import com.google.common.collect.Lists;
import com.wdf.fudoc.assemble.handler.ParamValueExecutor;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.data.FuApiDocItemData;
import com.wdf.fudoc.pojo.data.FuApiDocParamData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 组装抽象类
 * @date 2022-05-09 23:32:00
 */
public abstract class AbstractAssembleService implements FuDocAssembleService {


    /**
     * 构建渲染接口文档参数的数据对象
     *
     * @param objectInfoDescList 对象解析后的描述信息集合
     * @return 接口文档页面显示的参数数据
     */
    protected List<FuApiDocParamData> buildFuApiDocParamData(List<ObjectInfoDesc> objectInfoDescList) {
        List<FuApiDocParamData> fuApiDocParamDataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(objectInfoDescList)) {
            fuApiDocParamDataList.addAll(buildFuApiDocParamData(objectInfoDescList, null));
        }
        return fuApiDocParamDataList;
    }


    protected List<FuApiDocParamData> buildFuApiDocParamData(List<ObjectInfoDesc> objectInfoDescList, FuApiDocParamData parent) {
        List<FuApiDocParamData> resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(objectInfoDescList)) {
            for (int i = 0; i < objectInfoDescList.size(); i++) {
                ObjectInfoDesc objectInfoDesc = objectInfoDescList.get(i);
                FuApiDocParamData fuApiDocParamData = null;
                if (filter(objectInfoDesc)) {
                    fuApiDocParamData = new FuApiDocParamData();
                    String parentParamNo = Objects.nonNull(parent) ? parent.getParamNo() : StringUtils.EMPTY;
                    fuApiDocParamData.setParentParamNo(parentParamNo);
                    fuApiDocParamData.setParamNo(StringUtils.isNotBlank(parentParamNo) ? parentParamNo + i : i + "");
                    fuApiDocParamData.setParamName(ParamValueExecutor.doGetValue(ParamValueType.PARAM_NAME, objectInfoDesc));
                    fuApiDocParamData.setParamDesc(ParamValueExecutor.doGetValue(ParamValueType.PARAM_COMMENT, objectInfoDesc));
                    fuApiDocParamData.setParamType(ParamValueExecutor.doGetValue(ParamValueType.PARAM_TYPE_VIEW, objectInfoDesc));
                    if (Objects.nonNull(parent)) {
                        String paramPrefix = parent.getParamPrefix();
                        fuApiDocParamData.setParamPrefix(StringUtils.isBlank(paramPrefix) ? "└─" : "&emsp;&ensp;" + paramPrefix);
                    }
                    fuApiDocParamData.setParamRequire(ParamValueExecutor.doGetValue(ParamValueType.PARAM_REQUIRE, objectInfoDesc));
                    resultList.add(fuApiDocParamData);
                }
                List<ObjectInfoDesc> childList = objectInfoDesc.getChildList();
                if (CollectionUtils.isNotEmpty(childList)) {
                    resultList.addAll(buildFuApiDocParamData(childList, fuApiDocParamData));
                }
            }
        }
        return resultList;
    }


    protected boolean filter(ObjectInfoDesc objectInfoDesc) {
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
