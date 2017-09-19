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

package com.haulmont.cuba.security.idp;

import com.haulmont.cuba.security.global.IdpSession;
import com.haulmont.cuba.security.global.LoginException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Provides session storage for IDP web service.
 */
public interface IdpService {
    String NAME = "cuba_IdpService";

    String IDP_USER_SESSION_ATTRIBUTE = "idpSessionId";

    /**
     * Login using user name and password
     *
     * @param login    login name
     * @param password encrypted password
     * @param locale   client locale
     * @param params   map of login parameters. Supported parameters are:<br>
     *                 - "com.haulmont.cuba.core.global.ClientType": "WEB" or "DESKTOP". It is used to check the
     *                   "cuba.gui.loginToClient" specific permission.
     * @return login result
     * @throws LoginException in case of unsuccessful login
     */
    @Nonnull
    IdpLoginResult login(String login, String password, Locale locale,
                         @Nullable Map<String, Object> params) throws LoginException;

    /**
     * Logout idp session
     *
     * @param idpSessionId idp session id
     * @return true if session found and logged out
     */
    boolean logout(String idpSessionId);

    /***
     * Logout user session with special attribute idpSessionId equal to passed parameter.
     *
     * @param idpSessionId idp session id
     * @return true if session found and logged out
     */
    boolean logoutUserSession(String idpSessionId);

    /**
     * Activate service provider ticket and get IDP session.
     *
     * @param serviceProviderTicket service provider ticket
     * @return IDP session object or null if service provider ticket not found.
     */
    @Nullable
    IdpSession activateServiceProviderTicket(String serviceProviderTicket);

    /**
     * Create service provider ticket.
     *
     * @param sessionId IDP session id
     * @return new service provider ticket or null if session not found.
     */
    @Nullable
    String createServiceProviderTicket(String sessionId);

    /**
     * Get session object.
     *
     * @param sessionId IDP session id
     * @return IDP session object or null if session not found.
     */
    @Nullable
    IdpSession getSession(String sessionId);

    /**
     * Set a session attribute value, propagating changes to the cluster.
     *
     * @param sessionId an active session identifier
     * @param name      attribute name
     * @param value     attribute value
     * @return updated user session or null if session not found.
     */
    @Nullable
    IdpSession setSessionAttribute(String sessionId, String name, Serializable value);

    /**
     * Remove a session attribute, propagating changes to the cluster.
     *
     * @param sessionId an active session identifier
     * @param name      attribute name
     * @return updated user session or null if session not found.
     */
    @Nullable
    IdpSession removeSessionAttribute(String sessionId, String name);

    /**
     * Evict timed out sessions and their tickets from the session storage.
     *
     * @return removed session ids.
     */
    List<String> processEviction(int sessionExpirationTimeoutSec, int ticketExpirationTimeoutSec);

    class IdpLoginResult implements Serializable {
        public IdpLoginResult() {
        }

        public IdpLoginResult(String sessionId, String serviceProviderTicket) {
            this.sessionId = sessionId;
            this.serviceProviderTicket = serviceProviderTicket;
        }

        private String sessionId;
        private String serviceProviderTicket;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getServiceProviderTicket() {
            return serviceProviderTicket;
        }

        public void setServiceProviderTicket(String serviceProviderTicket) {
            this.serviceProviderTicket = serviceProviderTicket;
        }

        @Override
        public String toString() {
            return "IdpLoginResult{" +
                    "sessionId='" + sessionId + '\'' +
                    ", serviceProviderTicket='" + serviceProviderTicket + '\'' +
                    '}';
        }
    }
}