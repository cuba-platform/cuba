/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;

import java.io.Serializable;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ClientUserSession extends UserSession {

    public ClientUserSession(UserSession src) {
        super(src);
    }

    @Override
    public void setAttribute(String name, Serializable value) {
        super.setAttribute(name, value);
        UserSessionService uss = (UserSessionService) AppContext.getApplicationContext().getBean(UserSessionService.NAME);
        uss.setSessionAttribute(id, name, value);
    }

    @Override
    public void setAddress(String address) {
        super.setAddress(address);
        UserSessionService uss = (UserSessionService) AppContext.getApplicationContext().getBean(UserSessionService.NAME);
        uss.setSessionAddress(id, address);
    }

    @Override
    public void setClientInfo(String clientInfo) {
        super.setClientInfo(clientInfo);
        UserSessionService uss = (UserSessionService) AppContext.getApplicationContext().getBean(UserSessionService.NAME);
        uss.setSessionClientInfo(id, clientInfo);
    }
}
