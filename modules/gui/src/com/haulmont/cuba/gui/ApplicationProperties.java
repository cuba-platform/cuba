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
import com.haulmont.cuba.core.app.ResourceRepositoryService;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.PermissionConfig;

public class ApplicationProperties {
    public static final String IMPL_PROP = "cuba.ApplicationProperties.impl";
    public static final String DEFAULT_IMPL = "com.haulmont.cuba.gui.ApplicationProperties";

    public static final String WINDOW_CONFIG_IMPL_PROP = "cuba.ApplicationProperties.WindowConfig.impl";
    public static final String WINDOW_CONFIG_XML_PROP = "cuba.ApplicationProperties.WindowConfig.xml";
    public static final String WINDOW_CONFIG_IMPL = "com.haulmont.cuba.gui.config.WindowConfig";

    public static final String MENU_CONFIG_IMPL_PROP = "cuba.ApplicationProperties.MenuConfig.impl";
    public static final String MENU_CONFIG_XML_PROP = "cuba.ApplicationProperties.MenuConfig.xml";
    public static final String MENU_CONFIG_IMPL = "com.haulmont.cuba.gui.config.MenuConfig";

    public static final String PERMISSION_CONFIG_IMPL_PROP = "cuba.ApplicationProperties.PermissionConfig.impl";
    public static final String PERMISSION_CONFIG_XML_PROP = "cuba.ApplicationProperties.PermissionConfig.xml";
    public static final String PERMISSION_CONFIG_IMPL = "com.haulmont.cuba.gui.config.PermissionConfig";

    public static final String CLIENT_TYPE_PROP = "com.ApplicationProperties.ClientType";
    public static final String MESSAGES_PACKAGE_PROP = "com.ApplicationProperties.MessagesPackage";

    protected static ApplicationProperties instance;

    protected WindowConfig windowConfig;
    protected MenuConfig menuConfig;
    protected ClientType clientType;
    protected String messagesPackage;
    protected PermissionConfig permissionsConfig;

    public static ApplicationProperties getInstance() {
        if (instance == null) {
            instance = createInstance(IMPL_PROP, DEFAULT_IMPL);
        }
        return instance;
    }

    protected static <T> T createInstance(String propertyName, String defaultImplementation) {
        String implClassName = System.getProperty(propertyName);
        if (implClassName == null)
            implClassName = defaultImplementation;
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

    public PermissionConfig getPermissionConfig() {
        if (permissionsConfig == null) {
            permissionsConfig = createInstance(PERMISSION_CONFIG_IMPL_PROP, PERMISSION_CONFIG_IMPL);
            permissionsConfig.compile();
        }
        
        return permissionsConfig;
    }

    public MenuConfig getMenuConfig() {
        if (menuConfig == null) {
            ResourceRepositoryService repository = ServiceLocator.lookup(ResourceRepositoryService.JNDI_NAME);

            menuConfig = createInstance(MENU_CONFIG_IMPL_PROP, MENU_CONFIG_IMPL);
            final String path = System.getProperty(MENU_CONFIG_XML_PROP);
            menuConfig.loadConfig(getMessagesPackage(), repository.getResAsString(path));
        }
        return menuConfig;
    }

    public WindowConfig getWindowConfig() {
        if (windowConfig == null) {
            ResourceRepositoryService repository = ServiceLocator.lookup(ResourceRepositoryService.JNDI_NAME);

            windowConfig = createInstance(WINDOW_CONFIG_IMPL_PROP, WINDOW_CONFIG_IMPL);
            final String path = System.getProperty(WINDOW_CONFIG_XML_PROP);
            windowConfig.loadConfig(repository.getResAsString(path));
        }
        return windowConfig;
    }

    public ClientType getClientType() {
        if (clientType == null) {
            clientType = ClientType.valueOf(System.getProperty(CLIENT_TYPE_PROP));
        }
        return clientType;
    }

    public String getMessagesPackage() {
        if (messagesPackage == null) {
            messagesPackage = System.getProperty(MESSAGES_PACKAGE_PROP);
        }
        return messagesPackage;
     }
}
