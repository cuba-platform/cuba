/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.settings;

import com.google.common.base.Optional;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.ApplicationSession;
import com.haulmont.cuba.gui.settings.SettingsClient;
import com.haulmont.cuba.security.app.UserSettingService;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User settings provider for desktop application. Caches settings in desktop app per user session.
 *
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(SettingsClient.NAME)
public class DesktopSettingsClient implements SettingsClient {

    @Inject
    protected UserSettingService userSettingService;

    @Override
    public String getSetting(String name) {
        Map<String, Optional<String>> settings = getCache();
        Optional<String> cached = settings.get(name);
        if (cached != null) {
            return cached.orNull();
        }

        String setting = userSettingService.loadSetting(ClientType.DESKTOP, name);
        settings.put(name, Optional.fromNullable(setting));

        return setting;
    }

    @Override
    public void setSetting(String name, @Nullable String value) {
        getCache().put(name, Optional.fromNullable(value));
        userSettingService.saveSetting(ClientType.DESKTOP, name, value);
    }

    @Override
    public void deleteSettings(String name) {
        getCache().put(name, Optional.<String>absent());
        userSettingService.deleteSettings(ClientType.DESKTOP, name);
    }

    protected Map<String, Optional<String>> getCache() {
        ApplicationSession session = App.getInstance().getApplicationSession();
        if (session == null) {
            LogFactory.getLog(getClass()).warn("Application disconnected, used fake empty session");
            return new ConcurrentHashMap<>();
        }

        @SuppressWarnings("unchecked")
        Map<String, Optional<String>> settings = (Map<String, Optional<String>>) session.getAttribute(SettingsClient.NAME);
        if (settings == null) {
            settings = new ConcurrentHashMap<>();
            session.setAttribute(SettingsClient.NAME, settings);
        }
        return settings;
    }
}