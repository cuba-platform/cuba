/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.security;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;

import java.util.Locale;

/**
 * Provides access to the current user session in static context.<br>
 * <p>Injected {@link UserSessionSource} interface should be used instead of this class wherever possible.</p>
 *
 * @author artamonov
 * @version $Id$
 */
public abstract class PortalSessionProvider {

    private static UserSessionSource getSource() {
        return AppBeans.get(UserSessionSource.NAME, UserSessionSource.class);
    }

    /**
     * Current portal session
     *
     * @return current user session instance
     */
    public static <T extends PortalSession> T getUserSession() {
        return (T) getSource().getUserSession();
    }

    public static Locale getLocale() {
        return getSource().getLocale();
    }
}
