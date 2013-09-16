/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
