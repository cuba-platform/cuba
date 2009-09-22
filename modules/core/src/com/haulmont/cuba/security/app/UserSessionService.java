/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.02.2009 18:18:22
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.entity.UserSessionEntity;

import javax.ejb.Local;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;

/**
 * Local interface to {@link com.haulmont.cuba.security.app.UserSessionServiceBean}
 */
@Local
public interface UserSessionService
{
    String JNDI_NAME = "cuba/security/UserSessionService";

    void putSessionAttribute(UUID sessionId, String name, Serializable value);

    Collection<UserSessionEntity> getUserSessionInfo();

    void killSession(UUID id);

    void pingSession();
}
