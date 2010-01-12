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

import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSessionEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * Service interface to UserSessions MBean
 */
public interface UserSessionService
{
    String NAME = "cuba_UserSessionService";

    @Deprecated
    String JNDI_NAME = NAME;

    void putSessionAttribute(UUID sessionId, String name, Serializable value);

    Collection<UserSessionEntity> getUserSessionInfo();

    void killSession(UUID id);

    void pingSession();

    Integer getPermissionValue(User user, PermissionType permissionType, String target);

}
