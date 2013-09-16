/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.global.LoginException;

import javax.inject.Inject;

/**
 * DEPRECATED. Use {@link com.haulmont.cuba.security.app.Authentication} directly.
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public class ManagementBean {

    @Inject
    protected Authentication authentication;

    /**
     * Performs login with credentials set in cuba-app.properties<br>
     * First checks if a current thread session exists or the bean is already logged in and that session is still valid.
     * If no, performs login and stores sessionId in the protected field.<br>
     * No logout assumed.
     *
     * @throws LoginException If access denied
     */
    protected void loginOnce() throws LoginException {
        authentication.begin();
    }

    /**
     * Performs cleanup for SecurityContext if there was previous loginOnce in this thread.<br>
     * Should be placed into "finally" section of a try/finally block.
     */
    protected void clearSecurityContext() {
        authentication.end();
    }

    /**
     * Performs login with credentials set in cuba-app.properties<br>
     * Should be placed inside try/finally block with logout in "finally" section
     *
     * @throws LoginException If access denied
     */
    protected void login() throws LoginException {
        authentication.begin();
    }

    protected String getSystemLogin() {
        return AppContext.getProperty("cuba.jmxUserLogin");
    }

    /**
     * Performs logout if there was previous login in this thread.<br>
     * Should be placed into "finally" section of a try/finally block.
     */
    protected void logout() {
        authentication.end();
    }
}
