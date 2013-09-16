/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
