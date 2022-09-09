package com.wdf.fudoc.apidoc.config;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;

/**
 * @author wangdingfu
 * @Descption
 * @date 2022-05-26 19:51:23
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
        return process(configuration, templateName, data);
    }


    /**
     * 传入指定的模板 将数据渲染到模板中
     *
     * @param templateName       模板名称
     * @param freemarkerTemplate 模板内容
     * @param data               数据
     * @return 渲染后的模板内容
     */
    public static String generateContent(String templateName, String freemarkerTemplate, Object data) {
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(templateName, freemarkerTemplate);
        Configuration cfg = new Configuration(Configuration.getVersion());
        cfg.setTemplateLoader(stringLoader);
        return process(cfg, templateName, data);
    }


    private static String process(Configuration cfg, String templateName, Object data) {
        try {
            Template template = cfg.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("{}生成模板文件失败", templateName, e);
            return null;
        }
    }


}
