/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.global.LoginException;

import java.util.Locale;

/**
 * This class is used as a {@link Connection} implementation in case of CAS integration.
 *
 * @author krokhin
 * @version $Id$
 */
public class CASProtectedConnection extends AbstractConnection {

    @Override
    public void login(String login, String password, Locale locale) throws LoginException {
        if (locale == null)
            throw new IllegalArgumentException("Locale is null");

        update(loginService.login(login, password, locale));
    }

    @Override
    public String logout() {
        super.logout();
        return "logout";
    }
}
