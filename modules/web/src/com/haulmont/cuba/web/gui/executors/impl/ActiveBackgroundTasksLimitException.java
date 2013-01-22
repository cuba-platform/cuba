/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.executors.impl;

/**
 * @author artamonov
 * @version $Id$
 */
public class ActiveBackgroundTasksLimitException extends RuntimeException {

    public ActiveBackgroundTasksLimitException() {
    }

    public ActiveBackgroundTasksLimitException(String message) {
        super(message);
    }

    public ActiveBackgroundTasksLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActiveBackgroundTasksLimitException(Throwable cause) {
        super(cause);
    }

    public ActiveBackgroundTasksLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
