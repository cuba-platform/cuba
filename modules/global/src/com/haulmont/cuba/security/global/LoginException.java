/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.global;

import com.haulmont.cuba.core.global.Logging;
import com.haulmont.cuba.core.global.SupportedByClient;

/**
 * Login error. Contains message localized accordingly to the current user locale. 
 */
@SupportedByClient
@Logging(Logging.Type.BRIEF)
public class LoginException extends Exception {

    private static final long serialVersionUID = 6144194102176774627L;

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String template, Object... params) {
        super(String.format(template, params));
    }
}
