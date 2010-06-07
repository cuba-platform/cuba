/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.12.2008 11:09:01
 *
 * $Id$
 */
package com.haulmont.cuba.web.log;

import com.vaadin.terminal.ParameterHandler;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.URIHandler;
import com.vaadin.terminal.VariableOwner;
import com.vaadin.terminal.gwt.server.ChangeVariablesErrorEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppLog implements Serializable
{
    private transient LinkedList<LogItem> items = new LinkedList<LogItem>();

    private int capacity = 100;

    private static Log log = LogFactory.getLog(AppLog.class);

    private static final long serialVersionUID = 4968369834741256953L;

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        items = new LinkedList<LogItem>();
    }

    public void log(LogItem item) {
        String msg = item.getMessage() + "\n" + item.getStacktrace();
        if (item.getLevel().equals(LogLevel.ERROR))
            log.error(msg);
        else
            log.debug(item.getLevel() + ": " + msg);
        
        if (items.size() >= capacity) {
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
        if (t instanceof SocketException) {
            // Most likely client browser closed socket
            LogItem item = new LogItem(LogLevel.WARNING, "SocketException in CommunicationManager. Most likely client (browser) closed socket.", null);
            log(item);
            return;
        }

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
        if (owner != null)
            msg.append("[").append(owner.getClass().getName()).append("] ");
        msg.append("Uncaught throwable:");

        LogItem item = new LogItem(LogLevel.ERROR, msg.toString(), t);
        log(item);
    }

    public List<LogItem> getItems() {
        return new ArrayList<LogItem>(items);
    }
}
