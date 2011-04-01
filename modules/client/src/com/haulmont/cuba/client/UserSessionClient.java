/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 17:54
 *
 * $Id$
 */
package com.haulmont.cuba.client;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

import java.util.UUID;

/**
 * Client-side class providing access to the current user session
 */
public abstract class UserSessionClient {

    /**
     * Current user session
     */
    public static UserSession getUserSession() {
        return getInstance().__getUserSession();
    }

    public static UUID currentOrSubstitutedUserId() {
        UserSession us = getInstance().__getUserSession();
        return us.getSubstitutedUser() != null ? us.getSubstitutedUser().getId() : us.getUser().getId();
    }

    public static boolean isEditPermitted(MetaProperty metaProperty) {
        MetaClass metaClass = metaProperty.getDomain();

        UserSession userSession = getUserSession();
        return (userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                                || userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                && userSession.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);
    }

    protected abstract UserSession __getUserSession();

    private static UserSessionClient getInstance() {
        return (UserSessionClient) AppContext.getApplicationContext().getBean("cuba_UserSessionClient");
    }
}
