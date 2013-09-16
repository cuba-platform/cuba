/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui;

/**
 * Raised on attempt to open an unknown screen.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class NoSuchScreenException extends RuntimeException {

    private static final long serialVersionUID = -3751833162235475862L;

    private final String screenId;

    public NoSuchScreenException(String screenId) {
        super("Screen '" + screenId + "' is not defined");
        this.screenId = screenId;
    }

    public String getScreenId() {
        return screenId;
    }
}
