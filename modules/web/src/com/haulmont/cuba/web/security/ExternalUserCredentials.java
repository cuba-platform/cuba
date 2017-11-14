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

package com.haulmont.cuba.web.security;

import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.global.UserSession;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class ExternalUserCredentials extends AbstractClientCredentials {

    public static final String EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE = "LOGGED_IN_WITH_EXTERNAL_AUTHENTICATION";

    private String login;

    public ExternalUserCredentials(String login) {
        this.login = login;
    }

    public ExternalUserCredentials(String login, Locale locale) {
        super(locale, Collections.emptyMap());
        this.login = login;
    }

    public ExternalUserCredentials(String login, Locale locale, Map<String, Object> params) {
        super(locale, params);
        this.login = login;
    }

    @Override
    public String getUserIdentifier() {
        return login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public static boolean isLoggedInWithExternalAuth(UserSession userSession) {
        return userSession.getAttribute(EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE) != null;
    }

    @Override
    public String toString() {
        return "ExternalUserCredentials{" +
                "login='" + login + '\'' +
                '}';
    }
}