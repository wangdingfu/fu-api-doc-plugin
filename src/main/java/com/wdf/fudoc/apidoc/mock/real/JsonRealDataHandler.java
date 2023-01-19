package com.wdf.fudoc.apidoc.mock.real;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * JSON格式的数据
 *
 * @author wangdingfu
 * @date 2023-01-16 22:48:28
 */
public class JsonRealDataHandler implements MockRealData {

    private JSONObject jsonRealData;
    private JSONArray arrayRealData;

    public JsonRealDataHandler(String json) {
        if (StringUtils.isNotBlank(json)) {
            if (JSONUtil.isTypeJSONObject(json)) {
                this.jsonRealData = JSONUtil.parseObj(json);
            }
            if (JSONUtil.isTypeJSONArray(json)) {
                this.arrayRealData = JSONUtil.parseArray(json);
            }
        }
    }

    public JsonRealDataHandler(Object data) {
        this(Objects.nonNull(data) ? JSONUtil.toJsonStr(data) : null);
    }

    @Override
    public Object getData(String fieldName) {
        if (Objects.nonNull(this.jsonRealData)) {
            if (StringUtils.isBlank(fieldName)) {
                return this.jsonRealData;
            }
            return this.jsonRealData.getByPath(fieldName);
        }
        if (Objects.nonNull(this.arrayRealData) && !this.arrayRealData.isEmpty()) {
            Object firstRaw = this.arrayRealData.get(0);
            if (StringUtils.isBlank(fieldName)) {
                return firstRaw;
            }
            return new JsonRealDataHandler(firstRaw).getData(fieldName);
        }
        return null;
    }
}
