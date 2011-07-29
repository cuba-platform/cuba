/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.IFrame;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class NoSuchScreenHandler extends AbstractExceptionHandler<NoSuchScreenException> {

    public NoSuchScreenHandler() {
        super(NoSuchScreenException.class);
    }

    @Override
    protected void doHandle(Thread thread, NoSuchScreenException e) {
        String msg = MessageProvider.getMessage(getClass(), "noSuchScreen.message");
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
    }
}
