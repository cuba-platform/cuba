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

package com.haulmont.cuba.security.auth;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.remoting.RemoteClientInfo;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.app.UserSessionLog;
import com.haulmont.cuba.security.entity.SessionAction;
import com.haulmont.cuba.security.entity.SessionLogEntry;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.InternalAuthenticationException;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Component(AuthenticationService.NAME)
public class AuthenticationServiceBean implements AuthenticationService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationServiceBean.class);

    @Inject
    protected AuthenticationManager authenticationManager;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UserSessionLog userSessionLog;
    @Inject
    protected Authentication authentication;
    @Inject
    protected TrustedLoginHandler trustedLoginHandler;

    @Nonnull
    @Override
    public AuthenticationDetails authenticate(Credentials credentials) throws LoginException {
        try {
            preprocessCredentials(credentials);

            //noinspection UnnecessaryLocalVariable
            AuthenticationDetails authenticationDetails = authenticationManager.authenticate(credentials);
            return authenticationDetails;

        } catch (InternalAuthenticationException ie) {
            log.error("Authentication error", ie);
            throw ie;
        } catch (LoginException e) {
            log.info("Authentication failed: {}", e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Authentication error", e);
            throw wrapInLoginException(e);
        }
    }

    @Nonnull
    @Override
    public AuthenticationDetails login(Credentials credentials) throws LoginException {
        try {
            preprocessCredentials(credentials);

            //noinspection UnnecessaryLocalVariable
            AuthenticationDetails details = authenticationManager.login(credentials);

            Map<String, Object> logParams = emptyMap();
            if (credentials instanceof AbstractClientCredentials) {
                ClientType clientType = ((AbstractClientCredentials) credentials).getClientType();
                if (clientType != null) {
                    logParams = ParamsMap.of(ClientType.class.getName(), clientType.name());
                }
            }

            userSessionLog.createSessionLogRecord(details.getSession(), SessionAction.LOGIN, logParams);

            return details;
        } catch (InternalAuthenticationException ie) {
            log.error("Login error", ie);
            throw ie;
        } catch (LoginException e) {
            log.info("Login failed: {}", e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Nonnull
    @Override
    public UserSession substituteUser(User substitutedUser) {
        try {
            UserSession currentSession = userSessionSource.getUserSession();
            SessionLogEntry logEntry = userSessionLog.updateSessionLogRecord(currentSession, SessionAction.SUBSTITUTION);

            UserSession substitutionSession =
                    authenticationManager.substituteUser(substitutedUser);

            Map<String, Object> logParams = emptyMap();
            if (logEntry != null && logEntry.getClientType() != null) {
                logParams = ParamsMap.of(ClientType.class.getName(), logEntry.getClientType().name());
            }

            userSessionLog.createSessionLogRecord(substitutionSession, SessionAction.LOGIN, currentSession, logParams);

            return substitutionSession;
        } catch (Throwable e) {
            log.error("Substitution error", e);
            throw new RuntimeException("Substitution error: " + e.toString());
        }
    }

    @Override
    public void logout() {
        try {
            UserSession session = userSessionSource.getUserSession();

            if (session != null && session.isSystem()) {
                throw new RuntimeException("Logout of system session from client is not permitted");
            }

            authenticationManager.logout();

            userSessionLog.updateSessionLogRecord(session, SessionAction.LOGOUT);
        } catch (NoUserSessionException e) {
            log.debug("An attempt to perform logout for expired session", e);
            throw e;
        } catch (Throwable e) {
            log.error("Logout error", e);
            throw new RuntimeException("Logout error: " + e.toString());
        }
    }

    protected LoginException wrapInLoginException(Throwable throwable) {
        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause == null) {
            rootCause = throwable;
        }

        // todo rework, do not send exception messages they can contain sensitive configuration data

        // send text only to avoid ClassNotFoundException when the client has no dependency to some library
        return new InternalAuthenticationException(rootCause.toString());
    }

    protected void preprocessCredentials(Credentials credentials) {
        RemoteClientInfo remoteClientInfo = RemoteClientInfo.get();

        if (credentials instanceof TrustedClientCredentials) {
            TrustedClientCredentials tcCredentials = (TrustedClientCredentials) credentials;
            if (remoteClientInfo != null) {
                tcCredentials.setClientIpAddress(remoteClientInfo.getAddress());
            } else {
                tcCredentials.setClientIpAddress(null);
            }
        }

        if (remoteClientInfo != null &&
                credentials instanceof AbstractClientCredentials) {
            String address = remoteClientInfo.getAddress();
            if (!trustedLoginHandler.checkAddress(address)) {
                ((AbstractClientCredentials) credentials).setIpAddress(address);
            }
        }
    }
}