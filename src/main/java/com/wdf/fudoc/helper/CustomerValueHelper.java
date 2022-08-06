package com.wdf.fudoc.helper;

import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.data.SettingDynamicValueData;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangdingfu
 * @date 2022-08-06 23:16:57
 */
public class CustomerValueHelper {


    public static Map<String, Object> customerValue(BaseInfoDesc baseInfoDesc, FuDocContext fuDocContext) {
        Map<String, Object> extInfoMap = new HashMap<>();
        SettingData settingData = fuDocContext.getSettingData();
        List<SettingDynamicValueData> customerValueList = settingData.getCustomerSettingData().getSetting_customer_value();
        if (CollectionUtils.isNotEmpty(customerValueList)) {
            for (SettingDynamicValueData dynamicValueData : customerValueList) {
                extInfoMap.put(dynamicValueData.getAlias(), getValue(baseInfoDesc, dynamicValueData));
            }
        }
        return extInfoMap;
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
