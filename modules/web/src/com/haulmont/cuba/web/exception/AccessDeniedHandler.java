/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.07.2009 17:48:30
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import javax.annotation.Nullable;

/**
 * Handles {@link AccessDeniedException}.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AccessDeniedHandler extends AbstractExceptionHandler {
    
    public AccessDeniedHandler() {
        super(AccessDeniedException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        String msg = MessageProvider.getMessage(getClass(), "accessDenied.message");
        app.getAppUI().showNotification(msg, Notification.TYPE_ERROR_MESSAGE);
    }

    public void handle(AccessDeniedException e, App app) {
        doHandle(app, AccessDeniedException.class.getName(), e.getMessage(), e);
    }
}
