package com.wdf.fudoc.common;

import com.wdf.fudoc.apidoc.config.FreeMarkerConfig;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.data.SettingData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocEnumData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @descption: 根据数据渲染生成MarkDown接口文档
 * @date 2022-05-30 23:20:00
 */
public class FuDocRender {


    /**
     * 将多个接口文档数据一起渲染成markdown格式接口文档内容
     *
     * @param dataList 接口文档数据集合
     * @return markdown格式的接口文档内容
     */
    public static String markdownRender(SettingData settingData, List<FuDocItemData> dataList) {
        StringBuilder sb = new StringBuilder();
        for (FuDocItemData fuDocItemData : dataList) {
            sb.append(markdownRender(fuDocItemData, settingData)).append("\r\n");
        }
        return sb.toString();
    }


    /**
     * 根据组装好的数据渲染成markdown接口文档内容
     *
     * @param fuDocItemData 组装完毕的接口文档数据
     * @return 渲染完毕后markdown接口文档内容
     */
    public static String markdownRender(FuDocItemData fuDocItemData, SettingData settingData) {
        return render(fuDocItemData, "fu_doc.ftl", settingData.getFuDocTemplateValue());
    }


    public static String paramRender(List<FuDocParamData> fuDocParamDataList, SettingData settingData) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestParams", fuDocParamDataList);
        return render(map, "fu_doc_object.ftl", settingData.getObjectTemplateValue());
    }


    public static String enumRender(FuDocEnumData fuDocEnumData, Integer type) {
        SettingData settingData = FuDocSetting.getSettingData();
        String template = YesOrNo.YES.getCode() == type ? "fu_doc_enum.ftl" : "fu_doc_enum_table.ftl";
        String templateContent = YesOrNo.YES.getCode() == type ? settingData.getEnumTemplateValue1() : settingData.getEnumTemplateValue2();
        return render(fuDocEnumData, template, templateContent);
    }

    public static String render(Object data, String templateName) {
        return FreeMarkerConfig.generateContent(data, templateName);
    }

    public static String render(Object data, String templateName, String templateContent) {
        return FreeMarkerConfig.generateContent(templateName, templateContent, data);
    }
}
