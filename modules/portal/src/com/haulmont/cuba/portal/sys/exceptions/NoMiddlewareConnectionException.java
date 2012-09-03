/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.sys.exceptions;

/**
 * @author artamonov
 * @version $Id$
 */
public class NoMiddlewareConnectionException extends RuntimeException {
    public NoMiddlewareConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
