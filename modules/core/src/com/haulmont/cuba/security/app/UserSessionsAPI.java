/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.entity.UserSessionEntity;

import java.util.UUID;
import java.util.Collection;

/**
 * User sessions distributed cache API.
 *
 * @version $Id$
 *
 * @author krivopustov
 */
public interface UserSessionsAPI
{
    String NAME = "cuba_UserSessions";
    
    void add(UserSession session);

    void remove(UserSession session);

    UserSession get(UUID id);

    Collection<UserSessionEntity> getUserSessionInfo();

    void killSession(UUID id);
}
