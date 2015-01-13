/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UICreateEvent;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import java.util.Objects;
import java.util.Set;

/**
 * Custom provider for possible extension
 *
 * @author artamonov
 * @version $Id$
 */
public class CubaUIProvider extends DefaultUIProvider {

    @Override
    public String getTheme(UICreateEvent event) {
        // get theme from cookies before app ui initialized for smooth theme enabling

        Configuration configuration = AppBeans.get(Configuration.NAME);
        WebConfig webConfig = configuration.getConfig(WebConfig.class);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);

        String appWindowTheme = webConfig.getAppWindowTheme();
        String userAppTheme = getCookieValue(event.getRequest().getCookies(),
                App.APP_THEME_COOKIE_PREFIX + globalConfig.getWebContextName());
        if (userAppTheme != null) {
            if (!StringUtils.equals(userAppTheme, appWindowTheme)) {
                // check theme support
                ThemeConstantsRepository themeRepository = AppBeans.get(ThemeConstantsRepository.NAME);
                Set<String> supportedThemes = themeRepository.getAvailableThemes();
                if (supportedThemes.contains(userAppTheme)) {

                    return userAppTheme;
                }
            }
        }

        return super.getTheme(event);
    }

    protected String getCookieValue(Cookie[] cookies, String key) {
        if (cookies == null || key == null) {
            return null;
        }
        for (final Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), key)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}