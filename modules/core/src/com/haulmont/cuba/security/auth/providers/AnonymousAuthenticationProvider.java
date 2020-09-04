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

package com.haulmont.cuba.security.auth.providers;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.auth.AnonymousUserCredentials;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.SimpleAuthenticationDetails;
import com.haulmont.cuba.security.entity.SecurityScope;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.UUID;

@Component("cuba_AnonymousAuthenticationProvider")
public class AnonymousAuthenticationProvider extends AbstractAuthenticationProvider implements Ordered {
    @Inject
    protected UserSessionManager userSessionManager;
    @Inject
    protected ServerConfig serverConfig;
    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    public AnonymousAuthenticationProvider(Persistence persistence, Messages messages) {
        super(persistence, messages);
    }

    @Override
    public AuthenticationDetails authenticate(Credentials credentials) throws LoginException {
        AnonymousUserCredentials anonymous = (AnonymousUserCredentials) credentials;

        String login = serverConfig.getAnonymousLogin();

        Locale credentialsLocale = anonymous.getLocale() == null ?
                messages.getTools().trimLocale(messages.getTools().getDefaultLocale()) : anonymous.getLocale();

        User user = loadUser(login);
        if (user == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        String securityScope = ((AnonymousUserCredentials) credentials).getSecurityScope();
        Locale userLocale = getUserLocale(anonymous, user);

        UUID anonymousSessionId = generateAnonymousSessionId(securityScope);

        UserSession session = null;
        if (anonymousSessionId == null) {
            session = createSession(anonymous, user, userLocale, securityScope);
        } else {
            session = createSession(anonymousSessionId, anonymous, user, userLocale, securityScope);
        }

        session.setClientInfo("System anonymous session");

        return new SimpleAuthenticationDetails(session);
    }

    @Nullable
    protected UUID generateAnonymousSessionId(@Nullable String securityScope) {
        UUID anonymousSessionId;
        if (securityScope == null || SecurityScope.DEFAULT_SCOPE_NAME.equals(securityScope)) {
            anonymousSessionId = globalConfig.getAnonymousSessionId();
        } else {
            String value = AppContext.getProperty(String.format("cuba.%s.anonymousSessionId", securityScope.toLowerCase()));
            anonymousSessionId = value == null ? null : UUID.fromString(value);
        }
        return anonymousSessionId;
    }

    @SuppressWarnings("RedundantThrows")
    protected UserSession createSession(@SuppressWarnings("unused") AnonymousUserCredentials credentials, User user,
                                        Locale userLocale, String securityScope) throws LoginException {
        return userSessionManager.createSession(user, userLocale, true, securityScope);
    }

    @SuppressWarnings("RedundantThrows")
    protected UserSession createSession(UUID sessionId, @SuppressWarnings("unused") AnonymousUserCredentials credentials, User user,
                                        Locale userLocale, String securityScope) throws LoginException {
        return userSessionManager.createSession(sessionId, user, userLocale, true, securityScope);
    }

    @Override
    public boolean supports(Class<?> credentialsClass) {
        return AnonymousUserCredentials.class.isAssignableFrom(credentialsClass);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 50;
    }
}