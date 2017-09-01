/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * User sessions distributed cache API.
 */
public interface UserSessionsAPI {

    String NAME = "cuba_UserSessions";
    
    void add(UserSession session);

    void remove(UserSession session);

    /**
     * Get user session from cache.
     *
     * @param id        session id
     * @return user session instance or null if not found
     */
    @Nullable
    UserSession get(UUID id);

    /**
     * Get user session from cache, updating its "last used" timestamp.
     *
     * @param id        session id
     * @param propagate whether to propagate the new "last used" timestamp to the cluster
     * @return user session instance or null if not found
     */
    @Nullable
    UserSession get(UUID id, boolean propagate);

    /**
     * Propagates the user session state to the cluster
     * @param id    session id. If session with this id is not found, does nothing.
     */
    void propagate(UUID id);

    /**
     * @return collection of all active sessions
     */
    Collection<UserSessionEntity> getUserSessionInfo();

    /**
     * Immediately remove a sessions from cache.
     * @param id    session id
     */
    void killSession(UUID id);

    /**
     * Finds sessions with attribute with name {@code attributeName} and value equal to passed {@code attributeValue}
     *
     * @param attributeName attribute name
     * @param attributeValue attribute value
     * @return session ids
     */
    List<UUID> findUserSessionsByAttribute(String attributeName, Object attributeValue);

    /**
     * @return session expiration timeout in the cache
     */
    int getExpirationTimeoutSec();

    /**
     * Set session expiration timeout for the cache.
     * @param value timeout in seconds
     */
    void setExpirationTimeoutSec(int value);

    /**
     * @return session send timeout in cluster
     */
    int getSendTimeoutSec();

    /**
     * Set user session ping timeout in cluster.
     * If ping performed {@link com.haulmont.cuba.security.app.UserSessions#get},
     * user session sends in cluster only after specified timeout
     *
     * @param timeout in seconds
     */
    void setSendTimeoutSec(int timeout);

    /**
     * Evict timed out sessions from the cache.
     */
    void processEviction();
}