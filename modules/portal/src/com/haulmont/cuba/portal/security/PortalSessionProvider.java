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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;

import java.util.Locale;

/**
 * Provides access to the current user session in static context.<br>
 * <p>Injected {@link UserSessionSource} interface should be used instead of this class wherever possible.</p>
 *
 */
public abstract class PortalSessionProvider {

    private static UserSessionSource getSource() {
        return AppBeans.get(UserSessionSource.NAME, UserSessionSource.class);
    }

    /**
     * Current portal session
     *
     * @return current user session instance
     */
    public static PortalSession getUserSession() {
        return (PortalSession) getSource().getUserSession();
    }

    public static Locale getLocale() {
        return getSource().getLocale();
    }
}
