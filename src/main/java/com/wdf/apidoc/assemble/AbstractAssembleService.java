package com.wdf.apidoc.assemble;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.constant.ApiDocConstants;
import com.wdf.apidoc.constant.enumtype.ApiDocObjectType;
import com.wdf.apidoc.constant.enumtype.ContentType;
import com.wdf.apidoc.constant.enumtype.YesOrNo;
import com.wdf.apidoc.pojo.data.AnnotationData;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.data.FuApiDocParamData;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author wangdingfu
 * @descption: 组装抽象类
 * @date 2022-05-09 23:32:00
 */
public abstract class AbstractAssembleService implements ApiDocAssembleService {


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
        if (objectInfoDesc.getBooleanValue(ApiDocConstants.ModifierProperty.FINAL)) {
            return false;
        }
        if (objectInfoDesc.getBooleanValue(ApiDocConstants.ModifierProperty.STATIC)) {
            return false;
        }
        if (objectInfoDesc.getBooleanValue(ApiDocConstants.ExtInfo.IS_ATTR)) {
            return true;
        }
        return false;
    }


    protected void mockRequestData(FuApiDocItemData fuApiDocItemData, List<ObjectInfoDesc> objectInfoDescList) {
        Map<ContentType, List<ObjectInfoDesc>> map = new HashMap<>();
        for (ObjectInfoDesc objectInfoDesc : objectInfoDescList) {
            ContentType contentType = getContentType(objectInfoDesc);
            List<ObjectInfoDesc> itemList = map.get(contentType);
            if (Objects.isNull(itemList)) {
                itemList = Lists.newArrayList();
                map.put(contentType, itemList);
            }
            itemList.add(objectInfoDesc);
        }
        //如果存在RequestBody注解 则优先mock该注解标识的对象
        List<ObjectInfoDesc> itemList = map.get(ContentType.JSON);
        if (CollectionUtils.isNotEmpty(itemList)) {
            fuApiDocItemData.setRequestExample(mockJsonData(itemList));
        }


    }


    protected String mockData(ObjectInfoDesc objectInfoDesc) {
        return mockData(getContentType(objectInfoDesc), objectInfoDesc);
    }


    protected ContentType getContentType(ObjectInfoDesc objectInfoDesc) {
        if (objectInfoDesc.exists(AnnotationConstants.REQUEST_BODY)) {
            return ContentType.JSON;
        }
        if (objectInfoDesc.exists(AnnotationConstants.PATH_VARIABLE)) {
            return ContentType.URLENCODED1;
        }
        return ContentType.URLENCODED;
    }

    protected String mockData(ContentType contentType, ObjectInfoDesc objectInfoDesc) {
        if (Objects.nonNull(contentType)) {
            switch (contentType) {
                case URLENCODED:
                    return mockUrlEncodedData(objectInfoDesc);
                case URLENCODED1:

                case JSON:
                    return mockJsonData(Lists.newArrayList(objectInfoDesc));
            }
        }
        return StringUtils.EMPTY;
    }


    private String mockUrlEncodedData(ObjectInfoDesc objectInfoDesc) {
        if (Objects.isNull(objectInfoDesc)) {
            return StringUtils.EMPTY;
        }
        String value = formatValue(objectInfoDesc);
        return StringUtils.isBlank(value) ? StringUtils.EMPTY : "?" + value;
    }

    private String formatValue(ObjectInfoDesc objectInfoDesc) {
        ApiDocObjectType apiDocObjectType;
        Object value;
        if (Objects.nonNull(objectInfoDesc)
                && Objects.nonNull(apiDocObjectType = objectInfoDesc.getApiDocObjectType())
                && Objects.nonNull(value = objectInfoDesc.getValue())) {
            if (YesOrNo.YES.equals(isSimpleType(apiDocObjectType))) {
                return buildExpress(objectInfoDesc.getName(), value.toString());
            }
            if (value instanceof JSONObject) {
                JSONObject data = (JSONObject) value;
                List<String> expressList = Lists.newArrayList();
                data.forEach((k, v) -> expressList.add(buildExpress(k, v)));
                return CollectionUtils.isEmpty(expressList) ? StringUtils.EMPTY
                        : StringUtils.join(expressList, "&");
            }
        }
        return StringUtils.EMPTY;
    }

    private String buildExpress(String name, Object value) {
        if (StringUtils.isNotBlank(name) && Objects.nonNull(value)) {
            return name + "=" + value;
        }
        return StringUtils.EMPTY;
    }


    /**
     * 递归mock对象 返回一个mock后的json结构数据
     *
     * @param objectInfoDescList 对象描述信息
     * @return mock后的数据
     */
    protected String mockJsonData(List<ObjectInfoDesc> objectInfoDescList) {
        JSONObject jsonObject = new JSONObject();
        if (CollectionUtils.isNotEmpty(objectInfoDescList)) {
            if (objectInfoDescList.size() == 1) {
                ObjectInfoDesc objectInfoDesc = objectInfoDescList.get(0);
                Object value = objectInfoDesc.getValue();
                if (value instanceof JSONObject) {
                    return ((JSONObject) value).toJSONString();
                }
            }
            for (ObjectInfoDesc objectInfoDesc : objectInfoDescList) {
                add(objectInfoDesc, jsonObject);
            }
        }
        return jsonObject.toJSONString();
    }


    private void add(ObjectInfoDesc objectInfoDesc, JSONObject jsonObject) {
        if (filter(objectInfoDesc)) {
            String name = objectInfoDesc.getName();
            Object value = objectInfoDesc.getValue();
            if (StringUtils.isNotBlank(name) && Objects.nonNull(value)) {
                jsonObject.put(name, value);
            }
        }
    }


    private YesOrNo isSimpleType(ApiDocObjectType apiDocObjectType) {
        if (Objects.isNull(apiDocObjectType)) {
            return YesOrNo.NO;
        }
        switch (apiDocObjectType) {
            case ARRAY:
            case PRIMITIVE:
            case COMMON_OBJECT:
                return YesOrNo.YES;
            case DEFAULT_OBJECT:
            case COLLECTION_OBJECT:
            case MAP_OBJECT:
            default:
                return YesOrNo.NO;
        }
    }
}
