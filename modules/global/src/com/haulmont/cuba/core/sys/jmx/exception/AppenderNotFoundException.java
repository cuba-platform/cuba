/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jmx.exception;

import com.haulmont.cuba.core.global.SupportedByClient;
import com.haulmont.cuba.core.sys.logging.LogControlException;

/**
 * @author artamonov
 * @version $Id$
 */
@SupportedByClient
public class AppenderNotFoundException extends LogControlException {

    private static final long serialVersionUID = 511985938820193580L;

    public AppenderNotFoundException(String appenderName) {
        super(String.format("Not found appender with name '%s'", appenderName));
    }
}