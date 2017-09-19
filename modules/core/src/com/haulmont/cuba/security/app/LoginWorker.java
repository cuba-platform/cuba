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
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Interface to {@link com.haulmont.cuba.security.app.LoginWorkerBean}
 *
 * @deprecated Use {@link AuthenticationManager}
 */
@Deprecated
public interface LoginWorker {

    String NAME = "cuba_LoginWorker";

    /**
     * @deprecated Use {@link AuthenticationManager#logout()}
     */
    @Deprecated
    void logout();

    /**
     * @deprecated Use {@link AuthenticationManager#substituteUser(User)}
     */
    @Deprecated
    UserSession substituteUser(User substitutedUser);

    /**
     * @deprecated User {@link UserSessionManager#findSession(UUID)}
     */
    @Nullable
    @Deprecated
    UserSession getSession(UUID sessionId);

    /**
     * Log in from a middleware component. This method should not be exposed to any client tier.
     *
     * @param login login of a system user
     * @return system user session that is not replicated in cluster
     * @throws LoginException in case of unsuccessful log in
     *
     * @deprecated Use {@link AuthenticationManager} with {@link SystemUserCredentials}
     */
    @Deprecated
    UserSession loginSystem(String login) throws LoginException;

    /**
     * Login anonymous session for trusted clients
     *
     * @return anonymous user session that is not replicated in cluster
     * @throws LoginException in case of login problem
     *
     * @deprecated Use {@link AuthenticationManager} with {@link AnonymousUserCredentials}
     */
    @Deprecated
    UserSession loginAnonymous() throws LoginException;

    /**
     * @deprecated Use {@link AuthenticationManager} with {@link LoginPasswordCredentials}
     */
    @Deprecated
    UserSession login(String login, String password, Locale locale) throws LoginException;

    /**
     * @deprecated Use {@link AuthenticationManager} with {@link LoginPasswordCredentials}
     */
    @Deprecated
    UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException;

    /**
     * @deprecated Use {@link Authentication} directly.
     */
    @Deprecated
    UserSession getSystemSession(String trustedClientPassword) throws LoginException;

    /**
     * @see LoginService#loginTrusted(String, String, java.util.Locale)
     *
     * @deprecated Use {@link AuthenticationManager} with {@link TrustedClientCredentials}
     */
    @Deprecated
    UserSession loginTrusted(String login, String password, Locale locale) throws LoginException;

    /**
     * @deprecated Use {@link AuthenticationManager} with {@link TrustedClientCredentials}
     */
    @Deprecated
    UserSession loginTrusted(String login, String password, Locale locale, Map<String, Object> params)
            throws LoginException;

    /**
     * @deprecated Use {@link AuthenticationManager} with {@link RememberMeCredentials}
     */
    @Deprecated
    UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException;

    /**
     * @deprecated Use {@link AuthenticationManager} with {@link RememberMeCredentials}
     */
    @Deprecated
    UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> params)
            throws LoginException;

    /**
     * @deprecated is not supported any more, returns false
     */
    @Deprecated
    boolean checkRememberMe(String login, String rememberMeToken);

    /**
     * Check credentials of user and return loaded user entity.
     *
     * @param login      login
     * @param password   user's encrypted password
     * @param locale     locale
     * @param parameters additional parameters
     * @return user
     * @throws LoginException in case of unsuccessful authentication
     *
     * @deprecated Use {@link AuthenticationManager#authenticate(Credentials)} with {@link LoginPasswordCredentials}
     */
    @Deprecated
    User authenticate(String login, String password, Locale locale, Map<String, Object> parameters)
            throws LoginException;
}