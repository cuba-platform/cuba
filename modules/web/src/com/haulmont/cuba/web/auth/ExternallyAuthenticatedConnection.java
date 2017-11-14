/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.security.ExternalUserCredentials;
import com.haulmont.cuba.web.security.events.AppLoggedOutEvent;

import java.util.Locale;

/**
 * Interface to be implemented by middleware connection objects supporting external authentication.
 */
public interface ExternallyAuthenticatedConnection {

    /**
     * Log in to the system after external authentication.
     *
     * @deprecated Use {@link Connection#login(Credentials)} with {@link ExternalUserCredentials}.
     *
     * @param login             user login name
     * @param locale            user locale
     * @throws LoginException   in case of unsuccessful login due to wrong credentials or other issues
     */
    @Deprecated
    default void loginAfterExternalAuthentication(String login, Locale locale) throws LoginException {
        ((Connection) this).login(new ExternalUserCredentials(login, locale));
    }

    /**
     * Logout from external authentication.
     *
     * @deprecated Use event handler for {@link AppLoggedOutEvent}.
     *
     * @return target url of external identity provider or null.
     */
    @Deprecated
    default String logoutExternalAuthentication() {
        return null; // do nothing
    }
}