/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

/**
 * @author artamonov
 * @version $Id$
 */
public class DBNotInitializedException extends Exception {
    public DBNotInitializedException() {
    }

    public DBNotInitializedException(String message) {
        super(message);
    }
}
