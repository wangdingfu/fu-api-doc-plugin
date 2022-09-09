package com.wdfu.fudoc;

import cn.hutool.json.JSONUtil;
import com.wdf.fudoc.apidoc.data.TemplateData;

/**
 * @author wangdingfu
 * @date 2022-08-07 01:33:21
 */
public class FreemarkerContentTest {

    public static void main(String[] args) {
        TemplateData templateData = new TemplateData();
        System.out.println(JSONUtil.toJsonStr(templateData));
    }
}
