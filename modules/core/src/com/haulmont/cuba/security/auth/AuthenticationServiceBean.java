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

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.remoting.RemoteClientInfo;
import com.haulmont.cuba.security.app.UserSessionLog;
import com.haulmont.cuba.security.entity.SessionAction;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.InternalAuthenticationException;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;

@Component(AuthenticationService.NAME)
public class AuthenticationServiceBean implements AuthenticationService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationServiceBean.class);

    @Inject
    protected AuthenticationManager authenticationManager;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UserSessionLog userSessionLog;

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
            AuthenticationDetails authenticationDetails = authenticationManager.login(credentials);

            userSessionLog.createSessionLogRecord(authenticationDetails.getSession(), SessionAction.LOGIN, Collections.emptyMap());

            return authenticationDetails;
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
            userSessionLog.updateSessionLogRecord(currentSession, SessionAction.SUBSTITUTION);

            UserSession substitutionSession = authenticationManager.substituteUser(substitutedUser);

            userSessionLog.createSessionLogRecord(substitutionSession, SessionAction.LOGIN, currentSession, Collections.emptyMap());
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

            userSessionLog.updateSessionLogRecord(session, SessionAction.LOGOUT);

            authenticationManager.logout();

            userSessionLog.updateSessionLogRecord(session, SessionAction.LOGOUT);
        } catch (Throwable e) {
            log.error("Logout error", e);
            throw new RuntimeException("Logout error: " + e.toString());
        }
    }

    protected LoginException wrapInLoginException(Throwable throwable) {
        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause == null)
            rootCause = throwable;
        // send text only to avoid ClassNotFoundException when the client has no dependency to some library

        // todo rework, do not send exception messages they can contain sensitive configuration data

        return new InternalAuthenticationException(rootCause.toString());
    }

    protected void preprocessCredentials(Credentials credentials) {
        if (credentials instanceof TrustedClientCredentials) {
            RemoteClientInfo remoteClientInfo = RemoteClientInfo.get();

            TrustedClientCredentials trustedClientCredentials = (TrustedClientCredentials) credentials;
            if (remoteClientInfo != null) {
                trustedClientCredentials.setClientIpAddress(remoteClientInfo.getAddress());
            } else {
                trustedClientCredentials.setClientIpAddress(null);
            }
        }
    }
}