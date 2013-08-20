/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.log.DevelopmentExceptionWindow;
import com.vaadin.server.ErrorEvent;
import com.vaadin.ui.Window;
import javax.annotation.Nullable;


/**
 * @author hasanov
 * @version $Id$
 */
public class DevelopmentExceptionHandler extends AbstractExceptionHandler {

    public DevelopmentExceptionHandler() {
        super(DevelopmentException.class.getName());
    }

    @Override
    public boolean handle(ErrorEvent event, App app) {
        return AppBeans.get(Configuration.class).getConfig(ClientConfig.class).getDevelopmentExceptionEnabled()
                && super.handle(event, app);
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        if (throwable == null)
            return;
        DevelopmentExceptionWindow devWindow = new DevelopmentExceptionWindow(throwable);
        for (Window window : App.getInstance().getAppUI().getWindows()) {
            if (window.isModal()) {
                devWindow.setModal(true);
                break;
            }
        }
        App.getInstance().getAppUI().addWindow(devWindow);
        devWindow.focus();
    }
}
