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
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.auth.RememberMeCredentials;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection;
import com.haulmont.cuba.web.security.AnonymousUserCredentials;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.Locale;

/**
 * Interface to be implemented by objects that connect web-client to the middleware.
 */
public interface Connection extends ExternallyAuthenticatedConnection {

    String NAME = "cuba_Connection";

    /**
     * Authenticates a user, starts session and changes state of the connection.
     *
     * @param credentials credentials
     * @throws LoginException if authentication fails
     */
    void login(Credentials credentials) throws LoginException;

    /**
     * Log out of the system.
     */
    void logout();

    /**
     * Get current user session.
     *
     * @return user session object or null if not connected
     */
    @Nullable
    UserSession getSession();

    /**
     * Get current user session.
     *
     * @return user session object or null if not connected
     */
    @Nonnull
    default UserSession getSessionNN() {
        UserSession userSession = getSession();
        if (userSession == null) {
            throw new IllegalStateException("Unable to obtain session from connected Connection");
        }
        return userSession;
    }

    /**
     * Substitute a user in the current session with another user. This method creates a new UserSession instance,
     * but with the same session ID.
     * <p>New user is usually obtained from the current user's substitution list:
     * see {@link com.haulmont.cuba.security.entity.User#getSubstitutions()}</p>
     *
     * @param substitutedUser new user
     */
    void substituteUser(User substitutedUser);

    /**
     * Check if the client is connected to the middleware.
     *
     * @return true if connected
     */
    boolean isConnected();

    /**
     * Check if the client was authenticated.
     *
     * @return true if authenticated
     */
    boolean isAuthenticated();

    /**
     * Check if session is alive on middleware
     *
     * @return true if call to middleware performed successfully
     */
    boolean isAlive();

    /**
     * Add a connection listener.
     *
     * @param listener listener to add
     */
    void addStateChangeListener(StateChangeListener listener);

    /**
     * Remove a connection listener.
     *
     * @param listener listener to remove
     */
    void removeStateChangeListener(StateChangeListener listener);

    /**
     * Add a user substitution listener.
     *
     * @param listener listener to add
     */
    void addUserSubstitutionListener(UserSubstitutionListener listener);

    /**
     * Remove a user substitution listener.
     *
     * @param listener listener to remove
     */
    void removeUserSubstitutionListener(UserSubstitutionListener listener);

    /**
     * Listener of connection events. See {@link com.haulmont.cuba.web.Connection}.
     */
    @FunctionalInterface
    interface StateChangeListener {
        void connectionStateChanged(StateChangeEvent event);
    }

    /**
     * Listener of user substitution events. See {@link com.haulmont.cuba.web.Connection}.
     */
    @FunctionalInterface
    interface UserSubstitutionListener {
        void userSubstituted(UserSubstitutedEvent event);
    }

    class StateChangeEvent extends EventObject {
        public StateChangeEvent(Connection source) {
            super(source);
        }

        @Override
        public Connection getSource() {
            return (Connection) super.getSource();
        }
    }

    class UserSubstitutedEvent extends EventObject {
        public UserSubstitutedEvent(Connection source) {
            super(source);
        }

        @Override
        public Connection getSource() {
            return (Connection) super.getSource();
        }
    }

    /**
     * Log in to the system.
     *
     * @param login    user login name
     * @param password encrypted user password
     * @param locale   user locale
     * @throws LoginException in case of unsuccessful login due to wrong credentials or other issues
     */
    @Deprecated
    default void login(String login, String password, Locale locale) throws LoginException {
        PasswordEncryption passwordEncryption = AppBeans.get(PasswordEncryption.class);
        login(new LoginPasswordCredentials(login, passwordEncryption.getPlainHash(password), locale));
    }

    /**
     * Log in to the system.
     *
     * @param locale user locale
     * @throws LoginException in case of unsuccessful login due to wrong credentials or other issues
     */
    @Deprecated
    default void loginAnonymous(Locale locale) throws LoginException {
        login(new AnonymousUserCredentials(locale));
    }

    /**
     * Log in to the system.
     *
     * @param login           user login name
     * @param rememberMeToken remember me token
     * @param locale          user locale
     * @throws LoginException in case of unsuccessful login due to wrong credentials or other issues
     */
    @Deprecated
    default void loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException {
        login(new RememberMeCredentials(login, rememberMeToken, locale));
    }
}