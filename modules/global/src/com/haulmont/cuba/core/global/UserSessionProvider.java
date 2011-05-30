/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
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
public abstract class UserSessionProvider {

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

    public static Locale getLocale() {
        return getInstance().__getUserSession().getLocale();
    }

    public static boolean isEditPermitted(MetaProperty metaProperty) {
        MetaClass metaClass = metaProperty.getDomain();

        UserSession userSession = getUserSession();
        return (userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                                || userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                && userSession.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);
    }

    protected abstract UserSession __getUserSession();

    private static UserSessionProvider getInstance() {
        return (UserSessionProvider) AppContext.getApplicationContext().getBean("cuba_UserSessionProvider");
    }
}
