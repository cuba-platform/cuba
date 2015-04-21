/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(PersistenceManagerService.NAME)
public class PersistenceManagerServiceBean implements PersistenceManagerService {

    @Inject
    private PersistenceManagerAPI pm;

    @Override
    public boolean useLazyCollection(String entityName) {
        return pm.useLazyCollection(entityName);
    }

    @Override
    public boolean useLookupScreen(String entityName) {
        return pm.useLookupScreen(entityName);
    }

    @Override
    public int getFetchUI(String entityName) {
        return pm.getFetchUI(entityName);
    }

    @Override
    public int getMaxFetchUI(String entityName) {
        return pm.getMaxFetchUI(entityName);
    }

    @Override
    public String getDbmsType() {
        return DbmsType.getType();
    }

    @Override
    public String getDbmsVersion() {
        return DbmsType.getVersion();
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return DbmsSpecificFactory.getDbmsFeatures().getUniqueConstraintViolationPattern();
    }

    @Override
    public boolean isNullsLastSorting() {
        return DbmsSpecificFactory.getDbmsFeatures().isNullsLastSorting();
    }
}