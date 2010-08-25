/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 17.08.2010 11:08:03
 * $Id$
 */

package com.haulmont.cuba.jmxcontrol.global;

public class JmxControlException extends RuntimeException {
    private static final long serialVersionUID = -7683677050143447151L;

    public JmxControlException(String message) {
        super(message);
    }

    public JmxControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public JmxControlException(Throwable cause) {
        super(cause);
    }
}
