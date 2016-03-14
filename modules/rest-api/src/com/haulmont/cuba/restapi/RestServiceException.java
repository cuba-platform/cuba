/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.restapi;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class RestServiceException extends Exception {

    public RestServiceException() {
    }

    public RestServiceException(String message) {
        super(message);
    }

    public RestServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestServiceException(Throwable cause) {
        super(cause);
    }

    public RestServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
