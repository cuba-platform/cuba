/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Ilya Grachev
 * Created: 29.07.2009 18:51:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.DeletePolicyException;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;

public class DeletePolicyHandler extends AbstractExceptionHandler<DeletePolicyException> {
    public DeletePolicyHandler() {
        super(DeletePolicyException.class);
    }

    protected void doHandle(DeletePolicyException t, App app) {
        String msg = MessageProvider.getMessage(getClass(), "deletePolicy.message");
        app.getMainWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
    }
}
