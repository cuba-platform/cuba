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

    public static String loadAppWindowTheme() {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.NAME);
        String s = uss.loadSetting(ClientType.WEB, "appWindowTheme");
        if (s != null) {
            return s;
        }
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
        return webConfig.getAppWindowTheme();
    }

    public static void saveAppWindowTheme(String theme) {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.NAME);
        uss.saveSetting(ClientType.WEB, "appWindowTheme", theme);
    }

    public static String loadAppWindowWallpaper() {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.NAME);
        String s = uss.loadSetting(ClientType.WEB, "appWindowWallpaper");
        if (s != null) {
            return s;
        }
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
        return webConfig.getAppWindowWallpaper();
    }

    public static void saveAppWindowWallpaper(String wallpaper) {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.NAME);
        uss.saveSetting(ClientType.WEB, "appWindowWallpaper", wallpaper);
    }

    public static class FoldersState {

        public final boolean visible;
        public final int horizontalSplit;
        public final int verticalSplit;

        public FoldersState(boolean visible, int horizontalSplit, int verticalSplit) {
            this.horizontalSplit = horizontalSplit;
            this.verticalSplit = verticalSplit;
            this.visible = visible;
        }
    }

    public static FoldersState loadFoldersState() {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.NAME);
        String s = uss.loadSetting(ClientType.WEB, "foldersState");
        if (s == null)
            return null;

        String[] parts = s.split(",");
        if (parts.length != 3)
            return null;

        try {
            return new FoldersState(Boolean.valueOf(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
        } catch (Exception e) {
            return null;
        }
    }

    public static void saveFoldersState(boolean visible, int horizontalSplit, int verticalSplit) {
        UserSettingService uss = ServiceLocator.lookup(UserSettingService.NAME);
        uss.saveSetting(ClientType.WEB, "foldersState",
                String.valueOf(visible) + ","
                + String.valueOf(horizontalSplit) + ","
                + String.valueOf(verticalSplit)
        );
    }

}
