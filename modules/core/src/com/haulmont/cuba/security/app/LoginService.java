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

import com.haulmont.cuba.security.global.LoginServiceRemote;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import javax.ejb.Local;
import java.util.Locale;

@Local
public interface LoginService extends LoginServiceRemote
{
    UserSession loginActiveDirectory(String activeDirectoryUser, Locale locale) throws LoginException;
}
