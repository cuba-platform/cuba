/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.app;

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
 * @author krivopustov
 * @version $Id$
 */
public interface LoginService {

    String NAME = "cuba_LoginService";

    /**
     * Log in using login and user's password.
     *
     * @param login    login
     * @param password user's encrypted password
     * @param locale   client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful log in
     */
    UserSession login(String login, String password, Locale locale) throws LoginException;

    /**
     * Login using user name and password
     *
     * @param login    login name
     * @param password encrypted password
     * @param locale   client locale
     * @param params   login params
     * @return created user session
     * @throws LoginException in case of unsuccessful login
     */
    UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException;

    /**
     * Log in from a trusted client.
     *
     * @param login    login
     * @param password client's encrypted trusted password
     * @param locale   client locale
     * @return created user session
     * @throws LoginException in case of unsuccessful log in
     */
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
     */
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
     */
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
     */
    UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> params) throws LoginException;

    /**
     * Log out and destroy an active user session.
     */
    void logout();

    /**
     * Substitute a user, obtaining all its security related environment.
     * <p/>
     * This method replaces an active UserSession with the new one, which is returned.
     *
     * @param substitutedUser a user to substitute. Must be in the current users' {@link User#substitutions} list.
     * @return new UserSession instance that contains: <ul>
     *         <li> id - the previously active user session id </li>
     *         <li> user - the logged in user </li>
     *         <li> substitutedUser - the user passed to this method  </li>
     *         <li> all security data - loaded for the substitutedUser </li>
     *         </ul>
     */
    UserSession substituteUser(User substitutedUser);

    /**
     * Get a UserSession from the cache of currently active sessions.
     *
     * @param sessionId the session id
     * @return a UserSession instance or null, if not found
     */
    @Nullable
    UserSession getSession(UUID sessionId);

    /**
     * Check if remember me token exists in db
     *
     * @param login           user login
     * @param rememberMeToken remember me token
     * @return true if remember me token exists in db
     */
    boolean checkRememberMe(String login, String rememberMeToken);
}