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
 * Credentials object for system-level mechanisms.
 */
public class SystemUserCredentials extends AbstractCredentials {

    private static final long serialVersionUID = 326392946916254748L;

    private String login;

    public SystemUserCredentials() {
    }

    public SystemUserCredentials(String login) {
        this(login, null);
    }

    public SystemUserCredentials(String login, Locale locale) {
        this(login, locale, Collections.emptyMap());
    }

    public SystemUserCredentials(String login, Locale locale, Map<String, Object> params) {
        super(locale, params);
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}