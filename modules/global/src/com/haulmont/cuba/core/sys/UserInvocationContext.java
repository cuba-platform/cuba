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
import java.util.TimeZone;
import java.util.UUID;

/**
 * Parameters of user invocation, may be passed by client tier
 */
public final class UserInvocationContext {
    private static final ThreadLocal<RequestScopeUserInfo> userRequestScopeInfo = new ThreadLocal<>();

    public static void setRequestScopeInfo(UUID sessionId, Locale locale, TimeZone timeZone, String address,
                                           String clientInfo) {
        userRequestScopeInfo.set(new RequestScopeUserInfo(sessionId, locale, timeZone, address, clientInfo));
    }

    @Nullable
    public static Locale getRequestScopeLocale(UUID sessionId) {
        RequestScopeUserInfo requestScopeInfo = userRequestScopeInfo.get();
        if (requestScopeInfo != null && Objects.equals(sessionId, requestScopeInfo.getSessionId())) {
            return requestScopeInfo.getLocale();
        }

        return null;
    }

    @Nullable
    public static TimeZone getRequestScopeTimeZone(UUID sessionId) {
        RequestScopeUserInfo requestScopeInfo = userRequestScopeInfo.get();
        if (requestScopeInfo != null && Objects.equals(sessionId, requestScopeInfo.getSessionId())) {
            return requestScopeInfo.getTimeZone();
        }

        return null;
    }

    @Nullable
    public static String getRequestScopeAddress(UUID sessionId) {
        RequestScopeUserInfo requestScopeInfo = userRequestScopeInfo.get();
        if (requestScopeInfo != null && Objects.equals(sessionId, requestScopeInfo.getSessionId())) {
            return requestScopeInfo.getAddress();
        }

        return null;
    }

    @Nullable
    public static String getRequestScopeClientInfo(UUID sessionId) {
        RequestScopeUserInfo requestScopeInfo = userRequestScopeInfo.get();
        if (requestScopeInfo != null && Objects.equals(sessionId, requestScopeInfo.getSessionId())) {
            return requestScopeInfo.getClientInfo();
        }

        return null;
    }

    public static void clearRequestScopeInfo() {
        userRequestScopeInfo.set(null);
    }

    protected static final class RequestScopeUserInfo {
        private final UUID sessionId;
        private final Locale locale;
        private final TimeZone timeZone;
        private final String address;
        private final String clientInfo;

        public RequestScopeUserInfo(UUID sessionId, Locale locale, TimeZone timeZone, String address, String clientInfo) {
            this.sessionId = sessionId;
            this.locale = locale;
            this.timeZone = timeZone;
            this.address = address;
            this.clientInfo = clientInfo;
        }

        public UUID getSessionId() {
            return sessionId;
        }

        public Locale getLocale() {
            return locale;
        }

        public TimeZone getTimeZone() {
            return timeZone;
        }

        public String getAddress() {
            return address;
        }

        public String getClientInfo() {
            return clientInfo;
        }
    }
}