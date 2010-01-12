/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 9:40:11
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.LoginServiceRemote;
import com.haulmont.cuba.security.global.UserSession;

import java.util.Locale;

/**
 * Local interface to LoginServiceBean
 */
public interface LoginService extends LoginServiceRemote
{
    /**
     * Login using password stored in ActiveDirectory. For local clients (e.g. web) only.
     * @param login login name
     * @param locale client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    UserSession loginActiveDirectory(String login, Locale locale) throws LoginException;
}
