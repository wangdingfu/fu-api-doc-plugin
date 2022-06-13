package com.wdf.apidoc.assemble;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.constant.FuDocConstants;
import com.wdf.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.apidoc.constant.enumtype.ContentType;
import com.wdf.apidoc.constant.enumtype.YesOrNo;
import com.wdf.apidoc.pojo.data.AnnotationData;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.data.FuApiDocParamData;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.apidoc.util.FastJsonUtils;
import com.wdf.apidoc.util.MapListUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
                    fuApiDocParamData.setParamName(objectInfoDesc.getName());
                    fuApiDocParamData.setParamDesc(objectInfoDesc.getDocText());
                    fuApiDocParamData.setParamType(objectInfoDesc.getTypeView());
                    if (Objects.nonNull(parent)) {
                        String paramPrefix = parent.getParamPrefix();
                        fuApiDocParamData.setParamPrefix(StringUtils.isBlank(paramPrefix) ? "└─" : "&emsp;&ensp;" + paramPrefix);
                    }
                    //设置是否必填
                    Optional<AnnotationData> annotation = objectInfoDesc.getAnnotation(AnnotationConstants.VALID_NOT);
                    fuApiDocParamData.setParamRequire(annotation.isPresent() ? "是" : "否");
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
