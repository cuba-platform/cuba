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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.security.global.UserSession;

import java.util.UUID;

/**
 * @deprecated Use {@link com.haulmont.cuba.client.UserSessionClient}
 */
@Deprecated
public abstract class UserSessionClient
{
    @Deprecated
    public static UserSession getUserSession() {
        return com.haulmont.cuba.client.UserSessionClient.getUserSession();
    }

    @Deprecated
    public static UUID currentOrSubstitutedUserId() {
        return com.haulmont.cuba.client.UserSessionClient.currentOrSubstitutedUserId();
    }

    @Deprecated
    public static boolean isEditPermitted(MetaProperty metaProperty) {
        return com.haulmont.cuba.client.UserSessionClient.isEditPermitted(metaProperty);
    }
}
