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

package com.haulmont.cuba.portal.security;

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.security.global.UserSession;

/**
 * User session that holds middleware session.
 *
 */
public class PortalSession extends ClientUserSession {
    private static final long serialVersionUID = 64089583666599524L;

    private volatile boolean authenticated; // indicates whether user passed authentication

    public PortalSession(UserSession src) {
        super(src);
        authenticated = false;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
