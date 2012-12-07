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
public interface UserSessionsAPI {

    String NAME = "cuba_UserSessions";
    
    void add(UserSession session);

    void remove(UserSession session);

    /**
     * Get user session from cache, updating its "last used" timestamp.
     * @param id        session id
     * @param propagate whether to propagate the new "last used" timestamp to the cluster
     * @return          user session instance or null if not found
     */
    UserSession get(UUID id, boolean propagate);

    /**
     * Propagates the user session state to the cluster
     * @param id    session id. If session with this id is not found, does nothing.
     */
    void propagate(UUID id);

    Collection<UserSessionEntity> getUserSessionInfo();

    void killSession(UUID id);

    int getExpirationTimeoutSec();

    void setExpirationTimeoutSec(int value);

    void processEviction();
}
