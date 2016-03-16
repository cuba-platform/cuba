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

import com.haulmont.cuba.security.global.LoginException;

import java.util.Locale;

/**
 * Interface to be implemented by middleware connection objects supporting external authentication.
 *
 */
public interface ExternallyAuthenticatedConnection {

    String EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE = "LOGGED_IN_WITH_EXTERNAL_AUTHENTICATION";

    /**
     * Log in to the system after external authentication.
     * @param login             user login name
     * @param locale            user locale
     * @throws LoginException   in case of unsuccessful login due to wrong credentials or other issues
     */
    void loginAfterExternalAuthentication(String login, Locale locale) throws LoginException;
}