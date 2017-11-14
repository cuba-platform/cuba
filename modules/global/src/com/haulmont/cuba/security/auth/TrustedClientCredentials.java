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
 * Credentials for user that can be authenticated using trusted client password.
 */
public class TrustedClientCredentials extends AbstractClientCredentials {

    private static final long serialVersionUID = -2005955029379343063L;

    private String login;
    private String trustedClientPassword;

    private String clientIpAddress;

    public TrustedClientCredentials() {
    }

    public TrustedClientCredentials(String login, String trustedClientPassword, Locale locale) {
        this(login, trustedClientPassword, locale, Collections.emptyMap());
    }

    public TrustedClientCredentials(String login, String trustedClientPassword,
                                    Locale locale, Map<String, Object> params) {
        super(locale, params);

        this.login = login;
        this.trustedClientPassword = trustedClientPassword;
    }

    public String getTrustedClientPassword() {
        return trustedClientPassword;
    }

    public void setTrustedClientPassword(String trustedClientPassword) {
        this.trustedClientPassword = trustedClientPassword;
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

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    @Override
    public String toString() {
        return "TrustedClientCredentials{" +
                "login='" + login + '\'' +
                '}';
    }
}