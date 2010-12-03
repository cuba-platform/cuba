/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 25.11.2010 15:33:25
 *
 * $Id: ActiveDirectoryConnection.java 3253 2010-11-25 12:41:14Z gorodnov $
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.global.LoginException;

import java.util.Locale;

public interface ActiveDirectoryConnection {

    void loginActiveDirectory(String login, Locale locale) throws LoginException;
}
