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

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Session store for {@link IdpService}.
 */
public interface IdpSessionStore {
    String NAME = "cuba_IdpSessionStore";

    String putSession(IdpSession session);

    IdpSession getSession(String sessionId);

    IdpSessionInfo getSessionInfo(String sessionId);

    boolean removeSession(String sessionId);

    IdpSession activateSessionTicket(String serviceProviderTicket);

    String createServiceProviderTicket(String sessionId);

    List<IdpSessionInfo> getSessions();

    Map<String, IdpSessionTicketInfo> getTickets();

    List<IdpSessionInfo> processEviction(int sessionExpirationTimeoutSec, int expirationTimeoutSec);

    /**
     * Propagates the IDP session state to the cluster
     *
     * @param sessionId session id. If session with this id is not found, does nothing.
     */
    void propagate(String sessionId);

    class IdpSessionTicketInfo implements Serializable {
        private final String sessionId;
        private final long createTs;

        public IdpSessionTicketInfo(String sessionId, long createTs) {
            this.sessionId = sessionId;
            this.createTs = createTs;
        }

        public String getSessionId() {
            return sessionId;
        }

        public long getCreateTs() {
            return createTs;
        }

        @Override
        public String toString() {
            return "IdpSessionTicketInfo{" +
                    "id='" + sessionId + '\'' +
                    ", createTs=" + createTs +
                    '}';
        }
    }

    class IdpSessionInfo implements Serializable {
        private final String id;
        private final String login;
        private final String email;
        private final String locale;
        private final Date since;
        private final Date lastUsed;

        public IdpSessionInfo(String id, String login, String email, String locale, Date since, Date lastUsed) {
            this.id = id;
            this.login = login;
            this.email = email;
            this.locale = locale;
            this.since = since;
            this.lastUsed = lastUsed;
        }

        public String getId() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public String getEmail() {
            return email;
        }

        public String getLocale() {
            return locale;
        }

        public Date getSince() {
            return since;
        }

        public Date getLastUsed() {
            return lastUsed;
        }
    }
}