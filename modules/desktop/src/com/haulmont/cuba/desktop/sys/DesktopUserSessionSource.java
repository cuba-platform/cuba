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

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AbstractUserSessionSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.security.global.UserSession;

import org.springframework.stereotype.Component;

/**
 */
@Component(UserSessionSource.NAME)
public class DesktopUserSessionSource extends AbstractUserSessionSource {

    @Override
    public boolean checkCurrentUserSession() {
        return App.getInstance().getConnection().isConnected() && App.getInstance().getConnection().getSession() != null;
    }

    @Override
    public UserSession getUserSession() {
        UserSession session = App.getInstance().getConnection().getSession();
        if (session == null)
            throw new IllegalStateException("No user session");
        return session;
    }
}
