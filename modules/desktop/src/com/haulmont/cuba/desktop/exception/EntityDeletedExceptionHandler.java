/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.EntityDeletedException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;

import javax.annotation.Nullable;

/**
 * Handles {@link EntityDeletedException}.
 *
 * <p>$Id$</p>
 *
 * @author pavlov
 */
public class EntityDeletedExceptionHandler extends AbstractExceptionHandler {

    public EntityDeletedExceptionHandler() {
        super(EntityDeletedException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        String msg = MessageProvider.formatMessage(getClass(), "entityDeletedException.message");
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.WARNING);
    }
}
