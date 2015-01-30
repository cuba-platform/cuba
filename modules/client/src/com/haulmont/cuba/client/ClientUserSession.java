/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;

import java.io.Serializable;
import java.util.Locale;

/**
 * Client-side extension of {@link UserSession}.
 *
 * <p>Sends updates of the user session properties to the middleware.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public class ClientUserSession extends UserSession {

    private static final long serialVersionUID = -5358664165808633540L;

    public ClientUserSession(UserSession src) {
        super(src);
    }

    @Override
    public void setAttribute(String name, Serializable value) {
        super.setAttribute(name, value);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionAttribute(id, name, value);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionLocale(id, locale);
    }

    @Override
    public void setAddress(String address) {
        super.setAddress(address);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionAddress(id, address);
    }

    @Override
    public void setClientInfo(String clientInfo) {
        super.setClientInfo(clientInfo);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionClientInfo(id, clientInfo);
    }
}
