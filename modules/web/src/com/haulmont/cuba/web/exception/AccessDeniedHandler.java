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
import com.vaadin.ui.Window;

public class AccessDeniedHandler extends AbstractExceptionHandler<AccessDeniedException> {
    
    public AccessDeniedHandler() {
        super(AccessDeniedException.class);
    }

    protected void doHandle(AccessDeniedException t, App app) {
        String msg = MessageProvider.getMessage(getClass(), "accessDenied.message");
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
    }

    public void handle(AccessDeniedException e, App app) {
        doHandle(e, app);
    }
}
