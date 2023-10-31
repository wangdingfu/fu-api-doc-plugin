package com.wdf.fudoc.apidoc.helper;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.apidoc.sync.dto.YApiJsonSchema;
import com.wdf.fudoc.apidoc.sync.dto.YApiMock;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.api.util.JsonUtil;
import com.wdf.fudoc.util.MapListUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-06-13 10:19:08
 */
public class JsonSchemaHelper {


    /**
     * 构建json schema
     *
     * @param fuDocParamDataList 接口文档参数集合
     * @return json schema
     */
    public static String buildJsonSchemaStr(List<FuDocParamData> fuDocParamDataList) {
        return JsonUtil.toJson(buildJsonSchema(fuDocParamDataList));
    }


    /**
     * 构建json schema
     *
     * @param fuDocParamDataList 接口文档参数集合
     * @return json schema
     */
    public static YApiJsonSchema buildJsonSchema(List<FuDocParamData> fuDocParamDataList) {
        YApiJsonSchema jsonSchema = new YApiJsonSchema();
        jsonSchema.setType("object");
        if (CollectionUtils.isNotEmpty(fuDocParamDataList)) {
            MapListUtil<String, FuDocParamData> instance = MapListUtil.getInstance(fuDocParamDataList, FuDocParamData::getParentParamNo);
            YApiJsonSchema yApiJsonSchema = buildProperties(instance.get(FuDocConstants.ROOT), instance);
            jsonSchema.setProperties(yApiJsonSchema.getProperties());
            jsonSchema.setRequired(yApiJsonSchema.getRequired());
        }
        return jsonSchema;
    }

    /**
     * 构建一个对象下所有的参数 将这个对象构建出一个json schema
     *
     * @param childList 指定对象下所有的参数字段
     * @param instance  所有的参数
     * @return 指定对象的字段属性和是否必填
     */
    public static YApiJsonSchema buildProperties(List<FuDocParamData> childList, MapListUtil<String, FuDocParamData> instance) {
        YApiJsonSchema result = new YApiJsonSchema();
        List<String> required = Lists.newArrayList();
        Map<String, YApiJsonSchema> properties = new HashMap<>();
        if (CollectionUtils.isNotEmpty(childList)) {
            childList.forEach(f -> {
                properties.put(f.getParamName(), buildJsonSchema(f, instance));
                if (YesOrNo.YES.getDesc().equals(f.getParamRequire())) {
                    required.add(f.getParamName());
                }
            });
        }
        result.setProperties(properties);
        result.setRequired(required);
        return result;
    }


    /**
     * 递归遍历json schema
     *
     * @param fuDocParamData 当前处理的接口参数
     * @param instance       所有的接口参数
     * @return 当前参数的json schema
     */
    public static YApiJsonSchema buildJsonSchema(FuDocParamData fuDocParamData, MapListUtil<String, FuDocParamData> instance) {
        YApiJsonSchema jsonSchema = new YApiJsonSchema();
        String paramType = fuDocParamData.getParamType();
        if ("object".equals(paramType)) {
            //对象
            YApiJsonSchema yApiJsonSchema = buildProperties(instance.get(fuDocParamData.getParamNo()), instance);
            jsonSchema.setProperties(yApiJsonSchema.getProperties());
            jsonSchema.setRequired(yApiJsonSchema.getRequired());
        } else if ("array".equals(paramType)) {
            //组装items
            FuDocParamData item = new FuDocParamData();
            item.setParamNo(fuDocParamData.getParamNo());
            item.setParamDesc(fuDocParamData.getParamDesc());
            item.setParamValue(fuDocParamData.getParamValue());
            item.setParamType(fuDocParamData.getChildParamType());
            jsonSchema.setItems(buildJsonSchema(item, instance));
        } else {
            String paramValue = fuDocParamData.getParamValue();
            if (StringUtils.isNotBlank(paramValue)) {
                jsonSchema.setMock(new YApiMock(paramValue));
            }
        }
        jsonSchema.setType(paramType);
        jsonSchema.setDescription(fuDocParamData.getParamDesc());
        return jsonSchema;
    }


}
