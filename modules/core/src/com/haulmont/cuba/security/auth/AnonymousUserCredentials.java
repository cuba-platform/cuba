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

import com.haulmont.cuba.core.app.ServerConfig;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Credentials object for anonymous session.
 *
 * @see ServerConfig#getAnonymousLogin()
 */
public class AnonymousUserCredentials extends AbstractCredentials {
    private static final long serialVersionUID = 3137392403475947L;

    public AnonymousUserCredentials() {
    }

    public AnonymousUserCredentials(Locale locale) {
        super(locale, Collections.emptyMap());
    }

    public AnonymousUserCredentials(Locale locale, Map<String, Object> params) {
        super(locale, params);
    }

    @Override
    public String toString() {
        return "AnonymousUserCredentials{}";
    }
}