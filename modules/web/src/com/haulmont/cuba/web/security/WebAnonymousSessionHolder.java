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

package com.haulmont.cuba.web.security;

import com.haulmont.cuba.core.sys.serialization.SerializationSupport;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("cuba_WebAnonymousSessionHolder")
public class WebAnonymousSessionHolder {
    private final Logger log = LoggerFactory.getLogger(WebAnonymousSessionHolder.class);

    protected volatile boolean initialized;
    protected volatile UserSession session;

    @Inject
    protected TrustedClientService trustedClientService;
    @Inject
    protected WebAuthConfig webAuthConfig;

    public UserSession getAnonymousSession() {
        checkInitialized();

        byte[] sessionBytes = SerializationSupport.serialize(session);

        return (UserSession) SerializationSupport.deserialize(sessionBytes);
    }

    protected void checkInitialized() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    initializeAnonymousSession();
                    initialized = true;
                }
            }
        }
    }

    protected void initializeAnonymousSession() {
        log.debug("Loading anonymous session");

        try {
            this.session = getAnonymousSessionFromService();

            log.debug("Anonymous session loaded with id {}", session.getId());
        } catch (LoginException e) {
            throw new RuntimeException("Unable to obtain anonymous session from middleware", e);
        }
    }

    protected UserSession getAnonymousSessionFromService() throws LoginException {
        return trustedClientService.getAnonymousSession(webAuthConfig.getTrustedClientPassword());
    }
}