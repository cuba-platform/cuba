/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.03.2009 22:16:39
 * $Id$
 */

package com.haulmont.cuba.gui;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GenericUI singleton class holding common information about application configuration
 */
public abstract class AppConfig
{
    public static final String IMPL_PROP = "cuba.appConfig.impl";

    public static final String WINDOW_CONFIG_IMPL_PROP = "cuba.windowConfig.impl";
    public static final String WINDOW_CONFIG_XML_PROP = "cuba.windowConfig";
    public static final String WINDOW_CONFIG_DEFAULT_IMPL = "com.haulmont.cuba.gui.config.WindowConfig";

    public static final String MENU_CONFIG_IMPL_PROP = "cuba.menuConfig.impl";
    public static final String MENU_CONFIG_XML_PROP = "cuba.menuConfig";
    public static final String MENU_CONFIG_DEFAULT_IMPL = "com.haulmont.cuba.gui.config.MenuConfig";

    public static final String PERMISSION_CONFIG_IMPL_PROP = "cuba.permissionConfig.impl";
    public static final String PERMISSION_CONFIG_XML_PROP = "cuba.permissionConfig";
    public static final String PERMISSION_CONFIG_DEFAULT_IMPL = "com.haulmont.cuba.gui.config.PermissionConfig";

    public static final String CLIENT_TYPE_PROP = "cuba.appConfig.clientType";
    public static final String MESSAGES_PACK_PROP = "cuba.appConfig.messagesPack";
    public static final String THEME_NAME_PROP = "cuba.AppConfig.themeName";

    protected static AppConfig instance;

    protected WindowConfig windowConfig;
    protected MenuConfig menuConfig;
    protected ClientType clientType;
    protected String messagesPackage;
    protected Map<Locale, PermissionConfig> permissionsConfigMap = new ConcurrentHashMap<Locale, PermissionConfig>();

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = createInstance(IMPL_PROP, null);
        }
        return instance;
    }

    protected static <T> T createInstance(String propertyName, String defaultImplementation) {
        String implClassName = AppContext.getProperty(propertyName);
        if (implClassName == null)
            implClassName = defaultImplementation;

        if (implClassName == null)
            throw new IllegalStateException("Property " + propertyName + " is not set");
        try {
            Class implClass = ReflectionHelper.getClass(implClassName);
            //noinspection unchecked
            return (T) implClass.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PermissionConfig instance.
     * Implementation class is set up through system property
     * by specific client implementation.
     */
    public PermissionConfig getPermissionConfig(Locale locale) {
        if (locale == null)
            throw new IllegalArgumentException("Locale is null");
        
        PermissionConfig permissionConfig = permissionsConfigMap.get(locale);
        if (permissionConfig == null) {
            permissionConfig = createInstance(PERMISSION_CONFIG_IMPL_PROP, PERMISSION_CONFIG_DEFAULT_IMPL);
            permissionConfig.compile(locale);
            permissionsConfigMap.put(locale, permissionConfig);
        }
        return permissionConfig;
    }

    /**
     * MenuConfig instance.
     * Implementation class and XML storage are set up through system property
     * by specific client implementation.
     */
    public MenuConfig getMenuConfig() {
        if (menuConfig == null) {
            menuConfig = createInstance(MENU_CONFIG_IMPL_PROP, MENU_CONFIG_DEFAULT_IMPL);
            final String path = AppContext.getProperty(MENU_CONFIG_XML_PROP);

            String xml = ScriptingProvider.getResourceAsString(path);
            if (xml != null)
                menuConfig.loadConfig(xml);
        }
        return menuConfig;
    }

    /**
     * WindowConfig instance.
     * Implementation class and XML storage are set up through system property
     * by specific client implementation.
     */
    public WindowConfig getWindowConfig() {
        if (windowConfig == null) {
            windowConfig = createInstance(WINDOW_CONFIG_IMPL_PROP, WINDOW_CONFIG_DEFAULT_IMPL);
            final String path = AppContext.getProperty(WINDOW_CONFIG_XML_PROP);

            String xml = ScriptingProvider.getResourceAsString(path);
            if (xml != null)
                windowConfig.loadConfig(xml);
        }
        return windowConfig;
    }

    /**
     * Current client type.
     * Set up through system property by specific client implementation.
     */
    public ClientType getClientType() {
        if (clientType == null) {
            clientType = ClientType.valueOf(AppContext.getProperty(CLIENT_TYPE_PROP));
        }
        return clientType;
    }

    /**
     * Message pack used by GenericUI components.
     * Set up through system property by specific client implementation.
     */
    public static String getMessagesPack() {
        return getInstance().__getMessagesPack();
    }

    protected String __getMessagesPack() {
        if (messagesPackage == null) {
            messagesPackage = AppContext.getProperty(MESSAGES_PACK_PROP);
        }
        return messagesPackage;
    }

    /**
     * Client-specific ExportDisplay
     */
    public static ExportDisplay createExportDisplay() {
        return getInstance().__createExportDisplay();
    }

    /**
     * Client-specific components factory
     */
    public static ComponentsFactory getFactory() {
        return getInstance().__getFactory();
    }

    protected abstract ExportDisplay __createExportDisplay();

    protected abstract ComponentsFactory __getFactory();
}
