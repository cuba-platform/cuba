/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.portal;

import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.security.global.LoginException;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Locale;

/**
 * Interface to be implemented by middleware connection objects on Web Portal.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Connection extends Serializable {

    String NAME = "cuba_Connection";

    /**
     * Log in to the system.
     *
     * @param login      user login name
     * @param password   encrypted user password
     * @param locale     user locale
     * @param ipAddress  IP address
     * @param clientInfo Client info
     * @throws com.haulmont.cuba.security.global.LoginException
     *          in case of unsuccesful login due to wrong credentials or other issues
     */
    void login(String login, String password, Locale locale,
               @Nullable String ipAddress, @Nullable String clientInfo) throws LoginException;

    /**
     * Log in to the system with system account.
     *
     * @param locale     Locale
     * @param ipAddress  IP address
     * @param clientInfo Client info
     * @throws com.haulmont.cuba.security.global.LoginException
     *          in case of unsuccesful login due to wrong credentials or other issues
     */
    void login(Locale locale, @Nullable String ipAddress, @Nullable String clientInfo) throws LoginException;

    /**
     * Log out of the system.
     * Returns URL to which the user will be redirected after logout.
     */
    void logout();

    /**
     * Check if the client is connected to the middleware.
     *
     * @return true if connected
     */
    boolean isConnected();

    /**
     * Get current user session.
     *
     * @return user session object or null if not connected
     */
    @Nullable
    PortalSession getSession();

    /**
     * Update internal state with the passed user session object. Also fires connection listeners.
     *
     * @param session new UserSession object
     * @throws com.haulmont.cuba.security.global.LoginException
     *          in case of unsuccesful update
     */
    void update(PortalSession session) throws LoginException;

    /**
     * Add a connection listener.
     *
     * @param listener listener to add
     */
    void addListener(ConnectionListener listener);

    /**
     * Remove a connection listener.
     *
     * @param listener listener to remove
     */
    void removeListener(ConnectionListener listener);
}
