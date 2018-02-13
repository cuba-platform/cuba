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
package com.haulmont.cuba.security.app;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.SessionAction;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service facade to active {@link UserSession}s management.
 */
@Service(UserSessionService.NAME)
public class UserSessionServiceBean implements UserSessionService {
    public static final String MESSAGE_ATTR_PREFIX = "message-";

    private static final Logger log = LoggerFactory.getLogger(UserSessionServiceBean.class);

    @Inject
    private UserSessionManager userSessionManager;

    @Inject
    private UserSessionsAPI userSessions;

    @Inject
    private UserSessionLog userSessionLog;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private TimeSource timeSource;

    protected void checkSession(UserSession userSession) {
        if (userSession.isSystem())
            throw new RuntimeException("Access to system session " + userSession.getId() + " is not allowed");
    }

    @Override
    public UserSession getUserSession(UUID sessionId) {
        UserSession userSession = userSessions.getAndRefreshNN(sessionId);
        checkSession(userSession);
        return userSession;
    }

    @Override
    public void setSessionAttribute(UUID sessionId, String name, Serializable value) {
        UserSession userSession = userSessions.getNN(sessionId);
        checkSession(userSession);
        userSession.setAttribute(name, value);
        userSessions.propagate(sessionId);
    }

    @Override
    public void removeSessionAttribute(UUID sessionId, String name) {
        UserSession userSession = userSessions.getNN(sessionId);
        checkSession(userSession);
        userSession.removeAttribute(name);
        userSessions.propagate(sessionId);
    }

    @Override
    public void setSessionLocale(UUID sessionId, Locale locale) {
        UserSession userSession = userSessions.getNN(sessionId);
        checkSession(userSession);
        userSession.setLocale(locale);
        userSessions.propagate(sessionId);
    }

    @Override
    public void setSessionTimeZone(UUID sessionId, TimeZone timeZone) {
        UserSession userSession = userSessions.getNN(sessionId);
        checkSession(userSession);
        userSession.setTimeZone(timeZone);
        userSessions.propagate(sessionId);
    }

    @Override
    public void setSessionAddress(UUID sessionId, String address) {
        UserSession userSession = userSessions.getNN(sessionId);
        checkSession(userSession);
        userSession.setAddress(address);
        userSessions.propagate(sessionId);
    }

    @Override
    public void setSessionClientInfo(UUID sessionId, String clientInfo) {
        UserSession userSession = userSessions.getNN(sessionId);
        checkSession(userSession);
        userSession.setClientInfo(clientInfo);
        userSessions.propagate(sessionId);
    }

    @Override
    public Collection<UserSessionEntity> getUserSessionInfo() {
        return userSessions.getUserSessionInfo();
    }

    @Override
    public Collection<UserSessionEntity> loadUserSessionEntities(Filter filter) {
        Preconditions.checkNotNullArgument(filter, "filter is null");

        return userSessions.getUserSessionEntitiesStream()
                .filter(e -> {
                    if (filter == Filter.ALL)
                        return true;
                    boolean result = true;
                    if (!Strings.isNullOrEmpty(filter.getUserLogin())) {
                        result = testString(e.getLogin(), filter.getUserLogin(), filter.isStrict());
                    }
                    if (!Strings.isNullOrEmpty(filter.getUserName())) {
                        result = result && testString(e.getUserName(), filter.getUserName(), filter.isStrict());
                    }
                    if (!Strings.isNullOrEmpty(filter.getAddress())) {
                        result = result && testString(e.getAddress(), filter.getAddress(), filter.isStrict());
                    }
                    if (!Strings.isNullOrEmpty(filter.getClientInfo())) {
                        result = result && testString(e.getClientInfo(), filter.getClientInfo(), filter.isStrict());
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    protected boolean testString(String value, String test, boolean strict) {
        return value != null
                && (strict ? value.equals(test) : value.toLowerCase().contains(test.toLowerCase()));
    }

    @Override
    public void killSession(UUID id) {
        UserSession userSession = userSessions.get(id);
        if (userSession != null && !userSession.isSystem()) {
            userSessionLog.updateSessionLogRecord(userSession, SessionAction.TERMINATION);
            userSessions.killSession(id);
        }
    }

    @Override
    public void postMessage(List<UUID> sessionIds, String message) {
        long time = timeSource.currentTimeMillis();
        for (UUID sessionId : sessionIds) {
            UserSession userSession = userSessions.get(sessionId);
            if (userSession != null && !userSession.isSystem()) {
                userSession.setAttribute(MESSAGE_ATTR_PREFIX + time, message);
                userSessions.propagate(sessionId);
            }
        }
    }

    @Override
    @Nullable
    public String getMessages() {
        UserSession userSession = userSessionSource.getUserSession();
        try {
            Map<String, String> messages = new TreeMap<>();
            for (String name : userSession.getAttributeNames()) {
                if (name.startsWith(MESSAGE_ATTR_PREFIX)) {
                    Object message = userSession.getAttribute(name);
                    if (message instanceof String)
                        messages.put(name, (String) message);
                }
            }
            if (!messages.isEmpty()) {
                Datatype<Date> datatype = Datatypes.getNN(Date.class);
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : messages.entrySet()) {
                    if (sb.length() != 0)
                        sb.append("\n\n");

                    String name = entry.getKey();
                    String dateTimeMillis = name.substring(MESSAGE_ATTR_PREFIX.length());
                    Date dateTime = new Date(Long.parseLong(dateTimeMillis));

                    sb.append(datatype.format(dateTime, userSession.getLocale())).append("\n");
                    sb.append(entry.getValue());

                    userSession.removeAttribute(name);
                }
                userSessions.propagate(userSession.getId());
                return sb.toString();
            }
        } catch (Throwable e) {
            log.warn("Error getting messages for session " + userSession, e);
        }
        return null;
    }

    @Override
    public Integer getPermissionValue(User user, PermissionType permissionType, String target) {
        return userSessionManager.getPermissionValue(user, permissionType, target);
    }

}