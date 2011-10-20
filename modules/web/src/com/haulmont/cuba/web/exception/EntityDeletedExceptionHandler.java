/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.EntityDeletedException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;

/**
 * <p>$Id$</p>
 *
 * @author pavlov
 */
public class EntityDeletedExceptionHandler extends AbstractExceptionHandler<EntityDeletedException> {

    public EntityDeletedExceptionHandler() {
        super(EntityDeletedException.class);
    }

    @Override
    protected void doHandle(EntityDeletedException e, App app) {
        String msg = MessageProvider.formatMessage(getClass(), "entityDeletedException.message");
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_WARNING_MESSAGE);
    }
}
