/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.*;
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
    protected MetadataTools metadataTools;

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
        MetaPropertyPath mpp = metadataTools.resolveMetaPropertyPath(metaClass, property);
        return mpp != null && isEntityAttrPermitted(metaClass, mpp, access);
    }

    @Override
    public boolean isEntityAttrPermitted(Class<?> entityClass, String property, EntityAttrAccess access) {
        MetaClass metaClass = metadata.getSession().getClassNN(entityClass);
        return isEntityAttrPermitted(metaClass, property, access);
    }

    @Override
    public boolean isEntityAttrReadPermitted(MetaClass metaClass, String propertyPath) {
        MetaPropertyPath mpp = metadataTools.resolveMetaPropertyPath(metaClass, propertyPath);
        return mpp != null && isEntityAttrReadPermitted(mpp);
    }

    @Override
    public boolean isEntityAttrUpdatePermitted(MetaClass metaClass, String propertyPath) {
        MetaPropertyPath mpp = metadataTools.resolveMetaPropertyPath(metaClass, propertyPath);
        return mpp != null && isEntityAttrUpdatePermitted(mpp);
    }

    @Override
    public boolean isSpecificPermitted(String name) {
        return userSessionSource.getUserSession().isSpecificPermitted(name);
    }

    protected boolean isEntityAttrReadPermitted(MetaPropertyPath mpp) {
        MetaClass propertyMetaClass = metadata.getTools().getPropertyEnclosingMetaClass(mpp);
        return isEntityOpPermitted(propertyMetaClass, EntityOp.READ)
                && isEntityAttrPermitted(propertyMetaClass, mpp, EntityAttrAccess.VIEW);
    }

    protected boolean isEntityAttrPermitted(MetaClass metaClass, MetaPropertyPath propertyPath, EntityAttrAccess access) {
        MetaClass originalMetaClass = extendedEntities.getOriginalMetaClass(metaClass);
        if (originalMetaClass != null) {
            metaClass = originalMetaClass;
        }

        return userSessionSource.getUserSession()
                .isEntityAttrPermitted(metaClass, propertyPath.getMetaProperty().getName(), access);
    }

    protected boolean isEntityAttrUpdatePermitted(MetaPropertyPath mpp) {
        MetaClass propertyMetaClass = metadata.getTools().getPropertyEnclosingMetaClass(mpp);

        if (metadata.getTools().isEmbeddable(propertyMetaClass)) {
            return isEntityOpPermitted(propertyMetaClass, EntityOp.UPDATE)
                    && isEntityAttrPermitted(propertyMetaClass, mpp, EntityAttrAccess.MODIFY);
        }

        return (isEntityOpPermitted(propertyMetaClass, EntityOp.CREATE)
                || isEntityOpPermitted(propertyMetaClass, EntityOp.UPDATE))
                && isEntityAttrPermitted(propertyMetaClass, mpp, EntityAttrAccess.MODIFY);
    }
}