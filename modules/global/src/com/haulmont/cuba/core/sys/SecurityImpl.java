/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Security.NAME)
public class SecurityImpl implements Security {

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Metadata metadata;

    @Override
    public boolean isScreenPermitted(ClientType clientType, String windowAlias) {
        return userSessionSource.getUserSession().isScreenPermitted(windowAlias);
    }

    @Override
    public boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        return userSessionSource.getUserSession().isEntityOpPermitted(metaClass, entityOp);
    }

    @Override
    public boolean isEntityOpPermitted(Class<?> entityClass, EntityOp entityOp) {
        return isEntityOpPermitted(metadata.getSession().getClassNN(entityClass), entityOp);
    }

    @Override
    public boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access) {
        return userSessionSource.getUserSession().isEntityAttrPermitted(metaClass, property, access);
    }

    @Override
    public boolean isEntityAttrPermitted(Class<?> entityClass, String property, EntityAttrAccess access) {
        return isEntityAttrPermitted(metadata.getSession().getClassNN(entityClass), property, access);
    }

    @Override
    public boolean isEntityAttrModificationPermitted(MetaProperty metaProperty) {
        if (metaProperty.getModel() == null)
            return true;

        MetaClass metaClass = metaProperty.getDomain();

        return (isEntityOpPermitted(metaClass, EntityOp.CREATE) || isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                && isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);
    }

    @Override
    public boolean isSpecificPermitted(String name) {
        return userSessionSource.getUserSession().isSpecificPermitted(name);
    }
}
