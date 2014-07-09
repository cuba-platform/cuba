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
 * Interface to {@link com.haulmont.cuba.security.app.LoginWorkerBean}
 *
 * @author krivopustov
 * @version $Id$
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