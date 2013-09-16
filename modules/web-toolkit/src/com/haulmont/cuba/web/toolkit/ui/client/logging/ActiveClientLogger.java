/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author artamonov
 * @version $Id$
 */
public class ActiveClientLogger extends ClientLogger {

    private String name;

    public ActiveClientLogger(String name) {
        this.name = name;
        this.enabled = true;
    }

    @Override
    public void log(String message) {
        Logger.getLogger(name).log(new LogRecord(Level.INFO, "[" + name + "] " + message));
    }
}