/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
//import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.lib.jdbc.ReportingSQLException;

import javax.annotation.Nullable;

/**
 * Handles database "numeric overflow" exception.
 *
 * @author degtyarjov
 * @version $Id$
 */
public class NumericOverflowExceptionHandler extends AbstractExceptionHandler {

    public NumericOverflowExceptionHandler() {
        super(ReportingSQLException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        if (StringUtils.containsIgnoreCase(message, "Numeric field overflow")) {
            String msg = MessageProvider.getMessage(getClass(), "numericFieldOverflow.message");
            app.getAppUI().showNotification(msg, Notification.TYPE_ERROR_MESSAGE);
        }
    }
}