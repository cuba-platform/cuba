/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:13:18
 *
 * $Id$
 */
package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.entity.UserSessionEntity;

import java.util.UUID;
import java.util.Collection;

public interface UserSessionsAPI
{
    void add(UserSession session);

    void remove(UserSession session);

    UserSession get(UUID id);

    Collection<UserSessionEntity> getUserSessionInfo();

    void killSession(UUID id);
}
