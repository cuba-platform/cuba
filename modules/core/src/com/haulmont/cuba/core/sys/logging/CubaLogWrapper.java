/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.08.2009 15:44:33
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.logging;

import org.apache.commons.logging.Log;

import java.util.UUID;

import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;

public class CubaLogWrapper implements Log {

    private final Log delegate;

    public CubaLogWrapper(Log delegate) {
        this.delegate = delegate;
    }

    private String getCurrentUser() {
        UUID sessionId = ServerSecurityUtils.getSessionId();
        if (sessionId != null) {
            UserSession session = UserSessionManager.getInstance().findSession(sessionId);
            if (session != null) {
                return "[" + session.getUser().getLogin() + "] ";
            }
        }
        return "";
    }

    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return delegate.isFatalEnabled();
    }

    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    public void trace(Object o) {
        delegate.trace(getCurrentUser() + o);
    }

    public void trace(Object o, Throwable throwable) {
        delegate.trace(getCurrentUser() + o, throwable);
    }

    public void debug(Object o) {
        delegate.debug(getCurrentUser() + o);
    }

    public void debug(Object o, Throwable throwable) {
        delegate.debug(getCurrentUser() + o, throwable);
    }

    public void info(Object o) {
        delegate.info(getCurrentUser() + o);
    }

    public void info(Object o, Throwable throwable) {
        delegate.info(getCurrentUser() + o, throwable);
    }

    public void warn(Object o) {
        delegate.warn(getCurrentUser() + o);
    }

    public void warn(Object o, Throwable throwable) {
        delegate.warn(getCurrentUser() + o, throwable);
    }

    public void error(Object o) {
        delegate.error(getCurrentUser() + o);
    }

    public void error(Object o, Throwable throwable) {
        delegate.error(getCurrentUser() + o, throwable);
    }

    public void fatal(Object o) {
        delegate.fatal(getCurrentUser() + o);
    }

    public void fatal(Object o, Throwable throwable) {
        delegate.fatal(getCurrentUser() + o, throwable);
    }
}
