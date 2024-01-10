package com.wdf.fudoc.apidoc.mock.real;

import com.wdf.fudoc.components.bo.KeyValueTableBO;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GET请求的数据、POST请求中的form-data、x-www-urlencoded格式数据
 *
 * @author wangdingfu
 * @date 2023-01-16 22:25:04
 */
public class KeyValueRealDataHandler implements MockRealData {

    /**
     * 真实请求过的示例数据
     */
    private final Map<String, String> REAL_DATA_MAP = new ConcurrentHashMap<>();

    public KeyValueRealDataHandler(List<KeyValueTableBO> dataList) {
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.forEach(f -> REAL_DATA_MAP.put(f.getKey(), f.getValue()));
        }
    }

    @Override
    public Object getData(String fieldName) {
        if (FuStringUtils.isBlank(fieldName)) {
            return FuStringUtils.EMPTY;
        }
        return REAL_DATA_MAP.get(fieldName);
    }
}
