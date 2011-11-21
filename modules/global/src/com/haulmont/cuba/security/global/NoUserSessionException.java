/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.security.global;

import com.haulmont.cuba.core.sys.ClassesInfo;

import java.util.UUID;

/**
 * Raised by middleware if the client provides an invalid user session ID (e.g. if the user session has expired).
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class NoUserSessionException extends RuntimeException
{
    private static final long serialVersionUID = 4820628023682230319L;

    static {
        ClassesInfo.addClientSupported(NoUserSessionException.class);
    }

    public NoUserSessionException(UUID sessionId) {
        super(String.format("User session not found: %s", sessionId.toString()));
    }
}
