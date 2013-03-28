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
public class AppenderThresholdNotSupported extends LogControlException {

    private static final long serialVersionUID = 8472207426115530910L;

    public AppenderThresholdNotSupported(String appenderName) {
        super("Threshold for appender '%s' is not supported");
    }
}