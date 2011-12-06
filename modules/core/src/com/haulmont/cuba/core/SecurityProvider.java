/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:27:17
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.UserSession;

import java.util.Arrays;
import java.util.UUID;

/**
 * This class is obsolete and not recommended to use.<br/>
 * Use {@link UserSessionSource} and {@link PersistenceSecurity} instead.
 */
@Deprecated
public abstract class SecurityProvider
{
    private static UserSessionSource getUserSessionSource() {
        return AppContext.getBean(UserSessionSource.NAME, UserSessionSource.class);
    }

    private static PersistenceSecurity getPersistenceSecurity() {
        return AppContext.getBean(PersistenceSecurity.NAME, PersistenceSecurity.class);
    }

    /**
     * Check if current user session is valid
     */
    public static boolean checkCurrentUserSession() {
        return getUserSessionSource().checkCurrentUserSession();
    }

    /**
     * Current (logged in) user identifier
     */
    public static UUID currentUserId() {
        return getUserSessionSource().getUserSession().getUser().getId();
    }

    /**
     * Returns substituted user ID if there is one, otherwise returns logged in user ID
     */
    public static UUID currentOrSubstitutedUserId() {
        return getUserSessionSource().currentOrSubstitutedUserId();
    }

    /**
     * Current user session
     */
    public static UserSession currentUserSession() {
        return getUserSessionSource().getUserSession();
    }

    /**
     * Checks if the current user belongs to role
     * @param role role name
     */
    public static boolean currentUserInRole(String role) {
        UserSession session = getUserSessionSource().getUserSession();
        return session.getRoles().contains(role);
    }

    /**
     * Modifies the query depending on current user's security constraints
     * @param query query to modify
     * @param entityName name of entity which is quering
     */
    public static boolean applyConstraints(Query query, String entityName) {
        return getPersistenceSecurity().applyConstraints(query, entityName);
    }

    /**
     * Sets the query param to a value provided by user session (see constants above)
     * @param query Query instance
     * @param paramName parameter to set
     */
    public static void setQueryParam(Query query, String paramName) {
        getPersistenceSecurity().setQueryParam(query, paramName);
    }
}
