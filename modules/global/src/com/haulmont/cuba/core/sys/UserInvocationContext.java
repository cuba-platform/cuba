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
 */

package com.haulmont.cuba.core.sys;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Parameters of user invocation, may be passed by client tier
 */
public final class UserInvocationContext {
    private static final ThreadLocal<RequestScopeLocale> userRequestScopeLocale = new ThreadLocal<>();

    public static void setRequestScopeLocale(UUID sessionId, Locale locale) {
        userRequestScopeLocale.set(new RequestScopeLocale(sessionId, locale));
    }

    @Nullable
    public static Locale getRequestScopeLocale(UUID sessionId) {
        RequestScopeLocale requestScopeLocale = userRequestScopeLocale.get();
        if (requestScopeLocale != null && Objects.equals(sessionId, requestScopeLocale.getSessionId())) {
            return requestScopeLocale.getLocale();
        }

        return null;
    }

    public static void clearRequestScopeLocale() {
        userRequestScopeLocale.set(null);
    }

    protected static final class RequestScopeLocale {
        private final UUID sessionId;
        private final Locale locale;

        public RequestScopeLocale(UUID sessionId, Locale locale) {
            this.sessionId = sessionId;
            this.locale = locale;
        }

        public UUID getSessionId() {
            return sessionId;
        }

        public Locale getLocale() {
            return locale;
        }
    }
}