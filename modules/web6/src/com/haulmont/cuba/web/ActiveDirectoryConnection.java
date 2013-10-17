/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.global.LoginException;

import java.util.Locale;

/**
 * Interface to be implemented by middleware connection objects supporting ActiveDirectory integration.
 */
public interface ActiveDirectoryConnection {

    /**
     * Log in to the system using ActiveDirectory integration.
     * @param login             user login name
     * @param locale            user locale
     * @throws LoginException   in case of unsuccesful login due to wrong credentials or other issues
     */
    void loginActiveDirectory(String login, Locale locale) throws LoginException;
}
