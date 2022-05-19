package com.wdf.apidoc.mock;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.wdf.apidoc.constant.enumtype.CommonObjectType;
import com.wdf.apidoc.constant.enumtype.ContentType;
import com.wdf.apidoc.pojo.bo.MockDataValueBO;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @descption: mock数据抽象类
 * @date 2022-05-17 23:44:20
 */
public abstract class AbstractApiDocMockData implements ApiDocMockData {


    /**
     * mock指定类型的数据
     *
     * @param classType java class类型
     * @param name      字段名
     * @param <T>       泛型
     * @return mock的数据
     */
    protected abstract <T> T mock(Class<T> classType, String name);


    /**
     * mock java对象数据
     *
     * @param contentType        请求数据类型(http内容类型)
     * @param objectInfoDescList 对象描述信息
     * @return mock数据内容
     */
    @Override
    public String mock(ContentType contentType, List<ObjectInfoDesc> objectInfoDescList) {
        if (Objects.nonNull(contentType)) {
            switch (contentType) {
                case URLENCODED:
                    return mockUrlEncodedData(objectInfoDescList);
                case JSON:
                    return new Gson().toJson(mockJsonData(objectInfoDescList));
            }
        }
        return StringUtils.EMPTY;
    }


    private String mockUrlEncodedData(List<ObjectInfoDesc> objectInfoDescList) {
        if (CollectionUtils.isEmpty(objectInfoDescList)) {
            return StringUtils.EMPTY;
        }
        List<MockDataValueBO> mockDataValueBOList = Lists.newArrayList();
        for (ObjectInfoDesc objectInfoDesc : objectInfoDescList) {
            List<ObjectInfoDesc> childList = objectInfoDesc.getChildList();
            if (CollectionUtils.isNotEmpty(childList)) {
                return mockUrlEncodedData(childList);
            }
            MockDataValueBO mockDataValueBO = mockCommonType(objectInfoDesc);
            if (Objects.nonNull(mockDataValueBO)) {
                mockDataValueBOList.add(mockDataValueBO);
            }
        }
        String value = null;
        if (CollectionUtils.isNotEmpty(mockDataValueBOList)) {
            value = mockDataValueBOList.stream().map(m -> m.getParamName() + "=" + m.getValue()).collect(Collectors.joining("&"));
        }
        return Objects.isNull(value) ? StringUtils.EMPTY : "?" + value;
    }


    private Map<String, Object> mockJsonData(List<ObjectInfoDesc> objectInfoDescList) {
        Map<String, Object> map = new HashMap<>(objectInfoDescList.size());
        if (CollectionUtils.isNotEmpty(objectInfoDescList)) {
            for (ObjectInfoDesc objectInfoDesc : objectInfoDescList) {
                List<ObjectInfoDesc> childList = objectInfoDesc.getChildList();
                if (CollectionUtils.isNotEmpty(childList)) {
                    map.put(objectInfoDesc.getName(), mockJsonData(childList));
                }
                MockDataValueBO mockDataValueBO = mockCommonType(objectInfoDesc);
                if (Objects.nonNull(mockDataValueBO)) {
                    map.put(mockDataValueBO.getParamName(), mockDataValueBO.getValue());
                }
            }
        }
        return map;
    }


    /**
     * mock常用的基本数据类型(不递归遍历 只mock当前层级的参数)
     *
     * @param objectInfoDesc 对象信息描述
     * @return mock后的数据
     */
    private MockDataValueBO mockCommonType(ObjectInfoDesc objectInfoDesc) {
        String type;
        if (Objects.isNull(objectInfoDesc) || StringUtils.isBlank(type = objectInfoDesc.getType())) {
            return null;
        }
        Class<?> clazz = CommonObjectType.getClass(type);
        //没有可以mock的数据类型
        if (Objects.isNull(clazz)) {
            return null;
        }
        return new MockDataValueBO(objectInfoDesc.getName(), mock(clazz, objectInfoDesc.getName()));
    }


}
