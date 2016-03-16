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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.annotation.Nullable;

/**
 * GenericUI class holding common information about client application configuration,
 * as well as some static helper methods to obtain infrastructure objects.
 *
 */
public abstract class AppConfig {

    public static final String CLIENT_TYPE_PROP = "cuba.clientType";

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
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMainMessagePack();
    }

    /**
     * Create an ExportDisplay implementation.
     *
     * @param frame current frame. It is needed for desktop implementation to correctly show dialog inside of currently
     *              active main window. Can be null, but in this case a position of the dialog is not guaranteed.
     * @return  a new ExportDisplay instance
     */
    public static ExportDisplay createExportDisplay(@Nullable Frame frame) {
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
        return AppBeans.get(ComponentsFactory.NAME);
    }
}