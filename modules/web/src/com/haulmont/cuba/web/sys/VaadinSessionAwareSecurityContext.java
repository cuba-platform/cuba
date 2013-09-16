/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.vaadin.server.VaadinSession;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

/**
 * {@link SecurityContext} which gets UserSession from VaadinSession <br/>
 * <b>Attention!</b> Copy to another threads not supported.
 *
 * @author artamonov
 * @version $Id$
 */
public class VaadinSessionAwareSecurityContext extends SecurityContext {

    private UserSession session;
    private String user;
    private UUID sessionId;

    public VaadinSessionAwareSecurityContext() {
        super(new UserSession(
                UUID.fromString("a66abe96-3b9d-11e2-9db2-3860770d7eaf"), new User(),
                Collections.<Role>emptyList(), Locale.getDefault(), true) {
            @Override
            public UUID getId() {
                return AppContext.NO_USER_CONTEXT.getSessionId();
            }
        });
    }

    @Override
    public UUID getSessionId() {
        if (session == null)
            internalGetSession();
        return sessionId;
    }

    @Nullable
    @Override
    public UserSession getSession() {
        if (session == null)
            internalGetSession();
        return session;
    }

    @Nullable
    @Override
    public String getUser() {
        if (session == null)
            internalGetSession();
        return user;
    }

    private void internalGetSession() {
        VaadinSession vSession = VaadinSession.getCurrent();
        if (vSession != null) {
            session = vSession.getAttribute(UserSession.class);
            if (session != null) {
                sessionId = session.getId();
                user = session.getUser().getLogin();
            }
        }
    }
}