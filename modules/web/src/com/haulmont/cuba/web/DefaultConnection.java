/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 22.10.2010 18:18:36
 *
 * $Id: DefaultConnection.java 3253 2010-11-25 12:41:14Z gorodnov $
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;

import java.util.Locale;

/**
 * Default {@link Connection} implementation for web-client.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DefaultConnection extends AbstractConnection implements ActiveDirectoryConnection {

    @Override
    public void login(String login, String password, Locale locale) throws LoginException {
        if (locale == null)
            throw new IllegalArgumentException("Locale is null");

        update(getLoginService().login(login, password, locale));
    }

    @Override
    public void loginActiveDirectory(String login, Locale locale) throws LoginException {
        if (locale == null)
            throw new IllegalArgumentException("Locale is null");

        String password = ConfigProvider.getConfig(WebConfig.class).getTrustedClientPassword();
        update(getLoginService().loginTrusted(login, password, locale));
    }

    @Override
    public String logout() {
        super.logout();
        return ActiveDirectoryHelper.useActiveDirectory()? "login" : "";
    }
}
