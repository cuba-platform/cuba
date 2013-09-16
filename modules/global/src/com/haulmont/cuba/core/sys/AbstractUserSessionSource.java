/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;

import java.util.Locale;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractUserSessionSource implements UserSessionSource {

    @Override
    public UUID currentOrSubstitutedUserId() {
        UserSession us = getUserSession();
        return us.getSubstitutedUser() != null ? us.getSubstitutedUser().getId() : us.getUser().getId();
    }

    @Override
    public Locale getLocale() {
        return getUserSession().getLocale();
    }
}
