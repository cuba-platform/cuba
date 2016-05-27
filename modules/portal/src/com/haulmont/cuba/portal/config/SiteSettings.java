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

package com.haulmont.cuba.portal.config;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@Component("cuba_PortalSiteSettings")
public class SiteSettings {

    /**
     * Basically this method prepends webapp's prefix to the path
     *
     * @param path path relative to the root of webapp
     * @return Full relative path on server
     */
    public String composeFullRelativePath(String path) {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        String webAppPrefix = "/".concat(globalConfig.getWebContextName().intern());
        return path.startsWith("/") ? webAppPrefix.concat(path) : webAppPrefix.concat("/").concat(path);
    }

    /**
     * @param path path relative to the root of webapp
     * @return Full absolute path including protocol, domain and webapp prefix
     */
    public String composeFullAbsolutePath(String path) {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        String webAppUrl = configuration.getConfig(GlobalConfig.class).getWebAppUrl();
        webAppUrl = StringUtils.chomp(webAppUrl, "/"); //remove last slash
        return path.startsWith("/") ? webAppUrl.concat(path) : webAppUrl.concat("/").concat(path);
    }

    public Properties getFreeMarkerSettings() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        Map<String,Locale> availableLocales = globalConfig.getAvailableLocales();
        if (availableLocales.isEmpty())
            throw new IllegalStateException("Property cuba.availableLocales is not configured");

        Locale locale = availableLocales.values().iterator().next();
        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);

        final Properties freemarkerSettings = new Properties();
        freemarkerSettings.setProperty("number_format", "#");
        freemarkerSettings.setProperty("datetime_format", formatStrings.getDateTimeFormat());
        freemarkerSettings.setProperty("date_format", formatStrings.getDateFormat());
        freemarkerSettings.setProperty("template_exception_handler", "rethrow");
        return freemarkerSettings;
    }
}