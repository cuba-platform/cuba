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
public class LogFileNotFoundException extends LogControlException {

    private static final long serialVersionUID = -4325524987499763504L;

    public LogFileNotFoundException(String fileName) {
        super("Not found log file: " + fileName);
    }
}