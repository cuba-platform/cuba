/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class AccessDeniedHandler extends AbstractExceptionHandler<AccessDeniedException> {
    public AccessDeniedHandler() {
        super(AccessDeniedException.class);
    }

    @Override
    protected void doHandle(Thread thread, AccessDeniedException e) {
        String msg = MessageProvider.getMessage(getClass(), "accessDenied.message");
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
    }
}
