package com.wdf.fudoc.request.global;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局请求数据
 *
 * @author wangdingfu
 * @date 2022-09-18 20:38:42
 */
@Getter
@Setter
public class GlobalRequestData {

    /**
     * 当前项目激活的接口
     */
    private String activeApiKey;

    /**
     * 请求数据集合
     * key: 【apiKey】 组成格式:moduleId+`controller url`+`#methodName`+`(请求参数类型...)`
     * value:当前接口的http请求所需的数据
     */
    private Map<String, String> requestDataMap = new ConcurrentHashMap<>();

    /**
     * 最近请求记录
     * key:moduleId value:当前module最近的请求记录key
     */
    private Map<String, List<String>> recentRequestKeyMap = new ConcurrentHashMap<>();


}
