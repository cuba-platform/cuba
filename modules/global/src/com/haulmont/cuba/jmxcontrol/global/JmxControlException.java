/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.jmxcontrol.global;

import com.haulmont.cuba.core.global.SupportedByClient;

/**
 * @author budarov
 * @version $Id$
 */
@SupportedByClient
public class JmxControlException extends RuntimeException {
    private static final long serialVersionUID = -7683677050143447151L;

    public JmxControlException(Throwable cause) {
        super(cause);
    }
}