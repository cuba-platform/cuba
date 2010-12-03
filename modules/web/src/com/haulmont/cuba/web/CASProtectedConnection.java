/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 25.10.2010 20:12:43
 *
 * $Id: CASProtectedConnection.java 3253 2010-11-25 12:41:14Z gorodnov $
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.global.LoginException;

import java.util.Locale;

public class CASProtectedConnection extends AbstractConnection {
    private static final long serialVersionUID = -5483550137345750662L;

    public void login(String login, String password, Locale locale) throws LoginException {
        if (locale == null)
            throw new IllegalArgumentException("Locale is null");

        update(getLoginService().login(login, password, locale));
    }

    @Override
    public String logout() {
        super.logout();
        return "logout";
    }
}
