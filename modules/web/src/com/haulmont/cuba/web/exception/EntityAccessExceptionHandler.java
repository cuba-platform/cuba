/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.EntityAccessException;
import com.haulmont.cuba.core.global.MessageProvider;
//import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import javax.annotation.Nullable;

/**
 * Handles {@link com.haulmont.cuba.core.global.EntityAccessException}.
 *
 * @author pavlov
 * @version $Id$
 */
public class EntityAccessExceptionHandler extends AbstractExceptionHandler {

    public EntityAccessExceptionHandler() {
        super(EntityAccessException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        String msg = MessageProvider.formatMessage(getClass(), "entityAccessException.message");
        app.getAppUI().showNotification(msg, Notification.TYPE_WARNING_MESSAGE);
    }

    public void handle(EntityAccessException e, App app) {
        doHandle(app, EntityAccessException.class.getName(), e.getMessage(), e);
    }
}
