/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.PermissionType;
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

/**
 * Service facade to active {@link UserSession}s management.
 *
 * @author krivopustov
 * @version $Id$
 */
@Service(UserSessionService.NAME)
public class UserSessionServiceBean implements UserSessionService {
    public static final String MESSAGE_ATTR_PREFIX = "message-";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private UserSessionManager userSessionManager;

    @Inject
    private UserSessionsAPI userSessions;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private TimeSource timeSource;

    @Override
    public UserSession getUserSession(UUID sessionId) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        return userSession;
    }

    @Override
    public void setSessionAttribute(UUID sessionId, String name, Serializable value) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        userSession.setAttribute(name, value);
        userSessions.propagate(sessionId);
    }

    @Override
    public void setSessionLocale(UUID sessionId, Locale locale) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        userSession.setLocale(locale);
        userSessions.propagate(sessionId);
    }

    @Override
    public void setSessionTimeZone(UUID sessionId, TimeZone timeZone) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        userSession.setTimeZone(timeZone);
        userSessions.propagate(sessionId);
    }

    @Override
    public void setSessionAddress(UUID sessionId, String address) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        userSession.setAddress(address);
        userSessions.propagate(sessionId);
    }

    @Override
    public void setSessionClientInfo(UUID sessionId, String clientInfo) {
        UserSession userSession = userSessionManager.getSession(sessionId);
        userSession.setClientInfo(clientInfo);
        userSessions.propagate(sessionId);
    }

    @Override
    public Collection<UserSessionEntity> getUserSessionInfo() {
        return userSessions.getUserSessionInfo();
    }

    @Override
    public final Map<String, Object> getLicenseInfo() {
        return userSessions.getLicenseInfo();
    }

    @Override
    public void killSession(UUID id) {
        userSessions.killSession(id);
    }

    @Override
    public void postMessage(List<UUID> sessionIds, String message) {
        long time = timeSource.currentTimeMillis();
        for (UUID sessionId : sessionIds) {
            UserSession userSession = userSessionManager.findSession(sessionId);
            if (userSession != null) {
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
                    Date dateTime = new Date(Long.valueOf(dateTimeMillis));

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