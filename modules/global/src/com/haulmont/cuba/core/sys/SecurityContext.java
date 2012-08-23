/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

/**
 * Holds information about the current user session.
 *
 * <p/>Instances of this class are normally being set in {@link AppContext} by the framework, but also have to be
 * passed to it in case of manually running new threads. Here is the sample code for an asynchronous execution:
 * <pre>
 *     final SecurityContext securityContext = AppContext.getSecurityContext();
 *     executor.submit(new Runnable() {
 *         public void run() {
 *             AppContext.setSecurityContext(securityContext);
 *             // business logic here
 *         }
 *     });
 * </pre>
 *
 * @author krivopustov
 * @version $Id$
 */
public class SecurityContext {

    private final UUID sessionId;
    private UserSession session;
    private String user;

    public SecurityContext(UUID sessionId) {
        Objects.requireNonNull(sessionId, "sessionId is null");
        this.sessionId = sessionId;
    }

    public SecurityContext(UUID sessionId, String user) {
        this(sessionId);
        this.user = user;
    }

    public SecurityContext(UserSession session) {
        Objects.requireNonNull(session, "session is null");
        this.session = session;
        this.sessionId = session.getId();
        this.user = session.getUser().getLogin();
    }

    /**
     * @return Current {@link UserSession} ID. This is the only required value for the {@link SecurityContext}.
     */
    public UUID getSessionId() {
        if (BooleanUtils.toBoolean(System.getProperty("cuba.unitTestMode")))
            return UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

        return sessionId;
    }

    /**
     * @return current user session. Can be null, so don't rely on this method in application code - use
     * {@link com.haulmont.cuba.core.global.UserSessionSource} or {@link com.haulmont.cuba.core.global.UserSessionProvider}
     */
    @Nullable
    public UserSession getSession() {
        return session;
    }

    /**
     * @return current user login. Can be null, so don't rely on this method in application code - use
     * {@link com.haulmont.cuba.core.global.UserSessionSource} or {@link com.haulmont.cuba.core.global.UserSessionProvider}
     */
    @Nullable
    public String getUser() {
        return user;
    }
}
