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

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.auth.AnonymousSessionHolder;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.UUID;

import static com.haulmont.cuba.core.sys.AppContext.withSecurityContext;

@Component(TrustedClientService.NAME)
public class TrustedClientServiceBean implements TrustedClientService {

    protected static final String MSG_PACK = "com.haulmont.cuba.security";

    @Inject
    protected TrustedLoginHandler trustedLoginHandler;
    @Inject
    protected ServerConfig serverConfig;
    @Inject
    protected Messages messages;
    @Inject
    protected Authentication authentication;
    @Inject
    protected UserSessionManager userSessionManager;
    @Inject
    protected AnonymousSessionHolder anonymousSessionHolder;

    @Nonnull
    @Override
    public UserSession getSystemSession(String trustedClientPassword) throws LoginException {
        if (!trustedLoginHandler.checkPassword(trustedClientPassword)) {
            throw new LoginException(getInvalidCredentialsMessage(serverConfig.getJmxUserLogin(),
                    messages.getTools().getDefaultLocale()));
        }

        return withSecurityContext(null, () -> {
            UserSession userSession = authentication.begin();
            authentication.end();

            return userSession;
        });
    }

    @Nonnull
    @Override
    public UserSession getAnonymousSession(String trustedClientPassword) throws LoginException {
        if (!trustedLoginHandler.checkPassword(trustedClientPassword)) {
            throw new LoginException(getInvalidCredentialsMessage(serverConfig.getJmxUserLogin(),
                    messages.getTools().getDefaultLocale()));
        }

        return anonymousSessionHolder.getAnonymousSession();
    }

    @Nullable
    @Override
    public UserSession findSession(String trustedClientPassword, UUID sessionId) throws LoginException {
        if (!trustedLoginHandler.checkPassword(trustedClientPassword)) {
            throw new LoginException(getInvalidCredentialsMessage(serverConfig.getJmxUserLogin(),
                    messages.getTools().getDefaultLocale()));
        }

        return userSessionManager.findSession(sessionId);
    }

    protected String getInvalidCredentialsMessage(String login, Locale locale) {
        return messages.formatMessage(MSG_PACK, "LoginException.InvalidLoginOrPassword", locale, login);
    }
}