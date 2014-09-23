/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.config;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * @author minaev
 * @version $Id$
 */
@ManagedBean("cuba_PortalSiteSettings")
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