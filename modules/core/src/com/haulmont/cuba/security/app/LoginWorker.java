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

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Interface to {@link com.haulmont.cuba.security.app.LoginWorkerBean}
 *
 */
public interface LoginWorker {

    String NAME = "cuba_LoginWorker";

    /**
     * @see LoginService#login(String, String, java.util.Locale)
     */
    UserSession login(String login, String password, Locale locale) throws LoginException;

    /**
     * @see LoginService#login(String, String, java.util.Locale, java.util.Map)
     */
    UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException;

    /**
     * @see LoginService#loginTrusted(String, String, java.util.Locale)
     */
    UserSession loginTrusted(String login, String password, Locale locale) throws LoginException;

    /**
     * @see LoginService#loginTrusted(String, String, java.util.Locale, java.util.Map))
     */
    UserSession loginTrusted(String login, String password, Locale locale, Map<String, Object> params)
            throws LoginException;

    /**
     * @see LoginService#loginByRememberMe(String, String, java.util.Locale))
     */
    UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException;

    /**
     * @see LoginService#loginTrusted(String, String, java.util.Locale, java.util.Map))
     */
    UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> params)
            throws LoginException;

    /**
     * @see LoginService#logout()
     */
    void logout();

    /**
     * @see LoginService#substituteUser(User)
     */
    UserSession substituteUser(User substitutedUser);

    /**
     * @see LoginService#getSession(UUID)
     */
    @Nullable
    UserSession getSession(UUID sessionId);

    /**
     * Log in from a middleware component. This method should not be exposed to any client tier.
     *
     * @param login login of a system user
     * @return system user session that is not replicated in cluster
     * @throws LoginException in case of unsuccessful log in
     */
    UserSession loginSystem(String login) throws LoginException;

    /**
     * @see com.haulmont.cuba.security.app.LoginService#checkRememberMe(String, String)
     */
    boolean checkRememberMe(String login, String rememberMeToken);
}