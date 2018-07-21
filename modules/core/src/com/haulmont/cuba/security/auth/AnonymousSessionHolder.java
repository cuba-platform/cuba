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
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Stores shared anonymous session instance for clients.
 */
@Component("cuba_AnonymousSessionHolder")
public class AnonymousSessionHolder implements AppContext.Listener, Ordered {

    private final Logger log = LoggerFactory.getLogger(AnonymousSessionHolder.class);

    protected volatile UserSession session;

    @Inject
    protected AuthenticationManager authenticationManager;

    @Override
    public void applicationStarted() {
        initializeAnonymousSession();
    }

    @Override
    public void applicationStopped() {
        // do nothing
    }

    @Override
    public int getOrder() {
        return LOWEST_PLATFORM_PRECEDENCE - 110;
    }

    public UserSession getAnonymousSession() {
        if (session == null) {
            throw new IllegalStateException(
                    "Anonymous session is not initialized. Check the application log for the original cause."
            );
        }

        return session;
    }

    protected void initializeAnonymousSession() {
        log.debug("Initialize anonymous session");

        try {
            this.session = loginAnonymous();

            log.debug("Anonymous session initialized with id {}", session.getId());
        } catch (LoginException e) {
            // Server should not start in this case
            throw new RuntimeException("Unable to create anonymous session. It is required for system to start", e);
        }
    }

    protected UserSession loginAnonymous() throws LoginException {
        return authenticationManager.login(new AnonymousUserCredentials()).getSession();
    }

    @PostConstruct
    public void init() {
        AppContext.addListener(this);
    }
}