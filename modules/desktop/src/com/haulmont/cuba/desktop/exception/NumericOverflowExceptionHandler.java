/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.lib.jdbc.ReportingSQLException;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class NumericOverflowExceptionHandler extends AbstractExceptionHandler<ReportingSQLException> {

    public NumericOverflowExceptionHandler() {
        super(ReportingSQLException.class);
    }

    @Override
    protected void doHandle(Thread thread, ReportingSQLException e) {
        if (StringUtils.containsIgnoreCase(e.getMessage(), MessageProvider.getMessage(getClass(), "numericFieldOverflow.marker"))) {
            String msg = MessageProvider.getMessage(getClass(), "numericFieldOverflow.message");
            App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
        }
    }
}
