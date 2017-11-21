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

package com.haulmont.cuba.restapi;

import com.haulmont.cuba.security.global.UserSession;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;

public class RestUserSessionInfo implements Serializable {

    protected UUID id;

    protected Locale locale;

    public RestUserSessionInfo(UserSession userSession) {
        this.id = userSession.getId();
        this.locale = userSession.getLocale();
    }

    public RestUserSessionInfo(UUID id, Locale locale) {
        this.id = id;
        this.locale = locale;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
