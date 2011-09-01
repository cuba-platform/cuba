/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.12.2009 16:58:36
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class SecurityContext {

    private final UUID sessionId;
    private UserSession session;
    private String user;
    private String password;

    public SecurityContext(UUID sessionId) {
        if (sessionId == null)
            throw new IllegalArgumentException("sessionId is null");
        this.sessionId = sessionId;
    }

    public SecurityContext(UUID sessionId, String user) {
        this(sessionId);
        this.user = user;
    }

    public SecurityContext(UserSession session) {
        if (session == null)
            throw new IllegalArgumentException("session is null");
        this.session = session;
        this.sessionId = session.getId();
        this.user = session.getUser().getLogin();
        this.password = "md5:" + session.getUser().getPassword();
    }

    @Nonnull
    public UUID getSessionId() {
        if (BooleanUtils.toBoolean(System.getProperty("cuba.unitTestMode")))
            return UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

        return sessionId;
    }

    @Nullable
    public UserSession getSession() {
        return session;
    }

    @Nullable
    public String getUser() {
        return user;
    }

    @Nullable
    public String getPassword() {
        return password;
    }
}
