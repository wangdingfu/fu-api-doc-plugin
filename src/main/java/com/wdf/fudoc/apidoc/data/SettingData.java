package com.wdf.fudoc.apidoc.data;

import com.wdf.fudoc.util.ResourceUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;


/**
 * Fu Doc 需要持久化的数据对象
 *
 * @author wangdingfu
 * @date 2022-08-06 22:44:50
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
    private String yapiTemplateValue;


    public static SettingData defaultSettingData() {
        SettingData settingData = new SettingData();
        settingData.defaultValue();
        return settingData;
    }


    public void defaultValue() {
        if (StringUtils.isBlank(this.fuDocTemplateValue)) {
            this.setFuDocTemplateValue(ResourceUtils.readResource("template/fu_doc.ftl"));
        }
        if (StringUtils.isBlank(this.objectTemplateValue)) {
            this.setObjectTemplateValue(ResourceUtils.readResource("template/fu_doc_object.ftl"));
        }
        if (StringUtils.isBlank(this.enumTemplateValue1)) {
            this.setEnumTemplateValue1(ResourceUtils.readResource("template/fu_doc_enum.ftl"));
        }
        if (StringUtils.isBlank(this.enumTemplateValue2)) {
            this.setEnumTemplateValue2(ResourceUtils.readResource("template/fu_doc_enum_table.ftl"));
        }
        if (Objects.isNull(this.yapiTemplateValue)) {
            this.setYapiTemplateValue(ResourceUtils.readResource("template/yapi.ftl"));
        }
        if (Objects.isNull(this.customerSettingData)) {
            this.customerSettingData = new CustomerSettingData();
        }
        this.customerSettingData.defaultValue();
    }

}
