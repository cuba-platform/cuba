/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.GlobalPersistentAttributesLoadChecker;
import com.haulmont.cuba.core.global.MetadataTools;

import javax.inject.Inject;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class CorePersistentAttributesLoadChecker extends GlobalPersistentAttributesLoadChecker {
    @Inject
    protected Persistence persistence;

    @Inject
    protected MetadataTools metadataTools;

    @Override
    protected boolean isLoadedSpecificCheck(Object entity, String property, MetaClass metaClass, MetaProperty metaProperty) {
        if (metadataTools.isEmbeddable(metaClass)) {
            //TODO eude:
            // this is workaround for unexpected EclipseLink behaviour when PersistenceUnitUtil.isLoaded
            // throws exception if embedded entity refers to persistent entity
            return checkIsLoadedWithGetter(entity, property);
        }
        return ((PersistenceImpl) persistence).getJpaEmf().getPersistenceUnitUtil().isLoaded(entity, property);
    }
}
