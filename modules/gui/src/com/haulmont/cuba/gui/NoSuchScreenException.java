/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.07.2009 10:05:20
 *
 * $Id$
 */
package com.haulmont.cuba.gui;

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
