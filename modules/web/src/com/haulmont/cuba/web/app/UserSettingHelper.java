/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.09.2009 15:30:10
 *
 * $Id$
 */
package com.haulmont.cuba.web.app;

import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.ConfigProvider;

public class UserSettingHelper {

    public static AppWindow.Mode loadAppWindowMode() {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.JNDI_NAME);
        String s = uss.loadSetting(ClientType.WEB, "appWindowMode");
        if (s != null) {
            if (AppWindow.Mode.SINGLE.name().equals(s)) {
                return AppWindow.Mode.SINGLE;
            } else if (AppWindow.Mode.TABBED.name().equals(s)) {
                return AppWindow.Mode.TABBED;
            }
        }
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
        return AppWindow.Mode.valueOf(webConfig.getAppWindowMode().toUpperCase());
    }

    public static void saveAppWindowMode(AppWindow.Mode mode) {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.JNDI_NAME);
        uss.saveSetting(ClientType.WEB, "appWindowMode", mode.name());
    }

    public static boolean loadFoldersVisibleState() {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.JNDI_NAME);
        String s = uss.loadSetting(ClientType.WEB, "foldersVisible");
        return Boolean.valueOf(s);
    }

    public static void saveFoldersVisibleState(boolean visible) {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.JNDI_NAME);
        uss.saveSetting(ClientType.WEB, "foldersVisible", String.valueOf(visible));
    }
}
