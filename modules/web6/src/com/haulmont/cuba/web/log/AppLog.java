/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.log;

import com.haulmont.cuba.core.global.Logging;
import com.haulmont.cuba.core.global.SilentException;
import com.vaadin.data.Validator;
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.URIHandler;
import com.vaadin.terminal.VariableOwner;
import com.vaadin.terminal.gwt.server.ChangeVariablesErrorEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class AppLog {

    private transient LinkedList<LogItem> items = new LinkedList<>();

    private static final int CAPACITY = 100;

    private static Log log = LogFactory.getLog(AppLog.class);

    public void log(LogItem item) {
        String msg = item.getMessage() + "\n" + item.getStacktrace();
        if (item.getLevel().equals(LogLevel.ERROR))
            log.error(msg);
        else
            log.debug(item.getLevel() + ": " + msg);
        
        if (items.size() >= CAPACITY) {
            items.removeLast();
        }
        items.addFirst(item);
    }

    public void log(LogLevel level, String message, Throwable throwable) {
        log(new LogItem(level, message, throwable));
    }

    public void debug(String message) {
        log(new LogItem(LogLevel.DEBUG, message, null));
    }

    public void info(String message) {
        log(new LogItem(LogLevel.INFO, message, null));
    }

    public void warning(String message) {
        log(new LogItem(LogLevel.WARNING, message, null));
    }

    public void error(String message) {
        log(new LogItem(LogLevel.ERROR, message, null));
    }

    public void log(Terminal.ErrorEvent event) {
        Throwable t = event.getThrowable();

        if (t instanceof SilentException)
            return;

        if (t instanceof Validator.InvalidValueException)
            return;

        if (t instanceof SocketException) {
            // Most likely client browser closed socket
            LogItem item = new LogItem(LogLevel.WARNING, "SocketException in CommunicationManager. Most likely client (browser) closed socket.", null);
            log(item);
            return;
        }

        Logging annotation = t.getClass().getAnnotation(Logging.class);
        Logging.Type loggingType = annotation == null ? Logging.Type.FULL : annotation.value();
        if (loggingType == Logging.Type.NONE)
            return;

        // Finds the original source of the error/exception
        Object owner = null;
        if (event instanceof VariableOwner.ErrorEvent) {
            owner = ((VariableOwner.ErrorEvent) event).getVariableOwner();
        } else if (event instanceof URIHandler.ErrorEvent) {
            owner = ((URIHandler.ErrorEvent) event).getURIHandler();
        } else if (event instanceof ParameterHandler.ErrorEvent) {
            owner = ((ParameterHandler.ErrorEvent) event).getParameterHandler();
        } else if (event instanceof ChangeVariablesErrorEvent) {
            owner = ((ChangeVariablesErrorEvent) event).getComponent();
        }

        StringBuilder msg = new StringBuilder();
        msg.append("Uncaught exception");
        if (owner != null)
            msg.append(" in ").append(owner.getClass().getName());
        msg.append(": ");

        if (loggingType == Logging.Type.BRIEF) {
            error(msg + t.toString());
        } else {
            LogItem item = new LogItem(LogLevel.ERROR, msg.toString(), t);
            log(item);
        }
    }

    public List<LogItem> getItems() {
        return new ArrayList<>(items);
    }
}
