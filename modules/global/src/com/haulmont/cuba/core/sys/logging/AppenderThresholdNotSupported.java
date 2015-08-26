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
public class AppenderThresholdNotSupported extends LogControlException {

    private static final long serialVersionUID = 8472207426115530910L;

    public AppenderThresholdNotSupported(String appenderName) {
        super(String.format("Threshold for appender '%s' is not supported", appenderName));
    }
}