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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.entity.RememberMeToken;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

@Component("cuba_RememberMeAuthenticationProvider")
public class RememberMeAuthenticationProvider extends AbstractAuthenticationProvider implements Ordered {
    @Inject
    protected List<UserAccessChecker> userAccessCheckers;
    @Inject
    protected List<UserCredentialsChecker> userCredentialsCheckers;
    @Inject
    protected UserSessionManager userSessionManager;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    public RememberMeAuthenticationProvider(Persistence persistence, Messages messages) {
        super(persistence, messages);
    }

    @Override
    public AuthenticationDetails authenticate(Credentials credentials) throws LoginException {
        RememberMeCredentials rememberMe = (RememberMeCredentials) credentials;

        String login = rememberMe.getLogin();

        Locale credentialsLocale = rememberMe.getLocale() == null ?
                messages.getTools().getDefaultLocale() : rememberMe.getLocale();

        if (Strings.isNullOrEmpty(login)) {
            // empty login is not valid
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        checkUserCredentials(credentials);

        User user = loadUser(login);
        if (user == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        RememberMeToken loginToken = loadRememberMeToken(user, rememberMe.getRememberMeToken());
        if (loginToken == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        if (isTokenExpired(loginToken)) {
            throw new LoginException(getExpiredRememberMeTokenMessage(login, credentialsLocale));
        }

        Locale userLocale = getUserLocale(rememberMe, user);

        UserSession session = createSession(rememberMe, user, userLocale);

        setClientSessionParams(rememberMe, session);

        AuthenticationDetails authenticationDetails = new SimpleAuthenticationDetails(session);

        checkUserAccess(rememberMe, authenticationDetails);

        return authenticationDetails;
    }

    @SuppressWarnings("RedundantThrows")
    protected UserSession createSession(@SuppressWarnings("unused") RememberMeCredentials credentials,
                                        User user, Locale userLocale) throws LoginException {
        return userSessionManager.createSession(user, userLocale, false, credentials.getSecurityScope());
    }

    protected void checkUserCredentials(Credentials credentials) throws LoginException {
        if (userCredentialsCheckers != null) {
            for (UserCredentialsChecker checker : userCredentialsCheckers) {
                checker.check(credentials);
            }
        }
    }

    protected void checkUserAccess(Credentials loginAndPassword, AuthenticationDetails authenticationDetails)
            throws LoginException {
        if (userAccessCheckers != null) {
            for (UserAccessChecker checker : userAccessCheckers) {
                checker.check(loginAndPassword, authenticationDetails);
            }
        }
    }

    @Nullable
    protected RememberMeToken loadRememberMeToken(User user, String rememberMeToken) {
        EntityManager em = persistence.getEntityManager();
        TypedQuery<RememberMeToken> query = em.createQuery(
                "select rt from sec$RememberMeToken rt where rt.token = :token and rt.user.id = :userId",
                RememberMeToken.class);
        query.setParameter("token", rememberMeToken);
        query.setParameter("userId", user.getId());

        return query.getFirstResult();
    }

    protected boolean isTokenExpired(RememberMeToken rememberMeToken) {
        if (rememberMeToken.getCreateTs() == null) {
            return true;
        }

        long tokenCreated = rememberMeToken.getCreateTs().getTime();
        long now = timeSource.currentTimeMillis();
        int expirationTimeout = globalConfig.getRememberMeExpirationTimeoutSec();

        return tokenCreated + expirationTimeout < now;
    }

    @Override
    public boolean supports(Class<?> credentialsClass) {
        return RememberMeCredentials.class.isAssignableFrom(credentialsClass);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 20;
    }
}