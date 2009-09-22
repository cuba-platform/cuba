/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:18:40
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.app.Heartbeat;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;

import org.apache.commons.lang.text.StrBuilder;

/**
 * UserSessions MBean implementation.
 * <p>
 * Holds and controls the current user sessions list.
 */

public class UserSessions implements UserSessionsMBean, UserSessionsAPI, Heartbeat.Listener {
    private static class UserSessionInfo implements Serializable {
        private static final long serialVersionUID = -4834267718111570841L;

        private final UserSession session;
        private final long since;
        private volatile long lastUsedTs;

        private UserSessionInfo(UserSession session) {
            this.session = session;
            long now = TimeProvider.currentTimestamp().getTime();
            this.since = now;
            this.lastUsedTs = now;
        }

        public String toString() {
            return session + ", since: " + new Date(since) + ", lastUsed: " + new Date(lastUsedTs);
        }
    }

    private Map<UUID, UserSessionInfo> cache = new ConcurrentHashMap<UUID, UserSessionInfo>();

    private volatile int expirationTimeout = 1800;

    public UserSessions() {
        System.setProperty("cuba.logUserName", "true");
        Heartbeat.getInstance().addListener(this, 10);
    }

    public void add(UserSession session) {
        cache.put(session.getId(), new UserSessionInfo(session));
    }

    public void remove(UserSession session) {
        cache.remove(session.getId());
    }

    public UserSession get(UUID id) {
        UserSessionInfo info = cache.get(id);
        if (info != null) {
            info.lastUsedTs = TimeProvider.currentTimestamp().getTime();
            return info.session;
        }
        return null;
    }

    public UserSessionsAPI getAPI() {
        return this;
    }

    public int getExpirationTimeoutSec() {
        return expirationTimeout;
    }

    public void setExpirationTimeoutSec(int value) {
        expirationTimeout = value;
    }

    public int getCount() {
        return cache.size();
    }

    public String printSessions() {
        StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators(cache.values(), "\n");
        return sb.toString();
    }

    public Collection<UserSessionEntity> getUserSessionInfo() {
        ArrayList<UserSessionEntity> sessionInfoList = new ArrayList<UserSessionEntity>();
        for (UserSessionInfo nfo : cache.values()) {
            UserSessionEntity userSession = new UserSessionEntity();
            userSession.setId(nfo.session.getId());
            userSession.setLogin(nfo.session.getUser().getLogin());
            userSession.setUserName(nfo.session.getUser().getName());
            Date since = TimeProvider.currentTimestamp();
            since.setTime(nfo.since);
            userSession.setSince(since);
            Date last = TimeProvider.currentTimestamp();
            last.setTime(nfo.lastUsedTs);
            userSession.setLastUsedTs(last);
            sessionInfoList.add(userSession);
        }
        return sessionInfoList;
    }
    public void killSession(UUID id){
        cache.remove(id);
    }
    public void processEviction() {
        long now = TimeProvider.currentTimestamp().getTime();
        for (Iterator<UserSessionInfo> it = cache.values().iterator(); it.hasNext();) {
            UserSessionInfo info = it.next();
            if (now > (info.lastUsedTs + expirationTimeout * 1000)) {
                it.remove();
            }
        }
    }

    public void beat() {
        processEviction();
    }
}
