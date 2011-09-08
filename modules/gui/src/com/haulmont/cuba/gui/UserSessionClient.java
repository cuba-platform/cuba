/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.04.2009 15:17:01
 *
 * $Id$
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
