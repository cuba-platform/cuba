/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.gui.WebComponentsFactory;
import com.haulmont.cuba.web.gui.utils.WebBackgroundWorker;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class WebAppConfig extends AppConfig {

    private volatile ComponentsFactory componentsFactory;
    private volatile BackgroundWorker backgroundWorker;

    @Override
    protected ExportDisplay __createExportDisplay() {
        return new WebExportDisplay();
    }

    @Override
    protected BackgroundWorker __getBackgroundWorker() {
        if (backgroundWorker == null) {
            synchronized (this) {
                backgroundWorker = new WebBackgroundWorker();
            }
        }
        return backgroundWorker;
    }

    @Override
    protected ComponentsFactory __getFactory() {
        if (componentsFactory == null) {
            synchronized (this) {
                componentsFactory = new WebComponentsFactory();
            }
        }
        return componentsFactory;
    }
}
