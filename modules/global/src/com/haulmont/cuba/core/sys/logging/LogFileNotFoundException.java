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
public class LogFileNotFoundException extends LogControlException {

    private static final long serialVersionUID = -4325524987499763504L;

    public LogFileNotFoundException(String fileName) {
        super("Not found log file: " + fileName);
    }
}