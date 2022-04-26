package com.wdf.apidoc.pojo.context;

import com.intellij.openapi.project.Project;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 生成API接口文档全局上下文
 * @date 2022-04-05 19:53:44
 */
@Getter
@Setter
public class ApiDocContext {

    /**
     * 当前项目
     */
    private Project project;

    /**
     * 存放每一个参数解析后的数据对象
     * key: 参数对象全路径
     * value: 当前参数对象解析后的属性数据
     */
    private Map<String, ApiDocObjectData> apiParamDataMap;


    public ApiDocObjectData getApiDocObjectData(String key) {
        if (Objects.nonNull(apiParamDataMap)) {
            return apiParamDataMap.get(key);
        }
        return null;
    }


    public void add(String key, ApiDocObjectData apiDocObjectData) {
        if (StringUtils.isNotBlank(key) && Objects.nonNull(apiDocObjectData)) {
            if (Objects.isNull(this.apiParamDataMap)) {
                this.apiParamDataMap = new HashMap<>();
            }
            this.apiParamDataMap.put(key, apiDocObjectData);
        }
    }

}
