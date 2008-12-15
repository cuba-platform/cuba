/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 12:09:22
 *
 * $Id$
 */
package com.haulmont.cuba.security.impl;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.ProfileRole;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.NoUserSessionException;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class UserSessionManager
{
    private static UserSessionManager instance;

    private UserSessions sessions;

    public static UserSessionManager getInstance() {
        if (instance == null) {
            instance = new UserSessionManager();
        }
        return instance;
    }

    public UserSessionManager() {
        sessions = new UserSessionsCache();
    }

    public UserSession createSession(User user, Profile profile, Locale locale) {
        List<String> roles = new ArrayList<String>();
        for (ProfileRole profileRole : profile.getProfileRoles()) {
            roles.add(profileRole.getRole().getName());
        }
        UserSession session = new UserSession(user, roles.toArray(new String[roles.size()]), locale);
        sessions.add(session);
        return session;
    }

    public void removeSession(UserSession session) {
        sessions.remove(session);
    }

    public UserSession getSession(UUID sessionId) {
        UserSession session = findSession(sessionId);
        if (session == null) {
            throw new NoUserSessionException(sessionId);
        }
        return session;
    }

    public UserSession findSession(UUID sessionId) {
        return sessions.get(sessionId);
    }
}
