/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.sys.UserSessionFinder;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * User sessions distributed cache.
 */
public interface UserSessionsAPI extends UserSessionFinder {

    String NAME = "cuba_UserSessions";
    
    /**
     * Get user session from cache.
     *
     * @param id        session id
     * @return user session instance or null if not found
     */
    @Nullable
    UserSession get(UUID id);

    /**
     * Get user session from cache.
     *
     * @param id        session id
     * @return user session instance
     * @throws NoUserSessionException  if not found
     */
    UserSession getNN(UUID id);

    /**
     * Get user session from cache, updating its "last used" timestamp.
     *
     * @param id        session id
     * @return user session instance or null if not found
     */
    @Nullable
    UserSession getAndRefresh(UUID id);

    /**
     * Get user session from cache, updating its "last used" timestamp.
     *
     * @param id        session id
     * @return user session instance
     * @throws NoUserSessionException  if not found
     */
    UserSession getAndRefreshNN(UUID id);

    /**
     * Get user session from cache, updating its "last used" timestamp and optionally propagating the new timestamp
     * to the cluster.
     *
     * @param id        session id
     * @param propagate whether to propagate the new "last used" timestamp to the cluster
     * @return user session instance or null if not found
     */
    @Nullable
    UserSession getAndRefresh(UUID id, boolean propagate);

    /**
     * Get user session from cache, updating its "last used" timestamp and optionally propagating the new timestamp
     * to the cluster.
     *
     * @param id        session id
     * @param propagate whether to propagate the new "last used" timestamp to the cluster
     * @return user session instance
     * @throws NoUserSessionException  if not found
     */
    UserSession getAndRefreshNN(UUID id, boolean propagate);

    /**
     * Stream of non-persistent entities representing active user sessions.
     */
    Stream<UserSessionEntity> getUserSessionEntitiesStream();

    /**
     * Stream of active user sessions.
     */
    Stream<UserSession> getUserSessionsStream();

    /**
     * INTERNAL.
     *
     * Add a session.
     */
    void add(UserSession session);

    /**
     * INTERNAL.
     *
     * Remove a session.
     */
    void remove(UserSession session);

    /**
     * INTERNAL.
     *
     * Propagates the user session state to the cluster
     * @param id    session id. If session with this id is not found, does nothing.
     */
    void propagate(UUID id);

    /**
     * @deprecated use {@link #getUserSessionEntitiesStream()} or {@link #getUserSessionsStream()} methods
     */
    @Deprecated
    Collection<UserSessionEntity> getUserSessionInfo();

    /**
     * INTERNAL.
     *
     * Immediately remove a sessions from cache.
     * @param id    session id
     */
    void killSession(UUID id);

    /**
     * @deprecated use {@link #getUserSessionsStream()} and filter it as needed
     */
    @Deprecated
    List<UUID> findUserSessionsByAttribute(String attributeName, Object attributeValue);

    /**
     * INTERNAL.
     *
     * @return session expiration timeout in the cache
     * @see #setExpirationTimeoutSec(int)
     */
    int getExpirationTimeoutSec();

    /**
     * INTERNAL.
     *
     * Set session expiration timeout for the cache.
     * @param value timeout in seconds
     */
    void setExpirationTimeoutSec(int value);

    /**
     * INTERNAL.
     *
     * @return session sending timeout in cluster
     * @see #setSendTimeoutSec(int)
     */
    int getSendTimeoutSec();

    /**
     * INTERNAL.
     *
     * Set user session ping timeout in cluster.
     * If ping is performed by {@link UserSessionsAPI#getAndRefresh},
     * the user session is sent to the cluster only after the specified timeout.
     *
     * @param timeout in seconds
     */
    void setSendTimeoutSec(int timeout);

    /**
     * INTERNAL.
     *
     * Evict timed out sessions from the cache.
     */
    void processEviction();
}