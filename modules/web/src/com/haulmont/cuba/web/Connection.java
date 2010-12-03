/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 22.10.2010 18:40:11
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import java.io.Serializable;
import java.util.Locale;

public interface Connection extends Serializable {

    void login(String login, String password, Locale locale) throws LoginException;

    /**
     * Log out of the system.
     * Returns URL to which a user will be redirected after logout
     * 
     * @return Redirection URL
     */
    String logout();

    void substituteUser(User substitutedUser);

    boolean isConnected();

    UserSession getSession();

    void update(UserSession session) throws LoginException;

    void addListener(ConnectionListener listener);

    void removeListener(ConnectionListener listener);

    void addListener(UserSubstitutionListener listener);

    void removeListener(UserSubstitutionListener listener);
}
