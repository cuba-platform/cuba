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

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Service interface defining methods to login users to the middleware.
 *
 * @deprecated Use {@link AuthenticationService}
 */
@Deprecated
public interface LoginService {

    String NAME = "cuba_LoginService";

    /**
     * Log out and destroy an active user session.
     *
     * @deprecated Use {@link AuthenticationService#logout()}
     */
    @Deprecated
    void logout();

    /**
     * Substitute a user, obtaining all its security related environment.
     * <br>
     * This method replaces an active UserSession with the new one, which is returned.
     *
     * @param substitutedUser a user to substitute. Must be in the current users' {@link User#substitutions} list.
     * @return new UserSession instance that contains: <ul>
     * <li> id - the previously active user session id </li>
     * <li> user - the logged in user </li>
     * <li> substitutedUser - the user passed to this method  </li>
     * <li> all security data - loaded for the substitutedUser </li>
     * </ul>
     * @deprecated Use {@link AuthenticationService#substituteUser(User)}
     */
    @Deprecated
    UserSession substituteUser(User substitutedUser);

    /**
     * Get a UserSession from the cache of currently active sessions.
     *
     * @param sessionId the session id
     * @return a UserSession instance or null, if not found
     * @deprecated use {@link TrustedClientService#findSession(String, UUID)}
     */
    @Nullable
    @Deprecated
    UserSession getSession(UUID sessionId);

    /**
     * Get system user session from a trusted client. <br>
     * Do not call {@link #logout()} for obtained user session. It is cached on middleware for multiple clients. <br>
     * Do not cache system session on clients since it is not replicated in cluster.
     *
     * @param trustedClientPassword trusted client password
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     * @deprecated use {@link TrustedClientService#getSystemSession(String)}
     */
    @Deprecated
    UserSession getSystemSession(String trustedClientPassword) throws LoginException;

    /**
     * Log in using login and user's password.
     *
     * @param login    login
     * @param password user's encrypted password
     * @param locale   client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful log in
     * @deprecated Use {@link AuthenticationService#login(Credentials)} with {@link LoginPasswordCredentials}.
     */
    @Deprecated
    UserSession login(String login, String password, Locale locale) throws LoginException;

    /**
     * Login using user name and password
     *
     * @param login    login name
     * @param password encrypted password
     * @param locale   client locale
     * @param params   map of login parameters. Supported parameters are:
     *                 <ul>
     *                 <li>"com.haulmont.cuba.core.global.ClientType": "WEB" or "DESKTOP". It is used to check the
     *                 "cuba.gui.loginToClient" specific permission.</li>
     *                 <li>"cuba.syncNewUserSessionReplication": true or false. Indicates that a new user session
     *                 created on login should be sent to the cluster synchronously. Overrides the application property
     *                 with the same name.</li>
     *                 </ul>
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     * @deprecated Use {@link AuthenticationService#login(Credentials)} with {@link LoginPasswordCredentials}.
     */
    @Deprecated
    UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException;

    /**
     * Log in from a trusted client.
     *
     * @param login    login
     * @param password client's encrypted trusted password
     * @param locale   client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful log in
     * @deprecated Use {@link AuthenticationService#login(Credentials)} with {@link TrustedClientCredentials}.
     */
    @Deprecated
    UserSession loginTrusted(String login, String password, Locale locale) throws LoginException;

    /**
     * Login using user name and trusted password
     *
     * @param login    login name
     * @param password client's encrypted trusted password
     * @param locale   client locale
     * @param params   login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     * @deprecated Use {@link AuthenticationService#login(Credentials)} with {@link TrustedClientCredentials}.
     */
    @Deprecated
    UserSession loginTrusted(String login, String password, Locale locale, Map<String, Object> params)
            throws LoginException;

    /**
     * Login using user name and remember me token
     *
     * @param login           login name
     * @param rememberMeToken client's remember me token
     * @param locale          client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     * @deprecated Use {@link AuthenticationService#login(Credentials)} with {@link RememberMeCredentials}.
     */
    @Deprecated
    UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException;

    /**
     * Login using user name and remember me token
     *
     * @param login           login name
     * @param rememberMeToken client's remember me token
     * @param locale          client locale
     * @param params          login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     * @deprecated Use {@link AuthenticationService#login(Credentials)} with {@link RememberMeCredentials}.
     */
    @Deprecated
    UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> params) throws LoginException;

    /**
     * Check if remember me token exists in db.
     *
     * @param login           user login
     * @param rememberMeToken remember me token
     * @return true if remember me token exists in db
     * @deprecated Is not supported any more, returns false
     */
    @Deprecated
    boolean checkRememberMe(String login, String rememberMeToken);

    /**
     * @return true if the brute-force protection is enabled
     * @deprecated always returns false
     */
    @Deprecated
    boolean isBruteForceProtectionEnabled();

    /**
     * @return a time interval in seconds for which a user is blocked after a series of
     * unsuccessful login attempts
     * @deprecated is not supported any more
     */
    @Deprecated
    int getBruteForceBlockIntervalSec();

    /**
     * Returns a number of login attempts left for the specified pair of login and IP-address
     *
     * @param login     user login
     * @param ipAddress user IP-address
     * @return number of login attempts left
     * @deprecated is not supported any more
     */
    @Deprecated
    int loginAttemptsLeft(String login, String ipAddress);

    /**
     * Registers unsuccessful login attempt
     *
     * @param login     user login
     * @param ipAddress user IP-address
     * @return a number of login attempts left for the specified pair of login and IP-address
     * @deprecated is not supported any more
     */
    @Deprecated
    int registerUnsuccessfulLogin(String login, String ipAddress);
}