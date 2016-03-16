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

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Client-side extension of {@link UserSession}.
 *
 * <p>Sends updates of the user session properties to the middleware.</p>
 *
 */
public class ClientUserSession extends UserSession {

    private static final long serialVersionUID = -5358664165808633540L;

    public ClientUserSession(UserSession src) {
        super(src);
    }

    @Override
    public void setAttribute(String name, Serializable value) {
        super.setAttribute(name, value);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionAttribute(id, name, value);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionLocale(id, locale);
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        super.setTimeZone(timeZone);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionTimeZone(id, timeZone);
    }

    @Override
    public void setAddress(String address) {
        super.setAddress(address);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionAddress(id, address);
    }

    @Override
    public void setClientInfo(String clientInfo) {
        super.setClientInfo(clientInfo);
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        uss.setSessionClientInfo(id, clientInfo);
    }
}
