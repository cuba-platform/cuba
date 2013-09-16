/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.security.global.UserSession;

import java.util.UUID;

/**
 * @deprecated Use {@link com.haulmont.cuba.core.global.UserSessionProvider}
 */
@Deprecated
public abstract class UserSessionClient
{
    @Deprecated
    public static UserSession getUserSession() {
        return UserSessionProvider.getUserSession();
    }

    @Deprecated
    public static UUID currentOrSubstitutedUserId() {
        return UserSessionProvider.currentOrSubstitutedUserId();
    }
}
