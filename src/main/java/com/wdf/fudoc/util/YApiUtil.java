package com.wdf.fudoc.util;

import cn.hutool.json.JSONUtil;
import com.wdf.api.util.JsonUtil;
import com.wdf.fudoc.apidoc.sync.dto.YApiBaseRes;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-01-06 01:11:30
 */
public class YApiUtil {

    public static boolean isSuccess(String result) {
        if (StringUtils.isNotBlank(result)) {
            Integer errorCode = JSONUtil.parse(result).getByPath("errcode", Integer.class);
            return Objects.nonNull(errorCode) && errorCode == 0;
        }
        return false;
    }


    public static <T> T getData(String result, Class<T> clazz) {
        YApiBaseRes yApiBaseRes = JsonUtil.fromJSON(result, YApiBaseRes.class);
        if (Objects.nonNull(yApiBaseRes) && yApiBaseRes.success()) {
            Object data = yApiBaseRes.getData();
            if (Objects.nonNull(data)) {
                return JsonUtil.fromJSON(JSONUtil.toJsonStr(data), clazz);
            }
        }
        return null;
    }


    public static <T> List<T> getDataList(String result, Class<T> clazz) {
        YApiBaseRes yApiBaseRes = JsonUtil.fromJSON(result, YApiBaseRes.class);
        if (Objects.nonNull(yApiBaseRes) && yApiBaseRes.success()) {
            Object data = yApiBaseRes.getData();
            if (Objects.nonNull(data)) {
                return JsonUtil.toList(JSONUtil.toJsonStr(data), clazz);
            }
        }
        return null;
    }
}
