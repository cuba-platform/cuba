/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

/**
 * @author artamonov
 * @version $Id$
 */
@SupportedByClient
public class IllegalEntityStateException extends IllegalStateException {

    private static final long serialVersionUID = 2358371187583832894L;

    public IllegalEntityStateException(String s) {
        super(s);
    }
}