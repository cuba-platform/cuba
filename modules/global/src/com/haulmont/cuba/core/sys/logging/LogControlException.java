/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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