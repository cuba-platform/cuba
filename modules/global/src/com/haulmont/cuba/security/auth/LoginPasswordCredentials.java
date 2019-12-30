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
 * Simple login and password credentials.
 */
public class LoginPasswordCredentials extends AbstractClientCredentials {

    private static final long serialVersionUID = 348348249386685775L;

    private String login;
    private String password;

    public LoginPasswordCredentials() {
    }

    public LoginPasswordCredentials(String login, String password) {
        this(login, password, null);
    }

    public LoginPasswordCredentials(String login, String password, Locale locale) {
        this(login, password, locale, Collections.emptyMap());
    }

    public LoginPasswordCredentials(String login, String password, Locale locale, Map<String, Object> params) {
        super(locale, params);
        this.login = login;
        this.password = password;
    }

    @Override
    public String getUserIdentifier() {
        return getLogin();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginPasswordCredentials{" +
                "login='" + login + '\'' +
                '}';
    }
}