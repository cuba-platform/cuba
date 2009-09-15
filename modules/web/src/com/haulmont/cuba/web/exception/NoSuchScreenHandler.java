/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.07.2009 10:08:15
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.core.global.MessageProvider;
import com.vaadin.ui.Window;

public class NoSuchScreenHandler extends AbstractExceptionHandler {

    public NoSuchScreenHandler() {
        super(NoSuchScreenException.class);
    }

    protected void doHandle(App app) {
        String msg = MessageProvider.getMessage(getClass(), "noSuchScreen.message");
        app.getMainWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
    }

    public void handle(NoSuchScreenException e, App app) {
        doHandle(app);
    }
}
