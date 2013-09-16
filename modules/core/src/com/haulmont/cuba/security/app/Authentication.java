/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean that provides authentication to an arbitrary code on the Middleware.
 * <p/>
 * Authentication is required if the code doesn't belong to a normal user request handling, which is the case for
 * invocation by schedulers or JMX tools, other than Web Client's JMX-console.
 * <p/>
 * Example usage:
 * <pre>
 *     authentication.begin();
 *     try {
 *         // valid current thread's user session presents here
 *     } finally {
 *         authentication.end();
 *     }
 * </pre>
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Authentication.NAME)
public class Authentication {

    public static final String NAME = "cuba_Authentication";

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected LoginWorker loginWorker;

    @Inject
    protected UserSessionManager userSessionManager;

    protected ThreadLocal<Boolean> needCleanup = new ThreadLocal<>();

    protected Map<String, UUID> sessions = new ConcurrentHashMap<>();

    /**
     * Begin an authenticated code block.
     * <p/>
     * If a valid current thread session exists, does nothing.
     * Otherwise sets the current thread session, logging in if necessary.
     * <p/>
     * Subsequent {@link #end()} method must be called in "finally" section.
     *
     * @param login user login. If null, a value of <code>cuba.jmxUserLogin</code> app property is used.
     */
    public void begin(String login) {
        // first check if a current thread session exists, that is we got here from authenticated code
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext != null && userSessionManager.findSession(securityContext.getSessionId()) != null) {
            log.trace("Already authenticated, do nothing");
            return;
        }
        // no current thread session or it is expired - need to authenticate
        if (StringUtils.isBlank(login))
            login = getSystemLogin();

        UserSession session = null;
        log.trace("Authenticating as " + login);
        UUID sessionId = sessions.get(login);
        if (sessionId != null) {
            session = userSessionManager.findSession(sessionId);
        }
        if (session == null) {
            // saved session doesn't exist or is expired
            synchronized (this) {
                // double check to prevent the same log in by subsequent threads
                sessionId = sessions.get(login);
                if (sessionId != null) {
                    session = userSessionManager.findSession(sessionId);
                }
                if (session == null) {
                    try {
                        session = loginWorker.loginSystem(login);
                    } catch (LoginException e) {
                        throw new RuntimeException(e);
                    }
                    sessions.put(login, session.getId());
                }
            }
        }

        AppContext.setSecurityContext(new SecurityContext(session));
        needCleanup.set(true);
    }

    /**
     * Authenticate with login set in <code>cuba.jmxUserLogin</code> app property.
     * <p/>
     * Same as {@link #begin(String)} with null parameter
     */
    public void begin() {
        begin(null);
    }

    /**
     * End of an authenticated code block.
     * <p/>
     * Performs cleanup for SecurityContext if there was previous loginOnce in this thread.
     * Must be called in "finally" section of a try/finally block.
     */
    public void end() {
        if (Boolean.TRUE.equals(needCleanup.get())) {
            log.trace("Cleanup SecurityContext");
            AppContext.setSecurityContext(null);
        }
        needCleanup.remove();
    }

    protected String getSystemLogin() {
        return AppContext.getProperty("cuba.jmxUserLogin");
    }
}
