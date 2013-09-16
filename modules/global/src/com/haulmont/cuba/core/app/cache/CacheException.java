/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import com.haulmont.cuba.core.global.SupportedByClient;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@SupportedByClient
public class CacheException extends Exception {
    private static final long serialVersionUID = 5306831032981161592L;

    public CacheException() {
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}