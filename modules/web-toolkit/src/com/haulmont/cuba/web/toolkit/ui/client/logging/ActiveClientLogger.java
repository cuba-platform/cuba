/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.logging;

import com.vaadin.client.VConsole;

/**
 * @author artamonov
 * @version $Id$
 */
public class ActiveClientLogger extends ClientLogger {

    private String name;

    public ActiveClientLogger(String name) {
        this.name = name;
    }

    @Override
    public void log(String message) {
        VConsole.log("[" + name + "] " + message);
    }
}