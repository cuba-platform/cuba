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

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Interface to be implemented by objects that connect web-client to the middleware.
 */
public interface Connection {

    String NAME = "cuba_Connection";

    enum SessionMode {
        AUTHENTICATED,
        ANONYMOUS
    }

    /**
     * Log in to the system.
     * @param login             user login name
     * @param password          encrypted user password
     * @param locale            user locale
     * @throws LoginException   in case of unsuccessful login due to wrong credentials or other issues
     */
    void login(String login, String password, Locale locale) throws LoginException;

    /**
     * Log in to the system.
     * @param locale            user locale
     * @throws LoginException   in case of unsuccessful login due to wrong credentials or other issues
     */
    void loginAnonymous(Locale locale) throws LoginException;

    /**
     * Log in to the system.
     * @param login             user login name
     * @param rememberMeToken   remember me token
     * @param locale            user locale
     * @throws LoginException   in case of unsuccessful login due to wrong credentials or other issues
     */
    void loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException;

    /**
     * Log out of the system.
     */
    void logout();

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
     * Check if the client was authenticated.
     * @return  true if authenticated
     */
    boolean isAuthenticated();

    /**
     * Check if session is alive on middleware
     *
     * @return true if call to middleware performed successfully
     */
    boolean isAlive();

    /**
     * Check if remember me token exists in db
     *
     * @param login           user login
     * @param rememberMeToken remember me token
     * @return true if remember me token exists in db
     */
    @Deprecated
    default boolean checkRememberMe(String login, String rememberMeToken) {
        LoggerFactory.getLogger(Connection.class)
                .warn("LoginService checkRememberMe is not supported any more. Always returns false");
        return false;
    }

    /**
     * Get current user session.
     * @return  user session object or null if not connected
     */
    @Nullable
    UserSession getSession();

    /**
     * Update internal state with the passed user session object. Also fires connection listeners.
     *
     * @param session new UserSession object
     * @throws LoginException in case of unsuccessful update
     */
    void update(UserSession session, SessionMode sessionMode) throws LoginException;

    /**
     * Update internal state with the passed user session object. Also fires connection listeners.
     *
     * @param session            new UserSession object
     * @param sessionInitializer optional callback that will be triggered after session setup and before triggering
     *                           connection state change listeners
     * @throws LoginException in case of unsuccessful update
     */
    void update(UserSession session, SessionMode sessionMode,
                @Nullable Consumer<UserSessionInitEvent> sessionInitializer) throws LoginException;

    /**
     * Add a connection listener.
     * @param listener  listener to add
     */
    void addConnectionListener(ConnectionListener listener);
    /**
     * Remove a connection listener.
     * @param listener  listener to remove
     */
    void removeConnectionListener(ConnectionListener listener);

    /**
     * Add a user substitution listener.
     * @param listener  listener to add
     */
    void addSubstitutionListener(UserSubstitutionListener listener);
    /**
     * Remove a user substitution listener.
     * @param listener  listener to remove
     */
    void removeSubstitutionListener(UserSubstitutionListener listener);

    /**
     * Event that is used for additional initialization during {@link Connection#update(UserSession, SessionMode, Consumer)}.
     */
    class UserSessionInitEvent extends EventObject {
        private final UserSession userSession;

        public UserSessionInitEvent(Connection source, UserSession userSession) {
            super(source);
            this.userSession = userSession;
        }

        @Override
        public Connection getSource() {
            return (Connection) super.getSource();
        }

        public UserSession getUserSession() {
            return userSession;
        }
    }
}