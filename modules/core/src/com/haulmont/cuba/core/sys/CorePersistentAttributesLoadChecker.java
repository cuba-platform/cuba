/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.GlobalPersistentAttributesLoadChecker;

import javax.inject.Inject;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class CorePersistentAttributesLoadChecker extends GlobalPersistentAttributesLoadChecker {
    @Inject
    protected Persistence persistence;

    @Override
    public boolean isLoaded(Object entity, String property) {
        Boolean baseIsLoaded = isLoadedCommon(entity, property);
        if (baseIsLoaded != null) {
            return baseIsLoaded;
        }
        return ((PersistenceImpl) persistence).getJpaEmf().getPersistenceUnitUtil().isLoaded(entity, property);
    }
}
