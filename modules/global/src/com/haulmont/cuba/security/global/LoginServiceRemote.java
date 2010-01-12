/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 13:25:41
 *
 * $Id$
 */
package com.haulmont.cuba.security.global;

import com.haulmont.cuba.security.entity.User;

import java.util.Locale;

/**
 * Remote interface to LoginServiceBean
 */
public interface LoginServiceRemote
{
    String NAME = "cuba_LoginService";

    @Deprecated
    String JNDI_NAME = NAME;

    /**
     * Login to middleware for local and remote clients.
     * @param login login name
     * @param password encrypted password
     * @param locale client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    UserSession login(String login, String password, Locale locale) throws LoginException;

    void logout();

    UserSession substituteUser(User substitutedUser);

    void ping();
}
