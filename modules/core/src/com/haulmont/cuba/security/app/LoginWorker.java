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

import javax.ejb.Local;

/**
 * Local interface to {@link com.haulmont.cuba.security.app.LoginWorkerBean}
 */
@Local
public interface LoginWorker extends LoginService
{
    String JNDI_NAME = "cuba/security/LoginWorker";
}
