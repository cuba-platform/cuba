/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.03.2009 22:16:39
 * $Id$
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * GenericUI class holding common information about client application configuration,
 * as well as some static helper methods to obtain infrastructure objects.
 */
public abstract class AppConfig
{
    public static final String CLIENT_TYPE_PROP = "cuba.appConfig.clientType";

    public static final String MESSAGES_PACK_PROP = "cuba.appConfig.messagesPack";

    public static final String THEME_NAME_PROP = "cuba.AppConfig.themeName";

    private static ClientType clientType;

    private static String messagesPackage;

    /**
     * Current client type.
     * Set up through app property {@link #CLIENT_TYPE_PROP} by specific client implementation.
     * @return  current client type
     */
    public static ClientType getClientType() {
        if (clientType == null) {
            clientType = ClientType.valueOf(AppContext.getProperty(CLIENT_TYPE_PROP));
        }
        return clientType;
    }

    /**
     * Central messages pack used by GenericUI components and application code.
     * Set up through app property {@link #MESSAGES_PACK_PROP} depending on the client type and set of base projects.
     * @return  message pack name
     */
    public static String getMessagesPack() {
        if (messagesPackage == null) {
            messagesPackage = AppContext.getProperty(MESSAGES_PACK_PROP);
        }
        return messagesPackage;
    }

    /**
     * Client-specific ExportDisplay
     * @return  a new ExportDisplay instance
     */
    public static ExportDisplay createExportDisplay() {
        return AppBeans.get(ExportDisplay.NAME);
    }

    /**
     * Client-specific BackgroundWorker
     * @return  BackgroundWorker instance
     */
    public static BackgroundWorker getBackgroundWorker() {
        return AppBeans.get(BackgroundWorker.NAME);
    }

    /**
     * Client-specific components factory
     * @return  ComponentsFactory instance
     */
    public static ComponentsFactory getFactory() {
        return AppBeans.get(ComponentsFactory.class);
    }
}
