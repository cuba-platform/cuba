/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.02.2010 12:13:21
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import freemarker.cache.StringTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ManagedBean
public class FreemarkerProcessor {

    private Configuration cfg;

    private static final Pattern RTF_VAR_PATTERN = Pattern.compile("\\$\\\\\\{\\S*\\\\\\}");

    @Inject
    public void setConfig(ConfigProvider configProvider) {
        this.cfg = new Configuration();

        GlobalConfig conf = configProvider.doGetConfig(GlobalConfig.class);
        try {
            cfg.setDirectoryForTemplateLoading(new File(conf.getConfDir()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String processTemplate(String name, Map<String, Object> params) {
        return processTemplate(name, params, null);
    }

    public String processTemplate(String name, Map<String, Object> params, String outputCharset) {
        if (cfg == null)
            throw new IllegalStateException("Freemarker is not initialized");

        Template templ;
        try {
            if (name.endsWith(".rtf") || name.endsWith(".RTF")) {
                GlobalConfig conf = ConfigProvider.getConfig(GlobalConfig.class);
                String templateStr = preprocessRtf(FileUtils.readFileToString(new File(conf.getConfDir(), name)));

                Configuration configuration = new Configuration();
                StringTemplateLoader templateLoader = new StringTemplateLoader();
                templateLoader.putTemplate("template", templateStr);
                configuration.setTemplateLoader(templateLoader);
                templ = configuration.getTemplate("template");

                if (outputCharset == null) outputCharset = configuration.getDefaultEncoding();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Writer w = new OutputStreamWriter(out, outputCharset);
                Environment env = templ.createProcessingEnvironment(params, w);
                env.setOutputEncoding(outputCharset);
                env.process();

                String result = new String(out.toByteArray(), outputCharset);
                return result;
            } else {
                templ = cfg.getTemplate(name);
                StringWriter writer = new StringWriter();
                templ.process(params, writer);
                String result = writer.toString();
                return result;
            }

        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String preprocessRtf(String src) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = RTF_VAR_PATTERN.matcher(src);
        while (matcher.find()) {
            String s = matcher.group();
            String repl = "\\${" + s.substring(3, s.length() - 2) + "}";
            matcher.appendReplacement(sb, repl);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
