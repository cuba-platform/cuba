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

package com.haulmont.cuba.core.jmx;

import com.google.gson.Gson;
import com.haulmont.cuba.security.idp.IdpSessionStore;
import com.haulmont.cuba.security.idp.IdpSessionStore.IdpSessionInfo;
import com.haulmont.cuba.security.global.IdpSession;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("cuba_IdpSessionsMBean")
public class IdpSessions implements IdpSessionsMBean {
    @Inject
    protected IdpSessionStore idpSessionStore;

    protected Gson gson = new Gson();

    @Override
    public String getSessionInfo(String sessionId) {
        IdpSessionInfo session = idpSessionStore.getSessionInfo(sessionId);
        if (session == null) {
            return "Session not found";
        }
        return gson.toJson(session);
    }

    @Override
    public String activateSessionTicket(String serviceProviderTicket) {
        IdpSession session = idpSessionStore.activateSessionTicket(serviceProviderTicket);
        if (session == null) {
            return "Session not found";
        }
        return gson.toJson(session);
    }

    @Override
    public String createServiceProviderTicket(String sessionId) {
        String serviceProviderTicket = idpSessionStore.createServiceProviderTicket(sessionId);
        if (serviceProviderTicket == null) {
            return "Session not found";
        }
        return serviceProviderTicket;
    }

    @Override
    public String removeSession(String sessionId) {
        boolean removed = idpSessionStore.removeSession(sessionId);

        return removed ? "Session removed" : "Session not found";
    }

    @Override
    public List<String> getSessions() {
        List<IdpSessionInfo> sessions = idpSessionStore.getSessions();
        return sessions.stream()
                .map(IdpSessionInfo::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTickets() {
        Map<String, IdpSessionStore.IdpSessionTicketInfo> tickets = idpSessionStore.getTickets();

        return tickets.entrySet().stream()
                .map(entry -> {
                    String sessionId = entry.getValue().getSessionId();
                    long createTs = entry.getValue().getCreateTs();

                    return String.format("Ticket: %s Session: %s Since: %s", entry.getKey(), sessionId, createTs);
                })
                .collect(Collectors.toList());
    }
}