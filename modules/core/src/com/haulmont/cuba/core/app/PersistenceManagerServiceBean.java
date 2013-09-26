/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

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

    public boolean useLazyCollection(String entityName) {
        return pm.useLazyCollection(entityName);
    }

    public boolean useLookupScreen(String entityName) {
        return pm.useLookupScreen(entityName);
    }

    public int getFetchUI(String entityName) {
        return pm.getFetchUI(entityName);
    }

    public int getMaxFetchUI(String entityName) {
        return pm.getMaxFetchUI(entityName);
    }
}
