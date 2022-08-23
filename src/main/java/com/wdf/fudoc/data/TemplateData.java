package com.wdf.fudoc.data;

import cn.hutool.core.io.resource.ResourceUtil;

/**
 * 模板数据
 *
 * @author wangdingfu
 * @date 2022-08-07 01:08:34
 */
public class TemplateData {

    /**
     * fu doc 接口文档模板内容 请查看fu_doc.ftl
     */
    public static final String fuDocTemplateValue;
    public static final String objectTemplateValue;
    public static final String enumTemplateValue1;
    public static final String enumTemplateValue2;


    static {
        fuDocTemplateValue = ResourceUtil.readUtf8Str("template/fu_doc.ftl");
        objectTemplateValue = ResourceUtil.readUtf8Str("template/fu_doc_object.ftl");
        enumTemplateValue1 = ResourceUtil.readUtf8Str("template/fu_doc_enum.ftl");
        enumTemplateValue2 = ResourceUtil.readUtf8Str("template/fu_doc_enum_table.ftl");
    }


}
