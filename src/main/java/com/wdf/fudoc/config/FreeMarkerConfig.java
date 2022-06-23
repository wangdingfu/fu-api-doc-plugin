package com.wdf.fudoc.config;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;

/**
 * @author wangdingfu
 * @Descption
 * @Date 2022-05-26 19:51:23
 */
@Slf4j
public class FreeMarkerConfig {

    private static final Configuration configuration = new Configuration(Configuration.getVersion());

    static {
        try {
            configuration.setClassLoaderForTemplateLoading(FreeMarkerConfig.class.getClassLoader(), "/template/");
            configuration.setDefaultEncoding("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将数据渲染到模板中
     *
     * @param data         数据对象
     * @param templateName 模板名称
     * @return 渲染后的模板内容
     */
    public static String generateContent(Object data, String templateName) {
        try {
            Template template = configuration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("{}生成模板文件失败", templateName, e);
            return null;
        }
    }

}
