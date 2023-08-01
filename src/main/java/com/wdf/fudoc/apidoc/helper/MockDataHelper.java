package com.wdf.fudoc.apidoc.helper;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.FastJsonUtils;
import com.wdf.fudoc.util.MapListUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @Descption mock数据帮助类
 * @date 2022-06-13 10:13:54
 */
public class MockDataHelper {


    /**
     * mock请求参数数据
     *
     * @param requestType      请求类型
     * @param requestParamList 请求参数数据对象集合
     * @return 请求参数mock数据字符串
     */
    public static String mockRequestData(RequestType requestType, List<ObjectInfoDesc> requestParamList) {
        return Objects.isNull(requestType) ? mockJsonData(requestParamList) : mock(requestType, groupByRequestAnnotation(requestParamList));
    }


    /**
     * mock GET请求类型的数据
     *
     * @param requestParamList 请求参数集合
     * @return mock后的请求数据示例内容
     */
    private static String mockGetData(List<ObjectInfoDesc> requestParamList) {
        if (CollectionUtils.isEmpty(requestParamList)) {
            return StringUtils.EMPTY;
        }
        String value = requestParamList.stream().map(MockDataHelper::formatValue).collect(Collectors.joining("&"));
        return StringUtils.isBlank(value) ? StringUtils.EMPTY : "?" + value;
    }


    /**
     * mock JSON格式数据的字符串
     *
     * @param objectInfoDescList 请求参数数据对象集合
     * @return 美化后的json数据字符串
     */
    public static String mockJsonData(List<ObjectInfoDesc> objectInfoDescList) {
        JSONObject jsonObject = new JSONObject();
        if (CollectionUtils.isNotEmpty(objectInfoDescList)) {
            if (objectInfoDescList.size() == 1) {
                ObjectInfoDesc objectInfoDesc = objectInfoDescList.get(0);
                Object value = objectInfoDesc.getValue();
                if (value instanceof JSONObject) {
                    return FastJsonUtils.toJsonString(value);
                }
            }
            for (ObjectInfoDesc objectInfoDesc : objectInfoDescList) {
                add(objectInfoDesc, jsonObject);
            }
        }
        return FastJsonUtils.toJsonString(jsonObject);
    }


    private static void add(ObjectInfoDesc objectInfoDesc, JSONObject jsonObject) {
        String name = objectInfoDesc.getName();
        Object value = objectInfoDesc.getValue();
        if (StringUtils.isNotBlank(name) && Objects.nonNull(value)) {
            jsonObject.set(name, value);
        }
    }


    private static MapListUtil<String, ObjectInfoDesc> groupByRequestAnnotation(List<ObjectInfoDesc> objectInfoDescList) {
        MapListUtil<String, ObjectInfoDesc> instance = MapListUtil.getInstance();
        for (ObjectInfoDesc objectInfoDesc : objectInfoDescList) {
            boolean flag = false;
            if (objectInfoDesc.exists(AnnotationConstants.REQUEST_BODY)) {
                flag = true;
                instance.add(AnnotationConstants.REQUEST_BODY, objectInfoDesc);
            }
            if (objectInfoDesc.exists(AnnotationConstants.PATH_VARIABLE)) {
                flag = true;
            }
            if (objectInfoDesc.exists(AnnotationConstants.REQUEST_PARAM)) {
                flag = true;
                instance.add(AnnotationConstants.REQUEST_PARAM, objectInfoDesc);
            }
            if (!flag) {
                instance.add(AnnotationConstants.REQUEST_PARAM, objectInfoDesc);
            }
        }
        return instance;
    }


    private static String mock(RequestType requestType, MapListUtil<String, ObjectInfoDesc> instance) {
        if (Objects.nonNull(requestType)) {
            switch (requestType) {
                case GET:
                    //只mock RequestParam注解标识的请求参数
                    return mockGetData(instance.get(AnnotationConstants.REQUEST_PARAM));
                case DELETE:
                    List<ObjectInfoDesc> getList = instance.get(AnnotationConstants.REQUEST_PARAM);
                    if (CollectionUtils.isNotEmpty(getList)) {
                        return mockGetData(getList);
                    }
                    return mockJsonData(instance.get(AnnotationConstants.REQUEST_BODY));
                default:
                    List<ObjectInfoDesc> postList = instance.get(AnnotationConstants.REQUEST_BODY);
                    if (CollectionUtils.isNotEmpty(postList)) {
                        return mockJsonData(postList);
                    }
                    return mockGetData(instance.get(AnnotationConstants.REQUEST_PARAM));
            }
        }

        return StringUtils.EMPTY;
    }


    private static String formatValue(ObjectInfoDesc objectInfoDesc) {
        FuDocObjectType fuDocObjectType;
        Object value;
        if (Objects.nonNull(fuDocObjectType = objectInfoDesc.getFuDocObjectType())
                && Objects.nonNull(value = objectInfoDesc.getValue())) {
            if (YesOrNo.YES.equals(isSimpleType(fuDocObjectType))) {
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


    private static String buildExpress(String name, Object value) {
        if (StringUtils.isNotBlank(name) && Objects.nonNull(value)) {
            return name + "=" + mockStringValue(value);
        }
        return StringUtils.EMPTY;
    }


    public static String mockStringValue(Object value) {
        if (Objects.nonNull(value)) {
            if (value instanceof Collection<?>) {
                return StringUtils.join(Lists.newArrayList((Collection<?>) value), ",");
            }
            if (ArrayUtil.isArray(value)) {
                String arrayStr = ArrayUtil.toString(value);
                return StringUtils.substring(arrayStr, 1, arrayStr.length() - 2);
            }
            return value.toString();
        }
        return StringUtils.EMPTY;
    }


    private static YesOrNo isSimpleType(FuDocObjectType fuDocObjectType) {
        if (Objects.isNull(fuDocObjectType)) {
            return YesOrNo.NO;
        }
        switch (fuDocObjectType) {
            case ARRAY:
            case PRIMITIVE:
            case COMMON_OBJECT:
                return YesOrNo.YES;
            default:
                return YesOrNo.NO;
        }
    }
}
