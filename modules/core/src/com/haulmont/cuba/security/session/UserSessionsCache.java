/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:18:40
 *
 * $Id$
 */
package com.haulmont.cuba.security.session;

import com.haulmont.cuba.security.global.UserSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// TODO KK: implement as MBean with cluster support
public class UserSessionsCache implements UserSessions
{
    private Map<UUID, UserSession> cache = new ConcurrentHashMap<UUID, UserSession>();

    public void add(UserSession session) {
        cache.put(session.getId(), session);
    }

    public void remove(UserSession session) {
        cache.remove(session.getId());
    }

    public UserSession get(UUID id) {
        return cache.get(id);
    }
}
