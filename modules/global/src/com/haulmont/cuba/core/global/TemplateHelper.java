/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.global;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.sys.AppContext;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.collections4.map.LazyMap;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Use static methods of this class to process Freemarker templates.
 * Does not cache templates.
 */
public class TemplateHelper {
    public static String processTemplate(String templateStr, Map<String, ?> parameterValues) {
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("template", templateStr);
        return __processTemplate(templateLoader, "template", parameterValues);
    }

    public static String processTemplateFromFile(String templatePath, Map<String, Object> parameterValues) {
        final FileTemplateLoader templateLoader;
        try {
            String rootPath = AppContext.getProperty("cuba.templateRootDir");
            if (Strings.isNullOrEmpty(rootPath)) {
                rootPath = AppContext.getProperty("cuba.confDir");
            }
            if (Strings.isNullOrEmpty(rootPath)) {
                throw new IllegalStateException("Unable to get template root path");
            }
            templateLoader = new FileTemplateLoader(new File(rootPath), true);
        } catch (IOException e) {
            throw new RuntimeException("Unable to process template from file", e);
        }
        return __processTemplate(templateLoader, templatePath, parameterValues);
    }

    protected static String __processTemplate(TemplateLoader templateLoader, String templateName,
                                              Map<String, ?> parameterValues) {
        Map<String, Object> params = prepareParams(parameterValues);

        StringWriter writer = new StringWriter();

        try {
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setTemplateLoader(templateLoader);
            Template template = configuration.getTemplate(templateName);
            template.process(params, writer);

            return writer.toString();
        } catch (Throwable e) {
            throw new RuntimeException("Unable to process template", e);
        }
    }

    protected static Map<String, Object> prepareParams(Map<String, ?> parameterValues) {
        Map<String, Object> parameterValuesWithStats = new HashMap<>(parameterValues);
        BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
        parameterValuesWithStats.put("statics", beansWrapper.getStaticModels());

        @SuppressWarnings("unchecked")
        Map<String, Object> params = LazyMap.lazyMap(parameterValuesWithStats, propertyName -> {
            for (String appProperty : AppContext.getPropertyNames()) {
                if (appProperty.replace(".", "_").equals(propertyName)) {
                    return AppContext.getProperty(propertyName);
                }
            }
            return null;
        });

        return params;
    }
}