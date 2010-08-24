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

import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

/**
 * Interface to {@link com.haulmont.cuba.security.app.LoginWorkerBean}
 */
public interface LoginWorker extends LoginService
{
    String NAME = "cuba_LoginWorker";

    UserSession loginSystem(String login, String password) throws LoginException;
}
