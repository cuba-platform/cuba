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
 */

package com.haulmont.cuba.security.app;

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
     * @param params   map of login parameters. Supported parameters are:
     *                 <ul>
     *                 <li>"com.haulmont.cuba.core.global.ClientType": "WEB" or "DESKTOP". It is used to check the
     *                      "cuba.gui.loginToClient" specific permission.</li>
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
     * @param serviceProviderTicket service provider ticket
     * @return IDP session object
     */
    @Nullable
    IdpSession activateServiceProviderTicket(String serviceProviderTicket);

    /**
     * @param sessionId IDP session id
     * @return new service provider ticket
     */
    @Nullable
    String createServiceProviderTicket(String sessionId);

    /**
     * @param sessionId IDP session id
     * @return IDP session object
     */
    @Nullable
    IdpSession getSession(String sessionId);

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