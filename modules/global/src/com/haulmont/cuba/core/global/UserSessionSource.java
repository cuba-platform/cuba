/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.global.UserSession;

import java.util.Locale;
import java.util.UUID;

/**
 * Central infrastructure interface to provide access to a current user session.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface UserSessionSource {

    String NAME = "cuba_UserSessionSource";

    /**
     * @return  true if the current user session is valid
     */
    boolean checkCurrentUserSession();

    /**
     * @return current user session. Throws an exception if there is no active user session.
     */
    UserSession getUserSession();

    /**
     * @return effective user ID. This is either the logged in user, or substituted user if a substitution was performed
     * in this user session.
     */
    UUID currentOrSubstitutedUserId();

    /**
     * @return current user session locale
     */
    Locale getLocale();
}
