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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UICreateEvent;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import java.util.Objects;
import java.util.Set;

/**
 * Custom provider for possible extension
 */
public class CubaUIProvider extends DefaultUIProvider {
    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    @Override
    public String getTheme(UICreateEvent event) {
        // get theme from cookies before app ui initialized for smooth theme enabling
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

    @Override
    public PushMode getPushMode(UICreateEvent event) {
        WebConfig webConfig = configuration.getConfig(WebConfig.class);

        if (!webConfig.getPushEnabled()) {
            return PushMode.DISABLED;
        }

        return super.getPushMode(event);
    }

    @Override
    public Transport getPushTransport(UICreateEvent event) {
        WebConfig webConfig = configuration.getConfig(WebConfig.class);

        if (webConfig.getUsePushLongPolling()) {
            return Transport.LONG_POLLING;
        }

        return super.getPushTransport(event);
    }
}