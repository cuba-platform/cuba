/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.restapi;

import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Provides authentication for controllers code.
 * <p>
 * Example usage:
 * <pre>
 *     if (!authentication.begin(sessionId)) {
 *        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
 *        return;
 *     }
 *     try {
 *         // valid current thread's user session presents here
 *     } finally {
 *         authentication.end();
 *     }
 * </pre>
 */
@Component(Authentication.NAME)
public class Authentication {

    public static final String NAME = "cuba_RestApiAuthentication";

    public static final String PERMISSION_NAME = "cuba.restApi.enabled";

    private Logger log = LoggerFactory.getLogger(Authentication.class);

    @Inject
    protected LoginService loginService;

    /**
     * Begin authenticated block of code.
     * @param sessionId {@link UserSession} id
     * @return  true if the given session id is valid and authentication is successful
     */
    public boolean begin(String sessionId) {
        UUID uuid;
        try {
            uuid = UuidProvider.fromString(sessionId);
        } catch (Exception e) {
            log.warn("Invalid user session ID: " + e.toString());
            return false;
        }

        UserSession session = loginService.getSession(uuid);
        if (session == null) {
            log.warn("User session " + uuid + " does not exist");
            return false;
        }

        if (!session.isSpecificPermitted(PERMISSION_NAME)) {
            log.warn(PERMISSION_NAME + " is not permitted for user " + session.getUser().getLogin());
            return false;
        }

        AppContext.setSecurityContext(new SecurityContext(session));
        return true;
    }

    /**
     * End authenticated block of code, i.e. remove user session from the execution thread.
     */
    public void end() {
        AppContext.setSecurityContext(null);
    }
}