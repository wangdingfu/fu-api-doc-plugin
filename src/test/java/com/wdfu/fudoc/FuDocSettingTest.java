package com.wdfu.fudoc;

import com.google.common.collect.Lists;
import com.wdf.fudoc.config.FreeMarkerConfig;
import com.wdf.fudoc.pojo.data.FuDocEnumItemData;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author wangdingfu
 * @Description
 * @Date 2022-08-06 21:42:27
 */
public class FuDocSettingTest {

    public static void main(String[] args) {
        String templateName = "enum_template.ftl";
        String templateContent = "\n" +
                "<#if itemList??>\n" +
                "<#list itemList as item>`${item.code!''}-${item.msg!''}` </#list>\n" +
                "</#if>\n" +
                "\n";
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("itemList", Lists.newArrayList(new FuDocEnumItemData("1","测试数据"),new FuDocEnumItemData("2","测试数据2")));
        String content = FreeMarkerConfig.generateContent(templateName, templateContent, resultMap);
        System.out.println(content);
    }
}
