/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.jmx.exception;

import com.haulmont.cuba.core.global.SupportedByClient;
import com.haulmont.cuba.core.sys.logging.LogControlException;

/**
 * @author artamonov
 * @version $Id$
 */
@SupportedByClient
public class UnrecognizedLogThresholdException extends LogControlException {

    private static final long serialVersionUID = -5723789683222840034L;

    public UnrecognizedLogThresholdException(String thresholdString) {
        super(String.format("Unrecognized threshold '%s'", thresholdString));
    }
}