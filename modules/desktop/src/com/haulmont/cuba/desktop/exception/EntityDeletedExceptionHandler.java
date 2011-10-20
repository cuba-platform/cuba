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

/**
 * <p>$Id$</p>
 *
 * @author pavlov
 */
public class EntityDeletedExceptionHandler extends AbstractExceptionHandler<EntityDeletedException>{
    public EntityDeletedExceptionHandler() {
        super(EntityDeletedException.class);
    }

    @Override
    protected void doHandle(Thread thread, EntityDeletedException e) {
        String msg = MessageProvider.formatMessage(getClass(), "entityDeletedException.message");
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.WARNING);
    }
}
