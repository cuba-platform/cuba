/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Degtyarjov Eugeniy
 * Created: 17.11.2009 14:57:21
 *
 * $Id$
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.lib.jdbc.ReportingSQLException;

public class NumericOverflowExceptionHandler extends AbstractExceptionHandler<ReportingSQLException> {
    public NumericOverflowExceptionHandler() {
        super(ReportingSQLException.class);
    }

    protected void doHandle(ReportingSQLException e, App app) {
        if (StringUtils.containsIgnoreCase(e.getMessage(), MessageProvider.getMessage(getClass(), "numericFieldOverflow.marker"))) {
            String msg = MessageProvider.getMessage(getClass(), "numericFieldOverflow.message");
            app.getMainWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
        }
    }
}
