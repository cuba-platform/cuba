/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.security.global.LoginException;

import java.util.Locale;

/**
 * Interface to be implemented by middleware connection objects supporting external authentication.
 *
 * @author krokhin
 * @version $Id$
 */
public interface ExternallyAuthenticatedConnection {

    String EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE = "LOGGED_IN_WITH_EXTERNAL_AUTHENTICATION";

    /**
     * Log in to the system after external authentication.
     * @param login             user login name
     * @param locale            user locale
     * @throws LoginException   in case of unsuccessful login due to wrong credentials or other issues
     */
    void loginAfterExternalAuthentication(String login, Locale locale) throws LoginException;
}