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

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.UuidSource;
import com.haulmont.cuba.security.app.UserSessions;
import com.haulmont.cuba.security.auth.AbstractCredentials;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.AuthenticationManager;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.IdpSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.TrustedClientOnly;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service(IdpService.NAME)
@TrustedClientOnly
public class IdpServiceBean implements IdpService {

    private static final Logger log = LoggerFactory.getLogger(IdpServiceBean.class);

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected IdpSessionStore sessionStore;

    @Inject
    protected AuthenticationManager authenticationManager;

    @Inject
    protected UuidSource uuidSource;

    @Inject
    protected UserSessions userSessions;

    @Nonnull
    @Override
    public IdpLoginResult login(Credentials credentials,
                                @Nullable Map<String, Object> parameters) throws LoginException {
        log.debug("Authenticating CUBA user for IDP");

        AuthenticationDetails sessionDetails = authenticationManager.authenticate(credentials);
        User user = sessionDetails.getSession().getUser();

        IdpSession session = new IdpSession(createIdpSessionId());
        session.setLogin(user.getLogin());
        session.setEmail(user.getEmail());

        Locale userLocale = messageTools.getDefaultLocale();
        if (user.getLanguage() != null) {
            userLocale = LocaleUtils.toLocale(user.getLanguage());
        }
        if (credentials instanceof AbstractCredentials
                && ((AbstractCredentials) credentials).isOverrideLocale()) {
            userLocale = ((AbstractCredentials) credentials).getLocale();
        }

        session.setLocale(userLocale.toLanguageTag());

        String serviceProviderTicket = sessionStore.putSession(session);

        return new IdpLoginResult(session.getId(), serviceProviderTicket);
    }

    protected String createIdpSessionId() {
        return uuidSource.createUuid().toString().replace("-", "");
    }

    @Override
    public IdpSession setSessionAttribute(String sessionId, String name, Serializable value) {
        IdpSession session = sessionStore.getSession(sessionId);
        if (session != null) {
            Map<String, Object> attributes = session.getAttributes();
            if (attributes == null) {
                attributes = new HashMap<>();
                session.setAttributes(attributes);
            }
            attributes.put(name, value);

            sessionStore.propagate(session.getId());
        }
        return session;
    }

    @Override
    public IdpSession removeSessionAttribute(String sessionId, String name) {
        IdpSession session = sessionStore.getSession(sessionId);
        if (session != null) {
            Map<String, Object> attributes = session.getAttributes();
            if (attributes != null) {
                attributes.remove(name);
            }
            sessionStore.propagate(session.getId());
        }
        return session;
    }

    @Override
    public boolean logout(String sessionId) {
        return sessionStore.removeSession(sessionId);
    }

    @Override
    public boolean logoutUserSession(String idpSessionId) {
        List<UUID> sessionIds = userSessions.getUserSessionsStream()
                .filter(session -> Objects.equals(session.getAttribute(IDP_USER_SESSION_ATTRIBUTE), idpSessionId))
                .map(UserSession::getId)
                .collect(Collectors.toList());

        for (UUID sessionId : sessionIds) {
            userSessions.killSession(sessionId);
        }

        return sessionIds.size() > 0;
    }

    @Nullable
    @Override
    public IdpSession activateServiceProviderTicket(String serviceProviderTicket) {
        return sessionStore.activateSessionTicket(serviceProviderTicket);
    }

    @Nullable
    @Override
    public String createServiceProviderTicket(String sessionId) {
        return sessionStore.createServiceProviderTicket(sessionId);
    }

    @Nullable
    @Override
    public IdpSession getSession(String sessionId) {
        return sessionStore.getSession(sessionId);
    }

    @Override
    public List<String> processEviction(int sessionExpirationTimeoutSec, int ticketExpirationTimeoutSec) {
        return sessionStore.processEviction(sessionExpirationTimeoutSec, ticketExpirationTimeoutSec).stream()
                .map(IdpSessionStore.IdpSessionInfo::getId)
                .collect(Collectors.toList());
    }
}