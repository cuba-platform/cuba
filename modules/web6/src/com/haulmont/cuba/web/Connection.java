/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Interface to be implemented by middleware connection objects on web-client.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Connection {

    /**
     * Log in to the system.
     * @param login             user login name
     * @param password          encrypted user password
     * @param locale            user locale
     * @throws LoginException   in case of unsuccesful login due to wrong credentials or other issues
     */
    void login(String login, String password, Locale locale) throws LoginException;

    /**
     * Log out of the system.
     * Returns URL to which the user will be redirected after logout.
     * @return redirection URL
     */
    String logout();

    /**
     * Substitute a user in the current session with another user. This method creates a new UserSession instance,
     * but with the same session ID.
     * <p>New user is usually obtained from the current user's substitution list:
     * see {@link com.haulmont.cuba.security.entity.User#getSubstitutions()}</p>
     * @param substitutedUser   new user
     */
    void substituteUser(User substitutedUser);

    /**
     * Check if the client is connected to the middleware.
     * @return  true if connected
     */
    boolean isConnected();

    /**
     * Get current user session.
     * @return  user session object or null if not connected
     */
    @Nullable
    UserSession getSession();

    /**
     * Update internal state with the passed user session object. Also fires connection listeners.
     * @param session           new UserSession object
     * @throws LoginException   in case of unsuccesful update
     */
    void update(UserSession session) throws LoginException;

    /**
     * Add a connection listener.
     * @param listener  listener to add
     */
    void addListener(ConnectionListener listener);

    /**
     * Remove a connection listener.
     * @param listener  listener to remove
     */
    void removeListener(ConnectionListener listener);

    /**
     * Add a user substitution listener.
     * @param listener  listener to add
     */
    void addListener(UserSubstitutionListener listener);

    /**
     * Remove a user substitution listener.
     * @param listener  listener to remove
     */
    void removeListener(UserSubstitutionListener listener);
}
