/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.core.global.EntityAccessException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Handles {@link EntityAccessException}.
 *
 * @author pavlov
 * @version $Id$
 */
@ManagedBean("cuba_EntityAccessExceptionHandler")
public class EntityAccessExceptionHandler extends AbstractGenericExceptionHandler {

    @Inject
    protected Messages messages;

    public EntityAccessExceptionHandler() {
        super(EntityAccessException.class.getName());
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        String msg = messages.formatMessage(getClass(), "entityAccessException.message");
        windowManager.showNotification(msg, Frame.NotificationType.WARNING);
    }
}