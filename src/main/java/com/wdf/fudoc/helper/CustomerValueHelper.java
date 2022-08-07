package com.wdf.fudoc.helper;

import com.wdf.fudoc.constant.enumtype.CommentTagType;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.data.SettingDynamicValueData;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.pojo.data.CommentTagData;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author wangdingfu
 * @date 2022-08-06 23:16:57
 */
public class CustomerValueHelper {


    public static Map<String, Object> customerValue(BaseInfoDesc baseInfoDesc, FuDocContext fuDocContext) {
        Map<String, Object> fuDocMap = new HashMap<>();
        SettingData settingData = fuDocContext.getSettingData();
        List<SettingDynamicValueData> customerValueList = settingData.getCustomerSettingData().getSetting_customer_value();
        if (CollectionUtils.isNotEmpty(customerValueList)) {
            for (SettingDynamicValueData dynamicValueData : customerValueList) {
                fuDocMap.put(dynamicValueData.getAlias(), getValue(baseInfoDesc, dynamicValueData));
            }
        }
        ApiDocCommentData commentData = baseInfoDesc.getCommentData();

        Map<String, List<CommentTagData>> tagMap;
        if (Objects.nonNull(commentData) && MapUtils.isNotEmpty(tagMap = commentData.getTagMap())) {
            tagMap.forEach((key, value) -> {
                if (CollectionUtils.isEmpty(value)) {
                    return;
                }
                CommentTagType commentTagType = CommentTagType.getEnum(key);
                for (CommentTagData tagData : value) {
                    if (Objects.isNull(commentTagType)) {
                        autoPaddingComment(key, tagData, fuDocMap);
                        continue;
                    }
                    String tagDataName = tagData.getName();
                    String tagDataValue = tagData.getValue();
                    int type = commentTagType.getType();
                    if (CommentTagType.hasKey(type)) {
                        fuDocMap.put(name(tagDataName, 0, fuDocMap), tagDataValue);
                    }
                    if (CommentTagType.hasValue(type)) {
                        fuDocMap.put(name(key, 0, fuDocMap), tagDataValue);
                    }
                    if (CommentTagType.hasReference(type)) {
                        fuDocMap.put(name(key, 0, fuDocMap), tagDataValue);
                    }
                }
            });
        }
        return fuDocMap;
    }


    private static void autoPaddingComment(String tagName, CommentTagData tagData, Map<String, Object> fuDocMap) {
        if (StringUtils.isNotBlank(tagName) && Objects.nonNull(tagData) && Objects.nonNull(fuDocMap)) {
            String name = tagData.getName();
            String value = tagData.getValue();
            if (StringUtils.isNotBlank(name)) {
                fuDocMap.put(name(name, 0, fuDocMap), value);
            } else {
                //值为空 则不获取值
                fuDocMap.put(name(tagName, 0, fuDocMap), value);
            }

        }
    }


    private static String name(String name, int row, Map<String, Object> fudocMap) {
        if (fudocMap.containsKey(name)) {
            row++;
            return name(name + row, row, fudocMap);
        }
        return name;
    }


    private static String getValue(BaseInfoDesc baseInfoDesc, SettingDynamicValueData dynamicValueData) {
        String type = dynamicValueData.getType();
        if ("annotation".equals(type)) {
            return getValueFromAnnotation(baseInfoDesc, dynamicValueData.getValue());
        }
        return StringUtils.EMPTY;
    }


    private static String getValueFromAnnotation(BaseInfoDesc baseInfoDesc, String value) {
        String[] split;
        if (StringUtils.isNotBlank(value) && value.contains("#") && (split = value.split("#")).length == 2) {
            String annotationName = split[0];
            String annotationAttr = split[1];
            Optional<AnnotationData> annotation;
            if (StringUtils.isNotBlank(annotationName) && StringUtils.isNotBlank(annotationAttr) && (annotation = baseInfoDesc.getAnnotation(annotationName)).isPresent()) {
                AnnotationData annotationData = annotation.get();
                return annotationData.constant(annotationAttr).stringValue();
            }
        }
        return StringUtils.EMPTY;
    }
}
