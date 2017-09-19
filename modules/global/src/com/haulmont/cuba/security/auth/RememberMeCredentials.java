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

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Credentials with remember me token.
 */
public class RememberMeCredentials extends AbstractClientCredentials {

    private static final long serialVersionUID = -8040672847169347648L;

    private String login;
    private String rememberMeToken;

    public RememberMeCredentials() {
    }

    public RememberMeCredentials(String login, String rememberMeToken) {
        this(login, rememberMeToken, null);
    }

    public RememberMeCredentials(String login, String rememberMeToken, Locale locale) {
        this(login, rememberMeToken, locale, Collections.emptyMap());
    }

    public RememberMeCredentials(String login, String rememberMeToken, Locale locale, Map<String, Object> params) {
        super(locale, params);
        this.login = login;
        this.rememberMeToken = rememberMeToken;
    }

    public String getRememberMeToken() {
        return rememberMeToken;
    }

    public void setRememberMeToken(String rememberMeToken) {
        this.rememberMeToken = rememberMeToken;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getUserIdentifier() {
        return getLogin();
    }

    @Override
    public String toString() {
        return "RememberMeCredentials{" +
                "login='" + login + '\'' +
                '}';
    }
}