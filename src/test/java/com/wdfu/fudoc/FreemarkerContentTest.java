package com.wdfu.fudoc;

import com.alibaba.fastjson.JSON;
import com.wdf.fudoc.data.TemplateData;

/**
 * @author wangdingfu
 * @date 2022-08-07 01:33:21
 */
public class FreemarkerContentTest {

    public static void main(String[] args) {
        TemplateData templateData = new TemplateData();
        System.out.println(JSON.toJSON(templateData));
    }
}