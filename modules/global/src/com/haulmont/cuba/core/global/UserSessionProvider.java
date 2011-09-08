/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.UserSession;

import java.util.Locale;
import java.util.UUID;

/**
 * Provides access to the current user session in static context.<br>
 * <p>Injected {@link UserSessionSource} interface should be used instead of this class wherever possible.</p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class UserSessionProvider {

    private static UserSessionSource getSource() {
        return AppContext.getBean(UserSessionSource.NAME, UserSessionSource.class);
    }

    /**
     * Current user session
     * @return current user session instance
     */
    public static UserSession getUserSession() {
        return getSource().getUserSession();
    }

    public static UUID currentOrSubstitutedUserId() {
        return getSource().currentOrSubstitutedUserId();
    }

    public static Locale getLocale() {
        return getSource().getLocale();
    }
}
