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
import com.haulmont.cuba.security.auth.AnonymousUserCredentials;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.SimpleAuthenticationDetails;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

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

        Locale userLocale = getUserLocale(anonymous, user);

        UUID anonymousSessionId = globalConfig.getAnonymousSessionId();

        UserSession session = createSession(anonymous, user, userLocale, anonymousSessionId);
        session.setClientInfo("System anonymous session");

        return new SimpleAuthenticationDetails(session);
    }

    @SuppressWarnings("RedundantThrows")
    protected UserSession createSession(@SuppressWarnings("unused") AnonymousUserCredentials credentials, User user,
                                        Locale userLocale, UUID anonymousSessionId) throws LoginException {
        return userSessionManager.createSession(anonymousSessionId, user, userLocale, true);
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