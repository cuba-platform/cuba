/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.ExceptionHandlersConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.App;
import com.vaadin.terminal.Terminal;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles database unique constraint violations. Determines the exception type by searching a special marker string
 * in the messages of all exceptions in the chain.
 *
 * @author krivopustov
 * @version $Id$
 */
public class UniqueConstraintViolationHandler implements ExceptionHandler {

    private Messages messages = AppBeans.get(Messages.NAME);
    private DataService dataService = AppBeans.get(DataService.NAME);

    private Pattern getPattern() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ExceptionHandlersConfig exceptionHandlersConfig = configuration.getConfig(ExceptionHandlersConfig.class);

        String s = exceptionHandlersConfig.getUniqueConstraintViolationPattern();
        if (StringUtils.isBlank(s)) {
            s = dataService.getDbDialect().getUniqueConstraintViolationPattern();
        }

        return Pattern.compile(s);
    }

    @Override
    public boolean handle(Terminal.ErrorEvent event, App app) {
        Throwable t = event.getThrowable();
        try {
            while (t != null) {
                if (t.toString().contains("org.apache.openjpa.persistence.EntityExistsException")) {
                    doHandle(t, app);
                    return true;
                }
                t = t.getCause();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void doHandle(Throwable throwable, App app) {
        String constraintName = "";
        Matcher matcher = getPattern().matcher(throwable.toString());
        if (matcher.find()) {
            if (matcher.groupCount() > 1) {
                constraintName = matcher.group(2);
            } else {
                constraintName = matcher.group(1);
            }
        }

        String msg = "";
        if (StringUtils.isNotBlank(constraintName)) {
            msg = messages.getMainMessage(constraintName.toUpperCase());
        }

        if (msg.equalsIgnoreCase(constraintName)) {
            msg = messages.getMainMessage("uniqueConstraintViolation.message");
            if (StringUtils.isNotBlank(constraintName)) {
                msg = msg + " (" + constraintName + ")";
            }
        }

        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
    }
}
