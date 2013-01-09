/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.12.2008 17:31:41
 *
 * $Id$
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
