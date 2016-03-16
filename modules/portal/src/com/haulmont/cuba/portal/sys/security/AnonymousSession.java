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

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Locale;

/**
 */
public class AnonymousSession extends PortalSession {

    public AnonymousSession(UserSession src, @Nullable Locale locale) {
        super(src);
        if (locale != null)
            this.locale = locale;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public void setAttribute(String name, Serializable value) {
        attributes.put(name, value);
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
    }
}