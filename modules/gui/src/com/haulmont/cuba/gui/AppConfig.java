/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.annotation.Nullable;

/**
 * GenericUI class holding common information about client application configuration,
 * as well as some static helper methods to obtain infrastructure objects.
 *
 * @author abramov
 * @version $Id$
 */
public abstract class AppConfig {

    public static final String CLIENT_TYPE_PROP = "cuba.clientType";

    /**
     * DEPRECATED. To obtain a message from the main message pack use {@link com.haulmont.cuba.core.global.Messages#getMainMessage(String)}.
     */
    @Deprecated
    public static final String MESSAGES_PACK_PROP = "cuba.messagePack";

    /**
     * Current client type.
     * <p/> Set up through the app property {@link #CLIENT_TYPE_PROP} on a client tier.
     * @return  current client type
     */
    public static ClientType getClientType() {
        return ClientType.valueOf(AppContext.getProperty(CLIENT_TYPE_PROP));
    }

    /**
     * Main messages pack used by GenericUI components and application code.
     * <p/> Set up through app property <code>cuba.messagePack</code> depending on the client type and set of base projects.
     *
     * <p/> This method is outdated but not deprecated because it is used in lots of places. Preferred method to
     * obtain the main message pack is {@link com.haulmont.cuba.core.global.Messages#getMainMessagePack()}.
     *
     * <p/> To obtain a message from the main message pack use {@link com.haulmont.cuba.core.global.Messages#getMainMessage(String)}.
     */
    public static String getMessagesPack() {
        return AppBeans.get(Messages.class).getMainMessagePack();
    }

    /**
     * DEPRECATED! Use {@link #createExportDisplay(com.haulmont.cuba.gui.components.IFrame)}.
     */
    @Deprecated
    public static ExportDisplay createExportDisplay() {
        return AppBeans.get(ExportDisplay.NAME);
    }

    /**
     * Create an ExportDisplay implementation.
     *
     * @param frame current frame. It is needed for desktop implementation to corrrectly show dialog inside of currently
     *              active main window. Can be null, but in this case a position of the dialog is not guaranteed.
     * @return  a new ExportDisplay instance
     */
    public static ExportDisplay createExportDisplay(@Nullable IFrame frame) {
        ExportDisplay exportDisplay = AppBeans.get(ExportDisplay.NAME);
        exportDisplay.setFrame(frame);
        return exportDisplay;
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
