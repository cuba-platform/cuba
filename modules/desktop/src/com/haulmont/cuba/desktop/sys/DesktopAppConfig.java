/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.desktop.gui.DesktopComponentsFactory;
import com.haulmont.cuba.desktop.gui.components.DesktopExportDisplay;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopAppConfig extends AppConfig {

    private volatile ComponentsFactory componentsFactory;

    @Override
    protected ExportDisplay __createExportDisplay() {
        return new DesktopExportDisplay();
    }

    @Override
    protected ComponentsFactory __getFactory() {
        if (componentsFactory == null) {
            synchronized (this) {
                componentsFactory = new DesktopComponentsFactory();
            }
        }
        return componentsFactory;
    }
}
