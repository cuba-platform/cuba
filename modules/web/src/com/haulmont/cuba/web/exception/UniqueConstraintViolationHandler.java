/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.05.2009 10:41:48
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.itmill.toolkit.terminal.Terminal;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.core.global.MessageProvider;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UniqueConstraintViolationHandler implements ExceptionHandler
{
    private String marker;

    private Pattern pattern;

    private String getMarker() {
        if (marker == null) {
            marker = ServiceLocator.getDataService().getDbDialect().getUniqueConstraintViolationMarker();
        }
        return marker;
    }

    private Pattern getPattern() {
        if (pattern == null) {
            String s = ServiceLocator.getDataService().getDbDialect().getUniqueConstraintViolationPattern();
            pattern = Pattern.compile(s);
        }
        return pattern;
    }

    public boolean handle(Terminal.ErrorEvent event, App app) {
        Throwable t = event.getThrowable();
        while (t != null) {
            if (t.getMessage() != null && t.getMessage().contains(getMarker())) {
                doHandle(t, app);
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    private void doHandle(Throwable throwable, App app) {
        String msg = MessageProvider.getMessage(getClass(), "uniqueConstraintViolation.message");
        Matcher matcher = getPattern().matcher(throwable.getMessage());
        if (matcher.find()) {
            String s;
            if (matcher.groupCount() > 1)
                s = matcher.group(2);
            else
                s = matcher.group(1);
            msg = msg + " (" + s + ")";
        }
        ExceptionDialog dialog = new ExceptionDialog(msg);
        app.getMainWindow().addWindow(dialog);
    }
}
