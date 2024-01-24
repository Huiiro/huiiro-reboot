package com.huii.generator.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

/**
 * 模板配置
 *
 * @author huii
 */
public class TemplateConfig {

    public static Configuration generator() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(TemplateConfig.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");
        return cfg;
    }

    public static Configuration generator(String templatePath) throws TemplateModelException {
        Configuration cfg = generator();
        cfg.setSharedVariable("packagePath", templatePath);
        return cfg;
    }
}
