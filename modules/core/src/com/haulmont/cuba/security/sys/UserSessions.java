/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:18:40
 *
 * $Id: UserSessions.java 517 2009-07-08 12:31:10Z krivopustov $
 */
package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.app.Heartbeat;

import java.util.Map;
import java.util.UUID;
import java.util.Iterator;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;

import org.apache.commons.lang.text.StrBuilder;

public class UserSessions implements UserSessionsMBean, UserSessionsAPI, Heartbeat.Listener
{
    private static class UserSessionInfo implements Serializable {
        private static final long serialVersionUID = -4834267718111570841L;

        private final UserSession session;
        private final long since;
        private long lastUsedTs;

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
