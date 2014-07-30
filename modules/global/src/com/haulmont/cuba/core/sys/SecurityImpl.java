/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.ExtendedEntities;
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

    @Inject
    protected ExtendedEntities extendedEntities;

    @Override
    public boolean isScreenPermitted(String windowAlias) {
        return userSessionSource.getUserSession().isScreenPermitted(windowAlias);
    }

    @Override
    public boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        MetaClass originalMetaClass = extendedEntities.getOriginalMetaClass(metaClass);
        if (originalMetaClass != null) {
            metaClass = originalMetaClass;
        }

        return userSessionSource.getUserSession().isEntityOpPermitted(metaClass, entityOp);
    }

    @Override
    public boolean isEntityOpPermitted(Class<?> entityClass, EntityOp entityOp) {
        MetaClass metaClass = metadata.getSession().getClassNN(entityClass);

        return isEntityOpPermitted(metaClass, entityOp);
    }

    @Override
    public boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access) {
        MetaClass originalMetaClass = extendedEntities.getOriginalMetaClass(metaClass);
        if (originalMetaClass != null) {
            metaClass = originalMetaClass;
        }

        return userSessionSource.getUserSession().isEntityAttrPermitted(metaClass, property, access);
    }

    @Override
    public boolean isEntityAttrPermitted(Class<?> entityClass, String property, EntityAttrAccess access) {
        MetaClass metaClass = metadata.getSession().getClassNN(entityClass);

        return isEntityAttrPermitted(metaClass, property, access);
    }

    @Override
    public boolean isEntityAttrReadPermitted(MetaClass metaClass, String propertyPath) {
        MetaPropertyPath mpp = metaClass.getPropertyPath(propertyPath);
        return mpp != null && isEntityAttrReadPermitted(metaClass, mpp.get(), 0);
    }

    protected boolean isEntityAttrReadPermitted(MetaClass metaClass,
                                                MetaProperty[] propertyChain, int chainIndex) {
        MetaProperty chainProperty = propertyChain[chainIndex];

        if (chainIndex == propertyChain.length - 1) {
            return isEntityOpPermitted(metaClass, EntityOp.READ)
                    && isEntityAttrPermitted(metaClass, chainProperty.getName(), EntityAttrAccess.VIEW);
        } else {
            MetaClass chainMetaClass = chainProperty.getRange().asClass();

            return isEntityAttrReadPermitted(chainMetaClass, propertyChain, chainIndex + 1);
        }
    }

    @Override
    public boolean isEntityAttrUpdatePermitted(MetaClass metaClass, String propertyPath) {
        MetaPropertyPath mpp = metaClass.getPropertyPath(propertyPath);
        return mpp != null && isEntityAttrUpdatePermitted(metaClass, mpp.get(), 0);
    }

    protected boolean isEntityAttrUpdatePermitted(MetaClass metaClass,
                                                  MetaProperty[] propertyChain, int chainIndex) {
        MetaProperty chainProperty = propertyChain[chainIndex];

        if (chainIndex == propertyChain.length - 1) {
            return (isEntityOpPermitted(metaClass, EntityOp.CREATE) || isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                    && isEntityAttrPermitted(metaClass, chainProperty.getName(), EntityAttrAccess.MODIFY);
        } else {
            MetaClass chainMetaClass = chainProperty.getRange().asClass();

            return isEntityAttrUpdatePermitted(chainMetaClass, propertyChain, chainIndex + 1);
        }
    }

    @Override
    public boolean isSpecificPermitted(String name) {
        return userSessionSource.getUserSession().isSpecificPermitted(name);
    }
}