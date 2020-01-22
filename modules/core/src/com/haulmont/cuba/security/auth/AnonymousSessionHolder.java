/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.security.auth;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.SecurityScopes;
import com.haulmont.cuba.security.entity.SecurityScope;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Stores shared anonymous sessions instance for clients. A separate UserSession is created for each security scope.
 */
@Component("cuba_AnonymousSessionHolder")
public class AnonymousSessionHolder implements AppContext.Listener, Ordered {

    private final Logger log = LoggerFactory.getLogger(AnonymousSessionHolder.class);

    protected volatile Map<String, UserSession> sessions = new HashMap<>();

    @Inject
    protected AuthenticationManager authenticationManager;

    @Inject
    protected SecurityScopes securityScopes;

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void applicationStarted() {
        initializeAnonymousSessions();
    }

    @Override
    public void applicationStopped() {
        // do nothing
    }

    @Override
    public int getOrder() {
        return LOWEST_PLATFORM_PRECEDENCE - 110;
    }

    /**
     * Method returns a UserSession saved for the default scope
     * @return a UserSession saved for the default scope
     */
    public UserSession getAnonymousSession() {
        return getAnonymousSession(SecurityScope.DEFAULT_SCOPE_NAME);
    }

    /**
     * Method returns a UserSession saved for the given scope
     * @param securityScope security scope name
     * @return a UserSession saved for the given scope
     */
    public UserSession getAnonymousSession(String securityScope) {
        lock.readLock().lock();
        try {
            UserSession session = sessions.get(securityScope);
            if (session == null) {
                throw new IllegalStateException(
                        "Anonymous session is not initialized. Check the application log for the original cause."
                );
            }
            return session;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void initializeAnonymousSessions() {
        log.debug("Initialize anonymous sessions");
        lock.writeLock().lock();
        try {
            sessions.clear();
            Collection<SecurityScope> scopes = securityScopes.getAvailableSecurityScopes();
            for (SecurityScope scope : scopes) {
                String scopeName = scope.getName();
                UserSession session = loginAnonymous(scopeName);
                sessions.put(scopeName, session);
                log.debug("Anonymous session for scope {} initialized with id {}", scopeName, session.getId());
            }
        } catch (LoginException e) {
            // Server should not start in this case
            throw new RuntimeException("Unable to create anonymous session. It is required for system to start", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected UserSession loginAnonymous(String securityScope) throws LoginException {
        AnonymousUserCredentials credentials = new AnonymousUserCredentials();
        credentials.setSecurityScope(securityScope);
        return authenticationManager.login(credentials).getSession();
    }

    @PostConstruct
    public void init() {
        AppContext.addListener(this);
    }
}