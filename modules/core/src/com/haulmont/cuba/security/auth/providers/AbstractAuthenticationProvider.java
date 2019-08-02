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

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.AuthenticationProvider;
import com.haulmont.cuba.security.auth.LocalizedCredentials;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(AbstractAuthenticationProvider.class);

    protected static final String MSG_PACK = "com.haulmont.cuba.security";

    protected Persistence persistence;
    protected Messages messages;

    public AbstractAuthenticationProvider(Persistence persistence, Messages messages) {
        this.persistence = persistence;
        this.messages = messages;
    }

    @Nullable
    protected User loadUser(String login) throws LoginException {
        if (login == null) {
            throw new IllegalArgumentException("Login is null");
        }

        EntityManager em = persistence.getEntityManager();
        String queryStr = "select u from sec$User u where u.loginLowerCase = ?1 and (u.active = true or u.active is null)";

        Query q = em.createQuery(queryStr);
        q.setParameter(1, login.toLowerCase());

        List list = q.getResultList();
        if (list.isEmpty()) {
            log.debug("Unable to find user: {}", login);
            return null;
        } else {
            //noinspection UnnecessaryLocalVariable
            User user = (User) list.get(0);
            return user;
        }
    }

    protected String getInvalidCredentialsMessage(String login, Locale locale) {
        return messages.formatMessage(MSG_PACK, "LoginException.InvalidLoginOrPassword", locale, login);
    }

    protected String getExpiredRememberMeTokenMessage(String login, Locale locale) {
        return messages.formatMessage(MSG_PACK, "LoginException.rememberMeTokenExpired", locale, login);
    }

    protected Locale getUserLocale(LocalizedCredentials credentials, User user) {
        Locale userLocale = null;
        if (credentials.isOverrideLocale()) {
            userLocale = credentials.getLocale();
        }
        if (userLocale == null) {
            if (user.getLanguage() != null) {
                userLocale = LocaleUtils.toLocale(user.getLanguage());
            } else {
                userLocale = messages.getTools().trimLocale(messages.getTools().getDefaultLocale());
            }
        }

        return userLocale;
    }

    protected void setClientSessionParams(AbstractClientCredentials clientCredentials, UserSession userSession) {
        userSession.setClientInfo(clientCredentials.getClientInfo());

        if (clientCredentials.getHostName() != null) {
            StringBuilder addressBuilder = new StringBuilder();
            addressBuilder.append(clientCredentials.getHostName());
            if (clientCredentials.getIpAddress() != null) {
                addressBuilder.append(" (").append(clientCredentials.getIpAddress()).append(")");
            }
            userSession.setAddress(addressBuilder.toString());
        } else {
            userSession.setAddress(clientCredentials.getIpAddress());
        }
    }
}