/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.logging;

import com.haulmont.cuba.core.global.SupportedByClient;

/**
 * @author artamonov
 * @version $Id$
 */
@SupportedByClient
public class LogControlException extends Exception {

    private static final long serialVersionUID = -8129134565820337559L;

    public LogControlException(String message) {
        super(message);
    }

    public LogControlException(Throwable cause) {
        super(cause);
    }
}