package com.wdf.fudoc.data;

import lombok.Getter;
import lombok.Setter;



/**
 * @Author wangdingfu
 * @Description 配置数据
 * @Date 2022-08-06 22:44:50
 */
@Getter
@Setter
public class SettingData {

    /**
     * 自定义配置数据
     */
    private CustomerSettingData customerSettingData;

    /**
     * fu doc 接口文档模板内容 请查看fu_doc.ftl
     */
    private String fuDocTemplateValue;
    private String objectTemplateValue;
    private String enumTemplateValue1;
    private String enumTemplateValue2;


}
