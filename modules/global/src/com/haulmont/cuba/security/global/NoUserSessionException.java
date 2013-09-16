/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.global;

import com.haulmont.cuba.core.global.Logging;
import com.haulmont.cuba.core.global.SupportedByClient;

import java.util.UUID;

/**
 * Raised by middleware if the client provides an invalid user session ID (e.g. if the user session has expired).
 *
 * @author krivopustov
 * @version $Id$
 */
@SupportedByClient
@Logging(Logging.Type.BRIEF)
public class NoUserSessionException extends RuntimeException {

    private static final long serialVersionUID = 4820628023682230319L;

    public NoUserSessionException(UUID sessionId) {
        super(String.format("User session not found: %s", sessionId.toString()));
    }
}
