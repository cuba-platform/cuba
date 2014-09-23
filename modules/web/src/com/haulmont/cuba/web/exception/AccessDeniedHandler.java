/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.App;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

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
        Messages messages = AppBeans.get(Messages.NAME);
        String msg = messages.getMessage(getClass(), "accessDenied.message");
        new Notification(msg, Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
    }

    public void handle(AccessDeniedException e, App app) {
        doHandle(app, AccessDeniedException.class.getName(), e.getMessage(), e);
    }
}