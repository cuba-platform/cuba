/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.lib.jdbc.ReportingSQLException;

import javax.annotation.Nullable;

/**
 * Handles database "numeric overflow" exception.
 *
 * @author devyatkin
 * @version $Id$
 */
public class NumericOverflowExceptionHandler extends AbstractExceptionHandler {

    public NumericOverflowExceptionHandler() {
        super(ReportingSQLException.class.getName());
    }

    @Override
    protected boolean canHandle(String className, String message, @Nullable Throwable throwable) {
        return StringUtils.containsIgnoreCase(message, "Numeric field overflow");
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        Messages messages = AppBeans.get(Messages.NAME);
        String msg = messages.getMainMessage("numericFieldOverflow.message");
        App.getInstance().getMainFrame().showNotification(msg, IFrame.NotificationType.ERROR);
    }
}
