package com.wdf.apidoc.pojo.context;

import com.intellij.openapi.project.Project;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @descption: 生成API接口文档全局上下文
 * @author wangdingfu
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

}
