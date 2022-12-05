package com.wdf.fudoc.request.manager;

import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.global.GlobalRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.state.FuRequestState;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-10-08 17:19:54
 */
public class FuRequestManager {


    /**
     * 保存请求记录
     *
     * @param fuHttpRequestData http请求数据对象
     */
    public static void saveRequest(Project project, FuHttpRequestData fuHttpRequestData) {
        if (Objects.isNull(fuHttpRequestData)) {
            return;
        }
        GlobalRequestData data = FuRequestState.getData(project);
        Map<String, String> requestDataMap = data.getRequestDataMap();
        requestDataMap.put(fuHttpRequestData.getApiKey(), JSONUtil.toJsonStr(fuHttpRequestData));
    }


    /**
     * 从内存中获取指定接口的数据信息
     *
     * @param project 当前项目对象
     * @param apiKey  接口唯一标识
     * @return 之前请求过则会返回上一次请求的数据  否则返回null
     */
    public static FuHttpRequestData getRequest(Project project, String apiKey) {
        GlobalRequestData data = FuRequestState.getData(project);
        Map<String, String> requestDataMap = data.getRequestDataMap();
        String entries = requestDataMap.get(apiKey);
        if (StringUtils.isBlank(entries)) {
            return null;
        }
        return JSONUtil.toBean(entries, FuHttpRequestData.class);
    }

}
