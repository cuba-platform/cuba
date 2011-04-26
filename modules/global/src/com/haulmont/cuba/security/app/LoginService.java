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

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import java.util.Locale;
import java.util.UUID;

/**
 * Interface to LoginServiceBean
 */
public interface LoginService
{
    String NAME = "cuba_LoginService";

    /**
     * Login using user name and password
     * @param login login name
     * @param password encrypted password
     * @param locale client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    UserSession login(String login, String password, Locale locale) throws LoginException;

    /**
     * Login using user name and trusted password
     *
     *
     * @param login login name
     * @param password
     * @param locale client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    UserSession loginTrusted(String login, String password, Locale locale) throws LoginException;

    void logout();

    UserSession substituteUser(User substitutedUser);

    void ping();

    UserSession getSession(UUID sessionId);
}
