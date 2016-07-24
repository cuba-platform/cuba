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

package com.haulmont.cuba.web.settings;

import com.google.common.base.Optional;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.gui.settings.SettingsClient;
import com.haulmont.cuba.security.app.UserSettingService;
import com.vaadin.server.VaadinSession;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User settings provider for web application. Caches settings in HTTP session.
 */
@Component(SettingsClient.NAME)
public class WebSettingsClient implements SettingsClient {

    @Inject
    protected UserSettingService userSettingService;

    @Override
    public String getSetting(String name) {
        Map<String, Optional<String>> settings = getCache();
        //noinspection Guava
        Optional<String> cached = settings.get(name);
        if (cached != null) {
            return cached.orNull();
        }

        String setting = userSettingService.loadSetting(ClientType.WEB, name);
        settings.put(name, Optional.fromNullable(setting));

        return setting;
    }

    @Override
    public void setSetting(String name, @Nullable String value) {
        getCache().put(name, Optional.fromNullable(value));
        userSettingService.saveSetting(ClientType.WEB, name, value);
    }

    @Override
    public void deleteSettings(String name) {
        getCache().put(name, Optional.absent());
        userSettingService.deleteSettings(ClientType.WEB, name);
    }

    public void clearCache() {
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute(SettingsClient.NAME, null);
    }

    protected Map<String, Optional<String>> getCache() {
        VaadinSession session = VaadinSession.getCurrent();
        @SuppressWarnings("unchecked")
        Map<String, Optional<String>> settings = (Map<String, Optional<String>>) session.getAttribute(SettingsClient.NAME);
        if (settings == null) {
            settings = new ConcurrentHashMap<>();
            session.setAttribute(SettingsClient.NAME, settings);
        }
        return settings;
    }
}