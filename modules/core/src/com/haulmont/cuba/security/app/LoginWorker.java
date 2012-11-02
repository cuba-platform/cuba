/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 14:05:10
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
 * Interface to {@link com.haulmont.cuba.security.app.LoginWorkerBean}
 */
public interface LoginWorker {
    String NAME = "cuba_LoginWorker";

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
     * @param login login name
     * @param password Trusted password
     * @param locale client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    UserSession loginTrusted(String login, String password, Locale locale) throws LoginException;

    void logout();

    UserSession substituteUser(User substitutedUser);

    UserSession getSession(UUID sessionId);

    /**
     * Login for MBeans.
     *
     * @param login    user login
     * @return User session
     * @throws LoginException If used invalid credentials
     */
    UserSession loginSystem(String login) throws LoginException;
}
