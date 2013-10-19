/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

/**
 * @author budarov
 * @version $Id$
 */
public class JmxControlException extends RuntimeException {
    private static final long serialVersionUID = -7683677050143447151L;

    public JmxControlException(Throwable cause) {
        super(cause);
    }
}