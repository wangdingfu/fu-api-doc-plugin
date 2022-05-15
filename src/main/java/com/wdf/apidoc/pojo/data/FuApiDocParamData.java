package com.wdf.apidoc.pojo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author wangdingfu
 * @descption: 接口文档参数
 * @date 2022-05-09 23:46:33
 */
@Getter
@Setter
public class FuApiDocParamData {

    /**
     * 参数名前缀
     */
    private String paramPrefix;

    /**
     * 参数名
     */
    private String paramName;
    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 是否必填
     */
    private String paramRequire;

    /**
     * 参数描述信息
     */
    private String paramDesc;

    /**
     * 分组排序
     */
    private String groupSort;

    /**
     * 扩展信息
     */
    private Map<String, Object> extInfo;


}
