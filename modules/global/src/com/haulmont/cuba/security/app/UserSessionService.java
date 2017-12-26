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

import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * Service interface to active {@link UserSession}s management.
 */
public interface UserSessionService {

    String NAME = "cuba_UserSessionService";

    /**
     * Get a UserSession by its identifier.
     * <p>
     * When called from the client tier, returns a copy of the session object, so any modifications of its state
     * affect nothing.
     *
     * @param sessionId a session identifier
     * @return an active user session instance copy, if exists
     * @throws com.haulmont.cuba.security.global.NoUserSessionException in case of a session with the specified ID
     * doesn't exist
     * @throws RuntimeException if the session is system
     */
    UserSession getUserSession(UUID sessionId);

    /**
     * Set a session attribute value, propagating changes to the cluster.
     * @param sessionId an active session identifier
     * @param name      attribute name
     * @param value     attribute value
     * @throws com.haulmont.cuba.security.global.NoUserSessionException in case of a session with the specified ID
     * doesn't exist
     * @throws RuntimeException if the session is system
     */
    void setSessionAttribute(UUID sessionId, String name, Serializable value);

    /**
     * Remove a session attribute, propagating changes to the cluster.
     * @param sessionId an active session identifier
     * @param name      attribute name
     * @throws com.haulmont.cuba.security.global.NoUserSessionException in case of a session with the specified ID
     * doesn't exist
     * @throws RuntimeException if the session is system
     */
    void removeSessionAttribute(UUID sessionId, String name);

    /**
     * Set user locale into the session, propagating changes to the cluster.
     * @param sessionId an active session identifier
     * @param locale    user locale
     * @throws com.haulmont.cuba.security.global.NoUserSessionException in case of a session with the specified ID
     * doesn't exist
     * @throws RuntimeException if the session is system
     */
    void setSessionLocale(UUID sessionId, Locale locale);

    /**
     * Set user time zone into the session, propagating changes to the cluster.
     * @param sessionId an active session identifier
     * @param timeZone  user time zone
     * @throws com.haulmont.cuba.security.global.NoUserSessionException in case of a session with the specified ID
     * doesn't exist
     * @throws RuntimeException if the session is system
     */
    void setSessionTimeZone(UUID sessionId, TimeZone timeZone);

    /**
     * Set client's address into the session, propagating changes to the cluster.
     * @param sessionId an active session identifier
     * @param address   client's address
     * @throws com.haulmont.cuba.security.global.NoUserSessionException in case of a session with the specified ID
     * doesn't exist
     * @throws RuntimeException if the session is system
     */
    void setSessionAddress(UUID sessionId, String address);

    /**
     * Set client's information into the session, propagating changes to the cluster.
     * @param sessionId     an active session identifier
     * @param clientInfo    client's info
     * @throws com.haulmont.cuba.security.global.NoUserSessionException in case of a session with the specified ID
     * doesn't exist
     * @throws RuntimeException if the session is system
     */
    void setSessionClientInfo(UUID sessionId, String clientInfo);

    /**
     * @deprecated Use {@link #loadUserSessionEntities(Filter)} method passing a filter
     */
    @Deprecated
    Collection<UserSessionEntity> getUserSessionInfo();

    /**
     * Disconnect a session. Returns silently if there is no active session with the specified ID or the session is system.
     * @param id    an active session identifier
     */
    void killSession(UUID id);

    /**
     * Post a message to the list of active user sessions. If a session is not found or is system, it is ignored.
     * @param sessionIds    list of session identifiers
     * @param message       the message text
     */
    void postMessage(List<UUID> sessionIds, String message);

    /**
     * Poll for messages left for the current user session. Can also be used for session ping to prevent expiring on
     * user idle time.
     * @return  all messages sent to the current session in one string separated by carriage returns
     */
    @Nullable
    String getMessages();

    /**
     * Get effective user permission.
     * @param user              user
     * @param permissionType    type of permission
     * @param target            permission target
     * @return effective permission value
     */
    Integer getPermissionValue(User user, PermissionType permissionType, String target);

    /**
     * Load list of non-persistent entities representing active user sessions.
     *
     * @param filter can be used to limit the loaded list. Pass {@link Filter#ALL} to load all.
     * @return list of entities
     */
    Collection<UserSessionEntity> loadUserSessionEntities(Filter filter);

    class Filter implements Serializable {

        /**
         * An empty filter for loading all active sessions.
         */
        public static final Filter ALL = create();

        private String userLogin;
        private String userName;
        private String address;
        private String clientInfo;
        private boolean strict;

        public static Filter create() {
            return new Filter();
        }

        public String getUserLogin() {
            return userLogin;
        }

        /**
         * User login in lower case.
         */
        public Filter setUserLogin(String userLogin) {
            this.userLogin = userLogin;
            return this;
        }

        public String getUserName() {
            return userName;
        }

        /**
         * User name.
         */
        public Filter setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public String getAddress() {
            return address;
        }

        /**
         * Client IP address.
         */
        public Filter setAddress(String address) {
            this.address = address;
            return this;
        }

        public String getClientInfo() {
            return clientInfo;
        }

        /**
         * Client browser info.
         */
        public Filter setClientInfo(String clientInfo) {
            this.clientInfo = clientInfo;
            return this;
        }

        public boolean isStrict() {
            return strict;
        }

        /**
         * Whether to check for strict equality of other parameters.
         */
        public Filter setStrict(boolean strict) {
            this.strict = strict;
            return this;
        }
    }
}
