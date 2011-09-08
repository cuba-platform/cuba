/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.global.UserSession;

import java.util.Locale;
import java.util.UUID;

/**
 * Provides access to the current user session
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface UserSessionSource {

    String NAME = "cuba_UserSessionSource";

    /**
     * Check if the current user session is valid
     * @return  true if session is valid
     */
    boolean checkCurrentUserSession();

    /**
     * Current user session
     * @return current user session instance
     */
    UserSession getUserSession();

    UUID currentOrSubstitutedUserId();

    Locale getLocale();
}
