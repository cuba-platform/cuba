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

import com.haulmont.cuba.security.entity.Profile;

import javax.ejb.Remote;
import java.util.List;
import java.util.Locale;

@Remote
public interface LoginServiceRemote
{
    String JNDI_NAME = "cuba/security/LoginService";
    
    List<Profile> authenticate(String login, String password, Locale locale) throws LoginException;

    UserSession login(String login, String password, String profileName, Locale locale) throws LoginException;

    void logout();

    void ping();
}
